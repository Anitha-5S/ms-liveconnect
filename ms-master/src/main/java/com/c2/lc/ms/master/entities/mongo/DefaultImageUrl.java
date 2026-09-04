package com.c2.lc.ms.master.entities.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Document("default_image")
public class DefaultImageUrl {

    @Id
    public String id;

    @SerializedName("c_seller_image")
    @Field("c_seller_image")
    public String sellerImage;

    @SerializedName("c_mfg_image")
    @Field("c_mfg_image")
    public String mfgImage;

    @SerializedName("c_seller_thumbnail_image")
    @Field("c_seller_thumbnail_image")
    public String sellerThumbnailImage;

    @SerializedName("c_mfg_thumbnail_image")
    @Field("c_mfg_thumbnail_image")
    public String mfgThumbnailImage;

    @SerializedName("c_product_image")
    @Field("c_product_image")
    public String productImage;

    @SerializedName("c_product_thumbnail_image")
    @Field("c_product_thumbnail_image")
    public String productThumbnailImage;

}
