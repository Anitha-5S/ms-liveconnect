package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cust_cont_mst")
@IdClass(CustContMstEntityPK.class)
public class CustContMstEntity {

    private String cC2Code;
    private String cCode;
    private String cName;
    private String cShName;
    private String cNote;
    private String cNote1;
    private String cNote2;
    private String cNote3;
    private String cNote4;
    private LocalDateTime dLdate;
    private LocalDateTime dAdate;
    private String cCreateuser;
    private BigDecimal nAudited;
    private BigDecimal nPredefined;
    private LocalDateTime tLtime;
    private String cDiseaseCatCode;
    private String cScheduleCode;
    private BigDecimal nLock;
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

    @Column(name = "c_name", nullable = true, length = 1000)
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

     @Column(name = "c_note", nullable = true, length = 100)
    public String getcNote() {
        return cNote;
    }

    public void setcNote(String cNote) {
        this.cNote = cNote;
    }

     @Column(name = "c_note1", nullable = true, length = 100)
    public String getcNote1() {
        return cNote1;
    }

    public void setcNote1(String cNote1) {
        this.cNote1 = cNote1;
    }

     @Column(name = "c_note2", nullable = true, length = 100)
    public String getcNote2() {
        return cNote2;
    }

    public void setcNote2(String cNote2) {
        this.cNote2 = cNote2;
    }

     @Column(name = "c_note3", nullable = true, length = 100)
    public String getcNote3() {
        return cNote3;
    }

    public void setcNote3(String cNote3) {
        this.cNote3 = cNote3;
    }

     @Column(name = "c_note4", nullable = true, length = 100)
    public String getcNote4() {
        return cNote4;
    }

    public void setcNote4(String cNote4) {
        this.cNote4 = cNote4;
    }

     @Column(name = "d_ldate", nullable = false)
    public LocalDateTime getdLdate() {
        return dLdate;
    }

    public void setdLdate(LocalDateTime dLdate) {
        this.dLdate = dLdate;
    }

     @Column(name = "d_adate", nullable = false)
    public LocalDateTime getdAdate() {
        return dAdate;
    }

    public void setdAdate(LocalDateTime dAdate) {
        this.dAdate = dAdate;
    }

     @Column(name = "c_createuser", nullable = true, length = 10)
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

     @Column(name = "t_ltime", nullable = false)
    public LocalDateTime gettLtime() {
        return tLtime;
    }

    public void settLtime(LocalDateTime tLtime) {
        this.tLtime = tLtime;
    }

     @Column(name = "c_disease_cat_code", nullable = true, length = 6)
    public String getcDiseaseCatCode() {
        return cDiseaseCatCode;
    }

    public void setcDiseaseCatCode(String cDiseaseCatCode) {
        this.cDiseaseCatCode = cDiseaseCatCode;
    }

     @Column(name = "c_schedule_code", nullable = true, length = 6)
    public String getcScheduleCode() {
        return cScheduleCode;
    }

    public void setcScheduleCode(String cScheduleCode) {
        this.cScheduleCode = cScheduleCode;
    }

     @Column(name = "n_lock", nullable =false)
    public BigDecimal getnLock() {
        return nLock;
    }

    public void setnLock(BigDecimal nLock) {
        this.nLock = nLock;
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
        CustContMstEntity that = (CustContMstEntity) o;
        return Objects.equals(cC2Code, that.cC2Code) && Objects.equals(cCode, that.cCode) && Objects.equals(cName, that.cName) && Objects.equals(cShName, that.cShName) && Objects.equals(cNote, that.cNote) && Objects.equals(cNote1, that.cNote1) && Objects.equals(cNote2, that.cNote2) && Objects.equals(cNote3, that.cNote3) && Objects.equals(cNote4, that.cNote4) && Objects.equals(dLdate, that.dLdate) && Objects.equals(dAdate, that.dAdate) && Objects.equals(cCreateuser, that.cCreateuser) && Objects.equals(nAudited, that.nAudited) && Objects.equals(nPredefined, that.nPredefined) && Objects.equals(tLtime, that.tLtime) && Objects.equals(cDiseaseCatCode, that.cDiseaseCatCode) && Objects.equals(cScheduleCode, that.cScheduleCode) && Objects.equals(nLock, that.nLock) && Objects.equals(cModiuser, that.cModiuser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cC2Code, cCode, cName, cShName, cNote, cNote1, cNote2, cNote3, cNote4, dLdate, dAdate, cCreateuser, nAudited, nPredefined, tLtime, cDiseaseCatCode, cScheduleCode, nLock, cModiuser);
    }
}
