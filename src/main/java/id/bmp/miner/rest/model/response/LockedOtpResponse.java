package id.bmp.miner.rest.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LockedOtpResponse extends ServiceResponse {

    @JsonProperty("result")
    private LockedOtp result;

    public LockedOtpResponse() {
        super(HttpStatus.OK);
    }

    public LockedOtp getResult() {
        return result;
    }

    public void setResult(LockedOtp result) {
        this.result = result;
    }
}
