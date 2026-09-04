package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.c2.lc.ms.master.models.ItemDetailModel;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.google.gson.JsonArray;

import java.util.List;

public interface ItemDetailsMongoService extends MasterBaseService {

    ItemDetailModel getDetails(String itemCode) throws RecordNotFoundException, InvalidRequestException, CommunicationErrorException;

    ItemDetailModel getSummary(String itemCode) throws RecordNotFoundException, CommunicationErrorException;

    JsonArray getSummaryByMfac(String mfacCode, Long pageNumber, Long rowLimit) throws RecordNotFoundException, CommunicationErrorException;

    JsonArray getSummaryByContent(String contCode, Long pageNumber, Long rowLimit) throws RecordNotFoundException, CommunicationErrorException;

    JsonArray getItemSummary(List<String> itemList) throws CommunicationErrorException, RecordNotFoundException;
}
