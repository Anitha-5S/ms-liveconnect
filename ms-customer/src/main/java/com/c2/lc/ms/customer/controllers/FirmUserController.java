package com.c2.lc.ms.customer.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.NextPageBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.bos.ListUserModelBO;
import com.c2.lc.ms.customer.bos.UserModelBO;
import com.c2.lc.ms.customer.bos.UserProfileResponseBo;
import com.c2.lc.ms.customer.bos.UsersListResponseBO;
import com.c2.lc.ms.customer.transactions.interfaces.FirmUserTransaction;
import com.c2.lc.ms.customer.transactions.interfaces.LcUserTransaction;
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
@RequestMapping(value = {"${api.base.path}/user"})
public class FirmUserController extends LoBaseController {

    @Autowired
    private FirmUserTransaction firmUserTransaction;
    @Autowired
    private LcUserTransaction lcUserTransaction;

    @PostMapping(value = "/add", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> createUser(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/user/add");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            UserModelBO userModelBO = helper.fromJson(payload, UserModelBO.class);
            this.validateInputPayload(userModelBO);

            if (firmUserTransaction.doesExistMobileNo(userModelBO.getCMobileNo())) {
//            if (firmUserTransaction.doesExistMobileNoForParent(userModelBO.getCMobileNo(), header.getFirmId())) {
                apiResponse.getMessages().add(Messages.ALREADY_REGISTERED_MOBILE);
            } else {
                firmUserTransaction.createUser(header.getUserId(), header.getBrCode(), header.getFirmId(), userModelBO);
                this.addMessage(apiResponse, "User added successfully!");
            }
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/update", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> updateUser(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/user/update");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            UserModelBO userModelBO = helper.fromJson(payload, UserModelBO.class);
            this.validateInputPayload(userModelBO);

            firmUserTransaction.updateUser(header.getUserId(), header.getFirmId(), userModelBO);
            this.addMessage(apiResponse, "User updated successfully!");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /*3.11.2 --> User List*/
    @PostMapping(value = "/list", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> getUserOnId(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/user/list");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            PageBO pageBo = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBo);

            List<ListUserModelBO> customerList = firmUserTransaction.getFirmUsers(header.getUserId(), header.getFirmId(), pageBo.getPage(), pageBo.getLimit());

            UsersListResponseBO usersListResponseBO = new UsersListResponseBO();
            NextPageBO nextPageBO = new NextPageBO();
            JsonArray list = (JsonArray) helper.getGson().toJsonTree(customerList,
                    new TypeToken<List<ListUserModelBO>>() {
                    }.getType());

            usersListResponseBO.setList(list);
            nextPageBO.setPage(pageBo.getPage() + 1);
           // nextPageBO.setTotal(firmUserTransaction.getCount(header.getFirmId(), header.getUserId()));
            usersListResponseBO.setNextPage(nextPageBO);
            this.setJsonPayload(apiResponse, helper.toJsonObjectTree(usersListResponseBO, UsersListResponseBO.class));

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/delete", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteUser(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/user/delete");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            JsonObject json = helper.getJsonObject(payload);

            if(json.has("c_user_id")){
               String c_user_id_val = json.get("c_user_id").getAsString();
               if(c_user_id_val != null && !c_user_id_val.trim().isBlank()){
                   firmUserTransaction.deleteFirmUser(json.get("c_user_id").getAsLong(), header.getFirmId(),header);
                   this.addMessage(apiResponse, "User Deleted Successfully!");
               }
               else{
                   this.addMessage(apiResponse, "c_user_id can not be empty");
               }
            }
            else
            {
                this.addMessage(apiResponse, "c_user_id is mandatory");
            }


        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /*3.11.3 --> Get User Details*/
    @PostMapping(value = "/detail")
    public ResponseEntity<ApiResponse> getUserDetail(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/user/detail");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            JsonObject json = helper.getJsonObject(payload);
            UserModelBO userModelBO = null;
            if(json.has("c_user_id") && !helper.isEmpty(json.get("c_user_id").getAsString())){
                userModelBO = firmUserTransaction.getUserDetail(helper.getLong(json.get("c_user_id")), header.getFirmId());
            } else
                throw new InputPayloadException("User id can't be null");

            JsonObject response = (JsonObject) helper.getGson().toJsonTree(userModelBO,
                    new TypeToken<UserModelBO>() {
                    }.getType());

            this.setJsonPayload(apiResponse, response);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }



    @PostMapping(value = "/getUser", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getUser(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/user/add");
        {
            try {
                LcHeaderBO header = this.getLcHeader(headers);
                JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
                this.validateInputPayload(payload);
                String mobileNo = "";
                if (jsonObject.has("c_mobile_no")) {
                    mobileNo = jsonObject.get("c_mobile_no").getAsString();
                    if (helper.isEmpty(mobileNo) || mobileNo.length() < 10) {
                        throw new InvalidRequestException("c_mobile_no", "Mobile Number should be in length of 10-12");
                    }
                } else
                    throw new InvalidRequestException("key missing", Messages.INVALID_REQUEST);

               firmUserTransaction.getUser(header, mobileNo);
                this.addMessage(apiResponse, "LC1 Users Imported Successfully!");

            } catch (Exception e) {
                this.handleAppExceptions(e, apiResponse);
            }
            return this.getResponseEntity(apiResponse);
        }
    }
}