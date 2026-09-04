package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpiryCart {

    @NotEmpty(message = "'c_seller_code' can't be empty")
    @SerializedName("c_seller_code")
    private String sellerCode;

    @NotEmpty(message = "'c_buyer_code' can't be empty")
    @SerializedName("c_buyer_code")
    private String buyer;

    @NotEmpty(message = "'c_item_code' can't be empty")
    @SerializedName("c_item_code")
    private String itemCode;

    @NotEmpty(message = "'c_mobile' can't be empty")
    @SerializedName("c_mobile")
    private String mobile;

    @Min(value = 1, message = "n_qty should not be less than 1")
    @SerializedName("n_qty")
    private int qty;

    @SerializedName("n_loose_qty")
    private int looseQty;

    @NotEmpty(message = "'c_batch_name' can't be empty")
    @SerializedName("c_batch_name")
    private String batchName;

    @SerializedName("n_mrp")
    private double mrp;

    @SerializedName("n_discount_price")
    private double discountPrice;

    @NotEmpty(message = "'c_pack_size' can't be empty")
    @SerializedName("c_pack_size")
    private String packSize;

    @SerializedName("n_scheme")
    private String scheme;

    @SerializedName("d_expiry_date")
    private LocalDate expiryDate;

}
