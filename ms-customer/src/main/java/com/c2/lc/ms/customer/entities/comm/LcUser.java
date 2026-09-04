package com.c2.lc.ms.customer.entities.comm;

import com.c2.lc.lib.db.DateAudit;
import com.c2.lc.ms.customer.messages.FirmMessage;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="lc_user")
public class LcUser extends DateAudit implements Serializable {

    @Id
    @SerializedName("n_id")
    @Column(name="n_id")
    private long nId;

    @SerializedName("n_profile_id")
    @Column(name="n_profile_id", unique = true)
    private Long nPid;

    @SerializedName("c_user_id")
    @Column(name="c_user_id", length = 16,unique =true)
    private String userName;

    @SerializedName("c_mobile_no")
    @Size(message = FirmMessage.INVALIDATE_MOBILE_LENGTH, max = 10,min = 10)
    @NotEmpty(message = "c_mobile_no can not be empty!")
    @Column(name="c_mobile_no",length = 10)
    private String mobileNumber;

    @SerializedName("c_email_id")
    @Column(name="c_email_id",length = 120)
    private String emailId;

    @SerializedName("c_pwd")
    @Column(name="c_password", length = 512)
    private String password;

    @SerializedName("c_status")
    @Column(name="c_status",length = 1)
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

    @SerializedName("c_type")
    @Column(name="c_type",length = 1, nullable = false)
    private String type;

    @SerializedName("c_lc_user_status")
    @Column(name="c_lc_user_status",length = 1, nullable = false)
    private String lcUserStatus;

    public LcUser(Long userId, LocalDateTime currentTime) {
        super(userId, currentTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LcUser that = (LcUser) o;
        return nPid == that.nPid &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(mobileNumber, that.mobileNumber) &&
                Objects.equals(emailId, that.emailId) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nPid, userName, mobileNumber, emailId, password);
    }
}