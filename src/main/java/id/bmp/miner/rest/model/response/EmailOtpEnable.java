package id.bmp.miner.rest.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailOtpEnable {

    // uid is used to identify a user in LDAP
    @JsonProperty("uid")
    private String uid;
    // sub is required when updating user in LDAP via SCIM
    @JsonProperty("sub")
    private String sub;
    @JsonProperty("enable")
    private boolean enable;

    public EmailOtpEnable() {
        //Empty constructor
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
}
