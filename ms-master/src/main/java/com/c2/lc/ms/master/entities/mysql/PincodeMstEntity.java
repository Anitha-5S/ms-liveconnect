package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "pincode_mst")
@IdClass(PincodeMstEntityPK.class)
public class PincodeMstEntity implements Serializable {

    private static final long serialVersionUID = 3907949499209478111L;
    private String cC2Code;
    private String cCode;
    private String cName;
    private String cDistrict;
    private String cState;
    private String cStateCode;
    private BigInteger nPredefined;
    private BigInteger nAudited;
    private Timestamp dDatetime;
    private Timestamp dLdatetime;
    private String cCreateuser;
    private String cModiuser;
    private Timestamp tTime;
    private Timestamp tLtime;

    @Id
    @Column(name = "c_c2code", nullable = false, length = 45)
    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
    }

    @Id
    @Column(name = "c_code", nullable = false, length = 6)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    @Basic
    @Column(name = "c_name", nullable = false, length = 1500)
    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    @Basic
    @Column(name = "c_district", nullable = true, length = 100)
    public String getcDistrict() {
        return cDistrict;
    }

    public void setcDistrict(String cDistrict) {
        this.cDistrict = cDistrict;
    }

    @Basic
    @Column(name = "c_state", nullable = true, length = 100)
    public String getcState() {
        return cState;
    }

    public void setcState(String cState) {
        this.cState = cState;
    }

    @Basic
    @Column(name = "c_state_code", nullable = true, length = 10)
    public String getcStateCode() {
        return cStateCode;
    }

    public void setcStateCode(String cStateCode) {
        this.cStateCode = cStateCode;
    }

    @Basic
    @Column(name = "n_predefined", nullable = true, precision = 0)
    public BigInteger getnPredefined() {
        return nPredefined;
    }

    public void setnPredefined(BigInteger nPredefined) {
        this.nPredefined = nPredefined;
    }

    @Basic
    @Column(name = "n_audited", nullable = true, precision = 0)
    public BigInteger getnAudited() {
        return nAudited;
    }

    public void setnAudited(BigInteger nAudited) {
        this.nAudited = nAudited;
    }

    @Basic
    @Column(name = "d_datetime", nullable = true)
    public Timestamp getdDatetime() {
        return dDatetime;
    }

    public void setdDatetime(Timestamp dDatetime) {
        this.dDatetime = dDatetime;
    }

    @Basic
    @Column(name = "d_ldatetime", nullable = false)
    public Timestamp getdLdatetime() {
        return dLdatetime;
    }

    public void setdLdatetime(Timestamp dLdatetime) {
        this.dLdatetime = dLdatetime;
    }

    @Basic
    @Column(name = "c_createuser", nullable = true, length = 10)
    public String getcCreateuser() {
        return cCreateuser;
    }

    public void setcCreateuser(String cCreateuser) {
        this.cCreateuser = cCreateuser;
    }

    @Basic
    @Column(name = "c_modiuser", nullable = true, length = 10)
    public String getcModiuser() {
        return cModiuser;
    }

    public void setcModiuser(String cModiuser) {
        this.cModiuser = cModiuser;
    }

    @Basic
    @Column(name = "t_time", nullable = true)
    public Timestamp gettTime() {
        return tTime;
    }

    public void settTime(Timestamp tTime) {
        this.tTime = tTime;
    }

    @Basic
    @Column(name = "t_ltime", nullable = true)
    public Timestamp gettLtime() {
        return tLtime;
    }

    public void settLtime(Timestamp tLtime) {
        this.tLtime = tLtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PincodeMstEntity that = (PincodeMstEntity) o;
        return Objects.equals(cC2Code, that.cC2Code) &&
                Objects.equals(cCode, that.cCode) &&
                Objects.equals(cName, that.cName) &&
                Objects.equals(cDistrict, that.cDistrict) &&
                Objects.equals(cState, that.cState) &&
                Objects.equals(cStateCode, that.cStateCode) &&
                Objects.equals(nPredefined, that.nPredefined) &&
                Objects.equals(nAudited, that.nAudited) &&
                Objects.equals(dDatetime, that.dDatetime) &&
                Objects.equals(dLdatetime, that.dLdatetime) &&
                Objects.equals(cCreateuser, that.cCreateuser) &&
                Objects.equals(cModiuser, that.cModiuser) &&
                Objects.equals(tTime, that.tTime) &&
                Objects.equals(tLtime, that.tLtime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cC2Code, cCode, cName, cDistrict, cState, cStateCode, nPredefined, nAudited, dDatetime, dLdatetime, cCreateuser, cModiuser, tTime, tLtime);
    }
}
