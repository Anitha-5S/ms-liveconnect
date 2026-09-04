package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.ms.master.models.ItemSellersList;
import com.google.gson.JsonObject;

public interface SchemeService {

    JsonObject getSellerScheme(ItemSellersList itemSellersList);
}
