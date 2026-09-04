package com.c2.lc.ms.customer.transactions.base;

import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.ms.customer.services.interfaces.FirmUserService;
import org.springframework.beans.factory.annotation.Autowired;

public class LcBaseTransactionImpl extends BaseTransactionImpl implements LcBaseTransaction {

    @Autowired private FirmUserService firmUserService;


    @Override
    public void validateRequest(Long userId, Long firmId) throws InvalidRequestException {
        firmUserService.validateRequest(firmId, userId);
    }

}
