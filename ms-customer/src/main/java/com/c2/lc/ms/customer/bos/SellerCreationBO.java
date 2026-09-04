package com.c2.lc.ms.customer.bos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class SellerCreationBO implements Serializable {

    @SerializedName("c_firm_name")
    @Size(max = 250)
    @NotNull(message = "firm name cannot be null")
    private String firmName;

    @NotNull(message = "mobile cannot be null")
    @SerializedName("c_mobile_no")
    @Size(min = 10, max = 10)
    private String mobileNo;

    @NotNull(message = "pin code cannot be null")
    @SerializedName("c_pincode")
    @Size(min = 6, max = 6)
    private String pinCode;

    @SerializedName("c_drug_license_no")
    @Size(max = 32)
    private String drugLicenseNo1;

    @SerializedName("c_gst_number")
    @Size(max = 32)
    private String gstNumber;

    @SerializedName("c_contact_person_name")
    private String contactName;

    @SerializedName("c_state_code")
    private String stateCode;

    @SerializedName("c_state_name")
    private String stateName;

    @SerializedName("c_city_code")
    private String cityCode;

    @SerializedName("c_city_name")
    private String cityName;

    @SerializedName("c_type")
    @NotNull(message = "c_type cannot be null")
    private String cType;

}
