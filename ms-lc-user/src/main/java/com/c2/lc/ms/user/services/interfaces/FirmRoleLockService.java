package com.c2.lc.ms.user.services.interfaces;


import com.c2.lc.lib.services.interfaces.BaseDBService;
import com.c2.lc.ms.user.entities.FirmRoleLockEntity;

public interface FirmRoleLockService extends BaseDBService {

    FirmRoleLockEntity getExist(String mobile, String c2Code, String actCode);

    void saveOrUpdate(FirmRoleLockEntity firmRole);
}
