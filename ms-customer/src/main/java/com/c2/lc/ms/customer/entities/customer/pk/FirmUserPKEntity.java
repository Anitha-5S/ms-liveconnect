package com.c2.lc.ms.customer.entities.customer.pk;

import com.google.gson.annotations.SerializedName;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class FirmUserPKEntity implements Serializable {

    private static final long serialVersionUID = 1768031425552493039L;

    @SerializedName("n_user_id")
    @Column(name = "n_user_id", insertable = false, updatable = false, unique = true, nullable = false)
    private Long nUserId;

    @SerializedName("n_firm_id")
    @Column(name = "n_firm_id", insertable = false, updatable = false, unique = true, nullable = false)
    private Long nFirmId;

    public FirmUserPKEntity() {
    }

    public FirmUserPKEntity(Long firmId, Long userId) {
        this.nFirmId = firmId;
        this.nUserId = userId;
    }

    public Long getNUserId() {
        return this.nUserId;
    }

    public void setNUserId(Long nUserId) {
        this.nUserId = nUserId;
    }

    public Long getNFirmId() {
        return this.nFirmId;
    }

    public void setNFirmId(Long nFirmId) {
        this.nFirmId = nFirmId;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof FirmUserPKEntity)) {
            return false;
        }
        FirmUserPKEntity castOther = (FirmUserPKEntity) other;
        return
                this.nUserId.equals(castOther.nUserId)
                        && this.nFirmId.equals(castOther.nFirmId);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.nUserId.hashCode();
        hash = hash * prime + this.nFirmId.hashCode();

        return hash;
    }
}