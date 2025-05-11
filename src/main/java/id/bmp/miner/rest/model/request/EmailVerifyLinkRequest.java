package id.bmp.miner.rest.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailVerifyLinkRequest {

    @JsonProperty("otpType")
    private String otpType;
    @JsonProperty("email")
    private String email;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("code")
    private String code;
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("sub")
    private String sub;
    @JsonProperty("enable")
    private boolean enable;
    @JsonProperty("tokenUri")
    private String tokenUri;

    public EmailVerifyLinkRequest() {
        //Empty constructor
    }

    public String getOtpType() {
        return otpType;
    }

    public void setOtpType(String otpType) {
        this.otpType = otpType;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }
}
