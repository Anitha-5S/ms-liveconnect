package com.c2.lc.ms.customer.bos;

import com.c2.lc.lib.bo.NextPageBO;
import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersListResponseBO {

    @SerializedName("items")
    private JsonArray list;

    @SerializedName("page")
    private NextPageBO nextPage;

}
