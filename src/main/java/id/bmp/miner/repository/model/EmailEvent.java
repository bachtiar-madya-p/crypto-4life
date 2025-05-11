package id.bmp.miner.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailEvent {

    @JsonProperty("uid")
    private String uid;
    @JsonProperty("email")
    private String email;
    @JsonProperty("status")
    private boolean status;
    @JsonProperty("subject")
    private String subject;
    @JsonProperty("details")
    private String details;

    public EmailEvent() {
        //Empty Constructor
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
