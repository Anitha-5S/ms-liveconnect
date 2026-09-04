package com.c2.lc.ms.master.entities.mysql;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class LcUserSellerPriorityEntityPK implements Serializable {

    @Id
    @Column(name = "n_mst_id")
    private int nMstId;

    @Id
    @Column(name = "c_seller_code")
    private String cSellerCode;

    @Id
    @Column(name = "c_buyer_code")
    private String cBuyerCode;
}
