package id.bmp.miner.job;

import id.bmp.miner.controller.IndodaxController;
import id.bmp.miner.controller.TelegramController;
import id.bmp.miner.model.*;
import id.bmp.miner.repository.ScalpingSignalHistoryRepository;
import id.bmp.miner.repository.ScalpingSignalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

/**
 * ScalpingScannerJob is a background job that periodically scans the Indodax market
 * to identify potential scalping opportunities based on technical indicators:
 * - EMA crossover (EMA9 > EMA21)
 * - RSI between 50–70
 * - Strong bullish candle body
 * - Volume spike compared to recent average
 * - Early pump detection (2–5%) allowed
 * - Hard pump (≥5%) skipped to avoid FOMO traps
 */
@Component
public class ScalpingScannerJob extends BaseJob {

    @Autowired
    private TelegramController telegramController;

    @Autowired
    private IndodaxController indodaxController;

    @Autowired
    private ScalpingSignalRepository scalpingSignalRepository;
    @Autowired
    private ScalpingSignalHistoryRepository scalpingSignalHistoryRepository;

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

        /**
         * TODO :
         * 1. to add balance check before do buy
         */
        List<ScalpingSignal> openPositionMarkets = scalpingSignalRepository.findScalpingInOpenPosition();
        Set<String> openMarkets = new HashSet<>();
        for (ScalpingSignal signal : openPositionMarkets) {
            openMarkets.add(signal.getMarket());
        }

        IndodaxMarket markets = indodaxController.getMarketSummary();

        if (markets != null) {
            List<IndodaxMarketTicker> tickers = flattenMarketTickers(markets);
            List<IndodaxMarketTicker> topScalpingCoins = findTopScalpingCoins(tickers);

            for (IndodaxMarketTicker ticker : topScalpingCoins) {

                if (openMarkets.contains(ticker.getAlias())) {
                    log.debug(methodName, "🔁 Skipping ("+ticker.getAlias()+"), already open position");
                    continue;
                }

                List<IndodaxCoinCandle> candles = fetchCandles(ticker.getAlias(), 15, 100);
                if (candles == null || candles.size() < 21) continue;

                CandleAnalysisResult analysis = analyzeCandles(candles);

                log.debug(methodName,"📊 Market: " + ticker.getAlias() + " | Price: " + formatRupiah(analysis.getPrice()) +" | EMA9: " + formatRupiah(analysis.getEmaFast()) +
                                " | EMA21: " + formatRupiah(analysis.getEmaSlow()) + " | RSI: "+format2Decimal(analysis.getRsi())+" | StrongBody: " + analysis.isBodyStrong() + " | VolSpike: " + analysis.isVolumeSpike());

                boolean isPump = false;
                boolean earlyPump = false;
                int window = 3;
                if (candles.size() > window) {
                    double previousAvg = candles.subList(candles.size() - 1 - window, candles.size() - 1)
                            .stream().mapToDouble(IndodaxCoinCandle::getClose).average().orElse(0);
                    double currentPrice = candles.get(candles.size() - 1).getClose();
                    double pumpRatio = (currentPrice - previousAvg) / previousAvg;
                    if (pumpRatio >= 0.05) {
                        isPump = true;
                    } else if (pumpRatio >= 0.02) {
                        earlyPump = true;
                    }
                }

                if (isPump) {
                    String message = "⚠️ Hard pump detected (≥5%): " + ticker.getAlias() + " | Skipped due to price pump ≥ 5%%";
                    log.debug(methodName, message);
                    telegramController.sendMessage("[SCALPING] " +message);
                    continue;
                } else if (earlyPump) {
                    String message = "🔍 Early pump detected " + ticker.getAlias() + " (2–5%) : ";
                    log.debug(methodName, message);
                    telegramController.sendMessage("[SCALPING] " +message);
                }

                if (analysis.getRsi() >= 50 && analysis.getRsi() <= 70 && analysis.getEmaFast() > analysis.getEmaSlow()
                        && analysis.isBodyStrong() && analysis.isVolumeSpike()) {

                    double buyAmount = (balance * (1 - 0.003)) / analysis.getPrice();

                    ScalpingSignal signal = new ScalpingSignal();
                    signal.setMarket(ticker.getAlias());
                    signal.setTransactionId(UUID.randomUUID().toString());
                    signal.setBuyPrice(analysis.getPrice());
                    signal.setBuyAmount(buyAmount);
                    signal.setInitialCapital(balance);
                    signal.setOpenPosition(true);
                    signal.setSignalTime(Instant.now().getEpochSecond());
                    signal.setEmaFast(analysis.getEmaFast());
                    signal.setEmaSlow(analysis.getEmaSlow());
                    signal.setRsi(analysis.getRsi());
                    signal.setBodyRatio(analysis.isBodyStrong() ? 1.0 : 0.0);
                    signal.setVolume(analysis.isVolumeSpike() ? 1.0 : 0.0);
                    signal.setAvgVolume(1.0);
                    signal.setBodyStrong(analysis.isBodyStrong());
                    signal.setVolumeSpike(analysis.isVolumeSpike());
                    signal.setSignalType("BUY");
                    signal.setExecuted(false);
                    signal.setPompom(isPump || earlyPump);

                    scalpingSignalRepository.insertScalpingSignal(signal);
                    scalpingSignalHistoryRepository.insertScalpingSignal(signal);
                    String message = "🟢 BUY Signal (" + ticker.getAlias() + ") Saved @ " + formatRupiah(analysis.getPrice());
                    log.debug(methodName, message);
                    telegramController.sendMessage("[SCALPING] " +message);
                } else {
                    log.debug(methodName, "⏭️ Signal not valid for BUY yet.");
                }
            }
        }
        completed(methodName);
    }
}