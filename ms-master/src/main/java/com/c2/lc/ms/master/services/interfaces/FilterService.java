package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.ItemsSearchBO;
import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public interface FilterService extends MasterBaseService {

    String getSellerCodeAndBuyerCodeQuery(LcHeaderBO lcHeaderBO) throws RecordNotFoundException,CommunicationErrorException, InvalidRequestException ;

    JsonArray newLaunchFilter(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    long newLaunchCount(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    JsonArray topMostOrderFilter(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws CommunicationErrorException, InvalidRequestException, RecordNotFoundException;

    long topMostOrderCount(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws CommunicationErrorException, InvalidRequestException, RecordNotFoundException;

    List<JsonObject> preferredSellerFilter(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    long preferredSellerCount(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    JsonArray newLaunchSellers(SearchBO searchBO) throws RecordNotFoundException;

    long newLaunchSellersCount(SearchBO searchBO);

    JsonArray newLaunchMfc(SearchBO searchBO) throws RecordNotFoundException;

    long newLaunchMfcCount(SearchBO searchBO) ;

    JsonArray newLaunchBrand(SearchBO searchBO) throws RecordNotFoundException;

    long newLaunchBrandCount(SearchBO searchBO);

    JsonArray preferredSellerMfc(SearchBO searchBO) throws RecordNotFoundException;

    long preferredSellerMfcCount(SearchBO searchBO);

    JsonArray preferredSellerBrand(SearchBO searchBO) throws RecordNotFoundException;

    long preferredSellerBrandCount(SearchBO searchBO);

    JsonArray shopByMfcFilter(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    long shopByMfcFilterCount(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    JsonArray shopByMfcBrand(SearchBO searchBO) throws RecordNotFoundException;

    long shopByMfcBrandCount(SearchBO searchBO);

    JsonArray topMostOrderMfc(SearchBO searchBO,String stateCode) throws RecordNotFoundException;

    long topMostOrderMfcCount(SearchBO searchBO,String stateCode);

    JsonArray topMostOrderBrand(SearchBO searchBO,String stateCode) throws RecordNotFoundException;

    long topMostOrderBrandCount(SearchBO searchBO,String stateCode);

    JsonArray topMostOrderSeller(SearchBO searchBO, String stateCode) throws RecordNotFoundException;

    long topMostOrderSellerCount(SearchBO searchBO, String stateCode);

    JsonArray shopByMfcSellerSearch(SearchBO searchBO) throws RecordNotFoundException;

    long shopByMfcSellerCount(SearchBO searchBO);

    JsonArray moleculeMfc(SearchBO searchBO);

    long moleculeMfcCount(SearchBO searchBO);

    JsonArray moleculeBrand(SearchBO searchBO);

    long moleculeBrandCount(SearchBO searchBO);

    JsonArray moleculeSeller(SearchBO searchBO);

    long moleculeSellerCount(SearchBO searchBO);

    JsonArray moleculeFilter(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    long moleculeFilterCount(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    long productMfcCount(SearchBO searchBO);

    JsonArray productMfc(SearchBO searchBO);

    JsonArray productBrand(SearchBO searchBO);

    long productBrandCount(SearchBO searchBO);

    JsonArray productSeller(SearchBO searchBO);

    long productSellerCount(SearchBO searchBO);

    JsonArray productFilter(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    long productFilterCount(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    JsonArray categoryFilter(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    long categoryFilterCount(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    JsonArray categoryMfcList(SearchBO searchBO);

    long categoryMfcCount(SearchBO searchBO);

    JsonArray categoryBrandList(SearchBO searchBO);

    long categoryBrandCount(SearchBO searchBO);

    JsonArray categorySellerList(SearchBO searchBO);

    long categorySellerCount(SearchBO searchBO);

    List<Object[]> prdFilter(LcHeaderBO header, SearchBO searchBO);

    int prdFilterCount(LcHeaderBO lcHeaderBO, SearchBO searchBO);

    List<Object[]> trendingPrdFilter(String c2Code, SearchBO searchBO);

    int trendingPrdPlpCount(String c2Code, SearchBO searchBO);

    JsonArray dealOfTheDayBrand(String c2Code, SearchBO searchBO, JsonObject jsonObject);

    JsonArray dealOfTheDayPrdForms(String c2Code, SearchBO searchBO, JsonObject jsonObject);

    JsonArray dealOfTheDayUses(String c2Code, SearchBO searchBO, JsonObject jsonObject);
}
