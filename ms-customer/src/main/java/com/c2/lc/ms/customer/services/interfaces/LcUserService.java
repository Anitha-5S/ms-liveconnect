package com.c2.lc.ms.customer.services.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.*;
import com.c2.lc.ms.customer.entities.comm.EcoUsers;
import com.c2.lc.ms.customer.entities.comm.LcUser;
import com.c2.lc.ms.customer.entities.comm.LcUserType;
import com.c2.lc.ms.customer.entities.customer.FirmEntity;
import com.google.gson.JsonObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface LcUserService {

    void saveUser(LcUser lcUser) throws NoSuchAlgorithmException, InvalidKeyException;

    void saveLcUserType(String mobileNo, String type);

    List<LcUserType> checkMobileNumberExists(String mobileNo);

    boolean checkMobileNumberWithTypeExists(String mobileNo, String type);

    LcUser getLcUser(String mobileNumber) throws RecordNotFoundException;

    JsonObject login(Long userId, FirmEntity firm, String pwd, String lcUserStatus, FirmEntity firmStatus, String status, String deviceToken) throws Exception;

    void delete(String mobile) throws RecordNotFoundException;

    JsonObject register(Long userId, FirmEntity firm, String pwd, String deviceToken) throws Exception;

    Boolean checkUserExistInLC(String cMobileNo) throws DataFormatException;

    LcUser getLcUserByMobileAndType(String cMobileNo, String cType);

    void getUpdatedStatus(FirmEntity firmStatus, JsonObject json);

    void saveEcoUsers(Long userId, Long firmId, EcoUsers ecoUsers, String c2Code, String brCode) throws DuplicateRecordException, NoSuchAlgorithmException, InvalidKeyException;

    void updateLcUser(String mobileNo, Long userId);

    boolean callC2PasswordService(LcUser lcUser, String password) throws CommunicationErrorException, InvalidRequestException, NoSuchAlgorithmException, InvalidKeyException;

    boolean checkTSUserExist(String mobileNo, String c2code, String type);

    void save(LcUser lcUser);

    JsonObject getCustCount(String c2Code, String branch, String d_from_date, String d_to_date) throws RecordNotFoundException;

    List<JsonObject> fetchAllCustomers(String c2Code, SearchBO searchBO, JsonObject req) throws RecordNotFoundException;

    void updateStatus(long userId, String status);

    int count(String c2Code, SearchBO searchBO, JsonObject req) throws RecordNotFoundException;

    LcUser getUserIdByMobile(String mobile);

    void deleteUser(Long userId, LcHeaderBO header) throws RecordNotFoundException;

    void deleteBranchUser(Long nUserId, Long branchId, String c2Code) throws RecordNotFoundException;

    int getLoginCount(String c2Code, String brCode, String terminalId, String type);
}
