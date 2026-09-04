package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.c2.lc.ms.master.entities.mysql.CustPincodewiseC2codeEntity;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.google.gson.JsonObject;

import java.util.List;

public interface CustPincodewiseC2codeService extends MasterBaseService {

    List<CustPincodewiseC2codeEntity> getByPincode(String c2Code);

    List<CustPincodewiseC2codeEntity> getByC2code(String c2code);

    JsonObject getAllC2codeByStateCode(String stateCode) throws RecordNotFoundException;
}
