package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class UStatewiseFastmovingItemsEntityPK implements Serializable {
    private String cUcode;
    private String cStateCode;

    @Column(name = "c_ucode", nullable = false, length = 50)
    @Id
    public String getcUcode() {
        return cUcode;
    }

    public void setcUcode(String cUcode) {
        this.cUcode = cUcode;
    }

    @Column(name = "c_state_code", nullable = false, length = 20)
    @Id
    public String getcStateCode() {
        return cStateCode;
    }

    public void setcStateCode(String cStateCode) {
        this.cStateCode = cStateCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UStatewiseFastmovingItemsEntityPK that = (UStatewiseFastmovingItemsEntityPK) o;
        return Objects.equals(cUcode, that.cUcode) &&
                Objects.equals(cStateCode, that.cStateCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cUcode, cStateCode);
    }
}
