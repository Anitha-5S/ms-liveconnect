package com.c2.lc.ms.user.services;

import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.ms.user.entities.UserFirmEntity;
import com.c2.lc.ms.user.repos.UserFirmRepo;
import com.c2.lc.ms.user.services.interfaces.UserFirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFirmServiceImpl extends BaseDBServiceImpl implements UserFirmService {

    @Autowired private UserFirmRepo userFirmRepo;

    @Override
    public UserFirmEntity getExist(String mobile, String password) {
        return userFirmRepo.getExists(mobile, password);
    }

    @Override
    public void saveOrUpdate(UserFirmEntity userFirm) {
        userFirmRepo.save(userFirm);
    }
}
