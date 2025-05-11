package id.bmp.miner.job;

import id.bmp.miner.controller.IndodaxController;
import id.bmp.miner.model.*;
import id.bmp.miner.repository.ScalpingSignalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ScalpingScannerJob is a background job that periodically scans the Indodax market
 * to identify potential scalping opportunities based on technical indicators:
 * - EMA crossover (EMA9 > EMA21)
 * - RSI between 50–70
 * - Strong bullish candle body
 * - Volume spike compared to recent average
 *
 * If these conditions are met and no open position exists, it simulates a buy.
 * It also evaluates existing positions and sells if profit or loss targets are met.
 */
@Component
public class ScalpingScannerJob extends BaseJob {

    @Autowired
    private IndodaxController indodaxController;

    @Autowired
    private ScalpingSignalRepository scalpingSignalRepository;

    private double balance = 100_000; // Simulated capital in IDR

    public ScalpingScannerJob() {
        log = getLogger(this.getClass());
    }

    /**
     * Main execution method that runs the scalping logic:
     * - If there is an open position in the DB, evaluate sell condition.
     * - If no open position, scan and evaluate coins for buy signal.
     */
    public void run() {
        String methodName = "run";
        start(methodName);
        log.debug(methodName, "Running scan...");

        List<ScalpingSignal> openPositionMarkets = scalpingSignalRepository.findScalpingInOpenPosition();

        // Check and handle open positions
        if (!openPositionMarkets.isEmpty()) {
            for (ScalpingSignal signal : openPositionMarkets) {
                log.debug(methodName, "Open position market: " + signal.getMarket());

                List<IndodaxCoinCandle> candles = fetchCandles(signal.getMarket(), 5, 100);
                if (candles == null || candles.size() < 21) continue;

                CandleAnalysisResult analysis = analyzeCandles(candles);

                // Calculate profit or loss percentage
                boolean strongUptrend = analysis.getEmaFast() > analysis.getEmaSlow() && (analysis.getEmaFast() - analysis.getEmaSlow()) / analysis.getEmaSlow() > 0.01;
                boolean notOverbought = analysis.getRsi() < 70;
                double changePct = (analysis.getPrice() - signal.getBuyPrice()) / signal.getBuyPrice() * 100;
                double portfolioNow = signal.getBuyAmount() * analysis.getPrice() * (1 - 0.003);
                double profit = portfolioNow - signal.getInitialCapital();

                log.debug(methodName, String.format("📈 [%s] Change: %.2f%% | P/L: %.0f", signal.getMarket(), changePct, profit));

                // Sell conditions: take profit or stop loss
                if (changePct >= 0.7) {
                    if (strongUptrend && notOverbought) {
                        log.debug(methodName, "📈 Market ("+signal.getMarket()+") still trending UP, HOLD position.");
                        continue;
                    }
                    log.debug(methodName, "✅ Market ("+signal.getMarket()+") TAKE PROFIT! Sell @ " + analysis.getPrice());
                    signal.setSellPrice(analysis.getPrice());
                    signal.setProfit(profit);
                    signal.setSignalType("SELL");
                    signal.setOpenPosition(false);
                    scalpingSignalRepository.insertScalpingSignal(signal);
                } else if (changePct <= -0.5) {
                    log.debug(methodName, "🛑 Market ("+signal.getMarket()+") STOP LOSS! Sell @ " + analysis.getPrice());
                    signal.setSellPrice(analysis.getPrice());
                    signal.setProfit(profit);
                    signal.setSignalType("SKIP");
                    signal.setOpenPosition(false);
                    scalpingSignalRepository.insertScalpingSignal(signal);
                }
            }
        } else {
            // No open position: Scan for new buy opportunities
            IndodaxMarket markets = indodaxController.getMarketSummary();

            if (markets != null) {
                List<IndodaxMarketTicker> tickers = flattenMarketTickers(markets);
                List<IndodaxMarketTicker> topScalpingCoins = findTopScalpingCoins(tickers);

                for (IndodaxMarketTicker ticker : topScalpingCoins) {
                    List<IndodaxCoinCandle> candles = fetchCandles(ticker.getAlias(), 5, 100);
                    if (candles == null || candles.size() < 21) continue;

                    CandleAnalysisResult analysis = analyzeCandles(candles);

                    log.debug(methodName, "📊 Market: " + ticker.getAlias() + " | Price: " + analysis.getPrice() +
                            " | EMA9: " + analysis.getEmaFast() + " | EMA21: " + analysis.getEmaSlow() + " | RSI: " + analysis.getRsi());

                    // Check buy conditions
                    if (analysis.getRsi() >= 50 && analysis.getRsi() <= 70 && analysis.getEmaFast() > analysis.getEmaSlow() && analysis.isBodyStrong() && analysis.isVolumeSpike()) {
                        double buyAmount = (balance * (1 - 0.003)) / analysis.getPrice();

                        // Save BUY signal to DB
                        ScalpingSignal signal = new ScalpingSignal();
                        signal.setMarket(ticker.getAlias());
                        signal.setBuyPrice(analysis.getPrice());
                        signal.setBuyAmount(buyAmount);
                        signal.setInitialCapital(balance);
                        signal.setOpenPosition(true);
                        signal.setSignalTime(Instant.now().getEpochSecond());
                        signal.setEmaFast(analysis.getEmaFast());
                        signal.setEmaSlow(analysis.getEmaSlow());
                        signal.setRsi(analysis.getRsi());
                        signal.setBodyRatio(analysis.isBodyStrong() ? 1.0 : 0.0); // optional: set actual ratio
                        signal.setVolume(analysis.isVolumeSpike() ? 1.0 : 0.0);   // optional: set actual volume
                        signal.setAvgVolume(1.0); // optional: set actual avg
                        signal.setBodyStrong(analysis.isBodyStrong());
                        signal.setVolumeSpike(analysis.isVolumeSpike());
                        signal.setSignalType("BUY");
                        signal.setExecuted(false);

                        scalpingSignalRepository.insertScalpingSignal(signal);
                        log.debug(methodName, "🟢 BUY Signal Saved @ " + analysis.getPrice());
                    } else {
                        log.debug(methodName, "⏭️ Signal not valid for BUY yet.");
                    }
                }
            }
        }
        completed(methodName);
    }

