package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ItemSearchResultBO implements Serializable {

    @SerializedName("j_item_list")
    private List<ItemListBO> result;

    @SerializedName("n_next_offset")
    private long next;

    @SerializedName("n_limit")
    private long limit;

    @SerializedName("n_total")
    private long total;
}
