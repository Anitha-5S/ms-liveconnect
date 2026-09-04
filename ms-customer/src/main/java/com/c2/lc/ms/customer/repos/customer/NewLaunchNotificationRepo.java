package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.NewLaunchNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NewLaunchNotificationRepo extends JpaRepository<NewLaunchNotificationEntity, Long> {

    @Query(value = "select distinct n_user_id from new_launch_notification nln ",nativeQuery = true)
    List<String> getAllUserId();

    @Query(value = "select * from new_launch_notification nln WHERE n_user_id = :userId and c_seller_code = :cSellerCode ",nativeQuery = true)
    List<NewLaunchNotificationEntity> getNewLaunchedItemsByUserId(Long userId, String cSellerCode);

    @Query(value = "select * from new_launch_notification nln WHERE n_user_id = :userId and c_seller_code = :cSellerCode " +
            "and c_item_code = :cItemCode and c_buyer_code = :cBuyerCode and c_type = :type ",nativeQuery = true)
    NewLaunchNotificationEntity findRecord(String cBuyerCode, String cSellerCode, Long userId, String cItemCode, String type);

    @Query(value = "select * from new_launch_notification nln WHERE n_user_id = :userId and c_seller_code = :cSellerCode " +
            "and c_item_code = :cItemCode and c_buyer_code = :cBuyerCode and c_type = :type and d_adate >= :gDate ",nativeQuery = true)
    NewLaunchNotificationEntity findStockRecord(String cBuyerCode, String cSellerCode, Long userId,
                                                String cItemCode, String type, LocalDate gDate);
}
