package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class CustSchemeMstEntityPK implements Serializable {
    private static final long serialVersionUID = 2149796925852485709L;
    private String cC2Code;

    @Id
    @Column(name = "c_c2code", nullable = false, length = 45)
    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustSchemeMstEntityPK that = (CustSchemeMstEntityPK) o;
        return Objects.equals(cC2Code, that.cC2Code) &&
                Objects.equals(cItemCode, that.cItemCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cC2Code, cItemCode);
    }
}
