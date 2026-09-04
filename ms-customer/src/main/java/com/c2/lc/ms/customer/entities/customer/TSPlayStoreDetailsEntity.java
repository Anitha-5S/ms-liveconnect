package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.lib.db.DateAudit;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ts_play_store_details")
public class TSPlayStoreDetailsEntity extends DateAudit implements Serializable {

    private static final long serialVersionUID = -5443534162176298849L;


    @SerializedName("c_application_id")
    @Column(name = "c_application_id",unique = true, nullable = false)
    private String cApplicationId;

    @Id
    @SerializedName("c_c2code")
    @Column(name = "c_c2code", unique = true, nullable = false)
    private String cC2code;

    @SerializedName("c_shop_name")
    @Column(name = "c_shop_name")
    private String ShopName;

    @Column(name = "c_email")
    @SerializedName("c_email")
    private String Email;

    @Column(name = "c_sc_email")
    @SerializedName("c_sc_email")
    private String SecEmail;

    @Column(name = "c_mobile_number")
    @SerializedName("c_mobile_number")
    private String MobileNumber;


    @Column(name = "c_location")
    @SerializedName("c_location")
    private String Location;

    @Column(name = "c_state")
    @SerializedName("c_state")
    private String State;

    @Column(name = "c_app_icon")
    @SerializedName("c_app_icon")
    private String AppIcon;

    @Column(name = "n_app_version_code")
    @SerializedName("n_app_version_code")
    private Long AppVersionCode;

    @Column(name = "c_app_version_name")
    @SerializedName("c_app_version_name")
    private String AppVersionName;

    @Column(name = "c_key_file")
    @SerializedName("c_key_file")
    private String KeyFIle;

    @Column(name = "c_apk")
    @SerializedName("c_apk")
    private String Apk;

    @Column(name = "c_bundle")
    @SerializedName("c_bundle")
    private String Bundle;

    @Column(name = "c_p12_file")
    @SerializedName("c_p12_file")
    private String P12File;

    @Column(name = "c_application_name")
    @SerializedName("c_application_name")
    private String ApplicationName;

    public TSPlayStoreDetailsEntity(Long userId, LocalDateTime time){ super(userId, time);}

    public TSPlayStoreDetailsEntity() { }
}