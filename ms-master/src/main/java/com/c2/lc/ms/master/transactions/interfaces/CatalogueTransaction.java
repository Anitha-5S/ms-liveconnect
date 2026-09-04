package com.c2.lc.ms.master.transactions.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.Manufacture;
import com.c2.lc.ms.master.entities.mongo.LcManufacture;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public interface CatalogueTransaction {

    List<Manufacture> getManufacturerList(LcHeaderBO lcHeaderBO, PageBO pageBO, JsonObject jsonObject) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    void saveManufacture(LcManufacture lcManufacture);

    long manufactureCount(LcHeaderBO lcHeaderBO, JsonObject jsonObject) throws CommunicationErrorException, InvalidRequestException;

    JsonArray getLimitedOfferList(PageBO pageBO);



}
