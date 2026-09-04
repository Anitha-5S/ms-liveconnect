package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.RecentSearchedItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("RecentSearchedItemsRepo")
public interface RecentSearchedItemsRepo extends JpaRepository<RecentSearchedItemsEntity, Long> {
}
