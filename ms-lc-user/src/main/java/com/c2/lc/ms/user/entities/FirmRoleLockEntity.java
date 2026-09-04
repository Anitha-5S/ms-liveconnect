package com.c2.lc.ms.user.entities;

import com.c2.lc.lib.db.DateAudit;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Entity
@Table(name = "firm_role_lock")
public class FirmRoleLockEntity extends DateAudit implements Serializable {
    @Expose(serialize = false, deserialize = false)
    @SerializedName("n_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id", unique = true, nullable = false)
    private Long nId;

    @SerializedName("c_mobile_no")
    @NotEmpty(message = "'c_mobile_no' can not be empty!")
    @Column(name = "c_mobile_no", nullable = false, length = 16)
    private String cMobileNo;

    @SerializedName("c_c2code")
    @NotEmpty(message = "'c_c2code' can not be empty!")
    @Column(name = "c_c2code", nullable = false, length = 8)
    private String cC2Code;

    @SerializedName("c_act_code")
    @NotEmpty(message = "'c_act_code' can not be empty!")
    @Column(name = "c_act_code", nullable = false, length = 8)
    private String cActCode;

    @SerializedName("c_role_lock")
    @NotEmpty(message = "'c_role_lock' can not be empty!")
    @Column(name = "c_role_lock", nullable = false, length = 16)
    private String cRoleLock;

}
