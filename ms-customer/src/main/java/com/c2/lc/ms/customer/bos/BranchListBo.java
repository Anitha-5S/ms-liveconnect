package com.c2.lc.ms.customer.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchListBo {

    @SerializedName("c_br_code")
    private String branchCode;

    @SerializedName("n_firm_id")
    private Long firmId;

    @SerializedName("c_br_name")
    private String branchName;

    @SerializedName("c_city_name")
    private String cCityName;

    @SerializedName("c_pincode")
    private String cPincode;

    @SerializedName("c_default_status")
    private String defaultStatus;
}
