package com.c2.lc.ms.master.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
//these field are from u_item_mst, will consiered as it in query where clause
@Data
public class ItemRequest {

    @SerializedName("c_item_pack_code")
    private String packCode;
    @SerializedName("c_item_mfac_code")
    private String mfacCode;
    @SerializedName("c_item_cont_code")
    private String contCode;
    @SerializedName("c_item_brand_code")
    private String brandCode;
    @SerializedName("c_item_cat_code")
    private String catCode;
    @SerializedName("c_pack_type_code")
    private String packTypeCode;
}
