package id.bmp.miner.rest.service;

import id.bmp.miner.model.Mail;
import id.bmp.miner.repository.EmailEventRepository;
import id.bmp.miner.rest.model.request.MailRequest;
import id.bmp.miner.rest.model.response.ServiceResponse;
import id.bmp.miner.util.helper.MailHelper;
import id.bmp.miner.util.json.JsonUtil;
import id.bmp.miner.util.property.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "mail", produces = MediaType.APPLICATION_JSON_VALUE)
public class MailService extends BaseService {

    @Autowired
    private EmailEventRepository emailEventRepository;

    public MailService() {
        log = getLogger(this.getClass());
    }

    @PostMapping(path = "send")
    public ResponseEntity<ServiceResponse> sendEmail(@RequestBody @Valid MailRequest request) {
        final String methodName = "sendEmail";
        logRequest(methodName, "POST /mail/send");
        log.debug(methodName, JsonUtil.toJson(request));

        ResponseEntity<ServiceResponse> response = buildBadRequestResponse();

        Mail mail = new Mail();
        mail.setSender(request.getSender());
        mail.setRecipients(request.getRecipients());
        mail.setSubject(request.getSubject());
        mail.setBody(request.getBody());

        String status = MailHelper.sendEmail(mail);
        boolean sent = false;
        if (status.equals(Constant.MAIL_STATUS_SUCCESS)) {
            sent = true;
            response = buildSuccessResponse();
        } else {
            response = buildBadRequestResponse(status);
        }
/*        EmailEvent event = new EmailEvent();
        event.setUid(UUID.randomUUID().toString());
        event.setEmail(request.getRecipients().toString());
        event.setSubject(request.getSubject());
        event.setStatus(sent);
        event.setDetails(status);
        emailEventRepository.insertEmailEvent(event);*/

        logResponse(methodName, response.getBody());
        completed(methodName);
        return response;
    }

}
