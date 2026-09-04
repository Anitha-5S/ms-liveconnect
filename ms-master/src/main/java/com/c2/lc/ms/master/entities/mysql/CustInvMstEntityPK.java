package com.c2.lc.ms.master.entities.mysql;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
public class CustInvMstEntityPK implements Serializable {

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

}
