package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.entities.mongo.LcManufacture;
import com.c2.lc.ms.master.entities.mongo.LcPreferredMfc;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public interface CatalogueService {


    List<LcManufacture> getManufacturerList(PageBO pageBO) throws RecordNotFoundException;

    void saveManufacture(LcManufacture lcManufacture);

    long manufactureCount();

    JsonArray getLimitedOfferList(PageBO pageBO);

    String getFirmState(Long firmId) throws CommunicationErrorException, InvalidRequestException;

    JsonObject getFirm(Long firmId) throws CommunicationErrorException, InvalidRequestException;

    List<LcPreferredMfc> preferredMfc(String stateCode, PageBO pageBO, JsonObject jsonObject);

    long preferredMfcCount(String stateCode, JsonObject jsonObject);

}
