package com.c2.lc.ms.customer.transactions.interfaces;

import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.transactions.interfaces.BaseTransaction;
import com.c2.lc.ms.customer.entities.comm.EcoUsers;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface AuthTransaction extends BaseTransaction {

    void registerEcogreen(Long userId, EcoUsers ecoUsers) throws NoSuchAlgorithmException, InvalidKeyException, DuplicateRecordException;
}


