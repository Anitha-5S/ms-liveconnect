package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchBo {

    @NotEmpty(message = "'c_seller_code' can't be empty")
    @SerializedName("c_seller_code")
    private String sellerCode;

    @NotEmpty(message = "'c_item_code' can't be empty")
    @SerializedName("c_item_code")
    private String itemCode;

    @NotEmpty(message = "'c_batch_name' can't be empty")
    @SerializedName("c_batch_name")
    private String batch;

   // @Min(value = 1, message = "n_mrp should not be less than 1")
    @SerializedName("n_mrp")
    private String mrp;

    @NotEmpty(message = "'d_expiry_date' can't be empty")
    @SerializedName("d_expiry_date")
    private String expiryDate;

}
