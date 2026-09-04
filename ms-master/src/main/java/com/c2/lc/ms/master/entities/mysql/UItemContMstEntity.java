package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "u_item_cont_mst")
public class UItemContMstEntity {
    private String cCode;
    private String cName;
    private String cShName;
    private String cNote;
    private String cNote1;
    private String cNote2;
    private String cNote3;
    private String cNote4;
    private String cItemScheduleCode;
    private String cItemProfileCode;
    private BigInteger nLock;
    private BigInteger nAudited;
    private BigInteger nPredefined;
    private String cCreateuser;
    private Timestamp dAdate;
    private Date dLdate;
    private Timestamp tLtime;
    private String cModiuser;
    private BigInteger nBan;
    private BigInteger nActive;
    private String cRemark;
    private BigInteger nPriceControl;
    private String cFullName;
    private BigInteger nChronic;

    @Id
    @Column(name = "c_code", nullable = false, length = 6)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    @Basic
    @Column(name = "c_name", nullable = false, length = 150)
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
    @Column(name = "c_note", nullable = true, length = 100)
    public String getcNote() {
        return cNote;
    }

    public void setcNote(String cNote) {
        this.cNote = cNote;
    }

    @Basic
    @Column(name = "c_note1", nullable = true, length = 100)
    public String getcNote1() {
        return cNote1;
    }

    public void setcNote1(String cNote1) {
        this.cNote1 = cNote1;
    }

    @Basic
    @Column(name = "c_note2", nullable = true, length = 100)
    public String getcNote2() {
        return cNote2;
    }

    public void setcNote2(String cNote2) {
        this.cNote2 = cNote2;
    }

    @Basic
    @Column(name = "c_note3", nullable = true, length = 100)
    public String getcNote3() {
        return cNote3;
    }

    public void setcNote3(String cNote3) {
        this.cNote3 = cNote3;
    }

    @Basic
    @Column(name = "c_note4", nullable = true, length = 100)
    public String getcNote4() {
        return cNote4;
    }

    public void setcNote4(String cNote4) {
        this.cNote4 = cNote4;
    }

    @Basic
    @Column(name = "c_item_schedule_code", nullable = false, length = 6)
    public String getcItemScheduleCode() {
        return cItemScheduleCode;
    }

    public void setcItemScheduleCode(String cItemScheduleCode) {
        this.cItemScheduleCode = cItemScheduleCode;
    }

    @Basic
    @Column(name = "c_item_profile_code", nullable = false, length = 6)
    public String getcItemProfileCode() {
        return cItemProfileCode;
    }

    public void setcItemProfileCode(String cItemProfileCode) {
        this.cItemProfileCode = cItemProfileCode;
    }

    @Basic
    @Column(name = "n_lock", nullable = false, precision = 0)
    public BigInteger getnLock() {
        return nLock;
    }

    public void setnLock(BigInteger nLock) {
        this.nLock = nLock;
    }

    @Basic
    @Column(name = "n_audited", nullable = false, precision = 0)
    public BigInteger getnAudited() {
        return nAudited;
    }

    public void setnAudited(BigInteger nAudited) {
        this.nAudited = nAudited;
    }

    @Basic
    @Column(name = "n_predefined", nullable = false, precision = 0)
    public BigInteger getnPredefined() {
        return nPredefined;
    }

    public void setnPredefined(BigInteger nPredefined) {
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
    public Timestamp getdAdate() {
        return dAdate;
    }

    public void setdAdate(Timestamp dAdate) {
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

    @Basic
    @Column(name = "n_ban", nullable = true, precision = 0)
    public BigInteger getnBan() {
        return nBan;
    }

    public void setnBan(BigInteger nBan) {
        this.nBan = nBan;
    }

    @Basic
    @Column(name = "n_active", nullable = true, precision = 0)
    public BigInteger getnActive() {
        return nActive;
    }

    public void setnActive(BigInteger nActive) {
        this.nActive = nActive;
    }

    @Basic
    @Column(name = "c_remark", nullable = true, length = 150)
    public String getcRemark() {
        return cRemark;
    }

    public void setcRemark(String cRemark) {
        this.cRemark = cRemark;
    }

    @Basic
    @Column(name = "n_price_control", nullable = true, precision = 0)
    public BigInteger getnPriceControl() {
        return nPriceControl;
    }

    public void setnPriceControl(BigInteger nPriceControl) {
        this.nPriceControl = nPriceControl;
    }

    @Basic
    @Column(name = "c_full_name", nullable = true, length = 500)
    public String getcFullName() {
        return cFullName;
    }

    public void setcFullName(String cFullName) {
        this.cFullName = cFullName;
    }

    @Basic
    @Column(name = "n_chronic", nullable = true, precision = 0)
    public BigInteger getnChronic() {
        return nChronic;
    }

    public void setnChronic(BigInteger nChronic) {
        this.nChronic = nChronic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UItemContMstEntity that = (UItemContMstEntity) o;
        return Objects.equals(cCode, that.cCode) &&
                Objects.equals(cName, that.cName) &&
                Objects.equals(cShName, that.cShName) &&
                Objects.equals(cNote, that.cNote) &&
                Objects.equals(cNote1, that.cNote1) &&
                Objects.equals(cNote2, that.cNote2) &&
                Objects.equals(cNote3, that.cNote3) &&
                Objects.equals(cNote4, that.cNote4) &&
                Objects.equals(cItemScheduleCode, that.cItemScheduleCode) &&
                Objects.equals(cItemProfileCode, that.cItemProfileCode) &&
                Objects.equals(nLock, that.nLock) &&
                Objects.equals(nAudited, that.nAudited) &&
                Objects.equals(nPredefined, that.nPredefined) &&
                Objects.equals(cCreateuser, that.cCreateuser) &&
                Objects.equals(dAdate, that.dAdate) &&
                Objects.equals(dLdate, that.dLdate) &&
                Objects.equals(tLtime, that.tLtime) &&
                Objects.equals(cModiuser, that.cModiuser) &&
                Objects.equals(nBan, that.nBan) &&
                Objects.equals(nActive, that.nActive) &&
                Objects.equals(cRemark, that.cRemark) &&
                Objects.equals(nPriceControl, that.nPriceControl) &&
                Objects.equals(cFullName, that.cFullName) &&
                Objects.equals(nChronic, that.nChronic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cCode, cName, cShName, cNote, cNote1, cNote2, cNote3, cNote4, cItemScheduleCode, cItemProfileCode, nLock, nAudited, nPredefined, cCreateuser, dAdate, dLdate, tLtime, cModiuser, nBan, nActive, cRemark, nPriceControl, cFullName, nChronic);
    }
}
