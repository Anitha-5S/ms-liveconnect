package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.LcImagesMstEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface LcImagesMstRepository extends JpaRepository<LcImagesMstEntity, Integer> {

    @Query(value = "select det.c_seller_code AS stkCode, lid.t_aws_url " +
            " FROM lc_mobile_user_mst mst" +
            " JOIN lc_mobile_user_det det ON mst.n_id = det.n_mst_id" +
            " JOIN lc_c2code_mst lcm ON det.c_seller_code = lcm.c_code" +
            " JOIN cust_act_mst act ON act.c_code = det.c_buyer_code" +
            " AND (act.c_c2code = lcm.c_code OR act.c_c2code = lcm.c_parent_act_code) " +
            " LEFT JOIN" +
            " lc_user_seller_priority sp ON mst.n_id = sp.n_mst_id" +
            " AND det.c_seller_code = sp.c_seller_code" +
            " LEFT JOIN" +
            " lc_user_seller_priority sps ON mst.n_id = sps.n_mst_id" +
            " AND det.c_seller_code = sps.c_seller_code" +
            " AND sps.c_buyer_code = det.c_buyer_code" +
            " LEFT JOIN" +
            " lc_check_c2code_lock_status lock_status ON lock_status.c_c2code = act.c_c2code " +
            " JOIN" +
            " lc_images_mst lim on lim.c_c2code = det.c_seller_code" +
            " JOIN" +
            " lc_images_det lid on lim .n_id = lid.n_id" +
            " WHERE n_mobile_no =:mobileNumber AND det.n_type = 2" +
            " AND (lock_status.c_c2code = act.c_c2code OR act.n_lock = 0)" +
            " AND lcm.n_order_flag = 1 AND lcm.n_non_visible_flag=0" +
            " AND lim.c_type = 'LOGO' " +
            " GROUP BY det.c_seller_code , det.c_buyer_code" +
            " ORDER BY COALESCE(sps.n_buyer_seller_priority, sp.n_priority) ", nativeQuery = true)
    List<Object[]> findPreferredSellerCodeByMobileNumber(String mobileNumber, Pageable page);

//    @Query(value = "select lccm.c_code, lccm.c_name, lid.t_aws_url, lim.c_type from " +
//            "order_buk_new.lc_c2code_mst lccm " +
//            "left join " +
//            "order_buk_new.lc_images_mst lim " +
//            "on lccm.c_code = lim.c_c2code " +
//            "left join " +
//            "order_buk_new.lc_images_det lid " +
//            "on lim .n_id = lid.n_id " +
//            "where " +
//            "lccm.c_pin = :pincode  order by lccm.c_code", nativeQuery = true)

    @Query(value = "select lccm.c_code, lccm.c_name, imgDet.t_aws_url, imgMst.c_type  from lc_c2code_mst lccm  " +
            "join pincode_mst pin on lccm.c_pin = pin.c_code  " +
            "join (  " +
            "  select c_district from pincode_mst pm where c_c2code = 'C2INFO' and c_code = :pincode  " +
            "  ) t2 on pin.c_district = t2.c_district  " +
            "left join lc_images_mst imgMst  " +
            "  on lccm.c_code = imgmst.c_c2code  " +
            "  and imgmst.c_c2code = imgmst.c_code  " +
            "  and imgmst.c_type = 'LOGO'  " +
            "left join lc_images_det imgDet  " +
            "  on imgmst.n_id = imgDet.n_mstId and imgDet.n_status = 1  " +
            "left join lc_seller_preference prf on lccm.c_code = prf.c_c2code " +
            "where lccm.n_non_visible_flag = 0 and left(lccm.c_code, 3) <> 'UAT'" +
            "order by coalesce(prf.n_ord_cnt, 0) desc, coalesce(prf.n_inv_cnt, 0) desc", nativeQuery = true)
    List<Object[]> findPreferredSellerCodeByPincode(String pincode, Pageable page);

    @Query(value = "select lccm.c_code, lccm.c_name, lid.t_aws_url, lim.c_type from " +
            "order_buk_new.lc_c2code_mst lccm " +
            "left join " +
            "order_buk_new.lc_images_mst lim " +
            "on lccm.c_code = lim.c_c2code " +
            "left join " +
            "order_buk_new.lc_images_det lid " +
            "on lim .n_id = lid.n_id " +
            "where " +
            "lccm.c_city = :city  order by lccm.c_code", nativeQuery = true)
    List<Object[]> findPreferredSellerCodeByCity(String city, Pageable page);

    @Query(value = "            select cim.c_c2code,lccm.c_name , lid.t_aws_url     " +
            "                                    from cust_inv_mst cim FORCE INDEX(idx_combo)    " +
            "                                    left join lc_c2code_mst lccm      " +
            "                                    on cim.c_c2code = lccm.c_code      " +
            "                                    left join lc_images_mst lim     " +
            "                                    on lim.c_c2code = lccm.c_code     " +
            "                                    left join lc_images_det lid      " +
            "                                    on lim.n_id = lid.n_mstId " +
            "                                    left join lc_seller_buyer_priority lsbp  " +
            "                                    on lsbp.c_seller_code = cim.c_c2code  " +
            "                                    and lsbp.c_buyer_code = cim.c_cust_code  " +
            "                                    where lim.c_type='LOGO'  and lsbp.n_firm_id =:firmId " +
            "                                    group by cim.c_c2code order by sum(cim.n_total) desc ",nativeQuery = true)
    List<Object[]> findPreferredSellerCodeByInvoice(long firmId, Pageable page);

    @Query("SELECT lid.tAwsUrl, lid.nOfferCode, lim.c2Code FROM LcImagesDetEntity lid " +
            ", LcImagesMstEntity lim WHERE lid.mstId = lim.nId AND lid.nOfferCode is not null ")
    List<Object[]> findOffers(Pageable page);

    @Query(value = "            select cim.c_c2code,lccm.c_name , lid.t_aws_url  " +
            "                        from cust_inv_mst cim  FORCE INDEX(idx_combo)  " +
            "                        left join lc_c2code_mst lccm    " +
            "                        on cim.c_c2code = lccm.c_code    " +
            "                        left join lc_images_mst lim   " +
            "                        on lim.c_c2code = lccm.c_code   " +
            "                        left join lc_images_det lid    " +
            "                        on lim.n_id = lid.n_mstId   " +
            "                        where cim.c_cust_code in :buyerCode and cim.c_c2code in :sellerCode and lim.c_type='LOGO' " +
            "                        group by cim.c_c2code order by sum(cim.n_total) desc", nativeQuery = true)
    List<Object[]> findPreferredSellerCodeByInvoice(List<String> buyerCode, List<String> sellerCode);

//    @Query(value = "select lccm.c_code, lid.t_aws_url, lim.c_type from " +
//            "order_buk_new.lc_c2code_mst lccm " +
//            "left join " +
//            "order_buk_new.lc_images_mst lim " +
//            "on lccm.c_code = lim.c_c2code " +
//            "left join " +
//            "order_buk_new.lc_images_det lid " +
//            "on lim .n_id = lid.n_id " +
//            "where " +
//            "lccm.c_pin = :pincode ", nativeQuery = true)

    @Query(value = "select lccm.c_code, lccm.c_name, imgDet.t_aws_url, imgMst.c_type  from lc_c2code_mst lccm  " +
            "            join pincode_mst pin on lccm.c_pin = pin.c_code  " +
            "            join (  " +
            "              select c_district from pincode_mst pm where c_c2code = 'C2INFO' and c_code = :pincode  " +
            "              ) t2 on pin.c_district = t2.c_district  " +
            "            left join lc_images_mst imgMst  " +
            "              on lccm.c_code = imgmst.c_c2code  " +
            "              and imgmst.c_c2code = imgmst.c_code  " +
            "              and imgmst.c_type = 'LOGO'  " +
            "            left join lc_images_det imgDet  " +
            "              on imgmst.n_id = imgDet.n_mstId and imgDet.n_status = 1  " +
            "            where lccm.n_non_visible_flag = 0 and left(lccm.c_code, 3) <> 'UAT'", nativeQuery = true)
    List<Object[]> findPreferredSellerCodeByPincode(String pincode);

    @Query(value = "select lccm.c_code, lid.t_aws_url, lim.c_type from " +
            "order_buk_new.lc_c2code_mst lccm " +
            "left join " +
            "order_buk_new.lc_images_mst lim " +
            "on lccm.c_code = lim.c_c2code " +
            "left join " +
            "order_buk_new.lc_images_det lid " +
            "on lim .n_id = lid.n_id " +
            "where " +
            "lccm.c_city = :city ", nativeQuery = true)
    List<Object[]> findPreferredSellerCodeByCity(String city);

}
