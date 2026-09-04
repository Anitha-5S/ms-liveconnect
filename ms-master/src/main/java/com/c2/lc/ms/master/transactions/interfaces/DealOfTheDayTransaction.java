package com.c2.lc.ms.master.transactions.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.DataFormatException;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.DealOfTheDayBO;
import com.c2.lc.ms.master.bos.DealSearchBo;
import com.c2.lc.ms.master.bos.ItemPLPResponseBO;
import com.c2.lc.ms.master.entities.mysql.DealOfTheDayEntity;
import com.google.gson.JsonObject;

import java.util.List;

public interface DealOfTheDayTransaction {
    DealOfTheDayEntity save(LcHeaderBO header, DealOfTheDayBO dealOfTheDayBO) throws DuplicateRecordException;

    void updateDealStatus(JsonObject jsonObject, LcHeaderBO header) throws RecordNotFoundException;

    void editDeal(LcHeaderBO header, DealOfTheDayBO dealOfTheDayBO) throws RecordNotFoundException;

    DealOfTheDayBO singleDeal(JsonObject jsonDeal) throws RecordNotFoundException;

    List<DealOfTheDayBO> fetchDeals(String c2Code, DealSearchBo dealSearchBo) throws RecordNotFoundException;

    List<ItemPLPResponseBO> DealOfTheDayProducts(SearchBO searchBO, LcHeaderBO header, JsonObject request) throws RecordNotFoundException, DataFormatException;

    long DealOfTheDayCount(SearchBO searchBO, LcHeaderBO header, JsonObject request) throws RecordNotFoundException;

    int DealOfTheDayListCount(String c2Code, DealSearchBo dealSearchBo) throws RecordNotFoundException;
}
