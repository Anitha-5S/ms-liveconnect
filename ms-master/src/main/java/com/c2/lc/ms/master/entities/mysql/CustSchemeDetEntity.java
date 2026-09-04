package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(name = "cust_scheme_det")
@IdClass(CustSchemeDetEntityPK.class)
public class CustSchemeDetEntity {
    private String cC2Code;

    @Id
    @Column(name = "c_c2code", nullable = false, length = 100)
    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
    }

    private String cItemCode;

    @Id
    @Column(name = "c_item_code", nullable = false, length = 6)
    public String getcItemCode() {
        return cItemCode;
    }

    public void setcItemCode(String cItemCode) {
        this.cItemCode = cItemCode;
    }

    private String cBatchNo;

    @Id
    @Column(name = "c_batch_no", nullable = false, length = 15)
    public String getcBatchNo() {
        return cBatchNo;
    }

    public void setcBatchNo(String cBatchNo) {
        this.cBatchNo = cBatchNo;
    }

    private String cCategory;

    @Id
    @Column(name = "c_category", nullable = false, length = 6)
    public String getcCategory() {
        return cCategory;
    }

    public void setcCategory(String cCategory) {
        this.cCategory = cCategory;
    }

    private Integer nSchQty1;

    @Basic
    @Column(name = "n_sch_qty_1", nullable = true, precision = 0)
    public Integer getnSchQty1() {
        return nSchQty1;
    }

    public void setnSchQty1(Integer nSchQty1) {
        this.nSchQty1 = nSchQty1;
    }

    private Integer nFreeQty1;

    @Basic
    @Column(name = "n_free_qty_1", nullable = true, precision = 0)
    public Integer getnFreeQty1() {
        return nFreeQty1;
    }

    public void setnFreeQty1(Integer nFreeQty1) {
        this.nFreeQty1 = nFreeQty1;
    }

    private BigDecimal nSchDiscPerc1;

    @Basic
    @Column(name = "n_sch_disc_perc_1", nullable = true, precision = 2)
    public BigDecimal getnSchDiscPerc1() {
        return nSchDiscPerc1;
    }

    public void setnSchDiscPerc1(BigDecimal nSchDiscPerc1) {
        this.nSchDiscPerc1 = nSchDiscPerc1;
    }

    private Integer nFlatDisc1;

    @Basic
    @Column(name = "n_flat_disc_1", nullable = true, precision = 0)
    public Integer getnFlatDisc1() {
        return nFlatDisc1;
    }

    public void setnFlatDisc1(Integer nFlatDisc1) {
        this.nFlatDisc1 = nFlatDisc1;
    }

    private Integer nSchQty2;

    @Basic
    @Column(name = "n_sch_qty_2", nullable = true, precision = 0)
    public Integer getnSchQty2() {
        return nSchQty2;
    }

    public void setnSchQty2(Integer nSchQty2) {
        this.nSchQty2 = nSchQty2;
    }

    private Integer nFreeQty2;

    @Basic
    @Column(name = "n_free_qty_2", nullable = true, precision = 0)
    public Integer getnFreeQty2() {
        return nFreeQty2;
    }

    public void setnFreeQty2(Integer nFreeQty2) {
        this.nFreeQty2 = nFreeQty2;
    }

    private BigDecimal nSchDiscPerc2;

    @Basic
    @Column(name = "n_sch_disc_perc_2", nullable = true, precision = 2)
    public BigDecimal getnSchDiscPerc2() {
        return nSchDiscPerc2;
    }

    public void setnSchDiscPerc2(BigDecimal nSchDiscPerc2) {
        this.nSchDiscPerc2 = nSchDiscPerc2;
    }

    private BigDecimal nFlatDisc2;

    @Basic
    @Column(name = "n_flat_disc_2", nullable = true, precision = 2)
    public BigDecimal getnFlatDisc2() {
        return nFlatDisc2;
    }

    public void setnFlatDisc2(BigDecimal nFlatDisc2) {
        this.nFlatDisc2 = nFlatDisc2;
    }

    private Integer nSchQty3;

    @Basic
    @Column(name = "n_sch_qty_3", nullable = true, precision = 0)
    public Integer getnSchQty3() {
        return nSchQty3;
    }

    public void setnSchQty3(Integer nSchQty3) {
        this.nSchQty3 = nSchQty3;
    }

    private Integer nFreeQty3;

    @Basic
    @Column(name = "n_free_qty_3", nullable = true, precision = 0)
    public Integer getnFreeQty3() {
        return nFreeQty3;
    }

    public void setnFreeQty3(Integer nFreeQty3) {
        this.nFreeQty3 = nFreeQty3;
    }

    private BigDecimal nSchDiscPerc3;

    @Basic
    @Column(name = "n_sch_disc_perc_3", nullable = true, precision = 2)
    public BigDecimal getnSchDiscPerc3() {
        return nSchDiscPerc3;
    }

    public void setnSchDiscPerc3(BigDecimal nSchDiscPerc3) {
        this.nSchDiscPerc3 = nSchDiscPerc3;
    }

    private BigDecimal nFlatDisc3;

    @Basic
    @Column(name = "n_flat_disc_3", nullable = true, precision = 2)
    public BigDecimal getnFlatDisc3() {
        return nFlatDisc3;
    }

    public void setnFlatDisc3(BigDecimal nFlatDisc3) {
        this.nFlatDisc3 = nFlatDisc3;
    }

    private String nSchItem1;

    @Basic
    @Column(name = "n_sch_item_1", nullable = false, length = 6)
    public String getnSchItem1() {
        return nSchItem1;
    }

    public void setnSchItem1(String nSchItem1) {
        this.nSchItem1 = nSchItem1;
    }

    private Integer nSchItemQty1;

    @Basic
    @Column(name = "n_sch_item_qty_1", nullable = true, precision = 0)
    public Integer getnSchItemQty1() {
        return nSchItemQty1;
    }

    public void setnSchItemQty1(Integer nSchItemQty1) {
        this.nSchItemQty1 = nSchItemQty1;
    }

    private String nSchItem2;

    @Basic
    @Column(name = "n_sch_item_2", nullable = false, length = 6)
    public String getnSchItem2() {
        return nSchItem2;
    }

    public void setnSchItem2(String nSchItem2) {
        this.nSchItem2 = nSchItem2;
    }

    private Integer nSchItemQty2;

    @Basic
    @Column(name = "n_sch_item_qty_2", nullable = true, precision = 0)
    public Integer getnSchItemQty2() {
        return nSchItemQty2;
    }

    public void setnSchItemQty2(Integer nSchItemQty2) {
        this.nSchItemQty2 = nSchItemQty2;
    }

    private String nSchItem3;

    @Basic
    @Column(name = "n_sch_item_3", nullable = false, length = 6)
    public String getnSchItem3() {
        return nSchItem3;
    }

    public void setnSchItem3(String nSchItem3) {
        this.nSchItem3 = nSchItem3;
    }

    private Integer nSchItemQty3;

    @Basic
    @Column(name = "n_sch_item_qty_3", nullable = true, precision = 0)
    public Integer getnSchItemQty3() {
        return nSchItemQty3;
    }

    public void setnSchItemQty3(Integer nSchItemQty3) {
        this.nSchItemQty3 = nSchItemQty3;
    }

    private Integer nSchTillBalQty;

    @Basic
    @Column(name = "n_sch_till_bal_qty", nullable = true, precision = 0)
    public Integer getnSchTillBalQty() {
        return nSchTillBalQty;
    }

    public void setnSchTillBalQty(Integer nSchTillBalQty) {
        this.nSchTillBalQty = nSchTillBalQty;
    }

    private BigInteger nAllowNegative;

    @Basic
    @Column(name = "n_allow_negative", nullable = true, precision = 0)
    public BigInteger getnAllowNegative() {
        return nAllowNegative;
    }

    public void setnAllowNegative(BigInteger nAllowNegative) {
        this.nAllowNegative = nAllowNegative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustSchemeDetEntity that = (CustSchemeDetEntity) o;
        return Objects.equals(cC2Code, that.cC2Code) &&
                Objects.equals(cItemCode, that.cItemCode) &&
                Objects.equals(cBatchNo, that.cBatchNo) &&
                Objects.equals(cCategory, that.cCategory) &&
                Objects.equals(nSchQty1, that.nSchQty1) &&
                Objects.equals(nFreeQty1, that.nFreeQty1) &&
                Objects.equals(nSchDiscPerc1, that.nSchDiscPerc1) &&
                Objects.equals(nFlatDisc1, that.nFlatDisc1) &&
                Objects.equals(nSchQty2, that.nSchQty2) &&
                Objects.equals(nFreeQty2, that.nFreeQty2) &&
                Objects.equals(nSchDiscPerc2, that.nSchDiscPerc2) &&
                Objects.equals(nFlatDisc2, that.nFlatDisc2) &&
                Objects.equals(nSchQty3, that.nSchQty3) &&
                Objects.equals(nFreeQty3, that.nFreeQty3) &&
                Objects.equals(nSchDiscPerc3, that.nSchDiscPerc3) &&
                Objects.equals(nFlatDisc3, that.nFlatDisc3) &&
                Objects.equals(nSchItem1, that.nSchItem1) &&
                Objects.equals(nSchItemQty1, that.nSchItemQty1) &&
                Objects.equals(nSchItem2, that.nSchItem2) &&
                Objects.equals(nSchItemQty2, that.nSchItemQty2) &&
                Objects.equals(nSchItem3, that.nSchItem3) &&
                Objects.equals(nSchItemQty3, that.nSchItemQty3) &&
                Objects.equals(nSchTillBalQty, that.nSchTillBalQty) &&
                Objects.equals(nAllowNegative, that.nAllowNegative);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cC2Code, cItemCode, cBatchNo, cCategory, nSchQty1, nFreeQty1, nSchDiscPerc1, nFlatDisc1, nSchQty2, nFreeQty2, nSchDiscPerc2, nFlatDisc2, nSchQty3, nFreeQty3, nSchDiscPerc3, nFlatDisc3, nSchItem1, nSchItemQty1, nSchItem2, nSchItemQty2, nSchItem3, nSchItemQty3, nSchTillBalQty, nAllowNegative);
    }
}
