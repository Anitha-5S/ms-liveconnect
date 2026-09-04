package com.c2.lc.ms.master.services;

import java.util.*;
import java.math.BigDecimal;
import javax.persistence.Query;

import com.c2.lc.ms.master.bos.customerbos.EgCustomerCreationBO;
import com.c2.lc.ms.master.bos.customerbos.NmCustomerCreationDetailsBO;
import com.c2.lc.ms.master.bos.customerbos.NmCustomerCreationMasterBO;
import com.c2.lc.ms.master.entities.mysql.*;
import com.c2.lc.ms.master.repos.mongo.NmCustomerCreationRepository;
import com.c2.lc.ms.master.repos.mysql.*;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.CustomerMappingService;
import com.c2.lc.ms.master.services.interfaces.CustomerMasterService;
import lombok.extern.slf4j.Slf4j;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.ms.master.models.EgCustomerCreationLog;
import com.c2.lc.ms.master.models.NmCustomerCreationLog;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
public class CustomerMasterServiceImpl extends MasterBaseServiceImpl implements CustomerMasterService {

    @Autowired
    CustActMstRepository custActMstRepository;
    @Autowired
    CustActDetRepository custActDetRepository;
    @Autowired
    CustomerMappingService customerMappingCreation;
    @Autowired
    NmCustomerCreationRepository nmCustomerCreationRepository;
    @Autowired
    EgCustomerCreationRepository egCustomerCreationRepository;

    @Value("${eg.customer-creation.api}") private String egCustomerCreationApiEndPoint;
    @Value("${mysql.jpa.properties.hibernate.jdbc.batch_size}") private int batchSize;
    @Value("${netmeds.customer-creation.api}") private String customerCreationApiEndPoint;
    @Value("${netmeds.customer-creation.api.basic-auth.username}") private String basicAuthUserName;
    @Value("${netmeds.customer-creation.api.basic-auth.password}") private String basicAuthPassword;
    @Value("${fetch.max.size.limit}") private int maxSizelimit;


    private String rilC2Coode = "700000";
    private String grabC2Code = "000800";
    private String nmC2Code = "05G000";


