package com.c2.lc.ms.master.services;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import javax.persistence.Query;

import com.c2.lc.ms.master.bos.customerbos.EgCustomerCreationBO;
import com.c2.lc.ms.master.services.interfaces.CustomerMappingService;
import com.c2.lc.ms.master.services.interfaces.EgCustomerCreationPinCodeService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import com.google.gson.JsonElement;
import org.springframework.stereotype.Service;
import com.c2.lc.lib.services.BaseDBServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import com.c2.lc.ms.master.models.EgCustomerCreationLog;
import org.springframework.beans.factory.annotation.Autowired;
import com.c2.lc.ms.master.repos.mysql.EgCustomerCreationRepository;

@Log4j2
@Service
public class EgCustomerCreationPinCodeServiceImpl extends BaseDBServiceImpl implements EgCustomerCreationPinCodeService {

    @Value("${eg.customer-creation.api}") private String egCustomerCreationApiEndPoint;
    @Value("${eg.customer-creation.authorization}") private String egCustomerCreationAuthorizationValue;

    @Autowired
    CustomerMappingService customerMappingService;
    @Autowired EgCustomerCreationRepository egCustomerCreationRepository;

    @Override
    public void egCustomerCreationPinCode() {
        JsonArray c2codeList = getC2CodeList();
        log.info("c2code list {} ",c2codeList);
        for (JsonElement element:c2codeList) {
            JsonObject object = element.getAsJsonObject();
            String c2code = helper.getString(object.get("c2code"));
            String csqParentCode = helper.getString(object.get("c2ParentCode"));
            String cSquareUCode = helper.getString(object.get("cSquareUCode"));
            String pinCode = getCustomerPinDetail(csqParentCode, c2code);
            if (!helper.isEmpty(pinCode)) {
                pinCodeServiceabilityImplementMethod(c2code, cSquareUCode, pinCode);
            }
        }
    }

    private JsonArray getC2CodeList() {
        String cSquareC2Code = "03C000";
        String sql = " SELECT u_stockiest_cust_code, c_ucode FROM u_stockiest_customer_map " +
                " WHERE c_stockiest_code = :cSquareC2Code and u_stockiest_cust_code in ( " +
                " SELECT distinct cmid.c_c2code FROM lc_mid_merchant_onbording lmid  " +
                " INNER JOIN lc_merchant_onbording_c2code_mapping cmid ON " +
                " cmid.c_MID = lmid.c_MID WHERE cmid.c_c2code <> '-' AND cmid.c_c2code <> '') ";
        Query query = this.getQuery(sql);
        query.setParameter("cSquareC2Code", cSquareC2Code);

        List<Object[]> resultData = this.getResultList(query);
        JsonArray dataList = new JsonArray();
        for (Object[] res:resultData) {
            int i = -1;
            JsonObject data = new JsonObject();
            data.addProperty("c2ParentCode", cSquareC2Code);
            data.addProperty("c2code", helper.getString(res[++i]));
            data.addProperty("cSquareUCode", helper.getString(res[++i]));
            dataList.add(data);
        }
        return dataList;
    }

    private String getCustomerPinDetail(String csqParentCode, String c2code) {
        String sql = " SELECT lmm.c_pin_code FROM lc_merchant_onbording_c2code_mapping lmoc  " +
                " INNER JOIN lc_mid_merchant_onbording lmm ON " +
                " lmoc.c_MID = lmm.c_MID WHERE lmoc.c_c2code = :c2code ";
        Query query = this.getQuery(sql);
        query.setParameter("c2code",  c2code);
        return this.getSingleResultNull(query);
    }

    private void pinCodeServiceabilityImplementMethod(String c2code, String uCode, String pinCode) {
        JsonArray pinCodeMappedC2CodeData = getC2CodeBasedPinCode(pinCode);
        if (pinCodeMappedC2CodeData.size() > 0) {
            for (JsonElement element: pinCodeMappedC2CodeData) {
                JsonObject pinCodeData = element.getAsJsonObject();
                String egStoreC2Code = pinCodeData.get("egStoreC2Code").getAsString();
                String egParentCode = getParentCode(egStoreC2Code);
                String partyId = checkEgCustomerCodeMapping(egParentCode, uCode);
                if (partyId != null) {
                    customerMappingService.customerMappingCreation(partyId, egStoreC2Code, uCode);
                    insertCustomerActMasterDetails(c2code, egStoreC2Code, partyId, pinCode);
                } else {
                    callEgCustomerCreationApi(c2code, uCode, pinCode);
                }
            }
        }
    }

