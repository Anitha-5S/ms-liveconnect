package com.c2.lc.ms.master.entities.mysql;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "cust_inv_mst")
@IdClass(CustInvMstEntityPK.class)
@Data
public class CustInvMstEntity {

    @Id
    @Column(name = "c_c2code")
    private String c2Code;

    @Id
    @Column(name = "c_br_code")
    private  String brCode;

    @Id
    @Column(name = "c_year")
    private String year;

    @Id
    @Column(name = "c_prefix")
    private String prefix;

    @Id
    @Column(name = "n_srno")
    private int srno;

    @Id
    @Column(name = "c_cust_code")
    private String custCode;

    @Column(name = "n_total")
    private int total;

    @Column(name = "c_cust_c2code")
    private String custC2code;

}
