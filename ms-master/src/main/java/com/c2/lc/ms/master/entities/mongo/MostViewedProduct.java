package com.c2.lc.ms.master.entities.mongo;

import com.c2.lc.lib.db.DateAudit;
import com.c2.lc.ms.master.bos.ItemImageBO;
import com.c2.lc.ms.master.bos.ThumbnailBO;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;

@Data
@Document("ts_most_viewed")
public class MostViewedProduct {

    @Id
    private String id;

    @Indexed
    @SerializedName("c_c2_code")
    @Field("c_c2_code")
    private String cC2Code;

    @Indexed
    @SerializedName("c_br_code")
    @Field("c_br_code")
    private String cBrCode ;

    @Indexed
    @SerializedName("c_item_code")
    @Field("c_item_code")
    private String cItemCode;

    @SerializedName("c_item_name")
    @Field("c_item_name")
    private String cItemName ;

    @SerializedName("c_seller_item_code")
    @Field("c_seller_item_code")
    private String cSellerItemCode ;

    @SerializedName("c_web_img_link")
    @Field("c_web_img_link")
    private List<ThumbnailBO> cItemImg ;

    @SerializedName("c_packing")
    @Field("c_packing")
    private String cPacking ;

    @SerializedName("n_viewed_count")
    @Field("n_viewed_count")
    private long nViewedCount ;

    @SerializedName("n_stock_availability")
    @Field("n_stock_availability")
    private long nStockAvailability ;

    @SerializedName("n_sales_count")
    @Field("n_sales_count")
    private long nSalesCount = 0;

    @SerializedName("c_sales_ratio")
    @Field("c_sales_ratio")
    private BigDecimal cSalesRatio;
}
