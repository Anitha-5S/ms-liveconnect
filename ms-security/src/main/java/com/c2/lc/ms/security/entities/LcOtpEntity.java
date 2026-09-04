package com.c2.lc.ms.security.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Table(name="lc_otp")
@AllArgsConstructor
@NoArgsConstructor
public class LcOtpEntity {

    @Id
    @Column(name="c_mobile_no",length = 10,nullable = false)
    private String mobileNumber;

    @Column(name="n_otp",length = 4,nullable = false)
    private int nOtp;

    @Column(name="t_valid_till",nullable = false)
    private LocalDateTime validTill;

    @Column(name="c_used", length = 1) // Y - yes, N - no
    private String used;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LcOtpEntity that = (LcOtpEntity) o;
        return Objects.equals(mobileNumber, that.mobileNumber) &&
                nOtp == that.nOtp &&
                Objects.equals(validTill, that.validTill) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash( mobileNumber, nOtp, validTill);
    }
}
