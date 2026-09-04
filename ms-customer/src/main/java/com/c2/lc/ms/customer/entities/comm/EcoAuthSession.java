package com.c2.lc.ms.customer.entities.comm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "eco_auth_session")
public class EcoAuthSession implements Serializable {

    private static final long serialVersionUID = -1955625817160672590L;

    @EmbeddedId
    private EcoAuthSessionPK id;

    @Column(name="c_token")
    private String token;

    @Column(name="t_expiry_time")
    private LocalDateTime expiryTime;

}
