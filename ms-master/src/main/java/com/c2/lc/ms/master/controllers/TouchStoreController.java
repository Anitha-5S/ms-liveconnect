package com.c2.lc.ms.master.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.*;
import com.c2.lc.ms.master.controllers.base.MasterBaseController;
import com.c2.lc.ms.master.entities.mysql.LoGstTypeEntity;
import com.c2.lc.ms.master.models.*;
import com.c2.lc.ms.master.transactions.interfaces.*;
import com.c2.lc.ms.master.utils.MsMessages;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"/mst/b2c"})
public class TouchStoreController extends MasterBaseController {

    @Autowired
    private BannerTransaction bannerTransaction;
    @Autowired
    private CategoryTransaction categoryTransaction;
    @Autowired
    private ItemTransaction itemTransaction;
    @Autowired
    private SearchTransaction searchTransaction;
    @Autowired
    private FilterTransaction filterTransaction;
    @Autowired
    private DealOfTheDayTransaction dealOfTheDayTransaction;

    /**
     * API Id : 1.4.1
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/categories", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> categoryList(@RequestHeader Map<String, String> headers,@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/mst/b2c/categories"+" ->"+payload);
        try {
            //TODO fetch c2code from header.
            LcHeaderBO headerBO = this.getLcHeader(headers);
            String c2Code = headerBO.getC2Code();
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            JsonArray list = new JsonArray();
            JsonObject data = new JsonObject();
            if(jsonObject.has("c_search_term")){
                SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
                this.validateInputPayload(searchBO);
                 list = categoryTransaction.categoryList(c2Code,searchBO.getPage(),searchBO.getLimit(),searchBO.getSearchTerm());
                data.add("j_list", list);
                data.addProperty("n_next_page", searchBO.getPage() + 1);
            }else{
                PageBO pageBO = helper.fromJson(payload, PageBO.class);
                this.validateInputPayload(pageBO);
                list = categoryTransaction.categoryList(c2Code, pageBO.getPage(),pageBO.getLimit(),"");
                data.add("j_list", list);
                data.addProperty("n_next_page", pageBO.getPage() + 1);
                //  data.addProperty("n_total", categoryTransaction.countCategoryByC2Code(c2Code));
            }
            this.setJsonPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);


        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
    /**
     * TOUCH STORE : Get Banner list based on userId and applicationId
     * API ID : 1.2.1
     * Developer : sathya.narayan@c2info.com
     */
    @PostMapping(value = "/get/banner", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getBannerList(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("b2c/get/banner ->");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            List<AdminBannerBo> bannerModelList = bannerTransaction.getBanners(lcHeaderBO.getC2Code());

            JsonArray ret = helper.toJsonArrayTree(bannerModelList, new TypeToken<List<AdminBannerBo>>() {
            }.getType());

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("j_list", ret);
            this.setDataJsonObjectPayload(apiResponse, jsonObject);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * TOUCH STORE : Get Trending Categories
     * API ID : 1.6.1
     * Developer : sathya.narayan@c2info.com
     */
    @PostMapping(value = "/trending/categories", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getTrendingCategories(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/g/trending/categories");

        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            PageBO pageBo = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBo);

            List<JsonObject> list = categoryTransaction.geTrendingCategories(pageBo, lcHeaderBO.getC2Code());
            JsonObject data = new JsonObject();
            data.add("j_list", helper.toJsonArrayTree(list, new TypeToken<List<JsonObject>>() {}.getType()));
            data.addProperty("n_next_page", pageBo.getPage()+1);

            this.setDataJsonObjectPayload(apiResponse, data);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * TOUCH STORE : Get Trending Products
     * API ID : 1.6.2
     * Developer : sathya.narayan@c2info.com
     */
    @PostMapping(value = "/trending/products", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getTrendingProduct(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/g/trending/categories");

        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            PageBO pageBo = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBo);

            List<ItemPLPResponseBO> list = itemTransaction.getTrendingProducts(pageBo, lcHeaderBO);
            JsonObject data = new JsonObject();
            data.add("j_list", helper.toJsonArrayTree(list, new TypeToken<List<ItemPLPResponseBO>>() {}.getType()));
            data.addProperty("n_next_page", pageBo.getPage()+1);

            this.setDataJsonObjectPayload(apiResponse, data);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * TOUCH STORE: Recent Search API
     * API ID : 1.6.3
     * Developer : sathya.narayan@c2info.com
     */
    @PostMapping(value = "/recent/search", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> recentSearch(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${search.endpoint}/b2c/recent/search");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            PageBO pageBo = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBo);

            List<ItemPLPResponseBO> resultList = itemTransaction.getRecentSearch(pageBo, lcHeaderBO);
            JsonObject data = new JsonObject();
            data.add("j_list", helper.toJsonArrayTree(resultList, new TypeToken<List<ItemPLPResponseBO>>() {}.getType()));
            data.addProperty("n_next_page", pageBo.getPage()+1);

            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * TOUCH STORE: PDP API
     * API ID : 1.8.1
     * Developer : sathya.narayan@c2info.com
     */
    @PostMapping(value = "/pdp", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> pdp(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${search.endpoint}/b2c/pdp");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject json = helper.fromJson(payload, JsonObject.class);
            String itemCode = json.has("c_item_code") ? json.get("c_item_code").getAsString() : "";
            if (helper.isEmpty(itemCode)){
                throw new InvalidRequestException("c_item_code", "'c_item_code' can't be empty");
            }

            ItemPDPResponseBO itemPDPResponseBO = itemTransaction.getProductDetails(itemCode, lcHeaderBO);

            JsonObject ret = helper.toJsonObjectTree(itemPDPResponseBO, ItemPDPResponseBO.class);
            this.setDataJsonObjectPayload(apiResponse, ret);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 1.6.5
     * Developer : sathya.narayan@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/prd", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> prdSearch(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${search.endpoint}/prd");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            if(!json.has("c_search_term")){
                throw new InvalidRequestException("c_search_term", "'c_search_term' is mandatory");
            }
            SearchBO searchBO = this.getValidatedSearchBO(payload);

            String c2Code = lcHeaderBO.getC2Code();
            JsonArray list = searchTransaction.getProducts(lcHeaderBO.getUserId(), searchBO, c2Code);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage()+1);

//            saveTSSearchHistory(lcHeaderBO.getUserId(), searchBO.getSearchTerm(), c2Code);

            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * Developer : sathya.narayan@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/productFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> productFilter(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/b2c/productFilter");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            List<ItemPLPResponseBO> resultList = filterTransaction.prdFilter(header, searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", helper.toJsonArrayTree(resultList, new TypeToken<List<ItemPLPResponseBO>>() {}.getType()));
            data.addProperty("n_next_page", searchBO.getPage()+1);
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
     * API Id : 1.7.1
     */
    @PostMapping(value = "/catgorypdt", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> categoryWiseProducts(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/b2c/catgorypdt");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            List<ItemPLPResponseBO> resultList = itemTransaction.categoryWiseProducts(searchBO,header);
            JsonObject data = new JsonObject();
            data.add("j_list", helper.toJsonArrayTree(resultList, new TypeToken<List<ItemPLPResponseBO>>() {}.getType()));
            data.addProperty("n_next_page", searchBO.getPage()+1);
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
    @PostMapping(value = "/catgorypdt/count", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> categoryPdtCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/b2c/categoryPdt/count"+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            long count = itemTransaction.categoryWiseProductsCount(searchBO,header);
           // long count = resultList.size();
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);
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
     * API Id : 1.2.4
     */
    @PostMapping(value = "/dealoftheday", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> dealOfTheDay(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/mst/b2c/dealoftheday"+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject request = helper.fromJson(payload, JsonObject.class);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            List<ItemPLPResponseBO> resultList = dealOfTheDayTransaction.DealOfTheDayProducts(searchBO,header,request);
            JsonObject data = new JsonObject();
            data.add("j_list", helper.toJsonArrayTree(resultList, new TypeToken<List<ItemPLPResponseBO>>() {}.getType()));
            data.addProperty("n_next_page", searchBO.getPage()+1);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * Developer : sathya.narayan@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/recommended", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> RecommendByCart(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/recommended ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject request = helper.fromJson(payload, JsonObject.class);

            List<ItemPLPResponseBO> resultList = itemTransaction.recommendedByCart(header, request);
            JsonObject data = new JsonObject();
            data.add("j_list", helper.toJsonArrayTree(resultList, new TypeToken<List<ItemPLPResponseBO>>() {}.getType()));
            data.addProperty("n_next_page", request.get("n_page").getAsInt()+1);
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
     *
     */

        @PostMapping(path = "/categories/count", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
        public ResponseEntity<ApiResponse> categoryCount(@RequestHeader Map<String, String> headers,@RequestBody String payload) {
            ApiResponse apiResponse = this.initializeResponse("/mst/b2c/categories/count");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            JsonArray list = new JsonArray();
            JsonObject data = new JsonObject();
            if(jsonObject.has("c_search_term")){
                SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
                this.validateInputPayload(searchBO);
                data.addProperty("n_total", categoryTransaction.countCategoryByC2Code(headerBO, searchBO.getPage(),searchBO.getLimit(),searchBO.getSearchTerm()));

            }else{
                PageBO pageBO = helper.fromJson(payload, PageBO.class);
                this.validateInputPayload(pageBO);
                data.addProperty("n_total", categoryTransaction.countCategoryByC2Code(headerBO, pageBO.getPage(), pageBO.getLimit(), " "));
            }
            this.setJsonPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);
        }
        catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }


    /**
     * Developer : sathya.narayan@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/trending/products/plp", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getTrendingPrdPlp(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/trending/products/plp");

        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            List<ItemPLPResponseBO> list = filterTransaction.getTrendingPrdPlp(searchBO, lcHeaderBO);
            JsonObject data = new JsonObject();
            data.add("j_list", helper.toJsonArrayTree(list, new TypeToken<List<ItemPLPResponseBO>>() {}.getType()));
            data.addProperty("n_next_page", searchBO.getPage()+1);

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
    @PostMapping(value = "/dealOfTheDay/count", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> dealOfTheDayCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/mst/b2c/dealOfTheDay/count"+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject request = helper.fromJson(payload, JsonObject.class);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            long count = dealOfTheDayTransaction.DealOfTheDayCount(searchBO,header,request);
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/dealOfTheDayBrand", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> dealOfTheDayBrand(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/mst/b2c/dealOfTheDayBrand" + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.dealOfTheDayBrand(header.getC2Code(), searchBO, jsonObject);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/dealOfTheDayPrdForms", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> dealOfTheDayPrdForms(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/mst/b2c/dealOfTheDayPrdForms" + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.dealOfTheDayPrdForms(header.getC2Code(), searchBO, jsonObject);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/dealOfTheDayUses", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> dealOfTheDayUses(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/mst/b2c/dealOfTheDayUses" + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.dealOfTheDayUses(header.getC2Code(), searchBO, jsonObject);
            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
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
  /*  @PostMapping(value = "/recent/search/count", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> recentSearchCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${search.endpoint}/b2c/recent/search/count");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            PageBO pageBo = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBo);

            long count = itemTransaction.getRecentItemsCount(lcHeaderBO);
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }*/

}
