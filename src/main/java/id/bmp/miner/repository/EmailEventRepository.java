package id.bmp.miner.repository;

import id.bmp.miner.repository.model.EmailEvent;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.statement.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class EmailEventRepository extends BaseRepository {

    public EmailEventRepository() {
        log = getLogger(this.getClass());
    }

    public boolean insertEmailEvent (EmailEvent event)
    {
        final String methodName = "insertEmailEvent";
        start(methodName);
        boolean result = false;
        final String sql = "INSERT INTO mail_service_log (uid, email, status, subject, details, eventDt) " +
                "VALUES (:uid, :email, :status, :subject, :details, :eventDt);";

        try (Handle h = getHandle(); Update update = h.createUpdate(sql)) {
            update.bindBean(event);
            update.bind("eventDt", LocalDateTime.now());
            result = executeUpdate(update);
            log.debug(methodName, "Result: " + result);
        } catch (Exception ex) {
            log.error(methodName, ex);
        }
        completed(methodName);
        return result;
    }
}
