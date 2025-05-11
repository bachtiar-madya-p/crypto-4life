package id.bmp.miner.util.helper;

import id.bmp.miner.manager.EncryptionManager;
import id.bmp.miner.manager.PropertyManager;
import id.bmp.miner.model.Mail;
import id.bmp.miner.util.log.AppLogger;
import id.bmp.miner.util.property.Constant;
import id.bmp.miner.util.property.Property;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class MailHelper{

    private static AppLogger log =  new AppLogger(MailHelper.class);
    public MailHelper() {
    }

    public static String sendEmail(Mail email) {

        String methodName = "sendEmail";
        start(methodName);
        // SMTP server configuration
        Properties props = new Properties();
        props.put(Constant.MAIL_SMTP_AUTH, getProperty(Property.MAIL_SMTP_AUTH));
        props.put(Constant.MAIL_SMTP_STARTTLS_ENABLE, getProperty(Property.MAIL_SMTP_STARTTLS_ENABLE));
        props.put(Constant.MAIL_SMTP_HOST, getProperty(Property.MAIL_SMTP_HOST));
        props.put(Constant.MAIL_SMTP_PORT, getProperty(Property.MAIL_SMTP_PORT));

        // Create a session with the SMTP server
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(getProperty(Property.MAIL_SMTP_USERNAME),
                        EncryptionManager.getInstance().decrypt(getProperty(Property.MAIL_SMTP_PASSWORD)));
            }
        });
        String status = "";
        try {
            if (!email.getRecipients().isEmpty()) {
                for (String recipientEmail : email.getRecipients()) {

                    // Create a MimeMessage object
                    MimeMessage message = new MimeMessage(session);
                    String senderEmail = PropertyManager.getInstance().getProperty(Property.MAIL_SMTP_USERNAME);
                    message.setFrom(new InternetAddress(senderEmail, email.getSender()));

                    // Set the recipient's email address
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

                    // Set the email subject
                    message.setSubject(email.getSubject());

                    // Create and set the HTML content for the email
                    String htmlContent = email.getBody();
                    message.setContent(htmlContent, Constant.MAIL_CONTENT);

                    // Send the email
                    Transport.send(message);
                }
                log.debug(methodName, "Email sent successfully!");
                status = Constant.MAIL_STATUS_SUCCESS;
            } else {
                log.debug(methodName, "No email recipients found. Skip sending email");
                status = "No email recipients found";
            }

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error(methodName, e);
            status = e.getMessage();
        }
        completed(methodName);
        return status;
    }


    protected  static void start(String methodName) {
        log.debug(methodName, "Start");
    }
    protected static void completed(String methodName) {
        log.debug(methodName, "Completed");
    }
    protected static String getProperty(String key) {
        return PropertyManager.getInstance().getProperty(key);
    }

    protected static int getIntProperty(String key) {
        return PropertyManager.getInstance().getIntProperty(key);
    }

    protected static boolean getBooleanProperty(String key) {
        return PropertyManager.getInstance().getBooleanProperty(key);
    }
}
