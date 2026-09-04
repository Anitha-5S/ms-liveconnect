package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.google.gson.JsonObject;

public interface CustomerMasterService extends MasterBaseService {
    void insert(JsonObject data, int suppFlag, int custFlag) throws InputPayloadException;
    void customerCreationAndMapping();
    void egCustomerCreationAndMapping();

}
