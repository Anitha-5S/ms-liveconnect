package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ContactBO {

    @SerializedName("c_address_1")
    private String address1;

    @SerializedName("c_address_2")
    private String address2;

    @SerializedName("c_alternative_email_id")
    private String alternativeEmailId;

    @SerializedName("c_alternative_phone_no")
    private String alternativePhoneNo;

    @SerializedName("c_city")
    private String city;

    @SerializedName("c_contact_name")
    private String contactName;

    @SerializedName("c_country")
    private String country;

    @SerializedName("c_email_id")
    private String emailId;

    @SerializedName("c_mobile_no")
    private String mobileNo;

    @SerializedName("c_note")
    private String note;

    @SerializedName("c_phone_no")
    private String phoneNo;

    @SerializedName("c_pin")
    private String pin;

    @SerializedName("c_state")
    private String state;

}
