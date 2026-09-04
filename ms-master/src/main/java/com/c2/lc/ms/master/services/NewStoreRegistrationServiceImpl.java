package com.c2.lc.ms.master.services;


import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.MerchantOnBoardingBo;
import com.c2.lc.ms.master.entities.mysql.*;
import com.c2.lc.ms.master.repos.mysql.CustActDetRepository;
import com.c2.lc.ms.master.repos.mysql.CustActMstRepository;
import com.c2.lc.ms.master.repos.mysql.CustActMstRequestRepository;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.NewStoreRegistrationService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class NewStoreRegistrationServiceImpl extends MasterBaseServiceImpl implements NewStoreRegistrationService {
    @Autowired
    private CustActMstRepository custActMstRepository;

    @Autowired
    private CustActMstRequestRepository custActMstRequestRepository;

    @Autowired
    CustActDetRepository custActDetRepository;

    @Value("${merchant.onboarding.url}")
    private String merchantOnboardingUrl;
    @Value("${nm.store.registeration.url}")
    private String nmStoreRegistrationUrl;
    @Value("${nm.store.status.detail.url}")
    private String nmStoreStatusUrl;
    @Value("${nm.new.store.registeration.authorization}")
    private String newStoreRegiAuthentication;

    private String rilC2Coode = "700000";
    private String grabC2Code = "000800";
    private String nmC2Code = "05G000";


    @Override
    public void registerMerchant(Map<String, String> headers) {
        List<CustActMstEntity> unRegisteredBranches = custActMstRepository.unRegisteredBranches(rilC2Coode, grabC2Code);
        log.debug("List of unregistered branches " + unRegisteredBranches);
        unRegisteredBranches.forEach(branch -> {
            String response = null;
            try {
                JsonObject payload = getMerchantOnboardingPayload(branch);
                log.debug("call live order merchant register with grab API call- Request - {} - payload : {}", merchantOnboardingUrl, payload.toString());
                response = this.callWebClientPostSyncApiWithHeader(merchantOnboardingUrl, payload.toString(),headers);
                log.debug("call live order merchant register with grab API -- response - {} ", response);
                if ("0".equals(helper.getJsonObject(response).get("appStatusCode").getAsString())) {
                    JsonObject jsonBranch = helper.getJsonObject(branch);
                    CustActMstRequest custActMstRequest = helper.fromJson(jsonBranch, CustActMstRequest.class);
                    custActMstRequest.setDDate(helper.getCurrentDate());
                    custActMstRequest.setDLdate(helper.getCurrentDate());
                    custActMstRequest.getId().setCc2Code(grabC2Code);
                    custActMstRequest.getId().setCcode(branch.getCCode());
                    custActMstRequest.setCcommonCode(branch.getCCode());
                    custActMstRequestRepository.save(custActMstRequest);
                } else {
                    log.error("call live order merchant register with grab API got failed-- branch - {} - response : {}", branch.getCCode(), response);
                }
            } catch (Exception e) {
                log.error("error occurred while processing grab store registration for branch {} - response : {}", branch.getCCode(), response);
                e.printStackTrace();
            }
        });
    }

    public JsonObject getMerchantOnboardingPayload(CustActMstEntity CustActMst) {
        JsonObject merchantPayload = new JsonObject();
        merchantPayload.addProperty("merchant_id", CustActMst.getCCode());
        merchantPayload.addProperty("merchant_name", CustActMst.getCName());
        String address1 = helper.isEmpty(CustActMst.getCAdd1()) ? "" : CustActMst.getCAdd1();
        String address2 = helper.isEmpty(CustActMst.getCAdd2()) ? "" : "," + CustActMst.getCAdd2();
        String address3 = helper.isEmpty(CustActMst.getCAdd3()) ? "" : "," + CustActMst.getCAdd3();
        String city = helper.isEmpty(CustActMst.getCCity()) ? "" : "," + CustActMst.getCCity();
        String pin = CustActMst.getCPin();
        String address = address1 + address2 + address3 + city + " - " + pin;
        merchantPayload.addProperty("merchant_address", address);
        merchantPayload.addProperty("merchant_lat", CustActMst.getNGeoLat());
        merchantPayload.addProperty("merchant_long", CustActMst.getNGeoLon());
        merchantPayload.addProperty("merchant_contact", CustActMst.getCMobile());
        merchantPayload.addProperty("merchant_email", CustActMst.getCEmailId());
        merchantPayload.addProperty("merchant_type", 1);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
        merchantPayload.addProperty("dttm", helper.getCurrentTime().format(format));
        return merchantPayload;
    }

    public void saveMerchantStatus(JsonObject data) {

        JsonArray merchantArray = data.get("merchant_data").getAsJsonArray();
        log.debug("callback api request from grab payload : {}", data);
        merchantArray.forEach(merchant -> {
            String status = merchant.getAsJsonObject().get("errorCode").getAsString();
            if ("0".equals(status) || "809".equals(status)) {
                String merchantId = merchant.getAsJsonObject().get("merchant_id").getAsString();
                CustActMstRequest custActMstEco = custActMstRequestRepository.getCustActMstRequest(grabC2Code, merchant.getAsJsonObject().get("merchant_id").getAsString());
                if (!helper.isEmpty(custActMstEco)) {
                    try {
                        Optional<CustActMstEntity> custActMstEntity = custActMstRepository.findById(new CustActMstEntityPK(rilC2Coode, merchantId));
                        if (custActMstEntity.isPresent()) {
                            CustActMstEntity clonedBranch = custActMstEntity.get().clone();
                            clonedBranch.setCC2Code(grabC2Code);
                            clonedBranch.setCCode(merchant.getAsJsonObject().get("grab_merchant_id").getAsString());
                            clonedBranch.setCCommonCode(merchantId);
                            clonedBranch.setDDate(helper.getCurrentDate());
                            clonedBranch.setDLdate(helper.getCurrentDate());
                            custActMstRepository.save(clonedBranch);
                            custActMstRequestRepository.delete(custActMstEco);
                        }
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void storeRegistration() {
        List<CustActMstEntity> branchList = custActMstRepository.getNmUnregisteredBranches(rilC2Coode, nmC2Code);
        log.debug("branch list of netmeds unregister branches " + branchList);
        branchList.forEach(branch -> {
            String response = null;
            try {
                Optional<CustActDetEntity> custActDetEntity = custActDetRepository.findById(new CustActDetEntityPK(rilC2Coode, branch.getCCode()));
                JsonObject nmPayload = getNmAddStorePayload(branch, custActDetEntity);
                Map<String, String> apiHeader = new HashMap<>();
                apiHeader.put("Authorization", "Basic " + newStoreRegiAuthentication);
                log.debug("api call netmeds new store registration- Request - {} - payload : {}", nmStoreRegistrationUrl, nmPayload.toString());
                response = this.callWebClientPostSyncApiWithHeader(nmStoreRegistrationUrl, nmPayload.toString(), apiHeader);
                log.debug("api call netmeds new store registration- -- response - {} ", response);
                JsonObject jsonResponse = helper.getJsonObject(response);
                if (jsonResponse.get("response").getAsJsonObject().get("status").getAsBoolean()) {
                    try {
                        CustActMstEntity clonedBranch = branch.clone();
                        clonedBranch.setCC2Code(nmC2Code);
                        clonedBranch.setCCode(jsonResponse.get("response").getAsJsonObject().get("result").getAsJsonObject().get("netmedsCode").getAsString());
                        clonedBranch.setCCommonCode(branch.getCCode());
                        clonedBranch.setDDate(helper.getCurrentDate());
                        clonedBranch.setDLdate(helper.getCurrentDate());
                        custActMstRepository.save(clonedBranch);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                } else {
                    log.error("api call netmeds new store registration failed for branch - {} - response: {}", branch.getCCode(), response);
                }
            } catch (Exception e) {
                log.error("error occurred while calling nm store registration for branch {} - response: {}", branch.getCCode(), response);
                e.printStackTrace();
            }
        });
    }


    public JsonObject getNmAddStorePayload(CustActMstEntity custActMst, Optional<CustActDetEntity> custActDet) {
        JsonObject payload = new JsonObject();
        payload.addProperty("storeCode", "70" + custActMst.getCCode());
        payload.addProperty("storeName", custActMst.getCName());
        payload.addProperty("group", "");
        payload.addProperty("addressLine1", custActMst.getCAdd1());
        payload.addProperty("addressLine2", custActMst.getCAdd2());
        payload.addProperty("addressLine3", custActMst.getCAdd3());
        payload.addProperty("email", custActMst.getCEmailId());
        payload.addProperty("city", custActMst.getCCity());
        payload.addProperty("pincode", custActMst.getCPin());
        payload.addProperty("areaName", "");
        payload.addProperty("gstn", custActDet.isPresent() ? custActDet.get().getCGstNo() : "");
        payload.addProperty("dlNo1", custActMst.getCDrugLicenceNo1());
        payload.addProperty("dlNo2", custActMst.getCDrugLicenceNo2());
        return payload;
    }


    @Override
    public JsonObject getStoreDetails(String storeCode) throws Exception {
        Map<String, String> apiHeader = new HashMap<>();
        apiHeader.put("Authorization", "Basic " + newStoreRegiAuthentication);
        log.debug("api call netmeds store  registration  status details - Request - {} - {}", nmStoreStatusUrl + storeCode);
        String response = this.callWebClientGetSyncApi(nmStoreStatusUrl + storeCode, apiHeader);
        log.debug("api call netmeds store status details- -- response - {} ", response);
        JsonObject storeDetails = helper.getJsonObject(response);
        if (storeDetails.get("response").getAsJsonObject().get("status").getAsBoolean()) {
            return storeDetails.get("response").getAsJsonObject().get("result").getAsJsonObject();
        } else {
            log.error("api call netmeds store status details failed- -- response - {} ", response);
            throw new RecordNotFoundException(storeDetails.get("response").getAsJsonObject().get("message").getAsString());
        }

    }

    @Override
    public void merchantCreation(MerchantOnBoardingBo merchantOnBoardingBo){
        CustActMstEntity custActMstEntity = CustActMstEntity.builder()
                .cC2Code(merchantOnBoardingBo.getC2Code())
                .cCode(merchantOnBoardingBo.getMerchantId())
                .cName(merchantOnBoardingBo.getMerchantName())
                .cAdd1(merchantOnBoardingBo.getMerchantAddressLine1())
                .cAdd2(merchantOnBoardingBo.getMerchantAddressLine2())
                .cAdd3(merchantOnBoardingBo.getMerchantAddressLine3())
                .cCity(merchantOnBoardingBo.getMerchantCity())
                .cPin(merchantOnBoardingBo.getMerchantPin())
                .nGeoLat(merchantOnBoardingBo.getMerchantLat())
                .nGeoLon(merchantOnBoardingBo.getMerchantLong())
                .cPhone1(merchantOnBoardingBo.getMerchantContact())
                .cPhone2(merchantOnBoardingBo.getMerchantContact2())
                .cEmailId(merchantOnBoardingBo.getMerchantEmail())
                .cDrugLicenceNo1(merchantOnBoardingBo.getMerchantDrugLicenceNo1())
                .nAutoCrAdjust(new BigDecimal(0))
                .nAutoDrAdjust(new BigDecimal(0))
                .cGrpNo("")
                .nMaxBillAmt(new BigDecimal(0))
                .nSchmSlab2("")
                .nSchmSlab3("")
                .build();

        custActMstRepository.save(custActMstEntity);
    }
}
