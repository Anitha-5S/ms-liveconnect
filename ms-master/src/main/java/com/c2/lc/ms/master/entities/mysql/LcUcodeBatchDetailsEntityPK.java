package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class LcUcodeBatchDetailsEntityPK implements Serializable {
    private String cUcode;
    private String cBatchNo;

    @Column(name = "c_ucode", nullable = false, length = 100)
    @Id
    public String getcUcode() {
        return cUcode;
    }

    public void setcUcode(String cUcode) {
        this.cUcode = cUcode;
    }

    @Column(name = "c_batch_no", nullable = false, length = 100)
    @Id
    public String getcBatchNo() {
        return cBatchNo;
    }

    public void setcBatchNo(String cBatchNo) {
        this.cBatchNo = cBatchNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LcUcodeBatchDetailsEntityPK that = (LcUcodeBatchDetailsEntityPK) o;
        return Objects.equals(cUcode, that.cUcode) &&
                Objects.equals(cBatchNo, that.cBatchNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cUcode, cBatchNo);
    }
}
