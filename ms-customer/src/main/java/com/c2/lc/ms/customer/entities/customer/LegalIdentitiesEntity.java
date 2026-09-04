package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.lib.db.DateAudit;
import com.c2.lc.ms.customer.entities.customer.FirmEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "legal_identities")
public class LegalIdentitiesEntity extends DateAudit implements Serializable {

    @Expose(serialize = false, deserialize = false)
    @SerializedName("n_legal_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_legal_id", unique = true, nullable = false)
    private Long nLegalId;

    @SerializedName("c_narcotic_no")
    @Column(name = "c_narcotic_no", length = 32)
    private String cNarcoticNo;

    @SerializedName("c_narcotic_no_img")
    @Column(name = "c_narcotic_no_img", length = 1024)
    private String cNarcoticNoImg;

    @SerializedName("c_drug_license_no1")
    @Column(name = "c_drug_license_no1")
    private String cDrugLicenseNo1;

    @SerializedName("'d_drug_license_no1_expiry_date'")
    @Column(name = "d_drug_license_no1_expiry_date", length = 32)
    private LocalDate dDrugLicenseNo1ExpiryDate;

    @SerializedName("c_drug_license_no1_img")
    @Column(name = "c_drug_license_no1_img", length = 1024)
    private String cDrugLicenseNo1Img;

    @SerializedName("c_drug_license_no2")
    @Column(name = "c_drug_license_no2")
    private String cDrugLicenseNo2;

    @SerializedName("d_drug_license_no2_expiry_date")
    @Column(name = "d_drug_license_no2_expiry_date", length = 32)
    private LocalDate dDrugLicenseNo2ExpiryDate;

    @SerializedName("c_drug_license_no2_img")
    @Column(name = "c_drug_license_no2_img", length = 1024)
    private String cDrugLicenseNo2Img;

    @SerializedName("c_drug_license_no3")
    @Column(name = "c_drug_license_no3")
    private String cDrugLicenseNo3;

    @SerializedName("'d_drug_license_no3_expiry_date'")
    @Column(name = "d_drug_license_no3_expiry_date", length = 32)
    private LocalDate dDrugLicenseNo3ExpiryDate;

    @SerializedName("c_drug_license_no3_img")
    @Column(name = "c_drug_license_no3_img", length = 1024)
    private String cDrugLicenseNo3Img;

    public LegalIdentitiesEntity(Long userId, LocalDateTime time){ super(userId, time);}

    public LegalIdentitiesEntity() {

    }
}