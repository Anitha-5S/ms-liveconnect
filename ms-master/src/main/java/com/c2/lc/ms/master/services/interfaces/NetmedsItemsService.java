package com.c2.lc.ms.master.services.interfaces;

import com.google.gson.JsonArray;

public interface NetmedsItemsService {
    JsonArray getItems(int recordIndex, int recordCount, String itemsToPushDate);

    int getItemsCount(String itemsToPushDate);
}
