package com.c2.lc.ms.master.entities.mysql;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class LcCustItemRecommendationPK implements Serializable {

    @Id
    @Column(name = "c_c2code")
    private String cC2Code;

    @Id
    @Column(name = "c_item_code")
    private String cItemCode;

    @Id
    @Column(name = "c_cust_code")
    private String cCustCode;

    @Id
    @Column(name = "d_inv_date")
    private String dInvDate;
}
