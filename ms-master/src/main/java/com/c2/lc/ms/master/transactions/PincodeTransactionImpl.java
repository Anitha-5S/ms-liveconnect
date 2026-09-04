package com.c2.lc.ms.master.transactions;

import com.c2.lc.ms.master.services.interfaces.CustPincodewiseC2codeService;
import com.c2.lc.ms.master.services.interfaces.PincodeService;
import com.c2.lc.ms.master.transactions.base.MasterBaseTransactionImpl;
import com.c2.lc.ms.master.transactions.interfaces.PincodeTransaction;
import com.c2.lc.ms.master.entities.mysql.CustPincodewiseC2codeEntity;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PincodeTransactionImpl  extends MasterBaseTransactionImpl implements PincodeTransaction {

    @Autowired private PincodeService pincodeService;

    @Autowired private CustPincodewiseC2codeService custPincodewiseC2codeService;

    @Override
    public List<CustPincodewiseC2codeEntity> getByPincode(String c2Code) {
        return custPincodewiseC2codeService.getByPincode(c2Code);
    }

    @Override
    public List<CustPincodewiseC2codeEntity> getByC2code(String c2code) {
        return custPincodewiseC2codeService.getByC2code(c2code);
    }

    @Override
    public JsonArray getStateByPincode(String pinCode) {
        return pincodeService.getStateByPincode(pinCode);
    }

    @Override
    public JsonObject getC2codeByStateCode(String stateCode) throws RecordNotFoundException, InputPayloadException {

        if (helper.isEmpty(stateCode)) {
            throw new InputPayloadException("state code cannot be empty");
        }

        if(stateCode.length() > 2){
            throw new InputPayloadException("state code length cannot be more than 2 character");
        }

        return custPincodewiseC2codeService.getAllC2codeByStateCode(stateCode);
    }
}
