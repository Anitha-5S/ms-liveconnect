package com.c2.lc.ms.master.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.ImageUpdateBo;
import com.c2.lc.ms.master.transactions.interfaces.ManufactureTransaction;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping( value = {"${api.base.path}/mfg"})
public class ManufacturerController extends LoBaseController {

    @Value("${api.base.path}")
    private String basePath;
    @Autowired
    private ManufactureTransaction manufactureTransaction;
    @PostMapping(value = "/upload/image", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_MULTIPART_FORM_DATA)
    public ResponseEntity<ApiResponse> updateImage(@RequestHeader Map<String, String> headers,@RequestPart MultipartFile[] images) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/mfg/upload/image "+" ->"+headers.toString());
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonArray imgUrl = manufactureTransaction.uploadMfgImage(images);

            this.setDataJsonObjectPayload(apiResponse, helper.getJsonObject("ac_mfg_images", imgUrl));
            this.addMessage(apiResponse, "Upload successful!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/image", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> updateImage(@RequestHeader Map<String, String> headers,@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath + "/mfg/update/image "+" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            ImageUpdateBo imageUpdateBo = helper.fromJson(payload, ImageUpdateBo.class);

            this.validateInputPayload(imageUpdateBo);
            manufactureTransaction.updateMfgImage(imageUpdateBo);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

}
