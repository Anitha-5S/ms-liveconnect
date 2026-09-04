package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.c2.lc.ms.master.entities.mysql.UStockiestItemEntity;

public interface StockiestService extends MasterBaseService {

    UStockiestItemEntity saveStockiestDetails(String c2Code, String itemCode, String cUcode, Integer quantityPerBox);
}
