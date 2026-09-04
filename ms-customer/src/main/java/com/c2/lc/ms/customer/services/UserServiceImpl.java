package com.c2.lc.ms.customer.services;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.bos.ListUserModelBO;
import com.c2.lc.ms.customer.bos.TSRegisterBO;
import com.c2.lc.ms.customer.bos.UserModelBO;
import com.c2.lc.ms.customer.entities.customer.*;
import com.c2.lc.ms.customer.repos.customer.FirmRepo;
import com.c2.lc.ms.customer.repos.customer.FirmUserRepo;
import com.c2.lc.ms.customer.repos.customer.UserDetailRepo;
import com.c2.lc.ms.customer.repos.customer.UserOwnerRepo;
import com.c2.lc.ms.customer.services.base.LcBaseServiceImpl;
import com.c2.lc.ms.customer.services.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl extends LcBaseServiceImpl implements UserService {

    @Autowired private FirmRepo firmRepo;
    @Autowired private FirmUserRepo firmUserRepo;
    @Autowired private UserDetailRepo userDetailRepo;
    @Autowired private UserOwnerRepo userOwnerRepo;
    @PersistenceContext(unitName = "mysql")
    @Autowired
    private EntityManager entityManager;

    @Override
    public UserDetailEntity createUser(Long userId, ContactDetailEntity contactDetailEntity) {
        UserDetailEntity userDetailEntity = new UserDetailEntity(userId, helper.getCurrentTime());
        userDetailEntity.setContactDetail(contactDetailEntity);
        userDetailEntity.setCStatus(Constants.STATUS_ACTIVE);
        return userDetailRepo.save(userDetailEntity);
    }

    @Override
    public UserDetailEntity saveCustomer(UserDetailEntity customer) {
        return userDetailRepo.save(customer);
    }

    @Override
    public UserDetailEntity createUser(Long userId, ContactDetailEntity contactDetailEntity, UserModelBO model) {
        UserDetailEntity userDetailEntity = new UserDetailEntity(userId, helper.getCurrentTime());

        userDetailEntity.setContactDetail(contactDetailEntity);
        userDetailEntity.setCStatus(Constants.STATUS_ACTIVE);
        userDetailEntity.setCFirstName(model.getCName());
        return userDetailRepo.save(userDetailEntity);
    }

    @Override
    public UserDetailEntity getById(Long userId) throws RecordNotFoundException {
        return userDetailRepo.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(userId, "Record not found!"));
    }

    @Override
    public UserDetailEntity updateUser(Long userId, UserModelBO userModelBO, ContactDetailEntity contactDetailEntity) {
        UserDetailEntity userDetailEntity = userDetailRepo.getOne(userModelBO.getNUserId());
        userDetailEntity.setContactDetail(contactDetailEntity);
        userDetailEntity.setCStatus(Constants.STATUS_ACTIVE);
        userDetailEntity.setCFirstName(userModelBO.getCName());
        userDetailEntity.setTLastUpdatedAt(helper.getCurrentTime());
        userDetailEntity.setNLastUpdatedBy(userId);
        return userDetailRepo.save(userDetailEntity);
    }

    @Override
    public List<ListUserModelBO> getUserDetailsByFirmId(Long userId, Long firmId) {
        Long parentId = checkAndGetParentFirmId(firmId);
        FirmEntity firmEntity = firmRepo.getOne(parentId);
        List<ListUserModelBO> list = new ArrayList<>();
        List<FirmUserEntity> firmUserEntities = firmEntity.getFirmUserEntities();
        if (firmUserEntities != null) {
            for (FirmUserEntity user : firmUserEntities) {
                if (user.getId().getNUserId().longValue() == userId.longValue())
                    continue;

                ListUserModelBO model = new ListUserModelBO();

                UserDetailEntity userDetailEntity = user.getUserDetail();
                model.setUserId(userDetailEntity.getNUserId());
                model.setUserName(userDetailEntity.getCFirstName());

                ContactDetailEntity contactDetailEntity = userDetailEntity.getContactDetail();
                model.setEmailId(contactDetailEntity.getCEmailId());
                model.setMobileNo(contactDetailEntity.getCMobileNo());

                list.add(model);
            }
        }
        return list;
    }

    protected Long checkAndGetParentFirmId(Long firmId) {
        return firmId;
    }

    @Override
    public UserModelBO getUserDetail(Long uId) throws RecordNotFoundException {
        UserDetailEntity userDetailEntity = getById(uId);
        UserModelBO userModelBO = new UserModelBO();
        userModelBO.setNUserId(uId);
        userModelBO.setCName(userDetailEntity.getCFirstName());
        userModelBO.setCPincode(userDetailEntity.getContactDetail().getCPin());
        userModelBO.setCCityName(userDetailEntity.getContactDetail().getCCityName());
        userModelBO.setCEmail(userDetailEntity.getContactDetail().getCEmailId());
        userModelBO.setCStateName(userDetailEntity.getContactDetail().getCStateName());
        userModelBO.setCAddress1(userDetailEntity.getContactDetail().getCAddress1());
        userModelBO.setCAddress2(userDetailEntity.getContactDetail().getCAddress2());
        userModelBO.setCMobileNo(userDetailEntity.getContactDetail().getCMobileNo());
        userModelBO.setCAreaName(userDetailEntity.getContactDetail().getCAreaName());
        userModelBO.setCAreaCode(userDetailEntity.getContactDetail().getCAreaCode());
        userModelBO.setCCityCode(userDetailEntity.getContactDetail().getCCityCode());
        userModelBO.setCStateCode(userDetailEntity.getContactDetail().getCStateCode());
        return userModelBO;
    }

    @Override
    public List<ListUserModelBO> getUserDetails(Long userId, Long firmId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Long parentId = checkAndGetParentFirmId(firmId);
        List<ListUserModelBO> list = new ArrayList<>();
        List<FirmUserEntity> firmUserEntities = firmUserRepo.getByFirmAndUserId(parentId, userId, pageable);
        if (firmUserEntities != null) {
            for (FirmUserEntity user : firmUserEntities) {

                ListUserModelBO model = new ListUserModelBO();

                UserDetailEntity userDetailEntity = user.getUserDetail();
                model.setUserId(userDetailEntity.getNUserId());
                model.setUserName(userDetailEntity.getCFirstName());

                ContactDetailEntity contactDetailEntity = userDetailEntity.getContactDetail();
                model.setEmailId(contactDetailEntity.getCEmailId());
                model.setMobileNo(contactDetailEntity.getCMobileNo());

                list.add(model);
            }
        }
        return list;
    }

    @Override
    public void deleteUser(Long userId) {
        userDetailRepo.deleteById(userId);
    }

    @Override
    public void addUserToOwner(Long parentUserId, Long childUserId) {
        UserOwnerEntity userOwnerEntity = new UserOwnerEntity(parentUserId, helper.getCurrentTime());
        userOwnerEntity.setParentUser(parentUserId);
        userOwnerEntity.setChildUser(childUserId);
        userOwnerEntity.setCStatus(Constants.STATUS_ACTIVE);
        userOwnerRepo.save(userOwnerEntity);
    }

    @Override
    public UserOwnerEntity getParentUser(Long userId) throws RecordNotFoundException {
        return userOwnerRepo.getParent(userId);
    }

    @Override
    public UserDetailEntity createTSUser(long userId, ContactDetailEntity contactDetailEntity, TSRegisterBO registerBO) {
        UserDetailEntity userDetailEntity = new UserDetailEntity(userId, helper.getCurrentTime());
        userDetailEntity.setContactDetail(contactDetailEntity);
        userDetailEntity.setDateOfBirth(helper.getLocalDate(registerBO.getDateOfBirth()));
        userDetailEntity.setGender(registerBO.getGender());
        userDetailEntity.setCFirstName(registerBO.getName());
        userDetailEntity.setCStatus(Constants.STATUS_ACTIVE);
        return userDetailRepo.save(userDetailEntity);
    }

    public List<Object[]> getUSer(String mobileNo) {
        //JsonArray jsonArray = new JsonArray();
        String sql = getUser();
        Query query =entityManager.createNativeQuery(sql);
        query.setParameter("mobile",mobileNo);
        List<Object[]> resultList = this.getResultList(query);

        return resultList ;
    }

    private String getUser() {
        return "SELECT mdet.c_org_mob_no AS orgMobNo,mdet.c_new_mob_no AS newMobNo,mdet.c_name AS uname,mdet.n_add_new_mob AS addUserRight, "   +
                "  'Buyer' role FROM lc_add_mob_det mdet  "   +
                "  WHERE mdet.c_org_mob_no = :mobile AND mdet.n_active = 0 "   +
                "  union  "   +
                "  select adet.n_mobile_no AS orgMobNo, amst.n_mobile_no as newMobNo,amst.c_user_name as uname,0 AS addUserRight,'Auditor' role "   +
                "  FROM lc_auditor_mst amst "   +
                "  join lc_auditor_det adet on amst.n_id = adet.n_mst_id  "   +
                "  WHERE adet.n_mobile_no = :mobile AND adet.n_active_flag = 1"   ;
    }

    @Override
    public void updateStatus(long userId, String status) {
        UserDetailEntity userDetailEntity = userDetailRepo.getByUserId(userId);
        userDetailEntity.setCStatus(status);
        userDetailRepo.save(userDetailEntity);
    }
}
