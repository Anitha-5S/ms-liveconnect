package com.c2.lc.ms.master.services;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import javax.persistence.Query;

import com.c2.lc.ms.master.bos.customerbos.NmCustomerCreationDetailsBO;
import com.c2.lc.ms.master.bos.customerbos.NmCustomerCreationMasterBO;
import com.c2.lc.ms.master.services.interfaces.CustomerCreationPinCodeService;
import com.c2.lc.ms.master.services.interfaces.CustomerMappingService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import com.google.gson.JsonElement;
import org.springframework.stereotype.Service;
import com.c2.lc.lib.services.BaseDBServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import com.c2.lc.ms.master.models.NmCustomerCreationLog;
import org.springframework.beans.factory.annotation.Autowired;
import com.c2.lc.ms.master.repos.mongo.NmCustomerCreationRepository;

@Log4j2
@Service
public class CustomerCreationPinCodeServiceImpl extends BaseDBServiceImpl implements CustomerCreationPinCodeService {

    @Value("${netmeds.customer-creation.api}") private String customerCreationApiEndPoint;
    @Value("${netmeds.customer-creation.api.basic-auth.username}") private String basicAuthUserName;
    @Value("${netmeds.customer-creation.api.basic-auth.password}") private String basicAuthPassword;

    @Autowired
    CustomerMappingService customerMappingService;
    @Autowired NmCustomerCreationRepository nmCustomerCreationRepository;

