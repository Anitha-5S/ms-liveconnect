package com.c2.lc.ms.master.entities.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Document("lc_cart")
public class LcCart {


    @Id
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

    @SerializedName("c_net_gst")
    @Field("c_net_gst")
    public String netGst;

    @SerializedName("c_net_amount")
    @Field("c_net_amount")
    public String netAmount;

    @SerializedName("n_order_to_branch_id")
    @Field("n_order_to_branch_id")
    public long orderToBranchId;

    @SerializedName("n_deliver_to_branch_id")
    @Field("n_deliver_to_branch_id")
    public long deliverToBranchId;

    @SerializedName("j_supplier")
    @Field("j_supplier")
    public List<SellerInfo> supplier;



}
