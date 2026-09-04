package com.c2.lc.ms.customer.services;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.*;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.entities.comm.*;
import com.c2.lc.ms.customer.entities.customer.FirmEntity;
import com.c2.lc.ms.customer.entities.customer.UserDetailEntity;
import com.c2.lc.ms.customer.repos.comm.EcoAuthSessionRepository;
import com.c2.lc.ms.customer.repos.comm.EcoUsersRepo;
import com.c2.lc.ms.customer.repos.comm.LcUserRepo;
import com.c2.lc.ms.customer.repos.comm.LcUserTypeRepo;
import com.c2.lc.ms.customer.repos.customer.UserDetailRepo;
import com.c2.lc.ms.customer.services.base.SecurityBaseServiceImpl;
import com.c2.lc.ms.customer.services.interfaces.LcUserService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LcUserServiceImpl extends SecurityBaseServiceImpl implements LcUserService {

    @Autowired
    private LcUserRepo lcUserRepo;
    @Autowired
    private LcUserTypeRepo lcUserTypeRepo;
    @Autowired
    private EcoUsersRepo ecoUsersRepo;
    @Autowired
    private UserDetailRepo userDetailRepo;
    @PersistenceContext(unitName = "mysql")
    @Autowired
    EntityManager entityManager;

    @Autowired
    private EcoAuthSessionRepository ecoAuthSessionRepository;

    @Value("${secret.key}")
    private String secretKey;
    @Value("${hash.algorithm}")
    private String hMacAlgorithm;
    @Value("${hash.algorithm}")
    private String hashAlgorithm;
    @Value("${aes.key}")
    private String aesKey;
    @Value("${aes.iv}")
    private String aesIV;


    @Value("${auth.register.api}")
    private String authRegisterApi;
    @Value("${auth.login.api}")
    private String authLoginApi;
    @Value("${auth.password.api}")
    private String authPasswordApi;
    @Value("${cart.count.api.url}")
    private String cartCountApi;

    public void save(LcUser lcUser) {
        lcUserRepo.save(lcUser);
    }

    public boolean callC2PasswordService(LcUser lcUser, String password) throws CommunicationErrorException, InvalidRequestException, NoSuchAlgorithmException, InvalidKeyException {
        log.debug("Password is :" + password);
        JsonObject obj = callC2PasswordService(lcUser.getMobileNumber(), password, lcUser.getNId(), authPasswordApi);
        log.debug("Password sent flag is :" + obj.get("c_send_sms_flag").getAsBoolean());
        return obj.get("c_send_sms_flag").getAsBoolean();
    }

    private JsonObject callC2PasswordService(String mobileNumber, String password, long nId, String url) throws CommunicationErrorException, InvalidRequestException {
        JsonObject request = new JsonObject();
        request.addProperty("c_mobile", mobileNumber);
        request.addProperty("c_pwd", password);
        request.addProperty("c_user_id", nId);

        String result = callWebClientPostSyncApi(url, request.toString());
        log.debug("C2 Service Response : {}" + result);

        JsonObject responseObject;
        if (result == null || result.isEmpty()) {
            log.error("Result is null API {} -- Request {} -- Response {}", authPasswordApi, request, result);
            throw new CommunicationErrorException("", "Error connecting to C2 Service!");
        } else {
            responseObject = helper.getJsonObject(result);
            if (responseObject.get("appStatusCode").getAsInt() != 0) {
                log.error("API {} -- Request {} -- Response {}", authPasswordApi, request, result);
                throw new InvalidRequestException("", "Invalid Request!");
            } else {
                log.debug("Response {}", result);
            }
        }
        return responseObject.get("payloadJson").getAsJsonObject();
    }

    public void saveUser(LcUser lcUser) throws NoSuchAlgorithmException, InvalidKeyException {
        lcUser.setPassword(helper.generateHMacHash(hashAlgorithm, aesKey, lcUser.getPassword()));
        lcUser.setStatus(Constants.STATUS_ACTIVE);
        save(lcUser);
    }

    private String getEncodedPassword(String newPassword) throws InvalidKeyException, NoSuchAlgorithmException {
        return helper.generateHMacHash(hMacAlgorithm, secretKey, newPassword);
    }


    public LcUser getLcUser(String mobileNumber) throws RecordNotFoundException {
        LcUser users = lcUserRepo.findByMobileNumber(mobileNumber);
        if (users == null) {
            throw new RecordNotFoundException("", "Not a registered user!");
        }
        return users;
    }

    @Override
    public void saveLcUserType(String mobileNo, String type) {
        LcUserTypePK pk = new LcUserTypePK(mobileNo, type);
        lcUserTypeRepo.save((new LcUserType(pk)));
    }

    @Override
    public List<LcUserType> checkMobileNumberExists(String mobileNo) {
        return lcUserTypeRepo.countMobileNumber(mobileNo);
    }

    @Override
    public boolean checkMobileNumberWithTypeExists(String mobileNo, String type) {
        return lcUserTypeRepo.checkMobileNumberWithTypeExists(mobileNo, type) > 0;
    }

    @Override
    public JsonObject login(Long userId, FirmEntity firm, String pwd, String lcUserStatus, FirmEntity firmStatus, String status, String deviceToken) throws Exception {
        JsonObject json = callC2Service(userId, firm, pwd, authLoginApi, deviceToken);
//        getUpdatedStatus(firmStatus, json);
//        json.addProperty("c_lc_user_status", lcUserStatus);
        json.addProperty("c_lc_user_active_status", status);
        json.addProperty("c_update_status", Constants.STRING_VALUE_ONE);
        json.addProperty("c_store_combine_status", firmStatus.getStoreCombineStatus() == null ? Constants.STRING_VALUE_ZERO : firmStatus.getStoreCombineStatus());
        return json;
    }

    @Override
    public void getUpdatedStatus(FirmEntity firmStatus, JsonObject json) {
        List<LcUserType> list = lcUserTypeRepo.countMobileNumber(firmStatus.getCMobileNo());

        JsonArray arr = new JsonArray();
        for (LcUserType lcUserType : list) {
            arr.add(lcUserType.getType());
        }
        json.add("c_type", arr);

        if (firmStatus.getContactDetail() != null && firmStatus.getContactDetail().getCContactName() != null
                && firmStatus.getContactDetail().getCAddress1() != null && firmStatus.getContactDetail().getCPin() != null
                && firmStatus.getContactDetail().getCAddress2() != null && firmStatus.getContactDetail().getCStateName() != null && firmStatus.getContactDetail().getCStateCode() != null
                && firmStatus.getContactDetail().getCCityName() != null && firmStatus.getContactDetail().getCCityCode() != null && firmStatus.getContactDetail().getCAreaName() != null
                && firmStatus.getContactDetail().getCAreaCode() != null && firmStatus.getCEmail() != null && firmStatus.getCPin() != null && firmStatus.getCStateName() != null && firmStatus.getCStateCode() != null
                && firmStatus.getCCityName() != null && firmStatus.getCCityCode() != null && firmStatus.getCAreaName() != null && firmStatus.getCAreaCode() != null) {
            json.addProperty("c_update_status", "1");
        } else {
            json.addProperty("c_update_status", "0");
        }
    }

    @Override
    public void saveEcoUsers(Long userId, Long firmId, EcoUsers ecoUsers, String c2Code, String brCode) throws DuplicateRecordException, NoSuchAlgorithmException, InvalidKeyException {
        EcoUsersPK pk = new EcoUsersPK(c2Code, brCode, helper.getString(userId));
        Optional<EcoUsers> users = ecoUsersRepo.findById(pk);
        if (users.isPresent()) {
            throw new DuplicateRecordException(pk.toString(), "Already registered!");
        }

        ecoUsers.setIdTime(userId, helper.getCurrentTime());
        ecoUsers.setPwd(helper.generateHMacHash(hashAlgorithm, aesKey, ecoUsers.getPwd()));
        ecoUsers.setC2Code(c2Code);
        ecoUsers.setBrCode(brCode);
        ecoUsers.setTerminalId(helper.getString(userId));
        ecoUsers.setStatus(Constants.STATUS_ACTIVE);
        ecoUsersRepo.save(ecoUsers);
    }

    @Override
    public void updateLcUser(String mobileNo, Long userId) {
        LcUser lcUser = lcUserRepo.getByMobileAndType(mobileNo, Constants.ROLE_BUYER);
        if (lcUser != null) {
            lcUser.setLcUserStatus(Constants.STRING_VALUE_ZERO);
            lcUserRepo.save(lcUser);
        }
    }

    @Override
    public void delete(String mobile) throws RecordNotFoundException {
        List<LcUser> users = lcUserRepo.findByMobile(mobile);
        if (users != null && users.size() > 0) {
            lcUserRepo.deleteAll(users);
            lcUserTypeRepo.deleteAll(lcUserTypeRepo.countMobileNumber(mobile));
        }
    }

    @Override
    public JsonObject register(Long userId, FirmEntity firm, String pwd, String deviceToken) throws Exception {
        return callC2Service(userId, firm, pwd, authRegisterApi, deviceToken);
    }

    @Override
    public Boolean checkUserExistInLC(String cMobileNo) throws DataFormatException {
        String sql = "select * from lc_mobile_user_mst lmum where n_mobile_no = :mobile";
        Query userQuery = entityManager.createNativeQuery(sql);
        userQuery.setParameter("mobile", helper.getLongValue(cMobileNo));
        List<Object[]> result = this.getResultList(userQuery);
        return !result.isEmpty();
    }

    @Override
    public LcUser getLcUserByMobileAndType(String cMobileNo, String cType) {
        return lcUserRepo.getByMobileAndType(cMobileNo, cType);
    }

    private JsonObject callC2Service(Long userId, FirmEntity firm, String pwd, String url, String deviceToken) throws CommunicationErrorException, InvalidRequestException {
        JsonObject request = new JsonObject();
        request.addProperty("c_c2code", (firm.getC2Code() == null ? "L"+firm.getNFirmId() : (firm.getC2Code().equals("") ? "L"+firm.getNFirmId() : firm.getC2Code())));
//        request.addProperty("c_br_code", (firm.getBrCode() == null ? helper.getString(firm.getNFirmId()) : firm.getBrCode()));
        request.addProperty("c_br_code", "000");
        request.addProperty("c_terminal_id", userId);
        request.addProperty("c_mobile_number", firm.getCMobileNo());
        request.addProperty("c_type", firm.getCType());
        request.addProperty("c_pwd", pwd);
        request.addProperty("n_profile_id", userId);
        request.addProperty("c_device_token", deviceToken);

        JsonObject data = new JsonObject();
        data.add("data", request);

        String result = callWebClientPostSyncApi(url, data.toString());
        log.debug("C2 Service Response : {}" + result);

        JsonObject responseObject;
        if (result == null || result.isEmpty()) {
            log.error("Result is null API {} -- Request {} -- Response {}", authRegisterApi, request, result);
            throw new CommunicationErrorException("", "Error connecting to C2 Service!");
        } else {
            responseObject = helper.getJsonObject(result);
            if (responseObject.get("appStatusCode").getAsInt() != 0) {
                if (responseObject.get("appStatusCode").getAsInt() == 11) {
                    throw new InvalidRequestException("", "Invalid Credentials!");
                }
                log.error("API {} -- Request {} -- Response {}", authRegisterApi, request, result);
                throw new InvalidRequestException("", "Invalid Request!");
            } else {
                responseObject.get("payloadJson").getAsJsonObject()
                        .addProperty("cType", "B");
                log.debug("Response {}", result);
            }

        }
        return responseObject.get("payloadJson").getAsJsonObject();
    }

    @Override
    public boolean checkTSUserExist(String mobileNo, String c2code, String type) {
        boolean result = false;
        LcUser lcUser = lcUserRepo.checkUserExist(mobileNo, c2code, type);
        if (lcUser != null) {
            result = true;
        }
        return result;
    }

    @Override
    public JsonObject getCustCount(String c2Code, String branch, String d_from_date, String d_to_date) throws RecordNotFoundException {
        JsonObject result = new JsonObject();
        JsonObject payload = new JsonObject();
        LocalDateTime fromDate = helper.convertStringToTime(d_from_date);
        LocalDateTime toDate = helper.convertStringToTime(d_to_date);
        int count = 0;
        List<LcUser> lcUserList ;
        if (branch.equals(Constants.STRING_VALUE_ONE)) {
            lcUserList = lcUserRepo.getByC2Code(c2Code,fromDate,toDate);
        } else
            lcUserList = lcUserRepo.getByC2CodeBrCode(c2Code, branch,fromDate,toDate);

        if (lcUserList.size() > 0) {
            result.addProperty("n_total_customer", lcUserList.size());//registered users->active cust or tot cus
            for (LcUser lcUser : lcUserList) {
                if (lcUser.getStatus().equals(Constants.STATUS_INACTIVE)) {
                    count++;
                }
            }
            result.addProperty("n_inactive_users_count", count);
            // result.addProperty("n_order_count", 0);
            result.addProperty("n_new_downloads_count", 0);
            //n_incomplete_orders_count cart count (c2code,brcode)
            // exchagecall to orders-live
            payload.addProperty("c_c2_code",c2Code);
            payload.addProperty("c_br_code",branch);
            payload.addProperty("d_from_date", String.valueOf(fromDate));
            payload.addProperty("d_to_date", String.valueOf(toDate));
            log.debug("cartCount payload--->"+payload+" ");
            result.addProperty("n_incomplete_orders_count",callWebClientPostSyncApi(cartCountApi,payload.toString()));

        } else {
            throw new RecordNotFoundException("Record Not Found!");
        }
        return result;
    }

    @Override
    public List<JsonObject> fetchAllCustomers(String c2Code, SearchBO searchBO, JsonObject req) throws RecordNotFoundException {
        Pageable pageable = PageRequest.of(searchBO.getPage(), searchBO.getLimit());
        List<Long> userIdList = new ArrayList<>();
        List<JsonObject> resultList = new ArrayList<>();
        List<LcUser> list = lcUserRepo.getAllByC2Code(c2Code, pageable);

        setResultListForCustomer(searchBO, req, userIdList, resultList, list);
        return resultList;
    }

    private void setResultListForCustomer(SearchBO searchBO, JsonObject req, List<Long> userIdList, List<JsonObject> resultList, List<LcUser> list) throws RecordNotFoundException {
        UserDetailEntity userDetailEntity;
        String sql;
        JsonObject json;
        if (req.has("c_status")) {
            list = list.stream().filter(cust -> cust.getStatus().equals(req.get("c_status").getAsString())).collect(Collectors.toList());
        }

        if (list.size() > 0) {
            if (searchBO.getSearchTerm() != null) {
                for (LcUser lcUser : list) {
                    userIdList.add(lcUser.getNId());
                }
                sql = "SELECT ud.n_user_id, ud.c_first_name, cd.c_mobile_no, cd.c_email_id, ud.d_date_of_birth, " +
                        "ud.c_gender, ud.c_status " +
                        "FROM user_detail ud " +
                        "JOIN contact_detail cd ON cd.n_contact_id = ud.n_contact_id " +
                        "WHERE ud.n_user_id IN :userIdList AND (LOWER(ud.c_first_name) LIKE LOWER('" + searchBO.getSearchTerm() + "%') OR cd.c_mobile_no LIKE '" + searchBO.getSearchTerm() + "%')";
                Query query = this.getQuery(sql);
                query.setParameter("userIdList", userIdList);
                List<Object[]> objList = this.getResultList(query);

                if (!objList.isEmpty()) {
                    for (Object[] obj : objList) {
                        json = new JsonObject();
                        json.addProperty("c_customer_code", helper.getString(obj[0]));
                        json.addProperty("c_customer_name", helper.getString(obj[1]));
                        json.addProperty("c_mobile_no", helper.getString(obj[2]));
                        json.addProperty("c_email_id", helper.getString(obj[3]));
                        json.addProperty("d_dob", helper.getString(obj[4]));
                        json.addProperty("c_gender", helper.getString(obj[5]));
                        json.addProperty("c_status", helper.getString(obj[6]));
                        resultList.add(json);
                    }
                } else {
                    throw new RecordNotFoundException("Record Not Found!");
                }
            } else {
                for (LcUser lcUser : list) {
                    json = new JsonObject();
                    userDetailEntity = userDetailRepo.getByUserId(lcUser.getNId());
                    json.addProperty("c_customer_code", lcUser.getNId());
                    json.addProperty("c_customer_name", userDetailEntity.getCFirstName());
                    json.addProperty("c_mobile_no", userDetailEntity.getContactDetail().getCMobileNo());
                    json.addProperty("c_email_id", userDetailEntity.getContactDetail().getCEmailId());
                    json.addProperty("d_dob", helper.getString(userDetailEntity.getDateOfBirth()));
                    json.addProperty("c_gender", userDetailEntity.getGender());
                    json.addProperty("c_status", userDetailEntity.getCStatus());
                    resultList.add(json);
                }
            }
        } else {
            throw new RecordNotFoundException("Record Not Found!");
        }
    }

    @Override
    public void deleteUser(Long userId, LcHeaderBO headerBO) throws RecordNotFoundException {
        EcoUsersPK pk = new EcoUsersPK(headerBO.getC2Code(), headerBO.getBrCode(), helper.getString(userId));
        Optional<EcoUsers> users = ecoUsersRepo.findById(pk);
        if (users.isEmpty()) {
            log.debug(headerBO.getC2Code() + ":" +  userId + ":" + headerBO.getBrCode());
            throw new RecordNotFoundException("Eco users not found");
        } else {
            EcoUsers ecoUsers = users.get();
            ecoUsersRepo.delete(ecoUsers);
        }
        Optional<LcUser> user = lcUserRepo.findById(userId);
        if (user.isEmpty()) {
            throw new RecordNotFoundException("LcUser Record not found ");
        }
        LcUser lcUser = user.get();
        lcUserRepo.delete(lcUser);
        lcUserTypeRepo.deleteAll(lcUserTypeRepo.countMobileNumber(lcUser.getMobileNumber()));
    }

    @Override
    public void deleteBranchUser(Long nUserId, Long branchId, String c2Code) throws RecordNotFoundException {

        EcoUsersPK pk = new EcoUsersPK(c2Code, helper.getString(branchId), helper.getString(nUserId));
        Optional<EcoUsers> users = ecoUsersRepo.findById(pk);
        if (users.isEmpty()) {
            log.debug(c2Code + ":" +  nUserId + ":" + branchId);
            throw new RecordNotFoundException("Eco users not found");
        } else {
            EcoUsers ecoUsers = users.get();
            ecoUsersRepo.delete(ecoUsers);
        }
        Optional<LcUser> user = lcUserRepo.findById(nUserId);
        if (user.isEmpty()) {
            throw new RecordNotFoundException("LcUser Record not found ");
        }
        LcUser lcUser = user.get();
        lcUserRepo.delete(lcUser);
        lcUserTypeRepo.deleteAll(lcUserTypeRepo.countMobileNumber(lcUser.getMobileNumber()));
    }

    @Override
    public void updateStatus(long userId, String status) {
        Optional<LcUser> lcUser = lcUserRepo.findById(userId);
        if (lcUser.isPresent()) {
            LcUser user = lcUser.get();
            user.setStatus(status);
            lcUserRepo.save(user);
        }
    }

    @Override
    public LcUser getUserIdByMobile(String mobile) {
        return lcUserRepo.findByMobileNumber(mobile);
    }

    @Override
    public int count(String c2Code, SearchBO searchBO, JsonObject req) throws RecordNotFoundException {
        List<Long> userIdList = new ArrayList<>();
        List<JsonObject> resultList = new ArrayList<>();
        //LocalDateTime fromDate = null;
        //LocalDateTime toDate = null;
        List<LcUser> list = lcUserRepo.getByC2Code(c2Code, null, null);

        setResultListForCustomer(searchBO, req, userIdList, resultList, list);
        return resultList.size();
    }

    @Override
    public int getLoginCount(String c2Code, String brCode, String terminalId, String type) {
        List<EcoAuthSession> sessions = ecoAuthSessionRepository.findUserSessions(c2Code, brCode, terminalId, type);
        return sessions.size();
    }
}
