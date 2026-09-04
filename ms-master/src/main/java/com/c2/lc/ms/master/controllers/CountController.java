package com.c2.lc.ms.master.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.NextPageBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.*;
import com.c2.lc.ms.master.controllers.base.MasterBaseController;
import com.c2.lc.ms.master.entities.mongo.LcNotification;
import com.c2.lc.ms.master.transactions.interfaces.*;
import com.c2.lc.ms.master.utils.MsMessages;
import com.fasterxml.jackson.core.JsonProcessingException;
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
@RequestMapping({"/mst/count", "${api.base.path}/count"})
public class CountController extends MasterBaseController {

    @Value("${api.base.path}")
    private String basePath;
    @Autowired
    private SearchTransaction searchTransaction;
    @Autowired
    private CategoryTransaction categoryTransaction;
    @Autowired
    private RecentHistoryTransaction recentHistoryTransaction;
    @Autowired
    private SellerTransaction sellerTransaction;
    @Autowired
    private CatalogueTransaction catalogueTransaction;
    @Autowired
    private NotificationTransaction notificationTransaction;
    @Autowired
    OnePharmaTransaction onePharmaTransaction;
    @Autowired
    private FilterTransaction filterTransaction;
    @Autowired
    private ItemTransaction itemTransaction;
    @Autowired
    private BannerTransaction bannerTransaction;

    @Autowired private ExpireTransaction expireTransaction;

    @PostMapping(value = "/prd", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> searchByProductCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/count/prd" + " ->" + headers.toString() + " ->" + payload);
        try {
            SearchBO searchBO = this.getValidatedSearchBO(payload);

            long count = searchTransaction.countProduct(searchBO);
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, data);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/seller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> searchBySellerCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/count/seller" + " ->" + headers.toString() + " ->" + payload);
        try {
            SearchBO searchBO = this.getValidatedSearchBO(payload);

            long count = searchTransaction.countSeller(searchBO.getSearchTerm());
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, data);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/mfc", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> searchByManufactureCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/count/mfc" + " ->" + headers.toString() + " ->" + payload);
        try {
            SearchBO searchBO = this.getValidatedSearchBO(payload);

            long count = searchTransaction.countManufacture(searchBO.getSearchTerm());
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, data);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/getSellerByItemCode", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getSellerByItemCodeCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/count/getSellerByItemCode" + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            SearchBO searchBO = this.getValidatedSearchBO(payload);
            String seller = "";
            if (jsonObject.has("c_seller_code")) {
                seller = jsonObject.get("c_seller_code").getAsString();
            }

            long count = searchTransaction.getSellerOnProductCount(header, searchBO, seller);
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/mol/list", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getMoleculesCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/count/mol/list" + " ->" + headers.toString() + " ->" + payload);
        try {
            SearchBO searchBO = this.getValidatedSearchBO(payload);

            long count = searchTransaction.countMolecules(searchBO);
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, data);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(path = "/preferred", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getSellerPreferredCount(@RequestHeader Map<String, String> headers){
        ApiResponse apiResponse = this.initializeResponse(basePath + "/count/preferred" + " ->" + headers.toString() );

        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);

            JsonObject data = new JsonObject();
            data.addProperty("n_total", sellerTransaction.getSellerCount(headerBO.getFirmId()));

            this.setDataJsonObjectPayload(apiResponse, data);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/fetch-offers", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getOfferListCount(@RequestHeader Map<String, String> headers) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/count/fetch-offers" + " ->" + headers.toString() );

        try {
            JsonObject data = new JsonObject();
            data.addProperty("n_total", sellerTransaction.getOffersCount());

            this.setDataJsonObjectPayload(apiResponse, data);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/mfg", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getAllManufactureCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/count/mfg" + " ->" + headers.toString() + " ->" + payload);

        try {
            LcHeaderBO lcHeaderBO = getLcHeader(headers);
            PageBO pageBo = helper.fromJson(payload, PageBO.class);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            this.validateInputPayload(pageBo);

            JsonObject data = new JsonObject();
            data.addProperty("n_total", catalogueTransaction.manufactureCount(lcHeaderBO,jsonObject));

            this.setDataJsonObjectPayload(apiResponse, data);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/notification/list", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> notificationListCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "count/notification/list" + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            PageBO pageBO = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total", notificationTransaction.count(header));


            this.setDataJsonObjectPayload(apiResponse, data);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/invoice/list", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> invoiceListCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/count/invoice/list" + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            OnePharmaBo onePharmaBo = helper.fromJson(payload, OnePharmaBo.class);
            SearchBO searchBO = helper.fromJSON(payload, SearchBO.class);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            this.validateInputPayload(onePharmaBo);
