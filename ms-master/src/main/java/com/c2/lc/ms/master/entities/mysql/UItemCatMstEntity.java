package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "u_item_cat_mst")
public class UItemCatMstEntity {
    private String cCode;
    private String cName;
    private String cShName;
    private String cItemCategoryHeadCode;
    private int nAudited;
    private int nPredefined;
    private String cCreateuser;
    private Date dAdate;
    private Date dLdate;
    private Timestamp tLtime;
    private String cModiuser;

    @Id
    @Column(name = "c_code", nullable = false, length = 6)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    @Basic
    @Column(name = "c_name", nullable = false, length = 60)
    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    @Basic
    @Column(name = "c_sh_name", nullable = false, length = 6)
    public String getcShName() {
        return cShName;
    }

    public void setcShName(String cShName) {
        this.cShName = cShName;
    }

    @Basic
    @Column(name = "c_item_category_head_code", nullable = false, length = 6)
    public String getcItemCategoryHeadCode() {
        return cItemCategoryHeadCode;
    }

    public void setcItemCategoryHeadCode(String cItemCategoryHeadCode) {
        this.cItemCategoryHeadCode = cItemCategoryHeadCode;
    }

    @Basic
    @Column(name = "n_audited", nullable = false)
    public int getnAudited() {
        return nAudited;
    }

    public void setnAudited(int nAudited) {
        this.nAudited = nAudited;
    }

    @Basic
    @Column(name = "n_predefined", nullable = false)
    public int getnPredefined() {
        return nPredefined;
    }

    public void setnPredefined(int nPredefined) {
        this.nPredefined = nPredefined;
    }

    @Basic
    @Column(name = "c_createuser", nullable = false, length = 10)
    public String getcCreateuser() {
        return cCreateuser;
    }

    public void setcCreateuser(String cCreateuser) {
        this.cCreateuser = cCreateuser;
    }

    @Basic
    @Column(name = "d_adate", nullable = false)
    public Date getdAdate() {
        return dAdate;
    }

    public void setdAdate(Date dAdate) {
        this.dAdate = dAdate;
    }

    @Basic
    @Column(name = "d_ldate", nullable = false)
    public Date getdLdate() {
        return dLdate;
    }

    public void setdLdate(Date dLdate) {
        this.dLdate = dLdate;
    }

    @Basic
    @Column(name = "t_ltime", nullable = true)
    public Timestamp gettLtime() {
        return tLtime;
    }

    public void settLtime(Timestamp tLtime) {
        this.tLtime = tLtime;
    }

    @Basic
    @Column(name = "c_modiuser", nullable = false, length = 10)
    public String getcModiuser() {
        return cModiuser;
    }

    public void setcModiuser(String cModiuser) {
        this.cModiuser = cModiuser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UItemCatMstEntity that = (UItemCatMstEntity) o;
        return nAudited == that.nAudited &&
                nPredefined == that.nPredefined &&
                Objects.equals(cCode, that.cCode) &&
                Objects.equals(cName, that.cName) &&
                Objects.equals(cShName, that.cShName) &&
                Objects.equals(cItemCategoryHeadCode, that.cItemCategoryHeadCode) &&
                Objects.equals(cCreateuser, that.cCreateuser) &&
                Objects.equals(dAdate, that.dAdate) &&
                Objects.equals(dLdate, that.dLdate) &&
                Objects.equals(tLtime, that.tLtime) &&
                Objects.equals(cModiuser, that.cModiuser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cCode, cName, cShName, cItemCategoryHeadCode, nAudited, nPredefined, cCreateuser, dAdate, dLdate, tLtime, cModiuser);
    }
}
