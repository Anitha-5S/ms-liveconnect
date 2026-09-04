package com.c2.lc.ms.master.services;

import javax.persistence.Query;

import com.c2.lc.ms.master.services.interfaces.CustomerMappingService;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.c2.lc.lib.services.BaseDBServiceImpl;

@Service
@Log4j2
public class CustomerMappingServiceImpl extends BaseDBServiceImpl implements CustomerMappingService {

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void customerMappingCreation(String customerCode, String c2code, String uCode) {
        String sql = " INSERT INTO u_stockiest_customer_map(c_stockiest_code, u_stockiest_cust_code, c_ucode, d_date, " +
                " d_ldate, n_type) VALUES(:c2Code, :customerCode, :uCode , now(), now(), '2') ON DUPLICATE KEY UPDATE " +
                " d_ldate = now() ";
        Query query = this.getQuery(sql);
        query.setParameter("uCode", uCode);
        query.setParameter("c2Code", c2code);
        query.setParameter("customerCode", customerCode);
        query.executeUpdate();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createCustomerDetails(String netMedWarehouseC2Code, String partyId, JsonObject customerDetails) {
        String insertSql = " INSERT INTO cust_act_mst(c_c2code, c_code, c_name, c_short_name, c_grp_no, c_add_1," +
                " c_add_2, c_add_3, c_city, c_pin, c_drug_licence_no_1, c_drug_licence_no_2, n_auto_cr_adjust, " +
                " n_max_bill_amt, n_schm_slab2, n_schm_slab3 ) VALUES( :c2code, :code, :name, :shortName, :groupNo, " +
                " :address1, :address2, :address3, :city, :pin, :drugLicenseNo1, :drugLicenseNo2 , '0' , '0', '0', '0' ) " +
                " ON DUPLICATE KEY UPDATE c_name = :name ";
        Query query = this.getQuery(insertSql);
        query.setParameter("code", partyId);
        query.setParameter("c2code", netMedWarehouseC2Code);
        query.setParameter("name", helper.getString(customerDetails.get("name")));
        query.setParameter("city", helper.getString(customerDetails.get("city")));
        query.setParameter("pin", helper.getString(customerDetails.get("pinCode")));
        query.setParameter("groupNo", helper.getString(customerDetails.get("groupNo")));
        query.setParameter("address1", helper.getString(customerDetails.get("address1")));
        query.setParameter("address2", helper.getString(customerDetails.get("address2")));
        query.setParameter("address3", helper.getString(customerDetails.get("address3")));
        query.setParameter("shortName", helper.getString(customerDetails.get("shortName")));
        query.setParameter("drugLicenseNo1", helper.getString(customerDetails.get("drugLicenceNo1")));
        query.setParameter("drugLicenseNo2", helper.getString(customerDetails.get("drugLicenceNo2")));
        int count = query.executeUpdate();
        if (count > 0) {
            String sql = " INSERT INTO cust_act_det(c_c2code,c_cust_code,c_gst_no,d_date,d_ldate,t_ltime) " +
                    " VALUES( :c2code, :code, :gstNo, now(), now(), now()) ON DUPLICATE KEY UPDATE d_ldate = now() ";
            Query query1 = this.getQuery(sql);
            query1.setParameter("code", partyId);
            query1.setParameter("c2code", netMedWarehouseC2Code);
            query1.setParameter("gstNo", helper.getString(customerDetails.get("gstNo")));
            query1.executeUpdate();
        }
    }
}
