package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.lib.db.DateAudit;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ts_setting_detail")
public class TSSettingDetailEntity extends DateAudit implements Serializable {

    @SerializedName("c_c2code")
    @Id
    @Column(name = "c_c2code", unique = true, nullable = false)
    private String c2Code;

    @SerializedName("c_br_code")
    @NotEmpty(message = "'c_br_code' can not be empty!")
    @Column(name = "c_br_code")
    private String cBrcode;

    @Column(name = "c_store_name", nullable = false, length = 256)
    @SerializedName("c_store_name")
    @NotEmpty(message = "'c_store_name' can not be empty!")
    private String storeName;

    @Column(name = "c_color_code", nullable = false, length = 10)
    @SerializedName("c_color_code")
    @NotEmpty(message = "'c_color_code' can not be empty!")
    private String colorCode;

    @Column(name = "c_cod_status", nullable = false, length = 10)
    @SerializedName("c_cod_status")
    @NotEmpty(message = "'c_cod_status' can not be empty!")
    private String codStatus;

    @Column(name = "c_online_trans_status", nullable = false, length = 10)
    @SerializedName("c_online_trans_status")
    @NotEmpty(message = "'c_online_trans_status' can not be empty!")
    private String onlineTransStatus;

    @Column(name = "c_font_name", nullable = false, length = 10)
    @SerializedName("c_font_name")
    @NotEmpty(message = "'c_font_name' can not be empty!")
    private String fontName;

    @Column(name = "c_coupon_option", nullable = false, length = 10)
    @SerializedName("c_coupon_option")
    @NotEmpty(message = "'c_coupon_option' can not be empty!")
    private String couponOption;

    @Column(name = "c_store_online_status", nullable = false, length = 10)
    @SerializedName("c_store_online_status")
    @NotEmpty(message = "'c_store_online_status' can not be empty!")
    private String storeOnlineStatus;

    @Column(name = "c_banner_option", nullable = false, length = 10)
    @SerializedName("c_banner_option")
    @NotEmpty(message = "'c_banner_option' can not be empty!")
    private String bannerStatus;

    @Column(name = "c_promotion_option", nullable = false, length = 10)
    @SerializedName("c_promotion_option")
    @NotEmpty(message = "'c_promotion_option' can not be empty!")
    private String promotionOption;

    @Column(name = "c_refer_to_earn_option", nullable = false, length = 10)
    @SerializedName("c_refer_to_earn_option")
    @NotEmpty(message = "'c_refer_to_earn_option' can not be empty!")
    private String referToEarnOption;

    @Column(name = "c_delivery_option", nullable = false, length = 10)
    @SerializedName("c_delivery_option")
    @NotEmpty(message = "'c_delivery_option' can not be empty!")
    private String deliveryOption;

    @Column(name = "c_prescription_option", nullable = false, length = 10)
    @SerializedName("c_prescription_option")
    @NotEmpty(message = "'c_prescription_option' can not be empty!")
    private String presOption;

    @Column(name = "c_reorder_option", nullable = false, length = 10)
    @SerializedName("c_reorder_option")
    @NotEmpty(message = "'c_reorder_option' can not be empty!")
    private String reorderStatus;

    @Column(name = "c_call_to_order_option", nullable = false, length = 10)
    @SerializedName("c_call_to_order_option")
    private String callToOrderOption;

    @Column(name = "c_call_to_order_mobileno", nullable = false, length = 10)
    @SerializedName("c_call_to_order_mobileno")
    private String callToOrderMob;

    @Column(name = "n_default_item_page_limit", nullable = false, length = 10)
    @SerializedName("n_default_item_page_limit")
    private int defaultItemPage;

    @Column(name = "c_app_logo_img", nullable = false, length = 1024)
    @SerializedName("c_app_logo_img")
    @NotEmpty(message = "'c_app_logo_img' can not be empty!")
    private String appLogoImg;

    public TSSettingDetailEntity() {}

    public TSSettingDetailEntity(Long userId, LocalDateTime time) {}

}
