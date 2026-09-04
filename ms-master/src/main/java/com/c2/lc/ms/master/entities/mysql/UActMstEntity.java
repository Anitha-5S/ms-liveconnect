package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "u_act_mst")
public class UActMstEntity {
    @Id
    @Column(name = "c_ucode", nullable = false, length = 6)
    private String cUcode;
    private String cName;
    private String cShName;
    private String cFullName;
    private BigInteger nBuyer;
    private BigInteger nSeller;
    private BigInteger nRetailer;
    private BigInteger nDist;
    private BigInteger nCfa;
    private String cAdd1;
    private String cAdd2;
    private String cAdd3;
    private String cCity;
    private String cGeoAreaCode;
    private String cPin;
    private String cPhone1;
    private String cPhone2;
    private String cFax;
    private String cMainContactPerson;
    private String cMobile;
    private String cEmail;
    private String cWebsite;
    private String cActCatCode;
    private BigInteger nLock;
    private String cRemark;
    private String cGeoLat;
    private String cGeoLon;
    private Date dActivatedDate;
    private Date dDeactivatedDate;
    private String cAdd1C;
    private String cAdd2C;
    private String cAdd3C;
    private String cGeoAreaCodeC;
    private String cPinC;
    private String cCorPhNo;
    private String cCorContPerson;
    private String cBankCode;
    private String cBankActNo;
    private String cIfscCode;
    private String cMicrCode;
    private BigInteger nAudited;
    private BigInteger nPredefined;
    private String cCreateuser;
    private Timestamp dAdate;
    private Timestamp dLdate;
    private Timestamp tLtime;
    private String cModiuser;
    private BigInteger nActive;
    private String cParentCode;
    private BigInteger nUpload;
    private Date dInceptionDate;
    private String cShopType;
    private BigDecimal cSquareFeet;
    private BigInteger nAutoAlloc;
    private String cDlNo1;
    private String cDlNo2;
    private String cDocFilter;
    private String cCustPrefix;
    private String cGstNo;
    private Date dDlDate;
    private String cCsquareCode;
    private String cDlNo3;
    private String cPanNo;
    private String cCinNo;

    public String getcUcode() {
        return cUcode;
    }

    public void setcUcode(String cUcode) {
        this.cUcode = cUcode;
    }

    @Basic
    @Column(name = "c_name", nullable = false, length = 80)
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
    @Column(name = "c_full_name", nullable = true, length = 100)
    public String getcFullName() {
        return cFullName;
    }

    public void setcFullName(String cFullName) {
        this.cFullName = cFullName;
    }

    @Basic
    @Column(name = "n_buyer", nullable = false, precision = 0)
    public BigInteger getnBuyer() {
        return nBuyer;
    }

    public void setnBuyer(BigInteger nBuyer) {
        this.nBuyer = nBuyer;
    }

    @Basic
    @Column(name = "n_seller", nullable = false, precision = 0)
    public BigInteger getnSeller() {
        return nSeller;
    }

    public void setnSeller(BigInteger nSeller) {
        this.nSeller = nSeller;
    }

    @Basic
    @Column(name = "n_retailer", nullable = false, precision = 0)
    public BigInteger getnRetailer() {
        return nRetailer;
    }

    public void setnRetailer(BigInteger nRetailer) {
        this.nRetailer = nRetailer;
    }

    @Basic
    @Column(name = "n_dist", nullable = false, precision = 0)
    public BigInteger getnDist() {
        return nDist;
    }

    public void setnDist(BigInteger nDist) {
        this.nDist = nDist;
    }

    @Basic
    @Column(name = "n_cfa", nullable = false, precision = 0)
    public BigInteger getnCfa() {
        return nCfa;
    }

    public void setnCfa(BigInteger nCfa) {
        this.nCfa = nCfa;
    }

    @Basic
    @Column(name = "c_add_1", nullable = true, length = 250)
    public String getcAdd1() {
        return cAdd1;
    }

    public void setcAdd1(String cAdd1) {
        this.cAdd1 = cAdd1;
    }

    @Basic
    @Column(name = "c_add_2", nullable = true, length = 250)
    public String getcAdd2() {
        return cAdd2;
    }

    public void setcAdd2(String cAdd2) {
        this.cAdd2 = cAdd2;
    }

    @Basic
    @Column(name = "c_add_3", nullable = true, length = 250)
    public String getcAdd3() {
        return cAdd3;
    }

    public void setcAdd3(String cAdd3) {
        this.cAdd3 = cAdd3;
    }

    @Basic
    @Column(name = "c_city", nullable = true, length = 30)
    public String getcCity() {
        return cCity;
    }

