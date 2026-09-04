package com.c2.lc.ms.master.transactions.interfaces;

import com.google.gson.JsonObject;

public interface OrderSyncTransaction {
    
    JsonObject orderSync(JsonObject jsonObject);
}
