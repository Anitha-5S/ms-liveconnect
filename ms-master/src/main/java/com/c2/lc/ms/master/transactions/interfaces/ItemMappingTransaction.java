package com.c2.lc.ms.master.transactions.interfaces;

import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.ItemListBO;
import com.c2.lc.ms.master.bos.ItemMapCountBO;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public interface ItemMappingTransaction {
    ItemMapCountBO itemCount(String c2Code) throws RecordNotFoundException;

    void deleteItem(String c2Code, String itemCode) throws RecordNotFoundException;

    void moveToOwnAllItemList(String c2Code, JsonArray arr, JsonObject json) throws RecordNotFoundException;

    void moveToBlockedItem(String c2Code, String itemCode) throws RecordNotFoundException;

    void confirmItem(String c2Code, String itemCode, String cSquareItemCode, String cSquareItemName) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    void moveToOwnItem(String c2Code, JsonArray arr) throws RecordNotFoundException;

    List<ItemListBO> fetchItem(String c2Code, String listType, int page, int limit) throws RecordNotFoundException;

    int count(String c2Code) throws NoSuchFieldException;

}
