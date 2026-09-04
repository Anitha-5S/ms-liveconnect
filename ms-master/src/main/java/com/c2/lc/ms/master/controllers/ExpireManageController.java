package com.c2.lc.ms.master.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.*;
import com.c2.lc.ms.master.controllers.base.MasterBaseController;
import com.c2.lc.ms.master.transactions.interfaces.ExpireTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping({"/mst/exp", "${api.base.path}/exp"})
public class ExpireManageController extends MasterBaseController {

    @Value("${api.base.path}")
    private String basePath;

    @Autowired private ExpireTransaction expireTransaction;

    @PostMapping(value = "/itemBatch", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> itemBatch(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/exp/itemBatch" + " ->" + headers.toString() + " ->" + payload);
        try {
            BatchItemBo batchItemBo = helper.fromJSON(payload, BatchItemBo.class);
            this.validateInputPayload(batchItemBo);
            PageBO pageBO = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBO);
            JsonArray jsonArray = expireTransaction.getBatchItem(batchItemBo, pageBO);
            this.setDataJsonArrayPayload(apiResponse, jsonArray);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/newBatch", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> newBatch(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/exp/newBatch" + " ->" + headers.toString() + " ->" + payload);
        try {
            BatchBo batchItemBo = helper.fromJSON(payload, BatchBo.class);
            this.validateInputPayload(batchItemBo);
            expireTransaction.newBatch(batchItemBo);
            this.addMessage(apiResponse,"Batch created Successfully..!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/expireItem", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> expireItem(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/exp/expireItem" + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            BatchItemBo batchItemBo = helper.fromJSON(payload, BatchItemBo.class);
            this.validateInputPayload(batchItemBo);
            PageBO pageBO = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBO);
            JsonArray jsonArray = expireTransaction.getExpireItem(batchItemBo, pageBO, lcHeaderBO);
            this.setDataJsonArrayPayload(apiResponse, jsonArray);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/expireCart", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> expireCart(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/exp/expireCart" + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            ExpiryCart cart = helper.fromJSON(payload, ExpiryCart.class);
            this.validateInputPayload(cart);
            expireTransaction.addExpiryCart(cart, lcHeaderBO);
            this.addMessage(apiResponse, "Successfully Added...!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/expireRemove", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> expireRemove(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/exp/expireRemove" + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            DeleteExpiry cart = helper.fromJSON(payload, DeleteExpiry.class);
            this.validateInputPayload(cart);
            expireTransaction.deleteExpiryCart(cart);
            this.addMessage(apiResponse, "Item Removed Successfully ...!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/getCartItem", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getCartItem(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/exp/getCartItem" + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject inp = helper.fromJSON(payload, JsonObject.class);
            PageBO pageBO = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBO);
            if (inp.has("c_seller_code")){
                if (helper.isEmpty(inp.get("c_seller_code").getAsString()))
                    throw new InputPayloadException("'c_seller_code' can't be empty..!");
            }
            else
                throw new InputPayloadException("'c_seller_code' can't found..!");
            JsonArray jsonArray = expireTransaction.getExpireCart(inp.get("c_seller_code").getAsString(), lcHeaderBO, pageBO);
            this.setDataJsonArrayPayload(apiResponse, jsonArray);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/confirmCart", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> confirmCart(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/exp/confirmCart" + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject inp = helper.fromJSON(payload, JsonObject.class);
            PageBO pageBO = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBO);
            if (inp.has("c_seller_code")){
                if (helper.isEmpty(inp.get("c_seller_code").getAsString()))
                    throw new InputPayloadException("'c_seller_code' can't be empty..!");
            }
            else
                throw new InputPayloadException("'c_seller_code' can't found..!");
            JsonArray jsonArray = expireTransaction.confirmCart(inp.get("c_seller_code").getAsString(), lcHeaderBO, pageBO);
            this.setDataJsonArrayPayload(apiResponse, jsonArray);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/orders", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> orders(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/exp/orders" + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            ExpiryOrderFilterBo inp = helper.fromJSON(payload, ExpiryOrderFilterBo.class);
            PageBO pageBO = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBO);

            if (inp.getSellerCodes().size() == 0)
                throw new InputPayloadException("Seller code not found..!");

            JsonArray jsonArray = expireTransaction.getExpireOrders(inp, lcHeaderBO);
            this.setDataJsonArrayPayload(apiResponse, jsonArray);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/ordersById", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> ordersById(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/exp/ordersById" + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            ExpireOrderIdBo inp = helper.fromJSON(payload, ExpireOrderIdBo.class);
            this.validateInputPayload(inp);

            JsonObject jsonObject = expireTransaction.getOrdersById(inp, lcHeaderBO);
            this.setDataJsonObjectPayload(apiResponse, jsonObject);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/returnForm", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> returnForm(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/exp/returnForm" + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject jsonObject = helper.fromJSON(payload, JsonObject.class);
            if (jsonObject.has("c_seller_code") && jsonObject.has("c_buyer_code")){
                if (helper.isEmpty(jsonObject.get("c_seller_code").getAsString()))
                    throw new InputPayloadException("'c_seller_code' can't be empty..!");
                if (helper.isEmpty(jsonObject.get("c_buyer_code").getAsString()))
                    throw new InputPayloadException("'c_buyer_code' can't be empty..!");
            }
            else
                throw new InputPayloadException("Invalid Input..!");

            JsonObject resp = expireTransaction.getExpiryForm(jsonObject.get("c_seller_code").getAsString()
                    ,jsonObject.get("c_buyer_code").getAsString() , lcHeaderBO);
            this.setDataJsonObjectPayload(apiResponse, resp);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

}
