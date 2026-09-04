package com.c2.lc.ms.master.entities.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemBo {

    @SerializedName("c_cart_code")
    @Field("c_cart_code")
    public String cartCode;
    @SerializedName("c_item_code")
    @Field("c_item_code")
    public String itemCode;
    @SerializedName("c_item_name")
    @Field("c_item_name")
    public String itemName;
    @SerializedName("c_seller_code")
    @Field("c_seller_item_code")
    public String sellerItemCode;
    @SerializedName("ac_thumbnail_images")
    @Field("ac_thumbnail_images")
    public String thumbnailImages;
    @SerializedName("c_pack_name")
    @Field("c_pack_name")
    public String packName;
    @SerializedName("c_max_mrp")
    @Field("c_max_mrp")
    public String  maxMrp;
    @SerializedName("c_rate")
    @Field("c_rate")
    public String rate;
    @SerializedName("c_gst_percentage")
    @Field("c_gst_percentage")
    public String gstPercentage;
    @SerializedName("c_discount_percentage")
    @Field("c_discount_percentage")
    public String discountPercentage;

    @SerializedName("c_discount_amount")
    @Field("c_discount_amount")
    public String discountAmount;

    @SerializedName("c_scheme_qty")
    @Field("c_scheme_qty")
    public String schemeQty;

    @SerializedName("c_contain_name")
    @Field("c_contain_name")
    public String containName;
    @SerializedName("n_qty")
    @Field("n_qty")
    public int qty;

    @SerializedName("n_total")
    @Field("n_total")
    public String total;
    @SerializedName("n_gst_amount")
    @Field("n_gst_amount")
    public String gstAmount;
}
