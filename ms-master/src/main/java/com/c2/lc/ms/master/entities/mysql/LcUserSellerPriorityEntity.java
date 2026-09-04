package com.c2.lc.ms.master.entities.mysql;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "lc_user_seller_priority")
@IdClass(LcUserSellerPriorityEntityPK.class)
@Data
public class LcUserSellerPriorityEntity {

    @Id
    @Column(name = "n_mst_id")
    private int nMstId;

    @Id
    @Column(name = "c_seller_code")
    private String cSellerCode;

    @Id
    @Column(name = "c_buyer_code")
    private String cBuyerCode;

    @Column(name = "n_priority", nullable = false)
    private String nPriority;

    @Column(name = "d_lTime", nullable = false)
    private String dTime;

    @Column(name = "n_buyer_seller_priority", nullable = false)
    private String nBuyerSellerPriority;
}
