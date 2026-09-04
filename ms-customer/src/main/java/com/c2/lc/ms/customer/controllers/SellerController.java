package com.c2.lc.ms.customer.controllers;


import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.NextPageBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.bos.*;
import com.c2.lc.ms.customer.entities.comm.LcUserType;
import com.c2.lc.ms.customer.messages.FirmMessage;
import com.c2.lc.ms.customer.transactions.interfaces.SellerTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"${api.base.path}/seller"})
public class SellerController extends LoBaseController {


    @Value("${seller.new.launch.days}")
    private Long sellerNewLaunchDays;

    @Autowired private SellerTransaction sellerTransaction;

    /**
     * API: Unmapped seller list paginated
     * Description: Returns a page of unmapped sellers
     * Created By: kumar.arnav@c2info.com
     * Reviewed By:
     */
    @PostMapping(value = "/list", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> getUnmappedSellerList(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/seller/list");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            List<SellerDetailBO> sellerDetailBO = sellerTransaction.fetchUnmappedSellers(lcHeaderBO, searchBO);

            UnmappedSellersBo unmappedSellersBo = new UnmappedSellersBo();
            NextPageBO nextPageBO = new NextPageBO();
            nextPageBO.setPage(searchBO.getPage() + 1);
            //nextPageBO.setTotal(sellerTransaction.getUnmappedCount(lcHeaderBO,searchBO));
            unmappedSellersBo.setSellerDetails(sellerDetailBO);
            unmappedSellersBo.setNextPage(nextPageBO);

            this.setJsonPayload(apiResponse, helper.toJsonObjectTree(unmappedSellersBo, UnmappedSellersBo.class));
            this.addMessage(apiResponse, "Success");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }


    //Unmapped Search By Name
    @PostMapping(value = "/search/unmappedseller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> getUnmappedSellerSearch(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/seller/search/unmappedseller");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject json = helper.fromJson(payload, JsonObject.class);

            PageBO pageBo = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBo);

            String searchString = json.get("c_name").getAsString();
            if (helper.isEmpty(searchString) || searchString.length() < 3) {
                throw new InvalidRequestException("", "'c_name' should be minimum of 3 characters!");
            }
            List<SellerDetailBO> sellerDetailBO = sellerTransaction.unmappedSellersSearch(searchString, lcHeaderBO, pageBo);

            UnmappedSellersBo unmappedSellersBo = new UnmappedSellersBo();
            NextPageBO nextPageBO = new NextPageBO();
            nextPageBO.setPage(pageBo.getPage() + 1);
           // nextPageBO.setTotal(sellerTransaction.getUnmappedCountByName(searchString, lcHeaderBO));
            unmappedSellersBo.setSellerDetails(sellerDetailBO);
            unmappedSellersBo.setNextPage(nextPageBO);

            this.setJsonPayload(apiResponse, helper.toJsonObjectTree(unmappedSellersBo, UnmappedSellersBo.class));
            this.addMessage(apiResponse, "Success");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    //Unmapped Search By City,Area,State
    @PostMapping(value = "/search/unmappedseller/cas", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> getUnmappedSellerSearchByCity(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/seller/search/unmappedseller/city");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject json = helper.fromJson(payload, JsonObject.class);
            PageBO pageBo = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBo);

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
            List<SellerDetailBO> sellerDetailBO = sellerTransaction.unmappedSellersSearchByCity(searchCity, searchState, searchArea, lcHeaderBO, pageBo);

            UnmappedSellersBo unmappedSellersBo = new UnmappedSellersBo();
            NextPageBO nextPageBO = new NextPageBO();
            nextPageBO.setPage(pageBo.getPage() + 1);
            //nextPageBO.setTotal(sellerTransaction.getUnmappedSearchCount(searchCity, searchState, searchArea, lcHeaderBO));
            unmappedSellersBo.setSellerDetails(sellerDetailBO);
            unmappedSellersBo.setNextPage(nextPageBO);

            this.setJsonPayload(apiResponse, helper.toJsonObjectTree(unmappedSellersBo, UnmappedSellersBo.class));
            this.addMessage(apiResponse, "Success");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    //Mapped Search
    @PostMapping(value = "/search/mappedseller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> getMappedSellerSearch(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/seller/search/mappedseller");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject json = helper.fromJson(payload, JsonObject.class);
            PageBO pageBo = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBo);

            if (!json.has("c_mobile_no"))
                throw new InvalidRequestException("'c_mobile_no'", "Missing..!");
            else {
                if (helper.isEmpty(json.get("c_mobile_no").getAsString()) || (json.get("c_mobile_no").getAsString()).length() < 10) {
                    throw new InvalidRequestException(FirmMessage.INVALIDATE_MOBILE_LENGTH, "Mobile Number should be in length of 10-12");
                }
            }
            List<JsonObject> sellerDetailBO = sellerTransaction.mappedSellersSearch(json, lcHeaderBO, pageBo);

            MappedSellersBo mappedSellersBo = new MappedSellersBo();
            NextPageBO nextPageBO = new NextPageBO();
            nextPageBO.setPage(pageBo.getPage() + 1);
           // nextPageBO.setTotal(sellerTransaction.getMappedSearchCount(json, lcHeaderBO));
            mappedSellersBo.setSellerDetails(sellerDetailBO);
            mappedSellersBo.setNextPage(nextPageBO);

