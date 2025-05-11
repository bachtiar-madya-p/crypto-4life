package id.bmp.miner.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IndodaxCoinCandle {

    @JsonProperty("Time")
    private long time;

    @JsonProperty("Open")
    private double open;

    @JsonProperty("High")
    private double high;

    @JsonProperty("Low")
    private double low;

    @JsonProperty("Close")
    private double close;

    @JsonProperty("Volume")
    private String volume; // Bisa diganti double kalau kamu ingin parsing otomatis

    public IndodaxCoinCandle() {
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
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

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
}
