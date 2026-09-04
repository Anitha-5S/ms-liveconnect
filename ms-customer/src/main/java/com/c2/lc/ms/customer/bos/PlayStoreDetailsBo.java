package com.c2.lc.ms.customer.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Data
@AllArgsConstructor
public class PlayStoreDetailsBo  {

    @NotBlank(message = "c_application_id is mandatory")
    @SerializedName("c_application_id")
    private String AppId;

    @SerializedName("c_shop_name")
    @NotBlank(message = "c_shop_name is mandatory")
    @Size(max = 255)
    private String ShopName;

    @NotBlank(message = "c_email is mandatory")
    @SerializedName("c_email")
    @Size(max = 255)
    @Email(message ="c_email must be a well-formed email address", regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    private String Email;

    @NotBlank(message = "c_sc_email is mandatory")
    @SerializedName("c_sc_email")
    @Size(max = 255)
    @Email(message ="c_sc_email must be a well-formed email address", regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    private String SecEmail;

    @NotBlank(message = "c_mobile_number is mandatory")
    @SerializedName("c_mobile_number")
    @Size(min = 10, max = 10, message = "Mobile number should be 10 digit")
    private String MobileNumber;

    @NotBlank(message = "c_location is mandatory")
    @SerializedName("c_location")
    @Size(max = 255)
    private String Location;

    @NotBlank(message = "c_state is mandatory")
    @SerializedName("c_state")
    @Size(max = 255)
    private String State;

    @SerializedName("c_app_icon")
    @Size(max= 255)
    private String AppIcon;

    @Valid
    @NotNull(message = "n_app_version_code cannot be empty")
    @SerializedName("n_app_version_code")
    private Long AppVersionCode;

    @NotBlank(message = "c_app_version_name is mandatory")
    @SerializedName("c_app_version_name")
    private String AppVersionName;

    @NotBlank(message = "c_key_file is mandatory")
    @SerializedName("c_key_file")
    private String KeyFIle;

    @SerializedName("c_apk")
    private String Apk;

    @SerializedName("c_bundle")
    private String Bundle;

    @SerializedName("c_p12_file")
    private String P12File;

    @SerializedName("c_application_name")
    private String ApplicationName;

}
