package com.c2.lc.ms.master.services;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.LcOrder;
import com.c2.lc.ms.master.bos.LcOrderItemBO;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.OrderSyncService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import javax.transaction.Transactional;

@Service
public class OrderSyncServiceImpl extends MasterBaseServiceImpl implements OrderSyncService {

    @Override
    @Transactional(rollbackOn = Exception.class)
    public JsonObject orderSync(JsonObject jsonObject) {

        int lcOrderId = 0;
        int transId = 0;
        try{
            LcOrder lcOrder = helper.fromJSON(jsonObject, LcOrder.class);
           // String inQ = custMstQuery(lcOrder);
            System.out.println(lcOrder);
          //  System.out.println(inQ);

            String inQ = "INSERT INTO cust_order_mst ( c_c2code, c_year, c_prefix," +
                    " c_userid, d_upload_datetime, c_device_id, n_delivery_pickup_flag," +
                    " n_order_status, n_device_type, n_amount, n_discount, c_modified_user," +
                    " c_cust_code, c_created_user, c_order_no, n_ref_srno, n_cancel_flag, c_sman_code ) " +
                    "VALUES (:c_c2code,:c_year, :c_prefix, :c_userid," +
                    ":d_upload_datetime, :c_device_id, :n_delivery_pickup_flag, :n_order_status, " +
                    ":n_device_type, :n_amount, :n_discount, :c_modified_user," +
                    ":c_cust_code, :c_created_user, :c_order_no, :n_ref_srno, :n_cancel_flag, :smanCode)";
            System.out.println(inQ);
            Query query = this.getQuery(inQ);
            query.setParameter("c_c2code",lcOrder.getOrderSummary().getSellerCode());
            query.setParameter("c_year",helper.getCurrentYear());
            if(lcOrder.getOrderFrom() != null && !helper.isEmpty(lcOrder.getOrderFrom())) {
                if (lcOrder.getOrderFrom().equals("TS")) {
                    query.setParameter("c_prefix", "TS");
                    query.setParameter("c_device_id", "Touch Store");
                }
            } else {
                query.setParameter("c_prefix", "LO");
                query.setParameter("c_device_id", "Live Order");
            }
            // query.setParameter("c_userid",lcOrder.getUserId());
            query.setParameter("d_upload_datetime", helper.getCurrentTime());
            query.setParameter("n_delivery_pickup_flag", 1);
            query.setParameter("n_order_status", 4);
            query.setParameter("n_device_type", 21);
            query.setParameter("n_amount", lcOrder.getOrderDetails().getAmountPaid());
            query.setParameter("n_discount", lcOrder.getOrderDetails().getCashDiscount());
            query.setParameter("c_modified_user",lcOrder.getUserId());
            query.setParameter("c_cust_code",lcOrder.getCustCode());
            query.setParameter("c_created_user",lcOrder.getMobileNo());
            query.setParameter("c_order_no",lcOrder.getOrderId());
            query.setParameter("c_userid",lcOrder.getUserId());
            query.setParameter("n_ref_srno",lcOrder.getOrderId());
            query.setParameter("n_cancel_flag",0);
            query.setParameter("smanCode", getSmanCode(lcOrder.getOrderSummary().getSellerCode(),
                    lcOrder.getCustCode()));
            query.executeUpdate();
            Query custTrans = this.getQuery("INSERT INTO cust_transaction_mst ( c_c2code, n_bill_amount, n_discount_amount, " +
                    "               n_order_or_invoice_flag, n_order_status, n_cancel_flag, n_user_type_flag, " +
                    "                 c_created_user, c_modify_user, d_created_datetime, d_modified_datetime, " +
                    "                   n_ord_srno, c_device_type, n_type, c_item_hold_flag, " +
                    "                  c_cust_code, n_promo_discount, n_delivery_charge  ) " +
                    "                   VALUES (:c_c2code,:n_bill_amount, :n_discount_amount, :n_order_or_invoice_flag, " +
                    "                    :n_order_status, :n_cancel_flag, :n_user_type_flag, " +
                    "                    :c_created_user, :c_modify_user, :d_created_datetime, :d_modified_datetime," +
                    "                    :n_ord_srno, :c_device_type, :n_type, :c_item_hold_flag, :c_cust_code," +
                    "                  :n_promo_discount, :n_delivery_charge )");
            custTrans.setParameter("c_c2code", lcOrder.getOrderSummary().getSellerCode());
            custTrans.setParameter("n_bill_amount",lcOrder.getOrderDetails().getAmountPaid() );
            custTrans.setParameter("n_discount_amount",lcOrder.getOrderDetails().getCashDiscount() );
            custTrans.setParameter("n_order_or_invoice_flag", 1);
            custTrans.setParameter("n_order_status", 4);
            custTrans.setParameter("n_cancel_flag", 0);
            custTrans.setParameter("n_user_type_flag", 1);
            custTrans.setParameter("c_created_user", lcOrder.getMobileNo());
            custTrans.setParameter("c_modify_user", lcOrder.getUserId());
            custTrans.setParameter("d_created_datetime", helper.getCurrentTime());
            custTrans.setParameter("d_modified_datetime",helper.getCurrentTime() );
            lcOrderId = getLcOneOrderId(lcOrder.getOrderId());
            custTrans.setParameter("n_ord_srno",lcOrderId);
            custTrans.setParameter("c_device_type",21 );
            custTrans.setParameter("n_type",1 );
            custTrans.setParameter("c_item_hold_flag",0 );
            custTrans.setParameter("c_cust_code",lcOrder.getCustCode() );
            custTrans.setParameter("n_promo_discount", 0);
            custTrans.setParameter("n_delivery_charge",0 );
            custTrans.executeUpdate();
            for(int i =0; i < lcOrder.getOrderDetails().getOrderItems().size(); i++){
                Query custDet = this.getQuery("INSERT INTO cust_transaction_det ( c_c2code, n_tran_mst_srno, n_prec_det_rowid, " +
                        "                     c_item_code, n_qty, n_disc_per, n_rate, " +
                        "                     n_value, c_deviceid, n_device_type, n_rate_consider_flag, n_scheme_qty, " +
                        "                     n_actual_rate, c_buyer_itemcode, c_buyer_item_name ) " +
                        "                    VALUES (:c_c2code,:n_tran_mst_srno, :n_prec_det_rowid, :c_item_code, " +
                        "                    :n_qty, :n_disc_per, :n_rate, :n_value, " +
                        "                    :c_deviceid, :n_device_type, :n_rate_consider_flag, :n_scheme_qty, " +
                        "                    :n_actual_rate, :c_buyer_itemcode, :c_buyer_item_name )");
                custDet.setParameter("c_c2code",lcOrder.getOrderSummary().getSellerCode());
                transId = getTransId(getLcOneOrderId(lcOrder.getOrderId()));
                custDet.setParameter("n_tran_mst_srno",transId);
                custDet.setParameter("n_prec_det_rowid",i+1);
                LcOrderItemBO itemBO = lcOrder.getOrderDetails().getOrderItems().get(i);
                custDet.setParameter("c_item_code",itemBO.getSellerItemCode());
                custDet.setParameter("n_qty",itemBO.getQuantity());
                custDet.setParameter("n_disc_per",itemBO.getDiscountPercentage());
                custDet.setParameter("n_rate",itemBO.getSaleRate());
                custDet.setParameter("n_value",itemBO.getNetAmount());
                custDet.setParameter("c_deviceid","Live order");
                custDet.setParameter("n_device_type",21);
                custDet.setParameter("n_rate_consider_flag",0);
                custDet.setParameter("n_scheme_qty",itemBO.getSchemeQuantity());
                custDet.setParameter("n_actual_rate",itemBO.getMrp());
                custDet.setParameter("c_buyer_itemcode",itemBO.getItemCode());
                custDet.setParameter("c_buyer_item_name",itemBO.getItemName());
                custDet.executeUpdate();
            }}
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
        JsonObject res = new JsonObject();
        res.addProperty("c_order_id",lcOrderId);
        res.addProperty("c_trans_no", transId);
        return res;
    }

