package com.c2.lc.ms.master.controllers;

import com.c2.lc.ms.master.bos.MerchantOnBoardingBo;
import com.c2.lc.ms.master.controllers.base.MasterBaseController;
import com.c2.lc.ms.master.transactions.interfaces.CustomerCreationPinCodeTransaction;
import com.c2.lc.ms.master.transactions.interfaces.CustomerMasterTransaction;
import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.ms.master.transactions.interfaces.NewStoreRegistrationTransaction;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"/mst/eg", "/lc/ms/mst/customer", "${api.base.path}/customer"})
public class CustomerMasterController extends MasterBaseController {

    @Autowired CustomerMasterTransaction customerMasterTransaction;
    @Autowired NewStoreRegistrationTransaction newStoreRegistrationTransaction;
    @Autowired CustomerCreationPinCodeTransaction customerMasterPinCodeTransaction;

    @PostMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
    private ResponseEntity<ApiResponse> customerMaster(HttpServletRequest hsr, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(hsr.getRequestURI());
        try {
            JsonObject data = helper.getJsonObject(payload);
            customerMasterTransaction.customer(data.get("data").getAsJsonObject());
        }catch (Exception ex){
            this.handleAppExceptions(ex,apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/supplier",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
    private ResponseEntity<ApiResponse> supplierMaster(HttpServletRequest hsr, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(hsr.getRequestURI());
        try {
            JsonObject data = helper.getJsonObject(payload);
            customerMasterTransaction.supplier(data.get("data").getAsJsonObject());
        }catch (Exception ex){
            this.handleAppExceptions(ex,apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/branch",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
    private ResponseEntity<ApiResponse> branchMaster(HttpServletRequest hsr, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(hsr.getRequestURI());
        try {
            JsonObject data = helper.getJsonObject(payload);
            customerMasterTransaction.branch(data.get("data").getAsJsonObject());
        }catch (Exception ex){
            this.handleAppExceptions(ex,apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/create-map")
    private ResponseEntity<ApiResponse> customerCreationAndMapping(HttpServletRequest hsr) {
        ApiResponse apiResponse = this.initializeResponse(hsr.getRequestURI());
        try {
            customerMasterTransaction.customerCreationAndMapping();
        }catch (Exception ex){
            this.handleAppExceptions(ex,apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/eg-create")
    private ResponseEntity<ApiResponse> egCustomerCreation(HttpServletRequest hsr) {
        ApiResponse apiResponse = this.initializeResponse(hsr.getRequestURI());
        try {
            customerMasterTransaction.egCustomerCreationAndMapping();
        }catch (Exception ex){
            this.handleAppExceptions(ex,apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/nm-customer-creation")
    private ResponseEntity<ApiResponse> nmCustomerCreationPinCode(HttpServletRequest hsr) {
        ApiResponse apiResponse = this.initializeResponse(hsr.getRequestURI());
        try {
            customerMasterPinCodeTransaction.nmCustomerCreationPinCode();
        }catch (Exception ex){
            this.handleAppExceptions(ex,apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/eg-customer-creation")
    private ResponseEntity<ApiResponse> egCustomerCreationPinCode(HttpServletRequest hsr) {
        ApiResponse apiResponse = this.initializeResponse(hsr.getRequestURI());
        try {
            customerMasterPinCodeTransaction.egCustomerCreationPinCode();
        }catch (Exception ex){
            this.handleAppExceptions(ex,apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }


    @GetMapping(value = "/grab/store-registration-scheduler")
    private ResponseEntity<ApiResponse> merchantOnBoardingScheduler(HttpServletRequest hsr, @RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("/grab/store-registration-scheduler");
        try {
            newStoreRegistrationTransaction.registerMerchant(headers);
        } catch (Exception ex) {
            this.handleAppExceptions(ex, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }


    @PostMapping(path = "/grab/store-registration/status", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> merchantCreationStatus(HttpServletRequest hsr, @RequestHeader Map<String, String> headers,
                                                              @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/grab/store-registration/status");
        try {
            JsonObject data = helper.getJsonObject(payload);

            newStoreRegistrationTransaction.saveMerchantStatus(data);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/merchant-creation", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> NewMerchantCreation(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/grab/store-registration/status");
        try {
            MerchantOnBoardingBo merchantOnBoardingBo = helper.fromJSON(payload, MerchantOnBoardingBo.class);

            newStoreRegistrationTransaction.merchantCreation(merchantOnBoardingBo);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

}



