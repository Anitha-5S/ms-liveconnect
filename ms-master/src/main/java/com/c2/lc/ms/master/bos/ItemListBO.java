package com.c2.lc.ms.master.bos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
public class ItemListBO implements Serializable {

    @Id
    @JsonProperty("objectID")
    private String objectId;

    @SerializedName("c_item_code")
    private String itemCode;

    @SerializedName("c_item_name")
    private String itemName;

    @SerializedName("c_packing_size")
    private String packageSize;

    @SerializedName("c_csquare_item_code")
    private String cSquareItemCode;

    @SerializedName("c_csquare_item_name")
    private String cSquareItemName;

    @SerializedName("c_code")
    private String cCode;

    @SerializedName("c_name")
    private String cName;

    @SerializedName("c_csquare_code")
    private String cSquareCode;

    @SerializedName("c_csquare_name")
    private String cSquareName;

    @SerializedName("c_type_code")
    private String cTypeCode;

    @SerializedName("c_filtertype_name")
    private String cFilterTypeName;

}
