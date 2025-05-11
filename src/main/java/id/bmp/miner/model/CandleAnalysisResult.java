package id.bmp.miner.model;

public class CandleAnalysisResult {

    double price;
    double emaFast;
    double emaSlow;
    double rsi;
    boolean isBodyStrong;
    boolean isVolumeSpike;

    public CandleAnalysisResult(double price, double emaFast, double emaSlow, double rsi, boolean isBodyStrong, boolean isVolumeSpike) {
        this.price = price;
        this.emaFast = emaFast;
        this.emaSlow = emaSlow;
        this.rsi = rsi;
        this.isBodyStrong = isBodyStrong;
        this.isVolumeSpike = isVolumeSpike;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getEmaFast() {
        return emaFast;
    }

    public void setEmaFast(double emaFast) {
        this.emaFast = emaFast;
    }

    public double getEmaSlow() {
        return emaSlow;
    }

    public void setEmaSlow(double emaSlow) {
        this.emaSlow = emaSlow;
    }

    public double getRsi() {
        return rsi;
    }

    public void setRsi(double rsi) {
        this.rsi = rsi;
    }

    public boolean isBodyStrong() {
        return isBodyStrong;
    }

    public void setBodyStrong(boolean bodyStrong) {
        isBodyStrong = bodyStrong;
    }

    public boolean isVolumeSpike() {
        return isVolumeSpike;
    }

    public void setVolumeSpike(boolean volumeSpike) {
        isVolumeSpike = volumeSpike;
    }
}
