package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoleculeFilterBO {

    @SerializedName("c_search")
    private String searchString;

    @SerializedName("n_page")
    private int page;

    @SerializedName("n_size")
    private int size;

}
