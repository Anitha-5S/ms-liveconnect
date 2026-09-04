package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.utils.Constants;
import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPDPResponseBO extends ItemPLPResponseBO{

    @SerializedName("c_barcode")
    @Field("c_barcode")
    private String barcode = "";

    /*@SerializedName("j_item_images")
    @Field("j_item_images")
    private List<ItemImageBO> thumbnail;*/

    @SerializedName("j_alternatives")
    @Field("j_alternatives")
    private List<RelatedItemBo> relatedItem;

    @SerializedName("c_web_img_link")
    @Field("c_web_img_link")
    private String imageUrl;

    @SerializedName("c_hsn_code")
    @Field("c_hsn_code")
    private String hsnCode;

    @SerializedName("c_gst")
    @Field("c_gst")
    private String gst ;

    /*@SerializedName("c_pack_type_name")
    @Field("c_pack_type_name")
    private String packTypeName = Constants.EMPTY_STRING;*/

    @SerializedName("j_molecules")
    @Field("j_molecules")
    private List<ItemMoleculeBo> molecules;
}
