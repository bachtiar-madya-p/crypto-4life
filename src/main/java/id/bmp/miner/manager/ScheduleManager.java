package id.bmp.miner.manager;

import com.github.kagkarlsson.scheduler.task.helper.RecurringTask;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import com.github.kagkarlsson.scheduler.task.schedule.FixedDelay;
import id.bmp.miner.configuration.DatabaseConfig;
import id.bmp.miner.job.ScalpingScannerJob;
import id.bmp.miner.util.property.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ScheduleManager extends BaseManager {

    @Autowired
    DatabaseConfig databaseConfig;

    @Autowired
    ScalpingScannerJob ScalpingScannerJob;

    public ScheduleManager() {
        log = getLogger(this.getClass());
    }

    @Bean
    public RecurringTask<Void> scalpingScannerTask() {
        String methodName = "scalpingScannerTask";
        int interval = PropertyManager.getInstance().getIntProperty(Property.BOT_SCANNER_INTERVAL);
        return Tasks.recurring("scalpingScannerTask", FixedDelay.ofMinutes(interval))
                .execute((inst, ctx) -> {
                    start(methodName);
                    ScalpingScannerJob.run();
                    completed(methodName);
                });
    }
/*
    @Bean
    public RecurringTask<Void> sendDailySummary() {
        String methodName = "sendDailySummary";

        return Tasks.recurring("sendDailySummary", CronSchedule.parse("0 0 8 * * *"))
                .execute((inst, ctx) -> {
                    start(methodName);
                    // logic kirim rekap ke Telegram
                    completed(methodName);
                });
    }*/
}
