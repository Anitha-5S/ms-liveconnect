package com.c2.lc.ms.customer.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.bos.AddressBo;
import com.c2.lc.ms.customer.entities.customer.ContactDetailEntity;
import com.c2.lc.ms.customer.transactions.interfaces.AddressTransaction;
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
@RequestMapping(value = {"${api.base.path}/address"})
public class AddressController extends LoBaseController {

    @Autowired private AddressTransaction addressTransaction;

    /**
     * API Id : 1.13.1
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/b2c/addressadd", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> saveAddress(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/address/b2c/addressadd");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);
            AddressBo addressBo = helper.fromJson(payload, AddressBo.class);
            this.validateInputPayload(addressBo);
            addressTransaction.saveAddress(headerBO.getUserId(), addressBo);

            this.addMessage(apiResponse, "Address Added");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 1.13.3
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/b2c/addressupdate", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> updateAddress(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/address/b2c/addressupdate");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);
            AddressBo addressBo = helper.fromJson(payload, AddressBo.class);
            this.validateInputPayload(addressBo);
            addressTransaction.updateAddress(headerBO.getUserId(), addressBo);

            this.addMessage(apiResponse, "Address Updated");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 1.13.4
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/b2c/setdeliveryaddress", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> setDeliveryAddress(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/address/b2c/setdeliveryaddress");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
                if (json.get("c_add_id").getAsString().isEmpty() || json.get("c_add_id").getAsString().isBlank()) {
                    throw new InvalidRequestException("c_add_id", "'c_add_id' can't be empty/blank");
                }
            addressTransaction.setDeliveryAddress(headerBO.getUserId(),json.get("c_add_id").getAsLong());

            this.addMessage(apiResponse, "Success");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 1.13.2
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     */
    @GetMapping(value ="/b2c/addresslist")
    public ResponseEntity<ApiResponse> addressList(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/address/b2c/addresslist");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);
            Long userId = headerBO.getUserId();
            List<ContactDetailEntity> contactDetailEntityList = addressTransaction.addressList(userId);
            if(contactDetailEntityList.size()==0){
                throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
            }
            JsonArray list = (JsonArray) helper.getGson().toJsonTree(contactDetailEntityList,
                    new TypeToken<List<ContactDetailEntity>>() {
                    }.getType());
            this.setDataJsonArrayPayload(apiResponse, list);
            this.addMessage(apiResponse, "Success");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 1.13.4
     * Developer : shobha.hs@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/b2c/deleteAddress", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteAddress(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/address/b2c/deleteAddress");
        try {
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            addressTransaction.deleteAddress(json.get("c_add_id").getAsLong());

            this.addMessage(apiResponse, "Success");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

}
