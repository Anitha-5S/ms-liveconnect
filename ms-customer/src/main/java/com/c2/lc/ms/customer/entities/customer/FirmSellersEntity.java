package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.lib.db.DateAudit;
import com.c2.lc.ms.customer.entities.customer.pk.FirmSellersPKEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@IdClass(value = FirmSellersPKEntity.class)
@Table(name = "firm_sellers")
public class FirmSellersEntity extends DateAudit implements Serializable {

    private static final long serialVersionUID = -5443534162176298849L;

    @Id
    @SerializedName("n_firm_id")
    @Column(name = "n_firm_id", nullable = false)
    private Long nFirmId;

    @Id
    @NotEmpty(message = "c_seller_code should not be empty!")
    @Column(name = "c_seller_code")
    @SerializedName("c_seller_code")
    private String cSellerCode;

    @Id
    @NotEmpty(message = "c_buyer_code should not be empty!")
    @Column(name = "c_buyer_code")
    @SerializedName("c_buyer_code")
    private String cBuyerCode;

    @Column(name = "c_remarks")
    @SerializedName("c_remarks")
    private String cRemarks;

    @SerializedName("c_status")
    @Column(name = "c_status", length = 1)
    private String cStatus;

    public FirmSellersEntity(Long userId, LocalDateTime time){ super(userId, time);}

    public FirmSellersEntity() { }
}