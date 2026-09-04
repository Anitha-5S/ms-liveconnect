package com.c2.lc.ms.user.services.interfaces;

import com.c2.lc.lib.services.interfaces.BaseDBService;
import com.c2.lc.ms.user.entities.UserFirmEntity;

public interface UserFirmService extends BaseDBService {

    UserFirmEntity getExist(String mobile, String password);

    void saveOrUpdate(UserFirmEntity userFirm);
}
