package id.bmp.miner.rest.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtpResult {

    @JsonProperty("send")
    private boolean send;
    @JsonProperty("mobile")
    private String mobile;
    @JsonProperty("email")
    private String email;
    @JsonProperty("otp")
    private String otp;
    @JsonProperty("message")
    private String message;
    @JsonProperty("unlockDate")
    private String unlockDate;

    public OtpResult() {
        // Empty constructor
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUnlockDate() {
        return unlockDate;
    }

    public void setUnlockDate(String unlockDate) {
        this.unlockDate = unlockDate;
    }
}
