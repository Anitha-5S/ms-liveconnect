package com.c2.lc.ms.security.bos;

import com.c2.lc.ms.security.configs.MsMessages;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class ChangePasswordBO implements Serializable {

    private static final long serialVersionUID = -4646250854692739806L;

    @SerializedName("c_mobile_no")
    @Size(message = MsMessages.VALIDATE_MOBILE_LENGTH, min = 10, max = 10)
    @NotEmpty(message = "Mobile number Can Not be Empty!")
    private String mobileNumber;

    @SerializedName("c_type")
    @NotEmpty(message = "Type  Can Not be Empty!")
    private String type;

    @SerializedName("OTP")
    @NotNull(message = "OTP can not be Empty")
    private int OTP;

    @NotEmpty(message = "New Password Cant Not Be Empty!")
    @SerializedName("c_new_pwd")
    private String newPassword;

}
