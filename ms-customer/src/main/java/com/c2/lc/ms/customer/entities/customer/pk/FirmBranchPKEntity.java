package com.c2.lc.ms.customer.entities.customer.pk;

import com.google.gson.annotations.SerializedName;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class FirmBranchPKEntity implements Serializable {

    private static final long serialVersionUID = -5301084873866187756L;

    @SerializedName("n_firm_id")
    @Column(name = "n_firm_id", insertable = false, updatable = false, unique = true, nullable = false)
    private Long nFirmId;

    @SerializedName("n_branch_id")
    @Column(name = "n_branch_id", insertable = false, updatable = false, unique = true, nullable = false)
    private Long nBranchId;

    public FirmBranchPKEntity() {
    }

    public FirmBranchPKEntity(Long firmId, Long branchId) {
        this.nFirmId = firmId;
        this.nBranchId = branchId;
    }

    public Long getNFirmId() {
        return this.nFirmId;
    }

    public void setNFirmId(Long nFirmId) {
        this.nFirmId = nFirmId;
    }

    public Long getNBranchId() {
        return this.nBranchId;
    }

    public void setNBranchId(Long nBranchId) {
        this.nBranchId = nBranchId;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof FirmBranchPKEntity)) {
            return false;
        }
        FirmBranchPKEntity castOther = (FirmBranchPKEntity) other;
        return
                this.nFirmId.equals(castOther.nFirmId)
                        && this.nBranchId.equals(castOther.nBranchId);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.nFirmId.hashCode();
        hash = hash * prime + this.nBranchId.hashCode();

        return hash;
    }
}