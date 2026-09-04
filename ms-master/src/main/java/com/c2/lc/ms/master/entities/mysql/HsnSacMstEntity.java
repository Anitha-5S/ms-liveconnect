package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "hsn_sac_mst")
@IdClass(HsnSacMstEntityPK.class)
public class HsnSacMstEntity {
    private String cC2Code;
    private String cCode;
    private String cName;
    private String cHeadName;
    private String cChapterName;
    private String cSubHeadName;
    private BigInteger nHsnSacFlag;
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
    @Column(name = "c_code", nullable = false, length = 8)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    @Basic
    @Column(name = "c_name", nullable = false, length = 2048)
    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    @Basic
    @Column(name = "c_head_name", nullable = true, length = 500)
    public String getcHeadName() {
        return cHeadName;
    }

    public void setcHeadName(String cHeadName) {
        this.cHeadName = cHeadName;
    }

    @Basic
    @Column(name = "c_chapter_name", nullable = true, length = 500)
    public String getcChapterName() {
        return cChapterName;
    }

    public void setcChapterName(String cChapterName) {
        this.cChapterName = cChapterName;
    }

    @Basic
    @Column(name = "c_sub_head_name", nullable = true, length = 500)
    public String getcSubHeadName() {
        return cSubHeadName;
    }

    public void setcSubHeadName(String cSubHeadName) {
        this.cSubHeadName = cSubHeadName;
    }

    @Basic
    @Column(name = "n_hsn_sac_flag", nullable = true, precision = 0)
    public BigInteger getnHsnSacFlag() {
        return nHsnSacFlag;
    }

    public void setnHsnSacFlag(BigInteger nHsnSacFlag) {
        this.nHsnSacFlag = nHsnSacFlag;
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
        HsnSacMstEntity that = (HsnSacMstEntity) o;
        return Objects.equals(cC2Code, that.cC2Code) &&
                Objects.equals(cCode, that.cCode) &&
                Objects.equals(cName, that.cName) &&
                Objects.equals(cHeadName, that.cHeadName) &&
                Objects.equals(cChapterName, that.cChapterName) &&
                Objects.equals(cSubHeadName, that.cSubHeadName) &&
                Objects.equals(nHsnSacFlag, that.nHsnSacFlag) &&
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
        return Objects.hash(cC2Code, cCode, cName, cHeadName, cChapterName, cSubHeadName, nHsnSacFlag, nPredefined, nAudited, dDatetime, dLdatetime, cCreateuser, cModiuser, tTime, tLtime);
    }
}
