package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.DataFormatException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.*;
import com.c2.lc.ms.master.bos.customerbos.*;
import com.c2.lc.ms.master.entities.mongo.LcItem;
import com.c2.lc.ms.master.entities.mysql.UItemMstEntity;
import com.c2.lc.ms.master.services.interfaces.DealOfTheDayService;
import com.c2.lc.ms.master.services.interfaces.ItemService;
import com.c2.lc.ms.master.services.interfaces.StockiestService;
import com.c2.lc.ms.master.transactions.interfaces.ItemTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ItemTransactionImpl implements ItemTransaction {

    @Autowired private ItemService itemService;
    @Autowired private StockiestService stockiestService;
    @Autowired private DealOfTheDayService dealOfTheDayService;

    @Override
    public UItemMstEntity findByItemCode(String itemCode) {
        return itemService.findByItemCode(itemCode);
    }

    @Override
    public JsonArray getNewLaunched(PageBO request, Long days, LcHeaderBO headerBO) throws CommunicationErrorException, RecordNotFoundException, InvalidRequestException {
        return itemService.getNewLaunched(request, days, headerBO);
    }

/*    @Override
    public JsonArray geTopMostOrderItem(LcHeaderBO lcHeaderBO, ItemsSearchBO searchBO) throws CommunicationErrorException, RecordNotFoundException, InvalidRequestException {
        return itemService.geTopMostOrderItem(lcHeaderBO,searchBO);
    }*/
    @Override
    public List<ItemPLPResponseBO> geTopMostOrderItem(LcHeaderBO lcHeaderBO, PageBO pageBO) throws CommunicationErrorException, RecordNotFoundException, InvalidRequestException {
        List<String> items = itemService.geTopMostOrderUItemCode(lcHeaderBO, pageBO);
        return itemService.getPLP(items, pageBO);
    }

    @Override
    public JsonArray uploadProductImage(MultipartFile[] files) throws InvalidRequestException, StorageException, IOException, URISyntaxException, InvalidKeyException {
        return itemService.uploadProductImage(files);
    }

    @Override
    public void updateItemImage(ImageUpdateBo imageUpdateBo) throws RecordNotFoundException {
        itemService.updateItemImage(imageUpdateBo);
    }

    @Override
    public void saveCustMfacMst(String c2Code, String brCode, MfacDataBO data) {
        itemService.saveCustMfacMst(c2Code, brCode, data.getRow());
    }

    @Override
    public void saveCustBrandMst(String c2Code, String brCode, BrandDataBO data) {
        itemService.saveCustBrandMst(c2Code, brCode, data.getRow());
    }

    @Override
    public void saveCustScheduleMst(String c2Code, String brCode, ScheduleDataBO data) {
        itemService.saveCustScheduleMst(c2Code, brCode, data.getRow());
    }

    @Override
    public void saveCustContMst(String c2Code, String brCode, ContDataBO data) {
        itemService.saveCustContMst(c2Code, brCode, data.getRow());
    }

    @Override
    public void saveCustItemGroupMst(String c2Code, String brCode, ItemGroupDataBO data) {
        itemService.saveCustItemGroupMst(c2Code, brCode, data.getRow());
    }

    @Override
    public void saveCustItemCategoryMst(String c2Code, String brCode, ItemCategoryDataBO data) {
        itemService.saveCustItemCategoryMst(c2Code, brCode, data.getRow());
    }

    @Override
    public Integer getTopCount(Long firmId) throws CommunicationErrorException, RecordNotFoundException, InvalidRequestException {
        return itemService.getTopCount(firmId);
    }

    @Override
    public Long getMasterActiveItemCount() {
        return itemService.getMasterActiveItemCount();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void saveCustItemMst(String c2Code, String brCode, DataBO data) {
        itemService.saveCustItemMst(c2Code, brCode, data.getRow());
    }

    @Override
    public void saveCustPackMst(String c2Code, String brCode, PackDataBO data) {
        itemService.saveCustPackMst(c2Code, brCode, data.getRow());
    }

    @Override
    public void saveCustPackTypeMst(String c2Code, String brCode, PackTypeDataBO data) {
        itemService.saveCustPackTypeMst(c2Code, brCode, data.getRow());
    }

    @Override
    public JsonArray fetchConvertedItemList(String c2Code, String customerCode, JsonArray data) {
        return itemService.fetchConvertedItemList(c2Code, customerCode, data);
    }

    @Override
    public ItemPDPResponseBO getById(String itemCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        return itemService.getById(itemCode, lcHeaderBO);
    }

    @Override
    public ItemPDPResponseBO getItemSummary(String itemCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        return itemService.getItemSummary(itemCode, lcHeaderBO);
    }

    @Override
    public ItemPDPResponseBO getByBarCode(String barCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        return itemService.getByBarCode(barCode, lcHeaderBO);
    }

    @Override
    public List<LcItem> geTrendingItems(PageBO pageBo, String c2Code) {
        return null;
    }

    @Override
    public String getTrendingCount(String c2Code) {
        return null;
    }

    @Override
    public List<ItemPLPResponseBO> getTrendingProducts(PageBO pageBo, LcHeaderBO header) throws RecordNotFoundException {
        List<Object[]> items = itemService.getTrendingProducts(header.getC2Code(),pageBo);
        if (items.size() == 0) {
            throw new RecordNotFoundException("No u_code found!");
        }
        List<Object[]> dealItems = dealOfTheDayService.getDealItems(header);
        return dealOfTheDayService.getTSPLP(items, dealItems);
    }

    @Override
    public int getTrendingProdCount(String c2Code) {
        return itemService.getTrendingProdCount(c2Code);
    }

    @Override
    public List<ItemPLPResponseBO> getRecentSearch(PageBO pageBo, LcHeaderBO lcHeaderBO) throws CommunicationErrorException, RecordNotFoundException {
        List<Object[]> items = itemService.getRecentSearch(lcHeaderBO, pageBo);
        if (items.size() == 0) {
            throw new RecordNotFoundException("No u_code found!");
        }
        List<Object[]> dealItems = dealOfTheDayService.getDealItems(lcHeaderBO);
        return dealOfTheDayService.getTSPLP(items, dealItems);
    }

    @Override
    public ItemPDPResponseBO getProductDetails(String itemCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException, DataFormatException {
        return itemService.getProductDetail(itemCode, lcHeaderBO);
    }

    @Override
    public int getRecentItemsCount(LcHeaderBO lcHeaderBO) throws CommunicationErrorException {
        return itemService.getRecentItemsCount(lcHeaderBO);
    }

    @Override
    public JsonArray geTopMostOrderItems(LcHeaderBO lcHeaderBO, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        return itemService.geTopMostOrderItem(lcHeaderBO,searchBO);
    }

    @Override
    public List<ItemPLPResponseBO> categoryWiseProducts(SearchBO searchBO, LcHeaderBO header) throws RecordNotFoundException {
        List<String> categoryList = new ArrayList<>();
        categoryList.add(searchBO.getSearchTerm());
        List<Object[]> items = itemService.categoryWiseProducts(searchBO,header,categoryList);
        if (items.size() == 0) {
            throw new RecordNotFoundException("No u_code found!");
        }
        List<Object[]> dealItems = dealOfTheDayService.getDealItems(header);
        return dealOfTheDayService.getTSPLP(items, dealItems);
    }

   /* @Override
    public List<ItemPLPResponseBO> DealOfTheDayProducts(SearchBO searchBO, LcHeaderBO header) throws RecordNotFoundException {
        List<Object[]> items = itemService.DealOfTheDayProducts(searchBO,header);
        if (items.size() == 0) {
            throw new RecordNotFoundException("No u_code found!");
        }
        return itemService.getTSCategoryPLP(items, searchBO);
    }*/

    @Override
    public long categoryWiseProductsCount(SearchBO searchBO, LcHeaderBO header) throws RecordNotFoundException {
        List<Object[]> items = itemService.categoryWiseProductsCount(searchBO,header);
        if (items.size() == 0) {
            throw new RecordNotFoundException("No u_code found!");
        }
        return itemService.tsProductsCount(items, searchBO);
    }

   /*/ @Override
    public long DealOfTheDayCount(SearchBO searchBO, LcHeaderBO header) {
        return itemService.DealOfTheDayCount(searchBO, header);
    }*/

    @Override
    public List<ItemPLPResponseBO> recommendedByCart(LcHeaderBO header, JsonObject request) throws RecordNotFoundException {
        List<String> categoryList = itemService.getItemsByCategory(header, request);
        SearchBO searchBO = new SearchBO();
        searchBO.setPage(request.get("n_page").getAsInt());
        searchBO.setLimit(request.get("n_limit").getAsInt());
        searchBO.setSort(request.get("c_sort").getAsString());
        List<Object[]> items = itemService.categoryWiseProducts(searchBO,header, categoryList);
        return itemService.getTSCategoryPLP(items, searchBO);
    }
}
