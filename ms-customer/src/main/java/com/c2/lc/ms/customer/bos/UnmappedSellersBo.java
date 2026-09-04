package com.c2.lc.ms.customer.bos;

import com.c2.lc.lib.bo.NextPageBO;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnmappedSellersBo {

    @SerializedName("data")
    private List<SellerDetailBO> sellerDetails;

    @SerializedName("page")
    private NextPageBO nextPage;
}
