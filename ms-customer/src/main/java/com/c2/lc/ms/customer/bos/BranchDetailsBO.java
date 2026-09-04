package com.c2.lc.ms.customer.bos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
public class BranchDetailsBO implements Serializable {

    @SerializedName("c_firm_name")
    @Size(max = 250)
    private String firmName;

    @SerializedName("c_br_code")
    private String branchCode;

    @SerializedName("c_mobile_no")
    @Size(min = 10, max = 10, message = "Mobile number should be 10 digit")
    private String mobileNo;

    @NotNull
    @SerializedName("c_pincode")
    @Size(min = 6, max = 6, message = "Pincode should be 6 digit")
    private String pinCode;

    @SerializedName("c_firm_img")
    private String firmImage;

    @SerializedName("c_email")
    @Email(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    private String emailId;

    @SerializedName("c_drug_license_no1")
    @Size(max = 32)
    private String drugLicenseNo1;

    @SerializedName("c_drug_license_no1_img")
    private String drugLicenseNo1Img;

    @SerializedName("c_drug_license_no1_expiry_date")
    private String drugLicenseNo1ExpiryDate;

    @SerializedName("c_drug_license_no2")
    @Size(max = 32)
    private String drugLicenseNo2;

    @SerializedName("c_drug_license_no2_img")
    private String drugLicenseNo2img;

    @SerializedName("c_drug_license_no2_expiry_date")
    private String drugLicenseNo2ExpiryDate;

    @SerializedName("c_drug_license_no3")
    @Size(max = 32)
    private String drugLicenseNo3;

    @SerializedName("c_drug_license_no3_img")
    private String drugLicenseNo3img;

    @SerializedName("c_drug_license_no3_expiry_date")
    private String drugLicenseNo3ExpiryDate;

    @SerializedName("c_gst_type")
    private String gstType;

    @SerializedName("c_gst_number")
    @Size(max = 32)
    private String gstNumber;

    @SerializedName("c_narcotic_no")
    @Size(max = 32)
    private String narcoticNo;

    @SerializedName("c_narcotic_no_img")
    private String narcoticImg;

    @SerializedName("c_tan_no")
    @Size(max = 32)
    private String tanNo;

    @SerializedName("c_tan_no_img")
    private String tanNoImg;

    @SerializedName("c_pan_no")
    @Size(max = 32)
    private String panNo;

    @SerializedName("c_pan_no_img")
    private String panImg;

    @SerializedName("c_it_pan_no")
    @Size(max = 32)
    private String itPanNo;

    @SerializedName("c_it_pan_no_img")
    private String itPanNoImg;

    @SerializedName("c_electricity_bill")
    @Size(max = 32)
    private String electricityBill;

    @SerializedName("c_electricity_bill_img")
    private String electricityBillImg;

    @SerializedName("c_rent_agreement")
    @Size(max = 32)
    private String rentAgreement;

    @SerializedName("c_rent_agreement_img")
    private String rentAgreementImg;

    @SerializedName("c_partnership_deed")
    @Size(max = 32)
    private String partnershipDeed;

    @SerializedName("c_partnership_deed_img")
    private String partnershipDeedImg;

    @SerializedName("c_bank_statement")
    @Size(max = 32)
    private String bankStatement;

    @SerializedName("c_bank_statement_img")
    private String bankStatementImg;

    @SerializedName("c_authority_letter")
    @Size(max = 32)
    private String authLetter;

    @SerializedName("c_authority_letter_img")
    private String authLetterImg;

    @SerializedName("c_contact_person_name")
    @Size(max = 255, message = "c_contact_person_name size must be between 0 and 255")
    private String contactName;

    @SerializedName("c_address_no1")
    @Size(max = 1024)
    private String address1;

    @SerializedName("c_address_no2")
    @Size(max = 1024)
    private String address2;

    @SerializedName("c_state_code")
    @Size(max = 10)
    private String stateCode;

    @SerializedName("c_state_name")
    @Size(max = 40)
    private String stateName;

    @SerializedName("c_city_code")
    @Size(max = 6)
    private String cityCode;

    @SerializedName("c_city_name")
    @Size(max = 40)
    private String cityName;

    @SerializedName("c_area_code")
    @Size(max = 6)
    private String areaCode;

    @SerializedName("c_area_name")
    @Size(max = 40)
    private String areaName;

    @SerializedName("c_landmark")
    private String cLandmark;

    @SerializedName("c_type")
    private String cType;

}
