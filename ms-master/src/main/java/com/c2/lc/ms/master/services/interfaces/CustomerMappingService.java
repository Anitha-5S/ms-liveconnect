package com.c2.lc.ms.master.services.interfaces;

import com.google.gson.JsonObject;

public interface CustomerMappingService {
    void customerMappingCreation(String egCustomerId, String egC2Code, String uCode);
    void createCustomerDetails(String netMedWarehouseC2Code, String partyId, JsonObject customerDetails);
}
