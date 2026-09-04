package com.c2.lc.ms.master.entities.mysql;

import com.google.gson.annotations.SerializedName;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class OnePharmaEmailsEntityPK implements Serializable {
    @Id
    @Column(name = "c_c2code", nullable = false)
    @SerializedName("c_c2code")
    private String cC2Code;

    @Id
    @Column(name = "c_email", unique = true, nullable = false, length = 255)
    @SerializedName("c_email")
    private String cEmail;

    public OnePharmaEmailsEntityPK() {
    }

    public OnePharmaEmailsEntityPK(String cC2Code, String cEmail) {
        this.cC2Code = cC2Code;
        this.cEmail = cEmail;
    }

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
}
