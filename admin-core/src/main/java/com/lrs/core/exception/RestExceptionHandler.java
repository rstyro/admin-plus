package com.lrs.core.exception;

import com.lrs.common.constant.ApiResultEnum;
import com.lrs.common.vo.R;
import com.lrs.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/**
 * 全局异常捕获
 * @author rstyro
 *
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(NullPointerException.class)
	public R NullPointer(NullPointerException ex){
		log.error(ex.getMessage(),ex);
		return R.error(ApiResultEnum.ERROR_NULL);
	}

	@ExceptionHandler(ClassCastException.class)
	public R ClassCastException(ClassCastException ex){
		log.error(ex.getMessage(),ex);
		return R.error(ApiResultEnum.ERROR_CLASS_CAST);
	}

	@ExceptionHandler(IOException.class)
	public R IOException(IOException ex){
		log.error(ex.getMessage(),ex);
		return R.error(ApiResultEnum.ERROR_IO);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public R HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex){
		log.error(ex.getMessage(),ex);
		return R.error(ApiResultEnum.ERROR_MOTHODNOTSUPPORT);
	}


	@ExceptionHandler(ApiException.class)
	public R ApiException(ApiException ex) {
		log.error(ex.getMessage(),ex);
		return R.fail(ex.getStatus(),ex.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public R RuntimeException(RuntimeException ex){
		log.error(ex.getMessage(),ex);
		return R.error(ApiResultEnum.ERROR_RUNTION);
	}

	@ExceptionHandler(Exception.class)
	public R exception(Exception ex){
		log.error(ex.getMessage(),ex);
		return R.error(ApiResultEnum.ERROR);
	}

}
