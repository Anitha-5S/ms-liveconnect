package com.c2.lc.ms.customer.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.entities.customer.TSSettingDetailEntity;
import com.c2.lc.ms.customer.entities.customer.TSStoreRegisterEntity;
import com.c2.lc.ms.customer.transactions.interfaces.LcUserTransaction;
import com.c2.lc.ms.customer.transactions.interfaces.TouchStoreTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"${api.base.path}/b2c/web"})
public class TSAdmiController extends LoBaseController {

    @Autowired private LcUserTransaction lcUserTransaction;
    @Autowired private TouchStoreTransaction touchStoreTransaction;

    @PostMapping(value = "/cust/dash", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8,consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> custDashBoardCount(@RequestHeader Map<String, String> headers,@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/b2c/web/cust/dash");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            if(!json.has("c_br_code") || !json.has("d_from_date") || !json.has("d_to_date")){
                throw new InvalidRequestException("Date range or br_code", "Payload missing");
            }
            if(helper.isEmpty(json.get("c_br_code").getAsString()) || helper.isEmpty(json.get("d_from_date").getAsString())
                    || helper.isEmpty(json.get("d_to_date").getAsString())) {
                throw new InvalidRequestException("Input","can't be empty");
            }
            String branch = json.get("c_br_code").getAsString();
            JsonObject result = lcUserTransaction.getCustCount(headerBO.getC2Code(),branch,json.get("d_from_date").getAsString(),json.get("d_to_date").getAsString());

            this.setDataJsonObjectPayload(apiResponse, result);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/register/store", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> saveStores(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/register/store");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            TSStoreRegisterEntity tsStoreRegisterEntity = helper.fromJson(payload, TSStoreRegisterEntity.class);
            this.validateInputPayload(tsStoreRegisterEntity);

            touchStoreTransaction.save(header, tsStoreRegisterEntity);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/customer/list", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getCustomers(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/customer/list");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject req = helper.fromJson(payload, JsonObject.class);

            List<JsonObject> list = lcUserTransaction.fetchAllCustomers(header.getC2Code(), searchBO, req);

            JsonObject res = new JsonObject();
            JsonArray result = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<JsonObject>>() {
                    }.getType());
            res.add("j_list", result);
            res.addProperty("n_next_page", searchBO.getPage() + 1);
            this.setDataJsonObjectPayload(apiResponse, res);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/save/settings", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> saveSettings(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/save/settings");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            TSSettingDetailEntity settingDetail = helper.fromJson(payload, TSSettingDetailEntity.class);
            this.validateInputPayload(settingDetail);

            touchStoreTransaction.saveSettingDetail(header, settingDetail);

            this.addMessage(apiResponse, "Saved Setting Detail");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/get/settings", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getSettings(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/get/settings");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            TSSettingDetailEntity result = touchStoreTransaction.getSettingDetail(header.getC2Code());

            this.setDataJsonObjectPayload(apiResponse, helper.toJsonObjectTree(result, TSSettingDetailEntity.class));

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/update/cust/status", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> updateCustomerStatus(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/update/cust/status");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject req = helper.fromJson(payload, JsonObject.class);

            lcUserTransaction.updateCustStatus(req);

            this.addMessage(apiResponse, "Status updated successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/customer/count", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getCutomersCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/customer/list/count");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject req = helper.fromJson(payload, JsonObject.class);

            JsonObject res = new JsonObject();
            res.addProperty("n_total", lcUserTransaction.fetchAllCustomersCount(header.getC2Code(), searchBO, req));
            this.setDataJsonObjectPayload(apiResponse, res);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/req/pincode/list", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> reqPinCodeList(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/req/pincode/list");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            List<JsonObject> list = touchStoreTransaction.getReqPinCodeList(header, searchBO);

            JsonObject res = new JsonObject();
            JsonArray result = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<JsonObject>>() {
                    }.getType());
            res.add("j_list", result);
            res.addProperty("n_next_page", searchBO.getPage() + 1);
            this.setDataJsonObjectPayload(apiResponse, res);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/req/pincode/list/count", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> reqPinCodeListCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/req/pincode/list");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            long count = touchStoreTransaction.getReqPinCodeListCount(header, searchBO);

            JsonObject res = new JsonObject();
            res.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, res);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/req/pincode/cust/list", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> reqPinCodeCustList(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/req/pincode/cust/list");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject obj = helper.fromJson(payload, JsonObject.class);
            if (obj.has("c_pincode")) {
                if(obj.get("c_pincode").getAsString().isBlank()){
                    throw new InputPayloadException("'c_pincode' should not be empty");
                }
                else if (obj.get("c_pincode").getAsString().length() != 6) {
                    throw new InputPayloadException("'c_pincode' length should be 6");
                }
            } else {
                throw new InputPayloadException("'c_pincode' field required");
            }

            List<JsonObject> list = touchStoreTransaction.getReqPinCodeCustList(header, searchBO, obj.get("c_pincode").getAsString());

            JsonObject res = new JsonObject();
            JsonArray result = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<JsonObject>>() {
                    }.getType());
            res.add("j_list", result);
            res.addProperty("n_next_page", searchBO.getPage() + 1);
            this.setDataJsonObjectPayload(apiResponse, res);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/req/pincode/cust/list/count", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> reqPinCodeCustListCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/req/pincode/cust/list");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject obj = helper.fromJson(payload, JsonObject.class);
            if (obj.has("c_pincode")) {
                if (obj.get("c_pincode").getAsString().length() != 6) {
                    throw new InputPayloadException("'c_pincode' length should be 6");
                }
            } else {
                throw new InputPayloadException("'c_pincode' should not be empty!");
            }

            long count = touchStoreTransaction.getReqPinCodeCustListCount(header, searchBO, obj.get("c_pincode").getAsString());

            JsonObject res = new JsonObject();
            res.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, res);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/service/pincode/list", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> servicePinCodeList(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/service/pincode/list");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            List<JsonObject> list = touchStoreTransaction.getServicePinCodeList(header.getC2Code(), searchBO);

            JsonObject res = new JsonObject();
            JsonArray result = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<JsonObject>>() {
                    }.getType());
            res.add("j_list", result);
            res.addProperty("n_next_page", searchBO.getPage() + 1);
            this.setDataJsonObjectPayload(apiResponse, res);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/service/pincode/list/count", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> servicePinCodeListCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/service/pincode/list");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            long count = touchStoreTransaction.getServicePinCodeListCount(header.getC2Code(), searchBO);

            JsonObject res = new JsonObject();
            res.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, res);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/activate/pincode/req", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> activateReq(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/activate/pincode/req");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);

            if (json.has("c_pincode") && json.has("c_status")) {
                String pin = json.get("c_pincode").getAsString();
                String status = json.get("c_status").getAsString();
                if (helper.isEmpty(pin)){
                    throw new InputPayloadException("'c_pincode' can not be blank/empty");
                }
                if ( pin.length() != 6) {
                    throw new InputPayloadException("'c_pincode' length should be 6");
                }
                if (helper.isEmpty(status)){
                    throw new InputPayloadException("'c_status' can not be blank/empty");
                }
                if (helper.isEmpty(status) && status.length() != 1) {
                    throw new InputPayloadException("'c_status' length should be 1");
                }
                if (!Objects.equals(status, "Y") && !Objects.equals(status, "N")){
                    throw new InputPayloadException("'c_status' value should be 'Y' or 'N'");
                }
                touchStoreTransaction.activatePinCodeReq(header.getC2Code(), pin, status);
                this.addMessage(apiResponse, "Success!");
            }
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
}