    @Override
    public void nmCustomerCreationPinCode() {
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

    private void pinCodeServiceabilityImplementMethod(String c2code, String uCode, String pinCode) {
        log.debug("c2code {} pinCode {} ", c2code, pinCode);
        JsonArray pinCodeMappedC2CodeData = getC2CodeBasedPinCode(pinCode);
        if (pinCodeMappedC2CodeData.size() > 0) {
            log.info("data {} ", pinCodeMappedC2CodeData);
            for (JsonElement element: pinCodeMappedC2CodeData) {
                JsonObject pinCodeData = element.getAsJsonObject();
                String netMedWarehouseC2Code = pinCodeData.get("nmWareHouseC2Code").getAsString();
                String nmParentCode = getParentCode(pinCodeData.get("nmWareHouseC2Code").getAsString());
                String partyId = checkNmCustomerCodeMapping(nmParentCode, uCode);
                if (partyId != null) {
                    customerMappingService.customerMappingCreation(partyId, netMedWarehouseC2Code, uCode);
                    insertCustomerActMasterDetails(c2code, netMedWarehouseC2Code, partyId, pinCode);
                } else {
                    callNetMedApi(c2code, uCode, pinCode);
                }
            }
        }
    }

    private void callNetMedApi(String c2code, String uCode, String pinCode) {
        String nmC2Code = "05G000";
        StringBuilder str = new StringBuilder();
        NmCustomerCreationLog nmLog = new NmCustomerCreationLog();
        NmCustomerCreationMasterBO customerCreationBO = getCustomerCreationDetails(c2code, uCode);
        JsonObject response = callCustomerCreationApi(customerCreationBO);
        nmLog.setRequest(customerCreationBO);
        nmLog.setDate(helper.getCurrentDate());
        nmLog.setRequestTimeStamp(helper.getCurrentTime());
        if (response.get("returnCode").getAsInt() == 1000) {
            JsonObject data = response.get("returnData").getAsJsonObject();
            String netmedsPartyId = helper.getString(data.get("partyId"));
            customerMappingService.customerMappingCreation(netmedsPartyId, nmC2Code, uCode);
            nmLog.setStatus("S");
            nmLog.setNmCustomerId(netmedsPartyId);
        } else {
            nmLog.setStatus("F");
        }
        nmLog.setResponseTimeStamp(helper.getCurrentTime());
        nmLog.setResponse(helper.fromJson(response, Object.class));
        nmLog.setCsqCustomerCode(uCode);
        nmLog.setId(str.append(helper.getString(c2code)).append("-")
                .append(uCode).toString());
        nmCustomerCreationRepository.save(nmLog);
        if (nmLog.getStatus().equals("S")) {
            pinCodeServiceabilityImplementMethod(c2code, uCode, pinCode);
        }
    }

    private JsonObject callCustomerCreationApi(NmCustomerCreationMasterBO customerCreationBO) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic Q1NxdWFyZUIyQjozVjlAa1JoektyM1FqSTgw");
        return helper.fromJson(this.callWebClientPostSyncApiWithHeader(customerCreationApiEndPoint, helper.toJson(customerCreationBO), headers), JsonObject.class);
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

    private String checkNmCustomerCodeMapping(String nmParentCode, String uCode) {
        String sql = " SELECT u_stockiest_cust_code FROM u_stockiest_customer_map WHERE " +
                " c_stockiest_code = :nmParentCode AND c_ucode = :uCode ";
        Query query = this.getQuery(sql);
        query.setParameter("nmParentCode", nmParentCode);
        query.setParameter("uCode", uCode);
        return this.getSingleResultNull(query);
    }

    private String getParentCode(String nmWareHouseC2Code) {
        String sql = " SELECT c_multi_firm_code FROM multi_firm_det WHERE c_firm_code = :nmWareHouseC2Code ";
        Query query = this.getQuery(sql);
        query.setParameter("nmWareHouseC2Code", nmWareHouseC2Code);
        return this.getSingleResultNull(query);
    }

    private JsonArray getC2CodeBasedPinCode(String pinCode) {
        String sql = " SELECT c_c2code, c_pincode FROM cust_pincodewise_c2code WHERE c_pincode = :pinCode  and " +
                " c_c2code like '05G%' ";
        Query query = this.getQuery(sql);
        query.setParameter("pinCode", pinCode);
        List<Object[]> resultSetData = this.getResultList(query);
        JsonArray dataList = new JsonArray();
        for (Object[] res:resultSetData) {
            JsonObject details = new JsonObject();
            details.addProperty("nmWareHouseC2Code", helper.getString(res[0]));
            details.addProperty("pinCode", helper.getString(res[1]));
            dataList.add(details);
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

    private NmCustomerCreationMasterBO getCustomerCreationDetails(String c2code, String uCode) {

        String cSquareC2Code = "03C000";

        List<NmCustomerCreationDetailsBO> detailsBOList = new ArrayList<>();
        NmCustomerCreationMasterBO nmCustomerCreationMasterBO = new NmCustomerCreationMasterBO();

        String sql = " SELECT cam.c_name, cam.c_short_name, cad.c_gst_no, sm.c_state_code, cam.c_drug_licence_no_1, " +
                " cam.c_drug_licence_no_2, cam.c_pan_no, cam.c_contact_person, cam.c_add_1, cam.c_add_2, " +
                " cam.c_add_3, cam.c_city, sm.c_state_name, 'INDIA' country, cam.c_pin, cam.c_email_id, " +
                " cam.c_mobile from cust_act_mst cam left join cust_act_det cad ON cam.c_c2code = cad.c_c2code " +
                " AND cam.c_code = cad.c_cust_code LEFT JOIN pincode_mst pm ON pm.c_code = cam.c_pin " +
                " LEFT JOIN nm_rwos_state_mst sm ON sm.c_state_id = pm.c_state_code WHERE cam.c_c2code = :cSquareC2Code " +
                " AND cam.c_code = :cSquareCustomerCode AND cam.c_drug_licence_no_1 IS NOT NULL AND " +
                " cam.c_drug_licence_no_1 <> '' AND cam.c_pan_no IS NOT NULL ";
        Query query = this.getQuery(sql);
        query.setParameter("cSquareCustomerCode", c2code);
        query.setParameter("cSquareC2Code", cSquareC2Code);
        List<Object[]> resultDataSet = this.getResultList(query);
        for (Object[] res : resultDataSet) {
            int i = -1;
            NmCustomerCreationDetailsBO nmCustomerCreationDetailsBO = new NmCustomerCreationDetailsBO();
            nmCustomerCreationMasterBO.setCustomerRefId(c2code);
            nmCustomerCreationMasterBO.setPartyName(helper.getString(res[++i]));
            nmCustomerCreationMasterBO.setPartyShortName(helper.getString(res[++i]));
            nmCustomerCreationMasterBO.setGstin(helper.getString(res[++i]));
            nmCustomerCreationMasterBO.setStateCode(helper.getString(res[++i]));
            nmCustomerCreationMasterBO.setDlNum1(helper.getString(res[++i]));
            nmCustomerCreationMasterBO.setDlNum2(helper.getString(res[++i]));
            nmCustomerCreationMasterBO.setPan(helper.getString(res[++i]));
            nmCustomerCreationDetailsBO.setContactName(helper.getString(res[++i]));
            nmCustomerCreationDetailsBO.setAddressLine1(helper.getString(res[++i]));
            nmCustomerCreationDetailsBO.setAddressLine2(helper.getString(res[++i]));
            nmCustomerCreationDetailsBO.setAddressLine3(helper.getString(res[++i]));
            nmCustomerCreationDetailsBO.setCity(helper.getString(res[++i]));
            nmCustomerCreationDetailsBO.setStateName(helper.getString(res[++i]));
            nmCustomerCreationDetailsBO.setCountry(helper.getString(res[++i]));
            nmCustomerCreationDetailsBO.setPincode(helper.getString(res[++i]));
            nmCustomerCreationDetailsBO.setEmailId(helper.getString(res[++i]));
            nmCustomerCreationDetailsBO.setMobileNum1(helper.getString(res[++i]));

            int panLength = (nmCustomerCreationMasterBO.getPan().length());
            String panCode = "";
            if (panLength == 0) {
                if (nmCustomerCreationMasterBO.getGstin().length() >= 5) {
                    panCode = nmCustomerCreationMasterBO.getGstin().substring(2, (nmCustomerCreationMasterBO.getGstin().length()-3));
                    log.debug("pan length {}",panCode);
                    nmCustomerCreationMasterBO.setPan(panCode);
                }
            }

            detailsBOList.add(nmCustomerCreationDetailsBO);
        }
        nmCustomerCreationMasterBO.setNmCustomerCreationDetailsBOList(detailsBOList);
        return nmCustomerCreationMasterBO;
    }
}
