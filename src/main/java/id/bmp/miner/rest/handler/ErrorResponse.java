package id.bmp.miner.rest.handler;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

    private int code;
    private String message;

    public ErrorResponse(HttpStatus status) {
        this.code = status.value();
        this.message = status.getReasonPhrase();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
