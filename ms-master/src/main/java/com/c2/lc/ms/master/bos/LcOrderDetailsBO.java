package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.utils.Constants;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LcOrderDetailsBO {

    @SerializedName("c_seller_code")
    private String sellerCode = Constants.EMPTY_STRING;

    @SerializedName("c_seller_name")
    private String sellerName= Constants.EMPTY_STRING;;

    @SerializedName("c_seller_address")
    private String sellerAddress= Constants.EMPTY_STRING;;

    @SerializedName("c_order_id")
    private String orderId= Constants.EMPTY_STRING;;

    @Indexed
    @SerializedName("ac_invoice_date")
    private List<LocalDate> invoiceDate= new ArrayList<>();

    @SerializedName("ac_invoice_number")
    private List<String> invoiceNumber= new ArrayList<>();

    @SerializedName("c_payment_type")
    private String paymentType= Constants.EMPTY_STRING;;

    @SerializedName("n_total_amount")
    private BigDecimal totalAmount = new BigDecimal("0.0");

    @SerializedName("n_amount_paid")
    private BigDecimal amountPaid = new BigDecimal("0.0");

    @SerializedName("n_due_amount")
    private BigDecimal dueAmount = new BigDecimal("0.0");

    @SerializedName("d_payment_due_date")
    private LocalDate paymentDueDate= null;

    @SerializedName("a_order_items")
    private List<LcOrderItemBO> orderItems=new ArrayList<>();

    @SerializedName("n_sub_total")
    private BigDecimal subTotal= new BigDecimal("0.0");

    @SerializedName("n_discount_amount")
    private BigDecimal cashDiscount= new BigDecimal("0.0");

    @SerializedName("n_gst_amount")
    private BigDecimal gstAmount= new BigDecimal("0.0");

    public String getSellerCode() {
        return sellerCode;
    }

    public void setSellerCode(String sellerCode) {
        this.sellerCode = sellerCode;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }

    public LocalDate getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(LocalDate paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public List<LcOrderItemBO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<LcOrderItemBO> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getCashDiscount() {
        return cashDiscount;
    }

    public void setCashDiscount(BigDecimal cashDiscount) {
        this.cashDiscount = cashDiscount;
    }

    public BigDecimal getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(BigDecimal gstAmount) {
        this.gstAmount = gstAmount;
    }
}
