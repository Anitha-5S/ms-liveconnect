package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.utils.Constants;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LcOrderSummaryBO {

    @SerializedName("c_seller_logo_url")
    private String sellerLogoUrl = Constants.EMPTY_STRING;

    @SerializedName("c_seller_code")
    private String sellerCode ;

    @SerializedName("c_seller_name")
    private String sellerName = Constants.EMPTY_STRING;

    @SerializedName("c_order_id")
    private String orderId = Constants.EMPTY_STRING;

    @SerializedName("c_customer_name")
    private String customerName = Constants.EMPTY_STRING;

    @SerializedName("c_buyer_area")
    private String buyerArea = Constants.EMPTY_STRING;

    @SerializedName("d_order_date")
    private LocalDateTime orderDate = LocalDateTime.now();

    @SerializedName("n_line_items")
    private int noOfLineItems = Constants.INT_VALUE_ZERO;

    @SerializedName("n_outstanding_amount")
    private BigDecimal outstandingAmount = new BigDecimal("0.0");

    @SerializedName("c_order_status")
    private String orderStatus = Constants.EMPTY_STRING;

    @SerializedName("n_transaction_id")
    private long transactionId ;


    @Indexed
    @SerializedName("d_due_delivery")
    private LocalDate dueDeliveryDat = LocalDate.now();

    public LcOrderSummaryBO(String name) {
        this.sellerName = name;
    }

}
