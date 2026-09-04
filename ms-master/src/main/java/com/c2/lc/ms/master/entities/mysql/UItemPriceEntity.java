package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "u_item_price")
public class UItemPriceEntity {
    private String cCode;
    private String cBatchNo;
    private BigDecimal nMrp;
    private BigDecimal nPtr;
    private Date dExpDate;
    private Timestamp dDate;
    private Timestamp dLdate;
    private String cUser;
    private String cLuser;

    @Id
    @Column(name = "c_code", nullable = false, length = 6)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    @Basic
    @Column(name = "c_batch_no", nullable = true, length = 15)
    public String getcBatchNo() {
        return cBatchNo;
    }

    public void setcBatchNo(String cBatchNo) {
        this.cBatchNo = cBatchNo;
    }

    @Basic
    @Column(name = "n_mrp", nullable = true, precision = 2)
    public BigDecimal getnMrp() {
        return nMrp;
    }

    public void setnMrp(BigDecimal nMrp) {
        this.nMrp = nMrp;
    }

    @Basic
    @Column(name = "n_ptr", nullable = true, precision = 2)
    public BigDecimal getnPtr() {
        return nPtr;
    }

    public void setnPtr(BigDecimal nPtr) {
        this.nPtr = nPtr;
    }

    @Basic
    @Column(name = "d_exp_date", nullable = true)
    public Date getdExpDate() {
        return dExpDate;
    }

    public void setdExpDate(Date dExpDate) {
        this.dExpDate = dExpDate;
    }

    @Basic
    @Column(name = "d_date", nullable = true)
    public Timestamp getdDate() {
        return dDate;
    }

    public void setdDate(Timestamp dDate) {
        this.dDate = dDate;
    }

    @Basic
    @Column(name = "d_ldate", nullable = true)
    public Timestamp getdLdate() {
        return dLdate;
    }

    public void setdLdate(Timestamp dLdate) {
        this.dLdate = dLdate;
    }

    @Basic
    @Column(name = "c_user", nullable = true, length = 10)
    public String getcUser() {
        return cUser;
    }

    public void setcUser(String cUser) {
        this.cUser = cUser;
    }

    @Basic
    @Column(name = "c_luser", nullable = true, length = 10)
    public String getcLuser() {
        return cLuser;
    }

    public void setcLuser(String cLuser) {
        this.cLuser = cLuser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UItemPriceEntity that = (UItemPriceEntity) o;
        return Objects.equals(cCode, that.cCode) &&
                Objects.equals(cBatchNo, that.cBatchNo) &&
                Objects.equals(nMrp, that.nMrp) &&
                Objects.equals(nPtr, that.nPtr) &&
                Objects.equals(dExpDate, that.dExpDate) &&
                Objects.equals(dDate, that.dDate) &&
                Objects.equals(dLdate, that.dLdate) &&
                Objects.equals(cUser, that.cUser) &&
                Objects.equals(cLuser, that.cLuser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cCode, cBatchNo, nMrp, nPtr, dExpDate, dDate, dLdate, cUser, cLuser);
    }
}
