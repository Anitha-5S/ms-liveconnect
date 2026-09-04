package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LcCouponDetailBO implements Serializable {

    @SerializedName("c_coupon_code")
    private String couponCode;

    @SerializedName("n_coupon_discount_amount")
    private BigDecimal discAmount;
}
