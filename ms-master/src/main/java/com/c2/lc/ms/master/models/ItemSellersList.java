package com.c2.lc.ms.master.models;


import com.c2.lc.lib.utils.Constants;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemSellersList implements Serializable {
    private static final long serialVersionUID = -39542885895673130L;

    @SerializedName("c_seller_code")
    private String c_seller_code = Constants.EMPTY_STRING;

    @SerializedName("c_seller_name")
    private String c_seller_name = Constants.EMPTY_STRING;

    @SerializedName("c_seller_item_code")
    private String c_seller_item_code = Constants.EMPTY_STRING;

    @SerializedName("n_mrp")
    private Double n_mrp = Constants.DOUBLE_VALUE_ZERO;

    @SerializedName("n_rate")
    private Double n_rate = Constants.DOUBLE_VALUE_ZERO;

    @SerializedName("c_scheme")
    private String c_scheme = Constants.EMPTY_STRING;

    @SerializedName("n_stock_qty")
    private long n_stock_qty = Constants.LONG_VALUE_ZERO;

    @SerializedName("n_in_out_flag")
    private int n_in_out_flag = Constants.INT_VALUE_ZERO;

    @SerializedName("c_cat_code")
    private String c_cat_code = Constants.EMPTY_STRING;

    @SerializedName("d_scheme_max_value")
    private Double d_scheme_max_value = Constants.DOUBLE_VALUE_ZERO;

    @SerializedName("c_seller_image_url")
    private List<String> cSellerImageUrl = new ArrayList<>();

    @SerializedName("c_seller_thumbnail_url")
    private List<String> cSellerThumbnailUrl = new ArrayList<>();

    @SerializedName("c_seller_web_url")
    private List<String> cSellerWebUrl = new ArrayList<>();

}
