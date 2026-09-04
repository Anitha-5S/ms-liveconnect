package com.c2.lc.ms.customer.bos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
public class StoreCombineRequestBO implements Serializable {

    @SerializedName("c_mobile_no")
    @Size(min = 10, max = 10, message = "Mobile number should be 10 digit")
    private String mobileNo;

    @SerializedName("c_combine_store_list")
    private List<String> storeList;

    @SerializedName("c_drug_license_list")
    private List<String> drugList;
}
