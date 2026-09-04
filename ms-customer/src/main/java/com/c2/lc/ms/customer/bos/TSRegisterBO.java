package com.c2.lc.ms.customer.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TSRegisterBO implements Serializable {

    @SerializedName("c_mobile_no")
    @Size(min = 10, max = 10, message = "Mobile number should be 10 digit")
    @NotEmpty(message = "'c_mobile_no' can't be empty")
    private String mobileNumber;

    @SerializedName("c_name")
    @NotEmpty(message = "'c_name' can't be empty")
    private String name;

    @SerializedName("c_email")
    @Email(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @NotEmpty(message = "c_email can't be empty")
    private String email;

    @SerializedName("d_dob")
    @NotEmpty(message = "'d_dob' can't be empty")
    private String dateOfBirth;

    @SerializedName("c_c2code")
    @NotEmpty(message = "'c_c2code' can't be empty")
    private String c2Code;

    @SerializedName("c_gender")
    @NotEmpty(message = "'c_gender' can't be empty")
    private String gender;

    @SerializedName("c_pincode")
    @NotEmpty(message = "'c_pincode' can't be empty")
    private String pinCode;

    @SerializedName("n_firm_id")
    @Min(value = 1, message = "n_firm_id should not be less than 1")
    private Long firmId;

    @SerializedName("c_br_code")
    private String brCode;

    @SerializedName("c_service_active_status")
    private String serviceActiveStatus;
}
