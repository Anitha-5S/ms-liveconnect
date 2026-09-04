package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "lc_ucode_batch_details")
@IdClass(LcUcodeBatchDetailsEntityPK.class)
public class LcUcodeBatchDetailsEntity {
    private String cUcode;
    private String cBatchNo;
    private BigDecimal nMrp;
    private Date dExpDate;
    private String cBatchKey;
    private Timestamp dDate;
    private Timestamp dLdate;
    private BigDecimal nSaleRate;

    @Id
    @Column(name = "c_ucode", nullable = false, length = 100)
    public String getcUcode() {
        return cUcode;
    }

    public void setcUcode(String cUcode) {
        this.cUcode = cUcode;
    }

    @Id
    @Column(name = "c_batch_no", nullable = false, length = 100)
    public String getcBatchNo() {
        return cBatchNo;
    }

    public void setcBatchNo(String cBatchNo) {
        this.cBatchNo = cBatchNo;
    }

    @Basic
    @Column(name = "n_mrp", nullable = true, precision = 3)
    public BigDecimal getnMrp() {
        return nMrp;
    }

    public void setnMrp(BigDecimal nMrp) {
        this.nMrp = nMrp;
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
    @Column(name = "c_batch_key", nullable = true, length = 100)
    public String getcBatchKey() {
        return cBatchKey;
    }

    public void setcBatchKey(String cBatchKey) {
        this.cBatchKey = cBatchKey;
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
    @Column(name = "n_sale_rate", nullable = true, precision = 3)
    public BigDecimal getnSaleRate() {
        return nSaleRate;
    }

    public void setnSaleRate(BigDecimal nSaleRate) {
        this.nSaleRate = nSaleRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LcUcodeBatchDetailsEntity that = (LcUcodeBatchDetailsEntity) o;
        return Objects.equals(cUcode, that.cUcode) &&
                Objects.equals(cBatchNo, that.cBatchNo) &&
                Objects.equals(nMrp, that.nMrp) &&
                Objects.equals(dExpDate, that.dExpDate) &&
                Objects.equals(cBatchKey, that.cBatchKey) &&
                Objects.equals(dDate, that.dDate) &&
                Objects.equals(dLdate, that.dLdate) &&
                Objects.equals(nSaleRate, that.nSaleRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cUcode, cBatchNo, nMrp, dExpDate, cBatchKey, dDate, dLdate, nSaleRate);
    }
}
