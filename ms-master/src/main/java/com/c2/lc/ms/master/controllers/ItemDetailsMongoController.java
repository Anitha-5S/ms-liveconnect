package com.c2.lc.ms.master.controllers;

import com.c2.lc.ms.master.controllers.base.MasterBaseController;
import com.c2.lc.ms.master.transactions.interfaces.ItemDetailsMongoTransaction;
import com.c2.lc.ms.master.models.ItemDetailModel;
import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.utils.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping( value = {"/mst/cache", "${api.base.path}/cache"})
public class ItemDetailsMongoController  extends MasterBaseController {

    @Value("${api.base.path}")
    private String basePath;
    @Autowired
    ItemDetailsMongoTransaction itemDetailsMongoTransaction;

    @GetMapping(value = "/detail/{itemCode}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> getItemDetail(@PathVariable ("itemCode") String itemCode) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/item/detail/" + itemCode);
        try {
            ItemDetailModel obj = itemDetailsMongoTransaction.getDetails(itemCode);
            JsonObject jsonObject = helper.getJsonObject(obj.getData());
            this.setJsonPayload(apiResponse, jsonObject);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/summary/{itemCode}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> getItemummary(@PathVariable ("itemCode") String itemCode) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/item/summary/" + itemCode);
        try {
            ItemDetailModel obj = itemDetailsMongoTransaction.getSummary(itemCode);
            JsonObject jsonObject = helper.getJsonObject(obj.getData());
            this.setJsonPayload(apiResponse, jsonObject);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/summary/mfac/{mfacCode}/page/{pageNumber}/limit/{rowLimit}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getItemSummaryByMfac(@PathVariable("mfacCode") String mfacCode,
                                                            @PathVariable("pageNumber") Long pageNumber,
                                                            @PathVariable("rowLimit") Long rowLimit) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/item/summary/mfac/page/limit/"+ mfacCode+pageNumber+rowLimit);
        try {
            JsonArray jsonArray = itemDetailsMongoTransaction.getSummaryByMfac(mfacCode,pageNumber,rowLimit);

            JsonObject response = new JsonObject();
            response.add("data", jsonArray);
            this.setJsonPayload(apiResponse, response);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/summary/content/{contCode}/page/{pageNumber}/limit/{rowLimit}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getItemSummaryByContent(@PathVariable("contCode") String contCode,
                                                               @PathVariable("pageNumber") Long pageNumber,
                                                               @PathVariable("rowLimit") Long rowLimit) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/item/summary/content/"+ contCode+pageNumber+rowLimit);
        try {
            JsonArray jsonArray = itemDetailsMongoTransaction.getSummaryByContent(contCode,pageNumber,rowLimit);

            JsonObject response = new JsonObject();
            response.add("data", jsonArray);
            this.setJsonPayload(apiResponse, response);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
}
