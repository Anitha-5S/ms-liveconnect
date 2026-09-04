package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manufacture {


    @Field("c_manufacture_code")
    @SerializedName("c_manufacture_code")
    private String manufactureCode;

    @Field("c_manufacture_name")
    @SerializedName("c_manufacture_name")
    private String manufactureName;

    @SerializedName("ac_thumbnail_images")
    @Field("ac_thumbnail_images")
    private List<ThumbnailBO> thumbnailImages;

    @SerializedName("ac_images")
    @Field("ac_images")
    private List<ItemImageBO> images;

}
