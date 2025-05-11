package id.bmp.miner.rest.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtpRetryCount  extends ServiceResponse {

    @JsonProperty("count")
    private int count;

    public OtpRetryCount() {
        // Empty constructor
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