    @Override
    public void insert(JsonObject data, int suppFlag, int custFlag) throws InputPayloadException {

        if (!data.has("row") || data.get("row").getAsJsonArray().size() == 0)
            throw new InputPayloadException("row data not found");

        JsonArray rows = data.get("row").getAsJsonArray();
        List<CustActMstEntity> custActMstEntities = new ArrayList<>();
        List<CustActDetEntity> custActDetEntities = new ArrayList<>();

        for (int i=1; i < rows.size()+1; i++) {
            JsonObject rowObject = rows.get(i - 1).getAsJsonObject();
            CustActMstEntityPK custActMstEntityPK = new CustActMstEntityPK();
            CustActDetEntityPK custActDetEntityPK = new CustActDetEntityPK();


            custActDetEntityPK.setCC2Code(helper.getNotJsonNullString(rowObject.get("c_c2code")));
            custActDetEntityPK.setCCustCode(helper.getNotJsonNullString(rowObject.get("c_code")));

            CustActMstEntity custActMstEntity = new CustActMstEntity();
            CustActDetEntity custActDetEntity = new CustActDetEntity();

            custActMstEntity.setCC2Code(helper.getNotJsonNullString(rowObject.get("c_c2code")));
            custActMstEntity.setCCode(helper.getNotJsonNullString(rowObject.get("c_code")));
            custActMstEntity.setNSchmSlab2("-");
            custActMstEntity.setNSchmSlab3("-");
            custActMstEntity.setNAutoCrAdjust(new BigDecimal(0));
            custActMstEntity.setNAutoDrAdjust(new BigDecimal(0));
            custActMstEntity.setCName(helper.getNotJsonNullString(rowObject.get("c_name")));
            custActMstEntity.setCGrpNo(helper.getNotJsonNullString(rowObject.get("c_grp_no")));
            custActMstEntity.setNMaxBillAmt(helper.getBigDecimal(rowObject.get("n_bill_limit")));

            custActMstEntity.setNCustomer(new BigDecimal(custFlag));
            custActMstEntity.setNSupplier(new BigDecimal(suppFlag));
            custActMstEntity.setNLock(helper.getBigDecimal(rowObject.get("n_lock")));
            custActMstEntity.setCCity(helper.getNotJsonNullString(rowObject.get("c_city")));
            custActMstEntity.setCPin(helper.getNotJsonNullString(rowObject.get("c_pin")));
            custActMstEntity.setCFax(helper.getNotJsonNullString(rowObject.get("c_fax")));
            custActMstEntity.setCAdd1(helper.getNotJsonNullString(rowObject.get("c_add_1")));
            custActMstEntity.setCAdd2(helper.getNotJsonNullString(rowObject.get("c_add_2")));
            custActMstEntity.setCAdd3(helper.getNotJsonNullString(rowObject.get("c_add_3")));
            custActMstEntity.setNAutoLock(helper.getBigDecimal(rowObject.get("n_autolock")));
            custActMstEntity.setCStNo(helper.getNotJsonNullString(rowObject.get("c_st_no")));
            custActMstEntity.setCCstNo(helper.getNotJsonNullString(rowObject.get("c_cst_no")));
            custActMstEntity.setCPanNo(helper.getNotJsonNullString(rowObject.get("c_pan_no")));
            custActMstEntity.setCTanNo(helper.getNotJsonNullString(rowObject.get("c_tan_no")));
            custActMstEntity.setNDebitDays(helper.getBigDecimal(rowObject.get("n_debit_days")));
            custActMstEntity.setCRemark(helper.getNotJsonNullString(rowObject.get("c_remark")));
            custActMstEntity.setCEmailId(helper.getNotJsonNullString(rowObject.get("c_email")));
            custActMstEntity.setCMobile(helper.getNotJsonNullString(rowObject.get("c_mobile")));
            custActMstEntity.setNPredefined(helper.getBigDecimal(rowObject.get("n_debit_days")));
            custActMstEntity.setCPhone1(helper.getNotJsonNullString(rowObject.get("c_phone_1")));
            custActMstEntity.setCPhone2(helper.getNotJsonNullString(rowObject.get("c_phone_2")));
            custActMstEntity.setNMaxItem(helper.getBigDecimal(rowObject.get("n_max_line_item")));
            custActMstEntity.setNGeoLat(helper.getNotJsonNullString(rowObject.get("c_geo_lat")));
            custActMstEntity.setNGeoLon(helper.getNotJsonNullString(rowObject.get("c_geo_lon")));
            custActMstEntity.setNCreditDays(helper.getBigDecimal(rowObject.get("n_credit_days")));
            custActMstEntity.setNDebitLimit(helper.getBigDecimal(rowObject.get("n_debit_limit")));
            custActMstEntity.setNCreditLimit(helper.getBigDecimal(rowObject.get("n_credit_limit")));
            custActMstEntity.setCShortName(helper.getNotJsonNullString(rowObject.get("c_sh_name")));
            custActMstEntity.setCBankCode(helper.getNotJsonNullString(rowObject.get("c_bank_code")));
            custActMstEntity.setCBrCode(helper.getNotJsonNullString(rowObject.get("c_branch_code")));
            custActMstEntity.setCMicrCode(helper.getNotJsonNullString(rowObject.get("c_micr_code")));
            custActMstEntity.setCAreaCode(helper.getNotJsonNullString(rowObject.get("c_area_code")));
            custActMstEntity.setCSmanCode(helper.getNotJsonNullString(rowObject.get("c_sman_code")));
            custActMstEntity.setNInterestRate(helper.getBigDecimal(rowObject.get("n_interest_rate")));
            custActMstEntity.setCSortOrder(helper.getNotJsonNullString(rowObject.get("c_sort_order")));
            custActMstEntity.setNMaxChqBounce(helper.getBigDecimal(rowObject.get("n_max_chq_bounce")));
            custActMstEntity.setCPrintName(helper.getNotJsonNullString(rowObject.get("c_print_name")));
            custActMstEntity.setCTallyhname(helper.getNotJsonNullString(rowObject.get("c_tallyhname")));
            custActMstEntity.setCTallygname(helper.getNotJsonNullString(rowObject.get("c_tallygname")));
            custActMstEntity.setCBankActNumber(helper.getNotJsonNullString(rowObject.get("c_bank_act_no")));
            custActMstEntity.setCContactPerson(helper.getNotJsonNullString(rowObject.get("c_contact_person")));
            custActMstEntity.setCDrugLicenceNo1(helper.getNotJsonNullString(rowObject.get("c_drug_licence_no_1")));
            custActMstEntity.setCDrugLicenceNo2(helper.getNotJsonNullString(rowObject.get("c_drug_licence_no_2")));
            custActMstEntity.setCCustCategoryCode(helper.getNotJsonNullString(rowObject.get("c_cust_category_code")));
            custActMstEntity.setDDate(helper.convertStringToDate(helper.getNotJsonNullString(rowObject.get("d_date"))));
            custActMstEntity.setDLdate(helper.convertStringToDate(helper.getNotJsonNullString(rowObject.get("d_ldate"))));
            custActMstEntity.setDDlDate(helper.convertStringToDate(helper.getNotJsonNullString(rowObject.get("d_dl_expiry_date"))));

            custActDetEntity.setId(custActDetEntityPK);
            custActDetEntity.setCGstNo(helper.getNotJsonNullString(rowObject.get("c_permanent_gstn_no")));
            custActDetEntity.setDLdate(helper.convertStringToDate(helper.getNotJsonNullString(rowObject.get("d_ldate"))));
            custActDetEntity.setDDate(helper.convertStringToDate(helper.getNotJsonNullString(rowObject.get("d_date"))));
            custActDetEntity.setTLtime(helper.getCurrentTimestamp());

            custActMstEntities.add(custActMstEntity);
            custActDetEntities.add(custActDetEntity);

            if (i % batchSize == 0 || i == rows.size()) {
                custActMstRepository.saveAll(custActMstEntities);
                custActMstEntities.clear();
                if (!custActDetEntities.isEmpty()) {
                    custActDetRepository.saveAll(custActDetEntities);
                    custActDetEntities.clear();
                }
                log.debug("batch -> {}", i);
            }
        }

    }