    public void setcCity(String cCity) {
        this.cCity = cCity;
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
    @Column(name = "c_main_contact_person", nullable = true, length = 60)
    public String getcMainContactPerson() {
        return cMainContactPerson;
    }

    public void setcMainContactPerson(String cMainContactPerson) {
        this.cMainContactPerson = cMainContactPerson;
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
    @Column(name = "c_email", nullable = true, length = 100)
    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    @Basic
    @Column(name = "c_website", nullable = true, length = 150)
    public String getcWebsite() {
        return cWebsite;
    }

    public void setcWebsite(String cWebsite) {
        this.cWebsite = cWebsite;
    }

    @Basic
    @Column(name = "c_act_cat_code", nullable = false, length = 6)
    public String getcActCatCode() {
        return cActCatCode;
    }

    public void setcActCatCode(String cActCatCode) {
        this.cActCatCode = cActCatCode;
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
    @Column(name = "c_remark", nullable = true, length = 100)
    public String getcRemark() {
        return cRemark;
    }

    public void setcRemark(String cRemark) {
        this.cRemark = cRemark;
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
    @Column(name = "d_activated_date", nullable = true)
    public Date getdActivatedDate() {
        return dActivatedDate;
    }

    public void setdActivatedDate(Date dActivatedDate) {
        this.dActivatedDate = dActivatedDate;
    }

    @Basic
    @Column(name = "d_deactivated_date", nullable = true)
    public Date getdDeactivatedDate() {
        return dDeactivatedDate;
    }

    public void setdDeactivatedDate(Date dDeactivatedDate) {
        this.dDeactivatedDate = dDeactivatedDate;
    }

    @Basic
    @Column(name = "c_add_1_c", nullable = true, length = 40)
    public String getcAdd1C() {
        return cAdd1C;
    }

    public void setcAdd1C(String cAdd1C) {
        this.cAdd1C = cAdd1C;
    }

    @Basic
    @Column(name = "c_add_2_c", nullable = true, length = 40)
    public String getcAdd2C() {
        return cAdd2C;
    }

    public void setcAdd2C(String cAdd2C) {
        this.cAdd2C = cAdd2C;
    }

    @Basic
    @Column(name = "c_add_3_c", nullable = true, length = 40)
    public String getcAdd3C() {
        return cAdd3C;
    }

    public void setcAdd3C(String cAdd3C) {
        this.cAdd3C = cAdd3C;
    }

    @Basic
    @Column(name = "c_geo_area_code_c", nullable = false, length = 6)
    public String getcGeoAreaCodeC() {
        return cGeoAreaCodeC;
    }

    public void setcGeoAreaCodeC(String cGeoAreaCodeC) {
        this.cGeoAreaCodeC = cGeoAreaCodeC;
    }

    @Basic
    @Column(name = "c_pin_c", nullable = true, length = 6)
    public String getcPinC() {
        return cPinC;
    }

    public void setcPinC(String cPinC) {
        this.cPinC = cPinC;
    }

    @Basic
    @Column(name = "c_cor_ph_no", nullable = true, length = 15)
    public String getcCorPhNo() {
        return cCorPhNo;
    }

    public void setcCorPhNo(String cCorPhNo) {
        this.cCorPhNo = cCorPhNo;
    }

    @Basic
    @Column(name = "c_cor_cont_person", nullable = true, length = 40)
    public String getcCorContPerson() {
        return cCorContPerson;
    }

    public void setcCorContPerson(String cCorContPerson) {
        this.cCorContPerson = cCorContPerson;
    }

    @Basic
    @Column(name = "c_bank_code", nullable = false, length = 6)
    public String getcBankCode() {
        return cBankCode;
    }

    public void setcBankCode(String cBankCode) {
        this.cBankCode = cBankCode;
    }

    @Basic
    @Column(name = "c_bank_act_no", nullable = true, length = 25)
    public String getcBankActNo() {
        return cBankActNo;
    }

    public void setcBankActNo(String cBankActNo) {
        this.cBankActNo = cBankActNo;
    }

    @Basic
    @Column(name = "c_ifsc_code", nullable = true, length = 25)
    public String getcIfscCode() {
        return cIfscCode;
    }

    public void setcIfscCode(String cIfscCode) {
        this.cIfscCode = cIfscCode;
    }

    @Basic
    @Column(name = "c_micr_code", nullable = true, length = 25)
    public String getcMicrCode() {
        return cMicrCode;
    }

    public void setcMicrCode(String cMicrCode) {
        this.cMicrCode = cMicrCode;
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
    @Column(name = "n_predefined", nullable = true, precision = 0)
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
    public Timestamp getdLdate() {
        return dLdate;
    }

    public void setdLdate(Timestamp dLdate) {
        this.dLdate = dLdate;
    }

    @Basic
    @Column(name = "t_ltime", nullable = false)
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
    @Column(name = "n_active", nullable = true, precision = 0)
    public BigInteger getnActive() {
        return nActive;
    }

    public void setnActive(BigInteger nActive) {
        this.nActive = nActive;
    }

    @Basic
    @Column(name = "c_parent_code", nullable = false, length = 6)
    public String getcParentCode() {
        return cParentCode;
    }

    public void setcParentCode(String cParentCode) {
        this.cParentCode = cParentCode;
    }

    @Basic
    @Column(name = "n_upload", nullable = true, precision = 0)
    public BigInteger getnUpload() {
        return nUpload;
    }

    public void setnUpload(BigInteger nUpload) {
        this.nUpload = nUpload;
    }

    @Basic
    @Column(name = "d_inception_date", nullable = true)
    public Date getdInceptionDate() {
        return dInceptionDate;
    }

    public void setdInceptionDate(Date dInceptionDate) {
        this.dInceptionDate = dInceptionDate;
    }

    @Basic
    @Column(name = "c_shop_type", nullable = true, length = 6)
    public String getcShopType() {
        return cShopType;
    }

    public void setcShopType(String cShopType) {
        this.cShopType = cShopType;
    }

    @Basic
    @Column(name = "c_square_feet", nullable = true, precision = 3)
    public BigDecimal getcSquareFeet() {
        return cSquareFeet;
    }

    public void setcSquareFeet(BigDecimal cSquareFeet) {
        this.cSquareFeet = cSquareFeet;
    }

    @Basic
    @Column(name = "n_auto_alloc", nullable = true, precision = 0)
    public BigInteger getnAutoAlloc() {
        return nAutoAlloc;
    }

    public void setnAutoAlloc(BigInteger nAutoAlloc) {
        this.nAutoAlloc = nAutoAlloc;
    }

    @Basic
    @Column(name = "c_dl_no_1", nullable = true, length = 35)
    public String getcDlNo1() {
        return cDlNo1;
    }

    public void setcDlNo1(String cDlNo1) {
        this.cDlNo1 = cDlNo1;
    }

    @Basic
    @Column(name = "c_dl_no_2", nullable = true, length = 35)
    public String getcDlNo2() {
        return cDlNo2;
    }

    public void setcDlNo2(String cDlNo2) {
        this.cDlNo2 = cDlNo2;
    }

    @Basic
    @Column(name = "c_doc_filter", nullable = true, length = 15)
    public String getcDocFilter() {
        return cDocFilter;
    }

    public void setcDocFilter(String cDocFilter) {
        this.cDocFilter = cDocFilter;
    }

    @Basic
    @Column(name = "c_cust_prefix", nullable = true, length = 15)
    public String getcCustPrefix() {
        return cCustPrefix;
    }

    public void setcCustPrefix(String cCustPrefix) {
        this.cCustPrefix = cCustPrefix;
    }

    @Basic
    @Column(name = "c_gst_no", nullable = true, length = 20)
    public String getcGstNo() {
        return cGstNo;
    }

    public void setcGstNo(String cGstNo) {
        this.cGstNo = cGstNo;
    }

    @Basic
    @Column(name = "d_dl_date", nullable = true)
    public Date getdDlDate() {
        return dDlDate;
    }

    public void setdDlDate(Date dDlDate) {
        this.dDlDate = dDlDate;
    }

    @Basic
    @Column(name = "c_csquare_code", nullable = true, length = 20)
    public String getcCsquareCode() {
        return cCsquareCode;
    }

    public void setcCsquareCode(String cCsquareCode) {
        this.cCsquareCode = cCsquareCode;
    }

    @Basic
    @Column(name = "c_dl_no_3", nullable = true, length = 35)
    public String getcDlNo3() {
        return cDlNo3;
    }

    public void setcDlNo3(String cDlNo3) {
        this.cDlNo3 = cDlNo3;
    }

    @Basic
    @Column(name = "c_pan_no", nullable = true, length = 20)
    public String getcPanNo() {
        return cPanNo;
    }

    public void setcPanNo(String cPanNo) {
        this.cPanNo = cPanNo;
    }

    @Basic
    @Column(name = "c_cin_no", nullable = true, length = 45)
    public String getcCinNo() {
        return cCinNo;
    }

    public void setcCinNo(String cCinNo) {
        this.cCinNo = cCinNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UActMstEntity that = (UActMstEntity) o;
        return Objects.equals(cUcode, that.cUcode) &&
                Objects.equals(cName, that.cName) &&
                Objects.equals(cShName, that.cShName) &&
                Objects.equals(cFullName, that.cFullName) &&
                Objects.equals(nBuyer, that.nBuyer) &&
                Objects.equals(nSeller, that.nSeller) &&
                Objects.equals(nRetailer, that.nRetailer) &&
                Objects.equals(nDist, that.nDist) &&
                Objects.equals(nCfa, that.nCfa) &&
                Objects.equals(cAdd1, that.cAdd1) &&
                Objects.equals(cAdd2, that.cAdd2) &&
                Objects.equals(cAdd3, that.cAdd3) &&
                Objects.equals(cCity, that.cCity) &&
                Objects.equals(cGeoAreaCode, that.cGeoAreaCode) &&
                Objects.equals(cPin, that.cPin) &&
                Objects.equals(cPhone1, that.cPhone1) &&
                Objects.equals(cPhone2, that.cPhone2) &&
                Objects.equals(cFax, that.cFax) &&
                Objects.equals(cMainContactPerson, that.cMainContactPerson) &&
                Objects.equals(cMobile, that.cMobile) &&
                Objects.equals(cEmail, that.cEmail) &&
                Objects.equals(cWebsite, that.cWebsite) &&
                Objects.equals(cActCatCode, that.cActCatCode) &&
                Objects.equals(nLock, that.nLock) &&
                Objects.equals(cRemark, that.cRemark) &&
                Objects.equals(cGeoLat, that.cGeoLat) &&
                Objects.equals(cGeoLon, that.cGeoLon) &&
                Objects.equals(dActivatedDate, that.dActivatedDate) &&
                Objects.equals(dDeactivatedDate, that.dDeactivatedDate) &&
                Objects.equals(cAdd1C, that.cAdd1C) &&
                Objects.equals(cAdd2C, that.cAdd2C) &&
                Objects.equals(cAdd3C, that.cAdd3C) &&
                Objects.equals(cGeoAreaCodeC, that.cGeoAreaCodeC) &&
                Objects.equals(cPinC, that.cPinC) &&
                Objects.equals(cCorPhNo, that.cCorPhNo) &&
                Objects.equals(cCorContPerson, that.cCorContPerson) &&
                Objects.equals(cBankCode, that.cBankCode) &&
                Objects.equals(cBankActNo, that.cBankActNo) &&
                Objects.equals(cIfscCode, that.cIfscCode) &&
                Objects.equals(cMicrCode, that.cMicrCode) &&
                Objects.equals(nAudited, that.nAudited) &&
                Objects.equals(nPredefined, that.nPredefined) &&
                Objects.equals(cCreateuser, that.cCreateuser) &&
                Objects.equals(dAdate, that.dAdate) &&
                Objects.equals(dLdate, that.dLdate) &&
                Objects.equals(tLtime, that.tLtime) &&
                Objects.equals(cModiuser, that.cModiuser) &&
                Objects.equals(nActive, that.nActive) &&
                Objects.equals(cParentCode, that.cParentCode) &&
                Objects.equals(nUpload, that.nUpload) &&
                Objects.equals(dInceptionDate, that.dInceptionDate) &&
                Objects.equals(cShopType, that.cShopType) &&
                Objects.equals(cSquareFeet, that.cSquareFeet) &&
                Objects.equals(nAutoAlloc, that.nAutoAlloc) &&
                Objects.equals(cDlNo1, that.cDlNo1) &&
                Objects.equals(cDlNo2, that.cDlNo2) &&
                Objects.equals(cDocFilter, that.cDocFilter) &&
                Objects.equals(cCustPrefix, that.cCustPrefix) &&
                Objects.equals(cGstNo, that.cGstNo) &&
                Objects.equals(dDlDate, that.dDlDate) &&
                Objects.equals(cCsquareCode, that.cCsquareCode) &&
                Objects.equals(cDlNo3, that.cDlNo3) &&
                Objects.equals(cPanNo, that.cPanNo) &&
                Objects.equals(cCinNo, that.cCinNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cUcode, cName, cShName, cFullName, nBuyer, nSeller, nRetailer, nDist, nCfa, cAdd1, cAdd2, cAdd3, cCity, cGeoAreaCode, cPin, cPhone1, cPhone2, cFax, cMainContactPerson, cMobile, cEmail, cWebsite, cActCatCode, nLock, cRemark, cGeoLat, cGeoLon, dActivatedDate, dDeactivatedDate, cAdd1C, cAdd2C, cAdd3C, cGeoAreaCodeC, cPinC, cCorPhNo, cCorContPerson, cBankCode, cBankActNo, cIfscCode, cMicrCode, nAudited, nPredefined, cCreateuser, dAdate, dLdate, tLtime, cModiuser, nActive, cParentCode, nUpload, dInceptionDate, cShopType, cSquareFeet, nAutoAlloc, cDlNo1, cDlNo2, cDocFilter, cCustPrefix, cGstNo, dDlDate, cCsquareCode, cDlNo3, cPanNo, cCinNo);
    }
}
