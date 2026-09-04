package com.c2.lc.ms.master.transactions;

import com.c2.lc.ms.master.services.interfaces.ItemDetailsMongoService;
import com.c2.lc.ms.master.transactions.interfaces.ItemDetailsMongoTransaction;
import com.c2.lc.ms.master.models.ItemDetailModel;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemDetailsMongoTransactionimpl implements ItemDetailsMongoTransaction {

    @Autowired
    ItemDetailsMongoService itemDetailsMongoService;

    @Override
    public ItemDetailModel getDetails(String itemCode) throws RecordNotFoundException, InvalidRequestException, CommunicationErrorException {
        return itemDetailsMongoService.getDetails(itemCode);
    }

    @Override
    public ItemDetailModel getSummary(String itemCode) throws RecordNotFoundException, InvalidRequestException, CommunicationErrorException {
        return itemDetailsMongoService.getSummary(itemCode);
    }

    @Override
    public JsonArray getSummaryByMfac(String mfacCode, Long pageNumber, Long rowLimit) throws RecordNotFoundException, CommunicationErrorException {
        return itemDetailsMongoService.getSummaryByMfac(mfacCode,pageNumber,rowLimit);
    }

    @Override
    public JsonArray getSummaryByContent(String contCode, Long pageNumber, Long rowLimit) throws RecordNotFoundException, CommunicationErrorException {
        return itemDetailsMongoService.getSummaryByContent(contCode,pageNumber,rowLimit);
    }
}
