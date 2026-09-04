package com.c2.lc.ms.master.entities.mysql;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "cust_item_category_mst")
@IdClass(CustItemCategorySubMstEntityPK.class)
@Data
public class CustItemCategorySubMstEntity {



    private String c2Code;
    private String cCode;
    private String cName;
    private String cShName;
    private BigDecimal nRst;
    private LocalDate dLdate;
    private LocalDate dAdate;
    private String cCreateuser;
    private BigDecimal nAudited;
    private BigDecimal nPredefined;
    private BigDecimal nDiscount;
    private BigDecimal nPoints;
    private Timestamp tLtime;
    private String cItemCategoryHeadCode;
    private BigDecimal nAgePer;
    private String cModiuser;
    private String cImageUrl;
    private BigInteger nSalableOnline;
    private BigInteger nDisplayOnline;
    private BigDecimal nActive;

    @Id
    @Column(name = "c_c2code", nullable = false, length = 20)
    public String getC2Code() {
        return c2Code;
    }

    public void setC2Code(String c2Code) {
        this.c2Code = c2Code;
    }

    @Id
    @Column(name = "c_code", nullable = false, length = 20)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    @Column(name = "c_name", nullable = false, length = 100)
    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    @Column(name = "c_sh_name", nullable = false, length = 10)
    public String getcShName() {
        return cShName;
    }

    public void setcShName(String cShName) {
        this.cShName = cShName;
    }

    @Column(name = "n_rst", nullable =true)
    public BigDecimal getnRst() {
        return nRst;
    }

    public void setnRst(BigDecimal nRst) {
        this.nRst = nRst;
    }

    @Column(name = "d_ldate", nullable = false)
    public LocalDate getdLdate() {
        return dLdate;
    }

    public void setdLdate(LocalDate dLdate) {
        this.dLdate = dLdate;
    }

    @Column(name = "d_adate", nullable = false)
    public LocalDate getdAdate() {
        return dAdate;
    }

    public void setdAdate(LocalDate dAdate) {
        this.dAdate = dAdate;
    }

    @Column(name = "c_createuser", nullable = false, length = 10)
    public String getcCreateuser() {
        return cCreateuser;
    }

    public void setcCreateuser(String cCreateuser) {
        this.cCreateuser = cCreateuser;
    }

    @Column(name = "n_audited", nullable = false)
    public BigDecimal getnAudited() {
        return nAudited;
    }

    public void setnAudited(BigDecimal nAudited) {
        this.nAudited = nAudited;
    }

    @Column(name = "n_predefined", nullable = false)
    public BigDecimal getnPredefined() {
        return nPredefined;
    }

    public void setnPredefined(BigDecimal nPredefined) {
        this.nPredefined = nPredefined;
    }

    @Column(name = "n_discount", nullable = false)
    public BigDecimal getnDiscount() {
        return nDiscount;
    }

    public void setnDiscount(BigDecimal nDiscount) {
        this.nDiscount = nDiscount;
    }

    @Column(name = "n_points", nullable = false)
    public BigDecimal getnPoints() {
        return nPoints;
    }

    public void setnPoints(BigDecimal nPoints) {
        this.nPoints = nPoints;
    }

    @Column(name = "t_ltime", nullable = true)
    public Timestamp gettLtime() {
        return tLtime;
    }

    public void settLtime(Timestamp tLtime) {
        this.tLtime = tLtime;
    }

    @Column(name = "c_item_category_head_code", nullable = true, length = 6)
    public String getcItemCategoryHeadCode() {
        return cItemCategoryHeadCode;
    }

    public void setcItemCategoryHeadCode(String cItemCategoryHeadCode) {
        this.cItemCategoryHeadCode = cItemCategoryHeadCode;
    }

    @Column(name = "n_age_per", nullable = true)
    public BigDecimal getnAgePer() {
        return nAgePer;
    }

    public void setnAgePer(BigDecimal nAgePer) {
        this.nAgePer = nAgePer;
    }

    @Column(name = "c_modiuser", nullable = true, length = 10)
    public String getcModiuser() {
        return cModiuser;
    }

    public void setcModiuser(String cModiuser) {
        this.cModiuser = cModiuser;
    }

    @Column(name = "c_image_url", nullable = true, length = 300)
    public String getcImageUrl() {
        return cImageUrl;
    }

    public void setcImageUrl(String cImageUrl) {
        this.cImageUrl = cImageUrl;
    }

    @Column(name = "n_salable_online", nullable = true, length = 11)
    public BigInteger getnSalableOnline() {
        return nSalableOnline;
    }

    public void setnSalableOnline(BigInteger nSalableOnline) {
        this.nSalableOnline = nSalableOnline;
    }

    @Column(name = "n_display_online", nullable = true, length = 11)
    public BigInteger getnDisplayOnline() {
        return nDisplayOnline;
    }

    public void setnDisplayOnline(BigInteger nDisplayOnline) {
        this.nDisplayOnline = nDisplayOnline;
    }

    @Column(name = "n_active", nullable = true)
    public BigDecimal getnActive() {
        return nActive;
    }

    public void setnActive(BigDecimal nActive) {
        this.nActive = nActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustItemCategorySubMstEntity that = (CustItemCategorySubMstEntity) o;
        return Objects.equals(c2Code, that.c2Code) && Objects.equals(cCode, that.cCode) && Objects.equals(cName, that.cName) && Objects.equals(cShName, that.cShName) && Objects.equals(nRst, that.nRst) && Objects.equals(dLdate, that.dLdate) && Objects.equals(dAdate, that.dAdate) && Objects.equals(cCreateuser, that.cCreateuser) && Objects.equals(nAudited, that.nAudited) && Objects.equals(nPredefined, that.nPredefined) && Objects.equals(nDiscount, that.nDiscount) && Objects.equals(nPoints, that.nPoints) && Objects.equals(tLtime, that.tLtime) && Objects.equals(cItemCategoryHeadCode, that.cItemCategoryHeadCode) && Objects.equals(nAgePer, that.nAgePer) && Objects.equals(cModiuser, that.cModiuser) && Objects.equals(cImageUrl, that.cImageUrl) && Objects.equals(nSalableOnline, that.nSalableOnline) && Objects.equals(nDisplayOnline, that.nDisplayOnline) && Objects.equals(nActive, that.nActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c2Code, cCode, cName, cShName, nRst, dLdate, dAdate, cCreateuser, nAudited, nPredefined, nDiscount, nPoints, tLtime, cItemCategoryHeadCode, nAgePer, cModiuser, cImageUrl, nSalableOnline, nDisplayOnline, nActive);
    }
}
