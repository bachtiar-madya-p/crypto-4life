package id.bmp.miner.controller;

import id.bmp.miner.manager.PropertyManager;
import id.bmp.miner.util.http.model.HTTPContentType;
import id.bmp.miner.util.http.model.HTTPRequest;
import id.bmp.miner.util.log.AppLogger;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BaseController {
    @Autowired
    protected Jdbi jdbi;

    protected AppLogger log;

    public BaseController() {
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

    protected HTTPRequest buildRequest(String url) {
        return new HTTPRequest.Builder(url).setContentType(HTTPContentType.APPLICATION_JSON).build();
    }
}
