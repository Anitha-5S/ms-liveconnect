package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class LiveSchemeMstEntityPK implements Serializable {

    private static final long serialVersionUID = -1917124884713174280L;
    private String cItemCode;

    @Id
    @Column(name = "c_item_code", nullable = false, length = 100)
    public String getcItemCode() {
        return cItemCode;
    }

    public void setcItemCode(String cItemCode) {
        this.cItemCode = cItemCode;
    }

    private String cScheme;

    @Id
    @Column(name = "c_scheme", nullable = false, length = 40)
    public String getcScheme() {
        return cScheme;
    }

    public void setcScheme(String cScheme) {
        this.cScheme = cScheme;
    }

    private String cFirmCode;

    @Id
    @Column(name = "c_firm_code", nullable = false, length = 6)
    public String getcFirmCode() {
        return cFirmCode;
    }

    public void setcFirmCode(String cFirmCode) {
        this.cFirmCode = cFirmCode;
    }

    private String cSchCatCode;

    @Id
    @Column(name = "c_sch_cat_code", nullable = false, length = 6)
    public String getcSchCatCode() {
        return cSchCatCode;
    }

    public void setcSchCatCode(String cSchCatCode) {
        this.cSchCatCode = cSchCatCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiveSchemeMstEntityPK that = (LiveSchemeMstEntityPK) o;
        return Objects.equals(cItemCode, that.cItemCode) &&
                Objects.equals(cScheme, that.cScheme) &&
                Objects.equals(cFirmCode, that.cFirmCode) &&
                Objects.equals(cSchCatCode, that.cSchCatCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cItemCode, cScheme, cFirmCode, cSchCatCode);
    }
}
