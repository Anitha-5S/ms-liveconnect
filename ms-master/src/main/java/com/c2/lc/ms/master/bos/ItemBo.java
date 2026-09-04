package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.utils.Constants;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ItemBo implements Serializable {

    @SerializedName("c_item_code")
    private String itemCode;

    @SerializedName("c_item_ucode")
    private String itemUCode;

    @SerializedName("c_item_name")
    private String itemName ;

    @SerializedName("c_mfg_code")
    private String mfgCode;

    @SerializedName("c_mfg_name")
    private String mfgName ;

    @SerializedName("n_mrp")
    private BigDecimal mrp;

    @SerializedName("c_pack_name")
    private String packName;

    @SerializedName("c_variant_count")
    private String variantCount = Constants.STATUS_NO;

    @SerializedName("c_sponsored")
    private String Sponsored = Constants.STATUS_NO;
}
