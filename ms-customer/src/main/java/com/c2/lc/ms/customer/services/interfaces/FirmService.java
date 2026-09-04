package com.c2.lc.ms.customer.services.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.*;
import com.c2.lc.ms.customer.bos.*;
import com.c2.lc.ms.customer.entities.customer.*;
import com.c2.lc.ms.customer.services.interfaces.base.LcBaseService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

public interface FirmService extends LcBaseService {

    FirmEntity getFirmById(Long firmId) throws RecordNotFoundException;

    ContactDetailEntity getFirmContact(Long firmId) throws RecordNotFoundException;

    boolean doesExistMobileNo(String mobileNo);

    boolean doesExistEmail(String email);

    FirmEntity saveFirm(Long userId, FirmEntity firmEntity) throws RecordNotFoundException;

    ContactDetailEntity saveContact(Long userId, ContactDetailEntity firmEntity);

    void makeFirmInactive(Long firmId) throws RecordNotFoundException;

    String uploadDocument(Long userId, Long firmId, JsonObject data, String imageType) throws URISyntaxException, StorageException, IOException, RecordNotFoundException;

    boolean isBuyer(Long firmId) throws RecordNotFoundException;

    boolean isSeller(Long firmId) throws RecordNotFoundException;

    List<FirmEntity> getListSeller();

    List<FirmEntity> getListBuyer();

    boolean deleteDocument(Long firmId, String path) throws StorageException, IOException, URISyntaxException, RecordNotFoundException;

    List<Object> listDocument();

    void updateContact(Long userId, ContactDetailEntity contactDetailEntity, JsonObject json) throws RecordNotFoundException, DuplicateRecordException, InputPayloadException;

    FirmEntity getRegByMobileNoAndType(String cMobileNo, String type);

    boolean doesMobileNumberExist(String mobileNo, String type);

    boolean isScheduleExist(String mobileNo, String product);

    void saveScheduleDemo(ScheduleDemoEntity scheduleDemo);

    void updateFirmEntity(Long userId, FirmEntity current, BranchDetailsBO firm) throws InvalidRequestException;

    FirmEntity saveFirmBranch(LcHeaderBO header, BranchDetailsBO branch) throws DuplicateRecordException, InvalidRequestException;

    void updateBranch(LcHeaderBO header, Long branchId, BranchDetailsBO branch) throws RecordNotFoundException, InvalidRequestException;

    void delete(String mobile) throws RecordNotFoundException;

    int checkGst(String gstNumber, Long userId) throws RecordNotFoundException;

    int checkDrugLicense(String dlNo);

    JsonObject combineList(String mobileNo) throws DataFormatException;

    FirmEntity combineStores(StoreCombineRequestBO requestBO, Long userId) throws DataFormatException;

    void updateStore(StoreCombineBO store, Long userId) throws RecordNotFoundException;

    StoreCombineBO getStoreDetail(FirmEntity store);

    String getC2Code(String cMobileNo);

    AddressModelBO getAddressService(String pincode) throws RecordNotFoundException;

    FirmEntity saveUncombinedStores(StoreCombineRequestBO requestBO, Long userId) throws DataFormatException;

    void saveLoCombinedFirmTemp(StoreCombineRequestBO requestBO);

    void deleteLc1(String mobileNo) throws RecordNotFoundException;

    JsonArray searchLc1(String columnName, String searchKey, int page, int limit);

    int getLc1SearchCount(String colName, String searchKey);

    int getLc1StoreCount(String cMobileNo);

    List<FirmEntity> saveStore(String cMobileNo, Long nUserId, List<String> c2CodeAndCustCodeList, List<String> remainingComboList);

    void saveC2CodeAndBrCode(List<FirmEntity> firmList, Long nUserId, List<String> c2CodeAndCustCodeList, List<String> remainingComboList);

    List<JsonObject> fetchBranchList(String c2Code, SearchBO searchBO) throws RecordNotFoundException;

    List<String> getC2CodeCombination(String cMobileNo, int page, int size) throws RecordNotFoundException;

    List<String> getRemainingCombination(List<String> c2CodeAndCustCodeList, int page, int size) throws RecordNotFoundException;

    JsonObject getMobileNumber(String c_mid) throws RecordNotFoundException;

    Set<String> getMobileNumbersList(JsonObject request, int page, int size);

    List<FirmEntity> getByMobileNo(String cMobileNo);

    List<String> getFirmSellersCombo(List<FirmEntity> firmEntities);

    List<FirmEntity> saveNewLC1Store(List<String> firmNames, String cMobileNo, Long nUserId, List<String> c2CodeAndCustCodeList, List<String> remainingComboList);

    List<JsonObject> fetchBranchListByPinCode(String c2Code, String pinCode, PageBO pageBO);

    void savePinCodeReq(UserDetailEntity userDetailEntity, TSRegisterBO registerBO);

    List<String> getAllUserMobile(Long parentUserId);

    int updateFirmC2code();

    void saveFailedMobiles(String cMobileNo, String stackTrace);

    CombineCronTimeLogEntity saveLog(CombineCronTimeLogEntity logEntity);

    List<FirmEntity> getStoresFromUactMstAndSave(String cMobileNo, Long nUserId);

    void save(FirmEntity firm);

    int getUactMstCount(String cMobileNo);
}
