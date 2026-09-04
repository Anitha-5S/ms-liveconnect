package com.c2.lc.ms.user.transactions;

import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.ms.user.entities.UserFirmEntity;
import com.c2.lc.ms.user.services.interfaces.UserFirmService;
import com.c2.lc.ms.user.transactions.interfaces.UserFirmTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFirmTransactionImpl extends BaseTransactionImpl implements UserFirmTransaction {

    @Autowired private UserFirmService userFirmService;

    @Override
    public void saveOrUpdate(UserFirmEntity userFirm) {

        UserFirmEntity userFirmEntity = userFirmService.getExist(userFirm.getCMobileNo(), userFirm.getCPassword());
        if (userFirmEntity == null) {
            userFirm.setTCreatedAt(helper.getCurrentTime());
            userFirmService.saveOrUpdate(userFirm);
        } else {
            userFirmEntity.setCIp(userFirm.getCIp());
            userFirmEntity.setTLastLoginAt(helper.getCurrentTime());
            userFirmService.saveOrUpdate(userFirmEntity);
        }
    }
}
