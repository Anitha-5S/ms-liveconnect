package com.c2.lc.ms.master.bos;


import com.c2.lc.ms.master.entities.mongo.LcItem;
import com.c2.lc.ms.master.entities.mongo.LcManufacture;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturePLPBO {

    @SerializedName("j_product_list")
    private List<LcItem> manufactureList;

    @SerializedName("n_next_page")
    private int nextPage;

    @SerializedName("n_total")
    private long total;

}
