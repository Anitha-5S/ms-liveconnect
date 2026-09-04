package com.c2.lc.ms.master.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.*;
import com.c2.lc.ms.master.bos.customerbos.*;
import com.c2.lc.ms.master.entities.mongo.LcItem;
import com.c2.lc.ms.master.entities.mysql.UItemMstEntity;
import com.c2.lc.ms.master.models.ItemSellersList;
import com.c2.lc.ms.master.models.SellerListRequest;
import com.c2.lc.ms.master.transactions.interfaces.CatalogueTransaction;
import com.c2.lc.ms.master.transactions.interfaces.ItemTransaction;
import com.c2.lc.ms.master.transactions.interfaces.SearchTransaction;
import com.c2.lc.ms.master.transactions.interfaces.SellerTransaction;
import com.c2.lc.ms.master.utils.MsMessages;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.DateTimeException;
import java.util.List;
import java.util.Map;

//TODO - merge with master.

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"/mst/item", "/c2/lc/ms/item", "${api.base.path}/item"})
public class ItemController extends LoBaseController {

    @Value("${api.base.path}")
    private String basePath;
    @Autowired private ItemTransaction itemTransaction;
    @Autowired private SellerTransaction sellerTransaction;
    @Autowired private SearchTransaction searchTransaction;
    @Autowired private CatalogueTransaction catalogueTransaction;

    @Value("${new.launch.days}")
    private Long days;

/*    @GetMapping(value = "/dd/{itemCode}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getItemDD(@RequestHeader Map<String, String> headers, @PathVariable("itemCode") String itemCode) {
        ApiResponse apiResponse = this.initializeResponse("/c/lc/ms/mst/item/l/dd" + itemCode);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            PageBO pageBO = new PageBO();
            pageBO.setPage(0);
            pageBO.setLimit(10);
            JsonArray list = searchTransaction.getProductDetails(header, );

            this.setDataJsonArrayPayload(apiResponse, list);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }*/

