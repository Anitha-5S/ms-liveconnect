package com.c2.lc.ms.customer.services.interfaces;

import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.bos.AddressBo;
import com.c2.lc.ms.customer.entities.customer.ContactDetailEntity;
import com.c2.lc.ms.customer.services.interfaces.base.LcBaseService;

import java.util.List;

public interface AddressService extends LcBaseService {
    void saveAddress(Long userId, AddressBo addressBo) throws DuplicateRecordException;

    void updateAddress(Long userId, AddressBo addressBo) throws RecordNotFoundException;

    void setDeliveryAddress(Long userId, Long addresId) throws RecordNotFoundException;

    List<ContactDetailEntity> getaddressList(Long userId);

    void deleteAddress(long c_add_id) throws RecordNotFoundException;
}
