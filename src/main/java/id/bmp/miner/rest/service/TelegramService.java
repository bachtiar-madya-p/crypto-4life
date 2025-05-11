package id.bmp.miner.rest.service;


import id.bmp.miner.controller.TelegramController;
import id.bmp.miner.model.Telegram;
import id.bmp.miner.rest.model.response.ServiceResponse;
import id.bmp.miner.util.property.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "telegram", produces = MediaType.APPLICATION_JSON_VALUE)
public class TelegramService extends BaseService{


    @Autowired
    private TelegramController telegramController;

    public TelegramService() {
        log = getLogger(this.getClass());
    }

    @PostMapping(path = "test")
    public ResponseEntity<ServiceResponse> testTelegramBot(@RequestParam("body") String body) {
        final String methodName = "testTelegramBot";
        logRequest(methodName, "POST /telegram/test");

        ResponseEntity<ServiceResponse> response = buildBadRequestResponse();

        boolean telegramEnabled = getBooleanProperty(Property.TELEGRAM_BOT_ENABLED);
        if (telegramEnabled) {
            boolean sent = telegramController.sendMessage(body);
            if (sent) {
                response = buildSuccessResponse();
            } else {
                response = buildBadRequestResponse();
            }
        }
        else {
            response = buildBadRequestResponse("Telegram bot is disabled");
        }

        logResponse(methodName, response.getBody());
        completed(methodName);
        return response;
    }


}
