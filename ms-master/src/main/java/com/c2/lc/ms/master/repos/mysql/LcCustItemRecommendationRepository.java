package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.LcCustItemRecommendation;
import com.c2.lc.ms.master.entities.mysql.LcCustItemRecommendationPK;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LcCustItemRecommendationRepository extends JpaRepository<LcCustItemRecommendation, LcCustItemRecommendationPK> {


}
