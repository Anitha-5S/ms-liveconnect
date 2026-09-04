package com.c2.lc.ms.master.transactions.interfaces;

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
import com.c2.lc.ms.master.transactions.interfaces.base.MasterBaseTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;

public interface ItemTransaction extends MasterBaseTransaction {

    UItemMstEntity findByItemCode(String itemCode);

    JsonArray getNewLaunched(PageBO request, Long days, LcHeaderBO headerBO) throws CommunicationErrorException, RecordNotFoundException, InvalidRequestException;

    Integer getTopCount(Long firmId) throws CommunicationErrorException, RecordNotFoundException, InvalidRequestException;

    Long getMasterActiveItemCount();

    void saveCustItemMst(String c2Code, String brCode, DataBO data);

    void saveCustPackMst(String c2Code, String brCode, PackDataBO data);

    void saveCustPackTypeMst(String c2Code, String brCode, PackTypeDataBO data);

    JsonArray fetchConvertedItemList(String c2Code, String customerCode, JsonArray data);

    ItemPDPResponseBO getById(String itemCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;

    ItemPDPResponseBO getItemSummary(String itemCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;

    ItemPDPResponseBO getByBarCode(String barCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;

    List<ItemPLPResponseBO> geTopMostOrderItem(LcHeaderBO lcHeaderBO, PageBO pageBO) throws CommunicationErrorException, RecordNotFoundException, InvalidRequestException;

    JsonArray uploadProductImage(MultipartFile[] files) throws InvalidRequestException, StorageException, IOException, URISyntaxException, InvalidKeyException;

    void updateItemImage(ImageUpdateBo imageUpdateBo) throws RecordNotFoundException;

    void saveCustMfacMst(String c2Code, String brCode, MfacDataBO data);

    void saveCustBrandMst(String c2Code, String brCode, BrandDataBO data);

    void saveCustScheduleMst(String c2Code, String brCode, ScheduleDataBO data);

    void saveCustContMst(String c2Code, String brCode, ContDataBO data);

    void saveCustItemGroupMst(String c2Code, String brCode, ItemGroupDataBO data);

    void saveCustItemCategoryMst(String c2Code, String brCode, ItemCategoryDataBO data);

    List<LcItem> geTrendingItems(PageBO pageBo, String c2Code);

    String getTrendingCount(String c2Code);

    List<ItemPLPResponseBO> getTrendingProducts(PageBO pageBo, LcHeaderBO header) throws RecordNotFoundException;

    int getTrendingProdCount(String c2Code);

    List<ItemPLPResponseBO> getRecentSearch(PageBO pageBo, LcHeaderBO lcHeaderBO) throws CommunicationErrorException, RecordNotFoundException;

    ItemPDPResponseBO getProductDetails(String itemCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException, DataFormatException;

    int getRecentItemsCount(LcHeaderBO lcHeaderBO) throws CommunicationErrorException;

    JsonArray geTopMostOrderItems(LcHeaderBO lcHeaderBO, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    List<ItemPLPResponseBO> categoryWiseProducts(SearchBO searchBO, LcHeaderBO header) throws RecordNotFoundException;

    //List<ItemPLPResponseBO> DealOfTheDayProducts(SearchBO searchBO, LcHeaderBO header) throws RecordNotFoundException;

    long categoryWiseProductsCount(SearchBO searchBO, LcHeaderBO header) throws RecordNotFoundException;

    List<ItemPLPResponseBO> recommendedByCart(LcHeaderBO header, JsonObject request) throws RecordNotFoundException;

    //long DealOfTheDayCount(SearchBO searchBO, LcHeaderBO header);

}
