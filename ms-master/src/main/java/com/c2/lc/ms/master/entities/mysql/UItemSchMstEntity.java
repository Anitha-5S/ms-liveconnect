package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "u_item_sch_mst")
public class UItemSchMstEntity {
    private String cCode;
    private String cName;
    private String cShName;
    private BigInteger nWarning;
    private String cMessage;
    private BigInteger nAudited;
    private BigInteger nPredefined;
    private String cCreateuser;
    private Timestamp dAdate;
    private Date dLdate;
    private byte[] tLtime;
    private String cModiuser;
    private BigInteger nPrescription;

    @Id
    @Column(name = "c_code", nullable = false, length = 6)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    @Basic
    @Column(name = "c_name", nullable = false, length = 60)
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
    @Column(name = "n_warning", nullable = false, precision = 0)
    public BigInteger getnWarning() {
        return nWarning;
    }

    public void setnWarning(BigInteger nWarning) {
        this.nWarning = nWarning;
    }

    @Basic
    @Column(name = "c_message", nullable = true, length = 150)
    public String getcMessage() {
        return cMessage;
    }

    public void setcMessage(String cMessage) {
        this.cMessage = cMessage;
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
    public byte[] gettLtime() {
        return tLtime;
    }

    public void settLtime(byte[] tLtime) {
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
    @Column(name = "n_prescription", nullable = true, precision = 0)
    public BigInteger getnPrescription() {
        return nPrescription;
    }

    public void setnPrescription(BigInteger nPrescription) {
        this.nPrescription = nPrescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UItemSchMstEntity that = (UItemSchMstEntity) o;
        return Objects.equals(cCode, that.cCode) &&
                Objects.equals(cName, that.cName) &&
                Objects.equals(cShName, that.cShName) &&
                Objects.equals(nWarning, that.nWarning) &&
                Objects.equals(cMessage, that.cMessage) &&
                Objects.equals(nAudited, that.nAudited) &&
                Objects.equals(nPredefined, that.nPredefined) &&
                Objects.equals(cCreateuser, that.cCreateuser) &&
                Objects.equals(dAdate, that.dAdate) &&
                Objects.equals(dLdate, that.dLdate) &&
                Arrays.equals(tLtime, that.tLtime) &&
                Objects.equals(cModiuser, that.cModiuser) &&
                Objects.equals(nPrescription, that.nPrescription);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(cCode, cName, cShName, nWarning, cMessage, nAudited, nPredefined, cCreateuser, dAdate, dLdate, cModiuser, nPrescription);
        result = 31 * result + Arrays.hashCode(tLtime);
        return result;
    }
}
