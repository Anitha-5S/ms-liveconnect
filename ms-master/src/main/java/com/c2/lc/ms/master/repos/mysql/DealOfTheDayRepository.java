package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.DealOfTheDayEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DealOfTheDayRepository extends JpaRepository<DealOfTheDayEntity, Long> {
    @Query("SELECT cm FROM DealOfTheDayEntity cm WHERE cm.dealId = :deal_id ")
    DealOfTheDayEntity getByDealId(@Param("deal_id") Long deal_id);

    @Query("SELECT cm FROM DealOfTheDayEntity cm WHERE cm.c2Code = :c2Code ORDER BY cm.startDate ASC ")
    List<DealOfTheDayEntity> getAllByC2Code(String c2Code,Pageable pageable);
    //, Pageable pageable

    @Query("SELECT cm FROM DealOfTheDayEntity cm WHERE cm.dealId IN :dealIdList AND cm.itemName LIKE :searchTerm%  ORDER BY cm.itemName ASC ")
    List<DealOfTheDayEntity> getByItemName(@Param("dealIdList")List<Long> dealIdList, @Param("searchTerm") String searchTerm);

    @Query(value ="SELECT cm.* FROM deal_of_the_day cm WHERE cm.n_deal_id IN :dealIdList AND ((cm.t_start_date >= :startDate OR cm.t_start_date like :startDate%)and (cm.t_start_date <=:endDate OR cm.t_start_date like :endDate%)) ORDER BY cm.t_start_date ASC "
            , nativeQuery = true)
    List<DealOfTheDayEntity> getByDateRange(@Param("dealIdList")List<Long> dealIdList, @Param("startDate")String startDate, @Param("endDate")String endDate);

    @Query(value ="SELECT cm.* FROM deal_of_the_day cm WHERE cm.n_deal_id IN :dealIdList AND ((cm.t_start_date >= :startDate OR cm.t_start_date like :startDate%)and (cm.t_start_date <=:endDate OR cm.t_start_date like :endDate%)) AND cm.c_item_name LIKE :searchTerm% ORDER BY cm.t_start_date,cm.c_item_name ASC", nativeQuery = true)
    List<DealOfTheDayEntity> getByDateRangeAndItemName(@Param("dealIdList")List<Long> dealIdList, @Param("startDate")String startDate,@Param("endDate")String endDate, String searchTerm);

    @Query("SELECT cm FROM DealOfTheDayEntity cm WHERE cm.c2Code = :c2Code ORDER BY cm.startDate ASC ")
    List<DealOfTheDayEntity> getAllByC2CodeWithouPage(String c2Code);

    @Query("SELECT cm FROM DealOfTheDayEntity cm WHERE cm.c2Code = :c2Code AND cm.itemName LIKE :searchTerm%  ORDER BY cm.itemName ASC ")
    List<DealOfTheDayEntity> getAllByC2CodeAndSearch(String c2Code, String searchTerm, Pageable pageable);
}
