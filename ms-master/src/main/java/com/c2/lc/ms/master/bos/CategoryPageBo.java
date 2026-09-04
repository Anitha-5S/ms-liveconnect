package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPageBo {

    @SerializedName("j_category_list")
    private List<CategoryResponseBO> categoryList;

    @SerializedName("n_next_page")
    private int nextPage;

    @SerializedName("n_total")
    private long total;

}