    @Override
    public void customerCreationAndMapping() {
        String nmC2Code = "05G000";
        JsonArray customerMappingDetails;
        do {
            customerMappingDetails = getCustomerMappingData(nmC2Code);
            for (JsonElement element : customerMappingDetails) {
                StringBuilder str = new StringBuilder();
                NmCustomerCreationLog nmLog = new NmCustomerCreationLog();
                JsonObject customerInfo = element.getAsJsonObject();
                String csqCustomerUCode = helper.getString(customerInfo.get("cSquareCustomerUCode"));
                NmCustomerCreationMasterBO customerCreationBO = getCustomerCreationDetails(customerInfo);
                JsonObject response = callCustomerCreationApi(customerCreationBO);
                nmLog.setRequest(customerCreationBO);
                nmLog.setDate(helper.getCurrentDate());
                nmLog.setRequestTimeStamp(helper.getCurrentTime());
                if (response.get("returnCode").getAsInt() == 1000) {
                    JsonObject data = response.get("returnData").getAsJsonObject();
                    String netmedsPartyId = helper.getString(data.get("partyId"));
                    customerMappingCreation.customerMappingCreation(netmedsPartyId, nmC2Code, csqCustomerUCode);
                    nmLog.setStatus("S");
                    nmLog.setNmCustomerId(netmedsPartyId);
                } else {
                    nmLog.setStatus("F");
                }
                nmLog.setResponseTimeStamp(helper.getCurrentTime());
                nmLog.setResponse(helper.fromJson(response, Object.class));
                nmLog.setCsqCustomerCode(csqCustomerUCode);
                nmLog.setId(str.append(helper.getString(customerInfo.get("cSquareCustomerCode"))).append("-")
                        .append(csqCustomerUCode).toString());
                nmCustomerCreationRepository.save(nmLog);
            }
            log.info("unmapped customer size {} ", customerMappingDetails.size());
        } while (customerMappingDetails.size() == maxSizelimit);
    }

    @Override
    public void egCustomerCreationAndMapping() {
        String egC2Code = "700000";
        JsonArray customerMappingDetails;
        do {
            customerMappingDetails = getCustomerMappingData(egC2Code);
            for (JsonElement element : customerMappingDetails) {
                StringBuilder str = new StringBuilder();
                JsonObject customerInfo = element.getAsJsonObject();
                String csqCustomerUCode = helper.getString(customerInfo.get("cSquareCustomerUCode"));
                EgCustomerCreationBO customerCreationBO = getEgCustomerCreationDetails(customerInfo);
                if (customerCreationBO.getDlNo1() != null) {
                    JsonObject response = callEgCustomerCreationApi(customerCreationBO);
                    EgCustomerCreationLog egCustomerCreationLog = new EgCustomerCreationLog();
                    egCustomerCreationLog.setRequest(customerCreationBO);
                    egCustomerCreationLog.setDate(helper.getCurrentDate());
                    egCustomerCreationLog.setRequestTimeStamp(helper.getCurrentTime());
                    if (helper.getInt(response.get("appStatusCode")) == 0) {
                        JsonObject data = response.get("payloadJson").getAsJsonObject();
                        String egCustomerId = helper.getString(data.get("code"));
                        String uCode = helper.getString(customerInfo.get("cSquareCustomerUCode"));
                        customerMappingCreation.customerMappingCreation(egCustomerId, egC2Code, uCode);
                        egCustomerCreationLog.setStatus("S");
                        egCustomerCreationLog.setEgCustomerId(egCustomerId);
                    } else {
                        egCustomerCreationLog.setStatus("F");
                    }
                    egCustomerCreationLog.setResponseTimeStamp(helper.getCurrentTime());
                    egCustomerCreationLog.setResponse(helper.fromJson(response, Object.class));
                    egCustomerCreationLog.setCsqCustomerCode(csqCustomerUCode);
                    egCustomerCreationLog.setId(str.append(helper.getString(customerInfo.get("cSquareCustomerCode"))).append("-")
                            .append(csqCustomerUCode).toString());
                    egCustomerCreationRepository.save(egCustomerCreationLog);
                }
            }
        } while (customerMappingDetails.size() == maxSizelimit);
    }



