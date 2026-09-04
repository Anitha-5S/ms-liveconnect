package com.c2.lc.ms.master.controllers;

import com.c2.lc.ms.master.controllers.base.MasterBaseController;
import com.c2.lc.ms.master.transactions.interfaces.NetmedsItemPushTransaction;
import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.ms.master.transactions.interfaces.NewStoreRegistrationTransaction;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Log4j2
@RestController
@CrossOrigin(origins = "*")
@RequestMapping( value = {"/nm", "${api.base.path}/nm"})
public class NetmedsController extends MasterBaseController {

    @Value("${api.base.path}")
    private String basePath;
    @Autowired
    private NetmedsItemPushTransaction netmedsItemPushTransaction;

    @Autowired
    private NewStoreRegistrationTransaction newStoreRegistrationTransaction;

    @PostMapping(value = "/items/push")
    public ResponseEntity<?> netmedsItemPush(@RequestParam(name = "date", required = false) String date, HttpServletRequest servletRequest, @RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse(servletRequest.getRequestURI());

        try {
            JsonObject response = netmedsItemPushTransaction.netmedsItemPush(date);
            this.setJsonPayload(apiResponse, response);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/items/pull")
    public ResponseEntity<?> netmedsItemPull(@RequestBody String payload, HttpServletRequest servletRequest, @RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse(servletRequest.getRequestURI());
        try {


        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/store/registration")
    public ResponseEntity<ApiResponse> storeRegistration() {
        ApiResponse apiResponse = this.initializeResponse("/nm/store/registration");
        try {
            newStoreRegistrationTransaction.storeRegistration();

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/store/status/detail/{storeCode}")
    public ResponseEntity<ApiResponse> storeRegistration(@PathVariable("storeCode") String storeCode) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/nm/store/status/detail/" + storeCode);
        try {
            JsonObject response = newStoreRegistrationTransaction.getStoreDetails(storeCode);

            this.setDataJsonObjectPayload(apiResponse, response);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }


}
