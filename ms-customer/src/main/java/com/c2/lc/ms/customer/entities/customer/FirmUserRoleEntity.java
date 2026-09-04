package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.lib.db.DateAudit;
import com.c2.lc.ms.customer.entities.customer.pk.FirmUserRolePKEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * The persistent class for the firm_user_roles database table.
 */
@Entity
@Table(name = "firm_user_roles")
@NamedQuery(name = "FirmUserRole.findAll", query = "SELECT f FROM FirmUserRoleEntity f")
public class FirmUserRoleEntity extends DateAudit implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private FirmUserRolePKEntity id;

    @Column(name = "c_firm_user_role", length = 1024)
    private String cFirmUserRole;

    public FirmUserRoleEntity() {
    }

    public FirmUserRoleEntity(Long userId, LocalDateTime currentTime) {
        super(userId, currentTime);
    }

    public FirmUserRolePKEntity getId() {
        return this.id;
    }

    public void setId(FirmUserRolePKEntity id) {
        this.id = id;
    }

    public String getCFirmUserRole() {
        return this.cFirmUserRole;
    }

    public void setCFirmUserRole(String cFirmUserRole) {
        this.cFirmUserRole = cFirmUserRole;
    }

}