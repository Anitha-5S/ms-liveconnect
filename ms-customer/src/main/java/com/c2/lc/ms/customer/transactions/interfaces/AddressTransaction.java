package com.c2.lc.ms.customer.transactions.interfaces;

import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.bos.AddressBo;
import com.c2.lc.ms.customer.entities.customer.ContactDetailEntity;
import com.c2.lc.ms.customer.transactions.base.LcBaseTransaction;

import java.util.List;

public interface AddressTransaction extends LcBaseTransaction {
    void saveAddress(Long userId, AddressBo addressBo) throws DuplicateRecordException;

    void updateAddress(Long userId, AddressBo addressBo) throws RecordNotFoundException;

    void setDeliveryAddress(Long userId, Long addresId) throws RecordNotFoundException;

    List<ContactDetailEntity> addressList(Long userId);

    void deleteAddress(long c_add_id) throws RecordNotFoundException;
}
