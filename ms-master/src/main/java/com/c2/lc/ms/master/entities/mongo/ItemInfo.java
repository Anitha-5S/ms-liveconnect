package com.c2.lc.ms.master.entities.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfo {


    @SerializedName("c_hsn_code")
    @Field("c_hsn_code")
    private String hsnCode ;

    @SerializedName("ac_item_images")
    @Field("ac_item_images")
    private List<String> itemImages;

    @SerializedName("c_description")
    @Field("c_description")
    private String description ;

    @SerializedName("c_usage")
    @Field("c_usage")
    private String usage ;

    @SerializedName("c_note")
    @Field("c_note")
    private String note ;

    @SerializedName("c_side_effect")
    @Field("c_side_effect")
    private String sideEffect ;

    @SerializedName("c_contra_indications")
    @Field("c_contra_indications")
    private String contraIndications;
}
