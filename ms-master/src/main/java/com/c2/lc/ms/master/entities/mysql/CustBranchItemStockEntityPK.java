package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class CustBranchItemStockEntityPK implements Serializable {

    private String cItemCode;

    @Id
    @Column(name = "c_item_code", nullable = false, length = 45)
    public String getcItemCode() {
        return cItemCode;
    }

    public void setcItemCode(String cItemCode) {
        this.cItemCode = cItemCode;
    }

    private String cC2Code;

    @Id
    @Column(name = "c_c2code", nullable = false, length = 45)
    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
    }

    private String cBrCode;

    @Id
    @Column(name = "c_br_code", nullable = false, length = 45)
    public String getcBrCode() {
        return cBrCode;
    }

    public void setcBrCode(String cBrCode) {
        this.cBrCode = cBrCode;
    }

    public CustBranchItemStockEntityPK() {
    }

    public CustBranchItemStockEntityPK(String cItemCode, String cC2Code, String cBrCode) {
        this.cItemCode = cItemCode;
        this.cC2Code = cC2Code;
        this.cBrCode = cBrCode;
    }

}
