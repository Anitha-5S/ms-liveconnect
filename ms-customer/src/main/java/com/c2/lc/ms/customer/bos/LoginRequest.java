package com.c2.lc.ms.customer.bos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class LoginRequest implements Serializable {

    private static final long serialVersionUID = -2148155584590416662L;

    @SerializedName("c_mobile_no")
    @NotEmpty(message = "'c_mobile_no' can't be empty")
    private String mobileNumber;

    @SerializedName("c_pwd")
    @NotEmpty(message = "'c_pwd' can't be empty")
    private String password;

    @SerializedName("c_type")
//    @NotEmpty(message = "c_type can't be empty")
    private String type;

    @SerializedName("c_device_token")
    @NotEmpty(message = "'c_device_token' can't be empty")
    private String deviceToken;
}
