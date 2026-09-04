package com.c2.lc.ms.user.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.user.entities.FirmRoleEntity;
import com.c2.lc.ms.user.entities.FirmRoleLockEntity;
import com.c2.lc.ms.user.entities.UserFirmEntity;
import com.c2.lc.ms.user.transactions.interfaces.FirmRoleLockTransaction;
import com.c2.lc.ms.user.transactions.interfaces.FirmRoleTransaction;
import com.c2.lc.ms.user.transactions.interfaces.UserFirmTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"c2/lc/ms/user/na"})
@Slf4j
public class NonAuthController extends LoBaseController {

    @Autowired
    private FirmRoleTransaction roleTransaction;
    @Autowired private FirmRoleLockTransaction lockTransaction;
    @Autowired private UserFirmTransaction userFirmTransaction;

    @PostMapping(value = "/role", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> role(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/role");

        try {
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            roleTransaction.firmEventHub(jsonObject.get("data").getAsJsonArray());
            this.addMessage(apiResponse, "successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/role/lock", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> roleLock(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/role/lock");

        try {
            FirmRoleLockEntity firmRole = helper.fromJson(payload,  FirmRoleLockEntity.class);
            lockTransaction.save(firmRole);
            this.addMessage(apiResponse, "Role Locked successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/firm/user", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> firmUser(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/firm/user");

        try {
            UserFirmEntity firmRole = helper.fromJson(payload,  UserFirmEntity.class);
            userFirmTransaction.saveOrUpdate(firmRole);
            this.addMessage(apiResponse, "success..!!!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/firm/not/lock", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getNotLock(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/firm/not/lock");

        try {
            FirmRoleEntity firmRole = helper.fromJson(payload,  FirmRoleEntity.class);
            List<FirmRoleEntity> firmRoleEntities = roleTransaction.getNotInLock(firmRole.getCMobileNo(),
                    firmRole.getCC2Code(), firmRole.getCActCode());

            JsonArray list = (JsonArray) helper.getGson().toJsonTree(firmRoleEntities,
                    new TypeToken<List<FirmRoleEntity>>() {
                    }.getType());
            this.setDataJsonArrayPayload(apiResponse, list);
            this.addMessage(apiResponse, "success..!!!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/role/firm/update", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> firmUpdate(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/role/firm/update");

        try {
            JsonObject jsonObject = helper.fromJson(payload,  JsonObject.class);
            roleTransaction.updateFirmRole(jsonObject.get("data").getAsJsonArray());
            this.addMessage(apiResponse, "Firm update Success...!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/role/firm/lock", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> firmLock(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/role/firm/lock");

        try {
            JsonObject jsonObject = helper.fromJson(payload,  JsonObject.class);
            roleTransaction.lockFirm(jsonObject.get("data").getAsJsonArray());
            this.addMessage(apiResponse, "Firm update Success...!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }


}