//            String mobileNo = "";
//            if (json.has("c_mobile_no")) {
//                mobileNo = json.get("c_mobile_no").getAsString();
//                if (helper.isEmpty(mobileNo) || mobileNo.length() < 10) {
//                    throw new InvalidRequestException("c_mobile_no", "Mobile Number should be in length of 10-12");
//                }
//            } else
//                throw new InvalidRequestException("key missing", Messages.INVALID_REQUEST);

            JsonObject data = new JsonObject();
            data.addProperty("n_total", onePharmaTransaction.getInvoiceListCount(lcHeaderBO, onePharmaBo, searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/newLaunchFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> newLaunchFilterCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/count/newLaunchFilter " + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ItemsSearchBO searchBO = helper.fromJson(payload, ItemsSearchBO.class);
            this.validateInputPayload(searchBO);

            long count = filterTransaction.newLaunchCount(header, searchBO);
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/newLaunchMfc", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> newLaunchMfcCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/count/newLaunchMfc " + " ->" + headers.toString() + " ->" + payload);
        try {
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total", filterTransaction.newLaunchMfcCount(searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/newLaunchBrand", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> newLaunchBrandCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/count/newLaunchBrand " + " ->" + headers.toString() + " ->" + payload);
        try {
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total", filterTransaction.newLaunchBrandCount(searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/newLaunchSellers", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> newLaunchSellersCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/count/newLaunchSellers " + " ->" + headers.toString() + " ->" + payload);
        try {
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total", filterTransaction.newLaunchSellersCount(searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/topMostOrderFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> topMostOrderFilterCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/topMostOrderFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ItemsSearchBO searchBO = helper.fromJson(payload, ItemsSearchBO.class);
            this.validateInputPayload(searchBO);

            long count = filterTransaction.topMostOrderCount(header, searchBO);
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/topMostOrderMfc", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> topMostOrderMfcCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/topMostOrderMfc " +" ->"+headers.toString()+" ->"+payload);
        try {
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

            JsonObject data = new JsonObject();
            data.addProperty("n_total",filterTransaction.topMostOrderMfcCount(searchBO,stateCode));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/topMostOrderBrand", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> topMostOrderBrandCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/filter/l/topMostOrderBrand " +" ->"+headers.toString()+" ->"+payload);
        try {
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

            JsonObject data = new JsonObject();
            data.addProperty("n_total",filterTransaction.topMostOrderBrandCount(searchBO,stateCode));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/topMostOrderSeller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> topMostOrderSellerCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
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

            JsonObject data = new JsonObject();
            data.addProperty("n_total",filterTransaction.topMostOrderSellerCount(searchBO,stateCode));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/preferredSellerFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> preferredSellerFilterCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/preferredSellerFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ItemsSearchBO searchBO = helper.fromJson(payload, ItemsSearchBO.class);
            this.validateInputPayload(searchBO);

            long count = filterTransaction.preferredSellerCount(header, searchBO);
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
    @PostMapping(value = "/preferredSellerMfc", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getPreferredSellerMfcCount(@RequestHeader Map<String,String> headers,@RequestBody String payload){
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/preferredSellerMfc" +" ->"+headers.toString()+" ->"+payload);
        try {
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total",filterTransaction.preferredSellerMfcCount(searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/preferredSellerBrand", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> preferredSellerBrandCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/preferredSellerBrand" +" ->"+headers.toString()+" ->"+payload);
        try {
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total",filterTransaction.preferredSellerBrandCount(searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/shopByMfcFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> shopByMfcFilterCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/shopByMfcFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ItemsSearchBO searchBO = helper.fromJson(payload, ItemsSearchBO.class);
            this.validateInputPayload(searchBO);

            long count =  filterTransaction.shopByMfcFilterCount(header, searchBO);
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/shopByMfcBrand", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> shopByMfcBrandCount(@RequestHeader Map<String,String> headers,@RequestBody String payload){
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/shopByMfcBrand" +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total",filterTransaction.shopByMfcBrandCount(searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/shopByMfcSeller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> shopByMfcSellerCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/shopByMfcSeller " +" ->"+headers.toString()+" ->"+payload);
        try {
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total",filterTransaction.shopByMfcSellerCount(searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/productFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> productFilterCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/productFilter/count " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ItemsSearchBO searchBO = helper.fromJson(payload, ItemsSearchBO.class);
            this.validateInputPayload(searchBO);

            long count =  filterTransaction.productFilterCount(header, searchBO);
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/productMfc", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> productMfcCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/productMfc " +" ->"+headers.toString()+" ->"+payload);
        try {
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total", filterTransaction.productMfcCount(searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/productBrand", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> productBrandCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/productBrand " +" ->"+headers.toString()+" ->"+payload);
        try {
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total",filterTransaction.productBrandCount( searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/productSeller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> productSellerCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/productSeller " +" ->"+headers.toString()+" ->"+payload);
        try {
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total",filterTransaction.productSellerCount( searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/categoryFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> categoryFilterCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/categoryFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ItemsSearchBO searchBO = helper.fromJson(payload, ItemsSearchBO.class);
            this.validateInputPayload(searchBO);

            long count =  filterTransaction.categoryFilterCount(header, searchBO);
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/categoryMfc", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> categoryMfcCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/categoryMfc " +" ->"+headers.toString()+" ->"+payload);
        try {
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total", filterTransaction.categoryMfcCount(searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/categoryBrand", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> categoryBrandCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/categoryBrand " +" ->"+headers.toString()+" ->"+payload);
        try {
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = filterTransaction.categoryBrand(searchBO);
            JsonObject data = new JsonObject();
            data.addProperty("n_total",filterTransaction.categoryBrandCount( searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/categorySeller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> categorySellerCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/categorySellerFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total",filterTransaction.categorySellerCount( searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/moleculeFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> moleculeFilterCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/moleculeFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ItemsSearchBO searchBO = helper.fromJson(payload, ItemsSearchBO.class);
            this.validateInputPayload(searchBO);

            long count =  filterTransaction.moleculeFilterCount(header, searchBO);
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/moleculeMfc", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> moleculeMfcCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/moleculeMfc " +" ->"+headers.toString()+" ->"+payload);
        try {
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total",filterTransaction.moleculeMfcCount( searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/moleculeBrand", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> moleculeBrandCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/moleculeBrand " +" ->"+headers.toString()+" ->"+payload);
        try {
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total",filterTransaction.moleculeBrandCount( searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/moleculeSeller", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> moleculeSellerCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/count/moleculeSeller " +" ->"+headers.toString()+" ->"+payload);
        try {
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total",filterTransaction.moleculeSellerCount( searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/b2c/trending/categories", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> trendingCategoriesCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/b2c/trending/categories " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject data = new JsonObject();
            data.addProperty("n_total",categoryTransaction.getTrendingCount(lcHeaderBO.getC2Code()));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/b2c/trending/prd", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> trendingPrdCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/b2c/trending/prd " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject data = new JsonObject();
            data.addProperty("n_total",itemTransaction.getTrendingProdCount(lcHeaderBO.getC2Code()));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/b2c/productFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> prdFilterCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/b2c/productFilter " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total",filterTransaction.prdFilterCount(lcHeaderBO, searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/b2c/trending/prd/plp", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> trendingPrdPlpCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/b2c/trending/prd/plp " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject data = new JsonObject();
            data.addProperty("n_total",filterTransaction.trendingPrdPlpCount(lcHeaderBO, searchBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/b2c/banner", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> bannerCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/b2c/banner " +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("n_total", bannerTransaction.getAdminBannerCount(lcHeaderBO.getC2Code(), searchBO));
            this.setDataJsonObjectPayload(apiResponse, jsonObject);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/expire/order", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> expireOrder(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/expire/order" +" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            ExpiryOrderFilterBo inp = helper.fromJSON(payload, ExpiryOrderFilterBo.class);
            if (inp.getSellerCodes().size() == 0)
                throw new InputPayloadException("Seller code not found..!");

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("n_total", expireTransaction.getExpiryOrdersCount(inp, lcHeaderBO));
            this.setDataJsonObjectPayload(apiResponse, jsonObject);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
}