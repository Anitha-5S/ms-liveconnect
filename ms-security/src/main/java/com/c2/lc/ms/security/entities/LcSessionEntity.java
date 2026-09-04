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
@Table(name = "lc_session")
@AllArgsConstructor
@NoArgsConstructor
public class LcSessionEntity {

    @Id
    @Column(name = "c_key", length = 10, nullable = false)
    private String key;

    @Column(name = "c_token", length = 64, nullable = false)
    private String token;

    @Column(name = "t_valid_till", nullable = false)
    private LocalDateTime validTill;

    @Column(name = "c_status", nullable = false)
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LcSessionEntity that = (LcSessionEntity) o;
        return Objects.equals(key, that.key) &&
                token == that.token &&
                Objects.equals(validTill, that.validTill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, token, validTill);
    }
}
