package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.bo.PageBO;
import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Size;

@Data
public class MostViewedPrdsBO extends PageBO {

    @SerializedName("c_c2_code")
    private String cC2Code;

    @SerializedName("c_br_code")
    private String cBrCode;

    @SerializedName("c_search_term")
    @Size(min = 3, max = 30, message = "'Search Term' has to be length {min} to {max} characters!")
    private String cSearchTerm;

    @SerializedName("c_sort_type")
    private String cSortType;

    @SerializedName("c_sort_by_column")
    private String cSortByCol;

}
