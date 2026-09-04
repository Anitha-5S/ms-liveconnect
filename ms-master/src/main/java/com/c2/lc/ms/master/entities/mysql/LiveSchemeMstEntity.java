package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "live_scheme_mst")
@IdClass(LiveSchemeMstEntityPK.class)
public class LiveSchemeMstEntity {
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

    private Byte nType;

    @Basic
    @Column(name = "n_type", nullable = true)
    public Byte getnType() {
        return nType;
    }

    public void setnType(Byte nType) {
        this.nType = nType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiveSchemeMstEntity that = (LiveSchemeMstEntity) o;
        return Objects.equals(cItemCode, that.cItemCode) &&
                Objects.equals(cScheme, that.cScheme) &&
                Objects.equals(cFirmCode, that.cFirmCode) &&
                Objects.equals(cSchCatCode, that.cSchCatCode) &&
                Objects.equals(nType, that.nType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cItemCode, cScheme, cFirmCode, cSchCatCode, nType);
    }
}
