package com.c2.lc.ms.user.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_firm")
public class UserFirmEntity implements Serializable {

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

    @SerializedName("c_password")
    @NotEmpty(message = "'c_password' can not be empty!")
    @Column(name = "c_password", nullable = false, length = 356)
    private String cPassword;

    @SerializedName("c_ip")
    @NotEmpty(message = "'c_ip' can not be empty!")
    @Column(name = "c_ip", length = 32)
    private String cIp;

    @SerializedName("t_created_at")
    @Column(name = "t_created_at")
    private LocalDateTime tCreatedAt;

    @SerializedName("t_last_login_at")
    @Column(name = "t_last_login_at")
    private LocalDateTime tLastLoginAt;
}
