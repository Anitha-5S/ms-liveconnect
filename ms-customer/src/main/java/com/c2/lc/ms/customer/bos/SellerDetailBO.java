package com.c2.lc.ms.customer.bos;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class SellerDetailBO implements Serializable {

    @SerializedName("c_seller_code")
    private String sellerCode;

    @SerializedName("c_mobile_no")
    @Size(min = 10, max = 10)
    private String mobileNo;

    @SerializedName("c_pincode")
    @Size(min = 6, max = 6)
    private String pinCode;

    @SerializedName("c_seller_name")
    private String sellerName;

    @SerializedName("c_drug_license_no")
    @Size(max = 32)
    private String drugLicenseNo;

    @SerializedName("c_drug_license_no_expiry_date")
    private String drugLicenseNoExpiryDate;

    @SerializedName("c_gst_type")
    private String gstType;

    @SerializedName("c_gst_number")
    @Size(max = 32)
    private String gstNumber;

    @SerializedName("c_contact_person_name")
    private String contactName;

    @SerializedName("c_address_no1")
    private String address1;

    @SerializedName("c_address_no2")
    private String address2;

    @SerializedName("c_state_code")
    private String stateCode;

    @SerializedName("c_state_name")
    private String stateName;

    @SerializedName("c_city_code")
    private String cityCode;

    @SerializedName("c_city_name")
    private String cityName;

    @SerializedName("c_area_code")
    private String areaCode;

    @SerializedName("c_area_name")
    private String areaName;

    @SerializedName("c_landmark")
    private String cLandmark;

    @SerializedName("j_email_id")
    private JsonArray jEmail;

    @SerializedName("n_seller_rate")
    private double sellerRate;

    @SerializedName("n_seller_stock")
    private int sellerStock;

}
