package id.bmp.miner.rest.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtpValidateResult {

    // for validate otp, only require mobile, regardless of OTP type
    // even for EMAIL OTP, we only keep track of mobile in db

    @JsonProperty("validate")
    private boolean validate;
    @JsonProperty("mobile")
    private String mobile;
    @JsonProperty("otp")
    private String otp;
    @JsonProperty("message")
    private String message;

    public OtpValidateResult() {
        // Empty constructor
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
