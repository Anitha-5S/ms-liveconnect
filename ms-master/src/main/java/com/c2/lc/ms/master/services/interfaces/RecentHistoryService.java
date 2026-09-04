package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.entities.mongo.RecentHistory;
import com.google.gson.JsonArray;

public interface RecentHistoryService {

    void save(RecentHistory recentHistory);

    RecentHistory getById(long userId, String type, long firmId) throws RecordNotFoundException;

    void clearHistory(String type, long userId, long firmId) throws RecordNotFoundException;

    void addRecentHistory(LcHeaderBO header, String type, String code) throws RecordNotFoundException, DuplicateRecordException;

    JsonArray getManufactureDetails(LcHeaderBO header, String type);

    JsonArray getMolecules(LcHeaderBO header, String type);

    JsonArray getSellerDetails(LcHeaderBO header, String type);

    JsonArray getProductDetails(LcHeaderBO header, String type);
}
