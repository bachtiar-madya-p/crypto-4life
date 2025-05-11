package id.bmp.miner.rest.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OtpRequest {

    @JsonProperty("otpType")
    private String otpType;
    @JsonProperty("mobile")
    private String mobile;
    @JsonProperty("email")
    private String email;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("source")
    private String source;

    public OtpRequest() {
        //Empty constructor
    }

    public String getOtpType() {
        return otpType;
    }

    public void setOtpType(String otpType) {
        this.otpType = otpType;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
