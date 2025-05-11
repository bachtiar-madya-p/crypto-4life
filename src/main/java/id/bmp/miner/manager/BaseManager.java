package id.bmp.miner.manager;

import id.bmp.miner.util.log.AppLogger;

public class BaseManager {

    protected AppLogger log;

    public BaseManager() {
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

    protected String getProp(String key) {
        return PropertyManager.getInstance().getProperty(key);
    }
}
