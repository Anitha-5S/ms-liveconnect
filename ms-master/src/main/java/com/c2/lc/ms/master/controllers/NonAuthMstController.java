package com.c2.lc.ms.master.controllers;


import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.ItemPDPResponseBO;
import com.c2.lc.ms.master.bos.PlpBO;
import com.c2.lc.ms.master.controllers.base.MasterBaseController;
import com.c2.lc.ms.master.transactions.interfaces.ItemTransaction;
import com.c2.lc.ms.master.transactions.interfaces.OrderSyncTransaction;
import com.c2.lc.ms.master.transactions.interfaces.SearchTransaction;
import com.c2.lc.ms.master.transactions.interfaces.SellerTransaction;
import com.c2.lc.ms.master.utils.MsMessages;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping({"c2/lc/na"})
public class NonAuthMstController extends MasterBaseController {

    @Value("${api.base.path}")
    private String basePath;
    @Autowired
    private SearchTransaction searchTransaction;
    @Autowired private SellerTransaction sellerTransaction;

    @Autowired private OrderSyncTransaction orderSyncTransaction;

    @Autowired
    private ItemTransaction itemTransaction;

    @PostMapping(value = "/search/prd", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> searchByProduct(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/na/search/prd " +" ->"+ payload);
        try {

            Map<String, String> headers = new HashMap<>();
            headers.put("x-csquare-c2-code", "0");
            headers.put("x-csquare-br-code", "0");
            headers.put("x-csquare-terminal-id", "0");

            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            SearchBO searchBO = this.getValidatedSearchBO(payload);

            JsonArray list = searchTransaction.getProductDetails(lcHeaderBO, searchBO);
            PlpBO responseBO = new PlpBO();
            responseBO.setList(list);
            responseBO.setNextPage(searchBO.getPage() + 1);
            responseBO.setTotal(searchTransaction.countProduct(searchBO));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);

            this.setDataJsonObjectPayload(apiResponse, ret);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
    @PostMapping(value = "/pdp", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getItemById( @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/na/search/pdp " +" ->"+ payload);
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("x-csquare-c2-code", "0");
            headers.put("x-csquare-br-code", "0");
            headers.put("x-csquare-terminal-id", "0");
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);

            String itemCode = "";
            if (json.has("c_item_code")) {
                itemCode = json.get("c_item_code").getAsString();
                if (helper.isEmpty(itemCode)){
                    throw new InvalidRequestException("c_item_code", "'c_item_code' can't be empty");
                }
            }
            else
                throw new InvalidRequestException("'c_item_code' missing!", Messages.INVALID_REQUEST);

            ItemPDPResponseBO itemPDPResponseBO = itemTransaction.getById(itemCode, lcHeaderBO);
            JsonObject ret = helper.toJsonObjectTree(itemPDPResponseBO, ItemPDPResponseBO.class);
            this.setDataJsonObjectPayload(apiResponse, ret);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/search/el/prd", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> elSearchByProduct(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/na/search/el/prd " +" ->"+ payload);
        try {

            Map<String, String> headers = new HashMap<>();
            headers.put("x-csquare-c2-code", "0");
            headers.put("x-csquare-br-code", "0");
            headers.put("x-csquare-terminal-id", "0");

            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            SearchBO searchBO = this.getValidatedSearchBO(payload);

            JsonArray list = searchTransaction.getElProductDetails(lcHeaderBO, searchBO);
            PlpBO responseBO = new PlpBO();
            responseBO.setList(list);
            responseBO.setNextPage(searchBO.getPage() + 1);
            responseBO.setTotal(searchTransaction.elProductCount(searchBO));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);

            this.setDataJsonObjectPayload(apiResponse, ret);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/add/el/prd", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> elAdd(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/na/add/el/prd " +" ->"+ payload);
        try {

            Map<String, String> headers = new HashMap<>();
            headers.put("x-csquare-c2-code", "0");
            headers.put("x-csquare-br-code", "0");
            headers.put("x-csquare-terminal-id", "0");

            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            PageBO pageBO = helper.fromJson(payload, PageBO.class);

            searchTransaction.syncLcItemToElItem( pageBO);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/seller/logo", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> sellerLogo(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/na/seller/logo " +" ->"+ payload);
        try {

            JsonObject jsonObject = helper.fromJSON(payload, JsonObject.class);
            JsonObject response = sellerTransaction.getSellerLogo(jsonObject.get("c_seller_code").getAsString());
            this.setDataJsonObjectPayload(apiResponse, response);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/seller/uitem", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getUitem(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/na/seller/uitem " +" ->"+ payload);
        try {

            JsonObject jsonObject = helper.fromJSON(payload, JsonObject.class);
            JsonObject response = sellerTransaction.getUitemCode(jsonObject.get("c_seller_code").getAsString(),
                    jsonObject.get("c_seller_item_code").getAsString());
            this.setDataJsonObjectPayload(apiResponse, response);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/loToLc", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> loToLc(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/c2/lc/ms/mst/loToLc" + "->" + payload);
        try {
            JsonObject jsonObject = helper.fromJSON(payload, JsonObject.class);
            JsonObject res = orderSyncTransaction.orderSync(jsonObject);
            this.setDataJsonObjectPayload(apiResponse, res);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);

    }
}
