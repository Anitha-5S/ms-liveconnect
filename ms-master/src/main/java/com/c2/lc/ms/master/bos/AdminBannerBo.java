package com.c2.lc.ms.master.bos;

import com.c2.lc.ms.master.models.BannerDealWiseModel;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminBannerBo {

    @NotBlank(message = "'c_offer_type' should not be empty!")
    @SerializedName("c_offer_type")
    private String offerType;

    @NotBlank(message = "'c_banner_title' should not be empty!")
    @Size(max = 100, message = "'c_banner_title' should not be more than 100 characters!")
    @SerializedName("c_banner_title")
    private String bannerTitle;

    @SerializedName("c_display_name")
    private String displayName;

    @SerializedName("c_deal_option")
    private String dealOption;

    @SerializedName("n_banner_temp_id")
    private int bannerTempId;

    @SerializedName("c_content_align")
    private String contentAlign;

    @SerializedName("c_banner_content")
    private String bannerDesc;

    @SerializedName("c_item_code")
    private String itemCode;

    @SerializedName("c_item_name")
    private String itemName;

    @SerializedName("n_item_qty")
    private int itemQty;

    @SerializedName("c_item_free_qty")
    private String itemFreeQty;

    @SerializedName("n_bill_value")
    private BigDecimal billValue;

    @SerializedName("n_discount_amount")
    private BigDecimal discAmount;

    @SerializedName("c_discount_type")
    private String discType;

    @SerializedName("n_discount_percentage")
    private BigDecimal discPercentage;

    @SerializedName("j_dealwise_details")
    private List<BannerDealWiseModel> dealWiseDetails;

    @NotBlank(message = "'d_start_date' should not be empty!")
    @SerializedName("d_start_date")
    private String startDate;

    @NotBlank(message = "'d_end_date' should not be empty!")
    @SerializedName("d_end_date")
    private String endDate;

    @SerializedName("n_banner_status")
    @Min(value = 0, message = "n_banner_status should not be less than 0")
    @Max(value = 1, message = "n_banner_status should not be greater than 1")
    private int status;

    @SerializedName("c_banner_id")
    private String bannerId;

    @SerializedName("c_banner_img")
    private String bannerURL;



}
