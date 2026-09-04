package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.lib.db.DateAudit;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_detail")
@NamedQuery(name = "UserDetail.findAll", query = "SELECT u FROM UserDetailEntity u")
public class UserDetailEntity extends DateAudit implements Serializable {

    private static final long serialVersionUID = 6575886552124651855L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_user_id", unique = true, nullable = false)
    @SerializedName("n_user_id")
    private Long nUserId;

    @Column(name = "c_first_name", length = 255)
    @SerializedName("c_first_name")
    private String cFirstName;

    @Column(name = "c_last_name", length = 255)
    @SerializedName("c_last_name")
    private String cLastName;

    @Column(name = "c_status", length = 2)
    @SerializedName("c_status")
    private String cStatus;

    //bi-directional one-to-one association to FirmDefault
//    @OneToOne(mappedBy = "userDetail")
//    private FirmDefault firmDefault;

    //bi-directional many-to-one association to FirmUser
    @OneToMany(mappedBy = "userDetailEntity")
    private List<FirmUserEntity> firmUserEntities;

    @ManyToOne
    @JoinColumn(name = "n_contact_id")
    private ContactDetailEntity contactDetailEntity;

    @SerializedName("ac_profile_image")
    @Column(name = "ac_profile_image")
    private String profileImage;

    @SerializedName("d_date_of_birth")
    @Column(name = "d_date_of_birth")
    private LocalDate dateOfBirth;

    @SerializedName("c_gender")
    @Column(name = "c_gender")
    private String gender;



    public UserDetailEntity() {
    }

    public UserDetailEntity(Long userId, LocalDateTime currentTime) {
        super(userId, currentTime);
    }

    public Long getNUserId() {
        return this.nUserId;
    }

    public void setNUserId(Long nUserId) {
        this.nUserId = nUserId;
    }

    public String getCFirstName() {
        return this.cFirstName;
    }

    public void setCFirstName(String cFirstName) {
        this.cFirstName = cFirstName;
    }

    public String getCLastName() {
        return this.cLastName;
    }

    public void setCLastName(String cLastName) {
        this.cLastName = cLastName;
    }

    public String getCStatus() {
        return this.cStatus;
    }

    public void setCStatus(String cStatus) {
        this.cStatus = cStatus;
    }



//    public FirmDefault getFirmDefault() {
//        return this.firmDefault;
//    }
//
//    public void setFirmDefault(FirmDefault firmDefault) {
//        this.firmDefault = firmDefault;
//    }

    public List<FirmUserEntity> getFirmUsers() {
        return this.firmUserEntities;
    }

    public void setFirmUsers(List<FirmUserEntity> firmUserEntities) {
        this.firmUserEntities = firmUserEntities;
    }

    public FirmUserEntity addFirmUser(FirmUserEntity firmUserEntity) {
        getFirmUsers().add(firmUserEntity);
        firmUserEntity.setUserDetail(this);

        return firmUserEntity;
    }

    public FirmUserEntity removeFirmUser(FirmUserEntity firmUserEntity) {
        getFirmUsers().remove(firmUserEntity);
        firmUserEntity.setUserDetail(null);

        return firmUserEntity;
    }

    public ContactDetailEntity getContactDetail() {
        return this.contactDetailEntity;
    }

    public void setContactDetail(ContactDetailEntity contactDetailEntity) {
        this.contactDetailEntity = contactDetailEntity;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}