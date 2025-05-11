package id.bmp.miner.util.property;

public class Property {
    private Property() {
        //Empty constructor
    }

    // Telegram Config
    public static final String TELEGRAM_BOT_ENABLED = "telegram.bot.enabled";
    public static final String TELEGRAM_BOT_URL = "telegram.bot.url";
    public static final String TELEGRAM_BOT_SEND_URL = "telegram.bot.send-url";
    public static final String TELEGRAM_BOT_TOKEN = "telegram.bot.token";
    public static final String TELEGRAM_CHAT_ID = "telegram.bot.chat-id";


    // Email Config
    public static final String MAIL_SMTP_HOST = "mail.smtp.host";
    public static final String MAIL_SMTP_PORT = "mail.smtp.port";
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String MAIL_SMTP_SENDER = "mail.smtp.sender";
    public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    public static final String MAIL_SMTP_USERNAME = "mail.smtp.username";
    public static final String MAIL_SMTP_PASSWORD = "mail.smtp.password";

    // Bot Config
    public static final String BOT_SCANNER_ENABLED = "bot.scanner.enabled";
    public static final String BOT_SCANNER_INTERVAL = "bot.scanner.interval";

    // Indodax Config
    public static final String IDX_BASE_URL = "idx.market.base-url";
    public static final String IDX_SUMMARY_URL = "idx.market.summary-url";
    public static final String IDX_CANDLE_URL = "idx.market.candle-url";

    public static final String IDX_CANDLE_TIME_FRAME = "idx.market.time-frame";
    public static final String IDX_CANDLE_LIMIT = "idx.market.candle-limit";

}
