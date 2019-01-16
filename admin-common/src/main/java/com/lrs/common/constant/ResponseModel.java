package com.lrs.common.constant;

import lombok.Data;

@Data
public class ResponseModel {
	private String status;
	private String message;
	private Object data;

	public  ResponseModel(Object data){
		this.message = "成功";
		this.status="200";
		this.data=data;
	}
	public  ResponseModel(String status, String message, Object data){
		this.message = message;
		this.status=status;
		this.data=data;
	}
	public  ResponseModel(ApiResultEnum resultEnum, Object data){
		this(resultEnum.getStatus(),resultEnum.getMessage(),data);
	}
}
