package com.c2.lc.ms.master.models;

import lombok.Data;

@Data
public class NewLaunchesResponse {
    private String c_item_name;
    private String c_item_code;
    private String c_pack_name;
    private Long n_max_mrp;
    private String c_content_name;
    private String ac_thumbnail_images;

}
