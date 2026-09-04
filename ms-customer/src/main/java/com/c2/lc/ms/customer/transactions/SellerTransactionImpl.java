package com.c2.lc.ms.customer.transactions;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.bos.SellerCreationBO;
import com.c2.lc.ms.customer.bos.SellerDetailBO;
import com.c2.lc.ms.customer.bos.SellerPriorityBo;
import com.c2.lc.ms.customer.entities.customer.FirmSellersEntity;
import com.c2.lc.ms.customer.entities.customer.NewLaunchNotificationEntity;
import com.c2.lc.ms.customer.repos.customer.NewLaunchNotificationRepo;
import com.c2.lc.ms.customer.services.interfaces.SellerService;
import com.c2.lc.ms.customer.transactions.base.LcBaseTransactionImpl;
import com.c2.lc.ms.customer.transactions.interfaces.SellerTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class SellerTransactionImpl extends LcBaseTransactionImpl implements SellerTransaction {
    @Autowired
    private SellerService sellerService;

    @Autowired private NewLaunchNotificationRepo notificationRepo;

    @Value("${lost.stock.days}")
    private int lostDays;

    @Override
    public JsonArray sendReqToSeller() {
        return sellerService.sendReqToSeller();
    }

    @Override
    public JsonArray fetchWithFilters() {
        return sellerService.fetchWithFilters();
    }

    @Override
    public JsonArray getUnmappedSellerList(Pageable page) {
        return sellerService.getUnmappedSellerList(page);
    }

    @Override
    public void updatePriority(LcHeaderBO lcHeaderBO, SellerPriorityBo sellerPriorityBo) throws RecordNotFoundException {
        sellerService.updatePriority(lcHeaderBO, sellerPriorityBo);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createSeller(SellerCreationBO sellerCreationBO) {
        sellerService.createSeller(sellerCreationBO);
    }

    @Override
    public List<SellerDetailBO> fetchUnmappedSellers(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws RecordNotFoundException {
        return sellerService.fetchUnmappedSellers(lcHeaderBO, searchBO);
    }

    @Override
    public SellerDetailBO getSellerInfo(String sellerCode) throws RecordNotFoundException {
        return sellerService.fetchSellers(sellerCode);
    }

    @Override
    public List<JsonObject> fetchMappedSellers(LcHeaderBO lcHeaderBO, PageBO pageBO,String mobile) throws RecordNotFoundException {
        return sellerService.fetchMappedSellers(lcHeaderBO, pageBO, mobile);
    }

    @Override
    public int getCount(LcHeaderBO lcHeaderBO, String mobileNumber) throws NoSuchFieldException, RecordNotFoundException {
        return sellerService.getCount(lcHeaderBO, mobileNumber);
    }

    @Override
    public int getUnmappedCount(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws RecordNotFoundException {
        return sellerService.getUnmappedCount(lcHeaderBO, searchBO);
    }

    @Override
    public List<SellerDetailBO> unmappedSellersSearch(String searchString, LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException, InvalidRequestException {
        return sellerService.unMappedSellersSearch(searchString, lcHeaderBO, pageBO);
    }

    @Override
    public List<SellerDetailBO> unmappedSellersSearchByCity(String searchCity, String searchState, String searchArea, LcHeaderBO lcHeaderBO, PageBO pageBO)
            throws RecordNotFoundException, InvalidRequestException {
        return sellerService.unMappedSellersSearchByCity(searchCity, searchState, searchArea, lcHeaderBO, pageBO );
    }

    @Override
    public List<JsonObject> mappedSellersSearch(JsonObject jsonObject, LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException, InvalidRequestException {
        return sellerService.mappedSellersSearch(jsonObject, lcHeaderBO, pageBO);
    }

    @Override
    public int getUnmappedSearchCount(String searchCity, String searchState, String searchArea, LcHeaderBO lcHeaderBO) {
        return sellerService.getUnmappedSearchCount(searchCity, searchState, searchArea, lcHeaderBO);
    }

    @Override
    public int getMappedSearchCount(JsonObject jsonObject, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        return sellerService.getMappedSearchCount(jsonObject, lcHeaderBO);
    }

    @Override
    public int getUnmappedCountByName(String searchString, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        return sellerService.getUnmappedCountByName(searchString, lcHeaderBO);
    }

    @Override
    public JsonArray getCustCode(LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException {
        return sellerService.getCustcode(lcHeaderBO, pageBO);
    }

    @Override
    public List<JsonObject> getFirmSeller(LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        return sellerService.getFirmSeller(lcHeaderBO);
    }

    @Override
    public JsonObject getItemMappingInfo(String itemUcode) {
        return sellerService.getItemMappingInfo(itemUcode);
    }

    @Override
    public void getSellerNewLaunched(Long sellerNewLaunchDays) throws RecordNotFoundException {
        int count = 50;
        sellerService.getFirmSellerBuyer(count,sellerNewLaunchDays);
        //return sellerService.getSellerNewLaunched(firmSeller,sellerNewLaunchDays);
    }

    @Override
    public void stockNotification(JsonArray jsonArray) {

        if (jsonArray.size() > 0) {
            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                if (obj.has("c_seller_code") && obj.has("c_item_code") && obj.has("c_seller_item_code") &&
                        !helper.isEmpty(obj.get("c_item_code").getAsString()) && !helper.isEmpty(obj.get("c_item_code").getAsString())
                        && !helper.isEmpty(obj.get("c_seller_item_code").getAsString())) {
                    JsonArray bounceItem = sellerService.getLostOrder(obj.get("c_seller_code").getAsString(),
                            obj.get("c_seller_item_code").getAsString());
                    System.out.println("lost order ------>");
                    if (bounceItem.size() > 0) {
                        for (JsonElement jsonElement : bounceItem) {
                            JsonObject bounceObj = jsonElement.getAsJsonObject();
                            List<FirmSellersEntity> firmSellers = sellerService.getBySellerBuyer(obj.get("c_seller_code").getAsString(),
                                    bounceObj.get("c_buyer_code").getAsString());
                            System.out.println("firm seller ------>");
                            if (firmSellers.size() > 0) {
                                for (FirmSellersEntity sellersEntity : firmSellers) {

                                    NewLaunchNotificationEntity newEntity = notificationRepo.findStockRecord(
                                            bounceObj.get("c_buyer_code").getAsString(), obj.get("c_seller_code").getAsString(), sellersEntity.getNCreatedBy(),
                                            obj.get("c_item_code").getAsString(), "Stock",helper.getCurrentDate().minusDays(lostDays));
                                    System.out.println("NewLaunchNotificationEntity ------>" + newEntity);
                                    if (newEntity == null) {
                                        NewLaunchNotificationEntity entity = new NewLaunchNotificationEntity();
                                        entity.setCBuyerCode( bounceObj.get("c_buyer_code").getAsString());
                                        entity.setCSellerCode(obj.get("c_seller_code").getAsString());
                                        entity.setCType("Stock");
                                        entity.setCItemCode(obj.get("c_item_code").getAsString());
                                        entity.setADate(helper.getCurrentDate());
                                        entity.setNUserId(sellersEntity.getNCreatedBy());
                                        notificationRepo.save(entity);

                                        sellerService.sendStockNotification(bounceObj.get("c_item_name").getAsString(),
                                                String.valueOf(sellersEntity.getNCreatedBy()), obj.get("c_item_code").getAsString());
                                        System.out.println("sendStockNotification ------>" + entity);
                                    }
                                }
                            }
                            sellerService.updateLostDate(obj.get("n_seq").getAsString(), helper.getCurrentDate());
                        }
                    }
                }
            }
        }
    }
}
