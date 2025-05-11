package id.bmp.miner.rest.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnlockedOtpResponse extends ServiceResponse {

    @JsonProperty("result")
    private UnlockOtp result;

    public UnlockedOtpResponse() {
        super(HttpStatus.OK);
    }

    public UnlockOtp getResult() {
        return result;
    }

    public void setResult(UnlockOtp result) {
        this.result = result;
    }
}
