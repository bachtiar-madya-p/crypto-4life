package id.bmp.miner.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Telegram {

    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("text")
    private String message;

    public Telegram() {
        // Empty constructor
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
