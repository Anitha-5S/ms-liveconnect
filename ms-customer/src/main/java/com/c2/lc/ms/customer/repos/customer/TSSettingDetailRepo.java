package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.TSSettingDetailEntity;
import com.c2.lc.ms.customer.entities.customer.TSStoreRegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("TSSettingDetailRepo")
public interface TSSettingDetailRepo extends JpaRepository<TSSettingDetailEntity, String> {
}
