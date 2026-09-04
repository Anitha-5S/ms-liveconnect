package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "u_item_molecule_mst")
public class UItemMoleculeMstEntity {
    private String cCode;
    private String cName;
    private String cShName;
    private String cNote;
    private String cNote1;
    private String cNote2;
    private String cNote3;
    private String cNote4;
    private String cItemProfileCode;
    private String cScheduleCode;
    private BigInteger nLock;
    private BigInteger nAudited;
    private BigInteger nPredefined;
    private String cCreateuser;
    private Timestamp dAdate;
    private Date dLdate;
    private Timestamp tLtime;
    private String cModiuser;
    private String cIndication;
    private String cAdministration;
    private String cContraIndication;
    private String cDosage;
    private String cSpecialPrecaution;
    private String cAdverseReaction;
    private String cDrugInteraction;
    private String cPregnancy;
    private String cOtherDet;

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
    @Column(name = "c_sh_name", nullable = true, length = 6)
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
    @Column(name = "c_item_profile_code", nullable = true, length = 6)
    public String getcItemProfileCode() {
        return cItemProfileCode;
    }

    public void setcItemProfileCode(String cItemProfileCode) {
        this.cItemProfileCode = cItemProfileCode;
    }

    @Basic
    @Column(name = "c_schedule_code", nullable = true, length = 6)
    public String getcScheduleCode() {
        return cScheduleCode;
    }

    public void setcScheduleCode(String cScheduleCode) {
        this.cScheduleCode = cScheduleCode;
    }

    @Basic
    @Column(name = "n_lock", nullable = true, precision = 0)
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
    @Column(name = "d_ldate", nullable = true)
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
    @Column(name = "c_modiuser", nullable = true, length = 10)
    public String getcModiuser() {
        return cModiuser;
    }

    public void setcModiuser(String cModiuser) {
        this.cModiuser = cModiuser;
    }

    @Basic
    @Column(name = "c_indication")
    public String getcIndication() {
        return cIndication;
    }

    public void setcIndication(String cIndication) {
        this.cIndication = cIndication;
    }

    @Basic
    @Column(name = "c_administration")
    public String getcAdministration() {
        return cAdministration;
    }

    public void setcAdministration(String cAdministration) {
        this.cAdministration = cAdministration;
    }

    @Basic
    @Column(name = "c_contra_indication")
    public String getcContraIndication() {
        return cContraIndication;
    }

    public void setcContraIndication(String cContraIndication) {
        this.cContraIndication = cContraIndication;
    }

    @Basic
    @Column(name = "c_dosage")
    public String getcDosage() {
        return cDosage;
    }

    public void setcDosage(String cDosage) {
        this.cDosage = cDosage;
    }

    @Basic
    @Column(name = "c_special_precaution")
    public String getcSpecialPrecaution() {
        return cSpecialPrecaution;
    }

    public void setcSpecialPrecaution(String cSpecialPrecaution) {
        this.cSpecialPrecaution = cSpecialPrecaution;
    }

    @Basic
    @Column(name = "c_adverse_reaction")
    public String getcAdverseReaction() {
        return cAdverseReaction;
    }

    public void setcAdverseReaction(String cAdverseReaction) {
        this.cAdverseReaction = cAdverseReaction;
    }

    @Basic
    @Column(name = "c_drug_interaction")
    public String getcDrugInteraction() {
        return cDrugInteraction;
    }

    public void setcDrugInteraction(String cDrugInteraction) {
        this.cDrugInteraction = cDrugInteraction;
    }

    @Basic
    @Column(name = "c_pregnancy")
    public String getcPregnancy() {
        return cPregnancy;
    }

    public void setcPregnancy(String cPregnancy) {
        this.cPregnancy = cPregnancy;
    }

    @Basic
    @Column(name = "c_other_det")
    public String getcOtherDet() {
        return cOtherDet;
    }

    public void setcOtherDet(String cOtherDet) {
        this.cOtherDet = cOtherDet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UItemMoleculeMstEntity that = (UItemMoleculeMstEntity) o;
        return Objects.equals(cCode, that.cCode) &&
                Objects.equals(cName, that.cName) &&
                Objects.equals(cShName, that.cShName) &&
                Objects.equals(cNote, that.cNote) &&
                Objects.equals(cNote1, that.cNote1) &&
                Objects.equals(cNote2, that.cNote2) &&
                Objects.equals(cNote3, that.cNote3) &&
                Objects.equals(cNote4, that.cNote4) &&
                Objects.equals(cItemProfileCode, that.cItemProfileCode) &&
                Objects.equals(cScheduleCode, that.cScheduleCode) &&
                Objects.equals(nLock, that.nLock) &&
                Objects.equals(nAudited, that.nAudited) &&
                Objects.equals(nPredefined, that.nPredefined) &&
                Objects.equals(cCreateuser, that.cCreateuser) &&
                Objects.equals(dAdate, that.dAdate) &&
                Objects.equals(dLdate, that.dLdate) &&
                Objects.equals(tLtime, that.tLtime) &&
                Objects.equals(cModiuser, that.cModiuser) &&
                Objects.equals(cIndication, that.cIndication) &&
                Objects.equals(cAdministration, that.cAdministration) &&
                Objects.equals(cContraIndication, that.cContraIndication) &&
                Objects.equals(cDosage, that.cDosage) &&
                Objects.equals(cSpecialPrecaution, that.cSpecialPrecaution) &&
                Objects.equals(cAdverseReaction, that.cAdverseReaction) &&
                Objects.equals(cDrugInteraction, that.cDrugInteraction) &&
                Objects.equals(cPregnancy, that.cPregnancy) &&
                Objects.equals(cOtherDet, that.cOtherDet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cCode, cName, cShName, cNote, cNote1, cNote2, cNote3, cNote4, cItemProfileCode, cScheduleCode, nLock, nAudited, nPredefined, cCreateuser, dAdate, dLdate, tLtime, cModiuser, cIndication, cAdministration, cContraIndication, cDosage, cSpecialPrecaution, cAdverseReaction, cDrugInteraction, cPregnancy, cOtherDet);
    }
}
