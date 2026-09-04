package com.c2.lc.ms.master.entities.mysql;


import javax.persistence.*;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cust_schedule_mst")
@IdClass(CustScheduleMstEntityPK.class)
public class CustScheduleMstEntity {

    private String cC2code;
    private String cCode;
    private String cName;
    private BigDecimal nWarning;
    private String cMessage;
    private LocalDate dLdate;
    private LocalDate dAdate;
    private String cCreateuser;
    private BigDecimal nAudited;
    private BigDecimal nPredefined;
    private String cShName;
    private LocalDateTime tLtime;
    private String cModiuser;
    private BigDecimal nColor;
    private BigDecimal nDocScan;
    private BigDecimal nKeepScheduleRegister;
    private BigInteger nRxNonrxFlag;

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

    @Column(name = "c_name", nullable = true, length = 40)
    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    @Column(name = "n_warning", nullable = true)
    public BigDecimal getnWarning() {
        return nWarning;
    }

    public void setnWarning(BigDecimal nWarning) {
        this.nWarning = nWarning;
    }

    @Column(name = "c_message", nullable = true, length = 100)
    public String getcMessage() {
        return cMessage;
    }

    public void setcMessage(String cMessage) {
        this.cMessage = cMessage;
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

    @Column(name = "c_sh_name", nullable = true, length = 6)
    public String getcShName() {
        return cShName;
    }

    public void setcShName(String cShName) {
        this.cShName = cShName;
    }

    @Column(name = "t_ltime", nullable = true)
    public LocalDateTime gettLtime() {
        return tLtime;
    }

    public void settLtime(LocalDateTime tLtime) {
        this.tLtime = tLtime;
    }

    @Column(name = "c_modiuser", nullable = true, length = 10)
    public String getcModiuser() {
        return cModiuser;
    }

    public void setcModiuser(String cModiuser) {
        this.cModiuser = cModiuser;
    }

    @Column(name = "n_color", nullable = true)
    public BigDecimal getnColor() {
        return nColor;
    }

    public void setnColor(BigDecimal nColor) {
        this.nColor = nColor;
    }

    @Column(name = "n_doc_scan", nullable = true)
    public BigDecimal getnDocScan() {
        return nDocScan;
    }

    public void setnDocScan(BigDecimal nDocScan) {
        this.nDocScan = nDocScan;
    }

    @Column(name = "n_keep_schedule_register", nullable = true)
    public BigDecimal getnKeepScheduleRegister() {
        return nKeepScheduleRegister;
    }

    public void setnKeepScheduleRegister(BigDecimal nKeepScheduleRegister) {
        this.nKeepScheduleRegister = nKeepScheduleRegister;
    }

    @Column(name = "n_rx_nonrx_flag", nullable = true, length = 11)
    public BigInteger getnRxNonrxFlag() {
        return nRxNonrxFlag;
    }

    public void setnRxNonrxFlag(BigInteger nRxNonrxFlag) {
        this.nRxNonrxFlag = nRxNonrxFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustScheduleMstEntity that = (CustScheduleMstEntity) o;
        return Objects.equals(cC2code, that.cC2code) && Objects.equals(cCode, that.cCode) && Objects.equals(cName, that.cName) && Objects.equals(nWarning, that.nWarning) && Objects.equals(cMessage, that.cMessage) && Objects.equals(dLdate, that.dLdate) && Objects.equals(dAdate, that.dAdate) && Objects.equals(cCreateuser, that.cCreateuser) && Objects.equals(nAudited, that.nAudited) && Objects.equals(nPredefined, that.nPredefined) && Objects.equals(cShName, that.cShName) && Objects.equals(tLtime, that.tLtime) && Objects.equals(cModiuser, that.cModiuser) && Objects.equals(nColor, that.nColor) && Objects.equals(nDocScan, that.nDocScan) && Objects.equals(nKeepScheduleRegister, that.nKeepScheduleRegister) && Objects.equals(nRxNonrxFlag, that.nRxNonrxFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cC2code, cCode, cName, nWarning, cMessage, dLdate, dAdate, cCreateuser, nAudited, nPredefined, cShName, tLtime, cModiuser, nColor, nDocScan, nKeepScheduleRegister, nRxNonrxFlag);
    }
}
