package com.c2.lc.ms.customer.transactions;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.bos.BranchListBo;
import com.c2.lc.ms.customer.bos.ListUserModelBO;
import com.c2.lc.ms.customer.bos.UserModelBO;
import com.c2.lc.ms.customer.bos.UserProfileResponseBo;
import com.c2.lc.ms.customer.entities.comm.EcoUsers;
import com.c2.lc.ms.customer.entities.comm.LcUser;
import com.c2.lc.ms.customer.entities.customer.ContactDetailEntity;
import com.c2.lc.ms.customer.entities.customer.FirmEntity;
import com.c2.lc.ms.customer.entities.customer.FirmUserRoleEntity;
import com.c2.lc.ms.customer.entities.customer.UserDetailEntity;
import com.c2.lc.ms.customer.services.interfaces.*;
import com.c2.lc.ms.customer.transactions.base.LcBaseTransactionImpl;
import com.c2.lc.ms.customer.transactions.interfaces.FirmUserTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FirmUserTransactionImpl extends LcBaseTransactionImpl implements FirmUserTransaction {

    @Autowired
    private LcUserService lcUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private FirmService firmService;
    @Autowired
    private FirmUserService firmUserService;
    @Autowired
    private FirmUserRoleService firmUserRoleService;
    @Autowired
    private ContactDetailService contactDetailService;
    @Autowired
    private FirmDefaultService firmDefaultService;
    @Autowired
    private FirmBranchService firmBranchService;


    @Override
    public boolean doesExistMobileNo(String mobileNo) {
        return firmUserService.doesExistMobileNo(mobileNo);
    }

    @Override
    public boolean doesExistMobileNoForParent(String cMobileNo, Long firmId) {
        return firmUserService.doesExistMobileNoForParent(cMobileNo, firmId);
    }

    @Override
    public int getCount(Long firmId, Long userId) {
        return firmUserService.getCount(firmId, userId);
    }

    @Override
    public void mobilenoUpdate(Long userId, String c_mobile_no) throws RecordNotFoundException {
        firmUserService.mobilenoUpdate(userId, c_mobile_no);
    }

    public JsonArray getUser(LcHeaderBO headerBO, String mobileNo) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException, NoSuchAlgorithmException, InvalidKeyException, DuplicateRecordException {
        JsonObject json = new JsonObject();
        PageBO page = new PageBO();
        page.setPage(0);
        page.setLimit(Integer.MAX_VALUE);
        JsonArray jsonArray = null;
        List<UserModelBO> userModelBOList = new ArrayList<>();
        UserModelBO userModelBO =new UserModelBO();
        List<BranchListBo> branchList = firmBranchService.getListOfBranch(headerBO,page);
        List<Object[]> usersList = userService.getUSer(mobileNo);
        if (usersList == null) {
            throw new RecordNotFoundException( "RECORD_NOT_FOUND");
        }
        for (Object[] objects : usersList) {
            userModelBO = new UserModelBO();
            userModelBO.setCMobileNo(helper.getString(objects[1]));
            userModelBO.setCName(helper.getString(objects[2]));
            userModelBOList.add(userModelBO);
        }

        for(BranchListBo branch: branchList) {
            log.debug(branch.getBranchCode());
            for (UserModelBO user : userModelBOList) {
                json = new JsonObject();
                jsonArray = new JsonArray();
                json.addProperty("c_view_trans_status",Constants.STATUS_YES);
                json.addProperty("c_place_order_status",Constants.STATUS_YES);
                json.addProperty("c_order_value_limit",100000);
                json.addProperty("c_time_limit","month");
                json.addProperty("n_min_value",0);
                json.addProperty("n_firm_id",branch.getFirmId());

                jsonArray.add(json);
                user.setUserRoles(jsonArray);
                ContactDetailEntity contactDetailEntity = contactDetailService.createContactDetail(headerBO.getUserId(), user);
                UserDetailEntity userDetailEntity = userService.createUser(headerBO.getUserId(), contactDetailEntity, user);

                if(!lcUserService.checkMobileNumberWithTypeExists(user.getCMobileNo(), Constants.ROLE_BUYER )) {
                    EcoUsers ecoUsers = getEcoUsers(user, userDetailEntity);
                    ecoUsers.setStatus(Constants.STATUS_ACTIVE);
                    FirmEntity firmEntity = firmService.getFirmById(branch.getFirmId());
                    lcUserService.saveEcoUsers(userDetailEntity.getNUserId(), headerBO.getFirmId(), ecoUsers, firmEntity.getC2Code(), firmEntity.getBrCode());

                    firmDefaultService.setDefaultFirm(userDetailEntity.getNUserId(), headerBO.getFirmId());
                }

                firmUserService.addUserToFirm(headerBO.getUserId(), branch.getFirmId(), userDetailEntity.getNUserId(), Constants.STATUS_ACTIVE);
                firmUserRoleService.addRoleToUser(headerBO.getUserId(), branch.getFirmId(), userDetailEntity.getNUserId(), user);
                userService.addUserToOwner(headerBO.getUserId(), userDetailEntity.getNUserId());
            }
        }
        return jsonArray;
    }

    ; //check mob exists for parent firm

    @Override
    public boolean isAlreadyRegisteredMobileNo(Long userId, String cMobileNo) throws RecordNotFoundException {
        UserDetailEntity userDetailEntity = userService.getById(userId);
        return cMobileNo.equals(userDetailEntity.getContactDetail().getCMobileNo());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public JsonObject createUser(Long userId, String brCode, Long firmId, UserModelBO model) throws CommunicationErrorException, InvalidRequestException, NoSuchAlgorithmException, InvalidKeyException, DuplicateRecordException, RecordNotFoundException {
        JsonObject json = new JsonObject();
        ContactDetailEntity contactDetailEntity = contactDetailService.createContactDetail(userId, model);
        UserDetailEntity userDetailEntity = userService.createUser(userId, contactDetailEntity, model);

        Long id = model.getUserRoles().get(0).getAsJsonObject().get("n_firm_id").getAsLong();

        EcoUsers ecoUsers = getEcoUsers(model, userDetailEntity);
        ecoUsers.setStatus(Constants.STATUS_ACTIVE);
      //  ecoUsers.setBrCode(helper.getString(firmId));
        FirmEntity firmEntity = firmService.getFirmById(id);
        lcUserService.saveEcoUsers(userDetailEntity.getNUserId(), id, ecoUsers, firmEntity.getC2Code(), brCode);

        for (JsonElement userRole : model.getUserRoles()) {
            firmUserService.addUserToFirm(userId, userRole.getAsJsonObject().get("n_firm_id").getAsLong(), userDetailEntity.getNUserId(), Constants.STATUS_ACTIVE);
            firmUserRoleService.addRoleToUser(userId, userRole.getAsJsonObject().get("n_firm_id").getAsLong(), userDetailEntity.getNUserId(), model);
        }

        userService.addUserToOwner(userId, userDetailEntity.getNUserId());
        firmDefaultService.setDefaultFirm(userDetailEntity.getNUserId(), id);

        return json;
    }

    private EcoUsers getEcoUsers(UserModelBO model, UserDetailEntity userDetailEntity) throws CommunicationErrorException, InvalidRequestException, NoSuchAlgorithmException, InvalidKeyException {
        LcUser lcUser = new LcUser();
        EcoUsers ecoUsers = new EcoUsers();
        String password = generatePassword();

        lcUser.setNId(userDetailEntity.getNUserId());
        lcUser.setMobileNumber(model.getCMobileNo());
//        lcUser.setPassword(password);
        lcUser.setType("B");
        lcUser.setLcUserStatus(Constants.STRING_VALUE_ZERO);

//        boolean flag = lcUserService.callC2PasswordService(lcUser, password);
        lcUserService.saveLcUserType(model.getCMobileNo(), "B");

//        if (!flag) {
//            ecoUsers.setPwd(password);
//            lcUser.setPassword(password);
//        } else {
            ecoUsers.setPwd("Password@123");
            lcUser.setPassword("Password@123");
//        }
        lcUserService.saveUser(lcUser);
        return ecoUsers;
    }

    private String generatePassword() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "").substring(0, 14) + "@A";
    }

    @Override
    public List<ListUserModelBO> getFirmUsers(Long userId, Long firmId, int page, int limit) throws RecordNotFoundException {
        return userService.getUserDetails(userId, firmId, page, limit);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteFirmUser(Long userId, Long firmId, LcHeaderBO header) throws RecordNotFoundException {
        firmUserService.deleteFirmUser(firmId, userId);
        List<FirmUserRoleEntity> firmUserRoleEntity = firmUserRoleService.getBranchesByUserId(userId);
        for (FirmUserRoleEntity userRole : firmUserRoleEntity) {
            firmUserRoleService.deleteById(userRole.getId());
        }
        lcUserService.deleteUser(userId,header);
         }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateUser(Long userId, Long firmId, UserModelBO userModelBO) throws RecordNotFoundException {
        List<FirmUserRoleEntity> list = firmUserRoleService.getUserRoleByUserId(userModelBO.getNUserId());
        for (FirmUserRoleEntity firmUserRoleEntity : list) {
            firmUserRoleService.deleteById(firmUserRoleEntity.getId());
        }

        UserDetailEntity userDetailEntity = userService.getById(userModelBO.getNUserId());
        ContactDetailEntity contactDetailEntity = contactDetailService.updateContactDetail(userId, userDetailEntity.getContactDetail().getNContactId(), userModelBO);
        userService.updateUser(userId, userModelBO, contactDetailEntity);

        for (JsonElement userRole : userModelBO.getUserRoles()) {
            firmUserService.addUserToFirm(userId, userRole.getAsJsonObject().get("n_firm_id").getAsLong(), userDetailEntity.getNUserId(), Constants.STATUS_ACTIVE);
            firmUserRoleService.addRoleToUser(userId, userRole.getAsJsonObject().get("n_firm_id").getAsLong(), userDetailEntity.getNUserId(), userModelBO);
        }
    }

    @Override
    public UserModelBO getUserDetail(Long userId, Long firmId) throws RecordNotFoundException {
        UserModelBO userModelBO = userService.getUserDetail(userId);
        JsonArray userRole = firmUserRoleService.getArrayUserRoleByUserId(userId);
        userModelBO.setUserRoles(userRole);
        return userModelBO;
    }

    @Override
    public UserProfileResponseBo getProfile(Long userId) throws RecordNotFoundException, InvalidRequestException {
        return firmUserService.getProfile(userId);
    }

    @Override
    public void saveRecentItem(Long userId, String cItemCode) {
        firmUserService.saveRecentItem(userId, cItemCode);
    }

    @Override
    public void clearRecentItem(Long userId) throws RecordNotFoundException {
        firmUserService.clearRecentItem(userId);
    }

    @Override
    public JsonObject getRecentItem(Long userId) throws RecordNotFoundException {
        return firmUserService.getRecentItems(userId);
    }

    @Override
    public void profielImageUpdate(Long userId, String c_profile_image_url) throws RecordNotFoundException {
        firmUserService.profielImageUpdate(userId,c_profile_image_url);
    }
}
