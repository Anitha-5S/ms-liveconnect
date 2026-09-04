package com.c2.lc.ms.master.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.*;
import com.c2.lc.ms.master.controllers.base.MasterBaseController;
import com.c2.lc.ms.master.utils.MsMessages;
import com.c2.lc.ms.master.entities.mysql.LoGstTypeEntity;
import com.c2.lc.ms.master.models.*;
import com.c2.lc.ms.master.transactions.interfaces.*;
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
@RequestMapping(value = {"${api.base.path}/g"})
public class GeneralController extends MasterBaseController {

    @Value("${api.base.path}")
    private String basePath;
    @Autowired
    private BannerTransaction bannerTransaction;
    @Autowired
    private GeneralTransaction generalTransaction;
    @Autowired
    private CategoryTransaction categoryTransaction;
    @Autowired
    private RoadBlockTransaction roadBlockTransaction;
    @Autowired
    private AdvertisementTransaction advertisementTransaction;

    /**
     * API Id : 3.0.1
     * Developer : srutiarya.panda@c2info.com
     * Reviewed By : selva.sk@c2info.com
     */
    @GetMapping(value = "/state", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> getStateList(@RequestHeader Map<String, String> header) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/state"+" ->"+header.toString());
        try {
            List<MasterModel> list = generalTransaction.getStateList();

            JsonArray det = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<MasterModel>>() {
                    }.getType());

            this.setDataJsonArrayPayload(apiResponse, det);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 3.0.2
     * Developer : srutiarya.panda@c2info.com
     * Reviewed By : selva.sk@c2info.com
     */
    @GetMapping(value = "/city/{code}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> getCityList(@PathVariable("code") String code) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "g/city/{code}" + code);
        try {
            List<MasterModel> list = generalTransaction.getCityList(code);

            JsonArray det = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<MasterModel>>() {
                    }.getType());

            this.setDataJsonArrayPayload(apiResponse, det);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 3.0.3
     * Developer : srutiarya.panda@c2info.com
     * Reviewed By : selva.sk@c2info.com
     */
    @GetMapping(value = "/area/{code}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> getAreaList(@PathVariable("code") String code) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "g/area/{code}" + code);
        try {
            List<MasterModel> list = generalTransaction.getAreaList(code);

            JsonArray det = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<MasterModel>>() {
                    }.getType());

            this.setDataJsonArrayPayload(apiResponse, det);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/gst", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getGstTypeList() {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/gst");
        try {

            List<LoGstTypeEntity> gstType = generalTransaction.getGstTypeList();

            JsonArray list = (JsonArray) helper.getGson().toJsonTree(gstType,
                    new TypeToken<List<LoGstTypeEntity>>() {
                    }.getType());

            this.setDataJsonArrayPayload(apiResponse, list);

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

    @PostMapping(value = "/banner", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> saveBannerDetails(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/banner" +" ->"+payload);
        try {
            BannerModel bannerModel = helper.fromJson(payload, BannerModel.class);
            this.validateInputPayload(bannerModel);

            bannerTransaction.saveBanner(bannerModel);

            this.addMessage(apiResponse, "Banner saved successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 3.2.2
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By :
     */

    @PostMapping(value = "/getBanner", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getBannerDetails(@RequestHeader Map<String, String> headers,@RequestBody String payload ) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/banner"+" ->"+headers.toString());
        try {

            JsonObject obj = helper.fromJSON(payload, JsonObject.class);
            String type = "";
            if (obj.has("c_platform") && !helper.isEmpty(obj.get("c_platform").getAsString())){
                type = obj.get("c_platform").getAsString();
            }
            else
                throw new InvalidRequestException("","'c_platform' value can't be empty");

            List<BannerResponseBo> bannerModelList = bannerTransaction.getBannersList(type);

            JsonArray ret = helper.toJsonArrayTree(bannerModelList, new TypeToken<List<BannerListBo>>() {
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
     * API Id :
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By :
     */
    @GetMapping(value = "/banner/{bannerId}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getBannerById(@PathVariable("bannerId") String bannerId) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/banner/{bannerId}" + bannerId);
        try {
            BannerModel bannerModel = bannerTransaction.getByBannerId(bannerId);
            JsonObject ret = helper.toJsonObjectTree(bannerModel, BannerModel.class);
            this.setJsonPayload(apiResponse, ret);

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
    @DeleteMapping(value = "/banner/{bannerId}", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteBanner(@PathVariable("bannerId") String bannerId) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/banner/{bannerId}" + bannerId);
        try {

            bannerTransaction.deleteBanner(bannerId);
            this.addMessage(apiResponse, MsMessages.BANNER_DELETE);

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

    @PostMapping(value = "/roadBlock", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> saveRoadBlock(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/roadBlock"+" ->"+payload);
        try {

            RoadBlock roadBlock = helper.fromJson(payload, RoadBlock.class);
            this.validateInputPayload(roadBlock);
            roadBlockTransaction.saveRoadBlock(roadBlock);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

   /* *//**
     * API Id : 3.2.8
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By :
     *//*
    @GetMapping(value = "/roadBlock", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getRoadBlock(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/roadBlock" + headers.toString());
        try {

            List<RoadBlockResponse> roadBlocks = roadBlockTransaction.getRoadBlock();
            JsonArray ret = helper.toJsonArrayTree(roadBlocks, new TypeToken<List<BannerListBo>>() {
            }.getType());

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("j_list", ret);
            this.setDataJsonObjectPayload(apiResponse, jsonObject);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }*/

    /**
     * API Id :
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By :
     */
    @GetMapping(value = "/roadBlock/{roadBlockId}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getRoadBlockId(@PathVariable("roadBlockId") String roadBlockId) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/roadBlock/{roadBlockId}" + roadBlockId);
        try {

            RoadBlock roadBlock = roadBlockTransaction.getById(roadBlockId);
            JsonObject ret = helper.toJsonObjectTree(roadBlock, RoadBlock.class);
            this.setJsonPayload(apiResponse, ret);

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
    @DeleteMapping(value = "/roadBlock/{roadBlockId}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteRoadBlock(@PathVariable("roadBlockId") String roadBlockId) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/roadBlock/{roadBlockId}" + roadBlockId);
        try {

            roadBlockTransaction.deleteRoadBlock(roadBlockId);
            this.addMessage(apiResponse, MsMessages.ROAD_BLOCK_DELETE);

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

    @PostMapping(value = "/advertisement", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> saveAdvertisement(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/advertisement" + payload);
        try {

            Advertisement advertisement = helper.fromJson(payload, Advertisement.class);
            this.validateInputPayload(advertisement);
            advertisementTransaction.saveAdd(advertisement);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 3.2.8
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By :
     */
    @GetMapping(value = "/advertisement", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getAdvertisement(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/advertisement" + headers.toString());
        try {
            List<Advertisement> advertisements = advertisementTransaction.getAllAdd();
            JsonArray ret = helper.toJsonArrayTree(advertisements, new TypeToken<List<BannerListBo>>() {
            }.getType());

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("j_list", ret);
            this.setDataJsonObjectPayload(apiResponse, jsonObject);
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
    @GetMapping(value = "/advertisement/{advertisementId}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getByAdvertisementId(@PathVariable("advertisementId") String advertisementId) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/advertisement/{advertisementId}" + advertisementId);
        try {

            Advertisement advertisement = advertisementTransaction.getByAddId(advertisementId);
            JsonObject ret = helper.toJsonObjectTree(advertisement, Advertisement.class);
            this.setJsonPayload(apiResponse, ret);

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
    @DeleteMapping(value = "/advertisement/{advertisementId}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteAdvertisement(@PathVariable("advertisementId") String advertisementId) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/advertisement/{advertisementId}" + advertisementId);
        try {

            advertisementTransaction.deleteAdd(advertisementId);
            this.addMessage(apiResponse, MsMessages.ADD_DELETE);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 3.2.1
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By :
     */

    @GetMapping(path = "/category", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getAllCategory(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/category"+" ->"+headers.toString());
        try {
            List<Category> list = categoryTransaction.getAllCategory();

            JsonArray det = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<MasterModel>>() {
                    }.getType());

            this.setDataJsonArrayPayload(apiResponse, det);

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
    @PostMapping(path = "/category", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> saveCategory(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/category" + "->"+payload);
        try {

            Category category = helper.fromJson(payload, Category.class);
            this.validateInputPayload(category);
            categoryTransaction.saveCategory(category);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/state/search", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> stateSearch(@RequestHeader Map<String, String> header, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/g/state/search"+" ->"+header.toString()+" ->"+payload);
        try {

            JsonObject json = helper.fromJson(payload, JsonObject.class);
            String searchString ="";
            if (json.has("c_search_term")){
                searchString = json.get("c_search_term").getAsString();
                if (helper.isEmpty(searchString) || searchString.length() < 3) {
                    throw new InvalidRequestException("c_search_term", "Minimum search length should be 3 characters!");
                }
            }
            else
                throw new InvalidRequestException( "c_search_term", "Key not Found..!");

            List<MasterModel> list = generalTransaction.getStateSearch(searchString);
            JsonArray det = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<MasterModel>>() {
                    }.getType());
            this.setDataJsonArrayPayload(apiResponse, det);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/city/search", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> searchCity(@RequestHeader Map<String, String> header, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "g/city/search"+" ->"+header.toString()+" ->"+payload);
        try {
            JsonObject json = helper.fromJson(payload, JsonObject.class);

            String searchString = "";
            String cCode = "";
            if (json.has("c_search_term")&& json.has("c_code")) {
                 searchString = json.get("c_search_term").getAsString();
                 cCode = json.get("c_code").getAsString();
                if (helper.isEmpty(searchString) || searchString.length() < 3) {
                    throw new InvalidRequestException("c_search_term", "Minimum search length should be 3 characters!");
                }
                if (helper.isEmpty(cCode)) {
                    throw new InvalidRequestException("c_code", " 'c_code' can't be empty");

                }
            }
            else
                throw new InvalidRequestException("key missing",Messages.INVALID_REQUEST);

            List<MasterModel> list = generalTransaction.getCityListSearch(cCode, searchString);
            JsonArray det = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<MasterModel>>() {
                    }.getType());

            this.setDataJsonArrayPayload(apiResponse, det);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/area/search", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getAreaSearch(@RequestHeader Map<String, String> header, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "g/area/search"+" ->"+header.toString()+" ->"+payload);
        try {

            JsonObject json = helper.fromJson(payload, JsonObject.class);

            String searchString = "";
            String cCode = "";
            if (json.has("c_search_term")&& json.has("c_code")) {
                searchString = json.get("c_search_term").getAsString();
                cCode = json.get("c_code").getAsString();
                if (helper.isEmpty(searchString) || searchString.length() < 3) {
                    throw new InvalidRequestException("c_search_term", "Minimum search length should be 3 characters!");
                }
                if (helper.isEmpty(cCode)) {
                    throw new InvalidRequestException("c_code", " 'c_code' can't be empty");

                }
            }
            else
                throw new InvalidRequestException("key missing",Messages.INVALID_REQUEST);

            List<MasterModel> list = generalTransaction.getAreaSearch(cCode, searchString);
            JsonArray det = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<MasterModel>>() {
                    }.getType());
            this.setDataJsonArrayPayload(apiResponse, det);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }



}
