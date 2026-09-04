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
@Table(name = "firm")
public class FirmEntity extends DateAudit implements Serializable {

    private static final long serialVersionUID = -5443534162176298849L;


    @Expose(serialize = false, deserialize = false)
    @SerializedName("n_firm_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_firm_id", unique = true, nullable = false)
    private Long nFirmId;

    @Column(name = "c_type", length = 6)
    @SerializedName("c_type")
    @NotEmpty(message = "'c_type' can not be empty!")
    private String cType;

    @SerializedName("c_gst_no")
    @Column(name = "c_gst_no", length = 32)
    private String cGstNo;

    @SerializedName("c_gst_type")
    @Column(name = "c_gst_type", length = 2)
    private String cGstType;

    @SerializedName("c_name")
    @NotEmpty(message = "'c_name' can not be empty!")
    @Column(name = "c_name", nullable = false, length = 255)
    private String cName;

    @SerializedName("c_status")
    @Column(name = "c_status", length = 2)
    private String cStatus;

    @SerializedName("c_image_url")
    @Column(name = "c_image_url", length = 1024)
    private String cImageUrl;

    @Column(name = "c_city_name", length = 255)
    @SerializedName("c_city_name")
    @Expose(serialize = false)
    private String cCityName;

    @Column(name = "c_city_code", length = 6)
    @SerializedName("c_city_code")
    @Expose(serialize = false)
    private String cCityCode;

    @Column(name = "c_state_name", length = 255)
    @SerializedName("c_state_name")
    @Expose(serialize = false)
    private String cStateName;

    @Column(name = "c_state_code", length = 6)
    @SerializedName("c_state_code")
    @Expose(serialize = false)
    private String cStateCode;

    @Column(name = "c_area_name", length = 255)
    @SerializedName("c_area_name")
    @Expose(serialize = false)
    private String cAreaName;

    @Column(name = "c_area_code", length = 6)
    @SerializedName("c_area_code")
    @Expose(serialize = false)
    private String cAreaCode;

    @Column(name = "c_mobile_no", length = 10)
    @SerializedName("c_mobile_no")
    private String cMobileNo;

    @Column(name = "c_pincode", length = 6)
    @SerializedName("c_pincode")
    private String cPin;

    @Column(name = "c_email", length = 255)
    @SerializedName("c_email")
    private String cEmail;

    @Column(name = "c_store_combine_status", length = 1)
    @SerializedName("c_store_combine_status")
    private String storeCombineStatus;

    @SerializedName(value = "contact")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "n_contact_id")
    private ContactDetailEntity contactDetail;

    @SerializedName(value = "document")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "n_documents_id")
    private DocumentEntity documentDetail;

    @SerializedName(value = "legal")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "n_legal_id")
    private LegalIdentitiesEntity legalIdentities;

    //bi-directional many-to-one association to FirmBranch
//    @JsonIgnore
//    @Expose(serialize = false, deserialize = false)
//    @OneToMany(mappedBy = "firmEntity")
//    private List<FirmBranchEntity> branches;

    //bi-directional many-to-one association to FirmDefault
    @JsonIgnore
    @Expose(serialize = false, deserialize = false)
    @OneToMany(mappedBy = "firmEntity")
//    @JoinColumn(name = "n_firm_id")
    private List<FirmDefaultEntity> defaultEntities;

    //bi-directional many-to-one association to FirmUser
    @JsonIgnore
    @Expose(serialize = false, deserialize = false)
    @OneToMany(mappedBy = "firmEntity")
    private List<FirmUserEntity> firmUserEntities;

    @SerializedName("c_c2code")
    @Column(name = "c_c2code")
    private String c2Code;

    @SerializedName("c_br_code")
    @Column(name = "c_br_code")
    private String brCode;

    @Expose(serialize = false, deserialize = false)
    @SerializedName("c_ucode")
    @Column(name = "c_ucode")
    private String cUcode;

    public FirmEntity(Long userId, LocalDateTime time){ super(userId, time);}

    public FirmEntity() { }
}