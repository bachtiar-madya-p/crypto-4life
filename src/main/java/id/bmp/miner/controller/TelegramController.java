package id.bmp.miner.controller;

import id.bmp.miner.manager.EncryptionManager;
import id.bmp.miner.model.Telegram;
import id.bmp.miner.util.http.HTTPClient;
import id.bmp.miner.util.http.model.HTTPContentType;
import id.bmp.miner.util.http.model.HTTPRequest;
import id.bmp.miner.util.http.model.HTTPResponse;
import id.bmp.miner.util.property.Property;
import org.springframework.stereotype.Repository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Repository
public class TelegramController extends BaseController {

    public TelegramController() {
        log = getLogger(this.getClass());
    }

    public boolean sendMessage(String message) {
        String methodName = "sendMessage";
        start(methodName);
        boolean result = false;
        try {
            String url = buildTelegramUrl() + "/sendMessage";
            String chatID = EncryptionManager.getInstance().decrypt(getProperty(Property.TELEGRAM_CHAT_ID));

            String payload = "chat_id=" + URLEncoder.encode(chatID, String.valueOf(StandardCharsets.UTF_8))
                    + "&text=" + URLEncoder.encode(message, String.valueOf(StandardCharsets.UTF_8));

            HTTPRequest request = new HTTPRequest.Builder(url).setContentType(HTTPContentType.APPLICATION_FORM_URLENCODED).build();

            HTTPResponse httpResponse = HTTPClient.post(request, payload);

            log.debug(methodName, "Status Code: " + httpResponse.getCode());
            log.debug(methodName,"Response: " + httpResponse.getBody());

            if(httpResponse.getCode() == 200) {
                result = true;
            }

        } catch (Exception e) {
            log.error(methodName, e);
        }

        return result;
    }


    private String buildTelegramUrl() {
        String baseUrl = getProperty(Property.TELEGRAM_BOT_URL);
        String token = EncryptionManager.getInstance().decrypt(getProperty(Property.TELEGRAM_BOT_TOKEN));
        return baseUrl + "/bot" + token;
    }

}
