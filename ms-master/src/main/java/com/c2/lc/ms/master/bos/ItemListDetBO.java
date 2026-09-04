package com.c2.lc.ms.master.bos;


import com.c2.lc.lib.bo.NextPageBO;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemListDetBO {
    @SerializedName("data")
    private List<ItemListBO> itemList;

    @SerializedName("page")
    private NextPageBO nextPage;
}
