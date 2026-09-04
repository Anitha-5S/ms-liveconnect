package com.c2.lc.ms.customer.utils;

import com.c2.lc.ms.customer.entities.customer.FirmEntity;
import com.google.gson.JsonObject;

import java.time.LocalDate;

public class Utils {

    public static JsonObject getFirmEntityJsonObject(FirmEntity firmEntity) {
        JsonObject firmEntityJsonObject = new JsonObject();

//        firmEntityJsonObject.addProperty("c_type", firmEntity.getCType() == null ? "" : firmEntity.getCType());
        firmEntityJsonObject.addProperty("c_druglicense_no1", firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo1() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo1());
        firmEntityJsonObject.addProperty("c_druglicense_no1_img",firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo1Img() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo1Img());
        firmEntityJsonObject.addProperty("c_drug_license_no1_expiry_date",firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getDDrugLicenseNo1ExpiryDate() == null ? "" : firmEntity.getLegalIdentities().getDDrugLicenseNo1ExpiryDate().toString());
        firmEntityJsonObject.addProperty("c_druglicense_no2",firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo2() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo2());
        firmEntityJsonObject.addProperty("c_druglicense_no2_img",firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo2Img() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo2Img());
        firmEntityJsonObject.addProperty("c_drug_license_no2_expiry_date",firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getDDrugLicenseNo2ExpiryDate() == null ? "" : firmEntity.getLegalIdentities().getDDrugLicenseNo2ExpiryDate().toString());
        firmEntityJsonObject.addProperty("c_druglicense_no3",firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo3() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo3());
        firmEntityJsonObject.addProperty("c_druglicense_no3_img",firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo3Img() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo3Img());
        firmEntityJsonObject.addProperty("c_drug_license_no3_expiry_date",firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getDDrugLicenseNo3ExpiryDate() == null ? "" : firmEntity.getLegalIdentities().getDDrugLicenseNo3ExpiryDate().toString());
        firmEntityJsonObject.addProperty("c_email",firmEntity.getContactDetail() == null ? "" : (firmEntity.getContactDetail().getCEmailId() == null ? "" : firmEntity.getContactDetail().getCEmailId()));
        firmEntityJsonObject.addProperty("c_firm_address1",firmEntity.getContactDetail() == null ? "" : (firmEntity.getContactDetail().getCAddress1() == null ? "" : firmEntity.getContactDetail().getCAddress1()));
        firmEntityJsonObject.addProperty("c_firm_address2",firmEntity.getContactDetail() == null ? "" : (firmEntity.getContactDetail().getCAddress2() == null ? "" : firmEntity.getContactDetail().getCAddress2()));
        firmEntityJsonObject.addProperty("c_firm_contact_person",firmEntity.getContactDetail() == null ? "" : (firmEntity.getContactDetail().getCContactName() == null ? "" : firmEntity.getContactDetail().getCContactName()));
        firmEntityJsonObject.addProperty("c_gst_no",firmEntity.getCGstNo() == null ? "" : firmEntity.getCGstNo());
        firmEntityJsonObject.addProperty("c_gst_type",firmEntity.getCGstType() == null ? "" : firmEntity.getCGstType());
        firmEntityJsonObject.addProperty("c_mobile_no",firmEntity.getCMobileNo() == null ? "" : firmEntity.getCMobileNo());
        firmEntityJsonObject.addProperty("c_name",firmEntity.getCName() == null ? "" : firmEntity.getCName());
        firmEntityJsonObject.addProperty("c_narcotic_no",firmEntity.getLegalIdentities() == null ? "" : (firmEntity.getLegalIdentities().getCNarcoticNo() == null ? "" : firmEntity.getLegalIdentities().getCNarcoticNo()));
        firmEntityJsonObject.addProperty("c_narcotic_no_img",firmEntity.getLegalIdentities() == null ? "" : (firmEntity.getLegalIdentities().getCNarcoticNoImg() == null ? "" : firmEntity.getLegalIdentities().getCNarcoticNoImg()));
        firmEntityJsonObject.addProperty("c_pincode",firmEntity.getCPin() == null ? "" : firmEntity.getCPin());
        firmEntityJsonObject.addProperty("c_state_name",firmEntity.getCStateName() == null ? "" : firmEntity.getCStateName());
        firmEntityJsonObject.addProperty("c_state_code",firmEntity.getCStateCode() == null ? "" : firmEntity.getCStateCode());
        firmEntityJsonObject.addProperty("c_city_name",firmEntity.getCCityName() == null ? "" : firmEntity.getCCityName());
        firmEntityJsonObject.addProperty("c_city_code",firmEntity.getCCityCode() == null ? "" : firmEntity.getCCityCode());
        firmEntityJsonObject.addProperty("c_area_name",firmEntity.getCAreaName() == null ? "" : firmEntity.getCAreaName());
        firmEntityJsonObject.addProperty("c_area_code",firmEntity.getCAreaCode() == null ? "" : firmEntity.getCAreaCode());
        firmEntityJsonObject.addProperty("c_landmark",firmEntity.getContactDetail() == null ? "" : (firmEntity.getContactDetail().getCLandmark() == null ? "" : firmEntity.getContactDetail().getCLandmark()));
        firmEntityJsonObject.addProperty("c_status",firmEntity.getCStatus() == null ? "" : firmEntity.getCStatus());
        firmEntityJsonObject.addProperty("c_image_url",firmEntity.getCImageUrl() == null ? "" : firmEntity.getCImageUrl());
        firmEntityJsonObject.addProperty("c_tan_no",firmEntity.getDocumentDetail() == null ? "" : (firmEntity.getDocumentDetail().getCTanNo() == null ? "" : firmEntity.getDocumentDetail().getCTanNo()));
        firmEntityJsonObject.addProperty("c_tan_no_img",firmEntity.getDocumentDetail() == null ? "" : (firmEntity.getDocumentDetail().getCTanNoImg() == null ? "" : firmEntity.getDocumentDetail().getCTanNoImg()));
        firmEntityJsonObject.addProperty("c_pan_no",firmEntity.getDocumentDetail() == null ? "" : (firmEntity.getDocumentDetail().getCPanNo() == null ? "" : firmEntity.getDocumentDetail().getCPanNo()));
        firmEntityJsonObject.addProperty("c_pan_no_img",firmEntity.getDocumentDetail() == null ? "" : (firmEntity.getDocumentDetail().getCPanNoImg() == null ? "" : firmEntity.getDocumentDetail().getCPanNoImg()));
        firmEntityJsonObject.addProperty("c_it_pan_no",firmEntity.getDocumentDetail() == null ? "" : (firmEntity.getDocumentDetail().getCItPanNo() == null ? "" : firmEntity.getDocumentDetail().getCItPanNo()));
        firmEntityJsonObject.addProperty("c_it_pan_no_img",firmEntity.getDocumentDetail() == null ? "" : (firmEntity.getDocumentDetail().getCItPanNoImg() == null ? "" : firmEntity.getDocumentDetail().getCItPanNoImg()));
        firmEntityJsonObject.addProperty("c_electricity_bill",firmEntity.getDocumentDetail() == null ? "" : (firmEntity.getDocumentDetail().getCElectricityBill() == null ? "" : firmEntity.getDocumentDetail().getCElectricityBill()));
        firmEntityJsonObject.addProperty("c_electricity_bill_img",firmEntity.getDocumentDetail() == null ? "" : (firmEntity.getDocumentDetail().getCElectricityBillImg() == null ? "" : firmEntity.getDocumentDetail().getCElectricityBillImg()));
        firmEntityJsonObject.addProperty("c_rent_agreement",firmEntity.getDocumentDetail() == null ? "" : (firmEntity.getDocumentDetail().getCRentAgreement() == null ? "" : firmEntity.getDocumentDetail().getCRentAgreement()));
        firmEntityJsonObject.addProperty("c_rent_agreement_img",firmEntity.getDocumentDetail() == null ? "" : (firmEntity.getDocumentDetail().getCRentAgreementImg() == null ? "" : firmEntity.getDocumentDetail().getCRentAgreementImg()));
        firmEntityJsonObject.addProperty("c_partnership_deed",firmEntity.getDocumentDetail() == null ? "" : (firmEntity.getDocumentDetail().getCPartnershipDeed() == null ? "" : firmEntity.getDocumentDetail().getCPartnershipDeed()));
        firmEntityJsonObject.addProperty("c_partnership_deed_img",firmEntity.getDocumentDetail() == null ? "" : (firmEntity.getDocumentDetail().getCPartnershipDeedImg() == null ? "" : firmEntity.getDocumentDetail().getCPartnershipDeedImg()));
        firmEntityJsonObject.addProperty("c_bank_statement",firmEntity.getDocumentDetail() == null ? "" : (firmEntity.getDocumentDetail().getCBankStatement() == null ? "" : firmEntity.getDocumentDetail().getCBankStatement()));
        firmEntityJsonObject.addProperty("c_bank_statement_img",firmEntity.getDocumentDetail() == null ? "" : (firmEntity.getDocumentDetail().getCBankStatementImg() == null ? "" : firmEntity.getDocumentDetail().getCBankStatementImg()));
        firmEntityJsonObject.addProperty("c_authority_letter",firmEntity.getDocumentDetail() == null ? "" : (firmEntity.getDocumentDetail().getCAuthorityLetter() == null ? "" : firmEntity.getDocumentDetail().getCAuthorityLetter()));
        firmEntityJsonObject.addProperty("c_authority_letter_img",firmEntity.getDocumentDetail() == null ? "" : (firmEntity.getDocumentDetail().getCAuthorityLetterImg() == null ? "" : firmEntity.getDocumentDetail().getCAuthorityLetterImg()));



        return firmEntityJsonObject;
    }
}
