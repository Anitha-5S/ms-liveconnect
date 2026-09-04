package com.c2.lc.ms.customer.entities.seller.pk;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class LcUserSellerPriorityEntityPK implements Serializable {

    @Id
    @Column(name = "n_firm_id")
    private long nFirmId;

    @Id
    @Column(name = "c_seller_code")
    private String cSellerCode;

    @Id
    @Column(name = "c_buyer_code")
    private String cBuyerCode;
}
