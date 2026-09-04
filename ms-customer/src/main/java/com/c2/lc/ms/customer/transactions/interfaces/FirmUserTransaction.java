package com.c2.lc.ms.customer.transactions.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.bos.ListUserModelBO;
import com.c2.lc.ms.customer.bos.UserModelBO;
import com.c2.lc.ms.customer.bos.UserProfileResponseBo;
import com.c2.lc.ms.customer.transactions.base.LcBaseTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface FirmUserTransaction extends LcBaseTransaction {


    boolean doesExistMobileNo(String c_mobile_no);

    boolean isAlreadyRegisteredMobileNo(Long userId, String cMobileNo) throws RecordNotFoundException;

    JsonObject createUser(Long userId, String brCode, Long firmId, UserModelBO customer) throws CommunicationErrorException, InvalidRequestException, NoSuchAlgorithmException, InvalidKeyException, DuplicateRecordException, RecordNotFoundException;

    List<ListUserModelBO> getFirmUsers(Long userId, Long firmId, int page, int limit) throws RecordNotFoundException;

    void deleteFirmUser(Long userId, Long firmId, LcHeaderBO header) throws RecordNotFoundException;

    void updateUser(Long userId, Long firmId, UserModelBO userModelBO) throws RecordNotFoundException;

    UserModelBO getUserDetail(Long userId, Long firmId) throws RecordNotFoundException;

    boolean doesExistMobileNoForParent(String cMobileNo, Long firmId);

    int getCount(Long firmId, Long userId);

    void mobilenoUpdate(Long userId, String c_mobile_no) throws RecordNotFoundException;

    UserProfileResponseBo getProfile(Long userId) throws RecordNotFoundException, InvalidRequestException;

    JsonArray getUser(LcHeaderBO headerBO, String mobileNo) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException, NoSuchAlgorithmException, InvalidKeyException, DuplicateRecordException;

    void saveRecentItem(Long userId, String cItemCode);

    void clearRecentItem(Long userId) throws RecordNotFoundException;

    JsonObject getRecentItem(Long userId) throws RecordNotFoundException;

    void profielImageUpdate(Long userId, String c_profile_image_url) throws RecordNotFoundException;
}
