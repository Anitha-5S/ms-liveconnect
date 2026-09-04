package com.c2.lc.ms.master.controllers.base;

import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Map;

public class MasterBaseController extends LoBaseController {

    private static final String CUS_ID = "x-csquare-cus-id";
    private static final String FIRM_ID = "x-csquare-firm-id";
    private static final String PIN_CODE = "x-csquare-pincode";
    private static final String C2CODE = "x-csquare-c2code";

    protected SearchBO getValidatedSearchBO (String payload) throws InputPayloadException, InvalidRequestException {
        SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
        this.validateInputPayload(searchBO);
        if (searchBO.getSearchTerm().startsWith("*")){
            throw new InvalidRequestException("c_search_term", "Search cannot start with '*'!");
        }
        return searchBO;
    }

    protected Long getCustomerId(Map<String, String> headers) throws InvalidRequestException {
        String id = headers.get(CUS_ID);
        if (helper.isEmpty(id)) {
            throw new InvalidRequestException("", "Invalid customer id!");
        }
        return Long.parseLong(id);
    }

    protected Long getFirmId(Map<String, String> headers) throws InvalidRequestException {
        String id = headers.get(FIRM_ID);
        if (helper.isEmpty(id)) {
            throw new InvalidRequestException("", "Invalid firm id!");
        }
        return Long.parseLong(id);
    }

    protected String getPinCode(Map<String, String> headers) throws InvalidRequestException {
        String pinCode = headers.get(PIN_CODE);
        if (helper.isEmpty(pinCode)) {
            throw new InvalidRequestException("", "Invalid Pincode!");
        }
        return pinCode;
    }

    protected JsonObject getDataObject(String data) {
        return getJsonObject(data, Constants.ROOT_DATA);
    }

    protected JsonObject getJsonObject(String payload, String key) {
        Gson gson = new Gson();
        JsonObject data = gson.fromJson(payload, JsonObject.class);
        return helper.isEmpty(key) ? data : data.getAsJsonObject(key);
    }
    protected String getHeaderC2Code(Map<String, String> headers) {
        String c2Code = headers.get(C2CODE);
        if (helper.isEmpty(c2Code)) {
            c2Code = "DEFAULT";
        }
        return c2Code;
    }







}
