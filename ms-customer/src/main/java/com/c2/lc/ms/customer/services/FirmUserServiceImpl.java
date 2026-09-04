package com.c2.lc.ms.customer.services;

import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.bos.SaveLoginBO;
import com.c2.lc.ms.customer.bos.UserProfileResponseBo;
import com.c2.lc.ms.customer.entities.comm.LcUser;
import com.c2.lc.ms.customer.entities.customer.*;
import com.c2.lc.ms.customer.entities.customer.pk.FirmUserPKEntity;
import com.c2.lc.ms.customer.repos.comm.LcUserRepo;
import com.c2.lc.ms.customer.repos.comm.LcUserTypeRepo;
import com.c2.lc.ms.customer.repos.customer.*;
import com.c2.lc.ms.customer.services.base.LcBaseServiceImpl;
import com.c2.lc.ms.customer.services.interfaces.FirmUserService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Service
public class FirmUserServiceImpl extends LcBaseServiceImpl implements FirmUserService {


    @Autowired private FirmRepo firmRepo;
    @Autowired private FirmUserRepo firmUserRepo;
    @Autowired private UserDetailRepo userDetailRepo;
    @Autowired private ContactDetailRepo contactDetailRepo;
    @Autowired private LcUserTypeRepo lcUserTypeRepo;
   // @Autowired private WebClient.Builder webClientBuilder;
    @Autowired private LcUserRepo lcUserRepo;
    @Autowired private RecentSearchedItemsRepo recentSearchedItemsRepo;
    @Autowired private TSPinCodeReqRepo pinCodeReqRepo;

    @Override
    public void addUserToFirm(Long userId, Long firmId, Long addedUserId, String status) {
        FirmUserEntity firmUserEntity = new FirmUserEntity(userId, helper.getCurrentTime());
        FirmUserPKEntity pk = new FirmUserPKEntity(firmId, addedUserId);
        firmUserEntity.setId(pk);
        firmUserEntity.setCStatus(status);
        firmUserRepo.save(firmUserEntity);
    }

    @Override
    public void addFirmToUser(Long userId, Long newFirmId, String status) {
        FirmUserEntity firmUserEntity = new FirmUserEntity(userId, helper.getCurrentTime());
        FirmUserPKEntity pk = new FirmUserPKEntity(newFirmId, userId);
        firmUserEntity.setId(pk);
        firmUserEntity.setCStatus(status);
        firmUserRepo.save(firmUserEntity);
    }

    @Override
    public void validateRequest(Long firmId, Long userId) throws InvalidRequestException {
        FirmUserEntity firmUserEntity = firmUserRepo.getByFirmUser(firmId, userId);
        if (firmUserEntity == null || !firmUserEntity.getCStatus().equals(Constants.STATUS_ACTIVE)) {
            throw new InvalidRequestException(firmId + ":" + userId, "Invalid request!");
        }
    }

    private void saveLogin(Long profileId, String mobileNumber, String pwd, String url) throws InvalidRequestException, CommunicationErrorException {
        SaveLoginBO saveLoginBO = new SaveLoginBO();

        saveLoginBO.setN_profile_id(profileId);
        saveLoginBO.setC_user_id(mobileNumber);
        saveLoginBO.setC_mobile_no(mobileNumber);
        saveLoginBO.setC_password(pwd);

        String ret = this.callWebClientPostSyncApi(url, saveLoginBO);

        JsonObject jsonObject = this.stringToJson(ret);
        if (jsonObject != null) {
            if (jsonObject.get("appStatusCode").getAsInt() != 0) {
                throw new InvalidRequestException("", jsonObject.get("messages").getAsString());
            }
        } else {
            throw new CommunicationErrorException("Auth Service", "Service un-available");
        }
    }

    @Override
    public boolean doesExistMobileNo(String mobileNo) {
        return lcUserTypeRepo.checkMobileNumberWithTypeExists(mobileNo, "B") > 0;
    }

    @Override
    public void deleteFirmUser(Long firmId, Long userId) throws RecordNotFoundException {
        FirmUserPKEntity pk = new FirmUserPKEntity(firmId, userId);
        Optional<FirmUserEntity> user = firmUserRepo.findById(pk);
        if(user.isPresent()) {
            FirmUserEntity firmUserEntity = user.get();
            firmUserEntity.setCStatus(Constants.STATUS_INACTIVE);
            firmUserRepo.save(firmUserEntity);
        } else {
            throw new RecordNotFoundException("Record not found");
        }
    }

    @Override
    public boolean doesExistMobileNoForParent(String mobileNo, Long firmId) {
        String sql = "SELECT cd.c_mobile_no FROM contact_detail cd " +
                "JOIN " +
                "user_detail ud " +
                "ON " +
                "ud.n_contact_id  = cd.n_contact_id " +
                "JOIN " +
                "firm_user fu " +
                "ON " +
                "fu.n_user_id = ud.n_user_id " +
                "JOIN " +
                "firm f " +
                "ON " +
                "f.n_firm_id = :nFirmId " +
                "WHERE " +
                "cd.c_mobile_no = :cMobileNo ";

        Query query = this.getQuery(sql);
        query.setParameter("nFirmId", firmId);
        query.setParameter("cMobileNo", mobileNo);

        return !helper.isEmpty(this.getSingleResultNull(query));
    }

    @Override
    public int getCount(Long firmId, Long userId) {
        return firmUserRepo.getCount(firmId, userId);
    }

    @Override
    public int getBranchCount(Long userId) {
        return firmUserRepo.getBranchCount(userId);
    }

