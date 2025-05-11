package id.bmp.miner;

import com.fasterxml.jackson.core.type.TypeReference;
import id.bmp.miner.model.IndodaxCoinCandle;
import id.bmp.miner.model.IndodaxMarket;
import id.bmp.miner.model.IndodaxMarketTicker;
import id.bmp.miner.util.http.HTTPClient;
import id.bmp.miner.util.http.model.HTTPContentType;
import id.bmp.miner.util.http.model.HTTPRequest;
import id.bmp.miner.util.http.model.HTTPResponse;
import id.bmp.miner.util.json.JsonUtil;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class IndodaxScanConsole {

    private static double balance = 100_000;
    private static double holdings = 0;
    private static Double buyPrice = null;


    public static void main(String [] args) {

        String url = "https://indodax.com/api/summaries";

        HTTPRequest request = new HTTPRequest.Builder(url).setContentType(HTTPContentType.APPLICATION_JSON).build();

        HTTPResponse httpResponse = HTTPClient.get(request);
        IndodaxMarket markets = JsonUtil.fromJson(httpResponse.getBody(), IndodaxMarket.class);

        if (markets != null) {
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

            List<IndodaxMarketTicker> topScalpingCoins = findTopScalpingCoins(tickers);

            for (IndodaxMarketTicker ticker : topScalpingCoins) {
                //simulateBuyAndSell(ticker, 300000, ticker.getSell());

                String symbol = ticker.getAlias().toLowerCase(Locale.ROOT).replace("_", "");
                int timeFrame = 5;
                int limit = 100;
                List<Double> closes = fetchClosePrices(symbol, timeFrame, limit);

                double emaFast = calculateEMA(closes, 9);
                double emaSlow = calculateEMA(closes, 21);
                double rsi = calculateRSI(closes, 14);
                double price = closes.get(closes.size() - 1);

                System.out.printf("📊 COIN: %s | Price: %.0f | EMA9: %.2f | EMA21: %.2f | RSI: %.2f\n",ticker.getAlias(), price, emaFast, emaSlow, rsi);

                if (buyPrice == null) {
                    if (rsi >= 50 && rsi <= 70 && emaFast > emaSlow) {
                        buyPrice = price;
                        holdings = (balance * (1 - 0.003)) / price;
                        System.out.printf("🟢 BUY @ %.0f | Holdings: %.4f\n", price, holdings);
                    } else {
                        System.out.println("⏭️ Sinyal belum valid untuk BUY.");
                    }
                } else {
                    double changePct = (price - buyPrice) / buyPrice * 100;
                    double portfolioNow = holdings * price * (1 - 0.003);
                    double profit = portfolioNow - balance;

                    System.out.printf("📈 Change: %.2f%% | P/L: %.0f\n", changePct, profit);

                    if (changePct >= 0.7) {
                        System.out.printf("✅ TAKE PROFIT! Sell @ %.0f\n", price);
                        buyPrice = null;
                        holdings = 0;
                    } else if (changePct <= -0.5) {
                        System.out.printf("🛑 STOP LOSS! Sell @ %.0f\n", price);
                        buyPrice = null;
                        holdings = 0;
                    }
                }

            }
        }
    }

    private static List<IndodaxMarketTicker> findTopScalpingCoins(List<IndodaxMarketTicker> allTickers) {
        return allTickers.stream()
                .filter(t -> t.getVolIdr() > 5_000_000_000L) // minimal 5M volume
                .filter(t -> t.getHigh() / t.getLow() > 1.03) // ada range naik turun
                .filter(t -> t.getPosisiHarga() < 0.3) // masih dekat harga bawah
                .sorted(Comparator.comparingDouble(IndodaxMarketTicker::getVolIdr).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public static List<Double> fetchClosePrices(String symbol, int tfMinutes, int limit) {

        List<Double> closes = new ArrayList<>();

        long now = Instant.now().getEpochSecond();
        long from = now - tfMinutes * 60L * limit;
        String url = String.format("https://indodax.com/tradingview/history_v2?symbol=%s&tf=%d&from=%d&to=%d",
                symbol, tfMinutes, from, now);

        try {

            HTTPRequest request = new HTTPRequest.Builder(url).setContentType(HTTPContentType.APPLICATION_JSON).build();
            HTTPResponse httpResponse = HTTPClient.get(request);

            if(httpResponse.getCode() == 200){

                List<IndodaxCoinCandle> candles = JsonUtil.fromJson(httpResponse.getBody(), new TypeReference<List<IndodaxCoinCandle>>() {});

                for (int i = 0; i < candles.size(); i++) {
                    IndodaxCoinCandle candleData = candles.get(i);
                    closes.add(candleData.getClose());
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch candles: " + e.getMessage());
            return Collections.emptyList();
        }
        return closes;
    }


    public static double calculateEMA(List<Double> prices, int period) {
        double multiplier = 2.0 / (period + 1);
        double ema = prices.get(0); // start with first value
        for (int i = 1; i < prices.size(); i++) {
            ema = (prices.get(i) - ema) * multiplier + ema;
        }
        return ema;
    }

    public static double calculateRSI(List<Double> prices, int period) {
        if (prices.size() <= period) return 0;

        double gain = 0, loss = 0;
        for (int i = 1; i <= period; i++) {
            double change = prices.get(i) - prices.get(i - 1);
            if (change > 0) gain += change;
            else loss -= change;
        }

        gain /= period;
        loss /= period;

        for (int i = period + 1; i < prices.size(); i++) {
            double change = prices.get(i) - prices.get(i - 1);
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
