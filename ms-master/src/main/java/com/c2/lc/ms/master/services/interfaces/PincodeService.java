package com.c2.lc.ms.master.services.interfaces;

import com.google.gson.JsonArray;

public interface PincodeService {
    JsonArray getStateByPincode(String pinCode);
}
