package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThumbnailBO {

    @Field("c_thumbnail_image")
    @SerializedName("c_thumbnail_image")
    private String thumbnailImage;
}
