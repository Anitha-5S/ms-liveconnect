package com.c2.lc.ms.master.transactions;

import com.c2.lc.ms.master.services.interfaces.NetmedsItemsService;
import com.c2.lc.ms.master.transactions.base.MasterBaseTransactionImpl;
import com.c2.lc.ms.master.transactions.interfaces.NetmedsItemPushTransaction;
import com.c2.lc.ms.master.services.NetmedsApiCallService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class NetmedsItemPushTransactionImpl extends MasterBaseTransactionImpl implements NetmedsItemPushTransaction {

    @Autowired
    private NetmedsApiCallService netmedsApiCallService;

    @Autowired
    private NetmedsItemsService netmedsItemsService;

    @Override
    public JsonObject netmedsItemPush(String date) {

        JsonObject response = new JsonObject();

        String itemsToPushDate = getItemsToPushDate(date);
        int recordIndex = 0;
        int recordCount = 25;

        int itemsCount = netmedsItemsService.getItemsCount(itemsToPushDate);

        boolean loop = true;
        while (loop) {

            JsonArray items = netmedsItemsService.getItems(recordIndex, recordCount, itemsToPushDate);

            if (items != null && items.size() > 0) {
                String apiCallResponse = netmedsApiCallService.makeApiCall(items);
            }

            if (recordIndex > itemsCount) {
                loop = false;
            }
            recordIndex = recordIndex + 25;
        }

        response.addProperty("c_item_count",itemsCount);
        response.addProperty("d_date",itemsToPushDate);
        return response;
    }

    private String getItemsToPushDate(String date) {
        String response;
        if (helper.isEmpty(date)) {
            response = helper.getCurrentDateString();
        } else {
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            response = parsedDate.toString();
        }
        return response;
    }
}
