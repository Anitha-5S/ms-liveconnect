package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "cust_scheme_special_rate", schema = "order_buk_new", catalog = "")
@IdClass(CustSchemeSpecialRateEntityPK.class)
public class CustSchemeSpecialRateEntity {
    private String cC2Code;

    @Id
    @Column(name = "c_c2code", nullable = false, length = 100)
    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
    }

    private String cCustCode;

    @Id
    @Column(name = "c_cust_code", nullable = false, length = 100)
    public String getcCustCode() {
        return cCustCode;
    }

    public void setcCustCode(String cCustCode) {
        this.cCustCode = cCustCode;
    }

    private String cItemCode;

    @Id
    @Column(name = "c_item_code", nullable = false, length = 100)
    public String getcItemCode() {
        return cItemCode;
    }

    public void setcItemCode(String cItemCode) {
        this.cItemCode = cItemCode;
    }

    private BigDecimal nRate;

    @Basic
    @Column(name = "n_rate", nullable = false, precision = 2)
    public BigDecimal getnRate() {
        return nRate;
    }

    public void setnRate(BigDecimal nRate) {
        this.nRate = nRate;
    }

    private Timestamp dTillDate;

    @Basic
    @Column(name = "d_till_date", nullable = false)
    public Timestamp getdTillDate() {
        return dTillDate;
    }

    public void setdTillDate(Timestamp dTillDate) {
        this.dTillDate = dTillDate;
    }

    private Date dLdate;

    @Basic
    @Column(name = "d_ldate", nullable = false)
    public Date getdLdate() {
        return dLdate;
    }

    public void setdLdate(Date dLdate) {
        this.dLdate = dLdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustSchemeSpecialRateEntity that = (CustSchemeSpecialRateEntity) o;
        return Objects.equals(cC2Code, that.cC2Code) &&
                Objects.equals(cCustCode, that.cCustCode) &&
                Objects.equals(cItemCode, that.cItemCode) &&
                Objects.equals(nRate, that.nRate) &&
                Objects.equals(dTillDate, that.dTillDate) &&
                Objects.equals(dLdate, that.dLdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cC2Code, cCustCode, cItemCode, nRate, dTillDate, dLdate);
    }
}
