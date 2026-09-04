package com.c2.lc.ms.master.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.PlpBO;
import com.c2.lc.ms.master.controllers.base.MasterBaseController;
import com.c2.lc.ms.master.entities.mongo.LcItem;
import com.c2.lc.ms.master.entities.mongo.RecentHistory;
import com.c2.lc.ms.master.transactions.interfaces.CategoryTransaction;
import com.c2.lc.ms.master.transactions.interfaces.RecentHistoryTransaction;
import com.c2.lc.ms.master.transactions.interfaces.SearchTransaction;
import com.c2.lc.ms.master.utils.MsMessages;
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
@RequestMapping({"/mst/search", "${search.endpoint}"})
public class SearchController extends MasterBaseController {

    @Value("${search.endpoint}")
    private String searchBasePath;
    @Autowired private SearchTransaction searchTransaction;
    @Autowired private CategoryTransaction categoryTransaction;
    @Autowired private RecentHistoryTransaction recentHistoryTransaction;


    @PostMapping(value = "/prd", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> searchByProduct(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(searchBasePath+"/prd" + " ->"+headers.toString()+" ->"+ payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = this.getValidatedSearchBO(payload);

            JsonArray list = searchTransaction.getProductDetails(header, searchBO);

           // saveSearchHistory(header, searchBO.getSearchTerm(), com.c2.lc.ms.master.utils.Constants.PRODUCT);

            JsonObject data = this.getPaginatedResponse(searchBO,list);
            this.setDataJsonObjectPayload(apiResponse, data);

            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/el/prd", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> elasticSearchByProduct(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(searchBasePath+"/el/prd" + " ->"+headers.toString()+" ->"+ payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = this.getValidatedSearchBO(payload);

            JsonArray list = searchTransaction.getElProductDetails(header, searchBO);

          //  saveSearchHistory(header, searchBO.getSearchTerm(), com.c2.lc.ms.master.utils.Constants.PRODUCT);

            JsonObject data = this.getPaginatedResponse(searchBO,list);
            this.setDataJsonObjectPayload(apiResponse, data);

            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }


  /*  private void saveSearchHistory(LcHeaderBO header, String searchString, String product) {

        RecentHistory history = new RecentHistory();
        history.setUserId(header.getUserId());
        history.setFirmId(header.getFirmId());
        history.setBranchId(header.getFirmId());
        history.setType(product);
        history.setSearchString(searchString);
        recentHistoryTransaction.save(history);
    }*/

    @GetMapping(value = "/history/{type}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> history(@RequestHeader Map<String, String> headers, @PathVariable String type) {
        ApiResponse apiResponse = this.initializeResponse(searchBasePath + "/history/{type}" + " ->" + headers.toString() + " ->" + type);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            RecentHistory recentHistory = recentHistoryTransaction.getById(header.getUserId(), type, header.getFirmId());
            JsonArray ret = new JsonArray();
            switch (recentHistory.getType()) {
                case com.c2.lc.ms.master.utils.Constants.MANUFACTURE:
                    ret = recentHistoryTransaction.getManufactureDetails(header, type);
                    break;
                case com.c2.lc.ms.master.utils.Constants.MOLECULE:
                    ret = recentHistoryTransaction.getMolecules(header, type);
                    break;
                case com.c2.lc.ms.master.utils.Constants.SELLER:
                    ret = recentHistoryTransaction.getSellerDetails(header, type);
                    break;
                case com.c2.lc.ms.master.utils.Constants.PRODUCT:
                    ret = recentHistoryTransaction.getProductDetails(header, type);
                    break;
            }
            JsonObject data = new JsonObject();
            data.add("j_list", ret);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/history/clear", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> historyClear(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(searchBasePath + "/history/clear" + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);

            String type = "";
            if (json.has("c_type")) {
                type = json.get("c_type").getAsString();
                if (helper.isEmpty(type)) {
                    throw new InvalidRequestException("c_type", "Type can't be empty");
                }
            } else
                throw new InvalidRequestException("c_search_filter_type", "Key not Found..!");

            recentHistoryTransaction.clearHistory(type, header.getUserId(), header.getFirmId());
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }


    @PostMapping(value = "/mol/items", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getProductsOnMolecule(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(searchBasePath+"/mol/items" +" ->"+headers.toString()+" ->"+ payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = this.getValidatedSearchBO(payload);

            JsonArray list = searchTransaction.getProductsOnMolecule(header, searchBO);

            JsonObject data = this.getPaginatedResponse(searchBO,list);
            this.setDataJsonObjectPayload(apiResponse, data);

            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/mol/list", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getMolecules(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(searchBasePath+"/mol/list" + " ->"+headers.toString()+" ->"+ payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = this.getValidatedSearchBO(payload);

            JsonArray list = searchTransaction.getMolecules(header, searchBO);

           // saveSearchHistory(header, searchBO.getSearchTerm(), com.c2.lc.ms.master.utils.Constants.MOLECULE);

            JsonObject data = this.getPaginatedResponse(searchBO,list);
            this.setDataJsonObjectPayload(apiResponse, data);

            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/seller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> searchBySeller(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(searchBasePath+"/seller" + " ->"+headers.toString()+" ->"+ payload);
        try {

            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = this.getValidatedSearchBO(payload);
            JsonArray list = searchTransaction.getSellerDetails(searchBO);
            JsonObject data = this.getPaginatedResponse(searchBO,list);
           // saveSearchHistory(header, searchBO.getSearchTerm(), com.c2.lc.ms.master.utils.Constants.SELLER);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/mfc", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> searchByManufacture(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(searchBasePath+"/mfc" + " ->"+headers.toString()+" ->"+ payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = this.getValidatedSearchBO(payload);
            JsonArray list = searchTransaction.getManufactureDetails(searchBO);
            JsonObject data = this.getPaginatedResponse(searchBO,list);
           // saveSearchHistory(header, searchBO.getSearchTerm(), com.c2.lc.ms.master.utils.Constants.MANUFACTURE);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/product/dd/{type}/{code}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> itemDetails(@PathVariable String type, @PathVariable String code) {
        ApiResponse apiResponse = this.initializeResponse(searchBasePath+"/product/dd/{type}/{code}"+type+code);
        try {
            JsonArray list = searchTransaction.getItemDetails(type, code);

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("data", list);
            this.setJsonPayload(apiResponse, jsonObject);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id :
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/categoryItem", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getItemListByCategory(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(searchBasePath+"/category" + " ->"+headers.toString()+" ->"+ payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            PageBO pageBO = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBO);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            JsonArray list = searchTransaction.getItemListByCategory(header,searchBO);

            JsonObject data = this.getPaginatedResponse(searchBO,list);
            this.setDataJsonObjectPayload(apiResponse, data);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id :
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/itemsByMfcCode", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> itemsByMfcCode(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(searchBasePath+"/itemsByMfcCode"+ " ->"+headers.toString()+" ->"+ payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            SearchBO searchBO = this.getValidatedSearchBO(payload);
            JsonArray list = searchTransaction.getProductOnManufacture(header,searchBO );
            JsonObject data = this.getPaginatedResponse(searchBO,list);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id :
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/itemsBySellerCode", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> itemsBySellerCode(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(searchBasePath+"/itemsBySellerCode" + " ->"+headers.toString()+" ->"+ payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = this.getValidatedSearchBO(payload);
            JsonArray list = searchTransaction.getProductOnSeller(header, searchBO );
            JsonObject data = this.getPaginatedResponse(searchBO,list);
            this.setDataJsonObjectPayload(apiResponse, data);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id :
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/itemsByMoleculeCode", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> itemsByMoleculeCode(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(searchBasePath+"/itemsByMoleculeCode" + " ->"+headers.toString()+" ->"+ payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = this.getValidatedSearchBO(payload);
           JsonArray list = searchTransaction.getProductOnMolecule(searchBO);
            /*JsonArray list = (JsonArray) helper.getGson().toJsonTree(items,
                    new TypeToken<List<LcItem>>() {
                    }.getType());*/
            JsonObject data = this.getPaginatedResponse(searchBO,list);
            this.setDataJsonObjectPayload(apiResponse, data);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id :
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/getSellerByItemCode", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getSellerByItemCode(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(searchBasePath+"/getSellerByItemCode"+ " ->"+headers.toString()+" ->"+ payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            SearchBO searchBO = this.getValidatedSearchBO(payload);
            String seller ="";
            if (jsonObject.has("c_seller_code")){
                seller = jsonObject.get("c_seller_code").getAsString();
            }
            JsonArray list = searchTransaction.getSellerOnProduct(header, searchBO,seller);
            JsonObject data = this.getPaginatedResponse(searchBO,list);
            this.setDataJsonObjectPayload(apiResponse, data);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /*private void saveTSSearchHistory(Long userId, String searchTerm, String c2Code) {
        RecentHistory history = new RecentHistory();
        history.setUserId(userId);
        history.setFirmId(appId);
        history.setBranchId(appId);
        history.setSearchString(searchTerm);
        history.setCreatedTimeStamp(helper.getCurrentTime());
        recentHistoryTransaction.save(history);
    }*/

    @PostMapping(path = "/recentHistory", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> addRecentHistory(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(searchBasePath+"/getSellerByItemCode"+ " ->"+headers.toString()+" ->"+ payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            this.validateInputPayload(jsonObject);
            String type = "";
            String code = "";
            if (jsonObject.has("c_type") && jsonObject.has("c_code")){
                type = jsonObject.get("c_type").getAsString();
                code = jsonObject.get("c_code").getAsString();
            }
            recentHistoryTransaction.addRecentHistory(header,type,code);
            this.addMessage(apiResponse,"Recent history added successfully");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

}
