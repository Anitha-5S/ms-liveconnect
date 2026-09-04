package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.DocumentEntity;
import com.c2.lc.ms.customer.entities.customer.LegalIdentitiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository("LegalIdentitiesRepo")
public interface LegalIdentitiesRepo extends JpaRepository<LegalIdentitiesEntity, Long>  {

    @Query("SELECT COUNT(*) from LegalIdentitiesEntity l " +
            "WHERE :drug IN (l.cDrugLicenseNo1,l.cDrugLicenseNo2,l.cDrugLicenseNo3)")
    int isDrugLicenseExists(String drug);

}
