package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class CustPackTypeMstPK implements Serializable {
    private String cC2code;
    private String cCode;

    @Id
    @Column(name = "c_c2code", nullable = true, length = 6)
    public String getcC2code() {
        return cC2code;
    }

    public void setcC2code(String cC2code) {
        this.cC2code = cC2code;
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
