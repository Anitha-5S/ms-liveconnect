package com.c2.lc.ms.security.entities;

import com.c2.lc.lib.db.DateAudit;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name="lc_user")
@AllArgsConstructor
@NoArgsConstructor
public class LcUserEntity extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="n_id")
    private long nId;

    @SerializedName("n_profile_id")
    @Column(name="n_profile_id")
    private Long nPid;

    @SerializedName("c_user_name")
    @Column(name="c_user_id")
    private String userName;

    @SerializedName("c_mobile_no")
    @Column(name="c_mobile_no", nullable = false, length = 10)
    private String mobileNumber;

    @SerializedName("c_email_id")
    @Column(name="c_email_id", length = 120)
    private String emailId;

    @SerializedName("c_password")
    @Column(name="c_password", nullable = false, length = 200)
    private String password;

    @SerializedName("c_type")
    @Column(name="c_type",length = 1, nullable = false)
    private String type;

    @SerializedName("c_status")
    @Column(name="c_status", length = 1, nullable = false)
    private String status;

    @SerializedName("c_c2code")
    @Column(name="c_c2code",length = 16, nullable = false)
    private String c2Code;

    @SerializedName("c_br_code")
    @Column(name="c_br_code",length = 6, nullable = false)
    private String brCode;

    @SerializedName("c_terminal_id")
    @Column(name="c_terminal_id",length = 6, nullable = false)
    private String terminalId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LcUserEntity that = (LcUserEntity) o;
        return nPid == that.nPid &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(mobileNumber, that.mobileNumber) &&
                Objects.equals(emailId, that.emailId) &&
                Objects.equals(type, that.type) &&
                Objects.equals(status, that.status) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nPid, userName, mobileNumber, emailId, password, type, status);
    }
}