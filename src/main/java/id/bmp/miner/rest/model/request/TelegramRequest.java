package id.bmp.miner.rest.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class TelegramRequest {

    @Valid
    @NotEmpty(message = "body is mandatory")
    @JsonProperty("body")
    private String body;

    public TelegramRequest() {
        //Empty constructor
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
