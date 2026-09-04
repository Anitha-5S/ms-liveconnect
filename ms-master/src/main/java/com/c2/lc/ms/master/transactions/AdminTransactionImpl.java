package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.exceptions.SimpleException;
import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.ms.master.services.interfaces.AdminService;
import com.c2.lc.ms.master.transactions.interfaces.AdminTransaction;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdminTransactionImpl  extends BaseTransactionImpl implements AdminTransaction {
    @Autowired
    private AdminService adminService;

    @Override
    public JsonObject getState(JsonObject data) {
        JsonObject response = adminService.getState(data);
        return response;
    }

    @Override
    public JsonObject getStateDetails(JsonObject data) {
        JsonObject response = adminService.getStateDetails(data);
        return response;
    }

    @Override
    public JsonObject addState(JsonObject data) throws SimpleException {
        JsonObject response = adminService.addState(data);
        return response;
    }

    @Override
    public JsonObject editState(JsonObject data) throws SimpleException {
        JsonObject response = adminService.editState(data);
        return response;
    }

    @Override
    public JsonObject addDistrict(JsonObject data) throws SimpleException {
        JsonObject response = adminService.addDistrict(data);
        return response;
    }

    @Override
    public JsonObject editDistrict(JsonObject data) throws SimpleException {
        JsonObject response = adminService.editDistrict(data);
        return response;
    }

    @Override
    public JsonObject getDistrictDetails(JsonObject data) {
        JsonObject response = adminService.getDistrictDetails(data);
        return response;
    }

    @Override
    public JsonObject getDistrict(JsonObject data) {
        JsonObject response = adminService.getDistrict(data);
        return response;
    }

    @Override
    public JsonObject addCity(JsonObject data) throws SimpleException {
        JsonObject response = adminService.addCity(data);
        return response;
    }

    @Override
    public JsonObject editCity(JsonObject data) throws SimpleException {
        JsonObject response = adminService.editCity(data);
        return response;
    }

    @Override
    public JsonObject getCityDetails(JsonObject data) {
        JsonObject response = adminService.getCityDetails(data);
        return response;
    }

    @Override
    public JsonObject getCity(JsonObject data) {
        JsonObject response = adminService.getCity(data);
        return response;
    }

    @Override
    public JsonObject addArea(JsonObject data) throws SimpleException {
        JsonObject response = adminService.addArea(data);
        return response;
    }

    @Override
    public JsonObject editArea(JsonObject data) throws SimpleException {
        JsonObject response = adminService.editArea(data);
        return response;
    }

    @Override
    public JsonObject getAreaDetails(JsonObject data) {
        JsonObject response = adminService.getAreaDetails(data);
        return response;
    }

    @Override
    public JsonObject getArea(JsonObject data) {
        JsonObject response = adminService.getArea(data);
        return response;
    }
}
