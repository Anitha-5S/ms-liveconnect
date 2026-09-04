package com.c2.lc.ms.customer.entities.customer.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the firm_user_roles database table.
 */
@Embeddable
public class FirmUserRolePKEntity implements Serializable {
    //default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "n_user_id", unique = true, nullable = false)
    private Long nUserId;

    @Column(name = "n_firm_id", unique = true, nullable = false)
    private Long nFirmId;

    public FirmUserRolePKEntity() {
    }

    public FirmUserRolePKEntity(Long nUserId, Long nFirmId) {
        this.nUserId = nUserId;
        this.nFirmId = nFirmId;
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
        if (!(other instanceof FirmUserRolePKEntity)) {
            return false;
        }
        FirmUserRolePKEntity castOther = (FirmUserRolePKEntity) other;
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