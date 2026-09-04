package com.c2.lc.ms.master.entities.mysql;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
@Getter
@Setter
public class LcBlockedEntityPK implements Serializable {
    @Column(name = "c_c2code")
    @Id
    private String cC2Code;

    @Column(name = "c_item_code")
    @Id
    private String cItemCode;

    @Column(name = "n_type")
    @Id
    private String nType;

}
