package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManufactureImageBo {

    @Field("c_mfg_code")
    @SerializedName("c_mfg_code")
    private String mfgCode;

    @Field("c_file_name")
    @SerializedName("c_file_name")
    private String fileName;

    @Field("c_image_data")
    @SerializedName("c_image_data")
    private String imageData;

}
