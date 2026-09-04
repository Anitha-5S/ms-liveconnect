package com.c2.lc.ms.master.transactions.interfaces;

import com.c2.lc.ms.master.transactions.interfaces.base.MasterBaseTransaction;
import com.c2.lc.ms.master.models.ItemDetailModel;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.google.gson.JsonArray;

public interface ItemDetailsMongoTransaction extends MasterBaseTransaction {

    ItemDetailModel getDetails(String itemCode) throws RecordNotFoundException, InvalidRequestException, CommunicationErrorException;

    ItemDetailModel getSummary(String itemCode) throws RecordNotFoundException, InvalidRequestException, CommunicationErrorException;

    JsonArray getSummaryByMfac(String mfacCode, Long pageNumber, Long rowLimit) throws RecordNotFoundException, CommunicationErrorException;
    JsonArray getSummaryByContent(String conCode, Long pageNumber, Long rowLimit) throws RecordNotFoundException, CommunicationErrorException;
}
