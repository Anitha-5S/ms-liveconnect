package com.c2.lc.ms.master.controllers;

import com.c2.lc.ms.master.controllers.base.MasterBaseController;
import com.c2.lc.ms.master.transactions.interfaces.PincodeTransaction;
import com.c2.lc.ms.master.entities.mysql.CustPincodewiseC2codeEntity;
import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.utils.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping({"/lc/ms/mst/pin/l", "/mst/pin", "${api.base.path}/pin"})
public class PincodeController extends MasterBaseController {

    @Autowired
    PincodeTransaction pincodeTransaction;

    @GetMapping(value = "/{pinCode}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getStateByPincode(@PathVariable String pinCode) {
        ApiResponse apiResponse = this.initializeResponse("/lc/ms/mst/pin/l/" + pinCode);
        try {
            JsonArray list = pincodeTransaction.getStateByPincode(pinCode);

            JsonObject response = new JsonObject();
            response.add("data", list);
            this.setJsonPayload(apiResponse, response);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/code/{pinCode}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getByPincode(@PathVariable String pinCode) {
        ApiResponse apiResponse = this.initializeResponse("/mst/pin/code/" + pinCode);
        try {
            List<CustPincodewiseC2codeEntity> list = pincodeTransaction.getByPincode(pinCode);

            JsonArray ret = helper.toJsonArrayTree(list, new TypeToken<List<CustPincodewiseC2codeEntity>>() {
            }.getType());
            JsonObject response = new JsonObject();
            response.add("data", ret);
            this.setJsonPayload(apiResponse, response);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/c2/{c2Code}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getByC2code(@PathVariable String c2Code) {
        ApiResponse apiResponse = this.initializeResponse("/mst/pin/c2/" + c2Code);
        try {
            List<CustPincodewiseC2codeEntity> list = pincodeTransaction.getByC2code(c2Code);

            JsonArray ret = helper.toJsonArrayTree(list, new TypeToken<List<CustPincodewiseC2codeEntity>>() {
            }.getType());
            JsonObject response = new JsonObject();
            response.add("data", ret);
            this.setJsonPayload(apiResponse, response);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/state-code/{stateCode}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getByC2code(@PathVariable String stateCode, HttpServletRequest request) {
        ApiResponse apiResponse = this.initializeResponse(request.getRequestURI());
        try {
            JsonObject response = pincodeTransaction.getC2codeByStateCode(stateCode);
            this.setJsonPayload(apiResponse, response);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
}
