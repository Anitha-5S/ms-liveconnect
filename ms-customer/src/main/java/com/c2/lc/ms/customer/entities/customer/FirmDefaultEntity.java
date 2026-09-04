package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.lib.db.DateAudit;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "firm_default")
@NamedQuery(name = "FirmDefault.findAll", query = "SELECT f FROM FirmDefaultEntity f")
public class FirmDefaultEntity extends DateAudit implements Serializable {

    private static final long serialVersionUID = 3265866677414853768L;

    @Id
    @Column(name = "n_user_id", unique = true, nullable = false)
    @SerializedName("n_user_id")
    private Long nUserId;

    //bi-directional many-to-one association to Firm
    @Expose(serialize = false)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "n_firm_id")
    private FirmEntity firmEntity;
    //@Column(name = "n_firm_id")
    //private Long nFirmId;

    //bi-directional one-to-one association to UserDetail
//    @OneToOne
//    @JoinColumn(name = "n_user_id", nullable = false, insertable = false, updatable = false)
//    private UserDetail userDetail;

    public FirmDefaultEntity() {
    }

    public FirmDefaultEntity(Long userId, LocalDateTime currentTime) {
        super(userId, currentTime);
    }

    public Long getNUserId() {
        return this.nUserId;
    }

    public void setNUserId(Long nUserId) {
        this.nUserId = nUserId;
    }

/*
    public Long getnFirmId() {
        return nFirmId;
    }

    public void setnFirmId(Long nFirmId) {
        this.nFirmId = nFirmId;
    }
*/


    public FirmEntity getFirm() {
        return this.firmEntity;
    }

    public void setFirm(FirmEntity firmEntity) {
        this.firmEntity = firmEntity;
    }

/*
    public UserDetail getUserDetail() {
        return this.userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }
*/

}