package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.lib.db.DateAudit;
import com.c2.lc.ms.customer.entities.customer.pk.FirmUserPKEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "firm_user")
@NamedQuery(name = "FirmUser.findAll", query = "SELECT f FROM FirmUserEntity f")
public class FirmUserEntity extends DateAudit implements Serializable {

    private static final long serialVersionUID = -6144633010457856104L;

//    @Expose(serialize = false)
    @EmbeddedId
FirmUserPKEntity id;

    @Column(name = "c_status", length = 255)
    @SerializedName("c_status")
    private String cStatus;

    @Column(name = "c_firm_user_role")
    private String firmUserRole;

    //bi-directional many-to-one association to Firm
    @Expose(serialize = false)
    @ManyToOne
    @JoinColumn(name = "n_firm_id", nullable = false, insertable = false, updatable = false)
    private FirmEntity firmEntity;

    //bi-directional many-to-one association to UserDetail
    @Expose(serialize = false)
    @ManyToOne
    @JoinColumn(name = "n_user_id", nullable = false, insertable = false, updatable = false)
    private UserDetailEntity userDetailEntity;

    public FirmUserEntity() {
    }

    public FirmUserEntity(Long userId, LocalDateTime currentTime) {
        super(userId, currentTime);
    }

    public FirmUserPKEntity getId() {
        return this.id;
    }

    public void setId(FirmUserPKEntity id) {
        this.id = id;
    }

    public String getCStatus() {
        return this.cStatus;
    }

    public void setCStatus(String cStatus) {
        this.cStatus = cStatus;
    }

    public FirmEntity getFirm() {
        return this.firmEntity;
    }

    public void setFirm(FirmEntity firmEntity) {
        this.firmEntity = firmEntity;
    }

    public UserDetailEntity getUserDetail() {
        return this.userDetailEntity;
    }

    public void setUserDetail(UserDetailEntity userDetailEntity) {
        this.userDetailEntity = userDetailEntity;
    }

    public String getFirmUserRole() {
        return firmUserRole;
    }

    public void setFirmUserRole(String firmUserRole) {
        this.firmUserRole = firmUserRole;
    }
}
