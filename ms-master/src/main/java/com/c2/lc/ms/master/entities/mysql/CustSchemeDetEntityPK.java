package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class CustSchemeDetEntityPK implements Serializable {
    private static final long serialVersionUID = -2926365818602202804L;
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
}
