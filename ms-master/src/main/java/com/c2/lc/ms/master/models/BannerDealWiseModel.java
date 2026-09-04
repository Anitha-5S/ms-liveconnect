package com.c2.lc.ms.master.models;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerDealWiseModel {

    @SerializedName("c_category_code")
    @Field("c_category_code")
    private String categoryCode;

    @SerializedName("c_display_name")
    @Field("c_display_name")
    private String displayName;

    @SerializedName("n_discount_percentage")
    @Field("n_discount_percentage")
    private BigDecimal discPercentage;
}
