package com.c2.lc.ms.master.entities.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Document("lc_item")
public class LcItem {

    @Id
    @SerializedName("c_item_code")
    @Field("c_item_code")
    private String itemCode;

    @Indexed
    @SerializedName("c_item_name")
    @Field("c_item_name")
    private String itemName ;

    @SerializedName("ac_thumbnail_images")
    @Field("ac_thumbnail_images")
    private List<String> thumbnailImages;

    @SerializedName("c_web_img_link")
    @Field("c_web_img_link")
    private String imgUrl;

    @Indexed
    @SerializedName("c_mfg_code")
    @Field("c_mfg_code")
    private String mfgCode;

    @SerializedName("c_bar_code")
    @Field("c_bar_code")
    private String barCode;

    @SerializedName("c_mfg_name")
    @Field("c_mfg_name")
    private String mfgName ;

    @SerializedName("c_contains")
    @Field("c_contains")
    private String contains;

    @SerializedName("n_mrp")
    @Field("n_mrp")
    private BigDecimal mrp;

    @SerializedName("c_pack_name")
    @Field("c_pack_name")
    private String packName;

    @SerializedName("c_pack_type_name")
    @Field("c_pack_type_name")
    private String packTypeName;

    @SerializedName("c_pack_type_code")
    @Field("c_pack_type_code")
    private String packTypeCode;

    @SerializedName("c_gst_code")
    @Field("c_gst_code")
    private String gstCode ;

    @SerializedName("n_pack_size")
    @Field("n_pack_size")
    private int packSize ;

    @SerializedName("c_hsn_code")
    @Field("c_hsn_code")
    private String hsnCode ;

    @SerializedName("molecules")
    @Field("molecules")
    private List<LcMolecule> molecules;

    @Indexed
    @SerializedName("c_brand_code")
    @Field("c_brand_code")
    private String brandCode;

    @SerializedName("c_brand_name")
    @Field("c_brand_name")
    private String brandName;

    @Indexed
    @SerializedName("c_item_cat_code")
    @Field("c_item_cat_code")
    private String categoryCode;

    @SerializedName("c_item_cat_name")
    @Field("c_item_cat_name")
    private String categoryName;

    @SerializedName("d_item_created_date")
    @Field("d_item_created_date")
    private LocalDate itemCreatedDate;

    @SerializedName("d_last_modified_date")
    @Field("d_last_modified_date")
    private LocalDate lastModifiedDate;

    @Indexed
    @SerializedName("selling_count")
    @Field("selling_count")
    private int sellingCount ;

    @Indexed
    @SerializedName("selling_qty")
    @Field("selling_qty")
    private int sellingQty ;

}
