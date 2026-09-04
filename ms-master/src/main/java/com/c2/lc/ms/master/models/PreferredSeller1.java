package com.c2.lc.ms.master.models;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class PreferredSeller1 implements Serializable {

    @SerializedName("n_page")
    @NotEmpty(message = "Page number can not be empty!")
    private int page;

    @SerializedName("n_limit")
    @NotEmpty(message = "Limit can not be empty!")
    private int size;

    @SerializedName("c_pincode")
    @NotEmpty(message = "Pincode can not be empty!")
    private String pincode;

    @SerializedName("c_mobile_no")
    @NotEmpty(message = "Mobile no can not be empty!")
    private String mobileNumber;
}
