package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.utils.Constants;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LcOrder {

    @Id
    @SerializedName("c_order_id")
    @Field("c_order_id")
    private String orderId;

    @Indexed
    @SerializedName("c_source_ref_no")
    @Field("c_source_ref_no")
    private String sourceRef;

    @Indexed
    @SerializedName("c_trans_no")
    @Field("c_trans_no")
    private String transNo;


    @Indexed
    @SerializedName("c_user_id")
    @Field("c_user_id")
    private String userId;

    @Indexed
    @SerializedName("n_firm_id")
    @Field("n_firm_id")
    private String firmId;

    @Indexed
    @SerializedName("n_branch_id")
    @Field("n_branch_id")
    private String branchId;

    @SerializedName("c_mobile_no")
    @Field("c_mobile_no")
    private String mobileNo;

    @Indexed
    @SerializedName("c_cust_code")
    @Field("c_cust_code")
    private String custCode;

    @Indexed
    @SerializedName("c_combine_code")
    @Field("c_combine_code")
    private String combineCode;

    @SerializedName("n_delivery_branch_id")
    @Field("n_delivery_branch_id")
    private long deliveryBranchId;

    @SerializedName("j_summary")
    @Field("j_summary")
    private LcOrderSummaryBO orderSummary;

    @SerializedName("j_order_details")
    @Field("j_order_details")
    private LcOrderDetailsBO orderDetails;

    @SerializedName("c_payment_status")
    @Field("c_payment_status")
    private String paymentStatus ;

    @SerializedName("c_seller_name")
    @Field("c_seller_name")
    private String sellerName;

    @SerializedName("d_order_date")
    @Field("d_order_date")
    private LocalDate orderDate ;

    @SerializedName("c_order_from")
    @Field("c_order_from")
    private String orderFrom = Constants.EMPTY_STRING;

    @SerializedName("c_discount_type")
    @Field("c_discount_type")
    private String discountType;

    @SerializedName("n_discount_percentage")
    @Field("n_discount_percentage")
    private BigDecimal discountPercentage;

    @SerializedName("n_discount_amount")
    @Field("n_discount_amount")
    private BigDecimal discountAmount;

    @SerializedName("j_prescription_details")
    @Field("j_prescription_details")
    private PrescriptionBO prescriptionBo = new PrescriptionBO();

    @SerializedName("j_ts_customer_details")
    @Field("j_ts_customer_details")
    private TsCustomerDetailsBo tsCustomerDetailsBo = new TsCustomerDetailsBo();

    @SerializedName("j_lc_coupon_details")
    @Field("j_lc_coupon_details")
    private LcCouponDetailBO lcCouponDetailBO = new LcCouponDetailBO();

}
