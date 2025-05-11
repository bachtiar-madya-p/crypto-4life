package id.bmp.miner.rest.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OtpValidateRequest {

    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("otp")
    private String otp;

    @JsonProperty("source")
    private String source;

    public OtpValidateRequest() {
        //Empty constructor
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
