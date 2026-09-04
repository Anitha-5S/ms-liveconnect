package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.base.BaseBO;
import com.c2.lc.lib.utils.Constants;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemPLPResponseBO extends BaseBO {

    @SerializedName("c_item_code")
    @Field("c_item_code")
    private String itemCode;

    @SerializedName("c_seller_item_code")
    @Field("c_seller_item_code")
    private String sellerItemCode;

    @SerializedName("c_item_name")
    @Field("c_item_name")
    private String itemName ;

    @SerializedName("c_pack_name")
    @Field("c_pack_name")
    private String packName;

    @SerializedName("c_mfg_code")
    @Field("c_mfg_code")
    private String mfgCode;

    @SerializedName("c_mfg_name")
    @Field("c_mfg_name")
    private String mfgName ;

    @SerializedName("n_mrp")
    @Field("n_mrp")
    private BigDecimal mrp;

    @SerializedName("n_pack_size")
    @Field("n_pack_size")
    private int packSize;

    @SerializedName("j_item_thumbnail_images")
    @Field("j_item_thumbnail_images")
    private List<ThumbnailBO> imageBOS;

    @SerializedName("c_contains")
    @Field("c_contains")
    private String contains;

    @SerializedName("c_watchlist_status")
    @Field("c_watchlist_status")
    private String watchListStatus;

    @SerializedName("c_shortbook_status")
    @Field("c_shortbook_status")
    private String shortbookStatus;

    @SerializedName("c_discount_status")
    @Field("c_discount_status")
    private String discountStatus;

    @SerializedName("n_offer_rate")
    private BigDecimal offerRate;

    @SerializedName("n_discount")
    private BigDecimal nDiscount;

    @SerializedName("dt_start_datetime")
    private String startDateTime;

    @SerializedName("dt_end_datetime")
    private String endDateTime;

    @SerializedName("n_discount_amount")
    private BigDecimal discAmount;

    @SerializedName("c_discount_type")
    private String discType;

    @SerializedName("n_discount_percentage")
    private BigDecimal discPercentage;

    @SerializedName("c_pack_type_name")
    @Field("c_pack_type_name")
    private String packTypeName = Constants.EMPTY_STRING;

    @SerializedName("j_item_images")
    @Field("j_item_images")
    private List<ItemImageBO> thumbnail;

}
