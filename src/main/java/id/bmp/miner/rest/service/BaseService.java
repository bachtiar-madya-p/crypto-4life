package id.bmp.miner.rest.service;

import id.bmp.miner.manager.PropertyManager;
import id.bmp.miner.util.log.AppLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import id.bmp.miner.rest.model.response.ServiceResponse;
import id.bmp.miner.util.json.JsonUtil;

import java.util.concurrent.ExecutorService;

public class BaseService {

	protected AppLogger log;

	protected ExecutorService executor;

	public BaseService() {
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

	protected boolean getBooleanProperty(String key) {
		return PropertyManager.getInstance().getBooleanProperty(key);
	}

	protected void logRequest(String methodName, Object object) {
		log.debug(methodName, "Request : " +JsonUtil.toJson(object));
	}

	protected void logResponse(String methodName, Object object) {
		log.debug( methodName, "Response : " +JsonUtil.toJson(object));
	}

	// Success response
	protected ResponseEntity<ServiceResponse> buildSuccessResponse() {
		return buildServiceResponseEntity(HttpStatus.OK);
	}

	protected <T> ResponseEntity<T> buildSuccessResponse(@Nullable T body) {
		return new ResponseEntity<>(body, HttpStatus.OK);
	}

	// Bad request response
	protected ResponseEntity<ServiceResponse> buildBadRequestResponse() {
		return buildServiceResponseEntity(HttpStatus.BAD_REQUEST, "Bad request");
	}

	protected ResponseEntity<ServiceResponse> buildBadRequestResponse(String message) {
		return new ResponseEntity<>(new ServiceResponse(HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
	}

	protected ResponseEntity<ServiceResponse> buildServiceResponseEntity(HttpStatus status, String message) {
		return new ResponseEntity<>(new ServiceResponse(status, message), status);
	}

	protected ResponseEntity<ServiceResponse> buildServiceResponseEntity(HttpStatus status) {
		return new ResponseEntity<>(new ServiceResponse(status), status);
	}

}
