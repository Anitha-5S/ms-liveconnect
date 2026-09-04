package com.c2.lc.ms.master.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.transactions.interfaces.BlobStorageTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("${api.base.path}/blob")
public class BlobStorageController extends LoBaseController {

    @Value("${api.base.path}")
    private String basePath;
    @Autowired
    BlobStorageTransaction blobStorageTransaction;

    @PostMapping(value = "/upload", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_MULTIPART_FORM_DATA)
    public ResponseEntity<ApiResponse> upload(@RequestHeader Map<String, String> headers, @RequestPart MultipartFile[] img) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/blob/upload"+" ->"+headers.toString());
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonArray imgUrl = blobStorageTransaction.upload(header.getUserId(), header.getFirmId(), img);
            this.setDataJsonObjectPayload(apiResponse, helper.getJsonObject("c_path", imgUrl));
            this.addMessage(apiResponse, "Upload successful!");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/delete", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> imgDelete(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/blob/delete"+" ->"+headers.toString()+" ->"+payload);
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject json = helper.getJsonObject(payload);
            blobStorageTransaction.delete(header.getUserId(), header.getFirmId(), json.get("c_path"));
            this.addMessage(apiResponse, "Delete successful!");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

}
