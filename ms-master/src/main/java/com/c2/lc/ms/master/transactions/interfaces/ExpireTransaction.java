package com.c2.lc.ms.master.transactions.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.*;
import com.c2.lc.ms.master.transactions.interfaces.base.MasterBaseTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.itextpdf.text.DocumentException;

import java.io.IOException;

public interface ExpireTransaction extends MasterBaseTransaction {

    JsonArray getBatchItem(BatchItemBo itemBo, PageBO pageBO) throws RecordNotFoundException;

    void newBatch(BatchBo batchBo);

    JsonArray getExpireItem(BatchItemBo itemBo, PageBO pageBO, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;

    void addExpiryCart(ExpiryCart cart, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;

    void deleteExpiryCart(DeleteExpiry cart) throws RecordNotFoundException;

    JsonArray getExpireCart(String sellerCode, LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException;

    JsonArray confirmCart(String sellerCode, LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException;

    JsonArray getExpireOrders(ExpiryOrderFilterBo filterBo, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;

    JsonObject getOrdersById(ExpireOrderIdBo orderIdBo, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;

    JsonObject getExpiryForm(String code, String buyerCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException, DocumentException, IOException;

    long getExpiryOrdersCount(ExpiryOrderFilterBo filterBo, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;

}
