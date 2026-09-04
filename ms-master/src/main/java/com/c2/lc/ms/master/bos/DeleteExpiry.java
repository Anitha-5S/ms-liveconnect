package com.c2.lc.ms.master.bos;


import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteExpiry {

    @NotEmpty(message = "'c_seller_code' can't be empty")
    @SerializedName("c_seller_code")
    private String sellerCode;

    @NotEmpty(message = "'c_buyer_code' can't be empty")
    @SerializedName("c_buyer_code")
    private String buyer;

    @NotEmpty(message = "'c_item_code' can't be empty")
    @SerializedName("c_item_code")
    private String itemCode;

    @SerializedName("d_expiry_date")
    private LocalDate expiryDate;

    @NotEmpty(message = "'c_batch_name' can't be empty")
    @SerializedName("c_batch_name")
    private String batchName;

    @SerializedName("n_mrp")
    private double mrp;
}
