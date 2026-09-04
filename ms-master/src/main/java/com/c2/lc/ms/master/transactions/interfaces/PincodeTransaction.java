package com.c2.lc.ms.master.transactions.interfaces;

import com.c2.lc.ms.master.transactions.interfaces.base.MasterBaseTransaction;
import com.c2.lc.ms.master.entities.mysql.CustPincodewiseC2codeEntity;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public interface PincodeTransaction extends MasterBaseTransaction {

    List<CustPincodewiseC2codeEntity> getByPincode(String c2Code);

    List<CustPincodewiseC2codeEntity> getByC2code(String c2code);

    JsonArray getStateByPincode(String pinCode);

    JsonObject getC2codeByStateCode(String stateCode) throws RecordNotFoundException, InputPayloadException;
}
