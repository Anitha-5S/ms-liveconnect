package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.ms.master.bos.ItemImageBO;
import com.c2.lc.ms.master.bos.Manufacture;
import com.c2.lc.ms.master.bos.ThumbnailBO;
import com.c2.lc.ms.master.entities.mongo.LcManufacture;
import com.c2.lc.ms.master.entities.mongo.LcPreferredMfc;
import com.c2.lc.ms.master.services.interfaces.CatalogueService;
import com.c2.lc.ms.master.transactions.interfaces.CatalogueTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CatalogueTransactionImpl extends BaseTransactionImpl implements CatalogueTransaction {

    @Autowired
    private CatalogueService catalogueService;

    @Override
    public List<Manufacture> getManufacturerList(LcHeaderBO lcHeaderBO, PageBO pageBO, JsonObject jsonObject) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {

        String state = catalogueService.getFirmState(lcHeaderBO.getFirmId());
        if (helper.isEmpty(state)) {
            throw new RecordNotFoundException("No state code found");
        }
        List<LcPreferredMfc> manufactures = catalogueService.preferredMfc(state, pageBO,jsonObject);
        List<Manufacture> manufactureList = new ArrayList<>();
        for (LcPreferredMfc lcManufacture : manufactures) {
            Manufacture manufacture = new Manufacture();
            manufacture.setManufactureCode(lcManufacture.getManufactureCode());
            manufacture.setManufactureName(lcManufacture.getManufactureName());
            List<ThumbnailBO> thumbnailBOS = new ArrayList<>();
            List<ItemImageBO> itemImageBOS = new ArrayList<>();
            ThumbnailBO thumbnailBO = new ThumbnailBO();
            ItemImageBO itemImageBO = new ItemImageBO();
            if (!helper.isEmpty(lcManufacture.getImageUrl())) {
                thumbnailBO.setThumbnailImage(lcManufacture.getImageUrl());
                itemImageBO.setItemImage(lcManufacture.getImageUrl());
                thumbnailBOS.add(thumbnailBO);
                itemImageBOS.add(itemImageBO);
            }
            manufacture.setImages(itemImageBOS);
            manufacture.setThumbnailImages(thumbnailBOS);
            manufactureList.add(manufacture);
        }
        return manufactureList;
    }

    @Override
    public void saveManufacture(LcManufacture lcManufacture) {
        catalogueService.saveManufacture(lcManufacture);
    }

    @Override
    public long manufactureCount(LcHeaderBO lcHeaderBO, JsonObject jsonObject) throws CommunicationErrorException, InvalidRequestException {
        String state = catalogueService.getFirmState(lcHeaderBO.getFirmId());
        return catalogueService.preferredMfcCount(state,jsonObject);
    }

    @Override
    public JsonArray getLimitedOfferList(PageBO pageBO) {
        return catalogueService.getLimitedOfferList(pageBO);
    }

}
