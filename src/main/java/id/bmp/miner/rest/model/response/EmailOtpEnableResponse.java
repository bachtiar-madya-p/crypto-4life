package id.bmp.miner.rest.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailOtpEnableResponse extends ServiceResponse {

    @JsonProperty("emailOtpEnable")
    private EmailOtpEnable emailOtpEnable;

    public EmailOtpEnableResponse() {
        super(HttpStatus.OK);
    }

    public EmailOtpEnable getEmailOtpEnable() {
        return emailOtpEnable;
    }

    public void setEmailOtpEnable(EmailOtpEnable emailOtpEnable) {
        this.emailOtpEnable = emailOtpEnable;
    }
}
