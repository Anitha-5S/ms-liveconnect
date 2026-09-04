package com.c2.lc.lib.controller;

import com.c2.lc.lib.exceptions.*;
import com.c2.lc.lib.properties.AppMessages;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.api.ResponseVO;
import com.c2.lc.lib.base.BaseSuper;
import com.c2.lc.lib.kafka.KafkaHelper;
import com.c2.lc.lib.security.AesCbcEncryption;
import com.c2.lc.lib.utils.AppStatus;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.lib.utils.OffsetRange;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class BaseController extends BaseSuper {
	@Autowired protected ObjectMapper objectMapper;

	@Autowired protected Validator validator;
	@Autowired protected KafkaHelper kafkaHelper;
	@Autowired protected AesCbcEncryption aesCbcEncryption;

	@Value("${fetch.max.size.limit:10000}")
	protected int maxSize;

	@Value("${api.connection.timeout:120000}")
	protected long apiConnectionTimeout;

	@Value("${infosec.api.response.setMessagesAndApiCall:true}")
	protected boolean enableAppMessagesAndApiCall;

	@GetMapping(path = "/ping", produces = "application/json")
	public ResponseEntity<ApiResponse> ping() {
		ApiResponse apiResponse = new ApiResponse(Constants.EMPTY_STRING, helper.getRandomUUID());
		try {
			JsonObject data = new JsonObject();
			data.addProperty("time", helper.getCurrentTimeString());

			setJsonPayload(apiResponse, data);
		} catch (Exception e) {
			this.handleAppExceptions(e, apiResponse);
		}
		return getResponseEntity(apiResponse);
	}

	protected void setDataJsonObjectPayload(ApiResponse response, JsonObject data) throws JsonProcessingException {
		JsonObject ret = new JsonObject();
		ret.add("data", data);
		this.setJsonPayload(response, ret);
	}

	protected void setDataJsonArrayPayload(ApiResponse response, JsonArray data) throws JsonProcessingException {
		JsonObject ret = new JsonObject();
		ret.add("data", data);
		this.setJsonPayload(response, ret);
	}

	protected JsonObject getDataJsonObject(String payload) throws InputPayloadException {
		JsonObject obj = helper.getJsonObject(payload);
		if (!obj.has("data"))  { throw new InputPayloadException("'data' element is missing!");}
		return obj.get("data").getAsJsonObject();
	}

	protected JsonArray getDataJsonArray(String payload) throws InputPayloadException {
		JsonObject obj = helper.getJsonObject(payload);
		if (!obj.has("data"))  { throw new InputPayloadException("'data' element is missing!");}
		return obj.get("data").getAsJsonArray();
	}

	protected void setJsonPayload(ApiResponse response, JsonElement data) throws JsonProcessingException {
		JsonObject resData = data.getAsJsonObject();
		if (resData.has("status") && !resData.get("status").getAsString().equalsIgnoreCase("success")){
			response.setAppStatusCode(6);
		}
		response.setPayloadJson(objectMapper.readValue(helper.toJson(data), Object.class));
	}

	protected ApiResponse initializeResponseWithoutLog(String message) {
		return new ApiResponse(message);
	}

	protected ApiResponse initializeResponse(Map<String, String> headers, String message) {
		log.debug("API End point {}", message);
		return new ApiResponse(message, helper.getRandomUUID(), headers);
	}

	protected ApiResponse initializeResponse(String message) {
		log.debug("API End point {}", message);
		return new ApiResponse(message, helper.getRandomUUID());
	}

	/*
	protected ApiResponse initializeResponse(HttpEntity<?> httpEntity, String message) {
		log.debug(message);
		if (log.isDebugEnabled()) {
			log.debug(systemHelper.toJSON(httpEntity));
		}
		return new ApiResponse(message);
	}
*/
	private String getCallType(String message) {
		int endIndex = message.indexOf('/', 0);
		message = message.substring(0 , endIndex);
		return message.trim();
	}

	private String getApiUrl(String message) {
		int beginIndex = message.indexOf('/', 0);
		message = message.substring(beginIndex);
		return message.trim();
	}
/*
	protected void logAPIEntry(Logger logger, HttpEntity<?> httpEntity, String message) {
		log.debug(message);
		if (log.isDebugEnabled()) {
			log.debug(helper.toJSON(httpEntity));
		}
	}

*/
	protected String getMessage(String message, String tag, String value) {
		return message.replace(tag, value);
	}

	protected void handleAppExceptions(Exception e, ApiResponse apiResponse) {
		StringBuilder message = new StringBuilder(apiResponse.getApiCall() + Constants.NEXT_LINE + Constants.NEXT_LINE + " Header Info :" + Constants.NEXT_LINE);
		String exception = Constants.EMPTY_STRING;

		if (e instanceof RecordNotFoundException) {
			RecordNotFoundException a = (RecordNotFoundException) e;
			exception = a.getMessage();
			apiResponse.setAppStatusCode(AppStatus.APP_CODE_RECORD_NOT_FOUND);
			log.debug(message + "RecordNotFoundException : " + a.getMessage());
		} else if (e instanceof DuplicateRecordException) {
			DuplicateRecordException a = (DuplicateRecordException) e;
			exception = a.getMessage();
			apiResponse.setAppStatusCode(AppStatus.APP_CODE_DUPLICATE_RECORD);
			log.debug(message + "DuplicateRecordException : " + a.getMessage());
		} else if (e instanceof SimpleException) {
			SimpleException a = (SimpleException) e;
			exception = a.getMessage();
			apiResponse.setAppStatusCode(AppStatus.APP_CODE_DATA_INPUT_ERROR);
			log.debug(message + "SimpleException : " + a.getMessage());
		} else if (e instanceof UnAuthorizedException) {
			UnAuthorizedException u = (UnAuthorizedException) e;
			exception = u.getMessage();
			apiResponse.setAppStatusCode(AppStatus.APP_CODE_INVALID_REQUEST);
			log.debug("UnAuthorizedException : " + u.getMessage());
		} else if (e instanceof SessionExpiredException) {
			SessionExpiredException u = (SessionExpiredException) e;
			exception = u.getMessage();
			apiResponse.setAppStatusCode(AppStatus.APP_CODE_SESSION_EXPIRED);
			log.debug("Session expired : " + u.getMessage());
		} else if (e instanceof UserAuthenticationException) {
			UserAuthenticationException u = (UserAuthenticationException) e;
			exception = u.getMessage();
			apiResponse.setAppStatusCode(u.getErrorCode());
			log.debug("UserAuthenticationException : " + u.getMessage());
		} else {

			if (e instanceof AppErrorException) {
				AppErrorException a = (AppErrorException) e;
				exception = a.getMessage();
				apiResponse.setAppStatusCode(a.getErrorCode());
				message.append("AppErrorException : ").append(a.getMessage());
			} else if (e instanceof DataFormatException) {
				DataFormatException d = (DataFormatException) e;
				exception = d.getMessage();
				apiResponse.setAppStatusCode(AppStatus.APP_CODE_DATA_INPUT_ERROR);
				message.append("DataFormatException : ").append(d.getMessage());
			}  else if (e instanceof InvalidDateException) {
				exception = getAppMessage(Messages.INVALID_DATE);
				apiResponse.setAppStatusCode(AppStatus.APP_CODE_DATA_INPUT_ERROR);
				message.append("InvalidDateException : ");
			} else if (e instanceof DataIntegrityViolationException) {
				exception = getAppMessage(Messages.APPLICATION_ERROR);
				apiResponse.setAppStatusCode(AppStatus.APP_CODE_DATA_INPUT_ERROR);
				message.append("DataIntegrityViolationException : ");
			} else if (e instanceof InputPayloadException) {
				InputPayloadException i = (InputPayloadException) e;
				apiResponse.setMessages(i.getErrorMessages());
				apiResponse.setAppStatusCode(AppStatus.APP_CODE_DATA_INPUT_ERROR);
				message.append("InputPayloadException : ");
			} else if (e instanceof CommunicationErrorException) {
				exception = e.getMessage();
				apiResponse.setAppStatusCode(AppStatus.APP_CODE_COMMUNICATION_ERROR);
				message.append("CommunicationErrorException : ");
			} else if (e instanceof ErrorException) {
				exception = e.getMessage();
				apiResponse.setAppStatusCode(((ErrorException) e).getErrorCode());
				message.append("Exception : ");
			} else {
				String messageChain = getExceptionMessageChain(e.getCause());
				exception = String.format("%s -> %s", getAppMessage(Messages.APPLICATION_ERROR), messageChain);
				apiResponse.setAppStatusCode(AppStatus.APP_CODE_APPLICATION_ERROR);
				if (!enableAppMessagesAndApiCall) {
					exception = Constants.EMPTY_STRING;
				} else {
					message.append(String.format("Exception : %s", messageChain));
				}
			}
			//message.append(Constants.NEXT_LINE).append(helper.toJSON(apiResponse));
			message.append(Constants.NEXT_LINE).append(apiResponse.getMessages());
			log.error(message.toString(), e);
		}

		// add exception to message
		apiResponse.getMessages().add(exception);

	}

	private String getExceptionMessageChain(Throwable throwable) {
		List<String> result = new ArrayList<>();
		while (throwable != null) {
			result.add(throwable.getMessage());
			throwable = throwable.getCause();
		}
		return StringUtils.join(result, ", ");
	}

	protected ResponseEntity<ApiResponse> getResponseEntity(ApiResponse response) {
		return getResponseEntity(response, response.getMessages(), HttpStatus.OK, null);
	}

	protected ResponseEntity<ApiResponse> getResponseEntity(ApiResponse response, HttpStatus status) {
		return getResponseEntity(response, response.getMessages(), status, null);
	}

	protected ResponseEntity<ApiResponse> getResponseEntity(ApiResponse response, List<String> messages) {
		return getResponseEntity(response, messages, HttpStatus.OK, null);
	}

	protected ResponseEntity<ApiResponse> getResponseEntity(ApiResponse response, List<String> messages, HttpStatus status) {
		return getResponseEntity(response, messages, status, null);
	}

	protected ResponseEntity<ApiResponse> getResponseEntity(ApiResponse response, HttpStatus status, HttpHeaders headers) {
		return getResponseEntity(response, response.getMessages(), status, headers);
	}

	protected ResponseEntity<ApiResponse> getResponseEntity(ApiResponse response, List<String> messages, HttpStatus status, HttpHeaders headers) {
		response.setMessages(messages);
		//suppress info for prod
		if (!enableAppMessagesAndApiCall) {
			response.setApiCall(Constants.EMPTY_STRING);
			response.setPayloadClass(null);
		}
		return new ResponseEntity(response, headers, status);
	}

	protected ResponseEntity<?> getBareResponseEntity() {
		return new ResponseEntity(HttpStatus.OK);
	}
	protected ResponseEntity<?> getBareResponseEntity(ApiResponse response) {
		//suppress info for prod
		if (!enableAppMessagesAndApiCall) {
			response.setApiCall(Constants.EMPTY_STRING);
			response.setPayloadClass(null);
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	protected void validateInputPayload(Object vo) throws InputPayloadException {
		ArrayList<String> messages = new ArrayList<>();

		if (vo == null) {
			messages.add(getAppMessage(Messages.INPUT_PAYLOAD_CANNOT_BE_NULL));
			throw new InputPayloadException(messages);
		}

		DirectFieldBindingResult result = new DirectFieldBindingResult(vo, vo.getClass().getName());
		validator.validate(vo, result);
		if (result.hasErrors()) {
			List<ObjectError> errors = result.getAllErrors();
			Iterator<ObjectError> iterator = errors.iterator();
			ObjectError obj;
			while (iterator.hasNext()) {
				obj = iterator.next();
				messages.add(obj.getDefaultMessage());
			}
			throw new InputPayloadException(messages);
		}
	}

	protected void addMessage(ApiResponse apiResponse, String msg) {
//		apiResponse.getMessages().add(getAppMessage(msg, helper.getLocale(apiResponse.getHeaders())));
		apiResponse.getMessages().add(msg);
	}
	protected String getAppMessage(String msg) {
		return AppMessages.getPropertyValue(msg);
	}

	protected void addMessage(ApiResponse apiResponse, String msg, String find, String replace) {
		apiResponse.getMessages().add(getAppMessage(msg, find, replace));
	}
	protected String getAppMessage(String msg, String find, String replace) {
		return getAppMessage(msg).replace(find, replace);
	}

	protected void addMessage(ApiResponse apiResponse, String msg, String locale) {
		apiResponse.getMessages().add(getAppMessage(msg, locale));
	}
	protected String getAppMessage(String msg, String locale) {
		return AppMessages.getPropertyValue(msg, locale);
	}

	protected void addMessage(ApiResponse apiResponse, String msg, String locale, String find, String replace) {
		apiResponse.getMessages().add(getAppMessage(msg, locale, find, replace));
	}
	protected String getAppMessage(String msg, String locale, String find, String replace) {
		return getAppMessage(msg, locale).replace(find, replace);
	}

	private static final String USER_ID = "x-csquare-user-id";
	protected Long getUserId(Map<String, String> headers) throws InvalidRequestException {
		String id = headers.get(USER_ID);
		if (helper.isEmpty(id)) {
			throw new InvalidRequestException("", "User id is not set!");
		}
		return Long.parseLong(id);
	}

	protected String getClassification(HttpHeaders header) {
		return helper.getTrimmedStringValue(header.getFirst(Constants.HEADER_CLASSIFICATION));
	}

	protected String getAppId(HttpHeaders header) {
		return helper.getTrimmedStringValue(header.getFirst(Constants.HEADER_APP_ID));
	}

	protected String getIpAddress(HttpHeaders header) {
		return helper.getTrimmedStringValue(header.getFirst(Constants.HEADER_IP_ADDRESS));
	}

	protected String getDeviceId(HttpHeaders header) {
		return helper.getTrimmedStringValue(header.getFirst(Constants.HEADER_DEVICE_ID));
	}

	protected String getAuthToken(HttpHeaders header) {
		return helper.getTrimmedStringValue(header.getFirst(Constants.HEADER_AUTH_TOKEN));
	}

	protected String getLoginId(HttpHeaders header)  {
		return helper.getTrimmedStringValue(header.getFirst(Constants.HEADER_LOGIN_ID));
	}

	protected String getPartnerId(HttpHeaders header)  {
		return helper.getTrimmedStringValue(header.getFirst(Constants.HEADER_PARTNER_ID));
	}

	protected String getLatitude(HttpHeaders header) {
		return helper.getTrimmedStringValue(header.getFirst(Constants.HEADER_LATITUDE));
	}

	protected String getLongitude(HttpHeaders header) {
		return helper.getTrimmedStringValue(header.getFirst(Constants.HEADER_LONGITUDE));
	}

	protected void logFormattedPayload(ApiResponse vo, ResponseEntity<ResponseVO> responseEntity) {
		if (log.isDebugEnabled()) {
			if (isGsonON() && vo.getPayloadClass() != null) {
				Object obj = vo.getPayloadJson();
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				log.debug(gson.toJson(obj));
			} else {
				log.debug(helper.toJSON(responseEntity));
			}
		}
	}

	private boolean isGsonON() {
		return !helper.getSystemProperty("gson", "true");
	}

	protected OffsetRange validatePageRequest(Integer page, Integer size) throws AppErrorException {
		if (page < 0) { throw new AppErrorException(page, Messages.INVALID_PAGE); }
		if (size > maxSize) { throw new AppErrorException(size, Messages.TOO_MANY_RECORDS); }
		return new OffsetRange(page, size);
    }

    protected Object getValidatedBody(HttpEntity<?> httpEntity) throws InputPayloadException {
	    final Object body = httpEntity.getBody();
	    validateInputPayload(body);
	    return body;
	}

}
