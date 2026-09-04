package com.c2.lc.ms.customer.transactions;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.*;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.bos.*;
import com.c2.lc.ms.customer.entities.comm.EcoUsers;
import com.c2.lc.ms.customer.entities.comm.LcUser;
import com.c2.lc.ms.customer.entities.comm.LcUserType;
import com.c2.lc.ms.customer.entities.customer.*;
import com.c2.lc.ms.customer.services.interfaces.*;
import com.c2.lc.ms.customer.transactions.base.LcBaseTransactionImpl;
import com.c2.lc.ms.customer.transactions.interfaces.FirmTransaction;
import com.c2.lc.ms.customer.transactions.interfaces.FirmUserTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
public class FirmTransactionImpl extends LcBaseTransactionImpl implements FirmTransaction {


    @Autowired
    private LcUserService lcUserService;
    @Autowired
    private FirmService firmService;
    @Autowired
    private UserService userService;
    @Autowired
    private FirmUserService firmUserService;
    @Autowired
    private FirmBranchService firmBranchService;
    @Autowired
    private FirmDefaultService firmDefaultService;
    @Autowired
    private ContactDetailService contactDetailService;
    @Autowired
    private FirmUserTransaction userTransaction;

    @Override
    public FirmEntity getFirmById(Long firmId) throws RecordNotFoundException {
        return firmService.getFirmById(firmId);
    }

    @Override
    public ContactDetailEntity getFirmContact(Long firmId) throws RecordNotFoundException {
        return firmService.getFirmContact(firmId);
    }

    @Override
    public boolean doesExistMobileNo(String mobileNo) {
        return firmService.doesExistMobileNo(mobileNo);
    }

    @Override
    public FirmEntity createUpdateReg(FirmEntity firmEntity, LcUser lcUser) throws NoSuchAlgorithmException, InvalidKeyException, RecordNotFoundException, DataFormatException {
        Long customerID = createFirm(firmEntity, lcUser);
        return firmService.getRegByMobileNoAndType(firmEntity.getCMobileNo(),
                firmEntity.getCType());
    }

    @Override
    public JsonObject login(Long userId, FirmEntity firm, String pwd, String lcUserStatus, FirmEntity firmStatus, String status, String deviceToken) throws Exception {
        return lcUserService.login(userId, firm, pwd, lcUserStatus, firmStatus, status, deviceToken);
    }

    /*
    1. Save User
    2. Save firm
    3. Save branch
    4. set firm branch
    5. set branch user
    6. set default
    7. save eco user
    */
    @Transactional(rollbackOn = Exception.class)
    @Override
    public JsonObject registerFirm(LcUser lcUser, FirmEntity firm, EcoUsers ecoUsers, String password, String deviceToken) throws Exception {
        String msg = "User exist.";
        JsonObject obj = new JsonObject();
        if (doesMobileNumberWithTypeExist(firm.getCMobileNo(), firm.getCType())) {
            LcUser user = lcUserService.getLcUserByMobileAndType(firm.getCMobileNo(), firm.getCType());
            switch (user.getStatus()) {
                case Constants.STATUS_ACTIVE:
                    msg += " Please Login.";
                    break;
                case Constants.STATUS_PENDING:
                    msg += " Approval Pending";
                    break;
                case "I":
                    msg += " Please contact Administrator to activate";
                    break;
            }
            throw new DuplicateRecordException(msg);
        }

//        boolean result = checkUserExistInLC(firm.getCMobileNo());
//        if(result){
//            obj.addProperty("c_lc_user_status",Constants.STATUS_YES);
//            throw new DuplicateRecordException("c_lc_user_status :" + obj.get("c_lc_user_status").getAsString());
//        }

        lcUserService.saveLcUserType(firm.getCMobileNo(), firm.getCType());
        createUpdateReg(firm, lcUser);
        return lcUserService.register(lcUser.getNId(), firm, password, deviceToken);
    }

