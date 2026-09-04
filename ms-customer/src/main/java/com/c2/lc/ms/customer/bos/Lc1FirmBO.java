package com.c2.lc.ms.customer.bos;

import com.c2.lc.lib.db.DateAudit;
import com.c2.lc.ms.customer.entities.customer.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lc1FirmBO implements Serializable {

    @SerializedName("c_firm_name")
    @Size(max = 250)
    private String firmName;

    @SerializedName("c_br_code")
    private String branchCode;

    @SerializedName("c_mobile_no")
    @Size(min = 10, max = 10, message = "Mobile number should be 10 digit")
    private String mobileNo;

    @SerializedName("c_pincode")
    @Size(min = 6, max = 6, message = "Pincode should be 6 digit")
    private String pinCode;

    @SerializedName("c_gst_number")
    @Size(max = 32)
    private String gstNumber;

    @SerializedName("c_address_no1")
    @Size(max = 1024)
    private String address1;

    @SerializedName("c_address_no2")
    @Size(max = 1024)
    private String address2;

    @SerializedName("c_address_no3")
    @Size(max = 1024)
    private String address3;

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

    @SerializedName("c_drug_license_no1")
    @Size(max = 32)
    private String drugLicenseNo1;

    @SerializedName("c_drug_license_no2")
    @Size(max = 32)
    private String drugLicenseNo2;

    @SerializedName("c_drug_license_no3")
    @Size(max = 32)
    private String drugLicenseNo3;

    @SerializedName("c_seller_code")
    private String sellerCode;

    @SerializedName("c_seller_name")
    private String sellerName;
}