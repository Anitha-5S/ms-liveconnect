package com.c2.lc.ms.customer.bos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class FirmDetailsBO implements Serializable {

    @SerializedName("c_firm_name")
    @Size(max = 250)
    @NotNull
    private String firmName;

    @NotNull
    @SerializedName("c_mobile_no")
    @Size(min = 10, max = 10)
    private String mobileNo;

    @NotNull
    @SerializedName("c_pincode")
    @Size(min = 6, max = 6)
    private String pinCode;

    @SerializedName("c_firm_img")
    private String firmImage;

    @SerializedName("c_email_id")
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

    @SerializedName("c_contact_person_name")
    private String contactName;

    @SerializedName("c_address_no1")
    @NotNull
    private String address1;

    @SerializedName("c_address_no2")
    private String address2;

    @SerializedName("c_state_code")
    @NotNull
    private String stateCode;

    @SerializedName("c_state_name")
    @NotNull
    private String stateName;

    @SerializedName("c_city_code")
    @NotNull
    private String cityCode;

    @SerializedName("c_city_name")
    @NotNull
    private String cityName;

    @SerializedName("c_area_code")
    @NotNull
    private String areaCode;

    @SerializedName("c_area_name")
    @NotNull
    private String areaName;

    @SerializedName("c_landmark")
    private String cLandmark;

}
