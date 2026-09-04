package com.c2.lc.ms.customer.entities.seller;

import com.c2.lc.ms.customer.entities.seller.pk.LcUserSellerPriorityEntityPK;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lc_seller_buyer_priority")
@IdClass(LcUserSellerPriorityEntityPK.class)
@Data
public class LcSellerBuyerPriorityEntity {

    @Id
    @Column(name = "n_firm_id")
    private long nFirmId;

    @Id
    @Column(name = "c_seller_code")
    private String cSellerCode;

    @Id
    @Column(name = "c_buyer_code")
    private String cBuyerCode;

    @Column(name = "n_priority", nullable = false)
    private int nPriority;

    @Column(name = "d_lTime", nullable = false)
    private LocalDateTime dTime;

    @Column(name = "n_buyer_seller_priority", nullable = false)
    private int nBuyerSellerPriority;
}
