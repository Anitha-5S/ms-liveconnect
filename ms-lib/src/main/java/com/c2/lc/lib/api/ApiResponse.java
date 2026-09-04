package com.c2.lc.lib.api;

import com.c2.lc.lib.utils.AppStatus;
import com.c2.lc.lib.utils.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@ApiModel
public class ApiResponse implements Serializable {

	private static final long serialVersionUID = -2479877644814765244L;

	public ApiResponse() {
		super();
	}

	public ApiResponse(String apiCall) {
		super();
		this.apiCall = apiCall;
	}

	public ApiResponse(String apiCall, String requestId) {
		super();
		this.apiCall = apiCall;
		this.requestId = requestId;
	}

	public ApiResponse(List<String> messages) {
		super();
		this.messages = messages;
	}

	public ApiResponse(int appStatusCode, List<String> messages) {
		super();
		this.appStatusCode = appStatusCode;
		this.messages = messages;
	}

	public ApiResponse(String message, String requestId, Map<String, String> headers) {
		super();
		this.messages = messages;
		this.requestId = requestId;
		this.headers = headers;
	}

	private Integer appStatusCode = AppStatus.APP_CODE_SUCCESS;

	@ApiModelProperty(example="Json Object")
	private Object payloadJson = null;

	private List<String> messages = new ArrayList<>();

	@ApiModelProperty(example="url endpoint")
	private String apiCall = Constants.EMPTY_STRING;

	@ApiModelProperty(example="Payload.class")
	private Class<?> payloadClass = Object.class;

	@ApiModelProperty(example="unique request id")
	private String requestId = Constants.EMPTY_STRING;

	private Map<String, String> headers = new HashMap<>();

}

