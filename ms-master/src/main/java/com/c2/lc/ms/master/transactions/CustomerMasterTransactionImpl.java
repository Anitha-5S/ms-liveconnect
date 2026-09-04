package com.c2.lc.ms.master.transactions;

import com.c2.lc.ms.master.services.interfaces.CustomerMasterService;
import com.c2.lc.ms.master.transactions.base.MasterBaseTransactionImpl;
import com.c2.lc.ms.master.transactions.interfaces.CustomerMasterTransaction;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CustomerMasterTransactionImpl extends MasterBaseTransactionImpl implements CustomerMasterTransaction {

    @Autowired
    CustomerMasterService customerMasterService;

    @Override
    public void customer(JsonObject data) throws InputPayloadException {
        customerMasterService.insert(data, 0, 1);
    }

    @Override
    public void supplier(JsonObject data) throws InputPayloadException {
        customerMasterService.insert(data, 1, 0);
    }

    @Override
    public void branch(JsonObject data) throws InputPayloadException {
        customerMasterService.insert(data, 0, 0);
    }

    @Override
    public void customerCreationAndMapping() {
        customerMasterService.customerCreationAndMapping();
    }

    @Override
    public void egCustomerCreationAndMapping() {
        customerMasterService.egCustomerCreationAndMapping();
    }

}
