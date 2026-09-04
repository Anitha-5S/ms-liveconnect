package com.c2.lc.ms.master.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.OnePharmaBo;
import com.c2.lc.ms.master.bos.PlpBO;
import com.c2.lc.ms.master.controllers.base.MasterBaseController;
import com.c2.lc.ms.master.entities.mysql.OnePharmaEmailsEntity;
import com.c2.lc.ms.master.transactions.interfaces.OnePharmaTransaction;
import com.c2.lc.ms.master.utils.MsMessages;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping( value = {"/mst/onePharma", "${api.base.path}/onePharma"})
public class OnePharmaController extends MasterBaseController {

    @Value("${api.base.path}")
    private String basePath;
    @Autowired
    OnePharmaTransaction onePharmaTransaction;

    @PostMapping(value = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> insert(@RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/onePharma"+" ->"+payload);
        try {
            JsonObject json = helper.getJsonObject(payload);
            OnePharmaEmailsEntity onePharma = helper.fromJson(json, OnePharmaEmailsEntity.class);

            onePharmaTransaction.insert(onePharma);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping("/email/{emailId}")
    public ResponseEntity<ApiResponse> getByEmailId(@PathVariable String emailId) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/onePharma/email"+emailId);
        try {
            String c2COde = onePharmaTransaction.getByEmailId(emailId);

            JsonObject response = new JsonObject();
            response.addProperty("c_c2code", c2COde);

            this.setJsonPayload(apiResponse, response);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping("/email/delete/{emailId}")
    public ResponseEntity<ApiResponse> deleteByEmailId(@PathVariable String emailId) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/onePharma/email/delete"+emailId);
        try {

            onePharmaTransaction.deleteByEmailId(emailId);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping("/itemCode")
    public ResponseEntity<ApiResponse> getItemCode(@RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/onePharma/itemCode"+" ->"+payload);
        try {

            JsonObject requestBody = helper.getJsonObject(payload);

            JsonObject response = onePharmaTransaction.getLatestItemCodeBasedOnDescription(requestBody);

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("data", response);
            this.setJsonPayload(apiResponse, jsonObject);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping("/blockListedEmail")
    public ResponseEntity<ApiResponse> getBlockListedEmail(@RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/onePharma/blockListedEmail"+payload);
        try {
            JsonObject requestBody = helper.getJsonObject(payload);

            JsonObject response = onePharmaTransaction.getBlockListedEmail(requestBody);
            this.setJsonPayload(apiResponse, response);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }

        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/invoice/list", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> invoiceList(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/onePharma/invoice/list" +" ->"+headers.toString()+" ->"+ payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            OnePharmaBo onePharmaBo = helper.fromJson(payload, OnePharmaBo.class);
            SearchBO searchBO = helper.fromJSON(payload, SearchBO.class);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            this.validateInputPayload(onePharmaBo);
//            String mobileNo = "";
//            if (json.has("c_mobile_no")) {
//                mobileNo = json.get("c_mobile_no").getAsString();
//                if (helper.isEmpty(mobileNo) || mobileNo.length() < 10) {
//                    throw new InvalidRequestException("c_mobile_no","Mobile Number should be in length of 10-12");
//                }
//            }
//            else
//                throw new InvalidRequestException("key missing", Messages.INVALID_REQUEST);

            //JsonArray invoiceList = onePharmaTransaction.getInvoiceList(mobileNo,onePharmaBo,searchBO);
            JsonArray invoiceList = onePharmaTransaction.getInvoiceList(lcHeaderBO,onePharmaBo,searchBO);
            JsonObject data = new JsonObject();
            data.add("j_list", invoiceList);
            data.addProperty("n_next_page", searchBO.getPage() + 1);
          //  responseBO.setTotal(onePharmaTransaction.getInvoiceListCount(mobileNo,onePharmaBo,pageBO));
            this.setDataJsonObjectPayload(apiResponse, data);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/downloadInvoice", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> downloadInvoice(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/onePharma/invoice/downloadInvoice" +" ->"+headers.toString()+" ->"+ payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            OnePharmaBo onePharmaBo = helper.fromJson(payload, OnePharmaBo.class);
            this.validateInputPayload(onePharmaBo);

            JsonObject invoiceList = onePharmaTransaction.getInvoiceRecord(onePharmaBo);
            this.setJsonPayload(apiResponse, invoiceList);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/invoice/itemsDetail", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> itemsDetail(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/onePharma/invoice/itemDetails" +" ->"+headers.toString()+" ->"+ payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            OnePharmaBo onePharmaBo = helper.fromJson(payload, OnePharmaBo.class);
            this.validateInputPayload(onePharmaBo);

            JsonArray itemDetails = onePharmaTransaction.getItemDetails(onePharmaBo);
            this.setDataJsonArrayPayload(apiResponse, itemDetails);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
    @PostMapping(value = "/downloadExcel", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> downloadExcel(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/onePharma/invoice/downloadExcel" +" ->"+headers.toString()+" ->"+ payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            OnePharmaBo onePharmaBo = helper.fromJson(payload, OnePharmaBo.class);
            this.validateInputPayload(onePharmaBo);

            JsonObject invoiceList = onePharmaTransaction.getInvoiceExcel(onePharmaBo);
            this.setJsonPayload(apiResponse, invoiceList);
            this.addMessage(apiResponse, MsMessages.SUCCESS);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
}
