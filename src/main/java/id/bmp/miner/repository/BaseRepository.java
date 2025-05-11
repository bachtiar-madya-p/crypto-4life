package id.bmp.miner.repository;

import id.bmp.miner.manager.PropertyManager;
import id.bmp.miner.util.log.AppLogger;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Update;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseRepository{

    @Autowired
    protected Jdbi jdbi;

    protected AppLogger log;

    public BaseRepository() {
        // Empty Constructor
    }

    protected AppLogger getLogger(Class<?> clazz) {
        return new AppLogger(clazz);
    }

    protected void start(String methodName) {
        log.debug(methodName, "Start");
    }

    protected void completed(String methodName) {
        log.debug(methodName, "Completed");
    }

    protected String getProperty(String key) {
        return PropertyManager.getInstance().getProperty(key);
    }

    protected int getIntProperty(String key) {
        return PropertyManager.getInstance().getIntProperty(key);
    }

    protected boolean getBoolProperty(String key) {
        return PropertyManager.getInstance().getBooleanProperty(key);
    }
    
    protected Handle getHandle() {
        return jdbi.open();
    }

    protected boolean executeUpdate(Update update) {
        return update.execute() > 0;
    }

    protected int execute(Update update) {
        return update.execute();
    }
}
