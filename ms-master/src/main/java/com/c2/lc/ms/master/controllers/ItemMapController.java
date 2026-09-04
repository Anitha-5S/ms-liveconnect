package com.c2.lc.ms.master.controllers;


import com.c2.lc.ms.master.bos.*;
import com.c2.lc.ms.master.transactions.interfaces.ItemMappingTransaction;
import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.NextPageBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.utils.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "${api.base.path}/item/live")
public class ItemMapController extends LoBaseController {

    @Autowired
    private ItemMappingTransaction itemMappingTransaction;

    @GetMapping(value = "/count",  produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> countItem(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("/eco/live/count");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
           // JsonObject json = helper.fromJson(payload, JsonObject.class);

           // String c2Code = json.get("c_c2code").getAsString();

            ItemMapCountBO itemMapCountBO = itemMappingTransaction.itemCount(header.getC2Code());
            this.setDataJsonObjectPayload(apiResponse, helper.toJsonObjectTree(itemMapCountBO, ItemMapCountBO.class));

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/deleteItem", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteItem(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/eco/live/deleteItem");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
          //  String c2Code = json.get("c_c2code").getAsString();
            String itemCode = json.get("c_item_code").getAsString();
            itemMappingTransaction.deleteItem(header.getC2Code(), itemCode);
            this.addMessage(apiResponse, "Item Moved Successfully");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/moveToOwnAllItem", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> moveToOwnAllItemList(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/eco/live/moveToOwnAllItem");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            //String c2Code = json.get("c_c2code").getAsString();
            JsonArray arr = new JsonArray();
            if(json.has("j_codes")) {
                arr = json.getAsJsonArray("j_codes");
            }
            itemMappingTransaction.moveToOwnAllItemList(header.getC2Code(),arr,json);
            this.addMessage(apiResponse, "Item Moved Successfully");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/moveToBlockedItem", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> moveToBlockedItem(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/eco/live/moveToBlockedItem");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            //String c2Code = json.get("c_c2code").getAsString();
            String itemCode = json.get("c_item_code").getAsString();
            itemMappingTransaction.moveToBlockedItem(header.getC2Code(), itemCode);
            this.addMessage(apiResponse, "Item Moved Successfully");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/confirmItem", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> confirmItem(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/eco/live/confirmItem");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
          //  String c2Code = json.get("c_c2code").getAsString();
            String itemCode = json.get("c_item_code").getAsString();
            String cSquareC2Code = json.get("c_csquare_item_code").getAsString();
            String cSquareItemName = json.get("c_csquare_item_name").getAsString();
            itemMappingTransaction.confirmItem(header.getC2Code(), itemCode, cSquareC2Code, cSquareItemName);
            this.addMessage(apiResponse, "Item Moved Successfully");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/moveToOwnItem", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> moveToOwnItem(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/eco/live/moveToOwnItem");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            //String c2Code = json.get("c_c2code").getAsString();
            JsonArray arr = json.getAsJsonArray("j_item_codes");
            itemMappingTransaction.moveToOwnItem(header.getC2Code(), arr);
            this.addMessage(apiResponse, "Item Moved Successfully");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/listItem", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> listItem(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException, InputPayloadException {
        ApiResponse apiResponse = this.initializeResponse("/eco/live/listItem");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            PageBO pageBo = helper.fromJSON(payload, PageBO.class);
            NextPageBO nextPageBO = new NextPageBO();
            ItemListDetBO itemListDetBO = new ItemListDetBO();
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            this.validateInputPayload(pageBo);
            //String c2Code = json.get("c_c2code").getAsString();
            String listType = json.get("c_list_type").getAsString();
            List<ItemListBO> itemListBO = itemMappingTransaction.fetchItem(lcHeaderBO.getC2Code(), listType, pageBo.getPage(), pageBo.getLimit());
            nextPageBO.setPage(pageBo.getPage() + 1);
            nextPageBO.setTotal(itemMappingTransaction.count(lcHeaderBO.getC2Code()));
            itemListDetBO.setItemList(itemListBO);
            itemListDetBO.setNextPage(nextPageBO);
            this.setJsonPayload(apiResponse, helper.toJsonObjectTree(itemListDetBO, ItemListDetBO.class));
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }


}
