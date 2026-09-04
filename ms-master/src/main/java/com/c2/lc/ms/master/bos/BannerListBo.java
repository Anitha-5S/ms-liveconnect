package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerListBo {

    @SerializedName("c_banner_image_url")
    private String imageUrl;

    @SerializedName("c_redirect_url")
    private String reDirectUrl;

}
