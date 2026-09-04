package com.c2.lc.ms.user.transactions.interfaces;

import com.c2.lc.lib.exceptions.RecordNotFoundException;

import com.c2.lc.lib.transactions.interfaces.BaseTransaction;
import com.c2.lc.ms.user.entities.FirmRoleEntity;
import com.google.gson.JsonArray;

import java.util.List;

public interface FirmRoleTransaction extends BaseTransaction {

    void save(FirmRoleEntity firmRole);

    List<FirmRoleEntity> getNotInLock(String mobile, String c2Code, String actCode);

    void updateFirmRole(JsonArray jsonArray) throws RecordNotFoundException;

    void lockFirm(JsonArray jsonArray) throws RecordNotFoundException;

    void firmEventHub(JsonArray jsonArray);
}
