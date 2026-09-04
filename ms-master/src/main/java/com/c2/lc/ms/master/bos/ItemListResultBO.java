package com.c2.lc.ms.master.bos;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ItemListResultBO implements Serializable {

    @SerializedName("j_item_list")
    private List<JsonObject> result;
}
