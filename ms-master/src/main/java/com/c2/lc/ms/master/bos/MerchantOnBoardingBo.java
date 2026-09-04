package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantOnBoardingBo {

    @SerializedName("c2_code")
    private String c2Code;

    @SerializedName("merchant_id")
    private String merchantId;

    @SerializedName("merchant_name")
    private String merchantName;

    @SerializedName("merchant_address_line1")
    private String merchantAddressLine1;

    @SerializedName("merchant_address_line2")
    private String merchantAddressLine2;

    @SerializedName("merchant_address_line3")
    private String merchantAddressLine3;

    @SerializedName("merchant_city")
    private String merchantCity;

    @SerializedName("merchant_pin")
    private String merchantPin;

    @SerializedName("merchant_lat")
    private String merchantLat;

    @SerializedName("merchant_long")
    private String merchantLong;

    @SerializedName("merchant_contact1")
    private String merchantContact;

    @SerializedName("merchant_contact2")
    private String merchantContact2;

    @SerializedName("merchant_email")
    private String merchantEmail;

    @SerializedName("merchant_gst_no")
    private String merchantGstNo;

    @SerializedName("merchant_pan_no")
    private String merchantPanNo;

    @SerializedName("merchant_drug_licence_no1")
    private String merchantDrugLicenceNo1;

    @SerializedName("merchant_drug_licence_no2")
    private String merchantDrugLicenceNo2;

    @SerializedName("merchant_created_date")
    private String merchantCreatedDate;

}
