package com.c2.lc.ms.master.controllers;

import com.c2.lc.ms.master.transactions.interfaces.UrlConfigTransaction;
import com.c2.lc.ms.master.entities.mysql.UrlConfig;
import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.controller.BaseController;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"/mst", "${api.base.path}/urlConfig"})
public class UrlConfigController extends BaseController {

    @Value("${api.base.path}")
    private String basePath;
    @Autowired private UrlConfigTransaction urlConfigTransaction;

    @GetMapping("/{c_c2code}/{c_product_code}/{c_env}")
    public ResponseEntity<ApiResponse> getUrlConfig(@PathVariable String c_c2code, @PathVariable String c_product_code, @PathVariable String c_env) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/urlConfig/"+c_c2code+c_product_code+c_env);
        try {
            UrlConfig url  = urlConfigTransaction.getUrl(c_c2code, c_product_code, c_env);

            JsonObject response = new JsonObject();
            response.addProperty("url", url.getC_url());
            this.setJsonPayload(apiResponse, response);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
}