package com.c2.lc.ms.master.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.ItemsSearchBO;
import com.c2.lc.ms.master.bos.PlpBO;
import com.c2.lc.ms.master.controllers.base.MasterBaseController;
import com.c2.lc.ms.master.transactions.interfaces.FilterTransaction;
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
@RequestMapping({"/mst/search", "${api.base.path}/filter/l"})
public class FilterController extends MasterBaseController {

    @Value("${api.base.path}")
    private String basePath;
    @Autowired
    private FilterTransaction filterTransaction;
    @Autowired
    private SearchTransaction searchTransaction;

    @PostMapping(value = "/newLaunchFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> newLaunchFilter(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/newLaunchFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ItemsSearchBO searchBO = helper.fromJson(payload, ItemsSearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.newLaunchFilter(header, searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            //data.addProperty("n_total",filterTransaction.newLaunchCount(header, searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/topMostOrderFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> topMostOrderFilter(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/topMostOrderFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ItemsSearchBO searchBO = helper.fromJson(payload, ItemsSearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.topMostOrderFilter(header, searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            //data.addProperty("n_total", filterTransaction.topMostOrderCount(header, searchBO) );

            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/preferredSellerFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> preferredSellerFilter(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/preferredSellerFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ItemsSearchBO searchBO = helper.fromJson(payload, ItemsSearchBO.class);
            this.validateInputPayload(searchBO);

            List<JsonObject> list = filterTransaction.preferredSellerFilter(header, searchBO);

            JsonObject data = new JsonObject();
            data.add("j_list", helper.toJsonArrayTree(list, new TypeToken<List<JsonObject>>() {}.getType()));
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            //data.addProperty("n_total", filterTransaction.preferredSellerCount(header, searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }


    @PostMapping(value = "/shopByMfcFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> shopByManufacturer(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/shopByMfcFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ItemsSearchBO searchBO = helper.fromJson(payload, ItemsSearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.shopByMfcFilter(header, searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
           // data.addProperty("n_total", filterTransaction.shopByMfcFilterCount(header, searchBO));

            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/moleculeFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> moleculeFilter(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/moleculeFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ItemsSearchBO searchBO = helper.fromJson(payload, ItemsSearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.moleculeFilter(header, searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
           // data.addProperty("n_total",filterTransaction.moleculeFilterCount(header, searchBO));
           // responseBO.setTotal( filterTransaction.moleculeFilterCount(header, searchBO));
           // JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);

            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }


    @PostMapping(value = "/preferredSellerBrand", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> preferredSellerBrand(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/preferredSellerBrand" +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.preferredSellerBrand(searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            /*responseBO.setTotal(filterTransaction.preferredSellerBrandCount(searchBO));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);*/
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/preferredSellerMfc", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getManufactures(@RequestHeader Map<String,String> headers,@RequestBody String payload){
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/preferredSellerMfc" +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.preferredSellerMfc(searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            /*responseBO.setTotal(filterTransaction.preferredSellerMfcCount(searchBO));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);*/
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/newLaunchSellers", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> newLaunchSellers(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/newLaunchSellers " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.newLaunchSellers(searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            //responseBO.setTotal(filterTransaction.newLaunchSellersCount( searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/newLaunchMfc", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> newLaunchMfc(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/newLaunchMfc " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.newLaunchMfc(searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
           // responseBO.setTotal(filterTransaction.newLaunchMfcCount( searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/newLaunchBrand", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> newLaunchBrand(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/newLaunchBrand " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.newLaunchBrand(searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            //responseBO.setTotal(filterTransaction.newLaunchBrandCount( searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/shopByMfcBrand", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> shopByMfcBrand(@RequestHeader Map<String,String> headers,@RequestBody String payload){
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/shopByMfcBrand" +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.shopByMfcBrand(searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            /*responseBO.setTotal(filterTransaction.shopByMfcBrandCount(searchBO));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);*/
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/shopByMfcSeller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> shopByMfcSeller(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/shopByMfcSeller " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.shopByMfcSellerSearch(searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            /*responseBO.setTotal(filterTransaction.shopByMfcSellerCount(searchBO));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);*/
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/topMostOrderMfc", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> topMostOrderMfc(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/topMostOrderMfc " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            this.validateInputPayload(searchBO);

            String stateCode = "";
            if (jsonObject.has("c_state_code")) {
                stateCode = jsonObject.get("c_state_code").getAsString();
                if (helper.isEmpty(stateCode)){
                    throw new InvalidRequestException("c_state_code","Can't be empty");
                }
            }
            else
                throw new InvalidRequestException("key missing", Messages.INVALID_REQUEST);

            JsonArray list = filterTransaction.topMostOrderMfc(searchBO,stateCode);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            //responseBO.setTotal(filterTransaction.topMostOrderMfcCount(searchBO,stateCode));
            //JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/topMostOrderBrand", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> topMostOrderBrand(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/topMostOrderBrand " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            this.validateInputPayload(searchBO);

            String stateCode = "";
            if (jsonObject.has("c_state_code")) {
                stateCode = jsonObject.get("c_state_code").getAsString();
                if (helper.isEmpty(stateCode)){
                    throw new InvalidRequestException("c_state_code","Can't be empty");
                }
            }
            else
                throw new InvalidRequestException("key missing", Messages.INVALID_REQUEST);

            JsonArray list = filterTransaction.topMostOrderBrand(searchBO,stateCode);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            /*responseBO.setTotal(filterTransaction.topMostOrderBrandCount(searchBO,stateCode));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);*/
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/topMostOrderSeller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> topMostOrderSeller(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/topMostOrderSeller " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            this.validateInputPayload(searchBO);

            String stateCode = "";
            if (jsonObject.has("c_state_code")) {
                stateCode = jsonObject.get("c_state_code").getAsString();
                if (helper.isEmpty(stateCode)){
                    throw new InvalidRequestException("c_state_code","Can't be empty");
                }
            }
            else
                throw new InvalidRequestException("key missing", Messages.INVALID_REQUEST);

            JsonArray list = filterTransaction.topMostOrderSeller(searchBO,stateCode);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            /*responseBO.setTotal(filterTransaction.topMostOrderSellerCount(searchBO,stateCode));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);*/
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/moleculeMfc", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> moleculeMfc(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/moleculeMfc " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.moleculeMfc(searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            /*responseBO.setTotal(filterTransaction.moleculeMfcCount( searchBO));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);*/
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/moleculeBrand", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> moleculeBrand(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/moleculeBrand " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.moleculeBrand(searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            /*responseBO.setTotal(filterTransaction.moleculeBrandCount( searchBO));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);*/
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/moleculeSeller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> moleculeSeller(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/moleculeSeller " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.moleculeSeller(searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            /*responseBO.setTotal(filterTransaction.moleculeSellerCount( searchBO));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);*/
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/productFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> productFilter(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/productFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ItemsSearchBO searchBO = helper.fromJson(payload, ItemsSearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.productFilter(header, searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
           // data.addProperty("n_total",filterTransaction.productFilterCount(header, searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/productMfc", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> productMfc(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/productMfc " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.productMfc(searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            /*responseBO.setTotal(filterTransaction.productMfcCount( searchBO));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);*/
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/productBrand", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> productBrand(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/productBrand " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.productBrand(searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            /*responseBO.setTotal(filterTransaction.productBrandCount( searchBO));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);*/
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/productSeller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> productSeller(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/productSeller " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.productSeller(searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            /*responseBO.setTotal(filterTransaction.productSellerCount( searchBO));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);*/
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/categoryFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> categoryFilter(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/categoryFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ItemsSearchBO searchBO = helper.fromJson(payload, ItemsSearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.categoryFilter(header, searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            //data.addProperty("n_total",filterTransaction.categoryFilterCount(header, searchBO));
            /*responseBO.setTotal(filterTransaction.categoryFilterCount(header, searchBO));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);*/
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/category/manufacturersFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> manufacturersFilter(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/category/manufacturersFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.categoryMfc(searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            /*responseBO.setTotal(filterTransaction.categoryMfcCount(searchBO));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);*/
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/categoryBrandFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> categoryBrand(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/categoryBrandFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.categoryBrand(searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            /*responseBO.setTotal(filterTransaction.categoryBrandCount( searchBO));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);*/
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/categorySellerFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> categorySeller(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/categorySellerFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.categorySeller(searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            /*responseBO.setTotal(filterTransaction.categorySellerCount( searchBO));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);*/
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
    
}
