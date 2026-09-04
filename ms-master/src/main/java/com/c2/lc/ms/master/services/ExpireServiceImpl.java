package com.c2.lc.ms.master.services;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.*;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.ExpireService;
import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Service
public class ExpireServiceImpl extends MasterBaseServiceImpl implements ExpireService {


    @Override
    public JsonArray getBatchItem(BatchItemBo itemBo, PageBO pageBO) throws RecordNotFoundException {

        String sql = getBatchQuery(itemBo);
        Query query = this.getQuery(sql);
        JsonArray arr = new JsonArray();
        List<Object[]> resultList = this.getResultList(query, pageBO.getPage(), pageBO.getLimit());
        if (resultList == null || resultList.size() == 0) {
            throw new RecordNotFoundException("No Batch Found..!");
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("n_batch_id", helper.getString(objects[0]));
            jsonObject.addProperty("c_batch_name", helper.getString(objects[1]));
            jsonObject.addProperty("n_mrp", helper.getString(objects[2]));
            jsonObject.addProperty("d_expiry_date", helper.getString(objects[4]));
            arr.add(jsonObject);
        }
        return arr;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void newBatch(BatchBo batchBo) {

        if (helper.isEmpty(getCustBatch(batchBo.getSellerCode(), batchBo.getItemCode(), batchBo.getBatch())) &&
                helper.isEmpty(getCustExpBatch(batchBo.getSellerCode(), batchBo.getItemCode(), batchBo.getBatch()))) {
            String sql = "INSERT INTO cust_exp_item_stock(c_c2code,c_item_code,c_batch_no,d_exp_dt,n_mrp,t_ltime) " +
                    "                            VALUES(:sellerCode,:itemCode,:batch,:expDt,:mrp,NOW()) " +
                    "                            on duplicate key update c_batch_no=:batch";
            Query query = this.getQuery(sql);
            query.setParameter("sellerCode", batchBo.getSellerCode());
            query.setParameter("itemCode", batchBo.getItemCode());
            query.setParameter("batch", batchBo.getBatch());
            query.setParameter("expDt", helper.convertStringToDate(batchBo.getExpiryDate()));
            query.setParameter("mrp", batchBo.getMrp());
            query.executeUpdate();
        }
    }

    @Override
    public JsonArray getExpireItem(BatchItemBo itemBo, PageBO pageBO, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        String buyer = getBuyerCode(itemBo.getSellerCode(), lcHeaderBO);
        JsonArray array = new JsonArray();
        Query query = this.getQuery(getExpireItem(itemBo.getSellerCode(), buyer,  itemBo.getSearchTerm(), itemBo.getItemCode()));
        List<Object[]> resultList = this.getResultList(query, pageBO.getPage(), pageBO.getLimit());
        if (resultList == null || resultList.size() == 0) {
            throw new RecordNotFoundException("No Batch Found..!");
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_seller_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_buyer_code", helper.getString(objects[1]));
            jsonObject.addProperty("c_item_code", helper.getString(objects[2]));
            jsonObject.addProperty("c_item_name", helper.getString(objects[3]));
            jsonObject.addProperty("n_expiry_days", helper.getString(objects[4]));
            jsonObject.addProperty("d_expiry_date", helper.getString(objects[5]));
            jsonObject.addProperty("c_mfg_name", helper.getString(objects[6]));
            jsonObject.addProperty("c_mfg_code", helper.getString(objects[7]));
            jsonObject.addProperty("n_percentage", helper.getString(objects[8]));
            jsonObject.addProperty("c_batch_name", helper.getString(objects[9]));
            jsonObject.addProperty("n_mrp", helper.getString(objects[10]));
            jsonObject.addProperty("n_rev_mrp", helper.getString(objects[11]));
            jsonObject.addProperty("n_qty_per_box", helper.getString(objects[12]));
            jsonObject.addProperty("c_pack_size", helper.getString(objects[13]));
            jsonObject.addProperty("n_scheme", helper.getString(objects[14]));
            array.add(jsonObject);

        }
        return array;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void addExpiryCart(ExpiryCart cart, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {

        if (getChemistStatus(cart.getSellerCode(), cart.getBuyer()) == 1) {
            String smanCode = getSmanCode(cart.getSellerCode(), cart.getBuyer());
            JsonObject jsonObject = getItemInfo(cart.getSellerCode(), cart.getBuyer(), cart.getItemCode(), cart.getBatchName());

            Query query = this.getQuery(insertCart());
            query.setParameter("sellerCode", cart.getSellerCode());
            query.setParameter("buyerCode", cart.getBuyer());
            query.setParameter("smanCode", smanCode);
            query.setParameter("confirm", 0);
            query.setParameter("itemCode", cart.getItemCode());
            query.setParameter("itemQty", cart.getQty());
            query.setParameter("user", cart.getMobile());
            query.setParameter("itemName", jsonObject.get("c_item_name").getAsString());
            query.setParameter("mrp", cart.getMrp());
            query.setParameter("mfgCode", jsonObject.get("c_mfg_code").getAsString());
            query.setParameter("mfgName", jsonObject.get("c_mfg_name").getAsString());
            query.setParameter("transNo", 0);
            query.setParameter("sellerBuyer", cart.getSellerCode() + cart.getBuyer());
            query.setParameter("batch", cart.getBatchName());
            query.setParameter("expiryDay", jsonObject.get("n_expiry_days").getAsInt());
            query.setParameter("expiryDate", cart.getExpiryDate());
            query.setParameter("percentage", jsonObject.get("n_percentage").getAsInt());
            query.setParameter("revMrp", jsonObject.get("n_rev_mrp").getAsDouble());
            query.setParameter("qtyPer", jsonObject.get("n_qty_per_box").getAsInt());
            query.setParameter("pack", cart.getPackSize());
            query.setParameter("looseQty", cart.getLooseQty());
            query.setParameter("scheme", cart.getScheme());
            query.setParameter("rowId", 0);
            query.executeUpdate();

        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteExpiryCart(DeleteExpiry cart) throws RecordNotFoundException {

        String sql = "delete from lc_temp_expiry_order where c_c2code = :sellerCode " +
                "and c_cust_code = :buyerCode and c_item_code= :itemCode " +
                "and c_batch_no= :batch and d_expiry_date= :expiry " +
                "and n_mrp_value= :mrp and n_confirm = '0'";
        Query query = this.getQuery(sql);
        query.setParameter("sellerCode", cart.getSellerCode());
        query.setParameter("buyerCode", cart.getBuyer());
        query.setParameter("itemCode", cart.getItemCode());
        query.setParameter("batch", cart.getBatchName());
        query.setParameter("expiry", cart.getExpiryDate());
        query.setParameter("mrp", cart.getMrp());
        query.executeUpdate();
    }

    @Override
    public JsonArray getExpireCart(String sellerCode, LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException {
        String buyerCode = getBuyerCode(sellerCode, lcHeaderBO);
        String sql = "SELECT  " +
                "                                        fl.c_item_code itemCode, " +
                "                                        fl.c_item_name itemName, " +
                "                                        fl.n_qty qty, " +
                "                                        c_userid userId, " +
                "                                        fl.c_batch_no batchNo, " +
                "                                        act.c_sman_code smanCode, " +
                "                                        fl.n_expiry_days expdays, " +
                "                                        fl.d_expiry_date expdate, " +
                "                                        fl.n_percentage percentage, " +
                "                                        fl.n_mrp_value mrpValue, " +
                "                                        fl.c_mfac_code mfaccode, " +
                "                                        fl.c_mfac_name mfacName, " +
                "                                        fl.n_revised_mrp_value revisedmrp, " +
                "                                        fl.c_c2code as sellercode, " +
                "                                        fl.c_cust_code as custCode, " +
                "                                        fl.c_pack as pack, " +
                "                                        fl.n_qty_per_box as qtyPerBox, " +
                "                                        fl.n_loose_qty as looseqty, " +
                "                                        fl.c_scheme scheme " +
                "                                    FROM " +
                "                                        lc_temp_expiry_order fl " +
                "                                            LEFT JOIN " +
                "                                        cust_act_mst act ON fl.c_c2code = act.c_c2code " +
                "                                            AND act.c_code = fl.c_cust_code " +
                "                                    WHERE " +
                "                                        (fl.n_qty <> 0 OR fl.n_loose_qty <> 0) " +
                "                                            AND fl.c_cust_code = :buyerCode " +
                "                                            AND fl.c_c2code = :sellerCode  " +
                "                                            AND fl.n_confirm = 0 " +
                "                                            AND fl.n_transaction_ref_no = 0 ORDER BY fl.d_order_date ASC ";
        Query query = this.getQuery(sql);
        query.setParameter("sellerCode", sellerCode);
        query.setParameter("buyerCode", buyerCode);
        List<Object[]> result = this.getResultList(query, pageBO.getPage(), pageBO.getLimit());
        if (result.size() == 0) {
            throw new RecordNotFoundException("No Records..!");
        }
        JsonArray array = new JsonArray();
        for (Object[] objects : result) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_seller_code", helper.getString(objects[13]));
            jsonObject.addProperty("c_buyer_code", helper.getString(objects[14]));
            jsonObject.addProperty("c_item_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_item_name", helper.getString(objects[1]));
            jsonObject.addProperty("n_expiry_days", helper.getString(objects[6]));
            jsonObject.addProperty("d_expiry_date", helper.getString(objects[7]));
            jsonObject.addProperty("c_mfg_name", helper.getString(objects[11]));
            jsonObject.addProperty("c_mfg_code", helper.getString(objects[10]));
            jsonObject.addProperty("n_percentage", helper.getString(objects[8]));
            jsonObject.addProperty("c_batch_name", helper.getString(objects[4]));
            jsonObject.addProperty("n_mrp", helper.getString(objects[9]));
            jsonObject.addProperty("n_rev_mrp", helper.getString(objects[12]));
            jsonObject.addProperty("n_qty_per_box", helper.getString(objects[16]));
            jsonObject.addProperty("c_pack_size", helper.getString(objects[15]));
            jsonObject.addProperty("n_scheme", helper.getString(objects[18]));
            jsonObject.addProperty("n_loose_qty", helper.getString(objects[17]));
            jsonObject.addProperty("n_qty", helper.getString(objects[2]));
            jsonObject.addProperty("c_sman_code", helper.getString(objects[5]));
            array.add(jsonObject);

        }
        return array;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public JsonArray confirmCart(String sellerCode, LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException {

        String buyerCode = getBuyerCode(sellerCode, lcHeaderBO);

        String sql = " SELECT  " +
                "                                        fl.c_item_code itemCode, " +
                "                                        fl.n_qty qty, " +
                "                                        c_userid userId, " +
                "                                        fl.c_batch_no batchNo, " +
                "                                        fl.c_sman_code smanCode, " +
                "                                        fl.n_expiry_days expdays, " +
                "                                        fl.d_expiry_date expdate, " +
                "                                        fl.n_percentage percentage, " +
                "                                        fl.n_mrp_value mrpValue, " +
                "                                        fl.c_mfac_code mfaccode, " +
                "                                        fl.c_mfac_name mfacName, " +
                "                                        fl.n_revised_mrp_value revisedmrp, " +
                "                                        fl.n_loose_qty looseqty, " +
                "                                        fl.c_pack pack, " +
                "                                        fl.n_qty_per_box qtyPerBox, " +
                "                                        fl.c_scheme scheme " +
                "                                    FROM " +
                "                                        lc_temp_expiry_order fl " +
                "                                    WHERE " +
                "                                        (fl.n_qty <> 0 OR fl.n_loose_qty <> 0) " +
                "                                            AND fl.c_cust_code =  :buyerCode" +
                "                                            AND fl.c_c2code = :sellerCode  " +
                "                                            AND fl.n_confirm = 0 " +
                "                                            AND fl.n_transaction_ref_no = 0 ";

        Query query = this.getQuery(sql);
        query.setParameter("sellerCode", sellerCode);
        query.setParameter("buyerCode", buyerCode);
        List<Object[]> result = this.getResultList(query, pageBO.getPage(), pageBO.getLimit());
        if (result.size() == 0) {
            throw new RecordNotFoundException("No Records..!");
        }
        JsonArray array = new JsonArray();
        for (Object[] objects : result) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("c_item_code", helper.getString(objects[0]));
            jsonObject.addProperty("n_qty", helper.getString(objects[1]));
            jsonObject.addProperty("user", helper.getString(objects[2]));
            jsonObject.addProperty("c_batch_name", helper.getString(objects[3]));
            jsonObject.addProperty("c_sman_code", helper.getString(objects[4]));
            jsonObject.addProperty("n_expiry_days", helper.getString(objects[5]));
            jsonObject.addProperty("d_expiry_date", helper.getString(objects[6]));
            jsonObject.addProperty("n_percentage", helper.getString(objects[7]));
            jsonObject.addProperty("n_mrp", helper.getString(objects[8]));
            jsonObject.addProperty("c_mfg_code", helper.getString(objects[9]));
            jsonObject.addProperty("c_mfg_name", helper.getString(objects[10]));
            jsonObject.addProperty("n_rev_mrp", helper.getString(objects[11]));
            jsonObject.addProperty("n_loose_qty", helper.getString(objects[12]));
            jsonObject.addProperty("c_pack_size", helper.getString(objects[13]));
            jsonObject.addProperty("n_qty_per_box", helper.getString(objects[14]));
            jsonObject.addProperty("n_scheme", helper.getString(objects[15]));
            array.add(jsonObject);

        }
        confirmCart(array, sellerCode, buyerCode);
        return array;
    }

    @Override
    public JsonArray getExpireOrders(ExpiryOrderFilterBo filterBo, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {

        String sellerBuyer = getBuyerSeller(filterBo.getSellerCodes(), lcHeaderBO);
       // String sellerBuyer = "'001003DM4619'";
        String sql = "SELECT  " +
                "                            ord.d_upload_datetime AS orderDate, " +
                "                            ord.n_srno AS orderNo, " +
                "                            ord.c_cust_code custCode, " +
                "                            ord.c_c2code AS sellercode, " +
                "                            ord.n_order_status, " +
                "                            ord.c_created_user mobileNo, " +
                "                            tm.n_srno transNo, " +
                "                            ord.c_upload_ip ip, " +
                "                            lsc.c_supp_chem, " +
                "                            mst.c_name  SellerName, " +
                "                            act.c_name  BuyerName, " +
                "                            det.n_cancel_flag cancelflag, " +
                "                            det.n_sman_confirm_flag salesmanflag, " +
                "                            det.n_sman_confirm_qty salesmanconfirmqty, " +
                "                            det.n_mrp mrp, " +
                "                            ord.n_amount as buyerordervalue, " +
                "                            tm.n_bill_amount as sellerordervalue, " +
                "                            tm.n_salesman_bill_amount as salesmanordervalue " +
                "                             " +
                "                        FROM " +
                "                             cust_expiry_order_mst ord " +
                "                                LEFT JOIN " +
                "                            cust_act_mst act ON ord.c_c2code = act.c_c2code " +
                "                                AND ord.c_cust_code = act.c_code " +
                "                                LEFT JOIN " +
                "                            cust_expiry_transaction_mst tm ON ord.c_c2code = tm.c_c2code " +
                "                                AND ord.n_srno = tm.n_ord_srno " +
                "                                 LEFT JOIN " +
                "                            cust_expiry_transaction_det det ON tm.n_srno = det.n_tran_mst_srno   " +
                "                              LEFT JOIN  " +
                "                               lc_c2code_mst mst ON  mst.c_code=ord.c_c2code " +
                "                                left join " +
                "    lc_supp_chem_comb lsc ON ord.c_c2code = lsc.c_c2code and ord.c_cust_code=lsc.c_chem_code " +
                "                                " +
                "                                " +
                "                        WHERE " +
                "                             lsc.c_supp_chem IN (" + sellerBuyer + ")  ";
        if (filterBo.getOrderStatus().size() > 0) {
            String status = "";
            for (int sel : filterBo.getOrderStatus()) {
                status = status + "'" + sel + "',";
            }
            status = status.substring(0, status.length() - 1);

            sql += "AND ord.n_order_status IN ("+status+ ") ";
        }
        if (!helper.isEmpty(filterBo.getFromDate())) {
            sql += " AND ord.d_upload_datetime >= '"+filterBo.getFromDate()+"' ";
        }
        if (!helper.isEmpty(filterBo.getToDate())) {
            sql += "AND ord.d_upload_datetime <=' "+filterBo.getToDate()+"' ";
        }
        if (!helper.isEmpty(filterBo.getSearchKey())) {
            sql += "";
        }
        sql += "                                GROUP BY ord.n_srno " +
                "                               order by ord.d_upload_datetime desc ";

        Query query = this.getQuery(sql);
        List<Object[]> result = this.getResultList(query, filterBo.getPage(), filterBo.getLimit());
        if (result.size() == 0) {
            throw new RecordNotFoundException("No Records..!");
        }
        JsonArray array = new JsonArray();
        for (Object[] objects : result) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("d_ord_date", helper.getString(objects[0]));
            jsonObject.addProperty("c_order_no", helper.getString(objects[1]));
            jsonObject.addProperty("c_buyer_code", helper.getString(objects[2]));
            jsonObject.addProperty("c_seller_code", helper.getString(objects[3]));
            jsonObject.addProperty("n_order_status", Integer.parseInt(helper.getString(objects[4])));
            jsonObject.addProperty("c_user_mobile", helper.getString(objects[5]));
            jsonObject.addProperty("c_trans_no", helper.getString(objects[6]));
            jsonObject.addProperty("c_upload_ip", helper.getString(objects[7]));
            jsonObject.addProperty("c_seller_buyer", helper.getString(objects[8]));
            jsonObject.addProperty("c_seller_name", helper.getString(objects[9]));
            jsonObject.addProperty("c_buyer_name", helper.getString(objects[10]));
            jsonObject.addProperty("n_cancel_flag", Integer.parseInt(helper.getString(objects[11])));
            jsonObject.addProperty("n_sales_flag", Integer.parseInt(helper.getString(objects[12])));
            jsonObject.addProperty("n_sales_qty", Integer.parseInt(helper.getString(objects[13])));
            jsonObject.addProperty("n_mrp", Double.parseDouble(helper.getString(objects[14])));
            jsonObject.addProperty("n_buyer_order_value", Double.parseDouble(helper.getString(objects[15])));
            jsonObject.addProperty("n_seller_order_value", Double.parseDouble(helper.getString(objects[16])));
            jsonObject.addProperty("n_sales_order_value", Double.parseDouble(helper.getString(objects[17])));
            array.add(jsonObject);
        }

        return array;
    }

    @Override
    public JsonObject getOrdersById(ExpireOrderIdBo orderIdBo, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {

        String status = "";
        for (int sel : orderIdBo.getCancelFlags()) {
            status = status + "'" + sel + "',";
        }
        status = status.substring(0, status.length() - 1);

        String sql = "SELECT  " +
                "                            TRIM(itm.c_code) c_item_code, " +
                "                            TRIM(itm.c_name) c_name, " +
                "                            itm.c_pack_code c_pack, " +
                "                            det.c_batch_no batchNo, " +
                "                            det.n_qty buyerqty, " +
                "                            det.n_mrp mrp, " +
                "                            det.n_expiry_days expDays, " +
                "                            DATE_FORMAT(det.d_expiry_date, '%Y/%m') expdate, " +
                "                            det.c_batch_no batch, " +
                "                            det.n_percentage, " +
                "                            det.n_confirm_qty sellerconfirmqty, " +
                "                            det.n_sman_confirm_qty salesmanconfirmqty, " +
                "                            det.n_cancel_flag cancelflag, " +
                "                            TRIM(COALESCE(mft.c_name, '')) mfac_name, " +
                "                            det.n_loose_qty looseqty, " +
                "                            det.c_pack pack, " +
                "                            det.n_qty_per_box qtyPerBox, " +
                "                            det.n_loose_confirm_qty sellerlooseconfirmqty, " +
                "                            det.n_loose_sman_confirm_qty salesmanlooseconfirmqty, " +
                "                            mst.d_created_datetime orderDate, " +
                "                            det.n_revised_mrp_value revisedmrp, " +
                "                            det.c_scheme scheme, " +
                "                            actmst.c_code buyerCode, " +
                "                            actmst.c_name buyerName, " +
                "                            CONCAT(actmst.c_add_1, " +
                "                                    actmst.c_add_2, " +
                "                                    actmst.c_add_3) buyerAdd, " +
                "                            actmst.c_city buyerCity, " +
                "                            actmst.c_pin buyerPin, " +
                "                            actmst.c_phone_1 phone1, " +
                "                            actmst.c_phone_2 phone2, " +
                "                            actmst.c_mobile mobile " +
                "                        FROM " +
                "                            cust_expiry_transaction_det det " +
                "                                INNER JOIN " +
                "                            cust_item_mst itm ON det.c_item_code = itm.c_code " +
                "                                AND itm.c_c2code = '"+orderIdBo.getSellerCode()+"'  " +
                "                                INNER JOIN " +
                "                            cust_expiry_transaction_mst mst ON mst.n_srno = det.n_tran_mst_srno " +
                "                                AND mst.c_c2code = det.c_c2code " +
                "                                LEFT JOIN " +
                "                            cust_pack_mst ip ON itm.c_c2code = ip.c_c2code " +
                "                                AND itm.c_pack_code = ip.c_code " +
                "                                LEFT JOIN " +
                "                            cust_mfac_mst mft ON itm.c_c2code = mft.c_c2code " +
                "                                AND itm.c_mfac_code = mft.c_code " +
                "                                LEFT JOIN " +
                "                            cust_act_mst actmst ON itm.c_c2code = actmst.c_c2code " +
                "                                AND mst.c_cust_code = actmst.c_code " +
                "                        WHERE " +
                "                            mst.n_ord_srno = '"+orderIdBo.getOrderId()+"' " +
                "                                AND det.n_cancel_flag IN ("+status+") " +
                "                        ORDER BY itm.c_name ";

        Query query = this.getQuery(sql);
        List<Object[]> result = this.getResultList(query);
        if (result.size() == 0) {
            throw new RecordNotFoundException("No Records..!");
        }
        JsonObject jsonObject = new JsonObject();
        for (Object[] objects : result) {

            jsonObject.addProperty("c_item_name", helper.getString(objects[0]));
            jsonObject.addProperty("c_item_code", helper.getString(objects[1]));
            jsonObject.addProperty("c_pack_code", helper.getString(objects[2]));
            jsonObject.addProperty("c_batch_name", helper.getString(objects[3]));
            jsonObject.addProperty("n_buyer_qty", Integer.parseInt(helper.getString(objects[4])));
            jsonObject.addProperty("n_mrp", Double.parseDouble(helper.getString(objects[5])));
            jsonObject.addProperty("n_expire_days", Integer.parseInt(helper.getString(objects[6])));
            jsonObject.addProperty("d_expire_date", helper.getString(objects[7]));

            jsonObject.addProperty("n_percentage", Double.parseDouble(helper.getString(objects[9])));
            jsonObject.addProperty("n_seller_confirm_qty", Integer.parseInt(helper.getString(objects[10])));
            jsonObject.addProperty("n_sales_confirm_qty", Integer.parseInt(helper.getString(objects[11])));
            jsonObject.addProperty("n_cancel_flag", Integer.parseInt(helper.getString(objects[12])));
            jsonObject.addProperty("c_mfg_name", helper.getString(objects[13]));
            jsonObject.addProperty("n_loose_qty", Integer.parseInt(helper.getString(objects[14])));
            jsonObject.addProperty("c_pack_name", helper.getString(objects[15]));
            jsonObject.addProperty("n_qty_per_box", Integer.parseInt(helper.getString(objects[16])));
            jsonObject.addProperty("n_seller_loose_confirm_qty", Integer.parseInt(helper.getString(objects[17])));
            jsonObject.addProperty("n_sales_loose_confirm_qty", Integer.parseInt(helper.getString(objects[18])));
            jsonObject.addProperty("d_order_date", helper.getString(objects[19]));
            jsonObject.addProperty("n_rev_mrp", Double.parseDouble(helper.getString(objects[20])));
            jsonObject.addProperty("c_scheme", helper.getString(objects[21]));
            jsonObject.addProperty("c_buyer_code", helper.getString(objects[22]));
            jsonObject.addProperty("c_buyer_name", helper.getString(objects[23]));
            jsonObject.addProperty("c_buyer_add", helper.getString(objects[24]));
            jsonObject.addProperty("c_buyer_city", helper.getString(objects[25]));
            jsonObject.addProperty("c_buyer_pin", helper.getString(objects[26]));
            jsonObject.addProperty("c_buyer_phone1", helper.getString(objects[27]));
            jsonObject.addProperty("c_buyer_phone2", helper.getString(objects[28]));
            jsonObject.addProperty("c_buyer_mobile", helper.getString(objects[29]));

        }
        return jsonObject;
    }

    @Override
    public JsonObject buyerSellerInfo(String sellerCode, String buyerCode) throws RecordNotFoundException {
        
        String sql = " select mst.c_code sellerCode,cap_first(mst.c_name) sellerName, cap_first(mst.c_add_1) sellerAdd1, cap_first(mst.c_add_2)  " +
                "sellerAdd2, cap_first(mst.c_add_3) sellerAdd3, cap_first(mst.c_city) sellerCity, mst.c_pin sellerPin,  " +
                "mst.c_phone_1 sellerPhone1, mst.c_mobile sellerMobile,mst.c_drug_licence_no_1 sellerDlNo1, mst.c_drug_licence_no_2  " +
                "sellerDlNo2,mst.c_st_no sellerStNo, cst.c_code buyerCode,  " +
                "cap_first(cst.c_name) buyerName,cap_first(cst.c_add_1) buyerAdd1, cap_first(cst.c_add_2) buyerAdd2,  " +
                "cap_first(cst.c_add_3) buyerAdd3, cap_first(cst.c_city) buyerCity,cst.c_phone_1 buyerPhone1, cst.c_mobile buyerMobile,  " +
                "cst.c_drug_licence_no_1 buyerDlNo1, cst.c_drug_licence_no_2 buyerDlNo2, cst.c_sman_code smanCode, cst.c_pin buyerPin,  " +
                "cst.c_st_no buyerStNo,sman.c_name smanName,  " +
                "cst.c_contact_person contact,'' route,coalesce(im.c_from_gst_no,'') sellerGSTNo,coalesce(im.c_to_gst_no,'') buyerGSTNo  " +
                "from lc_c2code_mst mst left join cust_act_mst cst on cst.c_c2code = mst.c_code  " +
                "left join cust_sman_mst sman on sman.c_code = cst.c_sman_code and sman.c_c2code = cst.c_c2code  " +
                "left join lc_supp_chem_comb cb on cb.c_c2code = cst.c_c2code and cst.c_code = cb.c_chem_code  " +
                "left join cust_inv_mst im on cst.c_c2code = im.c_c2code and cst.c_code = im.c_cust_code   " +
                "where cb.c_supp_chem in ('"+sellerCode+buyerCode+"') group by sellerCode,buyerCode " ;

        Query query = this.getQuery(sql);
        List<Object[]> result = this.getResultList(query);
        if (result.size() == 0) {
            throw new RecordNotFoundException("No Records..!");
        }
        JsonObject jsonObject = new JsonObject();
        for (Object[] objects : result) {

            String sAddress = helper.getString(objects[2])+","+
                                helper.getString(objects[3])+","+
                                helper.getString(objects[4])+ ","+
                                helper.getString(objects[5]);
            String bAdd = helper.getString(objects[14])+","+
                    helper.getString(objects[15])+","+
                    helper.getString(objects[16])+ ","+
                    helper.getString(objects[17]);

            jsonObject.addProperty("sellerName", helper.getString(objects[1]));
            jsonObject.addProperty("sellerAddress", sAddress);
            jsonObject.addProperty("sellerPhone", helper.getString(objects[7]));
            jsonObject.addProperty("sellerDlNo", helper.getString(objects[9]));
            jsonObject.addProperty("sellerGst", helper.getString(objects[28]));
            jsonObject.addProperty("buyerName", helper.getString(objects[13]));
            jsonObject.addProperty("buyerAddress", bAdd);
            jsonObject.addProperty("buyerPhone", helper.getString(objects[18]));
            jsonObject.addProperty("buyerDlNo", helper.getString(objects[20]));
            jsonObject.addProperty("buyerGst", helper.getString(objects[29]));
        }

        return jsonObject;
    }

    @Override
    public long getExpiryOrdersCount(ExpiryOrderFilterBo filterBo, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {

        String sellerBuyer = getBuyerSeller(filterBo.getSellerCodes(), lcHeaderBO);
        // String sellerBuyer = "'001003DM4619'";
        String sql = "SELECT  " +
                "                            ord.d_upload_datetime AS orderDate, " +
                "                            ord.n_srno AS orderNo, " +
                "                            ord.c_cust_code custCode, " +
                "                            ord.c_c2code AS sellercode, " +
                "                            ord.n_order_status, " +
                "                            ord.c_created_user mobileNo, " +
                "                            tm.n_srno transNo, " +
                "                            ord.c_upload_ip ip, " +
                "                            lsc.c_supp_chem, " +
                "                            mst.c_name  SellerName, " +
                "                            act.c_name  BuyerName, " +
                "                            det.n_cancel_flag cancelflag, " +
                "                            det.n_sman_confirm_flag salesmanflag, " +
                "                            det.n_sman_confirm_qty salesmanconfirmqty, " +
                "                            det.n_mrp mrp, " +
                "                            ord.n_amount as buyerordervalue, " +
                "                            tm.n_bill_amount as sellerordervalue, " +
                "                            tm.n_salesman_bill_amount as salesmanordervalue " +
                "                             " +
                "                        FROM " +
                "                             cust_expiry_order_mst ord " +
                "                                LEFT JOIN " +
                "                            cust_act_mst act ON ord.c_c2code = act.c_c2code " +
                "                                AND ord.c_cust_code = act.c_code " +
                "                                LEFT JOIN " +
                "                            cust_expiry_transaction_mst tm ON ord.c_c2code = tm.c_c2code " +
                "                                AND ord.n_srno = tm.n_ord_srno " +
                "                                 LEFT JOIN " +
                "                            cust_expiry_transaction_det det ON tm.n_srno = det.n_tran_mst_srno   " +
                "                              LEFT JOIN  " +
                "                               lc_c2code_mst mst ON  mst.c_code=ord.c_c2code " +
                "                                left join " +
                "    lc_supp_chem_comb lsc ON ord.c_c2code = lsc.c_c2code and ord.c_cust_code=lsc.c_chem_code " +
                "                                " +
                "                                " +
                "                        WHERE " +
                "                             lsc.c_supp_chem IN (" + sellerBuyer + ")  ";
        if (filterBo.getOrderStatus().size() > 0) {
            String status = "";
            for (int sel : filterBo.getOrderStatus()) {
                status = status + "'" + sel + "',";
            }
            status = status.substring(0, status.length() - 1);

            sql += "AND ord.n_order_status IN ("+status+ ") ";
        }
        if (!helper.isEmpty(filterBo.getFromDate())) {
            sql += " AND ord.d_upload_datetime >= '"+filterBo.getFromDate()+"' ";
        }
        if (!helper.isEmpty(filterBo.getToDate())) {
            sql += "AND ord.d_upload_datetime <=' "+filterBo.getToDate()+"' ";
        }
        if (!helper.isEmpty(filterBo.getSearchKey())) {
            sql += "";
        }
        sql += "                                GROUP BY ord.n_srno " +
                "                               order by ord.d_upload_datetime desc ";

        Query query = this.getQuery(sql);
        List<Object[]> result = this.getResultList(query);
        if (result.size() == 0) {
            throw new RecordNotFoundException("No Records..!");
        }

        return result.size();
    }

    private String insertCart() {
        return "INSERT INTO lc_temp_expiry_order(c_c2code,c_cust_code,c_sman_code,n_confirm,c_item_code, " +
                "                            n_qty,c_userid,c_item_name,n_mrp_value,c_mfac_code,c_mfac_name,n_transaction_ref_no,c_seller_buyer_code,c_batch_no,n_expiry_days,d_expiry_date,n_percentage,n_revised_mrp_value,n_qty_per_box,c_pack,n_loose_qty,c_scheme, n_rowid) " +
                "                            VALUES(:sellerCode, :buyerCode, :smanCode, :confirm, :itemCode, :itemQty, :user, " +
                "                           :itemName, :mrp, :mfgCode, :mfgName, :transNo, :sellerBuyer, :batch, :expiryDay, :expiryDate, " +
                "                           :percentage , :revMrp, :qtyPer, :pack, :looseQty , :scheme, :rowId) ON duplicate key update " +
                "                            c_sman_code=values(c_sman_code),n_qty=values(n_qty), " +
                "                            c_userid=values(c_userid),c_item_name=values(c_item_name), " +
                "                           n_mrp_value=values(n_mrp_value), " +
                "                            c_mfac_code=values(c_mfac_code),c_mfac_name=values(c_mfac_name),c_seller_buyer_code=values(c_seller_buyer_code),c_batch_no=values(c_batch_no),n_expiry_days=values(n_expiry_days), " +
                "                            d_expiry_date=values(d_expiry_date),n_percentage=values(n_percentage),n_revised_mrp_value=values(n_revised_mrp_value),n_qty_per_box=values(n_qty_per_box),c_pack=values(c_pack),n_loose_qty=values(n_loose_qty),c_scheme=values(c_scheme)";
    }

    private String getCustBatch(String seller, String itemCode, String batch) {
        String batNo = "";
        String sql = "select c_batch_no from cust_item_stock stk  " +
                "where stk.c_c2code='" + seller + "' and stk.c_item_code='" + itemCode + "' and stk.c_batch_no='" + batch + "'";
        Query query = this.getQuery(sql);
        Object out = this.getSingleResultNull(query);
        if (out != null) {
            batNo = out.toString();

        }
        return batNo;
    }

    private String getCustExpBatch(String seller, String itemCode, String batch) {
        String batNo = "";
        String sql = "select c_batch_no from cust_exp_item_stock stk  " +
                "where stk.c_c2code='" + seller + "' and stk.c_item_code='" + itemCode + "' and stk.c_batch_no='" + batch + "'";
        Query query = this.getQuery(sql);
        Object out = this.getSingleResultNull(query);
        if (out != null) {
            batNo = out.toString();

        }
        return batNo;
    }

    private String getBuyerCode(String sellerCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {

        String buyer = "";
        String sql = "select lsbp.c_buyer_code from lc_seller_buyer_priority lsbp  " +
                "where lsbp.n_firm_id = '" + lcHeaderBO.getFirmId() + "' and lsbp.c_seller_code = '" + sellerCode + "'";
        Query query = this.getQuery(sql);
        Object out = this.getSingleResultNull(query);
        if (out != null) {
            buyer = out.toString();

        } else
            throw new RecordNotFoundException("No buyer Code Found..!");

        return buyer;
    }


    private String getBatchQuery(BatchItemBo itemBo) {
        String search = "%" + itemBo.getSearchTerm() + "%";
        return "SELECT  stock.bachNo, stock.expno, stock.mrp, stock.ltime,stock.expdate  FROM ( " +
                "                    SELECT " +
                "                        c_batch_no AS bachNo, DATE_FORMAT(d_exp_dt, '%Y/%m') AS expno, n_mrp AS mrp , " +
                "                        t_ltime as ltime,d_exp_dt as expdate, " +
                "                        DATEDIFF(DATE_FORMAT(now(), '%Y-%m-%d'),stk.d_exp_dt) expdays, " +
                "                        slab.n_non_expiry_days_3 AS maxslabdays " +
                "                    FROM " +
                "                        cust_item_stock stk " +
                "                        join cust_system_parameter slab on slab.c_c2code=stk.c_c2code " +
                "                    WHERE " +
                "                        stk.c_c2code = '" + itemBo.getSellerCode() + "' " +
                "                            AND stk.c_item_code = '" + itemBo.getItemCode() + "' AND stk.c_batch_no like '" + search + "'   " +
                "                    UNION ALL " +
                " " +
                "                    select " +
                "                        c_batch_no AS bachNo, DATE_FORMAT(d_exp_dt, '%Y/%m') AS expno, n_mrp AS mrp, t_ltime as ltime,d_exp_dt as expdate, " +
                "                        DATEDIFF(DATE_FORMAT(now(), '%Y-%m-%d'),exp.d_exp_dt) expdays,slab.n_non_expiry_days_3 AS maxslabdays " +
                "                    FROM " +
                "                        cust_exp_item_stock exp " +
                "                        join cust_system_parameter slab on slab.c_c2code=exp.c_c2code " +
                "                    WHERE " +
                "                        exp.c_c2code = '" + itemBo.getSellerCode() + "' " +
                "                            AND exp.c_item_code = '" + itemBo.getItemCode() + "' AND exp.c_batch_no like '" + search + "'  ) as stock  " +
                "                            where stock.expdays<=stock.maxslabdays " +
                "                            ORDER BY stock.expdate desc";
    }

    private String getExpireItem(String sellerCode, String buyerCode, String search, String itemCode) {
        return "SELECT  " +
                "    d.seller_code, " +
                "    d.buyer_code, " +
                "    d.itemCode, " +
                "    d.itemName, " +
                "    d.expired_days AS expiry_days, " +
                "    d.d_exp_dt expiry_date, " +
                "    d.mfacName, " +
                "    d.mfac_code, " +
                "    d.percentage, " +
                "    d.c_batch_no, " +
                "    ROUND(d.mrp_value, 2) mrp_value, " +
                "    ROUND((d.mrp_value * d.percentage) / 100, 2) revised_mrp_value, " +
                "    d.qtyPerBox, " +
                "    d.pack, " +
                "    d.scheme " +
                "FROM " +
                "    (SELECT  " +
                "        t.seller_code, " +
                "            t.buyer_code, " +
                "            t.expired_days, " +
                "            t.itemCode, " +
                "            t.itemName, " +
                "            t.mfacName, " +
                "            t.mfac_code, " +
                "            CASE " +
                "                WHEN t.expired_days <= n_non_expiry_days THEN ROUND(n_credit_percentage_4) " +
                "                WHEN t.expired_days <= n_non_expiry_days_2 THEN ROUND(n_credit_percentage_5) " +
                "                WHEN t.expired_days <= n_non_expiry_days_3 THEN ROUND(n_credit_percentage_6) " +
                "                ELSE 0 " +
                "            END percentage, " +
                "            t.c_batch_no, " +
                "            t.mrp_value, " +
                "            t.d_exp_dt, " +
                "            t.minSaleQty, " +
                "            t.qtyPerBox, " +
                "            t.pack, " +
                "            t.scheme " +
                "    FROM " +
                "        (SELECT  " +
                "        lc.c_code seller_code, " +
                "            ac.c_code buyer_code, " +
                "            ac.c_name buyer_name, " +
                "            DATEDIFF(DATE_FORMAT(NOW(), '%Y-%m-%d'), stk.d_exp_dt) expired_days, " +
                "            stk.c_batch_no, " +
                "            stk.n_mrp mrp_value, " +
                "            slab.n_non_expiry_days, " +
                "            slab.n_non_expiry_days_2, " +
                "            slab.n_non_expiry_days_3, " +
                "            slab.n_credit_percentage_4, " +
                "            slab.n_credit_percentage_5, " +
                "            slab.n_credit_percentage_6, " +
                "            stk.d_exp_dt expiry_date, " +
                "            stk.d_exp_dt, " +
                "            i.c_code itemCode, " +
                "            CAP_FIRST(i.c_name) itemName, " +
                "            CAP_FIRST(im.c_name) mfacName, " +
                "            im.c_code mfac_code, " +
                "            COALESCE(i.n_min_sale_qty, 0) minSaleQty, " +
                "            COALESCE(i.n_qty_per_box, 0) qtyPerBox, " +
                "            COALESCE(ip.c_name, i.c_pack_code) pack, " +
                "            CASE " +
                "                WHEN " +
                "                    UPPER(custcat.c_sch_category_code) = 'RETAIL' " +
                "                THEN " +
                "                    CONCAT(CASE " +
                "                        WHEN schmst.n_sch_qty_1 > 0 THEN CONCAT(schmst.n_sch_qty_1, ' + ', schmst.n_free_qty_1, ' , ') " +
                "                        ELSE '' " +
                "                    END, CASE " +
                "                        WHEN schmst.n_sch_qty_2 > 0 THEN CONCAT(schmst.n_sch_qty_2, ' + ', schmst.n_free_qty_2, ' , ') " +
                "                        ELSE '' " +
                "                    END, CASE " +
                "                        WHEN schmst.n_sch_qty_3 > 0 THEN CONCAT(schmst.n_sch_qty_3, ' + ', schmst.n_free_qty_3) " +
                "                        ELSE '' " +
                "                    END) " +
                "                ELSE CONCAT(CASE " +
                "                    WHEN schdet.n_sch_qty_1 > 0 THEN CONCAT(schdet.n_sch_qty_1, ' + ', schdet.n_free_qty_1, ' , ') " +
                "                    ELSE '' " +
                "                END, CASE " +
                "                    WHEN schdet.n_sch_qty_2 > 0 THEN CONCAT(schdet.n_sch_qty_2, ' + ', schdet.n_free_qty_2, ' , ') " +
                "                    ELSE '' " +
                "                END, CASE " +
                "                    WHEN schdet.n_sch_qty_3 > 0 THEN CONCAT(schdet.n_sch_qty_3, ' + ', schdet.n_free_qty_3) " +
                "                    ELSE '' " +
                "                END) " +
                "            END scheme " +
                "    FROM " +
                "        lc_c2code_mst lc " +
                "    JOIN cust_act_mst ac ON ac.c_c2code = lc.c_code " +
                "        AND ac.c_code = '"+buyerCode+"' " +
                "    LEFT JOIN cust_item_mst i ON i.c_c2code = lc.c_code " +
                "    JOIN (SELECT  " +
                "        c_batch_no AS c_batch_no, " +
                "            d_exp_dt AS d_exp_dt, " +
                "            n_mrp AS n_mrp, " +
                "            c_c2code, " +
                "            c_item_code " +
                "    FROM " +
                "        cust_item_stock " +
                "    WHERE " +
                "        c_c2code = '"+sellerCode+"' " +
                "            AND c_item_code = '"+itemCode+"' " +
                "            AND c_batch_no = '"+search+"' " +
                "            UNION ALL SELECT  " +
                "        c_batch_no AS c_batch_no, " +
                "            d_exp_dt AS d_exp_dt, " +
                "            n_mrp AS n_mrp, " +
                "            c_c2code, " +
                "            c_item_code " +
                "    FROM " +
                "        cust_exp_item_stock " +
                "    WHERE " +
                "        c_c2code = '"+sellerCode+"' " +
                "            AND c_item_code = '"+itemCode+"' " +
                "            AND c_batch_no = '"+search+"' " +
                "           ) AS stk ON stk.c_c2code = i.c_c2code " +
                "        AND stk.c_item_code = i.c_code " +
                "    LEFT JOIN cust_mfac_mst im ON i.c_c2code = im.c_c2code " +
                "        AND i.c_mfac_code = im.c_code " +
                "    LEFT JOIN cust_mfac_mst cmf ON cmf.c_c2code = i.c_c2code " +
                "        AND cmf.c_code = i.c_mfac_code " +
                "    LEFT JOIN cust_category_mst custcat ON ac.c_c2code = custcat.c_c2code " +
                "        AND ac.c_cust_category_code = custcat.c_code " +
                "    LEFT JOIN cust_scheme_mst schmst ON stk.c_c2code = schmst.c_c2code " +
                "        AND stk.c_item_code = schmst.c_item_code " +
                "        AND stk.c_batch_no = schmst.c_batch_no " +
                "    LEFT JOIN cust_scheme_det schdet ON schdet.c_c2code = stk.c_c2code " +
                "        AND schdet.c_item_code = stk.c_item_code " +
                "        AND schdet.c_batch_no = stk.c_batch_no " +
                "        AND custcat.c_sch_category_code = schdet.c_category " +
                "    LEFT JOIN cust_pack_mst ip ON i.c_c2code = ip.c_c2code " +
                "        AND i.c_pack_code = ip.c_code " +
                "    LEFT JOIN lc_item_filter lif ON lif.c_c2code = i.c_c2code " +
                "        AND lif.n_cancel_flag = 0 " +
                "        AND CASE lif.n_item_type " +
                "        WHEN 2 THEN cmf.c_mfac_group_code = lif.c_item_type_code " +
                "        WHEN 3 THEN i.c_mfac_code = lif.c_item_type_code " +
                "        WHEN 4 THEN i.c_cat_code = lif.c_item_type_code " +
                "        WHEN 5 THEN i.c_cont_code = lif.c_item_type_code " +
                "        WHEN 6 THEN LEFT(i.c_name, LENGTH(lif.c_item_type_code)) = lif.c_item_type_code " +
                "        ELSE i.c_code = lif.c_item_type_code " +
                "    END " +
                "        AND CASE lif.n_cust_type " +
                "        WHEN 2 THEN ac.c_sman_code = lif.c_cust_type_code " +
                "        WHEN 3 THEN ac.c_route_code = lif.c_cust_type_code " +
                "        WHEN 4 THEN ac.c_cust_category_code = lif.c_cust_type_code " +
                "        WHEN 5 THEN ac.c_dman_code = lif.c_cust_type_code " +
                "        WHEN 6 THEN ac.c_area_code = lif.c_cust_type_code " +
                "        ELSE ac.c_code = lif.c_cust_type_code " +
                "    END " +
                "    JOIN cust_system_parameter slab ON slab.c_c2code = lc.c_code " +
                "    WHERE " +
                "        i.c_c2code = '"+sellerCode+"' " +
                "            AND lif.n_item_type IS NULL " +
                "            AND lif.n_cust_type IS NULL " +
                "            AND i.c_code = '"+itemCode+"' " +
                "    GROUP BY i.c_c2code , i.c_code) t) d " +
                "ORDER BY d.itemname ";
    }

    private int getChemistStatus(String seller, String buyerCode) {

        String sql = "select COALESCE(n_active,0) as n_active from live_retailer_userinfo " +
                "where c_chemist_code ='" + buyerCode + "' and c_distributor_code ='" + seller + "'";

        Query query = this.getQuery(sql);
        Object obj = this.getSingleResult(query);
        return Integer.parseInt(obj.toString());
    }

    private String getSmanCode(String seller, String buyerCode) throws RecordNotFoundException {

        String sCode = "";
        String sql = "select c_sman_code from cust_act_mst " +
                "where c_code='" + buyerCode + "' and c_c2code='" + seller + "' limit 1";
        Query query = this.getQuery(sql);
        Object obj = this.getSingleResultNull(query);
        if (obj != null)
            sCode = obj.toString();
        else
            throw new RecordNotFoundException("SmanCode Not Found..!");
        return sCode;
    }

    private JsonObject getItemInfo(String sellerCode, String buyerCode, String itemCode, String batch) throws RecordNotFoundException {

        String sql = "SELECT  " +
                "    d.seller_code, " +
                "    d.buyer_code, " +
                "    d.itemCode, " +
                "    d.itemName, " +
                "    d.expired_days AS expiry_days, " +
                "    d.d_exp_dt expiry_date, " +
                "    d.mfacName, " +
                "    d.mfac_code, " +
                "    d.percentage, " +
                "    d.c_batch_no, " +
                "    ROUND(d.mrp_value, 2) mrp_value, " +
                "    ROUND((d.mrp_value * d.percentage) / 100, 2) revised_mrp_value, " +
                "    d.qtyPerBox, " +
                "    d.pack, " +
                "    d.scheme " +
                "FROM " +
                "    (SELECT  " +
                "        t.seller_code, " +
                "            t.buyer_code, " +
                "            t.expired_days, " +
                "            t.itemCode, " +
                "            t.itemName, " +
                "            t.mfacName, " +
                "            t.mfac_code, " +
                "            CASE " +
                "                WHEN t.expired_days <= n_non_expiry_days THEN ROUND(n_credit_percentage_4) " +
                "                WHEN t.expired_days <= n_non_expiry_days_2 THEN ROUND(n_credit_percentage_5) " +
                "                WHEN t.expired_days <= n_non_expiry_days_3 THEN ROUND(n_credit_percentage_6) " +
                "                ELSE 0 " +
                "            END percentage, " +
                "            t.c_batch_no, " +
                "            t.mrp_value, " +
                "            t.d_exp_dt, " +
                "            t.minSaleQty, " +
                "            t.qtyPerBox, " +
                "            t.pack, " +
                "            t.scheme " +
                "    FROM " +
                "        (SELECT  " +
                "        lc.c_code seller_code, " +
                "            ac.c_code buyer_code, " +
                "            ac.c_name buyer_name, " +
                "            DATEDIFF(DATE_FORMAT(NOW(), '%Y-%m-%d'), stk.d_exp_dt) expired_days, " +
                "            stk.c_batch_no, " +
                "            stk.n_mrp mrp_value, " +
                "            slab.n_non_expiry_days, " +
                "            slab.n_non_expiry_days_2, " +
                "            slab.n_non_expiry_days_3, " +
                "            slab.n_credit_percentage_4, " +
                "            slab.n_credit_percentage_5, " +
                "            slab.n_credit_percentage_6, " +
                "            stk.d_exp_dt expiry_date, " +
                "            stk.d_exp_dt, " +
                "            i.c_code itemCode, " +
                "            CAP_FIRST(i.c_name) itemName, " +
                "            CAP_FIRST(im.c_name) mfacName, " +
                "            im.c_code mfac_code, " +
                "            COALESCE(i.n_min_sale_qty, 0) minSaleQty, " +
                "            COALESCE(i.n_qty_per_box, 0) qtyPerBox, " +
                "            COALESCE(ip.c_name, i.c_pack_code) pack, " +
                "            CASE " +
                "                WHEN " +
                "                    UPPER(custcat.c_sch_category_code) = 'RETAIL' " +
                "                THEN " +
                "                    CONCAT(CASE " +
                "                        WHEN schmst.n_sch_qty_1 > 0 THEN CONCAT(schmst.n_sch_qty_1, ' + ', schmst.n_free_qty_1, ' , ') " +
                "                        ELSE '' " +
                "                    END, CASE " +
                "                        WHEN schmst.n_sch_qty_2 > 0 THEN CONCAT(schmst.n_sch_qty_2, ' + ', schmst.n_free_qty_2, ' , ') " +
                "                        ELSE '' " +
                "                    END, CASE " +
                "                        WHEN schmst.n_sch_qty_3 > 0 THEN CONCAT(schmst.n_sch_qty_3, ' + ', schmst.n_free_qty_3) " +
                "                        ELSE '' " +
                "                    END) " +
                "                ELSE CONCAT(CASE " +
                "                    WHEN schdet.n_sch_qty_1 > 0 THEN CONCAT(schdet.n_sch_qty_1, ' + ', schdet.n_free_qty_1, ' , ') " +
                "                    ELSE '' " +
                "                END, CASE " +
                "                    WHEN schdet.n_sch_qty_2 > 0 THEN CONCAT(schdet.n_sch_qty_2, ' + ', schdet.n_free_qty_2, ' , ') " +
                "                    ELSE '' " +
                "                END, CASE " +
                "                    WHEN schdet.n_sch_qty_3 > 0 THEN CONCAT(schdet.n_sch_qty_3, ' + ', schdet.n_free_qty_3) " +
                "                    ELSE '' " +
                "                END) " +
                "            END scheme " +
                "    FROM " +
                "        lc_c2code_mst lc " +
                "    JOIN cust_act_mst ac ON ac.c_c2code = lc.c_code " +
                "        AND ac.c_code = '"+buyerCode+"' " +
                "    LEFT JOIN cust_item_mst i ON i.c_c2code = lc.c_code " +
                "    JOIN (SELECT  " +
                "        c_batch_no AS c_batch_no, " +
                "            d_exp_dt AS d_exp_dt, " +
                "            n_mrp AS n_mrp, " +
                "            c_c2code, " +
                "            c_item_code " +
                "    FROM " +
                "        cust_item_stock " +
                "    WHERE " +
                "        c_c2code = '"+sellerCode+"' " +
                "            AND c_item_code = '"+itemCode+"' " +
                "            AND c_batch_no = '"+batch+"' UNION ALL SELECT  " +
                "        c_batch_no AS c_batch_no, " +
                "            d_exp_dt AS d_exp_dt, " +
                "            n_mrp AS n_mrp, " +
                "            c_c2code, " +
                "            c_item_code " +
                "    FROM " +
                "        cust_exp_item_stock " +
                "    WHERE " +
                "        c_c2code = '"+sellerCode +"' " +
                "            AND c_item_code = '"+itemCode+"' " +
                "            AND c_batch_no = '"+batch+"' " +
                "            ) AS stk ON stk.c_c2code = i.c_c2code " +
                "        AND stk.c_item_code = i.c_code " +
                "    LEFT JOIN cust_mfac_mst im ON i.c_c2code = im.c_c2code " +
                "        AND i.c_mfac_code = im.c_code " +
                "    LEFT JOIN cust_mfac_mst cmf ON cmf.c_c2code = i.c_c2code " +
                "        AND cmf.c_code = i.c_mfac_code " +
                "    LEFT JOIN cust_category_mst custcat ON ac.c_c2code = custcat.c_c2code " +
                "        AND ac.c_cust_category_code = custcat.c_code " +
                "    LEFT JOIN cust_scheme_mst schmst ON stk.c_c2code = schmst.c_c2code " +
                "        AND stk.c_item_code = schmst.c_item_code " +
                "        AND stk.c_batch_no = schmst.c_batch_no " +
                "    LEFT JOIN cust_scheme_det schdet ON schdet.c_c2code = stk.c_c2code " +
                "        AND schdet.c_item_code = stk.c_item_code " +
                "        AND schdet.c_batch_no = stk.c_batch_no " +
                "        AND custcat.c_sch_category_code = schdet.c_category " +
                "    LEFT JOIN cust_pack_mst ip ON i.c_c2code = ip.c_c2code " +
                "        AND i.c_pack_code = ip.c_code " +
                "    LEFT JOIN lc_item_filter lif ON lif.c_c2code = i.c_c2code " +
                "        AND lif.n_cancel_flag = 0 " +
                "        AND CASE lif.n_item_type " +
                "        WHEN 2 THEN cmf.c_mfac_group_code = lif.c_item_type_code " +
                "        WHEN 3 THEN i.c_mfac_code = lif.c_item_type_code " +
                "        WHEN 4 THEN i.c_cat_code = lif.c_item_type_code " +
                "        WHEN 5 THEN i.c_cont_code = lif.c_item_type_code " +
                "        WHEN 6 THEN LEFT(i.c_name, LENGTH(lif.c_item_type_code)) = lif.c_item_type_code " +
                "        ELSE i.c_code = lif.c_item_type_code " +
                "    END " +
                "        AND CASE lif.n_cust_type " +
                "        WHEN 2 THEN ac.c_sman_code = lif.c_cust_type_code " +
                "        WHEN 3 THEN ac.c_route_code = lif.c_cust_type_code " +
                "        WHEN 4 THEN ac.c_cust_category_code = lif.c_cust_type_code " +
                "        WHEN 5 THEN ac.c_dman_code = lif.c_cust_type_code " +
                "        WHEN 6 THEN ac.c_area_code = lif.c_cust_type_code " +
                "        ELSE ac.c_code = lif.c_cust_type_code " +
                "    END " +
                "    JOIN cust_system_parameter slab ON slab.c_c2code = lc.c_code " +
                "    WHERE " +
                "        i.c_c2code = '"+sellerCode+"' " +
                "            AND lif.n_item_type IS NULL " +
                "            AND lif.n_cust_type IS NULL " +
                "            AND i.c_code = '"+itemCode+"' " +
                "    GROUP BY i.c_c2code , i.c_code) t) d " +
                "ORDER BY d.itemname ";

        Query query = this.getQuery(sql);
        List<Object[]> result = this.getResultList(query);
        JsonObject object = new JsonObject();
        if (result.size() == 0)
            throw new RecordNotFoundException("Item info Not Found..!");
        for (Object[] objects : result) {
            object.addProperty("c_item_name", helper.getString(objects[3]));
            object.addProperty("n_expiry_days", Integer.parseInt(helper.getString(objects[4])));
            object.addProperty("c_mfg_name", helper.getString(objects[6]));
            object.addProperty("n_percentage", Integer.parseInt(helper.getString(objects[8])));
            object.addProperty("n_rev_mrp", Double.parseDouble(helper.getString(objects[11])));
            object.addProperty("n_qty_per_box", Integer.parseInt(helper.getString(objects[12])));
            object.addProperty("c_mfg_code", helper.getString(objects[7]));
        }
        return object;
    }

    private String getBuyerSeller(List<String> sellers, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
            String seller = "";
        for (String sel : sellers) {
            seller = seller + "'" + sel + "',";
        }
        seller = seller.substring(0, seller.length() - 1);

        String sql = "select lsbp.c_seller_code, lsbp.c_buyer_code from lc_seller_buyer_priority lsbp  " +
                "where lsbp.n_firm_id = '" + lcHeaderBO.getFirmId() + "' and lsbp.c_seller_code IN ("+seller+")";
        Query query = this.getQuery(sql);
        List<Object[]> result = this.getResultList(query);
        String id = "";
        if (result.size() == 0) {
            throw new RecordNotFoundException("Seller Buyer combo not Found ..!");
        }
        for (Object[] objects : result) {
            id = id + "'" + helper.toString(objects[0]) + helper.toString(objects[1]) + "',";
        }
        id = id.substring(0, id.length() - 1);

        return id;
    }

    private void confirmCart(JsonArray jsonArray, String seller, String buyer){

        for (JsonElement jsonElement:jsonArray){
            JsonObject json = jsonElement.getAsJsonObject();
            String sql = "INSERT INTO cust_expiry_order_mst(c_c2code,d_upload_datetime,c_cust_code,c_sman_code," +
                    "c_prefix,c_upload_ip,c_device_id,n_cancel_flag,d_dldate, " +
                    "n_order_status,c_created_user,c_modified_user,c_year,c_batch_no," +
                    "n_mrp,n_expiry_days,d_expiry_date,n_percentage,n_revised_mrp_value," +
                    "n_amount,n_salesman_bill_amount,n_sman_cancelflag) " +
                    "values(:sellerCode,now(),:buyerCode,:smanCode,'BUK'," +
                    "'0.0.0.0','0.0',0,now(),'3',:user," +
                    ":user,date_format(now(),'%y'),:batchNo,:mrp," +
                    ":expDays,:expDate,:percentage,:revMrp,:total,:total,0)";
            Query query = this.getQuery(sql);
            query.setParameter("sellerCode", seller);
            query.setParameter("buyerCode", buyer);
            query.setParameter("smanCode", json.get("c_sman_code").getAsString());
            query.setParameter("user", json.get("user").getAsString());
            query.setParameter("batchNo", json.get("c_batch_name").getAsString());
            query.setParameter("mrp", json.get("n_mrp").getAsString());
            query.setParameter("expDays", json.get("n_expiry_days").getAsString());
            query.setParameter("expDate", json.get("d_expiry_date").getAsString());
            query.setParameter("percentage", json.get("n_percentage").getAsString());
            query.setParameter("revMrp", json.get("n_rev_mrp").getAsString());
            String total = getTotal(json.get("n_qty").getAsInt(), json.get("n_loose_qty").getAsInt(),
                    json.get("n_rev_mrp").getAsDouble(), json.get("n_qty_per_box").getAsInt());
            query.setParameter("total",total);
            query.executeUpdate();

            String traSql = "INSERT INTO cust_expiry_transaction_mst(c_c2code,n_ord_srno," +
                    "d_created_datetime,c_ipaddress,n_order_status,n_downloaded_flag," +
                    "d_modified_datetime,c_deviceid,c_created_user,c_cust_code,n_bill_amount," +
                    "n_salesman_bill_amount,n_sman_cancelflag) " +
                    "values(:sellerCode,:orderId,now()," +
                    "'0.0.0.0',3,0,now(),'0.0',:user," +
                    ":buyerCode,:total,:total,0)";
            Query tSql = this.getQuery(traSql);
            tSql.setParameter("sellerCode", seller);
            tSql.setParameter("buyerCode", buyer);
            tSql.setParameter("user", json.get("user").getAsString());
            tSql.setParameter("orderId",lastInsrtedId());
            tSql.setParameter("total", total);
            tSql.executeUpdate();

            String transMstLast = lastInsrtedId();
            JsonArray det = jsonArray;
            int count =0;
            for (JsonElement ele: det) {
                count += 1;
                JsonObject detJson = ele.getAsJsonObject();
                String transDet = "INSERT INTO cust_expiry_transaction_det(c_c2code,n_tran_mst_srno,n_prec_det_rowid,c_item_code,n_qty,n_mrp,c_deviceid,n_confirm," +
                        "n_confirm_qty,c_batch_no,n_expiry_days,d_expiry_date,n_percentage,n_revised_mrp_value," +
                        "n_cancel_flag,n_sman_confirm_qty,n_sman_confirm_flag,n_sman_cancelflag,n_loose_qty," +
                        "c_pack,n_qty_per_box,c_scheme) " +
                        "values (:sellerCode,:transId,:count,:itemCode," +
                        ":qty,:mrp,'0.0',0,0,:batch," +
                        ":expDays,:expDate,:percentage,:revMrp," +
                        "0,0,0,0,:looseQty,:pack,:qtyPer,:scheme)";
                Query detQuery = this.getQuery(transDet);
                detQuery.setParameter("sellerCode",seller);
                detQuery.setParameter("transId",transMstLast);
                detQuery.setParameter("count",count);
                detQuery.setParameter("itemCode",detJson.get("c_item_code").getAsString());
                detQuery.setParameter("qty",detJson.get("n_qty").getAsString());
                detQuery.setParameter("mrp",detJson.get("n_mrp").getAsString());
                detQuery.setParameter("batch",detJson.get("c_batch_name").getAsString());
                detQuery.setParameter("expDays",detJson.get("n_expiry_days").getAsString());
                detQuery.setParameter("expDate",detJson.get("d_expiry_date").getAsString());
                detQuery.setParameter("revMrp",detJson.get("n_rev_mrp").getAsString());
                detQuery.setParameter("percentage",detJson.get("n_percentage").getAsString());
                detQuery.setParameter("looseQty",detJson.get("n_loose_qty").getAsString());
                detQuery.setParameter("pack",detJson.get("c_pack_size").getAsString());
                detQuery.setParameter("qtyPer",detJson.get("n_qty_per_box").getAsString());
                detQuery.setParameter("scheme",detJson.get("n_scheme").getAsString());
                detQuery.executeUpdate();
            }

            String upTemp = "update lc_temp_expiry_order set n_transaction_ref_no = '"+transMstLast+"',n_confirm ='1' " +
                    "where c_cust_code='"+buyer+"' and c_c2code ='"+seller+"'" +
                    " and n_confirm = 0 and n_transaction_ref_no =0";
            Query upLc = this.getQuery(upTemp);
            upLc.executeUpdate();
        }


    }

    public String lastInsrtedId(){
        String sql = "SELECT LAST_INSERT_ID()";
        Query lastInsQuery = this.getQuery(sql);
        Object obj = this.getSingleResult(lastInsQuery);
        return obj.toString();
    }

    public String getTotal(int qty, int looseQty, double revMrp, int qtyPerBox){
        double tot = 0.0;

        if(qty == 0){

            if (qtyPerBox > 0)
                tot = looseQty * revMrp / qtyPerBox;
            else
                tot = looseQty * revMrp;
        }
        else if(looseQty == 0){
                tot = qty * revMrp;
        }
        else {
            if (qtyPerBox > 0)
                tot = ((qty * revMrp)+(looseQty * revMrp)) / qtyPerBox;
            else
                tot = ((qty * revMrp)+(looseQty * revMrp));
        }

        return String.format("%.2f",tot);
    }
}
