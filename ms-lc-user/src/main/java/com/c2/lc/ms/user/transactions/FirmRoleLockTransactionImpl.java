package com.c2.lc.ms.user.transactions;

import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.ms.user.entities.FirmRoleLockEntity;
import com.c2.lc.ms.user.services.interfaces.FirmRoleLockService;
import com.c2.lc.ms.user.transactions.interfaces.FirmRoleLockTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FirmRoleLockTransactionImpl extends BaseTransactionImpl implements FirmRoleLockTransaction {

    @Autowired private FirmRoleLockService lockService;

    @Override
    public void save(FirmRoleLockEntity firmRole) {

        FirmRoleLockEntity firmRoleEntity = lockService.getExist(firmRole.getCMobileNo(),
                firmRole.getCC2Code(), firmRole.getCActCode());
        if (firmRoleEntity == null) {
            firmRole.setTCreatedAt(helper.getCurrentTime());
            lockService.saveOrUpdate(firmRole);
        } else {
            firmRoleEntity.setCRoleLock(firmRole.getCRoleLock());
            firmRoleEntity.setTLastUpdatedAt(helper.getCurrentTime());
            lockService.saveOrUpdate(firmRoleEntity);
        }
    }
    }
