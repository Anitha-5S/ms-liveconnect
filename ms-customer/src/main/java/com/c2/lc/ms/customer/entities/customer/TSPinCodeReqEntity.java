package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.lib.db.DateAudit;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ts_pincode_wise_request")
public class TSPinCodeReqEntity implements Serializable {

    private static final long serialVersionUID = -5443534162176298849L;

    @Expose(serialize = false, deserialize = false)
    @SerializedName("n_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id", unique = true, nullable = false)
    private Long nId;

    @Expose(serialize = false)
    @ManyToOne
    @JoinColumn(name = "n_user_id")
    private UserDetailEntity userDetailEntity;

    @NotBlank(message = "c_pincode can't be blank")
    @Column(name = "c_pincode", length = 6)
    @SerializedName("c_pincode")
    private String cPin;

    @Column(name = "c_c2code", length = 10)
    @SerializedName("c_c2code")
    private String c2Code;

    @Column(name = "c_active_status")
    @SerializedName("c_active_status")
    private String serviceActiveStatus;

    @Expose(serialize = false, deserialize = false)
    @Column(name = "t_created_at", updatable = false)
    private LocalDateTime tCreatedAt;

    public TSPinCodeReqEntity() { }
}