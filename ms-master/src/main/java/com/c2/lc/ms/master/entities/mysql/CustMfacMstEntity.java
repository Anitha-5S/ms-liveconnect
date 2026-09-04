package com.c2.lc.ms.master.entities.mysql;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "cust_mfac_mst")
@IdClass(CustMfacMstEntityPK.class)
public class CustMfacMstEntity {
    private String cC2code;
    private String cCode;
    private String cNname;
    private String cShName;
    private String cAdd1;
    private String cAdd2;
    private String cAdd3;
    private String cCity;
    private String cPincode;
    private String cPhone1;
    private String cPhone2;
    private String cFax;
    private String cContactPerson;
    private Integer nLock;
    private String cDrugLicenceNo1;
    private String cDrugLicenceNo2;
    private String cStNo;
    private String cCstNo;
    private String cEmail;
    private LocalDate dLdate;
    private LocalDate dAdate;
    private String cCreateuser;
    private Integer nAudited;
    private Integer nPredefined;
    private String cGeoLat;
    private String cGeoLon;
    private String cMfacGroupCode;
    private String cAreaCode;
    private String cModifyUser;
    private String cFullName;
    private BigDecimal nSalableOnline;
    private BigDecimal nStocksale;
    private BigDecimal nBlockCrnt;

    @Id
    @Column(name = "c_c2code", nullable = true, length = 6)
    public String getcC2code() {
        return cC2code;
    }

    public void setcC2code(String cC2code) {
        this.cC2code = cC2code;
    }

    @Id
    @Column(name = "c_code", nullable = true, length = 100)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    @Column(name = "c_name", nullable = true, length = 255)
    public String getcNname() {
        return cNname;
    }

    public void setcNname(String cNname) {
        this.cNname = cNname;
    }

    @Column(name = "c_sh_name", nullable = true, length = 20)
    public String getcShName() {
        return cShName;
    }

    public void setcShName(String cShName) {
        this.cShName = cShName;
    }

    @Column(name = "c_add_1", nullable = true, length = 200)
    public String getcAdd1() {
        return cAdd1;
    }

    public void setcAdd1(String cAdd1) {
        this.cAdd1 = cAdd1;
    }

    @Column(name = "c_add_2", nullable = true, length = 200)
    public String getcAdd2() {
        return cAdd2;
    }

    public void setcAdd2(String cAdd2) {
        this.cAdd2 = cAdd2;
    }

    @Column(name = "c_add_3", nullable = true, length = 200)
    public String getcAdd3() {
        return cAdd3;
    }

    public void setcAdd3(String cAdd3) {
        this.cAdd3 = cAdd3;
    }

    @Column(name = "c_city", nullable = true, length = 100)
    public String getcCity() {
        return cCity;
    }

    public void setcCity(String cCity) {
        this.cCity = cCity;
    }

    @Column(name = "c_pincode", nullable = true, length = 6)
    public String getcPincode() {
        return cPincode;
    }

    public void setcPincode(String cPincode) {
        this.cPincode = cPincode;
    }

    @Column(name = "c_phone_1", nullable = true, length = 10)
    public String getcPhone1() {
        return cPhone1;
    }

    public void setcPhone1(String cPhone1) {
        this.cPhone1 = cPhone1;
    }

    @Column(name = "c_phone_2", nullable = true, length = 10)
    public String getcPhone2() {
        return cPhone2;
    }

    public void setcPhone2(String cPhone2) {
        this.cPhone2 = cPhone2;
    }

    @Column(name = "c_fax", nullable = true, length = 20)
    public String getcFax() {
        return cFax;
    }

    public void setcFax(String cFax) {
        this.cFax = cFax;
    }

    @Column(name = "c_contact_person", nullable = true, length = 200)
    public String getcContactPerson() {
        return cContactPerson;
    }

    public void setcContactPerson(String cContactPerson) {
        this.cContactPerson = cContactPerson;
    }

    @Column(name = "n_lock", nullable = true, length = 11)
    public Integer getnLock() {
        return nLock;
    }

    public void setnLock(Integer nLock) {
        this.nLock = nLock;
    }

    @Column(name = "c_drug_licence_no_1", nullable = true, length = 100)
    public String getcDrugLicenceNo1() {
        return cDrugLicenceNo1;
    }

    public void setcDrugLicenceNo1(String cDrugLicenceNo1) {
        this.cDrugLicenceNo1 = cDrugLicenceNo1;
    }

    @Column(name = "c_drug_licence_no_2", nullable = true, length = 100)
    public String getcDrugLicenceNo2() {
        return cDrugLicenceNo2;
    }

    public void setcDrugLicenceNo2(String cDrugLicenceNo2) {
        this.cDrugLicenceNo2 = cDrugLicenceNo2;
    }

    @Column(name = "c_st_no", nullable = true, length = 200)
    public String getcStNo() {
        return cStNo;
    }

    public void setcStNo(String cStNo) {
        this.cStNo = cStNo;
    }

    @Column(name = "c_cst_no", nullable = true, length = 200)
    public String getcCstNo() {
        return cCstNo;
    }

    public void setcCstNo(String cCstNo) {
        this.cCstNo = cCstNo;
    }

    @Column(name = "c_email", nullable = true, length = 200)
    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
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

