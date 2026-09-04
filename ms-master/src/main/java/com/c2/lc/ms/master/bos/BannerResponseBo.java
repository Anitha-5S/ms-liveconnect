package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.utils.Constants;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerResponseBo {

    @SerializedName("c_banner_id")
    private String bannerId;

    @SerializedName("c_banner_image_url")
    private String imageUrl;

    @SerializedName("c_redirect_url")
    private String reDirectUrl = Constants.EMPTY_STRING;

    @SerializedName("c_type")
    private String type;

    @SerializedName("c_height_width")
    private String heightWidth;


}
