package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.utils.Constants;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

@Data
public class SellerBo implements Serializable {

    @SerializedName("c_seller_code")
    private String sellerCode;

    @SerializedName("c_seller_name")
    private String sellerName;

    @SerializedName("c_seller_city")
    private String sellerCity;

    @SerializedName("n_schemes")
    private int scheme = Constants.INT_VALUE_ZERO;

    @SerializedName("c_sponsored")
    private String sponsored = Constants.STATUS_INACTIVE;

}
