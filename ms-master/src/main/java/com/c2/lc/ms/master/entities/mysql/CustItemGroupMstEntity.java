package com.c2.lc.ms.master.entities.mysql;


import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cust_item_group_mst")
@IdClass(CustItemGroupMstEntityPK.class)
public class CustItemGroupMstEntity  {

    private String cC2Code;
    private String cCode;
    private String cName;
    private String cShName;
    private LocalDate dLdate;
    private LocalDate dAdate;
    private String cCreateuser;
    private BigDecimal nAudited;
    private BigDecimal nPredefined;
    private BigDecimal nPurExpDays;
    private BigDecimal nSaleExpDays;
    private LocalDateTime tLtime;
    private BigDecimal nGdnExpDays;
    private String cModiuser;

    @Id
    @Column(name = "c_c2code", nullable = true, length = 20)
    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
    }

    @Id
    @Column(name = "c_code", nullable = true, length = 6)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    @Column(name = "c_name", nullable = true, length = 60)
    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    @Column(name = "c_sh_name", nullable = true, length = 6)
    public String getcShName() {
        return cShName;
    }

    public void setcShName(String cShName) {
        this.cShName = cShName;
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

    @Column(name = "c_createuser", nullable = true, length = 10)
    public String getcCreateuser() {
        return cCreateuser;
    }

    public void setcCreateuser(String cCreateuser) {
        this.cCreateuser = cCreateuser;
    }

    @Column(name = "n_audited", nullable = true)
    public BigDecimal getnAudited() {
        return nAudited;
    }

    public void setnAudited(BigDecimal nAudited) {
        this.nAudited = nAudited;
    }

    @Column(name = "n_predefined", nullable = true)
    public BigDecimal getnPredefined() {
        return nPredefined;
    }

    public void setnPredefined(BigDecimal nPredefined) {
        this.nPredefined = nPredefined;
    }

    @Column(name = "n_pur_exp_days", nullable = true)
    public BigDecimal getnPurExpDays() {
        return nPurExpDays;
    }

    public void setnPurExpDays(BigDecimal nPurExpDays) {
        this.nPurExpDays = nPurExpDays;
    }

    @Column(name = "n_sale_exp_days", nullable = true)
    public BigDecimal getnSaleExpDays() {
        return nSaleExpDays;
    }

    public void setnSaleExpDays(BigDecimal nSaleExpDays) {
        this.nSaleExpDays = nSaleExpDays;
    }

    @Column(name = "t_ltime", nullable = true)
    public LocalDateTime gettLtime() {
        return tLtime;
    }

    public void settLtime(LocalDateTime tLtime) {
        this.tLtime = tLtime;
    }

    @Column(name = "n_gdn_exp_days", nullable = true)
    public BigDecimal getnGdnExpDays() {
        return nGdnExpDays;
    }

    public void setnGdnExpDays(BigDecimal nGdnExpDays) {
        this.nGdnExpDays = nGdnExpDays;
    }

    @Column(name = "c_modiuser", nullable = true, length = 10)
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
        CustItemGroupMstEntity that = (CustItemGroupMstEntity) o;
        return Objects.equals(cC2Code, that.cC2Code) && Objects.equals(cCode, that.cCode) && Objects.equals(cName, that.cName) && Objects.equals(cShName, that.cShName) && Objects.equals(dLdate, that.dLdate) && Objects.equals(dAdate, that.dAdate) && Objects.equals(cCreateuser, that.cCreateuser) && Objects.equals(nAudited, that.nAudited) && Objects.equals(nPredefined, that.nPredefined) && Objects.equals(nPurExpDays, that.nPurExpDays) && Objects.equals(nSaleExpDays, that.nSaleExpDays) && Objects.equals(tLtime, that.tLtime) && Objects.equals(nGdnExpDays, that.nGdnExpDays) && Objects.equals(cModiuser, that.cModiuser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cC2Code, cCode, cName, cShName, dLdate, dAdate, cCreateuser, nAudited, nPredefined, nPurExpDays, nSaleExpDays, tLtime, nGdnExpDays, cModiuser);
    }
}
