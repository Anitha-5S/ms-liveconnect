package com.c2.lc.ms.master.entities.mysql;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "cust_branch_item_stock")
@IdClass(CustBranchItemStockEntityPK.class)
public class CustBranchItemStockEntity implements Serializable {
    private String cItemCode;

    @Id
    @Column(name = "c_item_code", nullable = false, length = 45)
    public String getcItemCode() {
        return cItemCode;
    }

    public void setcItemCode(String cItemCode) {
        this.cItemCode = cItemCode;
    }

    private String cC2Code;

    @Id
    @Column(name = "c_c2code", nullable = false, length = 45)
    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
    }

    private String cBrCode;

    @Id
    @Column(name = "c_br_code", nullable = false, length = 45)
    public String getcBrCode() {
        return cBrCode;
    }

    public void setcBrCode(String cBrCode) {
        this.cBrCode = cBrCode;
    }

    private int nBalQty;

    @Basic
    @Column(name = "n_bal_qty", nullable = false, precision = 0)
    public int getnBalQty() {
        return nBalQty;
    }

    public void setnBalQty(int nBalQty) {
        this.nBalQty = nBalQty;
    }

    private Timestamp tLtime;

    @Basic
    @Column(name = "t_ltime", nullable = true)
    public Timestamp gettLtime() {
        return tLtime;
    }

    public void settLtime(Timestamp tLtime) {
        this.tLtime = tLtime;
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

    private BigDecimal nSaleRate;

    @Basic
    @Column(name = "n_sale_rate", nullable = true, precision = 2)
    public BigDecimal getnSaleRate() {
        return nSaleRate;
    }

    public void setnSaleRate(BigDecimal nSaleRate) {
        this.nSaleRate = nSaleRate;
    }

    public CustBranchItemStockEntity(String cItemCode, String cC2Code, int nBalQty, BigDecimal nRate, BigDecimal nSaleRate) {
        this.cItemCode = cItemCode;
        this.cC2Code = cC2Code;
        this.nBalQty = nBalQty;
        this.nRate = nRate;
        this.nSaleRate = nSaleRate;
    }

    public CustBranchItemStockEntity() {
    }
}