    @Override
    public void mobilenoUpdate(Long userId, String c_mobile_no) throws RecordNotFoundException {
        UserDetailEntity userDetailEntity = userDetailRepo.getByUserId(userId);
        ContactDetailEntity contactDetailEntity = contactDetailRepo.getByContactId(userDetailEntity.getContactDetail().getNContactId());
        if(contactDetailEntity==null){
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        contactDetailRepo.updateMobileByContactId(contactDetailEntity.getNContactId(), c_mobile_no);
        lcUserRepo.updateMobileByUserId(userId,c_mobile_no);
        //TODO add extra column to lc_user_type table.
        //lcUserTypeRepo.updateMobileNumber(c2code,c_mobile_no);

    }

    @Override
    public UserProfileResponseBo getProfile(Long userId) throws  RecordNotFoundException {
        UserProfileResponseBo userProfileResponseBo = new UserProfileResponseBo();
        UserDetailEntity userProfile = userDetailRepo.getByUserId(userId);
        if(userProfile == null){
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }

        LcUser lcUser = lcUserRepo.findById(userId).get();
        List<TSPinCodeReqEntity> entityList = pinCodeReqRepo.getByPin(lcUser.getC2Code(), userProfile.getContactDetail().getCPin());

        userProfileResponseBo.setCustCode(userProfile.getNUserId().toString());
        if(!helper.isEmpty(userProfile.getCLastName())){
            userProfileResponseBo.setCustomerName(userProfile.getCFirstName()==null?Constants.EMPTY_STRING:userProfile.getCFirstName()+" "+userProfile.getCLastName());
        }else{
            userProfileResponseBo.setCustomerName(userProfile.getCFirstName()==null?Constants.EMPTY_STRING:userProfile.getCFirstName());
        }
        userProfileResponseBo.setMobileNo(userProfile.getContactDetail().getCMobileNo()==null?Constants.EMPTY_STRING:userProfile.getContactDetail().getCMobileNo());
        userProfileResponseBo.setEmail(userProfile.getContactDetail().getCEmailId()==null?Constants.EMPTY_STRING:userProfile.getContactDetail().getCEmailId());
        userProfileResponseBo.setProfileImage(userProfile.getProfileImage()==null?Constants.EMPTY_STRING:userProfile.getProfileImage());

        userProfileResponseBo.setGender(userProfile.getGender()==null?Constants.EMPTY_STRING:userProfile.getGender());
        userProfileResponseBo.setDateOfBirth(userProfile.getDateOfBirth()==null?Constants.EMPTY_STRING:helper.convertDateToString(userProfile.getDateOfBirth()));
        userProfileResponseBo.setPinCode(userProfile.getContactDetail().getCPin()==null?Constants.EMPTY_STRING:userProfile.getContactDetail().getCPin());
        userProfileResponseBo.setFirmId(helper.getLong(lcUser.getBrCode()));
        userProfileResponseBo.setBrCode(lcUser.getBrCode());
        if (entityList.size() > 0) {
            userProfileResponseBo.setServiceActiveStatus(entityList.get(0).getServiceActiveStatus());
        } else {
            userProfileResponseBo.setServiceActiveStatus("Y");
        }
        return userProfileResponseBo;

    }

    @Override
    public List<FirmUserEntity> listUsers(Long branchId) {
        List<FirmUserEntity> userList = firmUserRepo.getUsers(branchId);
        return userList;
    }

    public void saveRecentItem(Long userId, String cItemCode) {
        Optional<RecentSearchedItemsEntity> recentItems = recentSearchedItemsRepo.findById(userId);
        JsonObject obj;
        JsonArray arr;
        if (recentItems.isEmpty()) {
            RecentSearchedItemsEntity entity = new RecentSearchedItemsEntity();
            arr = new JsonArray();
            obj = new JsonObject();
            arr.add(cItemCode);
            obj.add("j_item_codes", arr);
            entity.setNUserId(userId);
            entity.setJItemCode(helper.toString(obj));
            recentSearchedItemsRepo.save(entity);
        } else {
            JsonArray newArray = new JsonArray();
            obj = helper.getJsonObject(recentItems.get().getJItemCode());
            arr = obj.get("j_item_codes").getAsJsonArray();
            newArray.add(cItemCode);
            for (int i=0; i<arr.size(); i++) {
                if (arr.get(i).getAsString().equals(cItemCode)) {
                    arr.remove(i);
                }
                if (i<arr.size()) {
                    newArray.add(arr.get(i));
                }
                if (newArray.size() > 10) {
                    newArray.remove(newArray.size()-1);
                }
            }
            obj.add("j_item_codes", newArray);
            recentItems.get().setJItemCode(helper.toString(obj));
            recentSearchedItemsRepo.save(recentItems.get());
        }
    }

    @Override
    public void clearRecentItem(Long userId) throws RecordNotFoundException {
        Optional<RecentSearchedItemsEntity> recentItems = recentSearchedItemsRepo.findById(userId);
        if (recentItems.isEmpty()) {
            throw new RecordNotFoundException("Record Not Found!");
        }
        recentSearchedItemsRepo.deleteById(userId);
    }

    @Override
    public JsonObject getRecentItems(Long userId) throws RecordNotFoundException {
        Optional<RecentSearchedItemsEntity> recentItems = recentSearchedItemsRepo.findById(userId);
        if (recentItems.isEmpty()) {
            throw new RecordNotFoundException("Record Not Found!");
        }
        return helper.getJsonObject(recentItems.get().getJItemCode());
    }

    @Override
    public void profielImageUpdate(Long userId, String c_profile_image_url) throws RecordNotFoundException {
        UserDetailEntity userProfile = userDetailRepo.getByUserId(userId);
        if(userProfile == null){
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        userProfile.setProfileImage(c_profile_image_url);
        userDetailRepo.save(userProfile);
    }
}
