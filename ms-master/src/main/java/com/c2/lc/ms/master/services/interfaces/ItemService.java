package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.DataFormatException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.*;
import com.c2.lc.ms.master.bos.customerbos.*;
import com.c2.lc.ms.master.entities.mysql.UItemMstEntity;
import com.c2.lc.ms.master.models.ItemDetailModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;

public interface ItemService {

    UItemMstEntity findByItemCode(String itemCode);

    List<String> findByMfacCode(String mfacCode, Long rowLimit, Long limit);

    List<String> findByContCode(String contCode, Long pageNumber, Long rowLimit);

    JsonArray getNewLaunched(PageBO request, Long days, LcHeaderBO headerBO) throws CommunicationErrorException, RecordNotFoundException, InvalidRequestException;

    Integer getTopCount(Long firmId) throws CommunicationErrorException, InvalidRequestException;

    Long getMasterActiveItemCount();

    void saveCustItemMst(String c2Code, String brCode, List<RowItemBO> data) throws DataIntegrityViolationException;

    void saveCustPackTypeMst(String c2Code, String brCode, List<RowPackTypeBO> data) throws DataIntegrityViolationException;

    JsonArray fetchConvertedItemList(String c2Code, String customerCode, JsonArray data);

    ItemPDPResponseBO getById(String itemCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;

    ItemPDPResponseBO getItemSummary(String itemCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;

    ItemPDPResponseBO getByBarCode(String barCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;

    JsonArray getItemSummary(JsonObject data) throws RecordNotFoundException;

    ItemDetailModel updateSummary(String itemCode) throws RecordNotFoundException;

    JsonArray geTopMostOrderItem(LcHeaderBO lcHeaderBO, ItemsSearchBO searchBO) throws CommunicationErrorException, RecordNotFoundException, InvalidRequestException;

    JsonArray uploadProductImage(MultipartFile[] files) throws InvalidRequestException, StorageException, IOException, URISyntaxException, InvalidKeyException;

    void updateItemImage(ImageUpdateBo imageUpdateBo) throws RecordNotFoundException;

    void saveCustPackMst(String c2Code, String brCode, List<RowPackBO> data) throws DataIntegrityViolationException;

    void saveCustMfacMst(String c2Code, String brCode, List<RowMfacBO> row);

    void saveCustBrandMst(String c2Code, String brCode, List<RowBrandBO> row);

    void saveCustScheduleMst(String c2Code, String brCode, List<RowScheduleBO> row);

    void saveCustContMst(String c2Code, String brCode, List<RowContBO> row);

    void saveCustItemGroupMst(String c2Code, String brCode, List<RowItemGroupBO> row);

    void saveCustItemCategoryMst(String c2Code, String brCode, List<RowItemCategoryBO> row);

    List<Object[]> getTrendingProducts(String c2Code, PageBO pageBO) throws RecordNotFoundException;

    int getTrendingProdCount(String c2Code);

    List<Object[]> getRecentSearch(LcHeaderBO lcHeaderBO, PageBO pageBO) throws CommunicationErrorException, RecordNotFoundException;

    ItemPDPResponseBO getProductDetail(String itemCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException, DataFormatException;

    int getRecentItemsCount(LcHeaderBO lcHeaderBO) throws CommunicationErrorException;
    
    List<ItemPLPResponseBO> getPLP(List<String> itemCodes, PageBO pageBO);

    List<ItemPLPResponseBO> getTSPLP(List<Object[]> itemCodes, PageBO pageBO);

    List<String> geTopMostOrderUItemCode(LcHeaderBO lcHeaderBO, PageBO pageBO) throws CommunicationErrorException, InvalidRequestException;

    List<Object[]> categoryWiseProducts(SearchBO searchBO, LcHeaderBO header, List<String> categoryList);

   // List<Object[]> DealOfTheDayProducts(SearchBO searchBO, LcHeaderBO header);

    List<ItemPLPResponseBO> getTSCategoryPLP(List<Object[]> items, SearchBO searchBO);

    long tsProductsCount(List<Object[]> items, SearchBO searchBO);

    List<String> getItemsByCategory(LcHeaderBO header, JsonObject request);

    List<Object[]> categoryWiseProductsCount(SearchBO searchBO, LcHeaderBO header);

    List<ItemPLPResponseBO> getRecentSearchPLP(List<String> items, PageBO pageBo);

    //long DealOfTheDayCount(SearchBO searchBO, LcHeaderBO header);


    }
