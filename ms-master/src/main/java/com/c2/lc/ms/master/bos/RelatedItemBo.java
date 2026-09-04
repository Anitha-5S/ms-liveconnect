package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatedItemBo {

    @SerializedName("c_item_code")
    @Field("c_item_code")
    private String itemCode;

    @SerializedName("c_pack_name")
    @Field("c_pack_name")
    private String packName ;
}
