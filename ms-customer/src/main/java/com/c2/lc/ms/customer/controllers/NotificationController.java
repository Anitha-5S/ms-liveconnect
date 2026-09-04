package com.c2.lc.ms.customer.controllers;


import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.entities.customer.NotificationEntity;
import com.c2.lc.ms.customer.transactions.interfaces.NotificationTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"${api.base.path}/notification"})
public class NotificationController extends LoBaseController {

    @Autowired private NotificationTransaction notificationTransaction;

    @PostMapping(value = "/list", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> list(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/notification/list");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            PageBO pageBO = helper.fromJson(payload, PageBO.class);

            List<NotificationEntity> list = notificationTransaction.list(header.getUserId(), pageBO.getPage(), pageBO.getLimit());
            JsonArray ret = helper.toJsonArrayTree(list, new TypeToken<List<NotificationEntity>>() {
            }.getType());
            this.setDataJsonArrayPayload(apiResponse, ret);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/read/{id}", produces =Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> markNotification(@RequestHeader Map<String, String> headers,@PathVariable("id") String id){
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/notification/read/" + id);
        try {
            System.out.println(id);
            notificationTransaction.read(helper.getLong(id));

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value ="/readAll/{id}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> markAllNotification(@RequestHeader Map<String, String> headers,@PathVariable("id") String id){
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/notification/readAll/" + id);
        try {
            System.out.println(id);
            notificationTransaction.readAll(helper.getLong(id));

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @DeleteMapping(value = "/{id}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteNotification(@PathVariable ("id") Long id){
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/notification/" + id);
        try {
            notificationTransaction.delete(id);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @DeleteMapping(value = "/clearAll/{id}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteAllNotification(@PathVariable ("id") Long id){
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/notification/clearAll/" + id);
        try {
            notificationTransaction.deleteAll(id);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/save", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> saveNotification(@RequestBody String payload){
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/notification/save" + payload);
        try {
            NotificationEntity notificationEntity = helper.fromJson(payload,NotificationEntity.class);
            this.validateInputPayload(notificationEntity);
            notificationTransaction.saveNotification(notificationEntity);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

}
