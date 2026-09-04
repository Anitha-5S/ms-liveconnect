package com.c2.lc.ms.customer.transactions.base;

import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.transactions.interfaces.BaseTransaction;

public interface LcBaseTransaction extends BaseTransaction {
    void validateRequest(Long userId, Long firmId) throws InvalidRequestException;

}
