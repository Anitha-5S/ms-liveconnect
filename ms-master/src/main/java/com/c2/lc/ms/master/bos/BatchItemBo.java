package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchItemBo {

    @NotEmpty(message = "'c_seller_code' can't be empty")
    @SerializedName("c_seller_code")
    private String sellerCode;

    @NotEmpty(message = "'c_item_code' can't be empty")
    @SerializedName("c_item_code")
    private String itemCode;

    @SerializedName("c_search_term")
    @Size(min = 3, max = 30, message = "'Search Term' has to be length {min} to {max} characters!")
    private String searchTerm;
}