    private String custMstQuery(LcOrder lcOrder){
        return "INSERT INTO cust_order_mst ( c_c2code, c_year, c_prefix," +
                " c_userid, d_upload_datetime, c_device_id, n_delivery_pickup_flag," +
                " n_order_status, n_device_type, n_amount, n_discount, c_modified_user," +
                " c_cust_code, c_created_user, c_order_no, n_ref_srno ) " +
                "VALUES ( "+lcOrder.getOrderSummary().getSellerCode()+", "+
                helper.getCurrentYear()+", LO, "+
                lcOrder.getUserId()+", "+
                lcOrder.getOrderSummary().getOrderDate() +", "+
                "Live Order,1 , 4, 21," + lcOrder.getOrderDetails().getAmountPaid()+ ", "+
                lcOrder.getOrderDetails().getCashDiscount()+", "+
                lcOrder.getUserId()+ ", "+
                lcOrder.getCustCode()+", "+
                lcOrder.getUserId()+", "+
                lcOrder.getOrderId()+", "+
                Integer.parseInt(lcOrder.getOrderId()) +")";
    }
    private JsonObject getCustOrderMst(LcOrder lcOrder){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("c_c2code",lcOrder.getOrderSummary().getSellerCode());
        jsonObject.addProperty("c_year",helper.getCurrentYear());
        if (lcOrder.getOrderFrom() != null && !helper.isEmpty(lcOrder.getOrderFrom())) {
            if (lcOrder.getOrderFrom().equals("TS")) {
                jsonObject.addProperty("c_prefix", "TS");
            }
        } else {
            jsonObject.addProperty("c_prefix", "LO");
        }
        jsonObject.addProperty("c_userid",lcOrder.getUserId());
        jsonObject.addProperty("d_upload_datetime", String.valueOf(lcOrder.getOrderSummary().getOrderDate()));
        if (lcOrder.getOrderFrom() != null && !helper.isEmpty(lcOrder.getOrderFrom())) {
            if (lcOrder.getOrderFrom().equals("TS")) {
                jsonObject.addProperty("c_device_id", "Touch Store");
            }
        } else {
            jsonObject.addProperty("c_device_id", "Live Order");
        }
        jsonObject.addProperty("n_delivery_pickup_flag",1);
        jsonObject.addProperty("n_order_status",4);
        jsonObject.addProperty("n_device_type",21);
        jsonObject.addProperty("n_amount",lcOrder.getOrderDetails().getAmountPaid());
        jsonObject.addProperty("n_discount",lcOrder.getOrderDetails().getCashDiscount());
        jsonObject.addProperty("c_modified_user",lcOrder.getUserId());
        jsonObject.addProperty("c_cust_code",lcOrder.getCustCode());
        jsonObject.addProperty("c_created_user",lcOrder.getUserId());
        jsonObject.addProperty("c_order_no",lcOrder.getOrderId());
        jsonObject.addProperty("n_ref_srno",Integer.parseInt(lcOrder.getOrderId()));
        return jsonObject;
    }
    private int getLcOneOrderId(String loOrderId){
        int orderId =0;
        Query query = this.getQuery(
                "SELECT com.n_srno FROM cust_order_mst com WHERE com.c_order_no = :orderId");
        query.setParameter("orderId",loOrderId);
        Object obj = this.getSingleResult(query);
        if (obj != null){
            orderId = (Integer) obj;
        }
        return orderId;
    }
    private int getTransId(int lcOneId){

        int transId =0;
        Query query = this.getQuery(
                "SELECT ctm.n_srno FROM cust_transaction_mst ctm WHERE ctm.n_ord_srno = :orderId");
        query.setParameter("orderId",lcOneId);
        Object obj = this.getSingleResult(query);
        if (obj != null){
            transId = (Integer) obj;
        }
        return transId;
    }
    private JsonObject getCustTransMst(LcOrder lcOrder){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("c_c2code",lcOrder.getOrderSummary().getSellerCode());
        jsonObject.addProperty("n_bill_amount",lcOrder.getOrderDetails().getAmountPaid());
        jsonObject.addProperty("n_discount_amount",lcOrder.getOrderDetails().getCashDiscount());
        jsonObject.addProperty("n_order_or_invoice_flag",1);
        jsonObject.addProperty("n_order_status",4);
        jsonObject.addProperty("n_cancel_flag",0);
        jsonObject.addProperty("n_user_type_flag",1);
        jsonObject.addProperty("c_created_user",lcOrder.getUserId());
        jsonObject.addProperty("c_modify_user",lcOrder.getUserId());
        jsonObject.addProperty("d_created_datetime", String.valueOf(lcOrder.getOrderSummary().getOrderDate()));
        jsonObject.addProperty("d_modified_datetime",String.valueOf(lcOrder.getOrderSummary().getOrderDate()));
        jsonObject.addProperty("n_ord_srno",getLcOneOrderId(lcOrder.getOrderId()));
        jsonObject.addProperty("c_device_type",21);
        jsonObject.addProperty("n_type",1);
        jsonObject.addProperty("c_item_hold_flag",0);
        jsonObject.addProperty("c_cust_code",lcOrder.getCustCode());
        jsonObject.addProperty("n_promo_discount",0);
        //n_delivery_charge
        jsonObject.addProperty("n_delivery_charge",0);


        return jsonObject;
    }
    private JsonObject getCustTransDet(LcOrder lcOrder, int i){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("c_c2code",lcOrder.getOrderSummary().getSellerCode());
        jsonObject.addProperty("n_tran_mst_srno",getTransId(getLcOneOrderId(lcOrder.getOrderId())));
        jsonObject.addProperty("n_prec_det_rowid",lcOrder.getOrderSummary().getNoOfLineItems());
        LcOrderItemBO itemBO = lcOrder.getOrderDetails().getOrderItems().get(i);
        jsonObject.addProperty("c_item_code",itemBO.getSellerItemCode());
        jsonObject.addProperty("n_qty",itemBO.getQuantity());
        jsonObject.addProperty("n_disc_per",itemBO.getDiscountPercentage());
        jsonObject.addProperty("n_rate",itemBO.getSaleRate());
        jsonObject.addProperty("n_value",itemBO.getNetAmount());
        jsonObject.addProperty("c_deviceid","Live order");
        jsonObject.addProperty("n_device_type",21);
        jsonObject.addProperty("n_rate_consider_flag",0);
        jsonObject.addProperty("n_scheme_qty",itemBO.getSchemeQuantity());
        jsonObject.addProperty("n_actual_rate",itemBO.getMrp());
        jsonObject.addProperty("c_buyer_itemcode",itemBO.getItemCode());
        jsonObject.addProperty("c_buyer_item_name",itemBO.getItemName());
        // jsonObject.addProperty("n_delivery_charge",0);



        return jsonObject;
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
}
