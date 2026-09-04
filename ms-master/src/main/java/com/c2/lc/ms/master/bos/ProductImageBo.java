package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageBo {

    @Field("c_uitem_code")
    @SerializedName("c_uitem_code")
    private String uitemCode;

    @Field("c_file_name")
    @SerializedName("c_file_name")
    private String fileName;

    @Field("c_image_data")
    @SerializedName("c_image_data")
    private String imageData;

    @Field("c_is_thumbnail")
    @SerializedName("c_is_thumbnail")
    private boolean isThumbnail;
}
