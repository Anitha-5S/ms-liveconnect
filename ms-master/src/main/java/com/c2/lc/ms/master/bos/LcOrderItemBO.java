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

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LcOrderItemBO {

    @SerializedName("c_item_code")
    private String itemCode = Constants.EMPTY_STRING;

    @SerializedName("c_seller_item_code")
    private String sellerItemCode = Constants.EMPTY_STRING;

    @SerializedName("c_item_name")
    private String itemName = Constants.EMPTY_STRING;

    @SerializedName("c_batch_no")
    private String batchNo = Constants.EMPTY_STRING;

    @SerializedName("d_expiry_date")
    private LocalDate expiryDate = null;

    @SerializedName("n_mrp")
    private BigDecimal mrp = new BigDecimal("0.0");

    @SerializedName("n_qty")
    private BigDecimal quantity = new BigDecimal("0.0");

    @SerializedName("n_scheme_qty")
    private BigDecimal schemeQuantity = new BigDecimal("0.0");

    @SerializedName("n_discount_percentage")
    private BigDecimal discountPercentage = new BigDecimal("0.0");

    @SerializedName("n_sale_rate")
    private BigDecimal saleRate = new BigDecimal("0.0");

    @SerializedName("c_gst_percentage")
    private String gstPercentage = "";

    @SerializedName("n_net_Amount")
    private BigDecimal netAmount = new BigDecimal("0.0");

    public LcOrderItemBO(String item) {
        this.itemCode = item;
    }

    @SerializedName("c_payment_status")
    private String paymentStatus = Constants.EMPTY_STRING;

    @SerializedName("c_seller_name")
    private String sellerName = Constants.STATUS_NO;

    @SerializedName("d_order_date")
    @Indexed
    private LocalDate orderDate = LocalDate.now();

    @SerializedName("c_brand_name")
    private String brandName = Constants.EMPTY_STRING;

    @SerializedName("c_mfg")
    private String mfg = Constants.EMPTY_STRING;

    @SerializedName("c_scheme")
    private String scheme = Constants.EMPTY_STRING;

    @SerializedName("c_cancellation_reason")
    private String reason = Constants.EMPTY_STRING;


}
