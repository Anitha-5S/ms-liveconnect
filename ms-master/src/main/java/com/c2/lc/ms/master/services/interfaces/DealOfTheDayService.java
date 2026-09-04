package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.DealOfTheDayBO;
import com.c2.lc.ms.master.bos.DealSearchBo;
import com.c2.lc.ms.master.bos.ItemPLPResponseBO;
import com.c2.lc.ms.master.entities.mysql.DealOfTheDayEntity;
import com.google.gson.JsonObject;

import java.util.List;

public interface DealOfTheDayService {
    DealOfTheDayEntity save(LcHeaderBO header, DealOfTheDayBO dealOfTheDayBO) throws DuplicateRecordException;

    void updateDealStatus(JsonObject jsonObject, LcHeaderBO header) throws RecordNotFoundException;

    void editDeal(LcHeaderBO header, DealOfTheDayBO dealOfTheDayBO) throws RecordNotFoundException;

    DealOfTheDayBO singleDeal(JsonObject jsonDeal) throws RecordNotFoundException;

    List<DealOfTheDayBO> fetchDeals(String c2Code, DealSearchBo dealSearchBo) throws RecordNotFoundException;

    List<Object[]> DealOfTheDayProducts(SearchBO searchBO, LcHeaderBO header, JsonObject request);

    List<ItemPLPResponseBO> getTSDealPLP(List<Object[]> items, SearchBO searchBO, JsonObject request);

    List<Object[]> DealOfTheDayProductsCount(SearchBO searchBO, LcHeaderBO header, JsonObject request);

    List<Object[]> getDealItems(LcHeaderBO header);

    List<ItemPLPResponseBO> getTSPLP(List<Object[]> items, List<Object[]> dealItems);

    int DealOfTheDayListCount(String c2Code, DealSearchBo dealSearchBo) throws RecordNotFoundException;
}
