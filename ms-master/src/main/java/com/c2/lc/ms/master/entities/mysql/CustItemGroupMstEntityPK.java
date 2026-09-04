package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class CustItemGroupMstEntityPK implements Serializable {
    private String cCode;
    private String cC2Code;

    @Id
    @Column(name = "c_c2code", nullable = true, length = 20)
    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
    }

    @Id
    @Column(name = "c_code", nullable = true, length = 100)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }





}
