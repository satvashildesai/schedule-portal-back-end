package com.storeshop.scheduleportal.util;

import java.util.List;

public class SuccessResponse {
	private String statusCodeValue;
	private String statusCode;
	private String message;
	private List<Object> data;

	public SuccessResponse() {
		super();
	}

	public SuccessResponse(String statusCodeValue, String statusCode, String message) {
		super();
		this.statusCodeValue = statusCodeValue;
		this.statusCode = statusCode;
		this.message = message;
	}

	public SuccessResponse(String statusCodeValue, String statusCode, String message, List<Object> data) {
		super();
		this.statusCodeValue = statusCodeValue;
		this.statusCode = statusCode;
		this.message = message;
		this.data = data;
	}

	public String getStatusCodeValue() {
		return statusCodeValue;
	}

	public void setStatusCodeValue(String statusCodeValue) {
		this.statusCodeValue = statusCodeValue;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "SuccessResponse [statusCodeValue=" + statusCodeValue + ", statusCode=" + statusCode + ", message="
				+ message + ", data=" + data + "]";
	}

}