    private JsonObject callCustomerCreationApi(NmCustomerCreationMasterBO customerCreationBO) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic Q1NxdWFyZUIyQjozVjlAa1JoektyM1FqSTgw");
        return helper.fromJson(this.callWebClientPostSyncApiWithHeader(customerCreationApiEndPoint, helper.toJson(customerCreationBO), headers), JsonObject.class);
    }

    private JsonObject callEgCustomerCreationApi(EgCustomerCreationBO customerCreationBO) {
        //TODO please changes RRL api endpoint in prod
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic Q1NxdWFyZUIyQjozVjlAa1JoektyM1FqSTgw");
        JsonObject egRequestBody = new JsonObject();
        egRequestBody.addProperty("c2_code","700000");
        egRequestBody.addProperty("br_code","0000");
        egRequestBody.add("data",helper.fromJson(helper.toJson(customerCreationBO), JsonObject.class));
        return helper.fromJson(this.callWebClientPostSyncApiWithHeader(egCustomerCreationApiEndPoint,
                helper.toJson(egRequestBody), headers), JsonObject.class);
    }

    private NmCustomerCreationMasterBO getCustomerCreationDetails(JsonObject customerInfo) {

        String cSquareC2Code = helper.getString(customerInfo.get("cSquareC2Code"));
        String customerC2Code = helper.getString(customerInfo.get("cSquareCustomerCode"));
        String cSquareCustomerUCode = helper.getString(customerInfo.get("cSquareCustomerUCode"));

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
        query.setParameter("cSquareC2Code", cSquareC2Code);
        query.setParameter("cSquareCustomerCode", customerC2Code);
        List<Object[]> resultDataSet = this.getResultList(query);
        for (Object[] res : resultDataSet) {
            int i = -1;
            NmCustomerCreationDetailsBO nmCustomerCreationDetailsBO = new NmCustomerCreationDetailsBO();
            nmCustomerCreationMasterBO.setCustomerRefId(cSquareCustomerUCode);
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

    private JsonArray getCustomerMappingData(String c2code) {
        String cSquareC2Code = "03C000";
        String ecoliteStartingC2Code = "300000";
        String ecoliteEndingC2Code = "599999";
        String sql = " SELECT uscm.u_stockiest_cust_code as c2code, uscm.c_ucode as ucode " +
                " FROM u_stockiest_customer_map uscm LEFT JOIN u_stockiest_customer_map scm ON  " +
                " uscm.c_ucode = scm.c_ucode AND scm.c_stockiest_code = :c2code " +
                " WHERE uscm.c_stockiest_code = :cSquareC2Code AND uscm.u_stockiest_cust_code >= :ecoliteStartingC2Code " +
                " AND uscm.u_stockiest_cust_code <= :ecoliteEndingC2Code AND scm.c_ucode IS NULL ";
        Query query = this.getQuery(sql);
        query.setParameter("c2code", c2code);
        query.setParameter("cSquareC2Code", cSquareC2Code);
        query.setParameter("ecoliteStartingC2Code", ecoliteStartingC2Code);
        query.setParameter("ecoliteEndingC2Code", ecoliteEndingC2Code);
        List<Object[]> resultData = this.getResultList(query);
        JsonArray array = new JsonArray();
        for (Object[] res : resultData) {
            JsonObject object = new JsonObject();
            object.addProperty("c2code", c2code);
            object.addProperty("cSquareC2Code", cSquareC2Code);
            object.addProperty("cSquareCustomerCode", helper.getString(res[0]));
            object.addProperty("cSquareCustomerUCode", helper.getString(res[1]));
            array.add(object);
        }
        return array;
    }

    private EgCustomerCreationBO getEgCustomerCreationDetails(JsonObject customerInfo) {

        String cSquareC2Code = helper.getString(customerInfo.get("cSquareC2Code"));
        String customerC2Code = helper.getString(customerInfo.get("cSquareCustomerCode"));
        String cSquareCustomerUCode = helper.getString(customerInfo.get("cSquareCustomerUCode"));

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
        query.setParameter("cSquareCustomerCode", customerC2Code);
        List<Object[]> resultDataSet = this.getResultList(query);
        for (Object[] res : resultDataSet) {
            int i = -1;
            egCustomerCreationBO.setUCode(cSquareCustomerUCode);
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



}
