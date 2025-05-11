package id.bmp.miner.util.log;


import id.bmp.miner.util.json.JsonUtil;

public class AppLogger extends BaseLogger {

    public AppLogger(Class<?> clazz) {
        super(clazz);
    }

    // DEBUG METHODS
    public void debug(String methodName, Object obj) {
        log.debug(() -> format(methodName, JsonUtil.toJson(obj)));
    }

    public void debug(String id, String methodName, Object obj) {
        log.debug(() -> format(id, methodName, JsonUtil.toJson(obj)));
    }

    // INFO METHODS
    public void info(String methodName, Object obj) {
        log.info(() -> format(methodName, JsonUtil.toJson(obj)));
    }

    public void info(String id, String methodName, Object obj) {
        log.info(() -> format(id, methodName, JsonUtil.toJson(obj)));
    }

    // WARN METHODS
    public void warn(String methodName, Object obj) {
        log.warn(() -> format(methodName, JsonUtil.toJson(obj)));
    }

    public void warn(String id, String methodName, Object obj) {
        log.warn(() -> format(id, methodName, JsonUtil.toJson(obj)));
    }

    // ERROR METHODS
    public void error(String methodName, Object obj) {
        log.error(() -> format(methodName, JsonUtil.toJson(obj)));
    }

    public void error(String id, String methodName, Object obj) {
        log.error(() -> format(id, methodName, JsonUtil.toJson(obj)));
    }
}