    @Override
    public List<LcUserType> doesMobileNumberExist(String mobileNo) {
        return lcUserService.checkMobileNumberExists(mobileNo);
    }

    @Override
    public boolean doesMobileNumberWithTypeExist(String mobileNo, String type) {
        return lcUserService.checkMobileNumberWithTypeExists(mobileNo, type);
    }

    @Override
    public boolean isScheduleExist(String mobileNo, String product) {
        return firmService.isScheduleExist(mobileNo, product);
    }

    @Override
    public void saveScheduleDemo(ScheduleDemoEntity scheduleDemo) {
        firmService.saveScheduleDemo(scheduleDemo);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Long createFirm(FirmEntity firmEntity, LcUser lcUser) throws NoSuchAlgorithmException, InvalidKeyException, RecordNotFoundException, DataFormatException {
        UserDetailEntity userDetailEntity = createUser(Constants.ROOT_USER, firmEntity.getCMobileNo());
//        boolean result = checkUserExistInLC(firmEntity.getCMobileNo());
//        if(result){
//            lcUser.setLcUserStatus(Constants.STRING_VALUE_ONE);
//        } else {
//            lcUser.setLcUserStatus(Constants.STRING_VALUE_ZERO);
//        }
        lcUser.setNId(userDetailEntity.getNUserId());
        lcUserService.saveUser(lcUser);
        firmEntity = initializeFirm(userDetailEntity.getNUserId(), firmEntity);
        firmEntity = updateC2Code(userDetailEntity.getNUserId(), firmEntity);
        firmUserService.addUserToFirm(userDetailEntity.getNUserId(), firmEntity.getNFirmId(), userDetailEntity.getNUserId(), Constants.STATUS_ACTIVE);
        firmDefaultService.setDefaultFirm(userDetailEntity.getNUserId(), firmEntity.getNFirmId());
        // firmUserService.createLogin(userDetailEntity.getNUserId(), firmEntity.getCMobileNo(), c_pwd);
        return userDetailEntity.getNUserId();
    }

    private FirmEntity updateC2Code(Long userId, FirmEntity firmEntity) throws RecordNotFoundException {
        firmEntity.setC2Code("L" + firmEntity.getNFirmId());
//        firmEntity.setBrCode(helper.getLongStringValue(firmEntity.getNFirmId()));
        firmEntity.setBrCode("000");
        firmEntity = firmService.saveFirm(userId, firmEntity);
        return firmEntity;
    }

    private FirmEntity initializeFirm(Long userId, FirmEntity firmEntity) throws RecordNotFoundException {
        firmEntity.setNCreatedBy(userId);
        firmEntity.setTCreatedAt(helper.getCurrentTime());
        firmEntity.setCStatus(Constants.STATUS_ACTIVE);
        firmEntity.setContactDetail(saveContactDetail(userId, firmEntity.getCName(), firmEntity.getCMobileNo(), firmEntity.getCPin()));
        firmEntity = firmService.saveFirm(userId, firmEntity);
        return firmEntity;
    }

    private UserDetailEntity createUser(Long userId, String mobileNo) {
        ContactDetailEntity contactDetailEntity = contactDetailService.createContactDetail(userId, mobileNo);
        return userService.createUser(userId, contactDetailEntity);
    }

    private ContactDetailEntity saveContactDetail(Long userId, String name, String phoneNo, String pin) throws RecordNotFoundException {
        ContactDetailEntity entity = new ContactDetailEntity(userId, helper.getCurrentTime());
        entity.setCContactName(name);
        entity.setCPhoneNo(phoneNo);
        entity.setCPin(pin);
        return contactDetailService.saveContactDetail(entity);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateContact(LcHeaderBO lcHeaderBO, JsonObject json) throws DuplicateRecordException, RecordNotFoundException, InputPayloadException {
        ContactDetailEntity contactDetailEntity = getFirmContact(lcHeaderBO.getUserId());
        firmService.updateContact(lcHeaderBO.getUserId(), contactDetailEntity, json);
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public JsonObject uploadProfileImage(Long userId, JsonObject data, String path) throws StorageException, IOException, URISyntaxException, RecordNotFoundException {
        Long firmId = data.get("firmId").getAsLong();
        FirmEntity firmEntity = getFirmById(firmId);
        String imageUrl = firmEntity.getCImageUrl();
        JsonObject response = new JsonObject();
        if (imageUrl != null) {
            firmService.deleteDocument(firmId, imageUrl);
        }
        String uri = firmService.uploadDocument(userId, firmId, data, path);
        firmEntity.setCImageUrl(uri);
        firmService.saveFirm(userId, firmEntity);
        response.addProperty("URI", uri);
        return response;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public JsonObject uploadDl1Image(Long userId, JsonObject data, String path) throws StorageException, IOException, URISyntaxException, RecordNotFoundException, InvalidRequestException {
        Long firmId = getFirmIdFromData(data);
        FirmEntity firmEntity = getFirmById(firmId);
        String imageUrl = firmEntity.getLegalIdentities().getCDrugLicenseNo1Img();
        JsonObject response = new JsonObject();

        if (imageUrl != null) {
            firmService.deleteDocument(firmId, imageUrl);
        }
        String uri = firmService.uploadDocument(userId, firmId, data, path);
        firmEntity.getLegalIdentities().setCDrugLicenseNo1Img(uri);
        firmService.saveFirm(userId, firmEntity);
        response.addProperty("URI", uri);
        return response;
    }

    private Long getFirmIdFromData(JsonObject data) throws InvalidRequestException {
//        data.get("firmId").getAsLong();
        if (helper.isEmpty(data.get("firmId"))) {
            throw new InvalidRequestException("firmId !", " firmId not present in request payload");
        }

        return data.get("firmId").getAsLong();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public JsonObject uploadDl2Image(Long userId, JsonObject data, String path) throws StorageException, IOException, URISyntaxException, RecordNotFoundException, InvalidRequestException {
        Long firmId = getFirmIdFromData(data);
        FirmEntity firmEntity = getFirmById(firmId);
        String imageUrl = firmEntity.getLegalIdentities().getCDrugLicenseNo2Img();
        JsonObject response = new JsonObject();

        if (imageUrl != null) {
            firmService.deleteDocument(firmId, imageUrl);
        }
        String uri = firmService.uploadDocument(userId, firmId, data, path);
        firmEntity.getLegalIdentities().setCDrugLicenseNo2Img(uri);
        firmService.saveFirm(userId, firmEntity);
        response.addProperty("URI", uri);
        return response;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public JsonObject uploadNarcoticImage(Long userId, JsonObject data, String path) throws StorageException, IOException, URISyntaxException, RecordNotFoundException {
        Long firmId = data.get("firmId").getAsLong();
        FirmEntity firmEntity = getFirmById(firmId);
        String imageUrl = firmEntity.getLegalIdentities().getCNarcoticNoImg();
        JsonObject response = new JsonObject();

        if (imageUrl != null) {
            firmService.deleteDocument(firmId, imageUrl);
        }
        String uri = firmService.uploadDocument(userId, firmId, data, path);
        firmEntity.getLegalIdentities().setCNarcoticNoImg(uri);
        firmService.saveFirm(userId, firmEntity);
        response.addProperty("URI", uri);
        return response;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean deleteProfileImage(Long userId, Long firmId) throws StorageException, IOException, URISyntaxException, RecordNotFoundException {
        FirmEntity firmEntity = firmService.getFirmById(firmId);
        String imageUrl = firmEntity.getCImageUrl();
        if (imageUrl == null) {
            throw new RecordNotFoundException(firmId, "Image URl not found");
        }
        boolean isDeleted = firmService.deleteDocument(firmId, firmEntity.getCImageUrl());
        if (isDeleted) {
            firmEntity.setCImageUrl(null);
            firmService.saveFirm(userId, firmEntity);
        }
        return isDeleted;
    }

    @Override
    public List<Object> listDocument() {
        return firmService.listDocument();
    }

    @Override
    public FirmEntity getFirm(String mobileNumber, String type) throws RecordNotFoundException {
        FirmEntity entity = firmService.getRegByMobileNoAndType(mobileNumber, type);
        if (entity == null) {
            throw new RecordNotFoundException("", "Record not found!");
        }
        return entity;
    }

    @Override
    public FirmEntity getDefaultFirm(long userid) throws RecordNotFoundException {
        return firmDefaultService.getDefaultFirm(userid);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public FirmEntity save(LcHeaderBO lcHeaderBO, BranchDetailsBO firm) throws RecordNotFoundException, InvalidRequestException {
        FirmEntity current = firmService.getFirmById(lcHeaderBO.getFirmId());
        current.setIdTime(lcHeaderBO.getUserId(), helper.getCurrentTime());
        firmService.updateFirmEntity(lcHeaderBO.getUserId(), current, firm);
        return current;


    }

    @Override
    public void delete(String mobile) throws RecordNotFoundException {
        LcUser lcUser = lcUserService.getUserIdByMobile(mobile);
        if (lcUser != null) {
            List<String> mobileList = firmService.getAllUserMobile(lcUser.getNCreatedBy());
            if (mobileList.size() > 0) {
                for (String mobileNo : mobileList) {
                    lcUserService.delete(mobileNo);
                }
            }
        }
        firmService.delete(mobile);
        lcUserService.delete(mobile);
    }

    @Override
    public int checkGst(String gstNumber, Long userId) throws RecordNotFoundException {
        return firmService.checkGst(gstNumber, userId);
    }

    @Override
    public int checkDrugLicense(String dlNo) {
        return firmService.checkDrugLicense(dlNo);
    }

    @Override
    public JsonObject combineList(String mobileNo) throws DataFormatException {
        return firmService.combineList(mobileNo);
    }

    @Override
    public CombineFirmAndRegisterBO combineStores(StoreCombineRequestBO storeCombineRequestBO) throws Exception {
        UserDetailEntity userDetailEntity = createUser(Constants.ROOT_USER, storeCombineRequestBO.getMobileNo());
        boolean isUserRegistered = doesMobileNumberWithTypeExist(storeCombineRequestBO.getMobileNo(), Constants.ROLE_BUYER);
        String password = "Password@123";
        CombineFirmAndRegisterBO combineFirmAndRegisterBO = new CombineFirmAndRegisterBO();
        if (!isUserRegistered) {
//            String password = generatePassword();
            LcUser lcUser = new LcUser(userDetailEntity.getNUserId(), helper.getCurrentTime());
            lcUser.setNId(userDetailEntity.getNUserId());
            lcUser.setMobileNumber(storeCombineRequestBO.getMobileNo());
            lcUser.setPassword(password);
            lcUser.setType(Constants.ROLE_BUYER);
            lcUser.setLcUserStatus(Constants.STRING_VALUE_ONE);
            lcUserService.saveUser(lcUser);
            lcUserService.saveLcUserType(storeCombineRequestBO.getMobileNo(), Constants.ROLE_BUYER);
        }

        FirmEntity firm = firmService.combineStores(storeCombineRequestBO, userDetailEntity.getNUserId());
        createFirmDefault(userDetailEntity.getNUserId(), firm.getNFirmId());

//        if(!isUserRegistered) {
//            combineFirmAndRegisterBO.setRegObj(lcUserService.register(userDetailEntity.getNUserId(), firm, password, deviceToken));
//        }
        combineFirmAndRegisterBO.setFirmEntity(firm);
        return combineFirmAndRegisterBO;
    }

    private String generatePassword() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "").substring(0, 14) + "@A";
    }

    private void createFirmDefault(Long userId, Long nFirmId) throws RecordNotFoundException {
        firmDefaultService.setDefaultFirm(userId, nFirmId);
    }

    @Override
    public void updateStore(StoreCombineBO store, Long userId) throws RecordNotFoundException {
        firmService.updateStore(store, userId);
    }

    @Override
    public StoreCombineBO getStoreDetail(FirmEntity store) {
        return firmService.getStoreDetail(store);
    }

    @Override
    public String getC2Code(String cMobileNo) {
        return firmService.getC2Code(cMobileNo);
    }

    @Override
    public List<ContactDetailEntity> getAddress(long delivery, long branch) throws RecordNotFoundException {
        List<ContactDetailEntity> list = new ArrayList<>();
        FirmEntity firmEntity = getFirmById(delivery);
        ContactDetailEntity contactDetail = firmEntity.getContactDetail();
        contactDetail.setCContactName(firmEntity.getCName());

        FirmEntity branchDetail = getFirmById(branch);
        ContactDetailEntity entity = branchDetail.getContactDetail();
        entity.setCContactName(branchDetail.getCName());

        list.add(contactDetail);
        list.add(entity);
        return list;
    }

    @Override
    public AddressModelBO getAddressService(String pincode) throws RecordNotFoundException {
        return firmService.getAddressService(pincode);
    }

    @Override
    public LcUser getLcUserStatus(String mobileNo) throws RecordNotFoundException {
        return lcUserService.getLcUser(mobileNo);
    }

    @Override
    public void getUpdatedStatus(FirmEntity firmEntity, JsonObject obj) {
        lcUserService.getUpdatedStatus(firmEntity, obj);
    }

    @Override
    public boolean checkUserExistInLC(String cMobileNo) throws DataFormatException {
        int count = 0;
        boolean flag = lcUserService.checkUserExistInLC(cMobileNo);
        if (flag) {
            count = firmService.getLc1StoreCount(cMobileNo);
            count += firmService.getUactMstCount(cMobileNo);
        }
        return count > 0;
    }

    @Override
    public void saveUncombinedStores(StoreCombineRequestBO requestBO, Long userId) throws DataFormatException {
        lcUserService.updateLcUser(requestBO.getMobileNo(), userId);
        if (requestBO.getStoreList().size() > 0) {
            firmService.saveLoCombinedFirmTemp(requestBO);
            firmService.saveUncombinedStores(requestBO, userId);
        }
    }

    @Override
    public CombineFirmAndRegisterBO combineRemainingStores(StoreCombineRequestBO requestBO, LcHeaderBO header) throws DataFormatException {
        FirmEntity firm = firmService.combineStores(requestBO, header.getUserId());
        CombineFirmAndRegisterBO combineFirmAndRegisterBO = new CombineFirmAndRegisterBO();
        combineFirmAndRegisterBO.setFirmEntity(firm);
        return combineFirmAndRegisterBO;
    }

    @Override
    public void deleteLc1(String mobileNo) throws RecordNotFoundException {
        firmService.deleteLc1(mobileNo);
    }

    @Override
    public CombineFirmAndRegisterBO saveUncombinedStoresNA(StoreCombineRequestBO requestBO) throws Exception {
        CombineFirmAndRegisterBO combineFirmAndRegisterBO = new CombineFirmAndRegisterBO();
        if (requestBO.getStoreList().size() > 0) {
            UserDetailEntity userDetailEntity = createUser(Constants.ROOT_USER, requestBO.getMobileNo());
            String password = "Password@123";
//            String password = generatePassword();
            LcUser lcUser = new LcUser(userDetailEntity.getNUserId(), helper.getCurrentTime());
            lcUser.setNId(userDetailEntity.getNUserId());
            lcUser.setMobileNumber(requestBO.getMobileNo());
            lcUser.setPassword(password);
            lcUser.setType(Constants.ROLE_BUYER);
            lcUser.setLcUserStatus(Constants.STRING_VALUE_ONE);
            lcUserService.saveUser(lcUser);
            lcUserService.saveLcUserType(requestBO.getMobileNo(), Constants.ROLE_BUYER);

            firmService.saveLoCombinedFirmTemp(requestBO);
            FirmEntity firm = firmService.saveUncombinedStores(requestBO, userDetailEntity.getNUserId());
            createFirmDefault(userDetailEntity.getNUserId(), firm.getNFirmId());

//            combineFirmAndRegisterBO.setRegObj(lcUserService.register(userDetailEntity.getNUserId(), firm, password, deviceToken));
            lcUserService.updateLcUser(requestBO.getMobileNo(), userDetailEntity.getNUserId());
        }
        return combineFirmAndRegisterBO;
    }

    @Override
    public JsonArray searchLc1(String columnName, String searchKey, int page, int limit) {
        return firmService.searchLc1(columnName, searchKey, page, limit);
    }

    @Override
    public int getLc1SearchCount(String colName, String searchKey) {
        return firmService.getLc1SearchCount(colName, searchKey);
    }

    @Override
    public JsonObject tsRegister(TSRegisterBO registerBO) throws CommunicationErrorException, InvalidRequestException, DuplicateRecordException {
        UserDetailEntity userDetailEntity = createTSUser(Constants.ROOT_USER, registerBO);

        if (registerBO.getServiceActiveStatus().equals("N")) {
            firmService.savePinCodeReq(userDetailEntity, registerBO);
        }

        if (!lcUserService.checkTSUserExist(registerBO.getMobileNumber(), registerBO.getC2Code(), Constants.ROLE_CUSTOMER)) {
            LcUser lcUser = new LcUser(userDetailEntity.getNUserId(), helper.getCurrentTime());
            lcUser.setNId(userDetailEntity.getNUserId());
            lcUser.setPassword("");
            lcUser.setMobileNumber(registerBO.getMobileNumber());
            lcUser.setC2Code(registerBO.getC2Code());
            lcUser.setBrCode(registerBO.getBrCode() == null ? "0" : registerBO.getBrCode());
            lcUser.setType(Constants.ROLE_CUSTOMER);
            lcUser.setStatus(Constants.STATUS_ACTIVE);
            lcUserService.save(lcUser);
            lcUserService.saveLcUserType(registerBO.getMobileNumber(), Constants.ROLE_CUSTOMER);
        } else {
            throw new DuplicateRecordException("Already Registered!");
        }
        JsonObject request = new JsonObject();
        request.addProperty("c_c2code", registerBO.getC2Code());
        request.addProperty("c_br_code", registerBO.getBrCode() == null ? "0" : registerBO.getBrCode());
        request.addProperty("c_terminal_id", userDetailEntity.getNUserId());
        request.addProperty("c_type", Constants.ROLE_CUSTOMER);
        request.addProperty("c_device_token", "E");

        return firmDefaultService.callC2Service(request);
    }

    private UserDetailEntity createTSUser(long userId, TSRegisterBO registerBO) {
        ContactDetailEntity contactDetailEntity = contactDetailService.createTSContactDetail(userId, registerBO);
        return userService.createTSUser(userId, contactDetailEntity, registerBO);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public String saveStore(String cMobileNo) throws Exception {
        boolean isUserRegistered = doesMobileNumberWithTypeExist(cMobileNo, Constants.ROLE_BUYER);
        String message;
        if (!isUserRegistered) {
            UserDetailEntity userDetailEntity = createUser(Constants.ROOT_USER, cMobileNo);
            message = combineStoreUserAndFirmCreate(cMobileNo, userDetailEntity);
        } else {
            importNewlyAddedStore(cMobileNo);
            message = "New Stores Imported!";
        }
        return message;
    }

    private String combineStoreUserAndFirmCreate(String cMobileNo, UserDetailEntity userDetailEntity) throws Exception {
//        boolean isUserRegistered = doesMobileNumberWithTypeExist(cMobileNo, Constants.ROLE_BUYER);
        String message = "";
        CombineFirmAndRegisterBO combineFirmAndRegisterBO = new CombineFirmAndRegisterBO();

//        if (!isUserRegistered) {
        String password = generatePassword();
        LcUser lcUser = new LcUser(userDetailEntity.getNUserId(), helper.getCurrentTime());
        lcUser.setNId(userDetailEntity.getNUserId());
        lcUser.setMobileNumber(cMobileNo);
        lcUser.setType(Constants.ROLE_BUYER);
        lcUser.setLcUserStatus(Constants.STRING_VALUE_ZERO);

//            boolean flag = lcUserService.callC2PasswordService(lcUser, password);
//            log.debug("Flag :" + flag);
//            if (!flag) {
//                lcUser.setPassword(password);
//            } else {
        password = "Password@123";
        lcUser.setPassword(password);
//            }
        lcUserService.saveUser(lcUser);
        lcUserService.saveLcUserType(cMobileNo, Constants.ROLE_BUYER);

        FirmEntity firm = new FirmEntity();
        int size = 20;
        for (int page = 0; ; page++) {
            List<String> c2CodeAndCustCodeList = firmService.getC2CodeCombination(cMobileNo, page, size);

            if (c2CodeAndCustCodeList.size() > 0) {
                List<String> remainingComboList = firmService.getRemainingCombination(c2CodeAndCustCodeList, page, size);
                List<FirmEntity> firmList = firmService.saveStore(cMobileNo, userDetailEntity.getNUserId(), c2CodeAndCustCodeList, remainingComboList);
                if (firmList.size() > 0) {
                    firmService.saveC2CodeAndBrCode(firmList, userDetailEntity.getNUserId(), c2CodeAndCustCodeList, remainingComboList);

                    firm = firmList.get(0);
                }
                if (c2CodeAndCustCodeList.size() == 0 && remainingComboList.size() == 0) {
                    message = "Stores Imported Successfully!";
                    break;
                }
            }
            if (c2CodeAndCustCodeList.size() == 0 && page == 0) {
                List<FirmEntity> firmList = firmService.getStoresFromUactMstAndSave(cMobileNo, userDetailEntity.getNUserId());
                if (firmList.size() > 0) {
                    for (FirmEntity firmEntity : firmList) {
                        if (firmEntity.getC2Code().equals("LO")) {
                            firmEntity.setC2Code("L" + firm.getNFirmId());
                        }
//                        firmEntity.setBrCode(helper.getLongStringValue(firmEntity.getNFirmId()));
                        firmService.save(firmEntity);
                    }
                    firm = firmList.get(0);
                } else {
                    message = "No Stores available!";
                    break;
                }
                message = "Stores Imported Successfully.";
                break;
            }
            if (c2CodeAndCustCodeList.size() == 0 && page > 0) {
                message = "Stores Imported Successfully.";
                break;
            }
        }
        if (firm.getNFirmId() != null) {
            createFirmDefault(userDetailEntity.getNUserId(), firm.getNFirmId());
            String deviceToken = "E";
            combineFirmAndRegisterBO.setRegObj(lcUserService.register(userDetailEntity.getNUserId(), firm, password, deviceToken));

            LcHeaderBO headerBO = new LcHeaderBO(userDetailEntity.getNUserId(), firm.getNFirmId(),
                    firm.getC2Code(), firm.getBrCode(), helper.getLongStringValue(userDetailEntity.getNUserId()), Constants.ROLE_BUYER);
            userTransaction.getUser(headerBO, cMobileNo);
        }
//        } else {
//            importNewlyAddedStore(cMobileNo);
//        }
        return message;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void importNewlyAddedStore(String cMobileNo) throws RecordNotFoundException {
        LcUser lcUser = lcUserService.getLcUser(cMobileNo);
        int size = 10;
        for (int page = 0; ; page++) {
            List<String> c2CodeAndCustCodeList = firmService.getC2CodeCombination(cMobileNo, page, size);
            if (c2CodeAndCustCodeList.size() > 0) {
                List<String> firmNames = new ArrayList<>();
                List<String> remainingComboList = firmService.getRemainingCombination(c2CodeAndCustCodeList, page, size);
                List<FirmEntity> firmEntities = firmService.getByMobileNo(cMobileNo);
                List<String> firmSellersEntities = firmService.getFirmSellersCombo(firmEntities);
                c2CodeAndCustCodeList.removeAll(firmSellersEntities);
                remainingComboList.removeAll(firmSellersEntities);

                for (FirmEntity firm : firmEntities) {
                    firmNames.add(firm.getCName());
                }

                if (c2CodeAndCustCodeList.size() > 0 || remainingComboList.size() > 0) {
                    List<FirmEntity> firmList = firmService.saveNewLC1Store(firmNames, cMobileNo, lcUser.getNId(), c2CodeAndCustCodeList, remainingComboList);
                    if (firmList.size() > 0) {
                        firmService.saveC2CodeAndBrCode(firmList, lcUser.getNId(), c2CodeAndCustCodeList, remainingComboList);
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }
    }

    @Override
    public List<JsonObject> fetchBranchList(String c2Code, SearchBO searchBO) throws RecordNotFoundException {
        return firmService.fetchBranchList(c2Code, searchBO);
    }

    @Override
    public JsonObject getMobileNumber(String c_mid) throws RecordNotFoundException {
        return firmService.getMobileNumber(c_mid);
    }

    @Override
    public void combine(JsonObject request, LcHeaderBO header) throws Exception {
        int page;
        int size = 10;
        for (page = 0; ; page++) {
            System.out.println(page + "___________" + size);
            Set<String> mobileNoSet = firmService.getMobileNumbersList(request, page, size);
            if (mobileNoSet.size() > 0) {
                for (String cMobileNo : mobileNoSet) {
                    try {
                        importLc1UserData(header, cMobileNo);
                    } catch (Exception e) {
                        log.error("Mobile no : " + cMobileNo + "and Exception is : " + e.getMessage());
                        firmService.saveFailedMobiles(cMobileNo, helper.getString(e.getStackTrace()));
                    }
                }
            } else {
                break;
            }
        }
    }

    @Override
    public void combine(JsonObject request) throws Exception {
        int page;
        int size = 10;
        for (page = 0; ; page++) {
            System.out.println(page + "___________" + size);
            Set<String> mobileNoSet = firmService.getMobileNumbersList(request, page, size);
            if (mobileNoSet.size() > 0) {
                for (String cMobileNo : mobileNoSet) {
                    try {
                        saveStore(cMobileNo);
                    } catch (Exception e) {
                        log.error("Mobile no : " + cMobileNo + "and Exception is : " + e.getMessage());
                        firmService.saveFailedMobiles(cMobileNo, helper.getString(e.getStackTrace()));
                    }
                }
            } else {
                break;
            }
        }
    }

    @org.springframework.transaction.annotation.Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = Exception.class)
    public void importLc1UserData(LcHeaderBO header, String cMobileNo) throws Exception {
        boolean isUserRegistered = doesMobileNumberWithTypeExist(cMobileNo, Constants.ROLE_BUYER);
        String message;
        if (!isUserRegistered) {
            UserDetailEntity userDetailEntity = createUser(header.getUserId(), cMobileNo);
            message = combineStoreUserAndFirmCreate(cMobileNo, userDetailEntity);
        } else {
            importNewlyAddedStore(cMobileNo);
            message = "New Stores Imported!";
        }
    }

    @Override
    public List<JsonObject> fetchBranchListByPinCode(String c2Code, JsonElement pinCode, PageBO pageBO) {
        return firmService.fetchBranchListByPinCode(c2Code, pinCode.getAsString(), pageBO);
    }

    @Override
    public int updateFirmC2code() {
        return firmService.updateFirmC2code();
    }

    @Override
    public CombineCronTimeLogEntity saveLog(CombineCronTimeLogEntity logEntity) {
        return firmService.saveLog(logEntity);
    }
}
