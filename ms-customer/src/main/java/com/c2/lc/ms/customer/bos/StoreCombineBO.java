package com.c2.lc.ms.customer.bos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class StoreCombineBO implements Serializable {

    @SerializedName("c_firm_name")
    private String firmName;

    @SerializedName("c_br_code")
    private String brCode;

    @SerializedName("c_pincode")
    @Size(min = 6, max = 6, message = "Pincode should be 6 digit")
    private String pinCode;

    @SerializedName("c_drug_license_no")
    private String drugLicenseNo1;

    @SerializedName("c_gst_number")
    private String gstNumber;

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

    @SerializedName("c_seller_code")
    private String sellerCode;
    
    @SerializedName("c_seller_name")
    private String sellerName;

    @SerializedName("c_email_id")
    private String emailId;
}
