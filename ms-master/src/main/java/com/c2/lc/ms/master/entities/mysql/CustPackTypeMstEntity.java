package com.c2.lc.ms.master.entities.mysql;


import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cust_pack_type_mst")
@IdClass(CustPackMstEntityPK.class)
public class CustPackTypeMstEntity {

    private String cC2code;
    private String cCode;
    private String cName;
    private LocalDate dLdate;
    private LocalDate dAdate;
    private String cCreateuser;
    private BigInteger nAudited;
    private BigInteger nPredefined;
    private String cShName;
    private LocalDateTime tLtime;
    private String cModiuser;

    @Id
     @Column(name = "c_c2code", nullable = true, length = 20)
    public String getcC2code() {
        return cC2code;
    }

    public void setcC2code(String cC2code) {
        this.cC2code = cC2code;
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

    @Column(name = "c_createuser", nullable = false)
    public String getcCreateuser() {
        return cCreateuser;
    }

    public void setcCreateuser(String cCreateuser) {
        this.cCreateuser = cCreateuser;
    }

    @Column(name = "n_audited", nullable = false)
    public BigInteger getnAudited() {
        return nAudited;
    }

    public void setnAudited(BigInteger nAudited) {
        this.nAudited = nAudited;
    }

    @Column(name = "n_predefined", nullable = true, length = 6)
    public BigInteger getnPredefined() {
        return nPredefined;
    }

    public void setnPredefined(BigInteger nPredefined) {
        this.nPredefined = nPredefined;
    }

    @Column(name = "c_sh_name", nullable = false)
    public String getcShName() {
        return cShName;
    }

    public void setcShName(String cShName) {
        this.cShName = cShName;
    }

    @Column(name = "t_ltime", nullable = false)
    public LocalDateTime gettLtime() {
        return tLtime;
    }

    public void settLtime(LocalDateTime tLtime) {
        this.tLtime = tLtime;
    }

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
        CustPackTypeMstEntity that = (CustPackTypeMstEntity) o;
        return Objects.equals(cC2code, that.cC2code) && Objects.equals(cCode, that.cCode) && Objects.equals(cName, that.cName) && Objects.equals(dLdate, that.dLdate) && Objects.equals(dAdate, that.dAdate) && Objects.equals(cCreateuser, that.cCreateuser) && Objects.equals(nAudited, that.nAudited) && Objects.equals(nPredefined, that.nPredefined) && Objects.equals(cShName, that.cShName) && Objects.equals(tLtime, that.tLtime) && Objects.equals(cModiuser, that.cModiuser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cC2code, cCode, cName, dLdate, dAdate, cCreateuser, nAudited, nPredefined, cShName, tLtime, cModiuser);
    }
}
