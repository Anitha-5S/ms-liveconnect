package com.c2.lc.ms.customer.services;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.bos.AddressBo;
import com.c2.lc.ms.customer.entities.customer.ContactDetailEntity;
import com.c2.lc.ms.customer.repos.customer.ContactDetailRepo;
import com.c2.lc.ms.customer.services.base.LcBaseServiceImpl;
import com.c2.lc.ms.customer.services.interfaces.AddressService;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl extends LcBaseServiceImpl implements AddressService {

    @Autowired private ContactDetailRepo contactDetailRepo;
    @Override
    public void saveAddress(Long userId, AddressBo addressBo)  {
        ContactDetailEntity contactDetails = new ContactDetailEntity();
        contactDetails.setCAddressType(addressBo.getAddressType());
        contactDetails.setCAddress1(addressBo.getAddress1());
        contactDetails.setCAddress2(addressBo.getAddress2());
        contactDetails.setCContactName(addressBo.getCustomerName());
        contactDetails.setCCityName(addressBo.getCityName());
        contactDetails.setCStateName(addressBo.getStateName());
        contactDetails.setCLandmark(addressBo.getLandmark());
        contactDetails.setCPin(addressBo.getPincode());
        contactDetails.setCMobileNo(addressBo.getMobileNo());
        contactDetails.setAdrdressName(addressBo.getAdrdressName());

        contactDetails.setIdTime(userId, helper.getCurrentTime());
        contactDetails.setNUserId(userId);
        contactDetails.setCDeliveryAddressStatus(Constants.STATUS_NO);
        contactDetailRepo.save(contactDetails);
    }

    @Override
    public void updateAddress(Long userId, AddressBo addressBo) throws RecordNotFoundException {
        ContactDetailEntity contactDetails = contactDetailRepo.getByContactId(addressBo.getAddressId());
if(contactDetails == null){
    throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
}
        contactDetails.setCAddressType(addressBo.getAddressType());
        contactDetails.setCAddress1(addressBo.getAddress1());
        contactDetails.setCAddress2(addressBo.getAddress2());
        contactDetails.setCContactName(addressBo.getCustomerName());
        contactDetails.setCCityName(addressBo.getCityName());
        contactDetails.setCStateName(addressBo.getStateName());
        contactDetails.setCLandmark(addressBo.getLandmark());
        contactDetails.setCPin(addressBo.getPincode());
        contactDetails.setCMobileNo(addressBo.getMobileNo());
        contactDetails.setTLastUpdatedAt(helper.getCurrentTime());
        contactDetails.setNLastUpdatedBy(userId);
        contactDetails.setCDeliveryAddressStatus(Constants.STATUS_NO);
        contactDetails.setAdrdressName(addressBo.getAdrdressName());
        contactDetailRepo.save(contactDetails);
    }

    @Override
    public void setDeliveryAddress(Long userId,Long addresId) throws RecordNotFoundException {
        ContactDetailEntity contactDetails = contactDetailRepo.getByContactId(addresId);
        if(contactDetails == null){
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        contactDetailRepo.UpdateByUserId(userId,Constants.STATUS_NO);
        contactDetails.setCDeliveryAddressStatus(Constants.STATUS_YES);
        contactDetailRepo.save(contactDetails);

    }

    @Override
    public List<ContactDetailEntity> getaddressList(Long userId) {
        return contactDetailRepo.getByUserId(userId);
    }

    @Override
    public void deleteAddress(long c_add_id) throws RecordNotFoundException {
        ContactDetailEntity contactDetails = contactDetailRepo.getByContactId(c_add_id);
        if(contactDetails == null){
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        contactDetailRepo.delete(contactDetails);
    }


}

