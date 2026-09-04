package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.utils.Constants;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TsCustomerDetailsBo {

    @SerializedName("c_customer_name")
    private String customerName = Constants.EMPTY_STRING;

    @SerializedName("c_cust_mobile_no")
    private String custMobile= Constants.EMPTY_STRING;

    @SerializedName("c_pincode")
    private String pincode= Constants.EMPTY_STRING;

    @SerializedName("dt_delivery_date")
    private LocalDateTime deliveryDate = null;

    @SerializedName("c_delivery_address")
    private String deliveryAddress = Constants.EMPTY_STRING ;

   // @SerializedName("n_discount_amount")
    //private BigDecimal retDiscountAmount= new BigDecimal("0.0");

    //@SerializedName("n_discount_percentage")
    //private BigDecimal retDiscountPercentage= new BigDecimal("0.0");

    @SerializedName("n_total_amount")
    private BigDecimal retailerTotalAmount= new BigDecimal("0.0");


}
