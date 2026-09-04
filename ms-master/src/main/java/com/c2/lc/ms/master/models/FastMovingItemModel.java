package com.c2.lc.ms.master.models;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class FastMovingItemModel implements Serializable {

    private static final long serialVersionUID = -959442883980638014L;
    @SerializedName("c_item_code")
    private String cItemcode;
    @SerializedName("c_item_name")
    private String cItemName;
    @SerializedName("c_pack_name")
    private String cPackName;
    @SerializedName("c_pack_type_name")
    private String cPackTypeName;
    @SerializedName("c_content_name")
    private String contentName;
    @SerializedName("n_max_mrp")
    private Double nMaxMrp;
    @SerializedName("c_gst_code")
    private String cGstCode;
    @SerializedName("c_image_link")
    private String cImageLink;

}
