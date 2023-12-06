package com.lrs.common.constant;

public enum ApiResultEnum {
	SUCCESS(200,"ok"),
	FAILED(400,"请求失败"),
	NO_AUTH(403,"您无权限访问"),
	ERROR(500,"不知名错误"),
	ERROR_NULL(501,"空指针异常"),
	ERROR_CLASS_CAST(502,"类型转换异常"),
	ERROR_RUN(503,"运行时异常"),
	ERROR_IO(504,"上传文件异常"),
	ERROR_REQUEST_ERR(505,"请求方法错误"),

	/**系统框架，报错code:1000-2000 */
	SYSTEM_CODE_ERROR(1000,"验证码错误"),
	SYSTEM_ACCOUNT_NOT_FOUND(1001,"账号或密码错误"),
	SYSTEM_PASSWORD_ERROR(1002,"账号或密码错误"),
	SYSTEM_USER_NOT_FOUND(1003,"用户找不到"),

	;

	private String message;
	private Integer code;

	public String getMessage() {
		return message;
	}

	public Integer getCode() {
		return code;
	}
	private ApiResultEnum(Integer code,String message) {
		this.message = message;
		this.code = code;
	}


}
