package com.c2.lc.ms.master.transactions.interfaces;


import com.c2.lc.lib.exceptions.SimpleException;
import com.google.gson.JsonObject;

public interface AdminTransaction{

    JsonObject getState(JsonObject data);

    JsonObject getStateDetails(JsonObject data);

    JsonObject addState(JsonObject data) throws SimpleException;

    JsonObject editState(JsonObject data) throws SimpleException;

    JsonObject addDistrict(JsonObject data) throws SimpleException;

    JsonObject editDistrict(JsonObject data) throws SimpleException;

    JsonObject getDistrictDetails(JsonObject data);

    JsonObject getDistrict(JsonObject data);

    JsonObject addCity(JsonObject data) throws SimpleException;

    JsonObject editCity(JsonObject data) throws SimpleException;

    JsonObject getCityDetails(JsonObject data);

    JsonObject getCity(JsonObject data);

    JsonObject addArea(JsonObject data) throws SimpleException;

    JsonObject editArea(JsonObject data) throws SimpleException;

    JsonObject getAreaDetails(JsonObject data);

    JsonObject getArea(JsonObject data);
}
