package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class CustSchemeSpecialRateEntityPK implements Serializable {

    private String cC2Code;

    @Id
    @Column(name = "c_c2code", nullable = false, length = 100)
    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
    }

    private String cCustCode;

    @Id
    @Column(name = "c_cust_code", nullable = false, length = 100)
    public String getcCustCode() {
        return cCustCode;
    }

    public void setcCustCode(String cCustCode) {
        this.cCustCode = cCustCode;
    }

    private String cItemCode;

    @Id
    @Column(name = "c_item_code", nullable = false, length = 100)
    public String getcItemCode() {
        return cItemCode;
    }

    public void setcItemCode(String cItemCode) {
        this.cItemCode = cItemCode;
    }

}
