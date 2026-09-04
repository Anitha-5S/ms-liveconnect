package com.c2.lc.ms.customer.transactions;

import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.bos.AddressBo;
import com.c2.lc.ms.customer.entities.customer.ContactDetailEntity;
import com.c2.lc.ms.customer.services.interfaces.AddressService;
import com.c2.lc.ms.customer.transactions.base.LcBaseTransactionImpl;
import com.c2.lc.ms.customer.transactions.interfaces.AddressTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressTransactionImpl extends LcBaseTransactionImpl implements AddressTransaction {

    @Autowired private AddressService addressService;

    @Override
    public void saveAddress(Long userId, AddressBo addressBo) throws DuplicateRecordException {
        addressService.saveAddress(userId,addressBo);

    }

    @Override
    public void updateAddress(Long userId, AddressBo addressBo) throws RecordNotFoundException {
        addressService.updateAddress(userId,addressBo);
    }

    @Override
    public void setDeliveryAddress(Long userId, Long addressId) throws RecordNotFoundException {
        addressService.setDeliveryAddress(userId,addressId);

    }

    @Override
    public List<ContactDetailEntity> addressList(Long userId) {
        return addressService.getaddressList(userId);
    }

    @Override
    public void deleteAddress(long c_add_id) throws RecordNotFoundException {
        addressService.deleteAddress(c_add_id);
    }
}
