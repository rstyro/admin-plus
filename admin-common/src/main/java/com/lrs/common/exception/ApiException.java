package com.lrs.common.exception;


import com.lrs.common.constant.ApiResultEnum;
import lombok.Data;

/**
 * 自定义的api异常
 * @author rstyro
 *
 */
@Data
public class ApiException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private Integer status;
	private String message;
	private Object data;
	private Exception exception;
	public ApiException() {
		super();
	}

	public ApiException(Integer status, String message, Object data, Exception exception) {
		this.status = status;
		this.message = message;
		this.data = data;
		this.exception = exception;
	}
	public ApiException(String message) {
		this(ApiResultEnum.ERROR.getCode(),message,null,null);
	}
	public ApiException(ApiResultEnum apiResultEnum) {
		this(apiResultEnum.getCode(),apiResultEnum.getMessage(),null,null);
	}
	public ApiException(ApiResultEnum apiResultEnum, Object data) {
		this(apiResultEnum.getCode(),apiResultEnum.getMessage(),data,null);
	}
	public ApiException(ApiResultEnum apiResultEnum, Object data, Exception exception) {
		this(apiResultEnum.getCode(),apiResultEnum.getMessage(),data,exception);
	}


}
