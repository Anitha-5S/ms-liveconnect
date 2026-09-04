package com.c2.lc.ms.customer.services;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.bos.TSRegisterBO;
import com.c2.lc.ms.customer.bos.UserModelBO;
import com.c2.lc.ms.customer.entities.customer.ContactDetailEntity;
import com.c2.lc.ms.customer.repos.customer.ContactDetailRepo;
import com.c2.lc.ms.customer.services.base.LcBaseServiceImpl;
import com.c2.lc.ms.customer.services.interfaces.ContactDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class ContactDetailServiceImpl extends LcBaseServiceImpl implements ContactDetailService {

    @Autowired private ContactDetailRepo contactDetailRepo;
    @PersistenceContext(unitName = "mysql")
    @Autowired private EntityManager entityManager;

    @Override
    public ContactDetailEntity createContactDetail(Long pId, String mobileNo) {
        ContactDetailEntity contactDetailEntity = new ContactDetailEntity(pId, helper.getCurrentTime());
        contactDetailEntity.setCMobileNo(mobileNo);
        return contactDetailRepo.save(contactDetailEntity);
    }

    @Override
    public ContactDetailEntity saveContactDetail(ContactDetailEntity contactDetailEntity) throws RecordNotFoundException {
        List<Object[]> location;
        String sql = "SELECT pm.c_state, pm.c_state_code, ugcm.c_code AS city_code, ugcm.c_name AS city_name, ugam.c_code AS area_code, ugam.c_name AS area_name " +
                "   FROM pincode_mst pm " +
                "   JOIN u_geo_district_mst ugdm ON ugdm.c_name = pm.c_district " +
                "   JOIN u_geo_city_mst ugcm ON ugdm.c_code = ugcm.c_geo_district_code AND ugcm.c_name = pm.c_district" +
                "   LEFT JOIN u_geo_area_mst ugam ON ugam.c_geo_city_code = ugcm.c_code " +
                "   WHERE pm.c_code = :pinCode";

        Query locQuery = entityManager.createNativeQuery(sql);
        locQuery.setParameter("pinCode", contactDetailEntity.getCPin());
        location = this.getResultList(locQuery);

        if (!location.isEmpty()) {
            for (Object[] objects : location) {
                contactDetailEntity.setCStateName(helper.getString(objects[0]).equals("") ? null : helper.getString(objects[0]));
                contactDetailEntity.setCStateCode(helper.getString(objects[1]).equals("") ? null : helper.getString(objects[1]));
                contactDetailEntity.setCCityCode(helper.getString(objects[2]).equals("") ? null : helper.getString(objects[2]));
                contactDetailEntity.setCCityName(helper.getString(objects[3]).equals("") ? null : helper.getString(objects[3]));
                contactDetailEntity.setCAreaCode(helper.getString(objects[4]).equals("") ? null : helper.getString(objects[4]));
                contactDetailEntity.setCAreaName(helper.getString(objects[5]).equals("") ? null : helper.getString(objects[5]));
            }
        }
        return contactDetailRepo.save(contactDetailEntity);
    }

    @Override
    public ContactDetailEntity createContactDetail(Long userId, UserModelBO customer) {
        ContactDetailEntity contactDetailEntity = new ContactDetailEntity(userId, helper.getCurrentTime());
        contactDetailEntity.setCMobileNo(customer.getCMobileNo());
        contactDetailEntity.setCEmailId(customer.getCEmail());
        contactDetailEntity.setCAddress1(customer.getCAddress1());
        contactDetailEntity.setCAddress2(customer.getCAddress2());
        contactDetailEntity.setCAreaName(customer.getCAreaName());
        contactDetailEntity.setCCityName(customer.getCCityName());
        contactDetailEntity.setCStateName(customer.getCStateName());
        contactDetailEntity.setCAreaCode(customer.getCAreaCode());
        contactDetailEntity.setCCityCode(customer.getCCityCode());
        contactDetailEntity.setCStateCode(customer.getCStateCode());
        contactDetailEntity.setCPin(customer.getCPincode());
        return contactDetailRepo.save(contactDetailEntity);
    }

    @Override
    public ContactDetailEntity getContact(Long contactId) throws RecordNotFoundException {
        return contactDetailRepo.findById(contactId)
                .orElseThrow(() -> new RecordNotFoundException(contactId, "Record not found!"));
    }

    @Override
    public void deleteContact(Long contactId) {
        contactDetailRepo.deleteById(contactId);
    }

    @Override
    public ContactDetailEntity getContactByMobile(String mobileNo) {
        return contactDetailRepo.getByMobileNo(mobileNo);
    }

    @Override
    public ContactDetailEntity updateContactDetail(Long userId, Long nContactId, UserModelBO userModelBO) {
        ContactDetailEntity contactDetailEntity = contactDetailRepo.getOne(nContactId);
        contactDetailEntity.setCMobileNo(userModelBO.getCMobileNo());
        contactDetailEntity.setCEmailId(userModelBO.getCEmail());
        contactDetailEntity.setCAddress1(userModelBO.getCAddress1());
        contactDetailEntity.setCAddress2(userModelBO.getCAddress2());
        contactDetailEntity.setCAreaName(userModelBO.getCAreaName());
        contactDetailEntity.setCCityName(userModelBO.getCCityName());
        contactDetailEntity.setCStateName(userModelBO.getCStateName());
        contactDetailEntity.setCAreaCode(userModelBO.getCAreaCode());
        contactDetailEntity.setCCityCode(userModelBO.getCCityCode());
        contactDetailEntity.setCStateCode(userModelBO.getCStateCode());
        contactDetailEntity.setCPin(userModelBO.getCPincode());
        contactDetailEntity.setTLastUpdatedAt(helper.getCurrentTime());
        contactDetailEntity.setNLastUpdatedBy(userId);
        return contactDetailRepo.save(contactDetailEntity);
    }

    @Override
    public ContactDetailEntity createTSContactDetail(long userId, TSRegisterBO registerBO) {
        ContactDetailEntity contactDetailEntity = new ContactDetailEntity(userId, helper.getCurrentTime());
        contactDetailEntity.setCMobileNo(registerBO.getMobileNumber());
        contactDetailEntity.setCEmailId(registerBO.getEmail());
        contactDetailEntity.setCContactName(registerBO.getName());
        contactDetailEntity.setCPin(registerBO.getPinCode());
        contactDetailEntity.setNUserId(userId);
        return contactDetailRepo.save(contactDetailEntity);
    }
}
