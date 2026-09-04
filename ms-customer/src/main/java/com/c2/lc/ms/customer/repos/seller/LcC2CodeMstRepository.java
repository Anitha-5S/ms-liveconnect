package com.c2.lc.ms.customer.repos.seller;

import com.c2.lc.ms.customer.entities.seller.LcC2CodeMstEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LcC2CodeMstRepository extends JpaRepository<LcC2CodeMstEntity, Long> {
    @Query(value = "SELECT * FROM lc_c2code_mst mst " +
            "INNER JOIN pincode_mst pm on mst.c_code = pm.c_code",
    countQuery = "SELECT count(*) FROM lc_c2code_mst mst " +
            "INNER JOIN pincode_mst pm on mst.c_code = pm.c_code", nativeQuery = true)
    Page<LcC2CodeMstEntity> getUnmappedSellerList(Pageable page);
}
