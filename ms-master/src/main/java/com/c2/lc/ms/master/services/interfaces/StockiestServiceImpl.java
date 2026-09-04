package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.ms.master.entities.mysql.UStockiestItemEntity;
import com.c2.lc.ms.master.entities.mysql.UStockiestItemEntityPK;
import com.c2.lc.ms.master.repos.mysql.UStockiestItemRepository;
import com.c2.lc.lib.base.BaseSuper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StockiestServiceImpl extends BaseSuper implements StockiestService{

    @Autowired
    UStockiestItemRepository stockiestItemRepository;

    @Override
    public UStockiestItemEntity saveStockiestDetails(String c2Code, String itemCode, String cUcode, Integer quantityPerBox) {

        UStockiestItemEntity stockitem = new UStockiestItemEntity();
        UStockiestItemEntityPK pk = new UStockiestItemEntityPK();

        pk.setCStockiestCode(c2Code);
        pk.setCStockiestItemCode(itemCode);
        stockitem.setPK(pk);
        if (cUcode!=null)
            stockitem.setCUcode(cUcode);
        stockitem.setDLdate(helper.getCurrentTime());
        stockitem.setNSuppQb(quantityPerBox);
        stockiestItemRepository.save(stockitem);
        return stockitem;
    }

}
