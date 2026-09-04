package com.c2.lc.ms.customer.entities.seller.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LoCombinedFirmEntityPK implements Serializable {
    private String c2Code;
    private String cCode;

    @Column(name = "c_c2code", nullable = false, length = 20)
    @Id
    public String getC2Code() {
        return c2Code;
    }

    public void setC2Code(String c2Code) {
        this.c2Code = c2Code;
    }

    @Column(name = "c_code", nullable = false, length = 6)
    @Id
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoCombinedFirmEntityPK that = (LoCombinedFirmEntityPK) o;
        return Objects.equals(c2Code, that.c2Code) &&
                Objects.equals(cCode, that.cCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c2Code, cCode);
    }
}
