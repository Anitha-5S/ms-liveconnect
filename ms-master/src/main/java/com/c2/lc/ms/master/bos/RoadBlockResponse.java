package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadBlockResponse {

    @SerializedName("c_roadBlock_id")
    private String roadblockId;

    @SerializedName("c_image_url")
    private String imageUrl;

    @SerializedName("c_redirect_url")
    private String reDirectUrl;

    @SerializedName("c_type")
    private String type;

    @SerializedName("c_height_width")
    private String heightWidth;
}
