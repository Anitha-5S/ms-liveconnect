package com.c2.lc.ms.customer.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListBranchModelBO {

    @SerializedName("n_branch_id")
    private Long nBranchId;

    @SerializedName("c_name")
    private String cName;

    @SerializedName("c_city_name")
    private String cCityName;

    @SerializedName("c_area_name")
    private String cAreaName;

    @SerializedName("c_landmark")
    private String cLandmark;

    @SerializedName("c_pincode")
    private String cPincode;

    @SerializedName("c_image_url")
    private String cImageUrl;
}
