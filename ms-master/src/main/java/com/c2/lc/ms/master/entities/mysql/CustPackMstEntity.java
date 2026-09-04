package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cust_pack_mst")
@IdClass(CustPackMstEntityPK.class)
public class CustPackMstEntity {
    private String cC2code;
    private String cCode;
    private String cName;
    private LocalDate dLdate;
    private LocalDate dAdate;
    private String cCreateuser;
    private BigInteger nAudited;
    private BigInteger nPredefined;
    private LocalDateTime tLtime;
    private String cModiuser;

    @Id
    @Column(name = "c_c2code", nullable = true, length = 6)
    public String getcC2code() {
        return cC2code;
    }

    public void setcC2code(String cC2code) {
        this.cC2code = cC2code;
    }

    @Id
    @Column(name = "c_code", nullable = true, length = 100)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    @Basic
    @Column(name = "c_name", nullable = true, length = 35)
    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    @Basic
    @Column(name = "d_ldate", nullable = false)
    public LocalDate getdLdate() {
        return dLdate;
    }

    public void setdLdate(LocalDate dLdate) {
        this.dLdate = dLdate;
    }

    @Basic
    @Column(name = "d_adate", nullable = false)
    public LocalDate getdAdate() {
        return dAdate;
    }

    public void setdAdate(LocalDate dAdate) {
        this.dAdate = dAdate;
    }

    @Basic
    @Column(name = "c_createuser", nullable = false)
    public String getcCreateuser() {
        return cCreateuser;
    }

    public void setcCreateuser(String cCreateuser) {
        this.cCreateuser = cCreateuser;
    }

    @Basic
    @Column(name = "n_audited", nullable = false)
    public BigInteger getnAudited() {
        return nAudited;
    }

    public void setnAudited(BigInteger nAudited) {
        this.nAudited = nAudited;
    }

    @Basic
    @Column(name = "n_predefined", nullable = false)
    public BigInteger getnPredefined() {
        return nPredefined;
    }

    public void setnPredefined(BigInteger nPredefined) {
        this.nPredefined = nPredefined;
    }

    @Basic
    @Column(name = "t_ltime", nullable = false)
    public LocalDateTime gettLtime() {
        return tLtime;
    }

    public void settLtime(LocalDateTime tLtime) {
        this.tLtime = tLtime;
    }

    @Basic
    @Column(name = "c_modiuser", nullable = false)
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
        CustPackMstEntity that = (CustPackMstEntity) o;
        return Objects.equals(cC2code, that.cC2code) && Objects.equals(cCode, that.cCode) && Objects.equals(cName, that.cName) && Objects.equals(dLdate, that.dLdate) && Objects.equals(dAdate, that.dAdate) && Objects.equals(cCreateuser, that.cCreateuser) && Objects.equals(nAudited, that.nAudited) && Objects.equals(nPredefined, that.nPredefined) && Objects.equals(tLtime, that.tLtime) && Objects.equals(cModiuser, that.cModiuser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cC2code, cCode, cName, dLdate, dAdate, cCreateuser, nAudited, nPredefined, tLtime, cModiuser);
    }
}
