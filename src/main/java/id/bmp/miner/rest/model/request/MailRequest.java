package id.bmp.miner.rest.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class MailRequest {

    @Valid
    @NotEmpty(message = "sender is mandatory")
    @JsonProperty("sender")
    private String sender;

    @Valid
    @NotEmpty(message = "recipients is mandatory")
    @JsonProperty("recipients")
    private List<String> recipients;

    @Valid
    @NotEmpty(message = "subject is mandatory")
    @JsonProperty("subject")
    private String subject;

    @Valid
    @NotEmpty(message = "body is mandatory")
    @JsonProperty("body")
    private String body;

    public MailRequest() {
        //Empty constructor
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
