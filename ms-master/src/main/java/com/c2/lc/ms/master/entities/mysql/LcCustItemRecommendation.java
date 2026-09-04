package com.c2.lc.ms.master.entities.mysql;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "lc_cust_item_recommendation")
@IdClass(LcCustItemRecommendationPK.class)
public class LcCustItemRecommendation {

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

    @Column(name = "n_count")
    private Long nCount;

}