    private void callEgCustomerCreationApi(String c2code, String uCode, String pinCode) {
        String egC2Code = "700000";
        StringBuilder str = new StringBuilder();
        EgCustomerCreationLog egCustomerCreationLog = new EgCustomerCreationLog();
        EgCustomerCreationBO customerCreationBO = getCustomerCreationDetails(c2code, uCode);
        if (customerCreationBO.getDlNo1() != null) {
            JsonObject response = apiCall(customerCreationBO);
            egCustomerCreationLog.setRequest(customerCreationBO);
            egCustomerCreationLog.setDate(helper.getCurrentDate());
            egCustomerCreationLog.setRequestTimeStamp(helper.getCurrentTime());
            if (helper.getInt(response.get("appStatusCode")) == 0) {
                JsonObject data = response.get("payloadJson").getAsJsonObject();
                String egCustomerId = helper.getString(data.get("code"));
                customerMappingService.customerMappingCreation(egCustomerId, egC2Code, uCode);
                egCustomerCreationLog.setStatus("S");
                egCustomerCreationLog.setEgCustomerId(egCustomerId);
            } else {
                egCustomerCreationLog.setStatus("F");
            }
            egCustomerCreationLog.setResponseTimeStamp(helper.getCurrentTime());
            egCustomerCreationLog.setResponse(helper.fromJson(response, Object.class));
            egCustomerCreationLog.setCsqCustomerCode(uCode);
            egCustomerCreationLog.setId(str.append(c2code).append("-").append(uCode).toString());
            egCustomerCreationRepository.save(egCustomerCreationLog);
            if (egCustomerCreationLog.getStatus().equals("S")) {
                pinCodeServiceabilityImplementMethod(c2code, uCode, pinCode);
            }
        }

    }

    private JsonArray getC2CodeBasedPinCode(String pinCode) {
        String sql = " SELECT c_c2code, c_pincode FROM cust_pincodewise_c2code WHERE c_pincode = :pinCode  and " +
                " c_c2code like '70%' ";
        Query query = this.getQuery(sql);
        query.setParameter("pinCode", pinCode);
        List<Object[]> resultSetData = this.getResultList(query);
        JsonArray dataList = new JsonArray();
        for (Object[] res:resultSetData) {
            JsonObject details = new JsonObject();
            details.addProperty("egStoreC2Code", helper.getString(res[0]));
            details.addProperty("pinCode", helper.getString(res[1]));
            dataList.add(details);
        }
        return dataList;
    }

    private String getParentCode(String egStoreC2Code) {
        String sql = " SELECT c_multi_firm_code FROM multi_firm_det WHERE c_firm_code = :egStoreC2Code ";
        Query query = this.getQuery(sql);
        query.setParameter("egStoreC2Code", egStoreC2Code);
        return this.getSingleResultNull(query);
    }

    private String checkEgCustomerCodeMapping(String egParentCode, String uCode) {
        String sql = " SELECT u_stockiest_cust_code FROM u_stockiest_customer_map WHERE " +
                " c_stockiest_code = :egParentCode AND c_ucode = :uCode ";
        Query query = this.getQuery(sql);
        query.setParameter(egParentCode, egParentCode);
        query.setParameter("uCode", uCode);
        return this.getSingleResultNull(query);
    }

    private void insertCustomerActMasterDetails(String c2code, String netMedWarehouseC2Code, String partyId, String pinCode) {
        String sql = " select mst.c_name, mst.c_short_name, mst.c_grp_no, mst.c_add_1, mst.c_add_2, mst.c_add_3, " +
                " mst.c_city, mst.c_pin, mst.c_drug_licence_no_1, mst.c_drug_licence_no_2,  " +
                " det.c_gst_no from cust_act_mst mst LEFT JOIN cust_act_det det ON mst.c_c2code = det.c_c2code " +
                " AND mst.c_code = det.c_cust_code where mst.c_c2code = '03C000' and mst.c_code = :c2code ";
        Query query =  this.getQuery(sql);
        query.setParameter("c2code", c2code);
        List<Object[]> resultData = this.getResultList(query);

        for (Object[] res:resultData) {
            int i = -1;
            JsonObject customerDetails = new JsonObject();
            customerDetails.addProperty("name", helper.getString(res[++i]));
            customerDetails.addProperty("shortName", helper.getString(res[++i]));
            customerDetails.addProperty("groupNo", helper.getString(res[++i]));
            customerDetails.addProperty("address1", helper.getString(res[++i]));
            customerDetails.addProperty("address2", helper.getString(res[++i]));
            customerDetails.addProperty("address3", helper.getString(res[++i]));
            customerDetails.addProperty("city", helper.getString(res[++i]));
            String pin = helper.getString(res[++i]);
            if (helper.isEmpty(pin)) {
                pin = pinCode;
            }
            customerDetails.addProperty("pinCode", pin);
            customerDetails.addProperty("drugLicenceNo1", helper.getString(res[++i]));
            customerDetails.addProperty("drugLicenceNo2", helper.getString(res[++i]));
            customerDetails.addProperty("gstNo", helper.getString(res[++i]));
            customerMappingService.createCustomerDetails(netMedWarehouseC2Code, partyId, customerDetails);
        }
    }