            this.setJsonPayload(apiResponse, helper.toJsonObjectTree(mappedSellersBo, MappedSellersBo.class));
            this.addMessage(apiResponse, "Success");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API: Send request to seller
     */
    @PostMapping(value = "req/to/seller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> sendReqToSeller(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/seller/req/to/seller");
        try {
            JsonObject json = helper.getJsonObject(payload);
            JsonArray fetchResults = sellerTransaction.sendReqToSeller();
            this.setJsonPayload(apiResponse, fetchResults);
            this.addMessage(apiResponse, "");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API: Search using filter
     */
    @PostMapping(value = "fetch/with/filters", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> fetchWithFilters(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/seller/fetch/with/filters");
        try {
            JsonObject json = helper.getJsonObject(payload);
            JsonArray fetchResults = sellerTransaction.fetchWithFilters();
            this.setJsonPayload(apiResponse, fetchResults);
            this.addMessage(apiResponse, "");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API: Mapped seller search
     */
    @PostMapping(value = "/mapped/seller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> fetchMappedSeller(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/seller/mapped/seller");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            PageBO pageBo = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBo);
            JsonObject jsonObject = helper.fromJSON(payload, JsonObject.class);
            if(!jsonObject.has("c_mobile_no"))
                throw new InvalidRequestException("'c_mobile_no'", "Missing..!");
            if (jsonObject.has("c_mobile_no") && helper.isEmpty(jsonObject.get("c_mobile_no").getAsString()))
                throw new InvalidRequestException("'c_mobile_no'", "can't be empty..!");

            List<JsonObject> sellerDetailBO = sellerTransaction.fetchMappedSellers(lcHeaderBO, pageBo,
                    jsonObject.get("c_mobile_no").getAsString());

            MappedSellersBo mappedSellersBo = new MappedSellersBo();
            NextPageBO nextPageBO = new NextPageBO();
            nextPageBO.setPage(pageBo.getPage() + 1);
            //nextPageBO.setTotal(sellerTransaction.getCount(lcHeaderBO,  jsonObject.get("c_mobile_no").getAsString()));
            mappedSellersBo.setSellerDetails(sellerDetailBO);
            mappedSellersBo.setNextPage(nextPageBO);

            this.setJsonPayload(apiResponse, helper.toJsonObjectTree(mappedSellersBo, MappedSellersBo.class));
            this.addMessage(apiResponse, "Success");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API: Update priority
     */
    @PostMapping(value = "/update/priority", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> updatePriority(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/seller/update/priority");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            SellerPriorityBo sellerPriorityBo = helper.fromJson(payload, SellerPriorityBo.class);
            this.validateInputPayload(sellerPriorityBo);

            sellerTransaction.updatePriority(lcHeaderBO, sellerPriorityBo);

            this.addMessage(apiResponse, "Successfully updated..!");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API: Fetch sellers
     */
    @PostMapping(value = "/getsellerinfo", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> getSellerInfo(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/seller/getsellerinfo");
        try {
            JsonObject json = helper.getJsonObject(payload);
            SellerDetailBO sellerDetailBO = sellerTransaction.getSellerInfo(json.get("c_seller_code").getAsString());
            this.setDataJsonObjectPayload(apiResponse, helper.toJsonObjectTree(sellerDetailBO, SellerDetailBO.class));
            //this.setJsonPayload(apiResponse, helper.toJsonObjectTree(sellerDetailBO, SellerDetailBO.class));
            this.addMessage(apiResponse, "Success");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API: Add seller
     */
    @PostMapping(value = "/create/seller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> createSeller(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/seller/create/seller");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            JsonObject json = helper.getJsonObject(payload);
            SellerCreationBO sellerCreationBO = helper.fromJson(json, SellerCreationBO.class);

            sellerTransaction.createSeller(sellerCreationBO);
            this.addMessage(apiResponse, "Saved Successfully!");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/getCustCode", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> getCustCode(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/seller/update/priority");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            PageBO pageBO = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBO);

            JsonArray list = sellerTransaction.getCustCode(lcHeaderBO, pageBO);
            this.setDataJsonArrayPayload(apiResponse, list);
            this.addMessage(apiResponse, "Successfully updated..!");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/firm/seller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> getFirmSellers(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/seller/firm/seller");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            List<JsonObject> sellerDetailBO = sellerTransaction.getFirmSeller(lcHeaderBO);
            JsonArray list = (JsonArray) helper.getGson().toJsonTree(sellerDetailBO,
                    new TypeToken<List<JsonObject>>() {
                    }.getType());
            this.setDataJsonArrayPayload(apiResponse, list);
            this.addMessage(apiResponse, "Successfully updated..!");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

 /*   @PostMapping(value = "/confirmItems/itemMapping", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> confirmItemMapping(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/seller/confirmItems/itemMapping");
        try {
            JsonObject jsonObject = helper.fromJSON(payload, JsonObject.class);
            this.validateInputPayload(jsonObject);

            String itemUcode = "";
            if (jsonObject.has("c_item_uCode")) {
                itemUcode = jsonObject.get("c_item_uCode").getAsString();
                if (helper.isEmpty(itemUcode)) {
                    throw new InvalidRequestException("'c_item_uCode'", "Missing..!");
                }
            }
            JsonObject result = sellerTransaction.getItemMappingInfo(itemUcode);
            this.setDataJsonObjectPayload(apiResponse, result);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }*/


}
