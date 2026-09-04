package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.entities.mongo.RecentHistory;
import com.c2.lc.ms.master.services.interfaces.RecentHistoryService;
import com.c2.lc.ms.master.transactions.base.MasterBaseTransactionImpl;
import com.c2.lc.ms.master.transactions.interfaces.RecentHistoryTransaction;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecentHistoryTransactionImpl  extends MasterBaseTransactionImpl implements RecentHistoryTransaction {

    @Autowired
    private RecentHistoryService recentHistoryService;

    @Override
    public void save(RecentHistory recentHistory) {
        recentHistoryService.save(recentHistory);
    }

    @Override
    public RecentHistory getById(long userId, String type, long firmId) throws RecordNotFoundException {
        return recentHistoryService.getById(userId, type,firmId);
    }

    @Override
    public void clearHistory(String type, long userId, long firmId) throws RecordNotFoundException {
         recentHistoryService.clearHistory(type, userId, firmId);
    }

    @Override
    public void clearSearchHistory(Long userId, String c2Code) {

    }

    @Override
    public void addRecentHistory(LcHeaderBO header, String type, String code) throws RecordNotFoundException, DuplicateRecordException {
        recentHistoryService.addRecentHistory(header,type,code);
    }

    @Override
    public JsonArray getManufactureDetails(LcHeaderBO header, String type) {
        return recentHistoryService.getManufactureDetails(header,type);
    }

    @Override
    public JsonArray getMolecules(LcHeaderBO header, String type) {
        return recentHistoryService.getMolecules(header,type);
    }

    @Override
    public JsonArray getSellerDetails(LcHeaderBO header, String type) {
        return recentHistoryService.getSellerDetails(header,type);
    }

    @Override
    public JsonArray getProductDetails(LcHeaderBO header, String type) {
        return recentHistoryService.getProductDetails(header,type);
    }
}
