package com.c2.lc.ms.master.transactions;


import com.c2.lc.ms.master.bos.MerchantOnBoardingBo;
import com.c2.lc.ms.master.services.interfaces.NewStoreRegistrationService;
import com.c2.lc.ms.master.transactions.interfaces.NewStoreRegistrationTransaction;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NewStoreRegistrationTransactionImpl implements NewStoreRegistrationTransaction {
    @Autowired
    private NewStoreRegistrationService newStoreRegistrationService;

    @Override
    public void registerMerchant(Map<String, String> headers) {
        newStoreRegistrationService.registerMerchant(headers);
    }

    @Override
    public void saveMerchantStatus(JsonObject data) {
        newStoreRegistrationService.saveMerchantStatus(data);
    }

    @Override
    public void storeRegistration() {
        newStoreRegistrationService.storeRegistration();
    }

    @Override
    public JsonObject getStoreDetails(String storeCode) throws Exception {
        return newStoreRegistrationService.getStoreDetails(storeCode);
    }

    @Override
    public void merchantCreation(MerchantOnBoardingBo merchantOnBoardingBo) {
        newStoreRegistrationService.merchantCreation(merchantOnBoardingBo);
    }
}
