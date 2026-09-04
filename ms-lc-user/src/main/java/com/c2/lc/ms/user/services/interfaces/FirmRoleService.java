package com.c2.lc.ms.user.services.interfaces;

import com.c2.lc.lib.services.interfaces.BaseDBService;
import com.c2.lc.ms.user.entities.FirmRoleEntity;

import java.util.List;

public interface FirmRoleService extends BaseDBService {

    FirmRoleEntity getExist(String mobile, String c2Code, String actCode, String type);

    void saveOrUpdate(FirmRoleEntity firmRole);

    Long getNextSeq();

    List<FirmRoleEntity> getNotInLock(String mobile, String c2Code, String actCode);

}
