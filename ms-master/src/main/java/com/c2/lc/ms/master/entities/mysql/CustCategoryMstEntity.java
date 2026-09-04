package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "cust_category_mst")
@IdClass(CustCategoryMstEntityPK.class)
public class CustCategoryMstEntity {
    private String cC2Code;

    @Id
    @Column(name = "c_c2code", nullable = false, length = 100)
    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
    }

    private String cCode;

    @Id
    @Column(name = "c_code", nullable = false, length = 10)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    private String cName;

    @Basic
    @Column(name = "c_name", nullable = false, length = 20)
    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    private BigInteger nPredefined;

    @Basic
    @Column(name = "n_predefined", nullable = true, precision = 0)
    public BigInteger getnPredefined() {
        return nPredefined;
    }

    public void setnPredefined(BigInteger nPredefined) {
        this.nPredefined = nPredefined;
    }

    private Date dLdate;

    @Basic
    @Column(name = "d_ldate", nullable = true)
    public Date getdLdate() {
        return dLdate;
    }

    public void setdLdate(Date dLdate) {
        this.dLdate = dLdate;
    }

    private String cSchCategoryCode;

    @Basic
    @Column(name = "c_sch_category_code", nullable = true, length = 10)
    public String getcSchCategoryCode() {
        return cSchCategoryCode;
    }

    public void setcSchCategoryCode(String cSchCategoryCode) {
        this.cSchCategoryCode = cSchCategoryCode;
    }

    private Integer nColor;

    @Basic
    @Column(name = "n_color", nullable = true, precision = 0)
    public Integer getnColor() {
        return nColor;
    }

    public void setnColor(Integer nColor) {
        this.nColor = nColor;
    }

    private BigDecimal nDiscount;

    @Basic
    @Column(name = "n_discount", nullable = true, precision = 2)
    public BigDecimal getnDiscount() {
        return nDiscount;
    }

    public void setnDiscount(BigDecimal nDiscount) {
        this.nDiscount = nDiscount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustCategoryMstEntity that = (CustCategoryMstEntity) o;
        return Objects.equals(cC2Code, that.cC2Code) &&
                Objects.equals(cCode, that.cCode) &&
                Objects.equals(cName, that.cName) &&
                Objects.equals(nPredefined, that.nPredefined) &&
                Objects.equals(dLdate, that.dLdate) &&
                Objects.equals(cSchCategoryCode, that.cSchCategoryCode) &&
                Objects.equals(nColor, that.nColor) &&
                Objects.equals(nDiscount, that.nDiscount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cC2Code, cCode, cName, nPredefined, dLdate, cSchCategoryCode, nColor, nDiscount);
    }
}
