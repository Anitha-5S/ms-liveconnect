package com.c2.lc.ms.user.services;

import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.ms.user.entities.FirmRoleLockEntity;
import com.c2.lc.ms.user.repos.FirmRoleLockRepo;
import com.c2.lc.ms.user.services.interfaces.FirmRoleLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirmRoleLockServiceImpl extends BaseDBServiceImpl implements FirmRoleLockService {

    @Autowired
    private FirmRoleLockRepo lockRepo;

    @Override
    public FirmRoleLockEntity getExist(String mobile, String c2Code, String actCode) {
        return lockRepo.getExists(mobile, c2Code, actCode);
    }

    @Override
    public void saveOrUpdate(FirmRoleLockEntity firmRole) {
        lockRepo.save(firmRole);
    }
}