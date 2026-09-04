package com.c2.lc.ms.master.entities.mysql;

import com.c2.lc.lib.db.DateAudit;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lc_offers_mst")
@IdClass(LcOfferMstEntityPK.class)
public class LcOfferMstEntity extends DateAudit implements Serializable {

    @Id
    @NotEmpty(message = "'c_item_code' cannot be empty!")
    @SerializedName("c_item_code")
    @Column(name = "c_item_code")
    private String itemCode;

    @Id
    @NotEmpty(message = "'c_offer_code' cannot be empty!")
    @SerializedName("c_offer_code")
    @Column(name = "c_offer_code")
    private String offerCode;

    @Id
    @NotEmpty(message = "'c_seller_c2code' cannot be empty!")
    @SerializedName("c_seller_c2code")
    @Column(name = "c_seller_c2code")
    private String sellerC2Code;

    @NotNull(message = "'t_start_date' cannot be empty!")
    @SerializedName("t_start_date")
    @Column(name = "t_start_date")
    private LocalDateTime startDate;

    @NotNull(message = "'t_end_date' cannot be empty!")
    @SerializedName("t_end_date")
    @Column(name = "t_end_date")
    private LocalDateTime endDate;

    @SerializedName("c_image_url")
    @Column(name = "c_image_url")
    private String imageUrl;

    @SerializedName("c_image_extension")
    @Column(name = "c_image_extension")
    private String imageExtension;

    @SerializedName("c_image_filename")
    @Column(name = "c_image_filename")
    private String imageFilename;

    @Transient
    @SerializedName("c_image_filedata")
    private String imageFiledata;

    @SerializedName("c_status")
    @Column(name = "c_status")
    private String status;

}
