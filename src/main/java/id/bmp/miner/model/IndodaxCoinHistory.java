package id.bmp.miner.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IndodaxCoinHistory {

    private List<IndodaxCoinCandle> candleData;

    public IndodaxCoinHistory() {
    }

    public List<IndodaxCoinCandle> getCandleData() {
        return candleData;
    }

    public void setCandleData(List<IndodaxCoinCandle> candleData) {
        this.candleData = candleData;
    }
}
