package com.c2.lc.ms.master.bos.customerbos;

import com.c2.lc.lib.base.BaseBO;
import com.c2.lc.ms.master.utils.MsMessages;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.google.gson.annotations.SerializedName;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EgCustomerCreationBO extends BaseBO implements Serializable {

    @SerializedName("c_name")
    private String name;

    @SerializedName("c_city")
    private String city;

    @SerializedName("c_pan_no")
    private String panNo;

    @SerializedName("c_u_code")
    private String uCode;

    @SerializedName("c_gst_no")
    private String gstNo;

    @SerializedName("c_dl_no1")
    private String dlNo1;

    @SerializedName("c_dl_no2")
    private String dlNo2;

    @SerializedName("c_country")
    private String country;

    @SerializedName("c_pincode")
    @Size(message = MsMessages.VALIDATE_PIN_LENGTH, max = 6)
    private String pincode;

    @SerializedName("c_emailId")
    private String emailId;

    @SerializedName("c_mobileNo")
    @Size(message = MsMessages.VALIDATE_MOBILE_LENGTH, max = 10)
    private String mobileNo;

    @SerializedName("c_address1")
    private String address1;

    @SerializedName("c_address2")
    private String address2;

    @SerializedName("c_address3")
    private String address3;

    @SerializedName("c_short_name")
    private String shortName;

    @SerializedName("c_state_code")
    private String stateCode;

    @SerializedName("c_state_name")
    private String stateName;

    @SerializedName("c_contact_name")
    private String contactName;

}