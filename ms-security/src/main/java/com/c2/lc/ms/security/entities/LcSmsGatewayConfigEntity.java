package com.c2.lc.ms.security.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "lc_sms_gateway_config", catalog = "")
public class LcSmsGatewayConfigEntity {
    private String cC2Code;
    private String cUrl;
    private String cParam;
    private int nActive;
    private String cSenderId;

    @Id
    @Column(name = "c_c2code", nullable = false, length = 20)
    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
    }

    @Basic
    @Column(name = "c_url", nullable = false, length = 100)
    public String getcUrl() {
        return cUrl;
    }

    public void setcUrl(String cUrl) {
        this.cUrl = cUrl;
    }

    @Basic
    @Column(name = "c_param", nullable = false, length = 250)
    public String getcParam() {
        return cParam;
    }

    public void setcParam(String cParam) {
        this.cParam = cParam;
    }

    @Basic
    @Column(name = "n_active", nullable = false)
    public int getnActive() {
        return nActive;
    }

    public void setnActive(int nActive) {
        this.nActive = nActive;
    }

    @Basic
    @Column(name = "c_sender_id", nullable = true, length = 6)
    public String getcSenderId() {
        return cSenderId;
    }

    public void setcSenderId(String cSenderId) {
        this.cSenderId = cSenderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LcSmsGatewayConfigEntity that = (LcSmsGatewayConfigEntity) o;
        return nActive == that.nActive &&
                Objects.equals(cC2Code, that.cC2Code) &&
                Objects.equals(cUrl, that.cUrl) &&
                Objects.equals(cParam, that.cParam) &&
                Objects.equals(cSenderId, that.cSenderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cC2Code, cUrl, cParam, nActive, cSenderId);
    }
}