    @Column(name = "c_createuser", nullable = true, length = 50)
    public String getcCreateuser() {
        return cCreateuser;
    }

    public void setcCreateuser(String cCreateuser) {
        this.cCreateuser = cCreateuser;
    }

    @Column(name = "n_audited", nullable = true, length = 11)
    public Integer getnAudited() {
        return nAudited;
    }

    public void setnAudited(Integer nAudited) {
        this.nAudited = nAudited;
    }

    @Column(name = "n_predefined", nullable = true, length = 11)
    public Integer getnPredefined() {
        return nPredefined;
    }

    public void setnPredefined(Integer nPredefined) {
        this.nPredefined = nPredefined;
    }

    @Column(name = "c_geo_lat", nullable = true, length = 20)
    public String getcGeoLat() {
        return cGeoLat;
    }

    public void setcGeoLat(String cGeoLat) {
        this.cGeoLat = cGeoLat;
    }

    @Column(name = "c_geo_lon", nullable = true, length = 20)
    public String getcGeoLon() {
        return cGeoLon;
    }

    public void setcGeoLon(String cGeoLon) {
        this.cGeoLon = cGeoLon;
    }

    @Column(name = "c_mfac_group_code", nullable = true, length = 50)
    public String getcMfacGroupCode() {
        return cMfacGroupCode;
    }

    public void setcMfacGroupCode(String cMfacGroupCode) {
        this.cMfacGroupCode = cMfacGroupCode;
    }

    @Column(name = "c_area_code", nullable = true, length = 50)
    public String getcAreaCode() {
        return cAreaCode;
    }

    public void setcAreaCode(String cAreaCode) {
        this.cAreaCode = cAreaCode;
    }

    @Column(name = "c_modify_user", nullable = true, length = 50)
    public String getcModifyUser() {
        return cModifyUser;
    }

    public void setcModifyUser(String cModifyUser) {
        this.cModifyUser = cModifyUser;
    }

    @Column(name = "c_full_name", nullable = true, length = 250)
    public String getcFullName() {
        return cFullName;
    }

    public void setcFullName(String cFullName) {
        this.cFullName = cFullName;
    }

    @Column(name = "n_salable_online", nullable = false)
    public BigDecimal getnSalableOnline() {
        return nSalableOnline;
    }

    public void setnSalableOnline(BigDecimal nSalableOnline) {
        this.nSalableOnline = nSalableOnline;
    }

    @Column(name = "n_stocksale", nullable = false)
    public BigDecimal getnStocksale() {
        return nStocksale;
    }

    public void setnStocksale(BigDecimal nStocksale) {
        this.nStocksale = nStocksale;
    }

    @Column(name = "n_block_crnt", nullable = false)
    public BigDecimal getnBlockCrnt() {
        return nBlockCrnt;
    }

    public void setnBlockCrnt(BigDecimal nBlockCrnt) {
        this.nBlockCrnt = nBlockCrnt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustMfacMstEntity that = (CustMfacMstEntity) o;
        return Objects.equals(cC2code, that.cC2code) && Objects.equals(cCode, that.cCode) && Objects.equals(cNname, that.cNname) && Objects.equals(cShName, that.cShName) && Objects.equals(cAdd1, that.cAdd1) && Objects.equals(cAdd2, that.cAdd2) && Objects.equals(cAdd3, that.cAdd3) && Objects.equals(cCity, that.cCity) && Objects.equals(cPincode, that.cPincode) && Objects.equals(cPhone1, that.cPhone1) && Objects.equals(cPhone2, that.cPhone2) && Objects.equals(cFax, that.cFax) && Objects.equals(cContactPerson, that.cContactPerson) && Objects.equals(nLock, that.nLock) && Objects.equals(cDrugLicenceNo1, that.cDrugLicenceNo1) && Objects.equals(cDrugLicenceNo2, that.cDrugLicenceNo2) && Objects.equals(cStNo, that.cStNo) && Objects.equals(cCstNo, that.cCstNo) && Objects.equals(cEmail, that.cEmail) && Objects.equals(dLdate, that.dLdate) && Objects.equals(dAdate, that.dAdate) && Objects.equals(cCreateuser, that.cCreateuser) && Objects.equals(nAudited, that.nAudited) && Objects.equals(nPredefined, that.nPredefined) && Objects.equals(cGeoLat, that.cGeoLat) && Objects.equals(cGeoLon, that.cGeoLon) && Objects.equals(cMfacGroupCode, that.cMfacGroupCode) && Objects.equals(cAreaCode, that.cAreaCode) && Objects.equals(cModifyUser, that.cModifyUser) && Objects.equals(cFullName, that.cFullName) && Objects.equals(nSalableOnline, that.nSalableOnline) && Objects.equals(nStocksale, that.nStocksale) && Objects.equals(nBlockCrnt, that.nBlockCrnt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cC2code, cCode, cNname, cShName, cAdd1, cAdd2, cAdd3, cCity, cPincode, cPhone1, cPhone2, cFax, cContactPerson, nLock, cDrugLicenceNo1, cDrugLicenceNo2, cStNo, cCstNo, cEmail, dLdate, dAdate, cCreateuser, nAudited, nPredefined, cGeoLat, cGeoLon, cMfacGroupCode, cAreaCode, cModifyUser, cFullName, nSalableOnline, nStocksale, nBlockCrnt);
    }
}
