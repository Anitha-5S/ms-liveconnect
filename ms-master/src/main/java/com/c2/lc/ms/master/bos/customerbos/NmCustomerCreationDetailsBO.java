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
public class NmCustomerCreationDetailsBO extends BaseBO implements Serializable {

    @SerializedName("c_city")
    private String city;

    @SerializedName("c_country")
    private String country;

    @SerializedName("c_pincode")
    @Size(message = MsMessages.VALIDATE_PIN_LENGTH, max = 6)
    private String pincode;

    @SerializedName("c_email_id")
    private String emailId;

    @SerializedName("c_state_name")
    private String stateName;

    @SerializedName("c_mobile_mum1")
    @Size(message = MsMessages.VALIDATE_MOBILE_LENGTH, max = 10)
    private String mobileNum1;

    @SerializedName("c_contact_name")
    private String contactName;

    @SerializedName("c_address_line1")
    private String addressLine1;

    @SerializedName("c_address_line2")
    private String addressLine2;

    @SerializedName("c_address_line3")
    private String addressLine3;

    @SerializedName("c_address_line4")
    private String addressLine4;

}
