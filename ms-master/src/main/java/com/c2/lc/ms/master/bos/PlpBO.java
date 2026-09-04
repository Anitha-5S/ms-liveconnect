package com.c2.lc.ms.master.bos;

import com.c2.lc.ms.master.entities.mongo.LcItem;
import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlpBO {

    @SerializedName("j_list")
    private JsonArray list;

    @SerializedName("n_next_page")
    private int nextPage;

    @SerializedName("n_total")
    private long total;

}
