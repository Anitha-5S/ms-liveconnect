package com.c2.lc.ms.master.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.Manufacture;
import com.c2.lc.ms.master.bos.PlpBO;
import com.c2.lc.ms.master.transactions.interfaces.CatalogueTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"/c2/lc/ms/head", "${api.base.path}/head"})
public class HeaderController extends LoBaseController {

    @Value("${api.base.path}")
    private String basePath;
    @Autowired private CatalogueTransaction catalogueTransaction;

    /**
     * API Id : 3.2.6
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/mfg", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getAllManufacture(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/head/mfg"  + " ->" + headers.toString()+" ->"+payload);

        try {
            LcHeaderBO lcHeaderBO = getLcHeader(headers);
            PageBO pageBo = helper.fromJson(payload, PageBO.class);
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            this.validateInputPayload(pageBo);

            List<Manufacture> manufactures = catalogueTransaction.getManufacturerList(lcHeaderBO, pageBo,jsonObject);
            JsonArray list = (JsonArray) helper.getGson().toJsonTree(manufactures,
                    new TypeToken<List<Manufacture>>() {
                    }.getType());

            JsonObject data = new JsonObject();
            data.add("j_list", list);
            data.addProperty("n_next_page", pageBo.getPage() + 1);
           // responseBO.setTotal(catalogueTransaction.manufactureCount(lcHeaderBO));

            this.setDataJsonObjectPayload(apiResponse, data);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
}
