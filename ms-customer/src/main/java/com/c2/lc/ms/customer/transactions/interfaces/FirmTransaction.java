package com.c2.lc.ms.customer.transactions.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.*;
import com.c2.lc.ms.customer.bos.*;
import com.c2.lc.ms.customer.entities.comm.EcoUsers;
import com.c2.lc.ms.customer.entities.comm.LcUser;
import com.c2.lc.ms.customer.entities.comm.LcUserType;
import com.c2.lc.ms.customer.entities.customer.CombineCronTimeLogEntity;
import com.c2.lc.ms.customer.entities.customer.ContactDetailEntity;
import com.c2.lc.ms.customer.entities.customer.FirmEntity;
import com.c2.lc.ms.customer.entities.customer.ScheduleDemoEntity;
import com.c2.lc.ms.customer.transactions.base.LcBaseTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface FirmTransaction extends LcBaseTransaction {

    FirmEntity getFirmById(Long firmId) throws RecordNotFoundException;

    ContactDetailEntity getFirmContact(Long firmId) throws RecordNotFoundException;

    boolean doesExistMobileNo(String mobileNo);

    Long createFirm(FirmEntity firmEntity, LcUser lcUser) throws InvalidRequestException, CommunicationErrorException, DuplicateRecordException, RecordNotFoundException, NoSuchAlgorithmException, InvalidKeyException, DataFormatException;

    void updateContact(LcHeaderBO lcHeaderBO, JsonObject json) throws DuplicateRecordException, RecordNotFoundException, InputPayloadException;

    JsonObject uploadProfileImage(Long userId, JsonObject payload, String path) throws StorageException, IOException, URISyntaxException, RecordNotFoundException, DuplicateRecordException;

    @Transactional(rollbackOn = Exception.class)
    JsonObject uploadDl1Image(Long userId, JsonObject data, String path) throws StorageException, IOException, URISyntaxException, RecordNotFoundException, DuplicateRecordException, InvalidRequestException;

    @Transactional(rollbackOn = Exception.class)
    JsonObject uploadDl2Image(Long userId, JsonObject data, String path) throws StorageException, IOException, URISyntaxException, RecordNotFoundException, DuplicateRecordException, InvalidRequestException;

    @Transactional(rollbackOn = Exception.class)
    JsonObject uploadNarcoticImage(Long userId, JsonObject data, String path) throws StorageException, IOException, URISyntaxException, RecordNotFoundException, DuplicateRecordException;

    boolean deleteProfileImage(Long userId, Long firmId) throws StorageException, IOException, URISyntaxException, RecordNotFoundException, DuplicateRecordException;

    List<Object> listDocument();

    FirmEntity createUpdateReg(FirmEntity firmEntity, LcUser lcUser) throws InvalidRequestException, CommunicationErrorException, DuplicateRecordException, RecordNotFoundException, NoSuchAlgorithmException, InvalidKeyException, DataFormatException;

    JsonObject login(Long userId, FirmEntity firm, String pwd, String lcUserStatus, FirmEntity firmStatus, String status, String deviceToken) throws Exception;

    JsonObject registerFirm(LcUser lcUser, FirmEntity firm, EcoUsers ecoUsers, String password, String deviceToken) throws Exception;

    List<LcUserType> doesMobileNumberExist(String mobileNo) throws DuplicateRecordException;

    boolean doesMobileNumberWithTypeExist(String mobileNo, String type) throws DuplicateRecordException;

    boolean isScheduleExist(String mobileNo, String product);

    void saveScheduleDemo(ScheduleDemoEntity scheduleDemo);

    FirmEntity getFirm(String mobileNumber, String type) throws RecordNotFoundException;

    FirmEntity getDefaultFirm(long userId) throws RecordNotFoundException;

    FirmEntity save(LcHeaderBO lcHeaderBO, BranchDetailsBO firm) throws RecordNotFoundException, InvalidRequestException;

    void delete(String mobile) throws RecordNotFoundException;

    int checkGst(String gstNumber, Long userId) throws RecordNotFoundException;

    int checkDrugLicense(String dlNo);

    JsonObject combineList(String mobileNo) throws DataFormatException;

    CombineFirmAndRegisterBO combineStores(StoreCombineRequestBO storeCombineRequestBO) throws Exception;

    void updateStore(StoreCombineBO store, Long userId) throws RecordNotFoundException;

    StoreCombineBO getStoreDetail(FirmEntity store);

    String getC2Code(String cMobileNo);

    List<ContactDetailEntity> getAddress(long delivery, long branch) throws RecordNotFoundException;

    AddressModelBO getAddressService(String pincode) throws RecordNotFoundException;

    LcUser getLcUserStatus(String mobileNo) throws RecordNotFoundException;

    void getUpdatedStatus(FirmEntity firmEntity, JsonObject obj);

    boolean checkUserExistInLC(String cMobileNo) throws DataFormatException;

    void saveUncombinedStores(StoreCombineRequestBO requestBO, Long userId) throws DataFormatException;

    CombineFirmAndRegisterBO combineRemainingStores(StoreCombineRequestBO requestBO, LcHeaderBO header) throws DataFormatException;

    void deleteLc1(String mobileNo) throws RecordNotFoundException;

    CombineFirmAndRegisterBO saveUncombinedStoresNA(StoreCombineRequestBO requestBO) throws Exception;

    JsonArray searchLc1(String columnName, String searchKey, int page, int limit);

    int getLc1SearchCount(String colName, String searchKey);

    JsonObject tsRegister(TSRegisterBO registerBO) throws CommunicationErrorException, InvalidRequestException, DuplicateRecordException;

    String saveStore(String cMobileNo) throws Exception;

    List<JsonObject> fetchBranchList(String c2Code, SearchBO searchBO) throws RecordNotFoundException;

    JsonObject getMobileNumber(String c_mid) throws RecordNotFoundException;

    void combine(JsonObject request, LcHeaderBO header) throws Exception;

    void combine(JsonObject request) throws Exception;

    List<JsonObject> fetchBranchListByPinCode(String c2Code, JsonElement pinCode, PageBO pageBO);

    int updateFirmC2code();

    CombineCronTimeLogEntity saveLog(CombineCronTimeLogEntity logEntity);

    void importNewlyAddedStore(String cMobileNo) throws RecordNotFoundException;
}
