package com.c2.lc.ms.master.controllers;


import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.NextPageBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.NotificationListBo;
import com.c2.lc.ms.master.entities.mongo.LcNotification;
import com.c2.lc.ms.master.transactions.interfaces.NotificationTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"${api.base.path}/notification"})
public class NotificationController extends LoBaseController {

    @Value("${api.base.path}")
    private String basePath;
    @Autowired private NotificationTransaction notificationTransaction;

    @GetMapping(value = "/count", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> count(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/notification/count"+headers.toString());
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            int count = notificationTransaction.count(header);

            JsonObject json = new JsonObject();
            json.addProperty("count", count);
            this.setDataJsonObjectPayload(apiResponse, json);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/list", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> list(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/notification/list"+" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            PageBO pageBO = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBO);

            List<LcNotification> list = notificationTransaction.list(header, pageBO);

            NotificationListBo result = new NotificationListBo();
            NextPageBO nextPageBO = new NextPageBO();
            nextPageBO.setPage(pageBO.getPage() + 1);
           // nextPageBO.setTotal(notificationTransaction.count(header));
            result.setNotificationList(list);
            result.setNextPage(nextPageBO);

            this.setDataJsonObjectPayload(apiResponse, helper.toJsonObjectTree(result, NotificationListBo.class));

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/read", produces =Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> markNotification(@RequestHeader Map<String, String> headers, @RequestBody String payload){
        ApiResponse apiResponse = this.initializeResponse(basePath+"/notification/read/"+" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);

            JsonObject obj = helper.fromJson(payload, JsonObject.class);

            notificationTransaction.read(obj.get("c_notification_id").getAsString());

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value ="/readAll", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> markAllNotification(@RequestHeader Map<String, String> headers){
        ApiResponse apiResponse = this.initializeResponse(basePath+"/notification/readAll/"+" ->"+headers.toString());
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);

            notificationTransaction.readAll(headerBO);

            this.addMessage(apiResponse, "Notification read successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @DeleteMapping(value = "/{id}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteNotification(@PathVariable ("id") Long id){
        ApiResponse apiResponse = this.initializeResponse(basePath+"/notification/" + id);
        try {
            notificationTransaction.delete(id);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/clearAll", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteAllNotification(@RequestHeader Map<String, String> headers){
        ApiResponse apiResponse = this.initializeResponse(basePath+"/notification/clearAll/"+" ->"+headers.toString());
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);

            notificationTransaction.deleteAll(headerBO);

            this.addMessage(apiResponse, "Notification cleared successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/save", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> saveNotification(@RequestHeader Map<String, String> headers, @RequestBody String payload){
        ApiResponse apiResponse = this.initializeResponse(basePath+"/notification/save"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            LcNotification lcNotification = helper.fromJson(payload, LcNotification.class);
            this.validateInputPayload(lcNotification);

            notificationTransaction.saveNotification(lcNotification, header);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

}
