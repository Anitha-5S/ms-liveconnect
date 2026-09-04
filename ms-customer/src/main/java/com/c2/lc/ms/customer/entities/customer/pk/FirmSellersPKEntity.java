package com.c2.lc.ms.customer.entities.customer.pk;

import com.google.gson.annotations.SerializedName;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


public class FirmSellersPKEntity implements Serializable {

    private static final long serialVersionUID = 1768031425552493039L;

    @SerializedName("n_firm_id")
    @Column(name = "n_firm_id", nullable = false)
    private Long nFirmId;

    @SerializedName("c_buyer_code")
    @Column(name = "c_buyer_code", nullable = false)
    private String cBuyerCode;

    @SerializedName("c_seller_code")
    @Column(name = "c_seller_code", nullable = false)
    private String cSellerCode;

    public FirmSellersPKEntity() {
    }

    public FirmSellersPKEntity(Long firmId, String buyerCode, String sellerCode) {
        this.nFirmId = firmId;
        this.cBuyerCode = buyerCode;
        this.cSellerCode = sellerCode;
    }

    public Long getnFirmId() {
        return nFirmId;
    }

    public String getcBuyerCode() {
        return cBuyerCode;
    }

    public String getcSellerCode() {
        return cSellerCode;
    }

    public void setnFirmId(Long nFirmId) {
        this.nFirmId = nFirmId;
    }

    public void setcBuyerCode(String cBuyerCode) {
        this.cBuyerCode = cBuyerCode;
    }

    public void setcSellerCode(String cSellerCode) {
        this.cSellerCode = cSellerCode;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof FirmSellersPKEntity)) {
            return false;
        }
        FirmSellersPKEntity castOther = (FirmSellersPKEntity) other;
        return
                this.cBuyerCode.equals(castOther.cBuyerCode)
                        && this.nFirmId.equals(castOther.nFirmId)
                        && this.cSellerCode.equals(castOther.cSellerCode);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.cBuyerCode.hashCode();
        hash = hash * prime + this.cSellerCode.hashCode();
        hash = hash * prime + this.nFirmId.hashCode();

        return hash;
    }
}