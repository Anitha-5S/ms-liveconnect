package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.ItemListBO;
import com.c2.lc.ms.master.bos.ItemMapCountBO;
import com.c2.lc.ms.master.services.interfaces.ItemMappingService;
import com.c2.lc.ms.master.transactions.interfaces.ItemMappingTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;


@Component
public class ItemMappingTransactionImpl implements ItemMappingTransaction {

    @Autowired
    private ItemMappingService itemMappingService;

    @Override
    public ItemMapCountBO itemCount(String c2Code) throws RecordNotFoundException {
        return itemMappingService.itemCount(c2Code);
    }

    @Override
    public void deleteItem(String c2Code, String itemCode) throws RecordNotFoundException {
        itemMappingService.checkIfItemExists(c2Code, itemCode);
        itemMappingService.deleteItem(c2Code, itemCode);
    }

    @Override
    public void moveToOwnAllItemList(String c2Code, JsonArray arr, JsonObject json) throws RecordNotFoundException {
        itemMappingService.moveToOwnAllItemList(c2Code, arr, json);
    }

    @Override
    public void moveToBlockedItem(String c2Code, String itemCode) throws RecordNotFoundException {
        itemMappingService.moveToBlockedItem(c2Code, itemCode);
    }

    @Override
    public void confirmItem(String c2Code, String itemCode, String cSquareItemCode, String cSquareItemName) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        itemMappingService.confirmItem(c2Code, itemCode, cSquareItemCode, cSquareItemName);
    }

    @Override
    public void moveToOwnItem(String c2Code, JsonArray arr) throws RecordNotFoundException {
        itemMappingService.moveToOwnItem(c2Code, arr);
    }

    @Override
    public List<ItemListBO> fetchItem(String c2Code, String listType, int page, int limit) throws RecordNotFoundException {
        return itemMappingService.fetchItem(c2Code, listType, page, limit);
    }

    @Override
    public int count(String c2Code) throws NoSuchFieldException {
        return itemMappingService.count(c2Code);
    }
}
