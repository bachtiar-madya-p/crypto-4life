package id.bmp.miner.model;

public class ScalpingSignal {

    private int id;
    private String market;
    private String transactionId;
    private long signalTime;
    private double buyPrice;
    private double buyAmount;
    private double initialCapital;
    private Double sellPrice;
    private Double profit;
    private double emaFast;
    private double emaSlow;
    private double rsi;
    private double bodyRatio;
    private double volume;
    private double avgVolume;
    private boolean bodyStrong;
    private boolean volumeSpike;
    private String signalType;
    private boolean executed;
    private boolean openPosition;
    private boolean pompom;

    public ScalpingSignal() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public long getSignalTime() {
        return signalTime;
    }

    public void setSignalTime(long signalTime) {
        this.signalTime = signalTime;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(double buyAmount) {
        this.buyAmount = buyAmount;
    }

    public double getInitialCapital() {
        return initialCapital;
    }

    public void setInitialCapital(double initialCapital) {
        this.initialCapital = initialCapital;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
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

    public double getBodyRatio() {
        return bodyRatio;
    }

    public void setBodyRatio(double bodyRatio) {
        this.bodyRatio = bodyRatio;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getAvgVolume() {
        return avgVolume;
    }

    public void setAvgVolume(double avgVolume) {
        this.avgVolume = avgVolume;
    }

    public boolean isBodyStrong() {
        return bodyStrong;
    }

    public void setBodyStrong(boolean bodyStrong) {
        this.bodyStrong = bodyStrong;
    }

    public boolean isVolumeSpike() {
        return volumeSpike;
    }

    public void setVolumeSpike(boolean volumeSpike) {
        this.volumeSpike = volumeSpike;
    }

    public String getSignalType() {
        return signalType;
    }

    public void setSignalType(String signalType) {
        this.signalType = signalType;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public boolean isOpenPosition() {
        return openPosition;
    }

    public void setOpenPosition(boolean openPosition) {
        this.openPosition = openPosition;
    }

    public boolean isPompom() {
        return pompom;
    }

    public void setPompom(boolean pompom) {
        this.pompom = pompom;
    }
}
