package com.c2.lc.ms.master.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.*;
import com.c2.lc.ms.master.controllers.base.MasterBaseController;
import com.c2.lc.ms.master.models.BannerModel;
import com.c2.lc.ms.master.models.TypeWiseBannerModel;
import com.c2.lc.ms.master.transactions.interfaces.*;
import com.c2.lc.ms.master.utils.MsMessages;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"/mst/b2c/web"})
public class TSAdminController extends MasterBaseController {

    @Autowired
    private BannerTransaction bannerTransaction;
    @Autowired
    private DealOfTheDayTransaction dealOfTheDayTransaction;
    @Autowired
    private SearchTransaction searchTransaction;

    /**
     * TOUCH STORE : Admin - Add banner
     * API ID : 3.4.1
     * Developer : sathya.narayan@c2info.com
     */
    @PostMapping(value = "/add/banner", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> addBanner(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("b2c/web/add/banner ->");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            BannerModel bannerModel = helper.fromJson(payload, BannerModel.class);
            bannerModel.setC2Code(lcHeaderBO.getC2Code());

            bannerTransaction.save(bannerModel);

            this.addMessage(apiResponse, "Banner Saved Successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * TOUCH STORE : Get Banner list based on c2code
     * API ID : 3.4.2
     * Developer : sathya.narayan@c2info.com
     */
    @PostMapping(value = "/get/banner", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getBannerList(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("b2c/web/get/banner ->");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            List<AdminBannerBo> bannerList = bannerTransaction.getBannersListTS(lcHeaderBO.getC2Code(), searchBO);

            JsonArray ret = helper.toJsonArrayTree(bannerList, new TypeToken<List<AdminBannerBo>>() {
            }.getType());

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("j_list", ret);
            jsonObject.addProperty("n_next_page", searchBO.getPage() + 1);
            this.setDataJsonObjectPayload(apiResponse, jsonObject);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * TOUCH STORE : Get Banner by ID
     * API ID : 3.4.3
     * Developer : sathya.narayan@c2info.com
     */
    @PostMapping(value = "/banner/detail", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getBanner(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("b2c/web/banner/detail ->");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            if (json.has("c_banner_id")) {
                    if (json.get("c_banner_id").getAsString().isEmpty() || json.get("c_banner_id").getAsString().isBlank()) {
                        throw new InvalidRequestException("c_banner_id", "'c_banner_id' can't be empty/blank");
                    }
            }

            AdminBannerBo banner = bannerTransaction.getBannerTS(json.get("c_banner_id").getAsString());

            this.setDataJsonObjectPayload(apiResponse, helper.toJsonObjectTree(banner, AdminBannerBo.class));

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * TOUCH STORE : Update Banner status by ID
     * API ID : 3.4.5
     * Developer : sathya.narayan@c2info.com
     */
    @PostMapping(value = "/update/banner/status", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> updateBannerStatus(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("b2c/web/update/banner/status ->");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            if (json.has("c_banner_id")) {
                if (json.get("c_banner_id").getAsString().isEmpty() || json.get("c_banner_id").getAsString().isBlank()) {
                    throw new InvalidRequestException("c_banner_id", "'c_banner_id' can't be empty/blank");
                }
            }

            int bannerStatus = json.get("n_banner_status").getAsInt();
            if (bannerStatus > 1 || bannerStatus < 0) {
                throw new InputPayloadException("'n_banner_status' should be 0 or 1");
            }

            bannerTransaction.updateBannerStatus(json.get("c_banner_id").getAsString(), bannerStatus);

            this.addMessage(apiResponse, "Updated successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * TOUCH STORE : Edit Banner by ID
     * API ID : 3.4.4
     * Developer : sathya.narayan@c2info.com
     */
    @PostMapping(value = "/update/banner", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> updateBanner(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("b2c/web/update/banner ->");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            AdminBannerBo bannerBo = helper.fromJson(payload, AdminBannerBo.class);
            this.validateInputPayload(bannerBo);

            bannerTransaction.updateBanner(bannerBo);

            this.addMessage(apiResponse, "Updated successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * TOUCH STORE : Admin - Add Deal item
     * API ID : 3.5.1
     * Developer : shobha.hs@c2info.com
     */
    @PostMapping(value = "/add/deal", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> addDeal(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/mst/b2c/web/add/deal"+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            DealOfTheDayBO dealOfTheDayBO = helper.fromJson(payload, DealOfTheDayBO.class);
            this.validateInputPayload(dealOfTheDayBO);
            dealOfTheDayTransaction.save(header, dealOfTheDayBO);
            this.addMessage(apiResponse, "Item Saved Successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     * API ID : 3.5.5
     */
    @PostMapping(value = "/update/deal/status", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> dealStatusUpdate(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/mst/b2c/web/update/deal/status ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            String c_deal_id = "";
            if (jsonObject.has("c_deal_id")) {
                c_deal_id = jsonObject.get("c_deal_id").getAsString();
                if (helper.isEmpty(c_deal_id)){
                    throw new InvalidRequestException("c_deal_id", "'c_coupon_id' can't be empty");
                }
            }
            else
                throw new InvalidRequestException("c_coupon_id key missing", Messages.INVALID_REQUEST);

            String c_deals_status = "" ;
            if (jsonObject.has("c_deals_status")) {
                c_deals_status = jsonObject.get("c_deals_status").getAsString();
                if (helper.isEmpty(c_deals_status)){
                    throw new InvalidRequestException("c_deals_status", "'c_deals_status' can't be empty");
                }else if(!(c_deals_status.equalsIgnoreCase("Y")||c_deals_status.equalsIgnoreCase("N"))){
                    throw new InvalidRequestException("c_deals_status", "Please enter a valid values in c_deals_status.");

                }
            }
            else
                throw new InvalidRequestException("c_coupon_status key missing", Messages.INVALID_REQUEST);

            dealOfTheDayTransaction.updateDealStatus(jsonObject, header);
            this.addMessage(apiResponse, "Success");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     * API ID : 3.5.4
     */
    @PostMapping(value = "/edit/deal", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> editDeal(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/mst/b2c/web/edit/deal"+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            DealOfTheDayBO dealOfTheDayBO = helper.fromJson(payload, DealOfTheDayBO.class);
            this.validateInputPayload(dealOfTheDayBO);

            dealOfTheDayTransaction.editDeal(header,dealOfTheDayBO);
            this.addMessage(apiResponse, "Deal Updated Successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     * API ID : 3.5.3
     */
    @PostMapping(value = "/single/deal", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> editCoupon(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/mst/b2c/web/single/deal"+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject jsonDeal = helper.fromJson(payload, JsonObject.class);
            DealOfTheDayBO result = dealOfTheDayTransaction.singleDeal(jsonDeal);
            this.setDataJsonObjectPayload(apiResponse, helper.toJsonObjectTree(result, DealOfTheDayBO.class));

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     * API ID : 3.5.2
     */
    @PostMapping(value = "/list/deal", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> listDeal(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/mst/b2c/web/list/deal"+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            DealSearchBo dealSearchBo = helper.fromJson(payload, DealSearchBo.class);
            this.validateInputPayload(dealSearchBo);

            List<DealOfTheDayBO> list = dealOfTheDayTransaction.fetchDeals(header.getC2Code(), dealSearchBo);

           /* JsonArray result = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<DealOfTheDayBO>>() {
                    }.getType());*/
            JsonObject data = new JsonObject();

            data.add("j_list",helper.toJsonArrayTree(list, new TypeToken<List<DealOfTheDayBO>>() {}.getType()));
            data.addProperty("n_next_page", dealSearchBo.getPage()+1);
            this.setDataJsonObjectPayload(apiResponse, data);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/list/deal/count", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> listDealCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/mst/b2c/web/list/deal/count");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            DealSearchBo dealSearchBo = helper.fromJson(payload, DealSearchBo.class);
            this.validateInputPayload(dealSearchBo);
            JsonObject data = new JsonObject();
            data.addProperty("n_total", dealOfTheDayTransaction.DealOfTheDayListCount(header.getC2Code(),dealSearchBo));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/checkstock", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> checkStock(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/mst/b2c/web/checkstock");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            //SearchBO searchBO = this.getValidatedSearchBO(payload);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            JsonArray j_item_codes = new JsonArray();
            if (jsonObject.has("j_item_code")) {
                j_item_codes = jsonObject.get("j_item_code").getAsJsonArray();
                if(j_item_codes.size()==0){
                    throw new InvalidRequestException("j_item_code", "'j_item_code' can't be empty");
                }
            }
            else
                throw new InvalidRequestException("j_item_code key missing", Messages.INVALID_REQUEST);


            List productStockList = new ArrayList();
            String c2Code = lcHeaderBO.getC2Code();
            JsonArray list = searchTransaction.checkProductsStock(j_item_codes,c2Code,lcHeaderBO.getBrCode());
            JsonObject data = new JsonObject();
            data.add("j_list", list);
           // data.addProperty("n_next_page", searchBO.getPage()+1);

//            saveTSSearchHistory(lcHeaderBO.getUserId(), searchBO.getSearchTerm(), c2Code);

            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * TOUCH STORE : Type wise banner list
     * API ID : 3.4.5
     * Developer : sathya.narayan@c2info.com
     */
    @PostMapping(value = "/typewise/banner", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> typeWiseBanner(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("b2c/web/typewise/banner ->");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            PageBO pageBO = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBO);
            JsonObject json = helper.fromJson(payload, JsonObject.class);

            if (!json.has("c_offer_type") || helper.isEmpty(json.get("c_offer_type").getAsString())) {
                throw new InputPayloadException("'c_offer_type' should not be empty!");
            }

            List<TypeWiseBannerModel> bannerList = bannerTransaction.getTypeWiseBanner(json.get("c_offer_type").getAsString(), pageBO);

            JsonArray ret = helper.toJsonArrayTree(bannerList, new TypeToken<List<TypeWiseBannerModel>>() {
            }.getType());

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("j_list", ret);
            jsonObject.addProperty("n_next_page", pageBO.getPage() + 1);
            this.setDataJsonObjectPayload(apiResponse, jsonObject);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * TOUCH STORE : Most viewed product (Multi Branch)
     * API ID : 3.1.6
     * Developer : anitalak.shan@c2info.com
     */
    @PostMapping(value = "/mostViewedPrds", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> mostViewedProducts(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/mst/b2c/web/mostViewedPrds"+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            MostViewedPrdsBO viewedPrdBO = helper.fromJson(payload, MostViewedPrdsBO.class);

            this.validateInputPayload(viewedPrdBO);
            JsonArray response = searchTransaction.mostViewedPrds(viewedPrdBO,header);

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("j_list", response);
            jsonObject.addProperty("n_next_page", viewedPrdBO.getPage() + 1);
            this.setDataJsonObjectPayload(apiResponse, jsonObject);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/count/mostViewedPrds", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> mostViewedProductsCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/mst/b2c/web/count/mostViewedPrds"+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            MostViewedPrdsBO viewedPrdBO = helper.fromJson(payload, MostViewedPrdsBO.class);

            this.validateInputPayload(viewedPrdBO);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("n_total", searchTransaction.mostViewedPrdsCount(viewedPrdBO,header));
            this.setDataJsonObjectPayload(apiResponse, jsonObject);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }



    /**
     * TOUCH STORE : SalesCount for Most viewed product (Multi Branch)
     * API ID : 3.1.6
     * Developer : anitalak.shan@c2info.com
     */
    @PostMapping(value = "/salesCount", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getSalesCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/mst/b2c/web/salesCount"+" ->"+payload);
        try {

            JsonObject inputJson = helper.fromJson(payload, JsonObject.class);

            this.validateInputPayload(inputJson);
            String response = searchTransaction.updateSalesCount(inputJson);

            if (response.equals(Constants.STATUS_YES))
                this.addMessage(apiResponse, "Success");
            else
                this.addMessage(apiResponse, "Failed");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
}
