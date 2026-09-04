package com.c2.lc.ms.user.transactions;

import com.azure.messaging.eventhubs.EventData;
import com.c2.lc.lib.eventhub.EventHubUtil;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.lib.transactions.interfaces.BaseTransaction;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.user.entities.FirmRoleEntity;
import com.c2.lc.ms.user.services.interfaces.FirmRoleService;
import com.c2.lc.ms.user.transactions.interfaces.FirmRoleTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FirmRoleTransactionImpl extends BaseTransactionImpl implements FirmRoleTransaction {

    @Autowired
    private FirmRoleService roleService;

    @Value("${azure.eventhub.combine.name}")
    private String eventHubForCombine;

    @Value("${azure.eventhub.connection.string}")
    private String connectionString;

    @Override
    public void save(FirmRoleEntity firmRole) {

        FirmRoleEntity firmRoleEntity = roleService.getExist(firmRole.getCMobileNo(),
                firmRole.getCC2Code(), firmRole.getCActCode(), firmRole.getCRoleType());
        if (firmRoleEntity == null) {
            firmRole.setTCreatedAt(helper.getCurrentTime());
            long seq = roleService.getNextSeq();
            firmRole.setCTempFirmId("TEMP_"+seq);
            roleService.saveOrUpdate(firmRole);
        } else {
            firmRoleEntity.setCFirmId(firmRole.getCFirmId());
            firmRoleEntity.setCTempFirmId(firmRoleEntity.getCTempFirmId());
            firmRoleEntity.setNLock(firmRole.getNLock());
            firmRoleEntity.setCOrgMobileNo(firmRoleEntity.getCOrgMobileNo());
            firmRoleEntity.setTLastUpdatedAt(helper.getCurrentTime());
            roleService.saveOrUpdate(firmRoleEntity);
        }
    }

    @Override
    public List<FirmRoleEntity> getNotInLock(String mobile, String c2Code, String actCode) {
        return roleService.getNotInLock(mobile, c2Code, actCode);
    }

    @Override
    public void updateFirmRole(JsonArray jsonArray) throws RecordNotFoundException {


        for (int i=0; i<jsonArray.size(); i++) {
            JsonObject obj = jsonArray.get(i).getAsJsonObject();
            FirmRoleEntity entity = helper.fromJson(obj, FirmRoleEntity.class);
            FirmRoleEntity firmRoleEntity = roleService.getExist(entity.getCMobileNo(),
                    entity.getCC2Code(), entity.getCActCode(), entity.getCRoleType());
            if (firmRoleEntity == null) {
                throw new RecordNotFoundException("Firm Role Not Exists");
            } else {
                firmRoleEntity.setCFirmId(entity.getCFirmId());
                roleService.saveOrUpdate(firmRoleEntity);
            }
        }
    }

    @Override
    public void lockFirm(JsonArray jsonArray) throws RecordNotFoundException {

        for (int i=0; i<jsonArray.size(); i++) {
            JsonObject obj = jsonArray.get(i).getAsJsonObject();
            FirmRoleEntity entity = helper.fromJson(obj, FirmRoleEntity.class);
            FirmRoleEntity firmRoleEntity = roleService.getExist(entity.getCMobileNo(),
                    entity.getCC2Code(), entity.getCActCode(), entity.getCRoleType());
            if (firmRoleEntity == null) {
                throw new RecordNotFoundException("Firm Role Not Exists");
            } else {
                firmRoleEntity.setNLock(Constants.INT_VALUE_ONE);
                roleService.saveOrUpdate(firmRoleEntity);
            }
        }
    }

    @Override
    public void firmEventHub(JsonArray jsonArray) {
        List<EventData> allEvents = new ArrayList<>();
        for (JsonElement row : jsonArray) {
            allEvents.add(new EventData(row.getAsJsonObject().toString()));
        }
        EventHubUtil.publishEvents(connectionString, eventHubForCombine, allEvents);
    }
}