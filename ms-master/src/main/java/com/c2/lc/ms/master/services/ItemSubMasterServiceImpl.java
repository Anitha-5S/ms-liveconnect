package com.c2.lc.ms.master.services;


import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.ItemListBO;
import com.c2.lc.ms.master.bos.ItemMapCountBO;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.ItemSubMasterService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ItemSubMasterServiceImpl extends MasterBaseServiceImpl implements ItemSubMasterService {


    @PersistenceContext(unitName = "sybase")
    @Autowired
    private EntityManager sybaseEntityManager;

    @Override
    public ItemMapCountBO count(String c2Code, String cTypeCode) throws RecordNotFoundException {

        String sql1 = "SELECT c_filter_flag_col FROM sub_mst_filter_mig WHERE c_list_id = :cTypeCode ";
        //Query query1 = this.getQuery(sql1);
        Query query1 = sybaseEntityManager.createNativeQuery(sql1);
        query1.setParameter("cTypeCode", cTypeCode);
        Object result1 = this.getSingleResult(query1);
        String type = result1.toString();

        String sql2 = "SELECT c_code_col FROM sub_mst_filter_mig WHERE c_list_id = :cTypeCode ";
        //Query query2 = this.getQuery(sql2);
        Query query2 = sybaseEntityManager.createNativeQuery(sql2);
        query2.setParameter("cTypeCode", cTypeCode);
        Object result2 = this.getSingleResult(query2);
        String type1 = result2.toString();

        String sql3 = "SELECT c_name_col FROM sub_mst_filter_mig WHERE c_list_id = :cTypeCode ";
       // Query query3 = this.getQuery(sql3);
        Query query3 = sybaseEntityManager.createNativeQuery(sql3);
        query3.setParameter("cTypeCode", cTypeCode);
        Object result3 = this.getSingleResult(query3);
        String type2 = result3.toString();


        String sql = " SELECT COUNT(*) AS c_total_count," +
                "SUM(IF " + type + " = '2' THEN 1 ELSE 0 ENDIF) AS c_mapped_count, " +
                "SUM(IF " + type + " = '1' THEN 1 ELSE 0 ENDIF) AS c_unmapped_count, " +
                "SUM(IF " + type + " = '3' THEN 1 ELSE 0 ENDIF) AS c_ownitems_count, " +
                "SUM(IF " + type + " = '4' THEN 1 ELSE 0 ENDIF) AS c_blocked_count " +
                "FROM ( SELECT DISTINCT " + type + " , " + type1 + ", " + type2 + " FROM item_mst_mig " +
                " WHERE c_c2code = :c2Code) t ";

        //Query query = this.getQuery(sql);
        Query query = sybaseEntityManager.createNativeQuery(sql);
        query.setParameter("c2Code", c2Code);
        List<Object[]> resultList = this.getResultList(query);

        if (resultList.isEmpty()) {
            throw new RecordNotFoundException("No Records found!");
        }

        ItemMapCountBO itemMapCountBO = new ItemMapCountBO();
        for (Object[] objects : resultList) {
            int i = -1;
            itemMapCountBO.setTotalCount(helper.getString(objects[++i]));
            itemMapCountBO.setMappedCount(helper.getString(objects[++i]));
            itemMapCountBO.setUnmappedCount(helper.getString(objects[++i]));
            itemMapCountBO.setOwnitemsCount(helper.getString(objects[++i]));
            itemMapCountBO.setBlockedCount(helper.getString(objects[++i]));
        }
        return itemMapCountBO;
    }

    @Override
    public int count(String c2Code) throws NoSuchFieldException {
        Object countList;
        int count = 0;
        String sql = "SELECT COUNT(*) FROM item_mst_mig WHERE c_c2code = :c2Code";
       // Query query = this.getQuery(sql);
        Query query = sybaseEntityManager.createNativeQuery(sql);
        query.setParameter("c2Code", c2Code);
        countList = this.getSingleResult(query);
        if (countList != null) {
            count = (Integer) countList;
        }

        return count;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteItem(String c2Code, String filterType, String c_Code) throws RecordNotFoundException {
        String sql1 = "SELECT c_filter_flag_col FROM sub_mst_filter_mig WHERE c_list_id = :filterType ";
       // Query query1 = this.getQuery(sql1);
        Query query1 = sybaseEntityManager.createNativeQuery(sql1);
        query1.setParameter("filterType", filterType);
        Object result1 = this.getSingleResult(query1);
        String type = result1.toString();

        String sql2 = "SELECT c_code_col FROM sub_mst_filter_mig WHERE c_list_id = :filterType ";
        //Query query2 = this.getQuery(sql2);
        Query query2 = sybaseEntityManager.createNativeQuery(sql2);
        query2.setParameter("filterType", filterType);
        Object result2 = this.getSingleResult(query2);
        String type1 = result2.toString();

        String selectSql = "SELECT * FROM item_mst_mig WHERE c_c2code = :c2Code AND " + type1 + " = :c_Code AND (" + type + " = 2 OR " + type + " = 4)";
        //Query query = this.getQuery(c2Code, selectSql);
        Query query = sybaseEntityManager.createNativeQuery(selectSql);
        query.setParameter("c2Code", c2Code);
        query.setParameter("c_Code", c_Code);
        Object resultList = this.getSingleResult(query);
        if (resultList.equals(0)) {
            throw new RecordNotFoundException("No Records found!");
        }

        String sql3 = "UPDATE item_mst_mig SET " + type + " = 1 WHERE c_c2code = :c2Code AND " + type1 + " = :c_Code AND (" + type + " = 2 OR " + type + " = 4)";
       // Query query3 = this.getQuery(sql3);
        Query query3 = sybaseEntityManager.createNativeQuery(sql3);
        query3.setParameter("c2Code", c2Code);
        query3.setParameter("c_Code", c_Code);
        sybaseEntityManager.joinTransaction();
        query3.executeUpdate();
       // this.executeUpdate(query3);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void moveToOwnAllManufactureList(String c2Code, String filterType, JsonArray arr, JsonObject jsonObject) throws RecordNotFoundException {
        String sql1 = "SELECT c_filter_flag_col FROM sub_mst_filter_mig WHERE c_list_id = :filterType ";
      //  Query query1 = this.getQuery(sql1);
        Query query1 = sybaseEntityManager.createNativeQuery(sql1);
        query1.setParameter("filterType", filterType);
        Object result1 = this.getSingleResult(query1);
        String type = result1.toString();

        String sql2 = "SELECT c_code_col FROM sub_mst_filter_mig WHERE c_list_id = :filterType ";
        //Query query2 = this.getQuery(sql2);
        Query query2 = sybaseEntityManager.createNativeQuery(sql2);
        query2.setParameter("filterType", filterType);
        Object result2 = this.getSingleResult(query2);
        String type1 = result2.toString();

        String selectSql = "SELECT * FROM item_mst_mig WHERE " + type + " = 1 AND c_c2code = :c2Code";
        //Query query = this.getQuery(c2Code, selectSql);
        Query query = sybaseEntityManager.createNativeQuery(selectSql);
        query.setParameter("c2Code", c2Code);
        Object resultList = this.getSingleResult(query);
        if (resultList.equals(0)) {
            throw new RecordNotFoundException("No Records found!");
        }
        List<String> codeList = new ArrayList<>();
        String sql = "";
        if (jsonObject.has("j_codes") && jsonObject.get("j_codes").getAsJsonArray().size() > 0) {
            for (int i = 0; i < jsonObject.get("j_codes").getAsJsonArray().size(); i++) {
                codeList.add(jsonObject.get("j_codes").getAsJsonArray().get(i).getAsString());
            }
            sql = "UPDATE item_mst_mig SET " + type + " = 3 WHERE " + type + " = 1 AND c_c2code = :c2Code AND " + type1 + " NOT IN (:list) ";
            //Query queryRes = this.getQuery(sql);
            Query queryRes = sybaseEntityManager.createNativeQuery(sql);
            queryRes.setParameter("c2Code", c2Code);
            queryRes.setParameter("list", codeList);
            sybaseEntityManager.joinTransaction();
            queryRes.executeUpdate();
            //this.executeUpdate(queryRes);
        }
        else {
            String sql3 = "UPDATE item_mst_mig SET " + type + " = 3 WHERE " + type + " = 1 AND c_c2code = :c2Code ";
           // Query query3 = this.getQuery(sql3);
            Query query3 = sybaseEntityManager.createNativeQuery(sql3);
            query3.setParameter("c2Code", c2Code);
            sybaseEntityManager.joinTransaction();
            query3.executeUpdate();
            // this.executeUpdate(query3);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void moveToBlockedManufacture(String c2Code, String filterType, String c_Code) throws RecordNotFoundException {
        String sql1 = "SELECT c_filter_flag_col FROM sub_mst_filter_mig WHERE c_list_id = :filterType ";
      //  Query query1 = this.getQuery(sql1);
        Query query1 = sybaseEntityManager.createNativeQuery(sql1);
        query1.setParameter("filterType", filterType);
        Object result1 = this.getSingleResult(query1);
        String type = result1.toString();

        String sql2 = "SELECT c_code_col FROM sub_mst_filter_mig WHERE c_list_id = :filterType ";
        //Query query2 = this.getQuery(sql2);
        Query query2 = sybaseEntityManager.createNativeQuery(sql2);
        query2.setParameter("filterType", filterType);
        Object result2 = this.getSingleResult(query2);
        String type1 = result2.toString();

        String selectSql = "SELECT * FROM item_mst_mig WHERE (" + type + " = 1 OR " + type + " = 3) AND c_c2code = :c2Code AND " + type1 + " = :c_Code ";
      //  Query query = this.getQuery(c2Code, selectSql);
        Query query = sybaseEntityManager.createNativeQuery(selectSql);
        query.setParameter("c2Code", c2Code);
        query.setParameter("c_Code", c_Code);
        Object resultList = this.getSingleResult(query);
        if (resultList.equals(0)) {
            throw new RecordNotFoundException("No Records found!");
        }

        String sql3 = "UPDATE item_mst_mig SET " + type + " = 4 WHERE (" + type + " = 1 OR " + type + " = 3) AND c_c2code = :c2Code AND " + type1 + " = :c_Code ";
       // Query query3 = this.getQuery(sql3);
        Query query3 = sybaseEntityManager.createNativeQuery(sql3);
        query3.setParameter("c2Code", c2Code);
        query3.setParameter("c_Code", c_Code);
        sybaseEntityManager.joinTransaction();
        query3.executeUpdate();
        //this.executeUpdate(query3);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void confirmManufacture(String c2Code, String filterType, String c_Code, String cSquareCode) throws RecordNotFoundException {
        String sql1 = "SELECT c_code_col FROM sub_mst_filter_mig WHERE c_list_id = :filterType ";
       // Query query1 = this.getQuery(sql1);
        Query query1 = sybaseEntityManager.createNativeQuery(sql1);
        query1.setParameter("filterType", filterType);
        Object result1 = this.getSingleResult(query1);
        String type = result1.toString();

        String sql2 = "SELECT c_csqr_code_col FROM sub_mst_filter_mig WHERE c_list_id = :filterType ";
       // Query query2 = this.getQuery(sql2);
        Query query2 = sybaseEntityManager.createNativeQuery(sql2);
        query2.setParameter("filterType", filterType);
        Object result2 = this.getSingleResult(query2);
        String type1 = result2.toString();

        String cSquareFilterNameSql="select c_csqr_name_col from sub_mst_filter_mig where c_list_id = :filterType";
        Query cSquareFilterNameQuery = sybaseEntityManager.createNativeQuery(cSquareFilterNameSql);
        cSquareFilterNameQuery.setParameter("filterType", filterType);
        Object cSquareFilterNameResult = this.getSingleResult(cSquareFilterNameQuery);
        String cSquareFilterName = cSquareFilterNameResult.toString();

        Query searchTable = subMappingSearchQuery(filterType);
        Object searchTableName = this.getSingleResult(searchTable);

        String cSquareNameSql = "select c_name from "+helper.getString(searchTableName)+" where c_code = :cSquareCode ";
        Query cSquareNameQuery = this.getQuery(cSquareNameSql);
        cSquareNameQuery.setParameter("cSquareCode", cSquareCode);
        Object cSquareNameResult = this.getSingleResult(cSquareNameQuery);
        String cSquareName = cSquareNameResult.toString();

        String filerSql = "SELECT c_filter_flag_col FROM sub_mst_filter_mig WHERE c_list_id = :filterType ";
       // Query filterQuery = this.getQuery(filerSql);
        Query filterQuery = sybaseEntityManager.createNativeQuery(filerSql);
        filterQuery.setParameter("filterType", filterType);
        Object result3 = this.getSingleResult(filterQuery);
        String type2 = result3.toString();

        String selectSql = "SELECT * FROM item_mst_mig WHERE c_c2code = :c2Code AND " + type + " = :c_Code AND (" + type2 + " = 1 OR " + type2 + " = 3) ";
       // Query query = this.getQuery(c2Code, selectSql);
        Query query = sybaseEntityManager.createNativeQuery(selectSql);
        query.setParameter("c2Code", c2Code);
        query.setParameter("c_Code", c_Code);
        Object resultList = this.getSingleResult(query);
        if (resultList.equals(0)) {
            throw new RecordNotFoundException("No Records found!");
        }

        String sql3 = "UPDATE item_mst_mig SET " + type1 + " = :cSquareCode," +
                " " + cSquareFilterName + " = :cSquareName, " + type2 + " = 2, " +
                " c_adate = today(), c_ltime = now()  WHERE c_c2code = :c2Code " +
                " AND " + type + " = :c_Code AND (" + type2 + " = 1 OR " + type2 + " = 3) ";
        //Query query3 = this.getQuery(sql3);
        Query query3 = sybaseEntityManager.createNativeQuery(sql3);
        query3.setParameter("cSquareCode", cSquareCode);
        query3.setParameter("cSquareName", cSquareName);
        query3.setParameter("c2Code", c2Code);
        query3.setParameter("c_Code", c_Code);
        sybaseEntityManager.joinTransaction();
        query3.executeUpdate();
        //this.executeUpdate(query3);
    }

    @Override
    public JsonObject moveToOwnManufacture(String c2Code, String filterType, ArrayList<Object> list) throws RecordNotFoundException {
        JsonObject jsonObject = new JsonObject();

        String sql1 = "SELECT c_filter_flag_col FROM sub_mst_filter_mig WHERE c_list_id = :filterType ";
        //Query query1 = this.getQuery(sql1);
        Query query1 = sybaseEntityManager.createNativeQuery(sql1);
        query1.setParameter("filterType", filterType);
        Object result1 = this.getSingleResult(query1);
        String type = result1.toString();

        String sql2 = "SELECT c_code_col FROM sub_mst_filter_mig WHERE c_list_id = :filterType ";
        //Query query2 = this.getQuery(sql2);
        Query query2 = sybaseEntityManager.createNativeQuery(sql2);
        query2.setParameter("filterType", filterType);
        Object result2 = this.getSingleResult(query2);
        String type1 = result2.toString();

        String selectSql = "SELECT * FROM item_mst_mig WHERE " + type + " = 1 AND c_c2code = :c2Code AND " + type1 + " IN (:list) ";
       // Query query = this.getQuery(c2Code, selectSql);
        Query query = sybaseEntityManager.createNativeQuery(selectSql);
        query.setParameter("c2Code", c2Code);
        query.setParameter("list", list);
        Object resultList = this.getSingleResult(query);
        if (resultList.equals(0)) {
            throw new RecordNotFoundException("No Records found!");
        }

        jsonObject.addProperty("c2Code",c2Code);
        jsonObject.addProperty("type",type);
        jsonObject.addProperty("type1",type1);
       // moveToOwnManufactureUpdate(c2Code, list, type, type1);
        System.out.println(jsonObject);
        return jsonObject;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void moveToOwnManufactureUpdate(String c2Code, ArrayList<Object> list, String type, String type1) {
        String sql3 = "UPDATE item_mst_mig SET " + type + " = 3 WHERE " + type + " = 1 AND c_c2code = :c2Code AND " + type1 + " IN (:list) ";
        //Query query3 = this.getQuery(sql3);
        Query query3 = sybaseEntityManager.createNativeQuery(sql3);
        query3.setParameter("c2Code", c2Code);
        query3.setParameter("list", list);
        sybaseEntityManager.joinTransaction();
        query3.executeUpdate();
    }

    @Override
    public List<ItemListBO> fetchItem(String c2Code, String listType, String filterType, String searchKey, int page, int limit) throws RecordNotFoundException {
        List<ItemListBO> list = new ArrayList<>();
        ItemListBO itemListBO;
        List<Object[]> allItems;

        Query query6 = fetchMappedItemsQuery(c2Code, filterType, searchKey, listType);
        allItems = this.getResultList(query6, page, limit);
        log.debug("fetch return");

        if (allItems.isEmpty()) {
            throw new RecordNotFoundException("No Records found");
        } else {
            for (Object[] objects : allItems) {
                itemListBO = new ItemListBO();
                itemListBO.setCCode(helper.getString(objects[0]));
                itemListBO.setCName(helper.getString(objects[1]));
                itemListBO.setCSquareCode(helper.getString(objects[2]));
                itemListBO.setCSquareName(helper.getString(objects[3]));
                list.add(itemListBO);
            }
        }
        return list;
    }

    private Query fetchMappedItemsQuery(String c2Code, String filterType, String searchKey, String listType) {
        String sql1 = "SELECT c_filter_flag_col FROM sub_mst_filter_mig WHERE c_list_id = :filterType ";
        //Query query1 = this.getQuery(sql1);
        Query query1 = sybaseEntityManager.createNativeQuery(sql1);
        query1.setParameter("filterType", filterType);
        Object result1 = this.getSingleResult(query1);
        String type = result1.toString();

       // String type = subMstFilterRepository.fetchFilterFlagCol(filterType);

        String sql2 = "SELECT c_code_col FROM sub_mst_filter_mig WHERE c_list_id = :filterType ";
        //Query query2 = this.getQuery(sql2);
        Query query2 = sybaseEntityManager.createNativeQuery(sql2);
        query2.setParameter("filterType", filterType);
        Object result2 = this.getSingleResult(query2);
        String type1 = result2.toString();

        String sql3 = "SELECT c_name_col FROM sub_mst_filter_mig WHERE c_list_id = :filterType ";
       // Query query3 = this.getQuery(sql3);
        Query query3 = sybaseEntityManager.createNativeQuery(sql3);
        query3.setParameter("filterType", filterType);
        Object result3 = this.getSingleResult(query3);
        String type2 = result3.toString();

        String sql4 = "SELECT c_csqr_code_col FROM sub_mst_filter_mig WHERE c_list_id = :filterType ";
       // Query query4 = this.getQuery(sql4);
        Query query4 = sybaseEntityManager.createNativeQuery(sql4);
        query4.setParameter("filterType", filterType);
        Object result4 = this.getSingleResult(query4);
        String type3 = result4.toString();

        String sql5 = "SELECT c_csqr_name_col FROM sub_mst_filter_mig WHERE c_list_id = :filterType ";
        //Query query5 = this.getQuery(sql5);
        Query query5 = sybaseEntityManager.createNativeQuery(sql5);
        query5.setParameter("filterType", filterType);
        Object result5 = this.getSingleResult(query5);
        String type4 = result5.toString();

        String sql6 = " SELECT DISTINCT " + type1 + " AS c_code, " + type2 + " AS c_name, " +
                " " + type3 + " AS c_csquare_code, " + type4 + " AS c_csquare_name " +
                " FROM item_mst_mig WHERE c_c2code = :c2Code AND " + type + " = :listType AND " + type2 + " LIKE :searchKey ";
        //Query query6 = this.getQuery(sql6);
        Query query6 = sybaseEntityManager.createNativeQuery(sql6);
        query6.setParameter("c2Code", c2Code);
        query6.setParameter("searchKey", helper.getLikeQueryString(searchKey));
        query6.setParameter("listType", listType);
        return query6;
    }

    @Override
    public List<JsonObject> fetchItemWithFilter(String c2Code) throws RecordNotFoundException {
        List<JsonObject> list = new ArrayList<>();
        JsonObject itemListBO;
        List<Object[]> allItems;
        String sql = " SELECT DISTINCT c_list_id AS c_type_code, c_list_type AS c_filtertype_name FROM sub_mst_filter_mig ";
      //  Query query = this.getQuery(sql);
        Query query = sybaseEntityManager.createNativeQuery(sql);
        allItems = this.getResultList(query);

        if (allItems.isEmpty()) {
            throw new RecordNotFoundException("No Records found");
        } else {
            for (Object[] objects : allItems) {
                itemListBO = new JsonObject();
                itemListBO.addProperty("c_type_code", helper.getString(objects[0]));
                itemListBO.addProperty("c_filtertype_name", helper.getString(objects[1]));
                list.add(itemListBO);
            }
        }

        return list;
    }

    @Override
    public JsonArray subMappingSearch(String search, String filterType, int offset, int limit) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        JsonArray array = new JsonArray();
        JsonObject res = new JsonObject();

        Query query4 = subMappingSearchQuery(filterType);
        Object tableName = this.getSingleResult(query4);

        if (tableName == null) {
            throw new RecordNotFoundException("No Records found");
        } else {
            String sql = "SELECT DISTINCT c_name, c_code FROM " + helper.getString(tableName) + " where c_name like '" + search + "%'";
            Query lcQuery = this.getQuery(sql);
            List<Object[]> resultList = this.getResultList(lcQuery, offset, limit);

            if (!resultList.isEmpty()) {
                for (Object[] objects : resultList) {
                    res.addProperty("c_name", helper.getString(objects[0]));
                    res.addProperty("c_code", helper.getString(objects[1]));
                    array.add(res);
                }
            }
        }
        return array;
    }

    @Override
    public int getLc1SearchCount(String colName, String searchKey) {
        List<Object[]> cNameList;
        int count = 0;

        String sql = "SELECT c_name FROM " + colName + " where c_name like '%" + searchKey + "%'";
        Query lcQuery = this.getQuery(sql);
        cNameList = this.getResultList(lcQuery);

        if (!cNameList.isEmpty()) {
            count = cNameList.size();
        }
        return count;
    }

    private Query subMappingSearchQuery(String filterType) {
        String sql1 = "SELECT c_search_table FROM sub_mst_filter_mig WHERE c_list_id = :filterType";
        //Query query1 = this.getQuery(sql1);
        Query query1 = sybaseEntityManager.createNativeQuery(sql1);
        query1.setParameter("filterType", filterType);

        return query1;
    }

    public JsonObject callCustomersService(JsonObject request, String url) throws InvalidRequestException, CommunicationErrorException {

        String result = callWebClientPostSyncApi(url, request.toString());

        JsonObject responseObject;
        if (result == null || result.isEmpty()) {
            log.error("Result is null API {} -- Request {} -- Response {}", url, request, result);
            throw new CommunicationErrorException("", "Error connecting to Customer Service!");
        } else {
            responseObject = helper.getJsonObject(result);
            if (responseObject.get("appStatusCode").getAsInt() != 0) {
                log.error("API {} -- Request {} -- Response {}", url, request, result);
                throw new InvalidRequestException("", "Invalid Request!");
            }
        }
        return responseObject.get("payloadJson").getAsJsonObject();
    }

    @Override
    public int mappedItemsCount(String c2Code, String listType, String cFilterType, String searchKey) {
        List<Object[]> countList;
        int count = 0;

        Query query = fetchMappedItemsQuery(c2Code, cFilterType, searchKey, listType);
        countList = this.getResultList(query);

        if(countList.size() > 0) {
            count = countList.size();
        }

        return count;
    }

    @Override
    public long mappingSearchCount(String c2Code, String search, String filterType) {
        List<Object[]> countList;
        long count = 0;

        Query query = subMappingSearchQuery(filterType);
        countList = this.getResultList(query);

        if(countList.size() > 0) {
            count = countList.size();
        }

        return count;
    }
}
