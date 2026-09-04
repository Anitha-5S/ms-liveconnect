package com.c2.lc.ms.master.services;

import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.NetmedsItemsService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

@Log4j2
@Service
public class NetmedsItemsServiceImpl extends MasterBaseServiceImpl implements NetmedsItemsService {


    @Override
    public JsonArray getItems(int recordIndex, int recordCount, String itemsToPushDate) {
        JsonArray items = new JsonArray();

        String selectSql = "SELECT u_item_mst.c_code AS productCode, " +
                "    u_item_mst.c_name AS productName, " +
                "    u_item_mst.c_sh_name AS shortName, " +
                "    u_item_mst.c_item_pack_Code AS itemPackCode, " +
                "    u_item_pack_mst.c_name AS itemPackName, " +
                "    u_item_mst.c_item_grp_code AS itemGroupCode, " +
                "    u_item_grp_mst.c_name AS itemGroupName, " +
                "    u_item_mst.c_item_cat_code AS itemCategoryCode, " +
                "    u_item_cat_mst.c_name AS itemCategoryName, " +
                "    u_item_mst.c_item_cont_code AS contentCode, " +
                "    u_item_cont_mst.c_name AS contentName, " +
                "    u_item_mst.c_item_brand_code AS itemBrandCode, " +
                "    u_item_brand_mst.c_name AS itemBrandName, " +
                "    u_item_mst.c_item_mfac_Code AS mfactCode, " +
                "    u_item_mfac_mst.c_name AS mfactName, " +
                "    u_item_mst.c_note AS itemNote, " +
                "    u_item_mst.d_adate AS itemCreatedDate, " +
                "    u_item_mst.c_hsn_code AS itemHsnSacCode, " +
                "    u_item_mst.n_lock AS itemLock, " +
                "    u_item_mst.d_ldate AS itemLastModifiedDate, " +
                "    u_item_mst.c_code AS uCode, " +
                "    u_item_mst.c_gst_code AS itemGstCode, " +
                "    u_item_cont_mst.c_item_schedule_Code AS ScheduleCode, " +
                "    u_item_sch_mst.c_name AS ScheduleName, " +
                "    u_item_mst.c_pack_type_code AS PackTypeCode, " +
                "    u_item_pack_type_mst.c_name AS PackTypeName " +
                "FROM u_item_mst " +
                "    LEFT JOIN u_item_pack_mst ON u_item_pack_mst.c_code = u_item_mst.c_pack_type_code " +
                "    LEFT JOIN u_item_grp_mst ON u_item_grp_mst.c_code = u_item_mst.c_item_grp_code " +
                "    LEFT JOIN u_item_cat_mst ON u_item_cat_mst.c_code = u_item_mst.c_item_cat_code " +
                "    LEFT JOIN u_item_cont_mst ON u_item_cont_mst.c_code = u_item_mst.c_item_cont_code " +
                "    LEFT JOIN u_item_brand_mst ON u_item_brand_mst.c_code = u_item_mst.c_item_brand_code " +
                "    LEFT JOIN u_item_mfac_mst ON u_item_mfac_mst.c_code = u_item_mst.c_item_mfac_code " +
                "    LEFT JOIN u_item_sch_mst ON u_item_sch_mst.c_code = u_item_cont_mst.c_item_schedule_Code " +
                "    LEFT JOIN u_item_pack_type_mst ON u_item_pack_type_mst.c_code = u_item_mst.c_item_pack_code " +
                "WHERE u_item_mst.d_adate = :date " +
                "LIMIT :recordIndex , :recordCount  ";

        Query query = this.getQuery(selectSql);
        query.setParameter("date", itemsToPushDate);
        query.setParameter("recordIndex", recordIndex);
        query.setParameter("recordCount", recordCount);

        List<Object[]> resultList = query.getResultList();
        if (resultList.size() > 0) {
            JsonObject item = new JsonObject();
            for (Object[] row : resultList) {

                int i = -1;
                item.addProperty("productCode", Long.valueOf(helper.getString(row[++i])));
                item.addProperty("productName", helper.getString(row[++i]));
                item.addProperty("shortName", helper.getString(row[++i]));
                item.addProperty("itemPackCode", helper.getString(row[++i]));
                item.addProperty("itemPackName", helper.getString(row[++i]));
                item.addProperty("itemGroupCode", helper.getString(row[++i]));
                item.addProperty("itemGroupName", helper.getString(row[++i]));
                item.addProperty("itemCategoryCode", helper.getString(row[++i]));
                item.addProperty("itemCategoryName", helper.getString(row[++i]));
                item.addProperty("contentCode", helper.getString(row[++i]));
                item.addProperty("contentName", helper.getString(row[++i]));
                item.addProperty("itemBrandCode", helper.getString(row[++i]));
                item.addProperty("itemBrandName", helper.getString(row[++i]));
                item.addProperty("mfactCode", helper.getString(row[++i]));
                item.addProperty("mfactName", helper.getString(row[++i]));
                item.addProperty("itemNote", helper.getString(row[++i]));
                item.addProperty("itemCreatedDate", helper.getString(row[++i]));
                item.addProperty("itemHsnSacCode", helper.getString(row[++i]));
                item.addProperty("itemLock", Long.valueOf(helper.getString(row[++i])));
                item.addProperty("itemLastModifiedDate", helper.getString(row[++i]));
                item.addProperty("uCode", Long.valueOf(helper.getString(row[++i])));
                item.addProperty("itemGstCode", Long.valueOf(helper.getString(row[++i])));
                item.addProperty("ScheduleCode", helper.getString(row[++i]));
                item.addProperty("ScheduleName", helper.getString(row[++i]));
                item.addProperty("PackTypeCode", helper.getString(row[++i]));
                item.addProperty("PackTypeName", helper.getString(row[++i]));

                items.add(item);
            }
        } else {
            items = null;
        }
        return items;
    }

    @Override
    public int getItemsCount(String itemsToPushDate) {
        String selectSql = "SELECT  " +
                "    COUNT(1) " +
                "FROM " +
                "    u_item_mst " +
                "WHERE " +
                "    u_item_mst.d_adate = :date ";
        Query query = this.getQuery(selectSql);
        query.setParameter("date", itemsToPushDate);

        BigInteger count = (BigInteger) this.getSingleResult(query);
        int value = 0;
        if (count != null) {
            value = count.intValue();
        }
        return value;
    }
}
