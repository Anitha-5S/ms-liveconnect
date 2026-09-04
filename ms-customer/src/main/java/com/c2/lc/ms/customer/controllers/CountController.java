package com.c2.lc.ms.customer.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.transactions.interfaces.FirmBranchTransaction;
import com.c2.lc.ms.customer.transactions.interfaces.FirmUserTransaction;
import com.c2.lc.ms.customer.transactions.interfaces.NotificationTransaction;
import com.c2.lc.ms.customer.transactions.interfaces.SellerTransaction;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"${api.base.path}/count"})
public class CountController extends LoBaseController {

    @Autowired
    private FirmBranchTransaction firmBranchTransaction;
    @Autowired private FirmUserTransaction firmUserTransaction;
    @Autowired private NotificationTransaction notificationTransaction;
    @Autowired private SellerTransaction sellerTransaction;

    @GetMapping(path = "/branch", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> branch(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/count/branch" + " ->" + headers.toString() );
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            long count = firmBranchTransaction.getCount(header.getUserId());
            JsonObject json = new JsonObject();
            json.addProperty("n_total", count);

            this.setDataJsonObjectPayload(apiResponse, json);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(path = "/user", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> user(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/count/user" + " ->" + headers.toString() );
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            long count = firmUserTransaction.getCount(header.getFirmId(),header.getUserId());
            JsonObject json = new JsonObject();
            json.addProperty("n_total", count);

            this.setDataJsonObjectPayload(apiResponse, json);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/notification", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> count(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/count/notification");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            int count = notificationTransaction.count(header.getUserId());
            JsonObject json = new JsonObject();
            json.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, json);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/unMappedSeller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> unMappedSeller(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/count/unMappedSeller");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);
            int count = sellerTransaction.getUnmappedCount(lcHeaderBO,searchBO);
            JsonObject json = new JsonObject();
            json.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, json);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/unMappedSellerSearch", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> unMappedSellerSearch(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/count/unMappedSellerSearch");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            String searchString = jsonObject.get("c_name").getAsString();
            if (helper.isEmpty(searchString) || searchString.length() < 3) {
                throw new InvalidRequestException("", "'c_name' should be minimum of 3 characters!");
            }
            int count = sellerTransaction.getUnmappedCountByName(searchString, lcHeaderBO);
            JsonObject json = new JsonObject();
            json.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, json);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/unMappedSellerSearch/cas", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> unMappedSellerSearchCas(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/count/unMappedSellerSearch/cas");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            String searchCity;
            String searchArea;
            String searchState;
            if (json.get("c_city") != null) {
                searchCity = json.get("c_city").getAsString();
            } else {
                searchCity = "";
            }
            if (json.get("c_area") != null) {
                searchArea = json.get("c_area").getAsString();
            } else {
                searchArea = "";
            }
            if (json.get("c_state") != null) {
                searchState = json.get("c_state").getAsString();
            } else {
                searchState = "";
            }
            int count = sellerTransaction.getUnmappedSearchCount(searchCity, searchState, searchArea, lcHeaderBO);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, jsonObject);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/mappedSearch", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> mappedSearch(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/count/mappedSearch");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            if(!jsonObject.has("c_mobile_no"))
                throw new InvalidRequestException("'c_mobile_no'", "Missing..!");
            if (jsonObject.has("c_mobile_no") && helper.isEmpty(jsonObject.get("c_mobile_no").getAsString()))
                throw new InvalidRequestException("'c_mobile_no'", "can't be empty..!");

            int count = sellerTransaction.getMappedSearchCount(jsonObject, lcHeaderBO);
            JsonObject json = new JsonObject();
            json.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, json);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/mapped", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> mapped(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/count/mapped");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            if(!jsonObject.has("c_mobile_no"))
                throw new InvalidRequestException("'c_mobile_no'", "Missing..!");
            if (jsonObject.has("c_mobile_no") && helper.isEmpty(jsonObject.get("c_mobile_no").getAsString()))
                throw new InvalidRequestException("'c_mobile_no'", "can't be empty..!");

            int count = sellerTransaction.getCount(lcHeaderBO,  jsonObject.get("c_mobile_no").getAsString());
            JsonObject json = new JsonObject();
            json.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, json);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
}
