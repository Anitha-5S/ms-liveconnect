package com.c2.lc.ms.user.transactions.interfaces;

import com.c2.lc.lib.transactions.interfaces.BaseTransaction;
import com.c2.lc.ms.user.entities.UserFirmEntity;

public interface UserFirmTransaction extends BaseTransaction {

    void saveOrUpdate(UserFirmEntity userFirm);

}
