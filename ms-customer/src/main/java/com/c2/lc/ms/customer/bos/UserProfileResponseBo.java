package com.c2.lc.ms.customer.bos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class UserProfileResponseBo {
    @SerializedName("c_cust_code")
    private String custCode;

    @SerializedName("c_cust_name")
    private String customerName;

    @SerializedName("d_date_of_birth")
    private String dateOfBirth;

    @SerializedName("c_mobile_no")
    private String MobileNo;

    @SerializedName("c_email")
    private String email;

    @SerializedName("c_profile_image")
    private String profileImage;

    @SerializedName("c_gender")
    private String gender;

    @SerializedName("c_pin_code")
    private String pinCode;

    @SerializedName("n_firm_id")
    private Long firmId;

    @SerializedName("c_br_code")
    private String brCode;

    @SerializedName("c_service_active_status")
    private String serviceActiveStatus;

}
