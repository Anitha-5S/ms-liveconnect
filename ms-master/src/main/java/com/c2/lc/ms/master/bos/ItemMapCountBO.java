package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class ItemMapCountBO implements Serializable {

    @SerializedName("c_total_count")
    private String totalCount;

    @SerializedName("c_mapped_count")
    private String mappedCount;

    @SerializedName("c_unmapped_count")
    private String unmappedCount;

    @SerializedName("c_ownitems_count")
    private String ownitemsCount;

    @SerializedName("c_blocked_count")
    private String blockedCount;
}
