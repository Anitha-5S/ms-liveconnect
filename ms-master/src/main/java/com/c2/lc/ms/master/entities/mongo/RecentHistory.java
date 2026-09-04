package com.c2.lc.ms.master.entities.mongo;


import com.c2.lc.ms.master.bos.ItemBo;
import com.c2.lc.ms.master.bos.ManufacturerBo;
import com.c2.lc.ms.master.bos.MoleculeBo;
import com.c2.lc.ms.master.bos.SellerBo;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("lc_recent_history")
public class RecentHistory {

    @Id
    private String id;

    @Indexed
    @SerializedName("c_user_id")
    @Field("c_user_id")
    private long userId;

    @Indexed
    @SerializedName("c_firm_id")
    @Field("c_firm_id")
    private long firmId;

    //@Id
    @SerializedName("c_type")
    @Field("c_type")
    private String type ;

    @SerializedName("c_code")
    @Field("c_code")
    private String cCode;

    @SerializedName("d_aDate")
    @Field("d_aDate")
    private LocalDateTime aDateTime;

    @SerializedName("c_seller_detail")
    @Field("c_seller_detail")
    private SellerBo sellerBo;

    @SerializedName("c_item_detail")
    @Field("c_item_detail")
    private ItemBo itemBo;

    @SerializedName("c_molecule_detail")
    @Field("c_molecule_detail")
    private MoleculeBo moleculeBo;

    @SerializedName("c_manufacturer_detail")
    @Field("c_manufacturer_detail")
    private ManufacturerBo manufacturerBo;
}
