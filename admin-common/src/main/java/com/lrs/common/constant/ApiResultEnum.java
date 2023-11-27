package com.lrs.common.constant;

public enum ApiResultEnum {
	SUCCESS(200,"ok"),
	FAILED(400,"请求失败"),
	ERROR(500,"不知名错误"),
	ERROR_NULL(501,"空指针异常"),
	ERROR_CLASS_CAST(502,"类型转换异常"),
	ERROR_RUNTION(503,"运行时异常"),
	ERROR_IO(504,"上传文件异常"),
	ERROR_MOTHODNOTSUPPORT(505,"请求方法错误"),

	;
	
	private String message;
	private Integer status;
	
	public String getMessage() {
		return message;
	}

	public Integer getStatus() {
		return status;
	}
	private ApiResultEnum(Integer status,String message) {
		this.message = message;
		this.status = status;
	}

	
}
