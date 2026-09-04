package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.ItemPLPResponseBO;
import com.c2.lc.ms.master.bos.ItemsSearchBO;
import com.c2.lc.ms.master.services.interfaces.DealOfTheDayService;
import com.c2.lc.ms.master.services.interfaces.FilterService;
import com.c2.lc.ms.master.services.interfaces.ItemService;
import com.c2.lc.ms.master.transactions.base.MasterBaseTransactionImpl;
import com.c2.lc.ms.master.transactions.interfaces.FilterTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterTransactionImpl extends MasterBaseTransactionImpl implements FilterTransaction {

    @Autowired
    private FilterService filterService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private DealOfTheDayService dealOfTheDayService;

    @Override
    public JsonArray newLaunchFilter(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        return filterService.newLaunchFilter(lcHeader, searchBO);
    }

    @Override
    public long newLaunchCount(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        return filterService.newLaunchCount(lcHeader, searchBO);
    }

    @Override
    public JsonArray topMostOrderFilter(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws CommunicationErrorException, InvalidRequestException, RecordNotFoundException {
        return filterService.topMostOrderFilter(lcHeader, searchBO);
    }

    @Override
    public long topMostOrderCount(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws CommunicationErrorException, InvalidRequestException, RecordNotFoundException {
        return filterService.topMostOrderCount(lcHeader, searchBO);
    }

    @Override
    public List<JsonObject> preferredSellerFilter(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        return filterService.preferredSellerFilter(header, searchBO);
    }

    @Override
    public long preferredSellerCount(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        return filterService.preferredSellerCount(lcHeader, searchBO);
    }

    @Override
    public JsonArray newLaunchSellers(SearchBO searchBO) throws RecordNotFoundException {
        return filterService.newLaunchSellers(searchBO);
    }

    @Override
    public long newLaunchSellersCount(SearchBO searchBO) {
        return filterService.newLaunchSellersCount(searchBO);
    }

    @Override
    public JsonArray newLaunchMfc(SearchBO searchBO) throws RecordNotFoundException {
        return filterService.newLaunchMfc(searchBO);
    }

    @Override
    public long newLaunchMfcCount(SearchBO searchBO) {
        return filterService.newLaunchMfcCount(searchBO);
    }

    @Override
    public JsonArray newLaunchBrand(SearchBO searchBO) throws RecordNotFoundException {
        return filterService.newLaunchBrand(searchBO);
    }

    @Override
    public long newLaunchBrandCount(SearchBO searchBO) {
        return filterService.newLaunchBrandCount(searchBO);
    }

    @Override
    public JsonArray preferredSellerMfc(SearchBO searchBO) throws RecordNotFoundException {
        return filterService.preferredSellerMfc(searchBO);
    }

    @Override
    public long preferredSellerMfcCount(SearchBO searchBO) {
        return filterService.preferredSellerMfcCount(searchBO);
    }

    @Override
    public JsonArray preferredSellerBrand(SearchBO searchBO) throws RecordNotFoundException {
        return filterService.preferredSellerBrand(searchBO);
    }

    @Override
    public long preferredSellerBrandCount(SearchBO searchBO) {
        return filterService.preferredSellerBrandCount(searchBO);
    }

    @Override
    public JsonArray shopByMfcFilter(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        return filterService.shopByMfcFilter(header, searchBO);
    }

    @Override
    public long shopByMfcFilterCount(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        return filterService.shopByMfcFilterCount(header, searchBO);
    }

    @Override
    public JsonArray shopByMfcBrand(SearchBO searchBO) throws RecordNotFoundException {
        return filterService.shopByMfcBrand(searchBO);
    }

    @Override
    public long shopByMfcBrandCount(SearchBO searchBO) {
        return filterService.shopByMfcBrandCount(searchBO);
    }

    @Override
    public JsonArray topMostOrderMfc(SearchBO searchBO,String stateCode) throws RecordNotFoundException {
        return filterService.topMostOrderMfc(searchBO,stateCode);
    }

    @Override
    public long topMostOrderMfcCount(SearchBO searchBO,String stateCode) {
        return filterService.topMostOrderMfcCount(searchBO,stateCode);
    }

    @Override
    public JsonArray topMostOrderBrand(SearchBO searchBO,String stateCode) throws RecordNotFoundException {
        return filterService.topMostOrderBrand(searchBO,stateCode);
    }

    @Override
    public long topMostOrderBrandCount(SearchBO searchBO,String stateCode) {
        return filterService.topMostOrderBrandCount(searchBO,stateCode);
    }

    @Override
    public JsonArray topMostOrderSeller(SearchBO searchBO, String stateCode) throws RecordNotFoundException {
        return filterService.topMostOrderSeller(searchBO,stateCode);
    }

    @Override
    public long topMostOrderSellerCount(SearchBO searchBO, String stateCode) {
        return filterService.topMostOrderSellerCount(searchBO,stateCode);
    }

    @Override
    public JsonArray shopByMfcSellerSearch(SearchBO searchBO) throws RecordNotFoundException {
        return filterService.shopByMfcSellerSearch(searchBO);
    }

    @Override
    public long shopByMfcSellerCount(SearchBO searchBO) {
        return filterService.shopByMfcSellerCount(searchBO);
    }

    @Override
    public JsonArray moleculeMfc(SearchBO searchBO) {
        return filterService.moleculeMfc(searchBO);
    }

    @Override
    public long moleculeMfcCount(SearchBO searchBO) {
        return filterService.moleculeMfcCount(searchBO);
    }

    @Override
    public JsonArray moleculeBrand(SearchBO searchBO) {
        return filterService.moleculeBrand(searchBO);
    }

    @Override
    public long moleculeBrandCount(SearchBO searchBO) {
        return filterService.moleculeBrandCount(searchBO);
    }

    @Override
    public JsonArray moleculeSeller(SearchBO searchBO) {
        return filterService.moleculeSeller(searchBO);
    }

    @Override
    public long moleculeSellerCount(SearchBO searchBO) {
        return filterService.moleculeSellerCount(searchBO);
    }

    @Override
    public JsonArray moleculeFilter(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        return filterService.moleculeFilter(header, searchBO);
    }

    @Override
    public long moleculeFilterCount(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        return filterService.moleculeFilterCount(header, searchBO);
    }

    @Override
    public JsonArray productFilter(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        return filterService.productFilter(header, searchBO);
    }

    @Override
    public long productFilterCount(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        return filterService.productFilterCount(header, searchBO);
    }

    @Override
    public JsonArray productMfc(SearchBO searchBO) {
        return filterService.productMfc(searchBO);
    }

    @Override
    public long productMfcCount(SearchBO searchBO) {
        return filterService.productMfcCount(searchBO);
    }

    @Override
    public JsonArray productBrand(SearchBO searchBO) {
        return filterService.productBrand(searchBO);
    }

    @Override
    public long productBrandCount(SearchBO searchBO) {
        return filterService.productBrandCount(searchBO);
    }

    @Override
    public JsonArray productSeller(SearchBO searchBO) {
        return filterService.productSeller(searchBO);
    }

    @Override
    public long productSellerCount(SearchBO searchBO) {
        return filterService.productSellerCount(searchBO);
    }

    @Override
    public JsonArray categoryFilter(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        return filterService.categoryFilter(header, searchBO);
    }

    @Override
    public long categoryFilterCount(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        return filterService.categoryFilterCount(header, searchBO);
    }

    @Override
    public JsonArray categoryMfc(SearchBO searchBO) {
        return filterService.categoryMfcList(searchBO);
    }

    @Override
    public long categoryMfcCount(SearchBO searchBO) {
        return filterService.categoryMfcCount(searchBO);
    }

    @Override
    public JsonArray categoryBrand(SearchBO searchBO) {
         return filterService.categoryBrandList(searchBO);
    }

    @Override
    public long categoryBrandCount(SearchBO searchBO) {
        return filterService.categoryBrandCount(searchBO);
    }

    @Override
    public JsonArray categorySeller(SearchBO searchBO) {
        return filterService.categorySellerList(searchBO);
    }

    @Override
    public long categorySellerCount(SearchBO searchBO) {
        return filterService.categorySellerCount(searchBO);
    }

    @Override
    public List<ItemPLPResponseBO> prdFilter(LcHeaderBO header, SearchBO searchBO) throws RecordNotFoundException {
        List<Object[]> items = filterService.prdFilter(header, searchBO);
        if (items.size() == 0) {
            throw new RecordNotFoundException("No u_code found!");
        }
        List<Object[]> dealItems = dealOfTheDayService.getDealItems(header);
        return dealOfTheDayService.getTSPLP(items, dealItems);
        //PageBO pageBO = new PageBO();
        //pageBO.setPage(searchBO.getPage());
        //pageBO.setLimit(searchBO.getLimit());
        //return itemService.getTSPLP(items, pageBO);
    }

    @Override
    public int prdFilterCount(LcHeaderBO lcHeaderBO, SearchBO searchBO) {
        return filterService.prdFilterCount(lcHeaderBO, searchBO);
    }

    @Override
    public List<ItemPLPResponseBO> getTrendingPrdPlp(SearchBO searchBO, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        List<Object[]> items = filterService.trendingPrdFilter(lcHeaderBO.getC2Code(), searchBO);
        if (items.size() == 0) {
            throw new RecordNotFoundException("No u_code found!");
        }
       /* PageBO pageBO = new PageBO();
        pageBO.setPage(searchBO.getPage());
        pageBO.setLimit(searchBO.getLimit());
        return itemService.getTSPLP(items, pageBO);*/
        List<Object[]> dealItems = dealOfTheDayService.getDealItems(lcHeaderBO);
        return dealOfTheDayService.getTSPLP(items, dealItems);
    }

    @Override
    public int trendingPrdPlpCount(LcHeaderBO lcHeaderBO, SearchBO searchBO) {
        return filterService.trendingPrdPlpCount(lcHeaderBO.getC2Code(), searchBO);
    }

    @Override
    public JsonArray dealOfTheDayBrand(String c2Code, SearchBO searchBO, JsonObject jsonObject) {
        return filterService.dealOfTheDayBrand(c2Code, searchBO, jsonObject);
    }

    @Override
    public JsonArray dealOfTheDayPrdForms(String c2Code, SearchBO searchBO, JsonObject jsonObject) {
        return filterService.dealOfTheDayPrdForms(c2Code, searchBO, jsonObject);
    }

    @Override
    public JsonArray dealOfTheDayUses(String c2Code, SearchBO searchBO, JsonObject jsonObject) {
        return filterService.dealOfTheDayUses(c2Code, searchBO, jsonObject);
    }
}
