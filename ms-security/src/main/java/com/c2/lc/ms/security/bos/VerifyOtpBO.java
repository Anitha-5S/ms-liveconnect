package com.c2.lc.ms.security.bos;

import com.c2.lc.lib.properties.Messages;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class VerifyOtpBO implements Serializable {

    private static final long serialVersionUID = 7620048306968022433L;

    @SerializedName("c_mobile_no")
    @Size(message = Messages.VALIDATE_MOBILE_LENGTH, min = 10, max = 10)
    private String mobileNumber;

    @SerializedName("OTP")
    private int OTP;
}
