package com.c2.lc.ms.master.transactions.interfaces;

import com.c2.lc.ms.master.transactions.interfaces.base.MasterBaseTransaction;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.google.gson.JsonObject;

public interface CustomerMasterTransaction extends MasterBaseTransaction {

    void customer(JsonObject object) throws InputPayloadException;
    void supplier(JsonObject object) throws InputPayloadException;
    void branch(JsonObject object) throws InputPayloadException;
    void customerCreationAndMapping();
    void egCustomerCreationAndMapping();

}
