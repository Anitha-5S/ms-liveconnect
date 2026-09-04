package com.c2.lc.ms.customer.bos;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserModelBO {

    @SerializedName("n_user_id")
    private Long nUserId;

    @SerializedName("c_mobile_no")
    @Size(min = 10, max = 10, message = "Mobile number should be 10 digit")
    private String cMobileNo;

    @SerializedName("c_name")
    private String cName;

    @SerializedName("c_email")
    @Email(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    private String cEmail;

    @SerializedName("c_address_1")
    private String cAddress1;

    @SerializedName("c_address_2")
    private String cAddress2;

    @SerializedName("c_area_name")
    private String cAreaName;

    @SerializedName("c_city_name")
    private String cCityName;

    @SerializedName("c_state_name")
    private String cStateName;

    @SerializedName("c_area_code")
    private String cAreaCode;

    @SerializedName("c_city_code")
    private String cCityCode;

    @SerializedName("c_state_code")
    private String cStateCode;

    @SerializedName("c_pincode")
    @Size(min = 6, max = 6, message = "Pincode should be 6 digit")
    private String cPincode;

    @SerializedName("j_role")
    private JsonArray userRoles;
}