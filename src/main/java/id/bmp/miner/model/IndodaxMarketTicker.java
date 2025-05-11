package id.bmp.miner.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class IndodaxMarketTicker {

    public double high;
    public double low;
    public double last;
    public double buy;
    public double sell;
    public double volCoin;
    public double volIdr;
    public long server_time;
    public String name;
    public String alias;

    public IndodaxMarketTicker() {
    }

    @JsonAnySetter
    private Map<String, Object> otherFields = new HashMap<>();

    public Map<String, Object> getOtherFields() {
        return otherFields;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    public double getBuy() {
        return buy;
    }

    public void setBuy(double buy) {
        this.buy = buy;
    }

    public double getSell() {
        return sell;
    }

    public void setSell(double sell) {
        this.sell = sell;
    }

    public double getVolCoin() {
        return volCoin;
    }

    public void setVolCoin(double volCoin) {
        this.volCoin = volCoin;
    }

    public double getVolIdr() {
        return volIdr;
    }

    public void setVolIdr(double volIdr) {
        this.volIdr = volIdr;
    }

    public long getServer_time() {
        return server_time;
    }

    public void setServer_time(long server_time) {
        this.server_time = server_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOtherFields(Map<String, Object> otherFields) {
        this.otherFields = otherFields;
    }

    public double getPosisiHarga() {
        return (last - low) / (high - low + 0.01); // +0.01 untuk hindari div 0
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
