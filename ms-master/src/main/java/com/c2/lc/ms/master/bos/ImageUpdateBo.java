package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageUpdateBo {

    @Field("c_code")
    @SerializedName("c_code")
    private String cCode;

    @Field("ac_image")
    @SerializedName("ac_image")
    private List<String> acImages;
}
