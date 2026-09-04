package com.c2.lc.ms.customer.services.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.bos.SellerCreationBO;
import com.c2.lc.ms.customer.bos.SellerDetailBO;
import com.c2.lc.ms.customer.bos.SellerPriorityBo;
import com.c2.lc.ms.customer.entities.customer.FirmSellersEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface SellerService {
    JsonArray sendReqToSeller();

    JsonArray fetchWithFilters();

    JsonArray getUnmappedSellerList(Pageable page);

    List<JsonObject> fetchMappedSellers(LcHeaderBO lcHeaderBO, PageBO pageBO, String mobile) throws RecordNotFoundException;

    void updatePriority(LcHeaderBO lcHeaderBO, SellerPriorityBo sellerPriorityBo) throws RecordNotFoundException;

    SellerDetailBO fetchSellers(String sellerCode) throws RecordNotFoundException;

    void createSeller(SellerCreationBO sellerCreationBO);

    List<SellerDetailBO> fetchUnmappedSellers(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws RecordNotFoundException;

    int getCount(LcHeaderBO lcHeaderBO, String mobileNumber) throws NoSuchFieldException, RecordNotFoundException;

    int getUnmappedCount(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws RecordNotFoundException;

    List<SellerDetailBO> unMappedSellersSearch(String searchString, LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException, InvalidRequestException;

    List<SellerDetailBO> unMappedSellersSearchByCity(String searchCity, String searchState, String searchArea, LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException, InvalidRequestException;

    List<JsonObject> mappedSellersSearch(JsonObject jsonObject, LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException, InvalidRequestException;

    int getUnmappedSearchCount(String searchCity, String searchState, String searchArea, LcHeaderBO lcHeaderBO);

    int getMappedSearchCount(JsonObject jsonObject, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;

    int getUnmappedCountByName(String searchString, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;

    JsonArray getCustcode(LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException;

    List<JsonObject> getFirmSeller(LcHeaderBO lcHeaderBO);

    JsonObject getItemMappingInfo(String itemUcode);

    String getBranchSeller(LcHeaderBO lcHeaderBO);

    String sellerBuyerCombine(LcHeaderBO lcHeaderBO);

    void getFirmSellerBuyer(int count, Long sellerNewLaunchDays) throws RecordNotFoundException;

    JsonArray getLostOrder(String seller, String itemCode);

    List<FirmSellersEntity> getBySellerBuyer(String seller, String buyer);

    void sendStockNotification(String itemName, String userId, String itemCode);

    String getSellerByItemCode(String sellerItem, String uItem);

    void updateLostDate(String nSeq, LocalDate date);

}
