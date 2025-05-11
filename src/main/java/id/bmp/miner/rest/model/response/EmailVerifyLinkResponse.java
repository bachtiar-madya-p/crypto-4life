package id.bmp.miner.rest.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailVerifyLinkResponse extends ServiceResponse {

    @JsonProperty("result")
    private EmailVerifyLinkResult result;

    public EmailVerifyLinkResponse() {
        super(HttpStatus.OK);
    }

    public EmailVerifyLinkResult getResult() {
        return result;
    }

    public void setResult(EmailVerifyLinkResult result) {
        this.result = result;
    }
}
