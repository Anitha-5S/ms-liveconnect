package com.c2.lc.ms.customer.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.entities.customer.FeedbackEntity;
import com.c2.lc.ms.customer.transactions.interfaces.FeedbackTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"${api.base.path}/feedback"})
public class FeedbackController extends LoBaseController {

    @Autowired private FeedbackTransaction feedbackTransaction;

    @PostMapping(value = "/saveFeedback", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> saveFeedback(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/feedback");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            FeedbackEntity feedbackEntity = helper.fromJson(payload, FeedbackEntity.class);
            feedbackTransaction.saveFeedback(header.getUserId(), header.getFirmId(), feedbackEntity);

            this.addMessage(apiResponse, "Feedback saved successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/{distributorId}")
    public ResponseEntity<ApiResponse> getFeedback(@RequestHeader Map<String, String> headers, @PathVariable Long distributorId) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/feedback/{distributorId}");
        try {
 /*           Long userId = this.getCustomerId(headers);
            Long firmId = this.getFirmId(headers);
            feedbackTransaction.validateRequest(userId, firmId);

            List<FeedbackEntity> feedbackEntity = feedbackTransaction.getFeedbackByDistributorId(distributorId);

            JsonArray list = (JsonArray) helper.getGson().toJsonTree(feedbackEntity,
                    new TypeToken<List<FeedbackEntity>>() {
                    }.getType());

            JsonObject response = new JsonObject();
            response.add("list", list);
            this.setJsonPayload(apiResponse, response);
*/        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/file", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> uploadDocument(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/feedback/file");
        try {
/*
            Long userId = this.getCustomerId(headers);
            Long firmId = this.getFirmId(headers);
            feedbackTransaction.validateRequest(userId, firmId);

            JsonObject json = helper.getJsonObject(payload);
            JsonObject response = feedbackTransaction.uploadDocument(userId, firmId, json);

            this.setJsonPayload(apiResponse, response);
*/
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/distributor/list", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getListDistributor(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/feedback/distributor/list");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject req = helper.fromJson(payload, JsonObject.class);
            String mobile = null;

            if(req.has("c_mobile_no")) {
                if(req.get("c_mobile_no").getAsString().length() < 10) {
                    throw new InvalidRequestException("Mobile number must be 10 digit");
                }
                mobile = req.get("c_mobile_no").getAsString();
            }

            JsonArray response = feedbackTransaction.getListDistributor(mobile,lcHeaderBO);
            if(response.size() < 1) {
                this.addMessage(apiResponse, "No Distributors found!");
            } else {
                this.addMessage(apiResponse, "Success!");
            }

            this.setDataJsonArrayPayload(apiResponse, response);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
}
