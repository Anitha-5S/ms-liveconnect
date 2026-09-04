package com.c2.lc.ms.customer.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.bos.PlayStoreDetailsBo;
import com.c2.lc.ms.customer.bos.UserProfileResponseBo;
import com.c2.lc.ms.customer.entities.customer.TSPlayStoreDetailsEntity;
import com.c2.lc.ms.customer.entities.customer.TSStoreRegisterEntity;
import com.c2.lc.ms.customer.transactions.interfaces.FirmTransaction;
import com.c2.lc.ms.customer.transactions.interfaces.FirmUserTransaction;
import com.c2.lc.ms.customer.transactions.interfaces.TouchStoreTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"${api.base.path}/b2c"})
public class TouchStoreController extends LoBaseController {

    @Autowired
    private TouchStoreTransaction touchStoreTransaction;
    @Autowired
    private FirmUserTransaction firmUserTransaction;
    @Autowired
    private FirmTransaction firmTransaction;

    @GetMapping(value = "/get/registered/store", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getRegisteredStore(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/get/registered/store");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);

            List<TSStoreRegisterEntity> tsStoreRegisterEntity = touchStoreTransaction.getRegisteredStores(headerBO);

            JsonArray result = (JsonArray) helper.getGson().toJsonTree(tsStoreRegisterEntity,
                    new TypeToken<List<TSStoreRegisterEntity>>() {
                    }.getType());
            this.setDataJsonArrayPayload(apiResponse, result);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 1.5.2
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     */
   @PostMapping(value = "/mobilenoaupdate", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> mobilenoUpdate(@RequestHeader Map<String, String> headers,@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/b2c/mobilenoaupdate");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            if(json.get("c_mobile_no").getAsString().isBlank()){
                throw new InvalidRequestException("c_mobile_no", "'c_mobile_no' can't be empty/blank");
            }else if(json.get("c_mobile_no").getAsString().length()!=10){
                throw new InvalidRequestException("c_mobile_no", "'c_mobile_no' length should be 10");
            }
            firmUserTransaction.mobilenoUpdate(headerBO.getUserId(),json.get("c_mobile_no").getAsString());

            this.addMessage(apiResponse, "Mobile Number Updated");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 1.5.1
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     */
    @GetMapping(value = "/profile")
    public ResponseEntity<ApiResponse> getProfile(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/b2c/profile");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);
            Long userId = headerBO.getUserId();
            UserProfileResponseBo userProfileResponseBo = firmUserTransaction.getProfile(userId);

            JsonObject response = (JsonObject) helper.getGson().toJsonTree(userProfileResponseBo,
                    new TypeToken<UserProfileResponseBo>() {
                    }.getType());

            this.setJsonPayload(apiResponse, response);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/save/recent/items", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> saveRecentItems(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/b2c/save/recent/items");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);

            firmUserTransaction.saveRecentItem(headerBO.getUserId(),json.get("c_item_code").getAsString());

            this.addMessage(apiResponse, "Items saved successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/clear/recent/items", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> clearRecentSearch(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/b2c/clear/recent/items");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);

            firmUserTransaction.clearRecentItem(headerBO.getUserId());

            this.addMessage(apiResponse, "Items cleared successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/get/recent/items", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getRecentSearch(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/b2c/get/recent/items");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);

            JsonObject result = firmUserTransaction.getRecentItem(headerBO.getUserId());

            this.setJsonPayload(apiResponse, result);
            this.addMessage(apiResponse, "Success!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/playstore/details/add", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> playstoreDetails(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/customer/list");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);
            PlayStoreDetailsBo playStoreDetailsBo = helper.fromJson(payload, PlayStoreDetailsBo.class);
            this.validateInputPayload(playStoreDetailsBo);

            touchStoreTransaction.savePsDetails(headerBO, playStoreDetailsBo);
            this.addMessage(apiResponse, "Success");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/playstore/details/update", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> playstoreDetailsUpdate(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/customer/list");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);
            PlayStoreDetailsBo playStoreDetailsBo = helper.fromJson(payload, PlayStoreDetailsBo.class);

            touchStoreTransaction.UpdatePsDetails(headerBO.getUserId(), playStoreDetailsBo);
            this.addMessage(apiResponse, "Success");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/playstore/details/retrieve", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> playStoreDetailsRetrieve(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/customer/list");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            TSPlayStoreDetailsEntity playStoreDetailsEntity = touchStoreTransaction.retrievePsDetails(jsonObject.get("c_application_id").getAsString());
            JsonObject res = helper.toJsonObjectTree(playStoreDetailsEntity, TSPlayStoreDetailsEntity.class);
            this.setDataJsonObjectPayload(apiResponse, res);
            this.addMessage(apiResponse, "Success");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 1.4.3
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/profile/image/update", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> ProfileIamgeUpdate(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/b2c/profile/image/update");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            if(json.has("c_profile_image_url")){
                if(json.get("c_profile_image_url").getAsString().isBlank()){
                    throw new InvalidRequestException("c_profile_image_url", "'c_profile_image_url' can't be empty/blank");
                }
            }
            else {
                throw new InvalidRequestException("c_profile_image_url", "c_profile_image_url field required");
            }
            firmUserTransaction.profielImageUpdate(headerBO.getUserId(),json.get("c_profile_image_url").getAsString());

            this.addMessage(apiResponse, "Success");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
}


