package com.c2.lc.ms.customer.transactions.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.bos.SellerCreationBO;
import com.c2.lc.ms.customer.bos.SellerDetailBO;
import com.c2.lc.ms.customer.bos.SellerPriorityBo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SellerTransaction {
    JsonArray sendReqToSeller();

    JsonArray fetchWithFilters();

    JsonArray getUnmappedSellerList(Pageable page);

    void updatePriority(LcHeaderBO lcHeaderBO, SellerPriorityBo sellerPriorityBo) throws RecordNotFoundException;

    void createSeller(SellerCreationBO sellerCreationBO);

    List<SellerDetailBO> fetchUnmappedSellers(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws RecordNotFoundException;

    SellerDetailBO getSellerInfo(String sellerCode) throws RecordNotFoundException;

    List<JsonObject> fetchMappedSellers(LcHeaderBO lcHeaderBO, PageBO pageBO, String mobileNumber) throws RecordNotFoundException;

    int getCount(LcHeaderBO lcHeaderBO, String mobileNumber) throws NoSuchFieldException, RecordNotFoundException;

    int getUnmappedCount(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws RecordNotFoundException;

    List<SellerDetailBO> unmappedSellersSearch(String searchString, LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException, InvalidRequestException;

    List<SellerDetailBO> unmappedSellersSearchByCity(String searchCity, String searchState, String searchArea, LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException, InvalidRequestException;

    List<JsonObject> mappedSellersSearch(JsonObject jsonObject, LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException, InvalidRequestException;

    int getUnmappedSearchCount(String searchCity, String searchState, String searchArea, LcHeaderBO lcHeaderBO);

    int getMappedSearchCount(JsonObject jsonObject, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;

    int getUnmappedCountByName(String searchString, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;

    JsonArray getCustCode(LcHeaderBO lcHeaderBO, PageBO pageBO)throws RecordNotFoundException;

    List<JsonObject> getFirmSeller(LcHeaderBO lcHeaderBO) throws RecordNotFoundException;

    JsonObject getItemMappingInfo(String itemUcode);

    void getSellerNewLaunched(Long sellerNewLaunchDays) throws RecordNotFoundException, DuplicateRecordException;

    void stockNotification(JsonArray jsonArray);
}
