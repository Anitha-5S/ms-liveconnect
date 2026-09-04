package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.lib.db.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "ts_store_register")
public class TSStoreRegisterEntity extends DateAudit implements Serializable {

    private static final long serialVersionUID = -5443534162176298849L;


    @SerializedName("n_user_id")
    @Column(name = "n_user_id", unique = true, nullable = false)
    private Long nUserId;

    @SerializedName("n_firm_id")
    @Id
    @Column(name = "n_firm_id", unique = true, nullable = false)
    private Long nFirmId;

    @SerializedName("c_br_code")
    @Column(name = "c_br_code")
    private String cBrCode;

    @Column(name = "c_store_name", nullable = false, length = 255)
    @SerializedName("c_store_name")
    @NotEmpty(message = "'c_store_name' can not be empty!")
    private String cStoreName;

    @Column(name = "c_city_code", length = 6)
    @SerializedName("c_city_code")
    @Expose(serialize = false)
    private String cCityCode;

    @Column(name = "c_state_code", length = 6)
    @SerializedName("c_state_code")
    @Expose(serialize = false)
    private String cStateCode;

    @NotBlank(message = "c_mobile_no can't be blank")
    @Column(name = "c_mobile_no", length = 10)
    @SerializedName("c_mobile_number")
    private String cMobileNo;

    @Column(name = "c_pincode", length = 6)
    @SerializedName("c_pincode")
    private String cPin;

    @NotBlank(message = "c_email can't be blank")
    @Email(message ="c_email must be a well-formed email address", regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @Column(name = "c_email", length = 255)
    @SerializedName("c_email")
    private String cEmail;

    @Column(name = "c_status", length = 1)
    @SerializedName("c_status")
    private String status;

    @Column(name = "c_c2code", length = 10)
    @SerializedName("c_c2code")
    private String c2Code;

    @Column(name = "c_package_name", length = 30)
    @SerializedName("c_package_name")
    private String packageName;

    public TSStoreRegisterEntity(Long userId, LocalDateTime time){ super(userId, time);}

    public TSStoreRegisterEntity() { }
}