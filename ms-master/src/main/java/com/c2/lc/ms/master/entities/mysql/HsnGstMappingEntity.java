package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "hsn_gst_mapping")
@IdClass(HsnGstMappingEntityPK.class)
public class HsnGstMappingEntity {
    private String cHsnSacCode;

    @Id
    @Column(name = "c_hsn_sac_code", nullable = false, length = 8)
    public String getcHsnSacCode() {
        return cHsnSacCode;
    }

    public void setcHsnSacCode(String cHsnSacCode) {
        this.cHsnSacCode = cHsnSacCode;
    }

    private String cGstCode;

    @Id
    @Column(name = "c_gst_code", nullable = false, length = 8)
    public String getcGstCode() {
        return cGstCode;
    }

    public void setcGstCode(String cGstCode) {
        this.cGstCode = cGstCode;
    }

    private Timestamp dAdate;

    @Basic
    @Column(name = "d_adate", nullable = true)
    public Timestamp getdAdate() {
        return dAdate;
    }

    public void setdAdate(Timestamp dAdate) {
        this.dAdate = dAdate;
    }

    private Timestamp dLdate;

    @Basic
    @Column(name = "d_ldate", nullable = true)
    public Timestamp getdLdate() {
        return dLdate;
    }

    public void setdLdate(Timestamp dLdate) {
        this.dLdate = dLdate;
    }

    private Integer nPredefined;

    @Basic
    @Column(name = "n_predefined", nullable = true)
    public Integer getnPredefined() {
        return nPredefined;
    }

    public void setnPredefined(Integer nPredefined) {
        this.nPredefined = nPredefined;
    }

    private String cModiUser;

    @Basic
    @Column(name = "c_modi_user", nullable = true, length = 45)
    public String getcModiUser() {
        return cModiUser;
    }

    public void setcModiUser(String cModiUser) {
        this.cModiUser = cModiUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HsnGstMappingEntity that = (HsnGstMappingEntity) o;
        return Objects.equals(cHsnSacCode, that.cHsnSacCode) &&
                Objects.equals(cGstCode, that.cGstCode) &&
                Objects.equals(dAdate, that.dAdate) &&
                Objects.equals(dLdate, that.dLdate) &&
                Objects.equals(nPredefined, that.nPredefined) &&
                Objects.equals(cModiUser, that.cModiUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cHsnSacCode, cGstCode, dAdate, dLdate, nPredefined, cModiUser);
    }
}
