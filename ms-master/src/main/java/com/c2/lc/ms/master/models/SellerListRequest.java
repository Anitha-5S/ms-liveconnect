package com.c2.lc.ms.master.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class SellerListRequest implements Serializable {

    private static final long serialVersionUID = 1556021492974305257L;

    @SerializedName("c_buyer_code")
    private String buyerCode;

    @SerializedName("c_mobile_no")
    private String mobileNo;

    @SerializedName("c_item_ucode")
    private String itemUCode;
}
