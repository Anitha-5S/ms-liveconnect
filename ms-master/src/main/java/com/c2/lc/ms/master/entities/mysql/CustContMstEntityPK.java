package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class CustContMstEntityPK implements Serializable {

    private String cC2Code;
    private String cCode;

    @Id
    @Column(name = "c_c2code", nullable = false, length = 6)
    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
    }

    @Id
    @Column(name = "c_code", nullable = false, length = 20)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }
}
