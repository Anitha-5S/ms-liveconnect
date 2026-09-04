package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.ItemListBO;
import com.c2.lc.ms.master.bos.ItemMapCountBO;
import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public interface ItemSubMasterService extends MasterBaseService {

    ItemMapCountBO count(String c2Code, String cTypeCode) throws RecordNotFoundException;

    void deleteItem(String c2Code, String filterType, String c_Code) throws RecordNotFoundException;

    void moveToOwnAllManufactureList(String c2Code, String filterType, JsonArray arr, JsonObject json) throws RecordNotFoundException;

    void moveToBlockedManufacture(String c2Code, String filterType, String c_Code) throws RecordNotFoundException;

    void confirmManufacture(String c2Code, String filterType, String c_Code, String cSquareCode) throws RecordNotFoundException;

    JsonObject moveToOwnManufacture(String c2Code, String filterType, ArrayList<Object> arr) throws RecordNotFoundException;

    List<ItemListBO> fetchItem(String c2Code, String listType, String filterType, String searchKey, int page, int limit) throws RecordNotFoundException;

    int count(String c2Code) throws NoSuchFieldException;

    List<JsonObject> fetchItemWithFilter(String c2Code) throws RecordNotFoundException;

    JsonArray subMappingSearch(String search, String filterType, int offset, int limit) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    int mappedItemsCount(String c2Code, String listType, String cFilterType, String searchKey);

    long mappingSearchCount(String c2Code, String search, String filterType);

    int getLc1SearchCount(String colName, String searchKey);

    void moveToOwnManufactureUpdate(String c2Code, ArrayList<Object> list, String type, String type1);
}
