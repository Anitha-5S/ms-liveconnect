package com.c2.lc.ms.master.transactions;

import com.c2.lc.ms.master.services.interfaces.OrderSyncService;
import com.c2.lc.ms.master.transactions.interfaces.OrderSyncTransaction;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class OrderSyncTransactionImpl implements OrderSyncTransaction {

    @Autowired
    private OrderSyncService orderSyncService;

    @Override
    public JsonObject orderSync(JsonObject jsonObject) {

        return orderSyncService.orderSync(jsonObject);
    }
}
