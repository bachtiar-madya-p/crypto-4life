package id.bmp.miner.model;

import java.util.Map;

public class IndodaxMarket {

    private Map<String, IndodaxMarketTicker> tickers;
    private Map<String, String> prices24h;
    private Map<String, String> prices7d;

    public IndodaxMarket() {
    }

    public Map<String, IndodaxMarketTicker> getTickers() {
        return tickers;
    }

    public void setTickers(Map<String, IndodaxMarketTicker> tickers) {
        this.tickers = tickers;
    }

    public Map<String, String> getPrices24h() {
        return prices24h;
    }

    public void setPrices24h(Map<String, String> prices24h) {
        this.prices24h = prices24h;
    }

    public Map<String, String> getPrices7d() {
        return prices7d;
    }

    public void setPrices7d(Map<String, String> prices7d) {
        this.prices7d = prices7d;
    }
}
