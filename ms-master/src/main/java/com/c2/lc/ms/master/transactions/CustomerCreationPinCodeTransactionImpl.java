package com.c2.lc.ms.master.transactions;

import com.c2.lc.ms.master.services.interfaces.CustomerCreationPinCodeService;
import com.c2.lc.ms.master.services.interfaces.EgCustomerCreationPinCodeService;
import com.c2.lc.ms.master.transactions.interfaces.CustomerCreationPinCodeTransaction;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import com.c2.lc.lib.transactions.BaseTransactionImpl;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Log4j2
public class CustomerCreationPinCodeTransactionImpl extends BaseTransactionImpl implements CustomerCreationPinCodeTransaction {

    @Autowired
    CustomerCreationPinCodeService customerCreationPinCodeService;
    @Autowired
    EgCustomerCreationPinCodeService egCustomerCreationPinCodeService;

    @Override
    public void nmCustomerCreationPinCode() {
        customerCreationPinCodeService.nmCustomerCreationPinCode();
    }

    @Override
    public void egCustomerCreationPinCode() {
        egCustomerCreationPinCodeService.egCustomerCreationPinCode();
    }
}
