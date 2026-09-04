package com.c2.lc.ms.master.bos;


import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpireOrderIdBo {

    @NotEmpty(message = "'ac_cancel_flag' can't be empty..!")
    @SerializedName("ac_cancel_flag")
    private List<Integer> cancelFlags = new ArrayList<>();

    @NotEmpty(message = "'c_order_id' can't be empty..!")
    @SerializedName("c_order_id")
    private String orderId;

    @NotEmpty(message = "'c_order_id' can't be empty..!")
    @SerializedName("c_seller_code")
    private String sellerCode;



}