    /**
     * Analyze the latest candle indicators and return calculated metrics
     */
    private CandleAnalysisResult analyzeCandles(List<IndodaxCoinCandle> candles) {
        IndodaxCoinCandle last = candles.get(candles.size() - 1);
        double body = Math.abs(last.getClose() - last.getOpen());
        double range = last.getHigh() - last.getLow();
        double bodyRatio = range == 0 ? 0 : body / range;

        double lastVolume = Double.parseDouble(last.getVolume());
        double avgVolume = candles.subList(candles.size() - 21, candles.size() - 1)
                .stream()
                .mapToDouble(c -> Double.parseDouble(c.getVolume()))
                .average().orElse(0);

        boolean isBodyStrong = bodyRatio >= 0.5;
        boolean isVolumeSpike = lastVolume >= avgVolume;

        double emaFast = calculateEMA(candles, 9);
        double emaSlow = calculateEMA(candles, 21);
        double rsi = calculateRSI(candles, 14);
        double price = last.getClose();

        return new CandleAnalysisResult(price, emaFast, emaSlow, rsi, isBodyStrong, isVolumeSpike);
    }

    /**
     * Extracts and normalizes ticker data from Indodax market response
     */
    private List<IndodaxMarketTicker> flattenMarketTickers(IndodaxMarket markets) {
        List<IndodaxMarketTicker> tickers = new ArrayList<>();

        for (Map.Entry<String, IndodaxMarketTicker> entry : markets.getTickers().entrySet()) {
            String marketName = entry.getKey();
            IndodaxMarketTicker ticker = entry.getValue();
            Map<String, Object> otherField = ticker.getOtherFields();

            for (Map.Entry<String, Object> volumes : otherField.entrySet()) {
                String key = volumes.getKey();
                if (key.contains("vol_idr") && !key.contains("vol_usd")) {
                    ticker.setVolIdr(Double.parseDouble(volumes.getValue().toString()));
                } else if (key.contains("vol") && !key.contains("vol_usd")) {
                    ticker.setVolCoin(Double.parseDouble(volumes.getValue().toString()));
                }
                ticker.setAlias(marketName.toUpperCase(Locale.ROOT));
            }
            tickers.add(ticker);
        }
        return tickers;
    }

    /**
     * Filters and selects top scalping candidates based on:
     * - High volume
     * - Price volatility
     * - Price near daily low
     */
    private List<IndodaxMarketTicker> findTopScalpingCoins(List<IndodaxMarketTicker> allTickers) {
        return allTickers.stream()
                .filter(t -> t.getVolIdr() > 5_000_000_000L)
                .filter(t -> t.getHigh() / t.getLow() > 1.03)
                .filter(t -> t.getPosisiHarga() < 0.3)
                .sorted(Comparator.comparingDouble(IndodaxMarketTicker::getVolIdr).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * Fetch historical candles from Indodax for a given market, timeframe and count
     */
    public List<IndodaxCoinCandle> fetchCandles(String market, int tfMinutes, int limit) {
        long now = Instant.now().getEpochSecond();
        long from = now - tfMinutes * 60L * limit;

        List<IndodaxCoinCandle> candles = indodaxController.getCandle(market, tfMinutes, from, now);
        if (candles == null || candles.size() < limit) {
            log.debug("Failed to fetch candles");
            return Collections.emptyList();
        }
        return candles;
    }

    /**
     * Exponential Moving Average calculation for given candles and period
     */
    public static double calculateEMA(List<IndodaxCoinCandle> candles, int period) {
        if (candles == null || candles.size() < period) return 0;

        List<Double> closes = candles.stream().map(IndodaxCoinCandle::getClose).collect(Collectors.toList());
        double multiplier = 2.0 / (period + 1);
        double ema = closes.get(0);

        for (int i = 1; i < closes.size(); i++) {
            ema = (closes.get(i) - ema) * multiplier + ema;
        }
        return ema;
    }

    /**
     * Relative Strength Index (RSI) calculation
     */
    public static double calculateRSI(List<IndodaxCoinCandle> candles, int period) {
        if (candles == null || candles.size() <= period) return 0;

        List<Double> closes = candles.stream().map(IndodaxCoinCandle::getClose).collect(Collectors.toList());

        double gain = 0, loss = 0;
        for (int i = 1; i <= period; i++) {
            double change = closes.get(i) - closes.get(i - 1);
            if (change > 0) gain += change;
            else loss -= change;
        }

        gain /= period;
        loss /= period;

        for (int i = period + 1; i < closes.size(); i++) {
            double change = closes.get(i) - closes.get(i - 1);
            if (change > 0) {
                gain = (gain * (period - 1) + change) / period;
                loss = (loss * (period - 1)) / period;
            } else {
                gain = (gain * (period - 1)) / period;
                loss = (loss * (period - 1) - change) / period;
            }
        }

        double rs = gain / (loss == 0 ? 1 : loss);
        return 100 - (100 / (1 + rs));
    }
}