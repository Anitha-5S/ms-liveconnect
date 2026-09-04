package com.c2.lc.ms.master.entities.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document("lc_item_images")
public class LcItemImages {

    @Id
    @SerializedName("c_item_code")
    @Field("c_item_code")
    private String itemCode;

    @SerializedName("ac_thumbnail_images")
    @Field("ac_thumbnail_images")
    private List<String> thumbnailImages;

    @SerializedName("ac_item_images")
    @Field("ac_item_images")
    private List<String> itemImages;
}
