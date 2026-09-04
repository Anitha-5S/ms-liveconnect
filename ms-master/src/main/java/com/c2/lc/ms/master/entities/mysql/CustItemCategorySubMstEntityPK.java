package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class CustItemCategorySubMstEntityPK implements Serializable {

    private String cCode;

    @Column(name = "c_code", nullable = false, length = 20)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    private String c2Code;

    @Column(name = "c_c2code", nullable = false, length = 20)
    public String getC2Code() {
        return c2Code;
    }

    public void setC2Code(String c2Code) {
        this.c2Code = c2Code;
    }



}
