package com.c2.lc.ms.master.services;

import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.ItemListBO;
import com.c2.lc.ms.master.bos.ItemMapCountBO;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.ItemMappingService;
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
public class ItemMappingServiceImpl extends MasterBaseServiceImpl implements ItemMappingService {


    @Autowired
    private ItemSubMasterServiceImpl itemSubMasterService;

    @PersistenceContext(unitName = "sybase")
    @Autowired
    private EntityManager sybaseEntityManager;

    @Override
    public ItemMapCountBO itemCount(String c2Code) throws RecordNotFoundException {
        String sql = " SELECT COUNT(*) AS c_total_count, " +
                " SUM(IF c_item_map_flag = '2' THEN 1 ELSE 0 ENDIF) AS c_mapped_count, " +
                " SUM(IF c_item_map_flag = '1' THEN 1 ELSE 0 ENDIF) AS c_unmapped_count, " +
                " SUM(IF c_item_map_flag = '3' THEN 1 ELSE 0 ENDIF) AS c_ownitems_count, " +
                " SUM(IF c_item_map_flag = '4' THEN 1 ELSE 0 ENDIF) AS c_blocked_count " +
                " FROM \"DBA\".item_mst_mig WHERE c_c2code = :c2Code";

       // Query query = this.getQuery(sql);
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

    @Override
    public void checkIfItemExists(String c2Code, String itemCode) throws RecordNotFoundException {
        String selectSql = "SELECT * FROM item_mst_mig WHERE (c_item_map_flag = 2 OR c_item_map_flag = 4) AND c_c2code = :c2Code AND c_code = :itemCode ";
        Query query = sybaseEntityManager.createNativeQuery(selectSql);
        query.setParameter("c2Code", c2Code);
        query.setParameter("itemCode", itemCode);
        Object resultList = this.getSingleResult(query);
        if (resultList.equals(0)) {
            throw new RecordNotFoundException("No Records found!");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteItem(String c2Code, String itemCode) {
    String sql = " UPDATE item_mst_mig " +
                " SET c_item_map_flag = 1 " +
                " WHERE (c_item_map_flag = 2 OR c_item_map_flag = 4) AND c_c2code = :c2Code AND c_code = :itemCode ";

        Query query = sybaseEntityManager.createNativeQuery(sql);
        query.setParameter("c2Code", c2Code);
        query.setParameter("itemCode", itemCode);
        sybaseEntityManager.joinTransaction();
        query.executeUpdate();
        log.debug("Item  deleted {}", itemCode);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void moveToOwnAllItemList(String c2Code, JsonArray arr, JsonObject jsonObject) throws RecordNotFoundException {
        String selectSql = "SELECT * FROM item_mst_mig WHERE c_item_map_flag = 1 AND c_c2code = :c2Code ";
        //Query query1 = this.getQuery(c2Code, selectSql);
        Query query1 = sybaseEntityManager.createNativeQuery(selectSql);
        query1.setParameter("c2Code", c2Code);
        Object resultList = this.getSingleResult(query1);
        if (resultList.equals(0)) {
            throw new RecordNotFoundException("No Records found!");
        }
        List<String> codeList = new ArrayList<>();
        String sql = "";
        if (jsonObject.has("j_codes") && jsonObject.get("j_codes").getAsJsonArray().size() > 0) {
            for (int i = 0; i < jsonObject.get("j_codes").getAsJsonArray().size(); i++) {
                codeList.add(jsonObject.get("j_codes").getAsJsonArray().get(i).getAsString());
            }
            sql = " UPDATE item_mst_mig " +
                    " SET c_item_map_flag = 3 ," +
                    " c_adate = today()," +
                    " c_ltime = now() "+
                    " WHERE c_item_map_flag = 1 AND c_c2code = :c2Code AND c_code NOT IN (:list) ";
           // Query queryRes = this.getQuery(sql);
            Query queryRes = sybaseEntityManager.createNativeQuery(sql);
            queryRes.setParameter("c2Code", c2Code);
            queryRes.setParameter("list", codeList);
           // this.executeUpdate(queryRes);
            sybaseEntityManager.joinTransaction();
            queryRes.executeUpdate();
        } else {
             sql = " UPDATE item_mst_mig " +
                    " SET c_item_map_flag = 3, " +
                     " c_adate = today()," +
                     " c_ltime = now() "+
                    " WHERE c_item_map_flag = 1 AND c_c2code = :c2Code ";

           // Query query = this.getQuery(c2Code, sql);
            Query query = sybaseEntityManager.createNativeQuery(sql);
            query.setParameter("c2Code", c2Code);
            sybaseEntityManager.joinTransaction();
            query.executeUpdate();
            //this.executeUpdate(query);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void moveToBlockedItem(String c2Code, String itemCode) throws RecordNotFoundException {
        String selectSql = "SELECT * FROM item_mst_mig WHERE c_c2code = :c2Code AND c_code = :itemCode AND (c_item_map_flag = 1 OR c_item_map_flag = 3) ";
      //  Query query1 = this.getQuery(c2Code, selectSql);
        Query query1 = sybaseEntityManager.createNativeQuery(selectSql);
        query1.setParameter("c2Code", c2Code);
        query1.setParameter("itemCode", itemCode);
        Object resultList = this.getSingleResult(query1);
        if (resultList.equals(0)) {
            throw new RecordNotFoundException("No Records found!");
        }

        String sql = " UPDATE item_mst_mig " +
                " SET c_item_map_flag = 4 , " +
                " c_adate = today()," +
                " c_ltime = now() "+
                " WHERE c_c2code = :c2Code AND c_code = :itemCode AND (c_item_map_flag = 1 OR c_item_map_flag = 3) ";

        //Query query = this.getQuery(c2Code, sql);
        Query query = sybaseEntityManager.createNativeQuery(sql);
        query.setParameter("c2Code", c2Code);
        query.setParameter("itemCode", itemCode);
        sybaseEntityManager.joinTransaction();
        query.executeUpdate();
        //this.executeUpdate(query);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void confirmItem(String c2Code, String itemCode, String cSquareItemCode, String cSquareItemName) throws RecordNotFoundException, InvalidRequestException, CommunicationErrorException {
        String selectSql = "SELECT * FROM item_mst_mig WHERE (c_item_map_flag = 1 OR c_item_map_flag = 3) AND c_code = :itemCode AND c_c2code = :c2Code ";
       // Query query1 = this.getQuery(c2Code, selectSql);
        Query query1 = sybaseEntityManager.createNativeQuery(selectSql);
        query1.setParameter("c2Code", c2Code);
        query1.setParameter("itemCode", itemCode);
        Object resultList = this.getSingleResult(query1);
        if (resultList.equals(0)) {
            throw new RecordNotFoundException("No Records found!");
        }
        String itemUCode = cSquareItemCode;
        JsonObject obj =  getItemMappingInfo(itemUCode);
       // JsonObject data = obj.get("data").getAsJsonObject();
         
        String updateSql = updateItem();
        Query query = sybaseEntityManager.createNativeQuery(updateSql);
        query.setParameter("c_csquare_item_code", cSquareItemCode);
        query.setParameter("c_full_name", obj.get("c_full_name").getAsString());
        query.setParameter("c_csquare_mfac_code", obj.get("c_csquare_mfac_code").getAsString());
        query.setParameter("c_csquare_pack_code", obj.get("c_csquare_pack_code").getAsString());
        query.setParameter("c_csquare_pack_type_code", obj.get("c_csquare_pack_type_code").getAsString());
        query.setParameter("c_csquare_cat_code", obj.get("c_csquare_cat_code").getAsString());
        query.setParameter("c_csquare_cont_code", obj.get("c_csquare_cont_code").getAsString());
        query.setParameter("c_csquare_mfac_name", obj.get("c_csquare_mfac_name").getAsString());
        query.setParameter("c_csquare_pack_name", obj.get("c_csquare_pack_name").getAsString());
        query.setParameter("c_csquare_pack_type_name", obj.get("c_csquare_pack_type_name").getAsString());
        query.setParameter("c_csquare_cat_name", obj.get("c_csquare_cat_name").getAsString());
        query.setParameter("c_csquare_cont_name", obj.get("c_csquare_cont_name").getAsString());
        query.setParameter("c_schedule_name", obj.get("c_schedule_name").getAsString());
        query.setParameter("c_hsn_code", obj.get("c_hsn_code").getAsString());
        query.setParameter("itemCode", itemCode);
        query.setParameter("c2Code", c2Code);
        System.out.println(query);
        sybaseEntityManager.joinTransaction();
        query.executeUpdate();
    }

    private JsonObject getItemMappingInfo(String itemUcode) {
        JsonObject jObj = new JsonObject();
        String sql = getItemMappingDetails();
        Query query = this.getQuery(sql);
        query.setParameter("itemUcode",itemUcode);
        List<Object[]>  resultList = this.getResultList(query);
        if (resultList.isEmpty()) {
            return jObj;

        } else {
            for (Object[] objects : resultList) {
                jObj.addProperty("c_full_name", helper.getString(objects[0]));
                jObj.addProperty("c_csquare_mfac_code", helper.getString(objects[1]));
                jObj.addProperty("c_csquare_pack_code", helper.getString(objects[2]));
                jObj.addProperty("c_csquare_pack_type_code", helper.getString(objects[3]));
                jObj.addProperty("c_csquare_cat_code", helper.getString(objects[4]));
                jObj.addProperty("c_csquare_cont_code", helper.getString(objects[5]));
                jObj.addProperty("c_csquare_mfac_name", helper.getString(objects[6]));
                jObj.addProperty("c_csquare_pack_name", helper.getString(objects[7]));
                jObj.addProperty("c_csquare_pack_type_name", helper.getString(objects[8]));
                jObj.addProperty("c_csquare_cat_name", helper.getString(objects[9]));
                jObj.addProperty("c_csquare_cont_name", helper.getString(objects[10]));
                jObj.addProperty("c_schedule_name",helper.getString(objects[11]));
                jObj.addProperty("c_hsn_code",helper.getString(objects[12]));
            }
        }
        return jObj;
    }

    private String getItemMappingDetails() {
        return "SELECT distinct  " +
                "order_buk_new.u_item_mst.c_full_name as c_full_name,  " +
                "order_buk_new.u_item_mst.c_item_mfac_code as c_csquare_mfac_code,  " +
                "order_buk_new.u_item_mst.c_item_pack_code as c_csquare_pack_code,  " +
                "order_buk_new.u_item_mst.c_pack_type_code as c_csquare_pack_type_code,  " +
                "order_buk_new.u_item_mst.c_item_cat_code as c_csquare_cat_code,  " +
                "order_buk_new.u_item_mst.c_item_cont_code as c_csquare_cont_code,  " +
                "order_buk_new.u_item_mfac_mst.c_name as c_csquare_mfac_name,  " +
                "order_buk_new.u_item_pack_mst.c_name as c_csquare_pack_name,  " +
                "order_buk_new.u_item_pack_type_mst.c_name as c_csquare_pack_type_name,  " +
                "order_buk_new.u_item_cat_mst.c_name as c_csquare_cat_name,  " +
                "order_buk_new.u_item_cont_mst.c_name as c_csquare_cont_name,  " +
                "order_buk_new.u_item_sch_mst.c_name as c_schedule_name,  " +
                "order_buk_new.u_item_mst.c_hsn_code as c_hsn_code  " +
                "FROM order_buk_new.u_stockiest_item  " +
                "left join order_buk_new.u_item_mst on order_buk_new.u_stockiest_item.c_ucode = order_buk_new.u_item_mst.c_code  " +
                "join order_buk_new.u_item_mfac_mst on order_buk_new.u_item_mst.c_item_mfac_code = order_buk_new.u_item_mfac_mst.c_code  " +
                "join order_buk_new.u_item_pack_mst on order_buk_new.u_item_mst.c_item_pack_code = order_buk_new.u_item_pack_mst.c_code  " +
                "join order_buk_new.u_item_pack_type_mst on order_buk_new.u_item_mst.c_pack_type_code = order_buk_new.u_item_pack_type_mst.c_code  " +
                "join order_buk_new.u_item_cat_mst on order_buk_new.u_item_mst.c_item_cat_code = order_buk_new.u_item_cat_mst.c_code  " +
                "join order_buk_new.u_item_cont_mst on order_buk_new.u_item_mst.c_item_cont_code = order_buk_new.u_item_cont_mst.c_code  " +
                "join order_buk_new.u_item_sch_mst on order_buk_new.u_item_sch_mst.c_code = order_buk_new.u_item_cont_mst.c_item_schedule_code "+
                "where " +
                "order_buk_new.u_stockiest_item.c_ucode = :itemUcode ";
    }


    private String updateItem() {
        return "UPDATE item_mst_mig " +
                "SET c_ucode = :c_csquare_item_code, " +
                "c_item_map_flag = 2, " +
                "c_mfac_map_flag = 2, " +
                "c_packing_flag = 2, " +
                "c_pack_type_flag = 2, " +
                "c_category_flag = 2, " +
                "c_content_flag =2, " +
                "c_full_name = :c_full_name, " +
                "c_csquare_mfac_code = :c_csquare_mfac_code, " +
                "c_csquare_pack_code = :c_csquare_pack_code, " +
                "c_csquare_pack_type_code = :c_csquare_pack_type_code, " +
                "c_csquare_cat_code = :c_csquare_cat_code, " +
                "c_csquare_cont_code = :c_csquare_cont_code, " +
                "c_csquare_mfac_name = :c_csquare_mfac_name, " +
                "c_csquare_pack_name = :c_csquare_pack_name, " +
                "c_csquare_pack_type_name = :c_csquare_pack_type_name, " +
                "c_csquare_cat_name = :c_csquare_cat_name, " +
                "c_csquare_cont_name = :c_csquare_cont_name, " +
                "c_qc_note1 = :c_schedule_name, " +
                "c_qc_note2 = :c_hsn_code, " +
                "c_adate = today(), " +
                "c_ltime = now() "+
                "WHERE c_code = :itemCode " +
                "and c_c2code = :c2Code and c_item_map_flag = 1 ";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void moveToOwnItem(String c2Code, JsonArray arr) throws RecordNotFoundException {
        ArrayList<Object> list = new ArrayList<>();
        if (arr != null) {
            for (int i = 0; i < arr.size(); i++) {
                list.add(arr.get(i).getAsString());
            }
        }

        String selectSql = "SELECT * FROM item_mst_mig WHERE c_item_map_flag = 1 AND c_c2code = :c2Code AND c_code IN (:list) ";
        //Query query1 = this.getQuery(c2Code, selectSql);
        Query query1 = sybaseEntityManager.createNativeQuery(selectSql);
        query1.setParameter("c2Code", c2Code);
        query1.setParameter("list", list);
        Object resultList = this.getSingleResult(query1);
        if (resultList.equals(0)) {
            throw new RecordNotFoundException("No Records found!");
        }

        String sql = " UPDATE item_mst_mig " +
                "SET c_item_map_flag = 3, " +
                " c_adate = today()," +
                " c_ltime = now() "+
                "WHERE c_item_map_flag = 1 AND c_c2code = :c2Code AND c_code IN (:list) ";

        //Query query = this.getQuery(c2Code, sql);
        Query query = sybaseEntityManager.createNativeQuery(sql);
        query.setParameter("c2Code", c2Code);
        query.setParameter("list", list);
        sybaseEntityManager.joinTransaction();
        query.executeUpdate();
        //this.executeUpdate(query);
    }

    @Override
    public List<ItemListBO> fetchItem(String c2Code, String listType, int page, int limit) throws RecordNotFoundException {
        List<ItemListBO> list = new ArrayList<>();
        ItemListBO itemListBO;
        List<Object[]> allItems;
        String sql = getAllItems();

        //Query query = this.getQuery(c2Code, sql);
        Query query = sybaseEntityManager.createNativeQuery(sql);
        query.setParameter("c2Code", c2Code);
        query.setParameter("itemMapFlag", listType);
        allItems = this.getResultList(query, page, limit);

        if (allItems.isEmpty()) {
            throw new RecordNotFoundException("No Records found");
        } else {
            for (Object[] objects : allItems) {
                int i = -1;
                itemListBO = new ItemListBO();
                itemListBO.setItemCode(helper.getString(objects[++i]));
                itemListBO.setItemName(helper.getString(objects[++i]));
                itemListBO.setPackageSize(helper.getString(objects[++i]));
                itemListBO.setCSquareItemCode(helper.getString(objects[++i]));
                itemListBO.setCSquareItemName(helper.getString(objects[++i]));
                list.add(itemListBO);
            }
        }
        return list;
    }

    private String getAllItems() {
        return "SELECT " +
                "imm.c_code as c_item_code, " +
                "imm.c_name as c_item_name, " +
                "imm.C_Pack_Name as c_packing_size, " +
                "imm.c_ucode as c_csquare_item_code, " +
                "imm.c_full_name as c_csquare_item_name, " +
                "count(*) as count " +
                "from item_mst_mig imm " +
                "left outer join trans_det_mig tdm on tdm.c_item_code=imm.c_code " +
                "where imm.c_c2code= :c2Code and imm.c_item_map_flag= :itemMapFlag " +
                "group by  imm.c_code,imm.c_name,imm.c_ucode,imm.c_full_name, imm.C_Pack_Name " +
                "order by  count desc ";
    }

}
