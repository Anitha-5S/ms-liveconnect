package com.c2.lc.ms.customer.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
public class AddressBo {
    @NotBlank(message = "c_add_type is mandatory")
    @SerializedName("c_add_type")
    private String AddressType;

    @NotBlank
    @SerializedName("c_add_1")
    @Size( max = 255)
    private String Address1;

    @NotBlank
    @SerializedName("c_add_2")
    @Size(max = 255)
    private String Address2;

    @NotBlank(message = "c_landmark is mandatory")
    @SerializedName("c_landmark")
    @Size(max = 255)
    private String Landmark;

    @NotBlank(message = "c_state is mandatory")
    @SerializedName("c_state")
    @Size(max = 255)
    private String StateName;

    @NotBlank(message = "c_city is mandatory")
    @SerializedName("c_city")
    @Size(max = 255)
    private String CityName;

    @NotBlank(message = "c_pincode is mandatory")
    @SerializedName("c_pincode")
    @Size(min = 6, max = 6, message="c_pincode should be 6 digits only.")
    private String Pincode;

    @NotBlank(message = "c_customer_name is mandatory")
    @SerializedName("c_customer_name")
    @Size(max= 255)
    private String CustomerName;

    @NotBlank
    @SerializedName("c_mobile_no")
    @Size(min = 10, max = 10, message = "Mobile number should be 10 digit")
    private String MobileNo;

    @SerializedName("c_add_id")
    private long AddressId;

    @SerializedName("c_extra_customer_name")
    @Size(max= 255)
    private String AdrdressName;

}
