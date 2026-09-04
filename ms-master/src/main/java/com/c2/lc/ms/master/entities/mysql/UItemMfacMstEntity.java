package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "u_item_mfac_mst")
public class UItemMfacMstEntity {
    private String cCode;
    private String cName;
    private String cShName;
    private String cMfacGroupCode;
    private String cAdd1;
    private String cAdd2;
    private String cAdd3;
    private String cGeoAreaCode;
    private String cPin;
    private String cContactPerson;
    private String cMobile;
    private String cPhone1;
    private String cPhone2;
    private String cFax;
    private String cEmail;
    private BigInteger nLock;
    private String cTradeLicenceNo1;
    private String cTradeLicenceNo2;
    private String cTradeLicenceNo3;
    private String cGeoLat;
    private String cGeoLon;
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
    private BigDecimal nCstPer;
    private String cNote;
    private String cWebsite;

    @Id
    @Column(name = "c_code", nullable = false, length = 6)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    @Basic
    @Column(name = "c_name", nullable = false, length = 100)
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
    @Column(name = "c_mfac_group_code", nullable = false, length = 6)
    public String getcMfacGroupCode() {
        return cMfacGroupCode;
    }

    public void setcMfacGroupCode(String cMfacGroupCode) {
        this.cMfacGroupCode = cMfacGroupCode;
    }

    @Basic
    @Column(name = "c_add_1", nullable = true, length = 40)
    public String getcAdd1() {
        return cAdd1;
    }

    public void setcAdd1(String cAdd1) {
        this.cAdd1 = cAdd1;
    }

    @Basic
    @Column(name = "c_add_2", nullable = true, length = 40)
    public String getcAdd2() {
        return cAdd2;
    }

    public void setcAdd2(String cAdd2) {
        this.cAdd2 = cAdd2;
    }

    @Basic
    @Column(name = "c_add_3", nullable = true, length = 40)
    public String getcAdd3() {
        return cAdd3;
    }

    public void setcAdd3(String cAdd3) {
        this.cAdd3 = cAdd3;
    }

    @Basic
    @Column(name = "c_geo_area_code", nullable = false, length = 6)
    public String getcGeoAreaCode() {
        return cGeoAreaCode;
    }

    public void setcGeoAreaCode(String cGeoAreaCode) {
        this.cGeoAreaCode = cGeoAreaCode;
    }

    @Basic
    @Column(name = "c_pin", nullable = true, length = 6)
    public String getcPin() {
        return cPin;
    }

    public void setcPin(String cPin) {
        this.cPin = cPin;
    }

    @Basic
    @Column(name = "c_contact_person", nullable = true, length = 40)
    public String getcContactPerson() {
        return cContactPerson;
    }

    public void setcContactPerson(String cContactPerson) {
        this.cContactPerson = cContactPerson;
    }

    @Basic
    @Column(name = "c_mobile", nullable = true, length = 20)
    public String getcMobile() {
        return cMobile;
    }

    public void setcMobile(String cMobile) {
        this.cMobile = cMobile;
    }

    @Basic
    @Column(name = "c_phone_1", nullable = true, length = 20)
    public String getcPhone1() {
        return cPhone1;
    }

    public void setcPhone1(String cPhone1) {
        this.cPhone1 = cPhone1;
    }

    @Basic
    @Column(name = "c_phone_2", nullable = true, length = 20)
    public String getcPhone2() {
        return cPhone2;
    }

    public void setcPhone2(String cPhone2) {
        this.cPhone2 = cPhone2;
    }

    @Basic
    @Column(name = "c_fax", nullable = true, length = 20)
    public String getcFax() {
        return cFax;
    }

    public void setcFax(String cFax) {
        this.cFax = cFax;
    }

    @Basic
    @Column(name = "c_email", nullable = true, length = 100)
    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
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
    @Column(name = "c_trade_licence_no_1", nullable = true, length = 30)
    public String getcTradeLicenceNo1() {
        return cTradeLicenceNo1;
    }

    public void setcTradeLicenceNo1(String cTradeLicenceNo1) {
        this.cTradeLicenceNo1 = cTradeLicenceNo1;
    }

