package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.ItemListBO;
import com.c2.lc.ms.master.bos.ItemMapCountBO;
import com.c2.lc.ms.master.services.interfaces.ItemSubMasterService;
import com.c2.lc.ms.master.transactions.interfaces.ItemSubMasterTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemSubMasterTransactionImpl implements ItemSubMasterTransaction {
    @Autowired
    ItemSubMasterService itemSubMasterService;

    @Override
    public ItemMapCountBO count(String c2Code, String cTypeCode) throws RecordNotFoundException {
        return itemSubMasterService.count(c2Code, cTypeCode);
    }

    @Override
    public void deleteItem(String c2Code, String filterType, String c_Code) throws RecordNotFoundException {
        itemSubMasterService.deleteItem(c2Code, filterType, c_Code);
    }

    @Override
    public void moveToOwnAllManufactureList(String c2Code, String filterType, JsonArray arr, JsonObject json) throws RecordNotFoundException {
        itemSubMasterService.moveToOwnAllManufactureList(c2Code, filterType, arr,json);
    }

    @Override
    public void moveToBlockedManufacture(String c2Code, String filterType, String c_Code) throws RecordNotFoundException {
        itemSubMasterService.moveToBlockedManufacture(c2Code, filterType, c_Code);
    }

    @Override
    public void confirmManufacture(String c2Code, String filterType, String c_Code, String cSquareCode) throws RecordNotFoundException {
        itemSubMasterService.confirmManufacture(c2Code, filterType, c_Code, cSquareCode);
    }

    @Override
    public void moveToOwnManufacture(String c2Code, String filterType, JsonArray arr) throws RecordNotFoundException {
        ArrayList<Object> list = new ArrayList<>();
        if (arr != null) {
            for (int i = 0; i < arr.size(); i++) {
                list.add(arr.get(i).getAsString());
            }
        }
        JsonObject jObj = itemSubMasterService.moveToOwnManufacture(c2Code, filterType, list);
        itemSubMasterService.moveToOwnManufactureUpdate(c2Code,list,jObj.get("type").getAsString(),jObj.get("type1").getAsString());
    }

    @Override
    public List<ItemListBO> fetchItem(String c2Code, String listType, String filterType, String searchKey, int page, int limit) throws RecordNotFoundException {
        return itemSubMasterService.fetchItem(c2Code, listType, filterType, searchKey, page, limit);
    }

    @Override
    public int count(String c2Code) throws NoSuchFieldException {
        return itemSubMasterService.count(c2Code);
    }

    @Override
    public List<JsonObject> fetchItemWithFilter(String c2Code) throws RecordNotFoundException {
        return itemSubMasterService.fetchItemWithFilter(c2Code);
    }

    @Override
    public JsonArray subMappingSearch(String search, String filterType, int offset, int limit) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        return itemSubMasterService.subMappingSearch(search, filterType, offset, limit);
    }

    @Override
    public int getLc1SearchCount(String colName, String searchKey){
        return itemSubMasterService.getLc1SearchCount(colName,searchKey);
    }
    @Override
    public int mappedItemsCount(String c2Code, String listType, String cFilterType, String searchKey) {
        return itemSubMasterService.mappedItemsCount(c2Code, listType, cFilterType, searchKey);
    }

    @Override
    public long mappingSearchCount(String c2Code, String search, String filterType) {
        return itemSubMasterService.mappingSearchCount(c2Code, search, filterType);
    }
}
