package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.ImageUpdateBo;
import com.c2.lc.ms.master.bos.SellerDetailBO;
import com.c2.lc.ms.master.bos.SellerImageBo;
import com.c2.lc.ms.master.entities.mysql.LcOfferMstEntity;
import com.c2.lc.ms.master.models.ItemSellersList;
import com.c2.lc.ms.master.services.interfaces.SellerService;
import com.c2.lc.ms.master.transactions.interfaces.SellerTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


@Component
public class SellerTransactionImpl implements SellerTransaction {

    @Autowired
    private SellerService sellerService;

    @Override
    public List<ItemSellersList> getSellerDetailsByItem(String buyerCode, String itemUCode) {
        return sellerService.getSellerDetailsByItem(buyerCode, itemUCode);
    }

    @Override
    public JsonArray getStockiestDetails(String sellerc2code, String cCode) {
        return sellerService.getStockiestDetails(sellerc2code, cCode);
    }

    @Override
    public JsonArray getSellerPreferred(PageBO pageBO, LcHeaderBO lcHeaderBO) throws CommunicationErrorException, InvalidRequestException, RecordNotFoundException {
        return sellerService.getSellerPreferred(pageBO, lcHeaderBO);
    }

    @Override
    public Integer getSellerCount(Long firmId) throws CommunicationErrorException, InvalidRequestException {
        return sellerService.getSellerCount(firmId);
    }

    @Override
    public JsonArray getLimitedOfferList(PageBO pageBO) {
        return sellerService.getLimitedOfferList(pageBO);
    }

    @Override
    public Integer getOffersCount() throws CommunicationErrorException, InvalidRequestException {
        return sellerService.getOffersCount();
    }

    @Override
    public JsonObject createOffer(LcOfferMstEntity offer) throws DuplicateRecordException, URISyntaxException, IOException, StorageException {
        return sellerService.createOffer(offer);
    }

    @Override
    public JsonObject updateOffer(LcOfferMstEntity offer) throws RecordNotFoundException, URISyntaxException, IOException, StorageException {
        return sellerService.updateOffer(offer);
    }

    @Override
    public void deleteOffer(LcOfferMstEntity offer) throws RecordNotFoundException {
        sellerService.deleteOffer(offer);
    }

    @Override
    public JsonArray uploadSellerImage(MultipartFile[] files) throws InvalidRequestException, StorageException, IOException, URISyntaxException {
        return sellerService.uploadSellerImage(files);
    }

    @Override
    public void updateSellerImage(ImageUpdateBo imageUpdateBo) throws RecordNotFoundException {
        sellerService.updateSellerImage(imageUpdateBo);
    }

    @Override
    public List<SellerImageBo> getListOfMappedSeller(Long firmId, Long userId, int page, int limit) {
        return sellerService.getListOfMappedSeller(firmId, userId, page, limit);
    }


    @Override
    public JsonArray mappedSellerSearch(String searchString, int page, int size, long userId, long firmId, long branchId) throws RecordNotFoundException {
        return sellerService.mappedSellerSearch(searchString, page, size, userId, firmId, branchId);
    }

    public JsonArray fetchUnmappedSellers() {
        return sellerService.fetchUnmappedSellers();

    }

    @Override
    public JsonArray sendReqToSeller() {
        return sellerService.sendReqToSeller();
    }

    @Override
    public JsonArray fetchWithFilters() {
        return sellerService.fetchWithFilters();
    }

    @Override
    public JsonObject getSellerLogo(String sellerCode) {
        return sellerService.getSellerLogo(sellerCode);
    }

    @Override
    public JsonObject getUitemCode(String sellerCode, String sellerItemCode) {
        return sellerService.getUitemCode(sellerCode, sellerItemCode);
    }

}
