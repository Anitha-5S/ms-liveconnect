package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.FirmUserEntity;
import com.c2.lc.ms.customer.entities.customer.pk.FirmUserPKEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("FirmUserRepository")
public interface FirmUserRepo extends JpaRepository<FirmUserEntity, FirmUserPKEntity> {

    @Query("SELECT f FROM FirmUserEntity f WHERE f.id.nFirmId = :firmId AND f.id.nUserId = :userId")
    FirmUserEntity getByFirmUser(Long firmId, Long userId);

    @Query("SELECT f FROM FirmUserEntity f WHERE f.id.nUserId = :userId AND f.cStatus != 'N' ")
    List<FirmUserEntity> getById(Long userId, Pageable pageable);

    @Query("SELECT COUNT(*) FROM FirmUserEntity f WHERE f.id.nFirmId = :firmId AND f.id.nUserId != :userId AND f.cStatus != 'N' ")
    int getCount(Long firmId, Long userId);

    @Query("SELECT COUNT(*) FROM FirmUserEntity f WHERE f.id.nUserId = :userId AND f.cStatus != 'N' ")
    int getBranchCount(Long userId);

    @Query("SELECT f FROM FirmUserEntity f WHERE f.id.nFirmId = :firmId AND f.id.nUserId != :userId AND f.cStatus != 'N' ")
    List<FirmUserEntity> getByFirmAndUserId(Long firmId, Long userId, Pageable pageable);

    @Query("SELECT f FROM FirmUserEntity f WHERE f.id.nFirmId = :branchId")
    List<FirmUserEntity> getUsers(Long branchId);
}
