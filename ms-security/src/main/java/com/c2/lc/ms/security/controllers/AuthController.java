package com.c2.lc.ms.security.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.SessionExpiredException;
import com.c2.lc.lib.exceptions.UnAuthorizedException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.security.bos.ChangePasswordBO;
import com.c2.lc.ms.security.bos.UpdatePasswordBO;
import com.c2.lc.ms.security.configs.MsMessages;
import com.c2.lc.ms.security.entities.LcUserEntity;
import com.c2.lc.ms.security.bos.LoginBO;
import com.c2.lc.ms.security.bos.LoginRequest;
import com.c2.lc.ms.security.transactions.inteface.LcAuthTransaction;
import com.c2.lc.ms.security.controllers.base.LcBaseController;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/c2/lc/ms/auth")
public class AuthController extends LcBaseController {

    @Autowired private LcAuthTransaction lcAuthTransaction;

    @GetMapping(path = "/validate", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> validate(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("/c2/lc/ms/auth/validate");
        HttpStatus httpStatus = HttpStatus.OK;
        JsonObject response = new JsonObject();
        HttpHeaders responseHeaders = new HttpHeaders();

        try {
            String key = getKey(headers);
            String token = getToken(headers);

            if (helper.isEmpty(key) || helper.isEmpty(token)){
                httpStatus = HttpStatus.UNAUTHORIZED;
                throw new InvalidRequestException("", Messages.INVALID_REQUEST);
            }

            try {
                String [] values = lcAuthTransaction.validateToken(key, token);

                responseHeaders.add("x-csquare-user-id", values[0]);
                responseHeaders.add("x-csquare-c2-code", values[1]);
                responseHeaders.add("x-csquare-br-code", values[2]);
                responseHeaders.add("x-csquare-terminal-id", values[3]);
                responseHeaders.add("x-csquare-type", values[4]);
            } catch (InvalidRequestException i) {
                httpStatus = HttpStatus.UNAUTHORIZED;
                throw new InvalidRequestException(token, Messages.INVALID_REQUEST);
            } catch (UnAuthorizedException u) {
                httpStatus = HttpStatus.UNAUTHORIZED;
                throw new UnAuthorizedException(Messages.UNAUTHORIZED_REQUEST);
            } catch (SessionExpiredException s) {
                httpStatus = HttpStatus.UNAUTHORIZED;
                throw new SessionExpiredException(token, Messages.SESSION_INVALID);
            }
        } catch (Exception e) {
            // hack code
            if (httpStatus == HttpStatus.OK) httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response.addProperty("exception", e.getMessage());
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse, httpStatus, responseHeaders);
    }

    protected String getKey(Map<String, String> headers) {
        return headers.get("x-csquare-api-key");
    }

    protected String getToken(Map<String, String> headers) {
        return headers.get("x-csquare-api-token");
    }

    @PostMapping(value = "/register", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> register(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/c2/lc/ms/auth/register");
        try {

            LcUserEntity lcUserEntity = helper.fromJson(payload, LcUserEntity.class);
            JsonObject ret = lcAuthTransaction.register(lcUserEntity);
            this.setDataJsonObjectPayload(apiResponse, ret);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/login", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> login(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/c2/lc/ms/auth/login");
        LoginBO loginBO = new LoginBO();
        List<String> messages = new ArrayList<>();
        HttpStatus status = HttpStatus.OK;
        LoginRequest loginRequest = helper.fromJson(payload, LoginRequest.class);
        try {
            this.validateInputPayload(loginRequest);
            JsonObject keyValue = lcAuthTransaction.login(loginRequest.getMobileNumber(), loginRequest.getPassword(), loginRequest.getType() );
            messages.add(MsMessages.LOGIN_SUCCESS);
            this.setDataJsonObjectPayload(apiResponse, keyValue);
        } catch (Exception e) {
            messages.add(e.getMessage());
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse, messages, status);
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<ApiResponse> logout(Map<String,String> headers) {
        ApiResponse apiResponse = this.initializeResponse("/c2/lc/ms/auth/logout");
        LoginRequest loginRequest = helper.fromJson(headers.get("X-csquare-api-key"), LoginRequest.class);
        try {
            lcAuthTransaction.logout(loginRequest.getMobileNumber());
            //messages.add(MsMessages.LOGIN_SUCCESS);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/changepassword", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> changePassword(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/c2/lc/ms/auth/changepassward");

        UpdatePasswordBO updatePasswordBO = helper.fromJson(payload, UpdatePasswordBO.class);
        try {
            this.validateInputPayload(updatePasswordBO);
             lcAuthTransaction.updatePassword(updatePasswordBO);
            this.addMessage(apiResponse, MsMessages.PASSWORD_UPDATE);
        } catch (Exception e) {

            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/forgotpassword", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/c2/lc/ms/auth/forgotpassword");

        ChangePasswordBO changePasswordBO = helper.fromJson(payload, ChangePasswordBO.class);
        try {
            this.validateInputPayload(changePasswordBO);
            lcAuthTransaction.forgotPassword(changePasswordBO);
            this.addMessage(apiResponse, MsMessages.PASSWORD_UPDATE);
        } catch (Exception e) {

            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

}