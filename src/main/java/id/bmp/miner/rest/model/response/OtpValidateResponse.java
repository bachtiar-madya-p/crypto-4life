package id.bmp.miner.rest.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtpValidateResponse extends ServiceResponse {

    @JsonProperty("result")
    private OtpValidateResult result;

    public OtpValidateResponse() {
        super(HttpStatus.OK);
    }

    public OtpValidateResult getResult() {
        return result;
    }

    public void setResult(OtpValidateResult result) {
        this.result = result;
    }
}
