package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class HsnGstMappingEntityPK implements Serializable {
    private String cHsnSacCode;

    @Id
    @Column(name = "c_hsn_sac_code", nullable = false, length = 8)
    public String getcHsnSacCode() {
        return cHsnSacCode;
    }

    public void setcHsnSacCode(String cHsnSacCode) {
        this.cHsnSacCode = cHsnSacCode;
    }

    private String cGstCode;

    @Id
    @Column(name = "c_gst_code", nullable = false, length = 8)
    public String getcGstCode() {
        return cGstCode;
    }

    public void setcGstCode(String cGstCode) {
        this.cGstCode = cGstCode;
    }

    public HsnGstMappingEntityPK() {
    }

    public HsnGstMappingEntityPK(String cHsnSacCode, String cGstCode) {
        this.cHsnSacCode = cHsnSacCode;
        this.cGstCode = cGstCode;
    }
}
