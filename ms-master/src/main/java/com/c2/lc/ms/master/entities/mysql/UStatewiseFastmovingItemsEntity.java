package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "u_statewise_fastmoving_items")
@IdClass(UStatewiseFastmovingItemsEntityPK.class)
public class UStatewiseFastmovingItemsEntity implements Serializable {

    private static final long serialVersionUID = -256750061968626971L;
    private String cUcode;
    private String cStateCode;
    private Integer nQty;
    private Integer nCount;
    private Timestamp dDate;
    private Timestamp dLdate;

    @Id
    @Column(name = "c_ucode", nullable = false, length = 50)
    public String getcUcode() {
        return cUcode;
    }

    public void setcUcode(String cUcode) {
        this.cUcode = cUcode;
    }

    @Id
    @Column(name = "c_state_code", nullable = false, length = 20)
    public String getcStateCode() {
        return cStateCode;
    }

    public void setcStateCode(String cStateCode) {
        this.cStateCode = cStateCode;
    }

    @Basic
    @Column(name = "n_qty", nullable = true)
    public Integer getnQty() {
        return nQty;
    }

    public void setnQty(Integer nQty) {
        this.nQty = nQty;
    }

    @Basic
    @Column(name = "n_count", nullable = true)
    public Integer getnCount() {
        return nCount;
    }

    public void setnCount(Integer nCount) {
        this.nCount = nCount;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UStatewiseFastmovingItemsEntity that = (UStatewiseFastmovingItemsEntity) o;
        return Objects.equals(cUcode, that.cUcode) &&
                Objects.equals(cStateCode, that.cStateCode) &&
                Objects.equals(nQty, that.nQty) &&
                Objects.equals(nCount, that.nCount) &&
                Objects.equals(dDate, that.dDate) &&
                Objects.equals(dLdate, that.dLdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cUcode, cStateCode, nQty, nCount, dDate, dLdate);
    }
}
