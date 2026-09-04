package com.c2.lc.ms.master.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.BuyerSellerBO;
import com.c2.lc.ms.master.bos.ImageUpdateBo;
import com.c2.lc.ms.master.bos.ItemsBO;
import com.c2.lc.ms.master.entities.mysql.LcOfferMstEntity;
import com.c2.lc.ms.master.entities.mysql.UStockiestItemEntity;
import com.c2.lc.ms.master.transactions.interfaces.CatalogueTransaction;
import com.c2.lc.ms.master.transactions.interfaces.SellerTransaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping( value = {"/c2/lc/ms/seller", "${api.base.path}/seller"})
public class SellerController extends LoBaseController {

    @Value("${api.base.path}")
    private String basePath;
    @Autowired private SellerTransaction sellerTransaction;
    @Autowired private CatalogueTransaction catalogueTransaction;

    @GetMapping(value = "/get-seller-details", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getSellerDetails(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/seller/get-seller-details"+" ->"+payload);

        BuyerSellerBO buyerSellerBo = helper.fromJson(payload, BuyerSellerBO.class);

        try {

            String sellerCode = buyerSellerBo.getC2Code();
            List<ItemsBO> list = buyerSellerBo.getItems();
            List<UStockiestItemEntity> data;
            JsonObject response = new JsonObject();
            JsonArray respItem = new JsonArray();
            for (ItemsBO item:list){
                respItem.add(sellerTransaction.getStockiestDetails(sellerCode,item.getCCode()));
            }
            response.add("data",respItem);
            this.setJsonPayload(apiResponse, response);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    //TODO add preferred sellers
    /**
     * API Id : 3.2.3
     * Developer : srutiarya.panda@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/preferred", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> getSellerPreferred(@RequestBody Object payload, @RequestHeader Map<String, String> headers) throws InvalidRequestException, JsonProcessingException {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/seller/preferred"+" ->"+headers.toString()+" ->"+payload);

        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);

            JsonObject jsonObject = helper.getJsonObject(payload);
            PageBO pageBO = helper.fromJson(jsonObject, PageBO.class);
            JsonArray jsonArray = sellerTransaction.getSellerPreferred(pageBO, headerBO);

            JsonObject data = new JsonObject();
            data.add("j_list", jsonArray);
            data.addProperty("n_next_page", pageBO.getPage() + 1);
           // data.addProperty("n_total", sellerTransaction.getSellerCount(headerBO.getFirmId()));

            this.setDataJsonObjectPayload(apiResponse, data);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 3.2.7
     * Developer : srutiarya.panda@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/fetch-offers", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> getOfferList(@RequestHeader Map<String, String> headers, @RequestBody Object payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/seller/fetch-offers"+" ->"+headers.toString()+" ->"+payload );

        JsonObject jsonObject = helper.getJsonObject(payload);
        PageBO pageBO = helper.fromJson(jsonObject, PageBO.class);
        try {

            JsonArray jsonArray = sellerTransaction.getLimitedOfferList(pageBO);

            JsonObject data = new JsonObject();
            data.add("j_list", jsonArray);
            data.addProperty("n_next_page", pageBO.getPage() + 1);
            //data.addProperty("n_total", sellerTransaction.getOffersCount());

            this.setDataJsonObjectPayload(apiResponse, data);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }


    /**
     * API Id : 3.2.7
     * Developer : srutiarya.panda@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/offers", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> createOffer(@RequestHeader Map<String, String> headers, @RequestBody Object payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/seller/offers"+" ->"+headers.toString()+" ->"+payload);

        try {
            JsonObject jsonObject = helper.getJsonObject(payload);
            LcOfferMstEntity offer = helper.fromJson(jsonObject, LcOfferMstEntity.class);
            validateInputPayload(offer);
            LcHeaderBO headerBO = this.getLcHeader(headers);
            offer.setNCreatedBy(headerBO.getUserId());
            offer.setNLastUpdatedBy(headerBO.getUserId());
            offer.setTCreatedAt(LocalDateTime.now());
            offer.setTLastUpdatedAt(offer.getTCreatedAt());
            offer.setStatus("I");

            JsonObject data = sellerTransaction.createOffer(offer);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, "Offer created!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 3.2.7
     * Developer : srutiarya.panda@c2info.com
     * Reviewed By :
     */
    @PutMapping(value = "/offers", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> updateOffer(@RequestHeader Map<String, String> headers, @RequestBody Object payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/seller/offers"+" ->"+headers.toString()+" ->"+payload);

        JsonObject jsonObject = helper.getJsonObject(payload);
        LcOfferMstEntity offer = helper.fromJson(jsonObject, LcOfferMstEntity.class);
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);
            offer.setNLastUpdatedBy(headerBO.getUserId());
            offer.setTLastUpdatedAt(offer.getTCreatedAt());

            JsonObject data = sellerTransaction.updateOffer(offer);
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, "Offer updated!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 3.2.7
     * Developer : srutiarya.panda@c2info.com
     * Reviewed By :
     */
    @DeleteMapping(value = "/offers", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> deleteOffer(@RequestHeader Map<String, String> headers, @RequestBody Object payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/seller/offers"+" ->"+headers.toString()+" ->"+payload);

        JsonObject jsonObject = helper.getJsonObject(payload);
        LcOfferMstEntity offer = helper.fromJson(jsonObject, LcOfferMstEntity.class);
        try {

            sellerTransaction.deleteOffer(offer);
            this.addMessage(apiResponse, "Offer deleted!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/upload/image", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_MULTIPART_FORM_DATA)
    public ResponseEntity<ApiResponse> updateImage(@RequestHeader Map<String, String> headers,@RequestPart MultipartFile[] images) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/seller/upload/image "+" ->"+headers.toString());
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonArray imgUrl = sellerTransaction.uploadSellerImage(images);
            this.setDataJsonObjectPayload(apiResponse, helper.getJsonObject("ac_mfg_images", imgUrl));
            this.addMessage(apiResponse, "Upload successful!");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
    @PostMapping(value = "/image", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> updateImage(@RequestHeader Map<String, String> headers,@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/seller/image"+" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ImageUpdateBo imageUpdateBo = helper.fromJson(payload, ImageUpdateBo.class);
            this.validateInputPayload(imageUpdateBo);
            sellerTransaction.updateSellerImage(imageUpdateBo);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
}
