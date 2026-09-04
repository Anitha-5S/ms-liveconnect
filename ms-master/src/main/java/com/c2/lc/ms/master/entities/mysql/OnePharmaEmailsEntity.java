package com.c2.lc.ms.master.entities.mysql;

import com.c2.lc.lib.db.DateAudit;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "one_pharma_emails")
@IdClass(OnePharmaEmailsEntityPK.class)
public class OnePharmaEmailsEntity extends DateAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "c_c2code", nullable = false)
    @SerializedName("c_c2code")
    private String cC2Code;

    @Id
    @Column(name = "c_email", unique = true, nullable = false, length = 255)
    @SerializedName("c_email")
    private String cEmail;

    @Column(name = "c_status", length = 2)
    @SerializedName("c_status")
    private String cStatus;

    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
    }

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    public String getcStatus() {
        return cStatus;
    }

    public void setcStatus(String cStatus) {
        this.cStatus = cStatus;
    }

    @PrePersist
    void onCreate() {
        this.setTCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toLocalDateTime());
        this.setTLastUpdatedAt(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toLocalDateTime());
    }

    @PreUpdate
    void onPersist() {
        this.setTLastUpdatedAt(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toLocalDateTime());
    }
}