    @Basic
    @Column(name = "c_trade_licence_no_2", nullable = true, length = 30)
    public String getcTradeLicenceNo2() {
        return cTradeLicenceNo2;
    }

    public void setcTradeLicenceNo2(String cTradeLicenceNo2) {
        this.cTradeLicenceNo2 = cTradeLicenceNo2;
    }

    @Basic
    @Column(name = "c_trade_licence_no_3", nullable = true, length = 30)
    public String getcTradeLicenceNo3() {
        return cTradeLicenceNo3;
    }

    public void setcTradeLicenceNo3(String cTradeLicenceNo3) {
        this.cTradeLicenceNo3 = cTradeLicenceNo3;
    }

    @Basic
    @Column(name = "c_geo_lat", nullable = true, length = 12)
    public String getcGeoLat() {
        return cGeoLat;
    }

    public void setcGeoLat(String cGeoLat) {
        this.cGeoLat = cGeoLat;
    }

    @Basic
    @Column(name = "c_geo_lon", nullable = true, length = 12)
    public String getcGeoLon() {
        return cGeoLon;
    }

    public void setcGeoLon(String cGeoLon) {
        this.cGeoLon = cGeoLon;
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
    @Column(name = "n_cst_per", nullable = true, precision = 2)
    public BigDecimal getnCstPer() {
        return nCstPer;
    }

    public void setnCstPer(BigDecimal nCstPer) {
        this.nCstPer = nCstPer;
    }

    @Basic
    @Column(name = "c_note", nullable = true, length = 150)
    public String getcNote() {
        return cNote;
    }

    public void setcNote(String cNote) {
        this.cNote = cNote;
    }

    @Basic
    @Column(name = "c_website", nullable = true, length = 150)
    public String getcWebsite() {
        return cWebsite;
    }

    public void setcWebsite(String cWebsite) {
        this.cWebsite = cWebsite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UItemMfacMstEntity that = (UItemMfacMstEntity) o;
        return Objects.equals(cCode, that.cCode) &&
                Objects.equals(cName, that.cName) &&
                Objects.equals(cShName, that.cShName) &&
                Objects.equals(cMfacGroupCode, that.cMfacGroupCode) &&
                Objects.equals(cAdd1, that.cAdd1) &&
                Objects.equals(cAdd2, that.cAdd2) &&
                Objects.equals(cAdd3, that.cAdd3) &&
                Objects.equals(cGeoAreaCode, that.cGeoAreaCode) &&
                Objects.equals(cPin, that.cPin) &&
                Objects.equals(cContactPerson, that.cContactPerson) &&
                Objects.equals(cMobile, that.cMobile) &&
                Objects.equals(cPhone1, that.cPhone1) &&
                Objects.equals(cPhone2, that.cPhone2) &&
                Objects.equals(cFax, that.cFax) &&
                Objects.equals(cEmail, that.cEmail) &&
                Objects.equals(nLock, that.nLock) &&
                Objects.equals(cTradeLicenceNo1, that.cTradeLicenceNo1) &&
                Objects.equals(cTradeLicenceNo2, that.cTradeLicenceNo2) &&
                Objects.equals(cTradeLicenceNo3, that.cTradeLicenceNo3) &&
                Objects.equals(cGeoLat, that.cGeoLat) &&
                Objects.equals(cGeoLon, that.cGeoLon) &&
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
                Objects.equals(nCstPer, that.nCstPer) &&
                Objects.equals(cNote, that.cNote) &&
                Objects.equals(cWebsite, that.cWebsite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cCode, cName, cShName, cMfacGroupCode, cAdd1, cAdd2, cAdd3, cGeoAreaCode, cPin, cContactPerson, cMobile, cPhone1, cPhone2, cFax, cEmail, nLock, cTradeLicenceNo1, cTradeLicenceNo2, cTradeLicenceNo3, cGeoLat, cGeoLon, nAudited, nPredefined, cCreateuser, dAdate, dLdate, tLtime, cModiuser, nBan, nActive, cRemark, nCstPer, cNote, cWebsite);
    }
}
