package com.c2.lc.ms.customer.services.interfaces;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.bos.ListUserModelBO;
import com.c2.lc.ms.customer.bos.TSRegisterBO;
import com.c2.lc.ms.customer.bos.UserModelBO;
import com.c2.lc.ms.customer.entities.customer.ContactDetailEntity;
import com.c2.lc.ms.customer.entities.customer.UserDetailEntity;
import com.c2.lc.ms.customer.entities.customer.UserOwnerEntity;
import com.c2.lc.ms.customer.services.interfaces.base.LcBaseService;

import java.util.List;

public interface UserService extends LcBaseService {

    UserDetailEntity getById(Long userId) throws RecordNotFoundException;

    UserDetailEntity createUser(Long userId, ContactDetailEntity contactDetailEntity);

    UserDetailEntity saveCustomer(UserDetailEntity customer);

    UserDetailEntity createUser(Long userId, ContactDetailEntity contactDetailEntity, UserModelBO model);

    UserDetailEntity updateUser(Long userId, UserModelBO userModelBO, ContactDetailEntity contactDetailEntity);

    List<ListUserModelBO> getUserDetailsByFirmId(Long userId, Long firmId);

    UserModelBO getUserDetail(Long uId) throws RecordNotFoundException;

    List<ListUserModelBO> getUserDetails(Long userId, Long firmId, int page, int limit);

    void deleteUser(Long userId);

    void addUserToOwner(Long parentUserId, Long childUserId);

    UserOwnerEntity getParentUser(Long userId) throws RecordNotFoundException;

    UserDetailEntity createTSUser(long userId, ContactDetailEntity contactDetailEntity, TSRegisterBO registerBO);

    List<Object[]> getUSer(String mobileNo);

    void updateStatus(long userId, String status);
}
