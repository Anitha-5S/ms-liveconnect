package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.CustActMstEntity;
import com.c2.lc.ms.master.entities.mysql.CustActMstEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustActMstRepository extends JpaRepository<CustActMstEntity, CustActMstEntityPK> {
    @Query(value ="select cam1.* from cust_act_mst cam1 where cam1.c_c2code = :cC2Code1 and cam1.c_cust_category_code ='branch' and cam1.c_drug_licence_no_1 <> '' and cam1.c_drug_licence_no_1 <> '0' and  " +
            "not exists (select 1 from cust_act_mst cam2 where cam1.c_code = cam2.c_common_code and cam2.c_c2code = :cC2Code2 ) and  " +
            "not exists (select 1 from cust_act_mst_request camr  where cam1.c_code = camr.c_common_code and camr.c_c2code = :cC2Code2 ) " , nativeQuery=true)
    List<CustActMstEntity> unRegisteredBranches(@Param("cC2Code1") String cC2Code1,@Param("cC2Code2") String cC2Code2);

    @Query(value ="select cam1.* from cust_act_mst cam1 where cam1.c_c2code = :cC2Code1 and cam1.c_drug_licence_no_1 <> '' and cam1.c_drug_licence_no_1 <> '0' and  " +
            "not exists (select 1 from cust_act_mst cam2 where cam1.c_code = cam2.c_common_code and cam2.c_c2code = :cC2Code2 ) and cam1.c_cust_category_code ='branch' " , nativeQuery=true)
    List<CustActMstEntity> getNmUnregisteredBranches(@Param("cC2Code1") String cC2Code1,@Param("cC2Code2") String cC2Code2);

}
