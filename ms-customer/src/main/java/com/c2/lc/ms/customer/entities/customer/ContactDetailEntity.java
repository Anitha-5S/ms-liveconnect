package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.lib.db.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "contact_detail")
public class ContactDetailEntity extends DateAudit implements Serializable {

    private static final long serialVersionUID = 2085360436916236919L;

    //@Expose(serialize = false, deserialize = false)
    @SerializedName("n_contact_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_contact_id", unique = true, nullable = false)
    private Long nContactId;

    @Column(name = "c_address_1", length = 255)
    @SerializedName("c_address_1")
    private String cAddress1;

    @Column(name = "c_address_2", length = 255)
    @SerializedName("c_address_2")
    private String cAddress2;

    @Column(name = "c_alternate_email_id", length = 255)
    @SerializedName("c_alternate_email_id")
    private String cAlternateEmailId;

    @Column(name = "c_alternate_mobile_no", length = 10)
    @SerializedName("c_alternate_mobile_no")
    private String cAlternateMobileNo;

    @Column(name = "c_alternate_phone_no", length = 10)
    @SerializedName("c_alternate_phone_no")
    private String cAlternatePhoneNo;

    @Column(name = "c_contact_name", length = 255)
    @SerializedName("c_contact_name")
    private String cContactName;

    @Column(name = "c_email_id", length = 255)
    @SerializedName("c_email_id")
    private String cEmailId;

    @Column(name = "c_mobile_no", length = 10)
    @SerializedName("c_mobile_no")
    private String cMobileNo;

    @Column(name = "c_note", length = 1024)
    @SerializedName("c_note")
    private String cNote;

    @Column(name = "c_phone_no", length = 10)
    @SerializedName("c_phone_no")
    private String cPhoneNo;

    @Column(name = "c_pin", length = 6)
    @SerializedName("c_pin")
    private String cPin;

    @Column(name = "c_country_name", length = 255)
    @SerializedName("c_country_name")
    private String cCountryName;

    @Column(name = "c_country_code", length = 6)
    @SerializedName("c_country_code")
    private String cCountryCode;

    @Column(name = "c_city_name", length = 255)
    @SerializedName("c_city_name")
    private String cCityName;

    @Column(name = "c_city_code", length = 6)
    @SerializedName("c_city_code")
    private String cCityCode;

    @Column(name = "c_state_name", length = 255)
    @SerializedName("c_state_name")
    private String cStateName;

    @Column(name = "c_state_code", length = 6)
    @SerializedName("c_state_code")
    private String cStateCode;

    @Column(name = "c_area_name", length = 255)
    @SerializedName("c_area_name")
    private String cAreaName;

    @Column(name = "c_area_code", length = 6)
    @SerializedName("c_area_code")
    private String cAreaCode;

    @Column(name = "c_image_url", length = 1024)
    @SerializedName("c_image_url")
    private String cImageUrl;

    @SerializedName("c_landmark")
    @Column(name = "c_landmark", length = 255)
    private String cLandmark;

    @SerializedName("n_user_id")
    @Column(name = "n_user_id")
    private Long nUserId;

    @SerializedName("c_add_type")
    @Column(name = "c_address_type", length = 10)
    private String cAddressType;

    @SerializedName("c_delivery_address_status")
    @Column(name = "c_delivery_address_status", length = 1)
    private String cDeliveryAddressStatus;

    @SerializedName("c_extra_customer_name")
    @Column(name = "c_address_name", length = 255)
    private String AdrdressName;

    public ContactDetailEntity(Long userId, LocalDateTime currentTime) {
        super(userId, currentTime);
    }
    public ContactDetailEntity(){}

}