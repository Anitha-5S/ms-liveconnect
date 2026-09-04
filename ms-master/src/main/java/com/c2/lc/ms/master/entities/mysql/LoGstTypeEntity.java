package com.c2.lc.ms.master.entities.mysql;

import com.c2.lc.lib.db.DateAudit;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "lo_gst_type")
@NamedQuery(name = "LoGstTypeEntity.findAll", query = "SELECT g FROM LoGstTypeEntity g")
public class LoGstTypeEntity extends DateAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id", unique = true, nullable = false)
    @SerializedName("n_id")
//    @Expose(serialize = false)
    private Long nId;

    @Column(name = "c_gst_type")
    @SerializedName("c_gst_type")
    private String cGstType;

    public LoGstTypeEntity() {
    }

    public LoGstTypeEntity(Long userId, LocalDateTime time) {
        super(userId, time);
    }

    public Long getnId() {
        return nId;
    }

    public void setnId(Long nId) {
        this.nId = nId;
    }

    public String getcGstType() {
        return cGstType;
    }

    public void setcGstType(String cGstType) {
        this.cGstType = cGstType;
    }
}
