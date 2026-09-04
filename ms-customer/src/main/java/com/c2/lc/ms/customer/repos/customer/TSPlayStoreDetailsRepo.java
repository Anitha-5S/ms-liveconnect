package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.ContactDetailEntity;
import com.c2.lc.ms.customer.entities.customer.TSPlayStoreDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;

@Repository("TSPlayStoreDetailsRepo")
public interface TSPlayStoreDetailsRepo extends JpaRepository<TSPlayStoreDetailsEntity, String> {

    @Query("SELECT cd FROM TSPlayStoreDetailsEntity cd WHERE cd.cApplicationId = :cApplicationId")
    TSPlayStoreDetailsEntity getByAppId(@Param("cApplicationId")  String cApplicationId);
}
