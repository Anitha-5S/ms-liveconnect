package com.c2.lc.ms.customer.entities.seller;
import com.c2.lc.ms.customer.entities.seller.pk.LoCombinedFirmEntityPK;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "lo_combined_firm_temp")
@IdClass(LoCombinedFirmEntityPK.class)
public class LoCombinedFirmEntity implements Serializable {

    private String cCode;
    private String c2Code;
    private String cName;
    private String cStateName;
    private String cStateCode;
    private String cCityName;
    private String cCityCode;
    private String cAreaName;
    private String cAreaCode;
    private String cPinCode;
    private String cAddress1;
    private String cAddress2;
    private String cMobile;
    private String cGstNo;
    private String cDrugLicenseNo1;
    private String cDrugLicenseNo2;
    private String cDrugLicenseNo3;

    @Id
    @Column(name = "c_code", nullable = false, length = 100)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    @Id
    @Column(name = "c_c2code", nullable = false, length = 100)
    public String getc2Code() {
        return c2Code;
    }

    public void setc2Code(String c2Code) {
        this.c2Code = c2Code;
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
    @Column(name = "c_state_name", length = 40)
    public String getcStateName() {
        return cStateName;
    }

    @Basic
    @Column(name = "c_state_code", length = 10)
    public String getcStateCode() {
        return cStateCode;
    }

    @Basic
    @Column(name = "c_city_name", length = 40)
    public String getcCityName() {
        return cCityName;
    }

    @Basic
    @Column(name = "c_city_code", length = 6)
    public String getcCityCode() {
        return cCityCode;
    }

    @Basic
    @Column(name = "c_area_name", length = 40)
    public String getcAreaName() {
        return cAreaName;
    }

    @Basic
    @Column(name = "c_area_code", length = 6)
    public String getcAreaCode() {
        return cAreaCode;
    }

    @Basic
    @Column(name = "c_pin", length = 6)
    public String getcPinCode() {
        return cPinCode;
    }

    @Basic
    @Column(name = "c_address_no1", length = 1024)
    public String getcAddress1() {
        return cAddress1;
    }

    @Basic
    @Column(name = "c_address_no2", length = 1024)
    public String getcAddress2() {
        return cAddress2;
    }

    @Basic
    @Column(name = "c_mobile_no", length = 10)
    public String getcMobile() {
        return cMobile;
    }

    @Basic
    @Column(name = "c_gst_no", length = 20)
    public String getcGstNo() {
        return cGstNo;
    }

    @Basic
    @Column(name = "c_drug_license_no1", length = 30)
    public String getcDrugLicenseNo1() {
        return cDrugLicenseNo1;
    }

    @Basic
    @Column(name = "c_drug_license_no2", length = 30)
    public String getcDrugLicenseNo2() {
        return cDrugLicenseNo2;
    }

    @Basic
    @Column(name = "c_drug_license_no3", length = 30)
    public String getcDrugLicenseNo3() {
        return cDrugLicenseNo3;
    }

    public void setcStateName(String cStateName) {
        this.cStateName = cStateName;
    }

    public void setcStateCode(String cStateCode) {
        this.cStateCode = cStateCode;
    }

    public void setcCityName(String cCityName) {
        this.cCityName = cCityName;
    }

    public void setcCityCode(String cCityCode) {
        this.cCityCode = cCityCode;
    }

    public void setcAreaName(String cAreaName) {
        this.cAreaName = cAreaName;
    }

    public void setcAreaCode(String cAreaCode) {
        this.cAreaCode = cAreaCode;
    }

    public void setcPinCode(String cPinCode) {
        this.cPinCode = cPinCode;
    }

    public void setcAddress1(String cAddress1) {
        this.cAddress1 = cAddress1;
    }

    public void setcAddress2(String cAddress2) {
        this.cAddress2 = cAddress2;
    }

    public void setcMobile(String cMobile) {
        this.cMobile = cMobile;
    }

    public void setcGstNo(String cGstNo) {
        this.cGstNo = cGstNo;
    }

    public void setcDrugLicenseNo1(String cDrugLicenseNo1) {
        this.cDrugLicenseNo1 = cDrugLicenseNo1;
    }

    public void setcDrugLicenseNo2(String cDrugLicenseNo2) {
        this.cDrugLicenseNo2 = cDrugLicenseNo2;
    }

    public void setcDrugLicenseNo3(String cDrugLicenseNo3) {
        this.cDrugLicenseNo3 = cDrugLicenseNo3;
    }
}
