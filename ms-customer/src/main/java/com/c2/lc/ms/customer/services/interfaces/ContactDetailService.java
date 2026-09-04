package com.c2.lc.ms.customer.services.interfaces;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.interfaces.BaseService;
import com.c2.lc.ms.customer.bos.TSRegisterBO;
import com.c2.lc.ms.customer.bos.UserModelBO;
import com.c2.lc.ms.customer.entities.customer.ContactDetailEntity;

public interface ContactDetailService extends BaseService {
    ContactDetailEntity createContactDetail(Long pId, String mobileNo);

    ContactDetailEntity saveContactDetail(ContactDetailEntity contactDetailEntity) throws RecordNotFoundException;

    ContactDetailEntity createContactDetail(Long userId, UserModelBO model);

    ContactDetailEntity getContact(Long contactId) throws RecordNotFoundException;

    void deleteContact(Long n_contact_id);

    ContactDetailEntity getContactByMobile(String mobileNo);

    ContactDetailEntity updateContactDetail(Long userId, Long nContactId, UserModelBO userModelBO);

    ContactDetailEntity createTSContactDetail(long userId, TSRegisterBO registerBO);
}
