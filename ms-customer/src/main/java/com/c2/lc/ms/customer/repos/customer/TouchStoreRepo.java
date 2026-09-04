package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.TSStoreRegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("TouchStoreRepo")
public interface TouchStoreRepo extends JpaRepository<TSStoreRegisterEntity, Long> {

    @Query("SELECT ts FROM TSStoreRegisterEntity ts WHERE ts.c2Code = :cC2Code ")
    List<TSStoreRegisterEntity> getByC2Code(String cC2Code);
}
