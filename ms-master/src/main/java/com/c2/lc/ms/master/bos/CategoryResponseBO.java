package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseBO {

    @SerializedName("c_code")
    private String categoryCode;

    @SerializedName("c_name")
    private String categoryName;

    @SerializedName("c_sh_name")
    private String shName;

}
