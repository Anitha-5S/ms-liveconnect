package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.CustPincodewiseC2codeEntity;
import com.c2.lc.ms.master.entities.mysql.CustPincodewiseC2codeEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustPincodewiseC2codeRepository extends JpaRepository<CustPincodewiseC2codeEntity, CustPincodewiseC2codeEntityPK> {

    @Query("SELECT c FROM CustPincodewiseC2codeEntity c " +
            "WHERE c.cPincode = :pincode ORDER BY c.cC2Code, c.cCity, c.cAreaName ASC")
    List<CustPincodewiseC2codeEntity> getByPincode(String pincode);

    @Query("SELECT c FROM CustPincodewiseC2codeEntity c " +
            "WHERE c.cC2Code = :c2code ORDER BY c.cPincode, c.cCity, c.cAreaName ASC")
    List<CustPincodewiseC2codeEntity> getByC2Code(String c2code);
}
