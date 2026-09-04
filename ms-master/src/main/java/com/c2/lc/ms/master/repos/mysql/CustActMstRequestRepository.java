package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.CustActMstRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustActMstRequestRepository extends JpaRepository<CustActMstRequest,CustActMstRequestPK> {
    @Query(value ="select * from cust_act_mst_request where c_c2code =:cC2Code1 and c_common_code = :cCommonCode", nativeQuery=true)
    CustActMstRequest getCustActMstRequest(@Param("cC2Code1") String cC2Code1, @Param("cCommonCode") String cCommonCode);
}
