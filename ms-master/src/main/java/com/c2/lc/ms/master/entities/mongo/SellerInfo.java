package com.c2.lc.ms.master.entities.mongo;


import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SellerInfo {


    @SerializedName("c_cart_code")
    @Field("c_cart_code")
    public String cartCode;

    @SerializedName("n_user_id")
    @Field("n_user_id")
    public long userId;

    @SerializedName("n_firm_id")
    @Field("n_firm_id")
    public long firmId;

    @SerializedName("n_branch_id")
    @Field("n_branch_id")
    public long branchId;

    @SerializedName("n_order_to_branch_id")
    @Field("n_order_to_branch_id")
    public long orderToBranchId;

    @SerializedName("c_seller_code")
    @Field("c_seller_code")
    public String sellerCode;

    @SerializedName("c_seller_name")
    @Field("c_seller_name")
    public String sellerName;

    @SerializedName("c_gst_amount")
    @Field("c_gst_amount")
    public double gstAmount;

    @SerializedName("c_cart_total_amount")
    @Field("c_cart_total_amount")
    public double cartTotalAmount;

    @SerializedName("c_total_amount")
    @Field("c_total_amount")
    public double totalAmount;

    @SerializedName("c_br_code")
    @Field("c_br_code")
    public long brCode;

    @SerializedName("c_seller_mapped_status")
    @Field("c_seller_mapped_status")
    public String sellerMappedStatus;

    @SerializedName("c_seller_wise_cart_count")
    @Field("c_seller_wise_cart_count")
    public int sellerWiseCartCount;

    @SerializedName("j_items")
    @Field("j_items")
    public List<CartItemBo> items;

    @SerializedName("d_created_date")
    @Field("d_created_date")
    @CreatedDate
    public LocalDate createdDate;

    @SerializedName("d_updated_date")
    @Field("d_updated_date")
    @CreatedDate
    public LocalDate updatedDate;

}
