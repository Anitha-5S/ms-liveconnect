package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.ScheduleDemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("ScheduleDemoRepository")
public interface ScheduleDemoRepo extends JpaRepository<ScheduleDemoEntity, Long> {

    @Query("SELECT sd FROM ScheduleDemoEntity sd WHERE sd.cMobileNo = :mobileNo AND sd.cProduct = :product")
    ScheduleDemoEntity checkScheduleExist(String mobileNo, String product);
}
