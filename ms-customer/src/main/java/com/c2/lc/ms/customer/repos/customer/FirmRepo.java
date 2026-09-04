package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.FirmEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository("FirmRepository")
public interface FirmRepo extends JpaRepository<FirmEntity, Long> {

    @Query("SELECT f FROM FirmEntity f WHERE f.cMobileNo = :mobileNo")
    List<FirmEntity> getByMobileNo(String mobileNo);

    @Query("SELECT f FROM FirmEntity f WHERE f.cEmail = :email")
    FirmEntity getByEmail(String email);

    @Query("SELECT f FROM FirmEntity f WHERE f.cType = :type ")
    List<FirmEntity> findBySeller(String type);

    @Query("SELECT f FROM FirmEntity f WHERE f.cType = :type")
    List<FirmEntity> findByBuyer(String type);

    @Query("SELECT f FROM FirmEntity f WHERE f.cMobileNo = :mobileNo AND f.cType = :type")
    FirmEntity getByMobileWithType( String mobileNo, String type);

    @Query("SELECT COUNT(*) FROM FirmEntity f WHERE f.cGstNo = :gstNumber AND f.cStatus !='N' AND f.nCreatedBy != :userId ")
    int getByGst(String gstNumber, Long userId);

    @Query("SELECT f FROM FirmEntity f WHERE f.c2Code = :cC2Code ")
    List<FirmEntity> getByC2Code(String cC2Code, Pageable pageable);

    @Query("SELECT f FROM FirmEntity f WHERE f.nFirmId IN :firmIdList AND UPPER(f.cName) LIKE UPPER(CONCAT(:searchTerm,'%'))")
    List<FirmEntity> getByNameAndId(List<Long> firmIdList, String searchTerm);

    @Transactional
    @Modifying
    @Query("update FirmEntity fm set fm.c2Code = :c_csquare_code, fm.tLastUpdatedAt= :currentTime where fm.cUcode = :c_ucode ")
    int updateC2codeByucode(String c_ucode, String c_csquare_code, LocalDateTime currentTime);

    @Query("SELECT f FROM FirmEntity f WHERE f.c2Code = :c2Code AND f.cUcode = :uCode ")
    List<FirmEntity> getByC2CodeAndUcode(String c2Code, String uCode);

}
