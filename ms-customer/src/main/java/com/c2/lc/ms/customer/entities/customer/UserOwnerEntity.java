package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.lib.db.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "user_owner")
public class UserOwnerEntity extends DateAudit implements Serializable {

    private static final long serialVersionUID = -5443534162176298849L;


    @Expose(serialize = false, deserialize = false)
    @SerializedName("n_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id", unique = true, nullable = false)
    private Long nId;

    @Column(name = "n_parent_user_id")
    @SerializedName("n_parent_user_id")
    private Long parentUser;

    @SerializedName("n_child_user_id")
    @Column(name = "n_child_user_id")
    private Long childUser;

    @SerializedName("c_status")
    @Column(name = "c_status", length = 1)
    private String cStatus;

    public UserOwnerEntity(Long userId, LocalDateTime time){ super(userId, time);}

    public UserOwnerEntity() { }
}