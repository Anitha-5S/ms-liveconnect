package com.c2.lc.ms.master.entities.elastic;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@Document(indexName = "els_lc_item")
public class ElLcItem {

    @Id
    @SerializedName("c_item_code")
    @Field("c_item_code")
    private String itemCode;

    @Indexed
    @SerializedName("c_item_name")
    @Field("c_item_name")
    private String itemName ;

    @SerializedName("n_mrp")
    @Field("n_mrp")
    private BigDecimal mrp;

    @SerializedName("c_gst_code")
    @Field("c_gst_code")
    private String gstCode ;

    @SerializedName("selling_count")
    @Field("selling_count")
    private int sellingCount ;

    @SerializedName("selling_qty")
    @Field("selling_qty")
    private int sellingQty ;

}
