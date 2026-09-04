package com.c2.lc.ms.customer.services.interfaces;

import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.bos.UserProfileResponseBo;
import com.c2.lc.ms.customer.entities.customer.FirmUserEntity;
import com.c2.lc.ms.customer.services.interfaces.base.LcBaseService;
import com.google.gson.JsonObject;

import java.util.List;

public interface FirmUserService extends LcBaseService {

    void addUserToFirm(Long userId, Long firmId, Long createdUserId, String status);

    void addFirmToUser(Long userId, Long newFirmId, String status);

    void validateRequest(Long firmId, Long userId) throws InvalidRequestException;

    boolean doesExistMobileNo(String mobileNo);

    void deleteFirmUser(Long firmId, Long userId) throws RecordNotFoundException;

    boolean doesExistMobileNoForParent(String mobileNo, Long firmId);

    int getCount(Long firmId, Long userId);

    int getBranchCount(Long userId);

    void mobilenoUpdate(Long userId, String c_mobile_no) throws RecordNotFoundException;

    UserProfileResponseBo getProfile(Long userId) throws InvalidRequestException, RecordNotFoundException;

    List<FirmUserEntity> listUsers(Long branchId);

    void saveRecentItem(Long userId, String cItemCode);

    void clearRecentItem(Long userId) throws RecordNotFoundException;

    JsonObject getRecentItems(Long userId) throws RecordNotFoundException;

    void profielImageUpdate(Long userId, String c_profile_image_url) throws RecordNotFoundException;
}
