
package com.c2.lc.ms.security.entities;

import com.c2.lc.lib.db.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name="lc_user_attempt")
@AllArgsConstructor
@NoArgsConstructor
public class LcUserAttemptsEntity extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="n_id")
    private Long nId;

    @Column(name="c_user_id",nullable = false,unique = true)
    private String userName;

    @Column(name="n_attempt",nullable = false)
    private int nAttempt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LcUserAttemptsEntity that = (LcUserAttemptsEntity) o;
        return nAttempt == that.nAttempt &&
                Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userName, nAttempt);
    }
}


