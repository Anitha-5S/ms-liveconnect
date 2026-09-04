package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.ms.master.bos.MerchantOnBoardingBo;
import com.google.gson.JsonObject;

import java.util.Map;

public interface NewStoreRegistrationService {
    void registerMerchant(Map<String, String> headers);

    void saveMerchantStatus(JsonObject data);

    void storeRegistration();

    JsonObject getStoreDetails(String storeCode) throws Exception;

    void merchantCreation(MerchantOnBoardingBo merchantOnBoardingBo);
}
