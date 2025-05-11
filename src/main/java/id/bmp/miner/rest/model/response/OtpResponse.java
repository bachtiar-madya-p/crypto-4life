package id.bmp.miner.rest.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtpResponse extends ServiceResponse {

    @JsonProperty("result")
    private OtpResult result;

    public OtpResponse() {
        super(HttpStatus.OK);
    }

    public OtpResult getResult() {
        return result;
    }

    public void setResult(OtpResult result) {
        this.result = result;
    }
}
