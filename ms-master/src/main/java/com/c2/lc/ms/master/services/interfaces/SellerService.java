package com.c2.lc.ms.master.services.interfaces;

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
import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface SellerService extends MasterBaseService {

    List<ItemSellersList> getSellerDetailsByItem(String buyerCode, String itemUCode);

    JsonArray getStockiestDetails(String sellerC2Code, String code);

    JsonArray getSellerPreferred(PageBO pageBO, LcHeaderBO lcHeaderBO) throws CommunicationErrorException, InvalidRequestException, RecordNotFoundException;

    Integer getSellerCount(Long firmId) throws CommunicationErrorException, InvalidRequestException;

    JsonArray getLimitedOfferList(PageBO pageBO);

    JsonObject createOffer(LcOfferMstEntity offer) throws DuplicateRecordException, URISyntaxException, IOException, StorageException;

    JsonObject updateOffer(LcOfferMstEntity offer) throws RecordNotFoundException, URISyntaxException, IOException, StorageException;

    void deleteOffer(LcOfferMstEntity offer) throws RecordNotFoundException;

    Integer getOffersCount();

    JsonArray uploadSellerImage(MultipartFile[] files) throws InvalidRequestException, StorageException, IOException, URISyntaxException;

    void updateSellerImage(ImageUpdateBo imageUpdateBo) throws RecordNotFoundException;

    List<SellerImageBo> getListOfMappedSeller(Long firmId, Long userId, int page, int limit);

    JsonArray mappedSellerSearch(String searchString, int page, int size, long userId, long firmId, long branchId) throws RecordNotFoundException;


    JsonArray fetchUnmappedSellers();

    JsonArray sendReqToSeller();

    JsonArray fetchWithFilters();

    JsonObject getSellerLogo(String sellerCode);

    JsonObject getUitemCode(String sellerCode, String sellerItemCode);
}