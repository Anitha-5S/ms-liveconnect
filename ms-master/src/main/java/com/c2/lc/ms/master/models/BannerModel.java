package com.c2.lc.ms.master.models;

import com.c2.lc.ms.master.utils.MsMessages;
import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("banner")
public class BannerModel {

    @Expose(serialize = false,deserialize = false)
    @Id
    private String id;

    @NotEmpty(message = MsMessages.VALIDATE_IMAGE_DATA)
    @SerializedName("c_file_data")
    @Field("c_file_data")
    private String fileData;

    @SerializedName("c_image_type")
    @Field("c_image_type")
    private String imageType;

    @SerializedName("c_height_width")
    @Field("c_height_width")
    private String heightWidth;

    @SerializedName("c_type")
    @Field("c_type")
    private String type;

    @SerializedName("c_platform")
    @Field("c_platform")
    private String platform;

    @SerializedName("c_file_name")
    @Field("c_file_name")
    private String fileName;

    @SerializedName("c_banner_image_url")
    @Field("c_banner_image_url")
    private String awsUrl;

    @SerializedName("c_redirect_url")
    @Field("c_redirect_url")
    private String redirectUrl;

    @SerializedName("j_body_payload")
    @Field("j_body_payload")
    private String payload;

    @SerializedName("c_state")
    @Field("c_state")
    private String state;

    @SerializedName("c_city")
    @Field("c_city")
    private String city;

    @SerializedName("c_created_at")
    @Field("c_created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @SerializedName("c_created_by")
    @Field("c_created_by")
    private String createdBy;

    @Expose(serialize = false)
    @SerializedName("c_c2code")
    @Field("c_c2code")
    private String c2Code;

    @SerializedName("n_banner_type")
    @Field("n_banner_type")
    private int bannerType;

    @SerializedName("c_offer_type")
    @Field("c_offer_type")
    private String offerType;

    @SerializedName("c_banner_title")
    @Field("c_banner_title")
    private String bannerTitle;

    @SerializedName("c_display_name")
    @Field("c_display_name")
    private String displayName;

    @SerializedName("c_deal_option")
    @Field("c_deal_option")
    private String dealOption;

    @SerializedName("n_banner_temp_id")
    @Field("n_banner_temp_id")
    private int bannerTempId;

    @SerializedName("c_content_align")
    @Field("c_content_align")
    private String contentAlign;

    @SerializedName("c_banner_content")
    @Field("c_banner_desc")
    private String bannerDesc;

    @SerializedName("d_start_date")
    @Field("d_start_date")
    private String startDate;

    @SerializedName("d_end_date")
    @Field("d_end_date")
    private String endDate;

    @SerializedName("n_banner_status")
    @Field("n_banner_status")
    private int status;

    @SerializedName("c_item_code")
    @Field("c_item_code")
    private String itemCode;

    @SerializedName("c_item_name")
    @Field("c_item_name")
    private String itemName;

    @SerializedName("n_item_qty")
    @Field("n_item_qty")
    private int itemQty;

    @SerializedName("c_item_free_qty")
    @Field("c_item_free_qty")
    private String itemFreeQty;

    @SerializedName("n_bill_value")
    @Field("n_bill_value")
    private BigDecimal billValue;

    @SerializedName("n_discount_amount")
    @Field("n_discount_amount")
    private BigDecimal discAmount;

    @SerializedName("c_discount_type")
    @Field("c_discount_type")
    private String discType;

    @SerializedName("n_discount_percentage")
    @Field("n_discount_percentage")
    private BigDecimal discPercentage;

    @SerializedName("j_dealwise_details")
    @Field("j_dealwise_details")
    private List<BannerDealWiseModel> dealWiseDetails;
}
