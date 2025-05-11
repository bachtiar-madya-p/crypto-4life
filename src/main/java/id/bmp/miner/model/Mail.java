package id.bmp.miner.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Mail {

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("recipients")
    private List<String> recipients;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("body")
    private String body;

    public Mail() {
        // Empty constructor
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
