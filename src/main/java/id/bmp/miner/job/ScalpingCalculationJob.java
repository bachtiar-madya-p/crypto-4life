package id.bmp.miner.job;

import id.bmp.miner.controller.TelegramController;
import id.bmp.miner.model.*;
import id.bmp.miner.repository.ScalpingSignalHistoryRepository;
import id.bmp.miner.repository.ScalpingSignalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * ScalpingCalculationJob is responsible for evaluating open scalping positions
 * and executing sell logic based on current market performance.
 *
 * It uses:
 * - Take Profit if gain ≥ 0.7%
 * - Stop Loss if loss ≥ 0.5% or -0.2% if flagged as pump
 * - Sends messages to Telegram on every action
 */
@Component
public class ScalpingCalculationJob extends BaseJob {

    @Autowired
    private TelegramController telegramController;

    @Autowired
    private ScalpingSignalRepository scalpingSignalRepository;

    @Autowired
    private ScalpingSignalHistoryRepository scalpingSignalHistoryRepository;

    public ScalpingCalculationJob() {
        log = getLogger(this.getClass());
    }

    public void run() {
        String methodName = "run";
        start(methodName);
        log.debug(methodName, "Running scan...");

        List<ScalpingSignal> openPositionMarkets = scalpingSignalRepository.findScalpingInOpenPosition();

        // Evaluate each open position
        if (!openPositionMarkets.isEmpty()) {
            for (ScalpingSignal signal : openPositionMarkets) {
                log.debug(methodName, "[SCALPING] Open position market: " + signal.getMarket());

                List<IndodaxCoinCandle> candles = fetchCandles(signal.getMarket(), 5, 100);
                if (candles == null || candles.size() < 21) continue;

                CandleAnalysisResult analysis = analyzeCandles(candles);

                boolean strongUptrend = analysis.getEmaFast() > analysis.getEmaSlow()
                        && (analysis.getEmaFast() - analysis.getEmaSlow()) / analysis.getEmaSlow() > 0.01;
                boolean notOverbought = analysis.getRsi() < 70;
                double changePct = (analysis.getPrice() - signal.getBuyPrice()) / signal.getBuyPrice() * 100;
                double portfolioNow = signal.getBuyAmount() * analysis.getPrice() * (1 - 0.003);
                double profit = portfolioNow - signal.getInitialCapital();

                log.debug(methodName, String.format("[SCALPING] 📈 [%s] Change: %.2f%% | P/L: %.0f", signal.getMarket(), changePct, profit));

                if (changePct >= 0.7) {
                    if (strongUptrend && notOverbought) {
                        String message = "\n[SCALPING] 📈 Market (" + signal.getMarket() + ") still trending UP, HOLD position.\n\n";
                        log.debug(methodName, message);
                        telegramController.sendMessage(message);
                        continue;
                    }
                    String message = "\n\n[SCALPING] 📈 Market (" + signal.getMarket() + ") still trending UP, TAKE PROFIT("+profit+")! Sell @ " + analysis.getPrice()+"\n\n";
                    log.debug(methodName, message);
                    telegramController.sendMessage(message);

                    signal.setSellPrice(analysis.getPrice());
                    signal.setProfit(profit);
                    signal.setSignalType("SELL");
                    signal.setOpenPosition(false);
                    scalpingSignalRepository.deleteScalpingSignal(signal.getId());
                    scalpingSignalHistoryRepository.insertScalpingSignal(signal);
                } else if (changePct <= -0.5 || (signal.isPompom() && changePct <= -0.2)) {
                    String message = "\n[SCALPING] 📉 Market (" + signal.getMarket() + ") still trending DOWN, STOP LOSS("+profit+")! Sell @ " + analysis.getPrice()+"\n\n";
                    log.debug(methodName, message);
                    telegramController.sendMessage(message);

                    signal.setSellPrice(analysis.getPrice());
                    signal.setProfit(profit);
                    signal.setSignalType("SELL");
                    signal.setOpenPosition(false);
                    scalpingSignalRepository.deleteScalpingSignal(signal.getId());
                    scalpingSignalRepository.insertScalpingSignal(signal);
                }
            }
        }

        completed(methodName);
    }
}