    private EgCustomerCreationBO getCustomerCreationDetails(String c2code, String uCode) {

        String cSquareC2Code = "700000";

        EgCustomerCreationBO egCustomerCreationBO = new EgCustomerCreationBO();

        String sql = " SELECT cam.c_name, cam.c_short_name, cad.c_gst_no, sm.c_state_code, cam.c_drug_licence_no_1, " +
                " cam.c_drug_licence_no_2, cam.c_pan_no, cam.c_contact_person, cam.c_add_1, cam.c_add_2, " +
                " cam.c_add_3, cam.c_city, sm.c_state_name, 'INDIA' country, cam.c_pin, cam.c_email_id, " +
                " cam.c_mobile from cust_act_mst cam left join cust_act_det cad ON cam.c_c2code = cad.c_c2code " +
                " AND cam.c_code = cad.c_cust_code LEFT JOIN pincode_mst pm ON pm.c_code = cam.c_pin " +
                " LEFT JOIN nm_rwos_state_mst sm ON sm.c_state_id = pm.c_state_code WHERE cam.c_c2code = :cSquareC2Code " +
                " AND cam.c_code = :cSquareCustomerCode AND cam.c_drug_licence_no_1 IS NOT NULL AND " +
                " cam.c_drug_licence_no_1 <> '' AND cam.c_pan_no IS NOT NULL ";
        Query query = this.getQuery(sql);
        query.setParameter("cSquareC2Code", cSquareC2Code);
        query.setParameter("cSquareCustomerCode", c2code);
        List<Object[]> resultDataSet = this.getResultList(query);
        for (Object[] res : resultDataSet) {
            int i = -1;
            egCustomerCreationBO.setUCode(c2code);
            egCustomerCreationBO.setName(helper.getString(res[++i]));
            egCustomerCreationBO.setShortName(helper.getString(res[++i]));
            egCustomerCreationBO.setGstNo(helper.getString(res[++i]));
            egCustomerCreationBO.setStateCode(helper.getString(res[++i]));
            egCustomerCreationBO.setDlNo1(helper.getString(res[++i]));
            egCustomerCreationBO.setDlNo2(helper.getString(res[++i]));
            egCustomerCreationBO.setPanNo(helper.getString(res[++i]));
            egCustomerCreationBO.setContactName(helper.getString(res[++i]));
            egCustomerCreationBO.setAddress1(helper.getString(res[++i]));
            egCustomerCreationBO.setAddress2(helper.getString(res[++i]));
            egCustomerCreationBO.setAddress3(helper.getString(res[++i]));
            egCustomerCreationBO.setCity(helper.getString(res[++i]));
            egCustomerCreationBO.setStateName(helper.getString(res[++i]));
            egCustomerCreationBO.setCountry(helper.getString(res[++i]));
            egCustomerCreationBO.setPincode(helper.getString(res[++i]));
            egCustomerCreationBO.setEmailId(helper.getString(res[++i]));
            egCustomerCreationBO.setMobileNo(helper.getString(res[++i]));

            int panLength = (egCustomerCreationBO.getPanNo().length());
            String panCode = "";
            if (panLength == 0) {
                if (egCustomerCreationBO.getGstNo().length() >= 5) {
                    panCode = egCustomerCreationBO.getGstNo().substring(2, (egCustomerCreationBO.getGstNo().length()-3));
                    log.debug("pan length {}",panCode);
                    egCustomerCreationBO.setPanNo(panCode);
                }
            }
        }
        return egCustomerCreationBO;
    }

    private JsonObject apiCall(EgCustomerCreationBO customerCreationBO) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", egCustomerCreationAuthorizationValue);
        JsonObject egRequestBody = new JsonObject();
        egRequestBody.addProperty("c2_code","700000");
        egRequestBody.addProperty("br_code","0000");
        egRequestBody.add("data",helper.fromJson(helper.toJson(customerCreationBO), JsonObject.class));
        return helper.fromJson(this.callWebClientPostSyncApiWithHeader(egCustomerCreationApiEndPoint,
                helper.toJson(egRequestBody), headers), JsonObject.class);
    }
}
