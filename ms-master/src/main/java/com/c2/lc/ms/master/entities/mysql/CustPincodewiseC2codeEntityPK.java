package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class CustPincodewiseC2codeEntityPK implements Serializable {
    private String cC2Code;
    private String cPincode;

    @Column(name = "c_c2code", nullable = false, length = 20)
    @Id
    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
    }

    @Column(name = "c_pincode", nullable = false, length = 6)
    @Id
    public String getcPincode() {
        return cPincode;
    }

    public void setcPincode(String cPincode) {
        this.cPincode = cPincode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustPincodewiseC2codeEntityPK that = (CustPincodewiseC2codeEntityPK) o;
        return Objects.equals(cC2Code, that.cC2Code) &&
                Objects.equals(cPincode, that.cPincode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cC2Code, cPincode);
    }
}
