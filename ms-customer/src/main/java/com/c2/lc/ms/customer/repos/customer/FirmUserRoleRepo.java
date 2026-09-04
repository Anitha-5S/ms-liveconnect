package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.FirmUserRoleEntity;
import com.c2.lc.ms.customer.entities.customer.pk.FirmUserRolePKEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("FirmUserRoleRepository")
public interface FirmUserRoleRepo extends JpaRepository<FirmUserRoleEntity, FirmUserRolePKEntity> {

    @Query("SELECT f FROM FirmUserRoleEntity f WHERE f.id.nUserId = :nUserId")
    List<FirmUserRoleEntity> findByUserId(@Param("nUserId") Long nUserId);

    @Query("SELECT f FROM FirmUserRoleEntity f WHERE f.id.nUserId = :userId AND f.id.nFirmId = :firmId")
    FirmUserRoleEntity getFirmUserRole(@Param("userId") Long userId, @Param("firmId") Long firmId);

    @Query("SELECT f FROM FirmUserRoleEntity f WHERE f.id.nUserId = :userId AND f.id.nFirmId = :firmId")
    FirmUserRoleEntity checkFirmUserRoleExists(@Param("userId") Long userId, @Param("firmId") Long firmId);

    @Query("SELECT f FROM FirmUserRoleEntity f WHERE f.id.nFirmId = :firmId")
    List<FirmUserRoleEntity> findByFirmId(Long firmId);
}
