package com.c2.lc.ms.master.models;

import com.google.gson.annotations.Expose;
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
@Document("ts_type_wise_banner")
public class TypeWiseBannerModel {

    @Expose(serialize = false,deserialize = false)
    @Id
    private String id;

    @SerializedName("n_banner_temp_id")
    @Field("n_banner_temp_id")
    private int bannerTempId;

    @SerializedName("c_offer_type")
    @Field("c_offer_type")
    private String offerType;

    @SerializedName("c_banner_temp_img_url")
    @Field("c_banner_temp_img_url")
    private String imgURL;

    @SerializedName("c_content_align")
    @Field("c_content_align")
    private String alignment;

}
