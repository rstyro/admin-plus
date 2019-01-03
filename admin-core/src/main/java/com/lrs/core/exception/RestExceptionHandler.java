package com.lrs.core.exception;

import com.lrs.core.entity.ApiResultEnum;
import com.lrs.core.entity.ResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/**
 * 全局异常捕获
 * @author rstyro
 *
 */
@RestControllerAdvice
public class RestExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)  
	public ResponseModel exceptionHandler(Exception ex) {
		logger.info(ex.getMessage(), ex);
		if(ex instanceof ApiException){
			return new ResponseModel(((ApiException) ex).getStatus(),ex.getMessage(),null);
		}else if(ex instanceof NullPointerException){
			return new ResponseModel(ApiResultEnum.ERROR_NULL, null);
		}else if(ex instanceof ClassCastException){
			return new ResponseModel(ApiResultEnum.ERROR_CLASS_CAST, null);
		}else if(ex instanceof IOException){
			return new ResponseModel(ApiResultEnum.ERROR_IO, null);
		}else if(ex instanceof HttpRequestMethodNotSupportedException){
			return new ResponseModel(ApiResultEnum.ERROR_MOTHODNOTSUPPORT, null);
		}else if(ex instanceof RuntimeException){
			return new ResponseModel(ApiResultEnum.ERROR_RUNTION, null);
		}
		return new ResponseModel(ApiResultEnum.FAILED, null);
	}
	
}
