package com.c2.lc.ms.user.transactions.interfaces;

import com.c2.lc.lib.transactions.interfaces.BaseTransaction;
import com.c2.lc.ms.user.entities.FirmRoleLockEntity;

public interface FirmRoleLockTransaction extends BaseTransaction {

    void save(FirmRoleLockEntity firmRole);
}