    @GetMapping(value = "/{itemCode}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getItemMst(@PathVariable("itemCode") String itemCode) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/item/{itemCode}" + itemCode);
        try {
            UItemMstEntity cityModel = itemTransaction.findByItemCode(itemCode);

            JsonObject jsonObject = helper.getJsonObject(cityModel);

            JsonObject response = new JsonObject();
            response.add("data", jsonObject);
            this.setJsonPayload(apiResponse, response);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/cache/{itemCode}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> cacheItemCode(@PathVariable("itemCode") String itemCode) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/item/cache/{itemCode}" +" ->"+ itemCode);
        try {
            UItemMstEntity cityModel = itemTransaction.findByItemCode(itemCode);

            JsonObject jsonObject = helper.getJsonObject(cityModel);

            JsonObject response = new JsonObject();
            response.add("data", jsonObject);
            this.setJsonPayload(apiResponse, response);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    //this will give list of sellers having requested item in stock
    @PostMapping(value = "/seller", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getSellerListByItem(@RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse("/c/lc/ms//mst/item/seller"+" ->"+payload);
        try {
            //TODO get buyer code from header
            JsonObject json = helper.getJsonObject(payload);
            SellerListRequest sellerListRequest = helper.fromJson(json, SellerListRequest.class);
            List<ItemSellersList> sellerModel = sellerTransaction.getSellerDetailsByItem(sellerListRequest.getBuyerCode(), sellerListRequest.getItemUCode());

            JsonArray list = (JsonArray) helper.getGson().toJsonTree(sellerModel,
                    new TypeToken<List<ItemSellersList>>() {
                    }.getType());

            this.setDataJsonArrayPayload(apiResponse, list);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 3.2.4
     * Developer : srutiarya.panda@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/top", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getItemTop(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/item/top"+" ->"+headers.toString()+" ->"+payload);

        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            ItemsSearchBO searchBO = helper.fromJson(payload, ItemsSearchBO.class);
            this.validateInputPayload(searchBO);

            JsonArray list = itemTransaction.geTopMostOrderItems(lcHeaderBO,searchBO);
            PlpBO responseBO = new PlpBO();
            responseBO.setList(list);
            responseBO.setNextPage(searchBO.getPage() + 1);
          //  responseBO.setTotal( itemTransaction.getTopCount(lcHeaderBO.getFirmId()));
            JsonObject ret = helper.toJsonObjectTree(responseBO, PlpBO.class);

            this.setDataJsonObjectPayload(apiResponse, ret);
            this.addMessage(apiResponse, MsMessages.SUCCESS);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
    @PostMapping(value = "/newTop", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getItemTopNew(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/item/top" + " ->" + headers.toString() + " ->" + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            PageBO pageBO = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBO);

            List<ItemPLPResponseBO> list = itemTransaction.geTopMostOrderItem(lcHeaderBO, pageBO);
            JsonArray det = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<ItemPLPResponseBO>>() {
                    }.getType());
            this.setDataJsonArrayPayload(apiResponse, det);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/top/count",  produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getItemTopCount(@RequestHeader Map<String, String> headers) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/item/top/count"+" ->"+headers.toString());

        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            Integer count = itemTransaction.getTopCount(lcHeaderBO.getFirmId());
            JsonObject data = new JsonObject();
            data.addProperty("count", count);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }


    /**
     * API Id : 3.2.5
     * Developer : srutiarya.panda@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/new", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getNewLaunchedItem(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/item/new"+" ->"+headers.toString()+" ->"+payload);

        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);

            JsonObject jsonObject = helper.getJsonObject(payload);
            PageBO pageBO = helper.fromJson(jsonObject, PageBO.class);
            this.validateInputPayload(pageBO);

            JsonArray jsonArray = itemTransaction.getNewLaunched(pageBO, days, headerBO);
            JsonObject data = new JsonObject();
            data.add("j_list", jsonArray);
            data.addProperty("n_next_page", pageBO.getPage() + 1);
            //TODO check if needed for every page call
            //data.addProperty("n_total", itemTransaction.getMasterActiveItemCount());

            this.setDataJsonObjectPayload(apiResponse, data);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }

        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/count", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getItemCount(@RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/item/count"+" ->"+payload);
        try {

            Long masterCount = itemTransaction.getMasterActiveItemCount();

            JsonObject response = new JsonObject();
            response.addProperty("count", masterCount);

            this.setDataJsonObjectPayload(apiResponse, response);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }


    @PostMapping(value = "/create/stockiest", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> createCustItemMst(@RequestHeader Map<String, String> headers,
                                                         @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/item/create/stockiest"+" ->"+headers.toString()+" ->"+payload);
        try {
            StockiestBO stockiestPayload = helper.fromJson(payload, StockiestBO.class);
            String c2Code = stockiestPayload.getC2Code();
            String brCode = stockiestPayload.getBrCode();
            DataBO data = stockiestPayload.getData();
            itemTransaction.saveCustItemMst(c2Code, brCode, data);
        } catch (Exception ex) {
            this.handleAppExceptions(ex, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/conversion", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> itemConversion(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/item/conversion"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject bo = helper.fromJson(payload, JsonObject.class);
            String c2Code = helper.getString(bo.get("c2code"));
            String customerCode = helper.getString(bo.get("customerCode"));
            JsonArray data = bo.get("items").getAsJsonArray();
            JsonArray res = itemTransaction.fetchConvertedItemList(c2Code, customerCode, data);
            JsonObject response = new JsonObject();
            response.add("result", res);
            this.setJsonPayload(apiResponse, response);
        } catch (Exception ex) {
            this.handleAppExceptions(ex, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/pdp", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getItemById(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/item/pdp " +" ->"+headers.toString()+" ->"+payload);
        try {
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
                throw new InvalidRequestException("key missing", Messages.INVALID_REQUEST);

            ItemPDPResponseBO itemPDPResponseBO = itemTransaction.getById(itemCode, lcHeaderBO);
            JsonObject ret = helper.toJsonObjectTree(itemPDPResponseBO, ItemPDPResponseBO.class);
            this.setDataJsonObjectPayload(apiResponse, ret);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/barcode", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getByBarCode(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/item/barcode " + " ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);

            String barCode = "";
            if (json.has("c_bar_code")) {
                barCode = json.get("c_bar_code").getAsString();
                if (helper.isEmpty(barCode)){
                    throw new InvalidRequestException("c_bar_code", "'c_bar_code' can't be empty");
                }
            }
            else
                throw new InvalidRequestException("key missing", Messages.INVALID_REQUEST);

            ItemPDPResponseBO itemPDPResponseBO = itemTransaction.getByBarCode(barCode, lcHeaderBO);
            JsonObject ret = helper.toJsonObjectTree(itemPDPResponseBO, ItemPDPResponseBO.class);
            this.setDataJsonObjectPayload(apiResponse, ret);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/upload/image", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_MULTIPART_FORM_DATA)
    public ResponseEntity<ApiResponse> updateImage(@RequestHeader Map<String, String> headers, @RequestPart MultipartFile[] images) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/item/upload/image"+" ->"+headers.toString());
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonArray imgUrl = itemTransaction.uploadProductImage(images);

            this.setDataJsonObjectPayload(apiResponse, helper.getJsonObject("ac_item_images", imgUrl));
            this.addMessage(apiResponse, "Upload successful!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/image", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> updateImage(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/item/image"+" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ImageUpdateBo imageUpdateBo = helper.fromJson(payload, ImageUpdateBo.class);
            this.validateInputPayload(imageUpdateBo);

            itemTransaction.updateItemImage(imageUpdateBo);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/create/cust-pack", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> createCustPackMst(@RequestHeader Map<String, String> headers,
                                                         @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/item/create/cust-pack"+" ->"+headers.toString()+" ->"+payload);
        try {
            PackBO packBO = helper.fromJson(payload, PackBO.class);
            this.validateInputPayload(packBO);
            String c2Code = packBO.getC2Code();
            String brCode = packBO.getBrCode();
            PackDataBO data = packBO.getData();

            itemTransaction.saveCustPackMst(c2Code, brCode, data);
        }  catch (Exception ex) {
            this.handleAppExceptions(ex, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/create/cust-pack-type", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> createCustPackType(@RequestHeader Map<String, String> headers,
                                                          @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/item/create/cust-pack-type"+" ->"+headers.toString()+" ->"+payload);
        try {
            PackTypeBO packType = helper.fromJson(payload, PackTypeBO.class);
            this.validateInputPayload(packType);
            String c2Code = packType.getC2Code();
            String brCode = packType.getBrCode();
            PackTypeDataBO data = packType.getData();
            itemTransaction.saveCustPackTypeMst(c2Code, brCode, data);
        }  catch (Exception ex) {
            this.handleAppExceptions(ex, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/create/cust-mfac-mst", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> createCustMfacMst(@RequestHeader Map<String, String> headers,
                                                         @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/item/create/cust-mfac-mst"+" ->"+headers.toString()+" ->"+payload);
        try {
            MfacBO mFacbo = helper.fromJson(payload, MfacBO.class);
            this.validateInputPayload(mFacbo);
            String c2Code = mFacbo.getC2Code();
            String brCode = mFacbo.getBrCode();
            MfacDataBO data = mFacbo.getData();
            itemTransaction.saveCustMfacMst(c2Code, brCode, data);
        }  catch (Exception ex) {
            this.handleAppExceptions(ex, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/create/cust-brand-mst", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> createCustBrandMst(@RequestHeader Map<String, String> headers,
                                                          @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/item/create/cust-brand-mst"+" ->"+headers.toString()+" ->"+payload);
        try {
            BrandBO brandBo = helper.fromJson(payload, BrandBO.class);
            this.validateInputPayload(brandBo);
            String c2Code = brandBo.getC2Code();
            String brCode = brandBo.getBrCode();
            BrandDataBO data = brandBo.getData();
            itemTransaction.saveCustBrandMst(c2Code, brCode, data);
        }  catch (Exception ex) {
            this.handleAppExceptions(ex, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/create/cust-schedule-mst", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> createCustScheduleMst(@RequestHeader Map<String, String> headers,
                                                             @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/item/create/cust-schedule-mst"+" ->"+headers.toString()+" ->"+payload);
        try {
            ScheduleBO scheduleBO = helper.fromJson(payload, ScheduleBO.class);
            this.validateInputPayload(scheduleBO);
            String c2Code = scheduleBO.getC2Code();
            String brCode = scheduleBO.getBrCode();
            ScheduleDataBO data = scheduleBO.getData();
            itemTransaction.saveCustScheduleMst(c2Code, brCode, data);
        }  catch (Exception ex) {
            this.handleAppExceptions(ex, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/create/cust-cont-mst", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> createCustContMst(@RequestHeader Map<String, String> headers,
                                                         @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/item/create/cust-cont-mst"+" ->"+headers.toString()+" ->"+payload);
        try {
            ContBo contBO = helper.fromJson(payload, ContBo.class);
            this.validateInputPayload(contBO);
            String c2Code = contBO.getC2Code();
            String brCode = contBO.getBrCode();
            ContDataBO data = contBO.getData();
            itemTransaction.saveCustContMst(c2Code, brCode, data);
        }  catch (Exception ex) {
            this.handleAppExceptions(ex, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/create/cust-item-group-mst", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> createCustItemGroupMst(@RequestHeader Map<String, String> headers,
                                                              @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/item/create/cust-item-group-mst"+" ->"+headers.toString()+" ->"+payload);
        try {
            ItemGroupBO itemGroupBO = helper.fromJson(payload, ItemGroupBO.class);
            this.validateInputPayload(itemGroupBO);
            String c2Code = itemGroupBO.getC2Code();
            String brCode = itemGroupBO.getBrCode();
            ItemGroupDataBO data = itemGroupBO.getData();
            itemTransaction.saveCustItemGroupMst(c2Code, brCode, data);
        }  catch (Exception ex) {
            this.handleAppExceptions(ex, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/create/cust-item-category-mst", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> createCustItemCategoryMst(@RequestHeader Map<String, String> headers,
                                                                 @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/item/create/cust-item-category-mst"+" ->"+headers.toString()+" ->"+payload);
        try {
            ItemCategoryBO itemCategoryBO = helper.fromJson(payload, ItemCategoryBO.class);
            this.validateInputPayload(itemCategoryBO);
            String c2Code = itemCategoryBO.getC2Code();
            String brCode = itemCategoryBO.getBrCode();
            ItemCategoryDataBO data = itemCategoryBO.getData();
            itemTransaction.saveCustItemCategoryMst(c2Code, brCode, data);
        }  catch (Exception ex) {
            this.handleAppExceptions(ex, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/summary", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> summary(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/item/summary"+" ->"+headers.toString()+" ->"+payload);
        try {
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
                throw new InvalidRequestException("key missing", Messages.INVALID_REQUEST);

            ItemPDPResponseBO itemPDPResponseBO = itemTransaction.getItemSummary(itemCode, lcHeaderBO);
            JsonObject ret = helper.toJsonObjectTree(itemPDPResponseBO, ItemPDPResponseBO.class);
            this.setDataJsonObjectPayload(apiResponse, ret);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     *
     * TOUCH STORE : Trending Products API
     */
    @PostMapping(value = "/trending/product", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getTrendingProduct(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/item/top");

        try {
            PageBO pageBo = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBo);

            JsonObject obj = helper.fromJson(payload, JsonObject.class);
            String c2Code = obj.get("c_c2Code").getAsString();

            List<LcItem> list = itemTransaction.geTrendingItems(pageBo, c2Code);
            JsonObject data = new JsonObject();
            data.add("j_list", helper.toJsonArrayTree(list, new TypeToken<List<LcItem>>() {}.getType()));
            data.addProperty("n_next_page", pageBo.getPage()+1);
            data.addProperty("n_total", itemTransaction.getTrendingCount(c2Code));

            this.setDataJsonObjectPayload(apiResponse, data);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

}

