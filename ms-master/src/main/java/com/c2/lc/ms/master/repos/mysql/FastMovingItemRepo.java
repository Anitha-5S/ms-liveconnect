package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.UStatewiseFastmovingItemsEntity;
import com.c2.lc.ms.master.entities.mysql.UStatewiseFastmovingItemsEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("FastMovingItemRepository")
public interface FastMovingItemRepo extends JpaRepository<UStatewiseFastmovingItemsEntity, UStatewiseFastmovingItemsEntityPK> {

}
