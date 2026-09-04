package com.c2.lc.ms.master.entities.mysql;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "cust_pincodewise_c2code")
@IdClass(CustPincodewiseC2codeEntityPK.class)
public class CustPincodewiseC2codeEntity implements Serializable {

    private static final long serialVersionUID = 3907949499209478111L;
    @SerializedName("c_c2code")
    private String cC2Code;
    @SerializedName("c_pincode")
    private String cPincode;
    @SerializedName("c_area_name")
    private String cAreaName;
    @SerializedName("c_city")
    private String cCity;
    @SerializedName("d_date")
    @Expose(serialize = false)
    private Timestamp dDate;
    @SerializedName("d_ldate")
    @Expose(serialize = false)
    private Timestamp dLdate;

    @Id
    @Column(name = "c_c2code", nullable = false, length = 20)
    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
    }

    @Id
    @Column(name = "c_pincode", nullable = false, length = 6)
    public String getcPincode() {
        return cPincode;
    }

    public void setcPincode(String cPincode) {
        this.cPincode = cPincode;
    }

    @Column(name = "c_area_name", length = 100)
    public String getcAreaName() {
        return cAreaName;
    }

    public void setcAreaName(String cAreaName) {
        this.cAreaName = cAreaName;
    }

    @Column(name = "c_city", length = 100)
    public String getcCity() {
        return cCity;
    }

    public void setcCity(String cCity) {
        this.cCity = cCity;
    }

    @Column(name = "d_date")
    public Timestamp getdDate() {
        return dDate;
    }

    public void setdDate(Timestamp dDate) {
        this.dDate = dDate;
    }

    @SerializedName("d_ldate")
    @Column(name = "d_ldate")
    public Timestamp getdLdate() {
        return dLdate;
    }

    public void setdLdate(Timestamp dLdate) {
        this.dLdate = dLdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustPincodewiseC2codeEntity that = (CustPincodewiseC2codeEntity) o;
        return Objects.equals(cC2Code, that.cC2Code) &&
                Objects.equals(cPincode, that.cPincode) &&
                Objects.equals(cAreaName, that.cAreaName) &&
                Objects.equals(cCity, that.cCity) &&
                Objects.equals(dDate, that.dDate) &&
                Objects.equals(dLdate, that.dLdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cC2Code, cPincode, cAreaName, cCity, dDate, dLdate);
    }
}
