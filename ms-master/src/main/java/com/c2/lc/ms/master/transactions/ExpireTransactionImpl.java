package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.*;
import com.c2.lc.ms.master.services.interfaces.DocumentService;
import com.c2.lc.ms.master.services.interfaces.ExpireService;
import com.c2.lc.ms.master.transactions.base.MasterBaseTransactionImpl;
import com.c2.lc.ms.master.transactions.interfaces.ExpireTransaction;
import com.c2.lc.ms.master.transactions.interfaces.base.MasterBaseTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ExpireTransactionImpl extends MasterBaseTransactionImpl implements ExpireTransaction {

    @Autowired private ExpireService expireService;

    @Autowired private DocumentService documentService;

    @Override
    public JsonArray getBatchItem(BatchItemBo itemBo, PageBO pageBO) throws RecordNotFoundException {
        return expireService.getBatchItem(itemBo, pageBO);
    }

    @Override
    public void newBatch(BatchBo batchBo) {
        expireService.newBatch(batchBo);
    }

    @Override
    public JsonArray getExpireItem(BatchItemBo itemBo, PageBO pageBO, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        return expireService.getExpireItem(itemBo, pageBO,lcHeaderBO );
    }

    @Override
    public void addExpiryCart(ExpiryCart cart, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        expireService.addExpiryCart(cart, lcHeaderBO);
    }

    @Override
    public void deleteExpiryCart(DeleteExpiry cart) throws RecordNotFoundException {
        expireService.deleteExpiryCart(cart);
    }

    @Override
    public JsonArray getExpireCart(String sellerCode, LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException {
        return expireService.getExpireCart(sellerCode, lcHeaderBO, pageBO);
    }

    @Override
    public JsonArray confirmCart(String sellerCode, LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException {
        return expireService.confirmCart(sellerCode, lcHeaderBO, pageBO);
    }

    @Override
    public JsonArray getExpireOrders(ExpiryOrderFilterBo filterBo, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        return expireService.getExpireOrders(filterBo, lcHeaderBO);
    }

    @Override
    public JsonObject getOrdersById(ExpireOrderIdBo orderIdBo, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        return expireService.getOrdersById(orderIdBo, lcHeaderBO);
    }

    @Override
    public JsonObject getExpiryForm(String code, String buyerCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException, DocumentException, IOException {

        JsonObject info = expireService.buyerSellerInfo(code, buyerCode);
        String byteString = documentService.generateExpiryReturn(info, lcHeaderBO);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data", byteString);
        return jsonObject;
    }

    @Override
    public long getExpiryOrdersCount(ExpiryOrderFilterBo filterBo, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        return expireService.getExpiryOrdersCount(filterBo, lcHeaderBO);
    }
}
