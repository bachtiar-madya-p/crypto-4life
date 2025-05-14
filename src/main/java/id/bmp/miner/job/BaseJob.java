package id.bmp.miner.job;

import id.bmp.miner.controller.IndodaxController;
import id.bmp.miner.manager.PropertyManager;
import id.bmp.miner.model.CandleAnalysisResult;
import id.bmp.miner.model.IndodaxCoinCandle;
import id.bmp.miner.model.IndodaxMarket;
import id.bmp.miner.model.IndodaxMarketTicker;
import id.bmp.miner.util.log.AppLogger;
import id.bmp.miner.util.property.Property;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class BaseJob {

    protected AppLogger log;

    @Autowired
    private IndodaxController indodaxController;

    public BaseJob() {
        // Empty Constructor
    }

    protected AppLogger getLogger(Class<?> clazz) {
        return new AppLogger(clazz);
    }

    protected void start(String methodName) {
        log.debug(methodName, "Start");
    }

    protected void completed(String methodName) {
        log.debug(methodName, "Completed");
    }

    protected String getProperty(String key) {
        return PropertyManager.getInstance().getProperty(key);
    }

    protected int getIntProperty(String key) {
        return PropertyManager.getInstance().getIntProperty(key);
    }

    protected boolean getBoolProperty(String key) {
        return PropertyManager.getInstance().getBooleanProperty(key);
    }



    /**
     * Analyze the latest candle indicators and return calculated metrics
     */
    protected CandleAnalysisResult analyzeCandles(List<IndodaxCoinCandle> candles) {
        if (candles == null || candles.size() < 22) {
            throw new IllegalArgumentException(" 22 Candle is required for analysis");
        }

        IndodaxCoinCandle last = candles.get(candles.size() - 1);
        double open = last.getOpen();
        double close = last.getClose();
        double high = last.getHigh();
        double low = last.getLow();

        // Rasio body candle to range
        double body = Math.abs(close - open);
        double range = high - low;
        double bodyRatio = (range == 0) ? 0 : body / range;

        // Volume spike check
        double lastVolume = Double.parseDouble(last.getVolume());
        double avgVolume = candles.subList(candles.size() - 21, candles.size() - 1)
                .stream()
                .mapToDouble(c -> Double.parseDouble(c.getVolume()))
                .average().orElse(0);

        boolean isBodyStrong = bodyRatio >= 0.5;
        boolean isVolumeSpike = lastVolume >= avgVolume;

        // EMA & RSI calculation
        double emaFast = calculateEMA(candles, getIntProperty(Property.CANDLES_EMA_FAST));
        double emaSlow =calculateEMA(candles, getIntProperty(Property.CANDLES_EMA_SLOW));
        double rsi = calculateRSI(candles, getIntProperty(Property.CANDLES_RSI_PERIOD));
        double price = close;

        return new CandleAnalysisResult(price, emaFast, emaSlow, rsi, isBodyStrong, isVolumeSpike);
    }


    /**
     * Extracts and normalizes ticker data from Indodax market response
     */
    protected List<IndodaxMarketTicker> flattenMarketTickers(IndodaxMarket markets) {
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

    protected String formatRupiah(double value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat formatter = new DecimalFormat("###,##0.000000", symbols);
        return "Rp" + formatter.format(value);
    }

    protected String format2Decimal(double value) {
        return String.format("%.2f", value);
    }


    /**
     * Filters and selects top scalping candidates based on:
     * - High volume
     * - Price volatility
     * - Price near daily low
     */
    protected List<IndodaxMarketTicker> findTopScalpingCoins(List<IndodaxMarketTicker> allTickers) {
        return allTickers.stream()
                .filter(t -> t.getVolIdr() > 5_000_000_000L)
                .filter(t -> t.getHigh() / t.getLow() > 1.03)
                .filter(t -> t.getPosisiHarga() < 0.3)
                .sorted(Comparator.comparingDouble(IndodaxMarketTicker::getVolIdr).reversed())
                .limit(20)
                .collect(Collectors.toList());
    }

    /**
     * Fetch historical candles from Indodax for a given market, timeframe and count
     */
    protected List<IndodaxCoinCandle> fetchCandles(String market, int tfMinutes, int limit) {
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
    protected static double calculateEMA(List<IndodaxCoinCandle> candles, int period) {
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
    protected static double calculateRSI(List<IndodaxCoinCandle> candles, int period) {
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
