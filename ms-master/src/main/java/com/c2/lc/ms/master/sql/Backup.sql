
------SellerOnProductCount -----------
select d.c_seller_code AS sellerCode, lccm.c_name AS sellerName, d.c_buyer_code AS buyerCode,
                cam.c_name as buyername,
                cbis.n_sale_rate AS sellerRate,
                cbis.n_bal_qty as stock, uit.c_stockiest_item_code as sellerItemCode,
                cbis.n_rate as mrp,
                 CASE COALESCE(cat.c_sch_category_code,
                COALESCE(cat.c_code, 'RETAIL'))
                WHEN '' THEN 'RETAIL'
                ELSE cat.c_sch_category_code
                END catCode,
                if(CASE COALESCE(cat.c_sch_category_code,
                COALESCE(cat.c_code, 'RETAIL'))
                WHEN '' THEN 'RETAIL'
                ELSE cat.c_sch_category_code
                END <> 'RETAIL' and flgChk.n_in_out_flag = 1,
                concat(if(schdet.scheme1 <> '', schdet.scheme1, ''), if(schdet.scheme2 <> '', concat(',', schdet.scheme2), ''), if(schdet.scheme3 <> '', concat(',', schdet.scheme3), ''))
                , cast(lvsch.c_scheme as char)
                ) as scheme
                from lc_mobile_user_mst m
                join lc_mobile_user_det d
                on m.n_id = d.n_mst_id
                join u_stockiest_item uit
                on d.c_seller_code = uit.c_stockiest_code
                and uit.c_ucode = :itemCode
                join lc_c2code_mst lccm on uit.c_stockiest_code = lccm.c_code
                left join cust_branch_item_stock cbis
                on uit.c_stockiest_code = cbis.c_c2code
                        and uit.c_stockiest_item_code = cbis.c_item_code
                        and cbis.c_br_code = lccm.c_cust_branch_code
                    join cust_act_mst cam on (cam.c_c2code = lccm.c_code OR cam.c_c2code = if(trim(lccm.c_parent_act_code) = '', null, lccm.c_parent_act_code)) and cam.c_code = d.c_buyer_code
                    join cust_item_mst citem on citem.c_c2code = cam.c_c2code and citem.c_code = uit.c_stockiest_item_code
                    left join live_scheme_mst lvsch on lvsch.c_firm_code = citem.c_c2code and lvsch.c_item_code = citem.c_code
                    left join cust_category_mst cat on cam.c_c2code = cat.c_c2code and cam.c_cust_category_code = cat.c_code
                    left join (
                   select im.c_c2code, im.c_code as c_item_code, schdet.c_category, schmst.d_ldate,
                 schmst.d_sch_close_date,
                 row_number() over(partition by im.c_c2code, im.c_code, schdet.c_category order by schmst.d_ldate desc) as rowid,
                 if(schdet.n_sch_qty_1 > 0, concat(schdet.n_sch_qty_1, '', if(schdet.n_free_qty_1 > 0, schdet.n_free_qty_1, ''), if(schdet.n_sch_disc_perc_1 > 0, concat(schdet.n_sch_disc_perc_1, '%'), '')) , '') as scheme1,
                 if(schdet.n_sch_qty_2 > 0, concat(schdet.n_sch_qty_2, '', if(schdet.n_free_qty_2 > 0, schdet.n_free_qty_2, ''), if(schdet.n_sch_disc_perc_2 > 0, concat(schdet.n_sch_disc_perc_2, '%'), '')) , '') as scheme2,
                 if(schdet.n_sch_qty_3 > 0, concat(schdet.n_sch_qty_3, '', if(schdet.n_free_qty_3 > 0, schdet.n_free_qty_3, ''), if(schdet.n_sch_disc_perc_3 > 0, concat(schdet.n_sch_disc_perc_3, '%'), '')) , '') as scheme3
                 from lc_mobile_user_mst m
                 join lc_mobile_user_det d
                on m.n_id = d.n_mst_id
                join u_stockiest_item uit
                on d.c_seller_code = uit.c_stockiest_code
                and uit.c_ucode = :itemCode
                join lc_c2code_mst lccm on d.c_seller_code = lccm.c_code
                join cust_item_mst im on (im.c_c2code = lccm.c_code or im.c_c2code = if(trim(lccm.c_parent_act_code) = '', null, lccm.c_parent_act_code)) and im.c_code = uit.c_stockiest_item_code
                join cust_scheme_mst schmst on schmst.c_c2code = im.c_c2code and schmst.c_item_code = im.c_code
                join cust_scheme_det schdet on schmst.c_c2code = schdet.c_c2code and schmst.c_item_code = schdet.c_item_code and schmst.c_batch_no = schdet.c_batch_no
                 where m.n_mobile_no = :mobile
                 ) schdet
                 on schdet.c_c2code = citem.c_c2code
                 and schdet.c_item_code = citem.c_code
                 and schdet.c_category = CASE COALESCE(cat.c_sch_category_code,
                 COALESCE(cat.c_code, 'RETAIL'))
                 WHEN '' THEN 'RETAIL'
                 ELSE cat.c_sch_category_code
                 end
                 and schdet.rowid = 1
                 and (schdet.d_sch_close_date = '0000-00-00'
                 or schdet.d_sch_close_date = '1900-01-01'
                 or schdet.d_sch_close_date >= curdate()
                )
                left join lc_lo_c2code flgChk on flgChk.c_c2code = cam.c_c2code
                left join lc_user_seller_priority lusp on d.n_mst_id = lusp.n_mst_id and d.c_seller_code = lusp.c_seller_code and d.c_buyer_code = lusp.c_buyer_code
                where n_buyer_flag = 1
                and m.n_mobile_no = :mobile
                order by if(cbis.n_bal_qty > 0, 0, 1), coalesce(lusp.n_buyer_seller_priority, 9999)

                -------------------------------SellerOnProduct---------------



                SELECT
                                                                s.c_stockiest_code AS sellerCode,
                                                                CAP_FIRST(stmst.c_name) AS sellerName,
                                                                s.c_stockiest_item_code AS itemCode,
                                                                CAP_FIRST(sitm.c_name) AS itemName,
                                                                sitm.c_pack_code AS pack,
                                                                st.n_rate AS mrp,
                                                                st.n_sale_rate AS ptr,
                                                                coalesce(st.n_bal_qty,0) AS stock,
                                                                sch.c_scheme AS scheme,
                                                                loc.n_in_out_flag inOutFlag,
                                                                CASE COALESCE(cmst.c_sch_category_code,
                                                                        COALESCE(cmst.c_code, 'RETAIL'))
                                                                    WHEN '' THEN 'RETAIL'
                                                                    ELSE c_sch_category_code
                                                                END catCode,
                                                               coalesce(sch.c_scheme,'') as liveSch
                                                            FROM
                                                                u_item_mst i
                                                                    LEFT OUTER JOIN
                                                                u_item_mfac_mst m ON m.c_code = i.c_item_mfac_code
                                                                    LEFT JOIN
                                                                u_stockiest_item s ON i.c_code = s.c_ucode
                                                                    AND s.c_stockiest_code IN (  seller  )
                                                                    JOIN
                                                                lc_supp_chem_comb cmb ON s.c_stockiest_code = cmb.c_c2code
                                                                    AND cmb.c_supp_chem IN (  sellerBuyer  )
                                                                    LEFT JOIN
                                                                lc_lo_c2code loc ON loc.c_c2code = s.c_stockiest_code
                                                                    LEFT JOIN
                                                                cust_item_mst sitem ON sitem.c_c2code = s.c_stockiest_code
                                                                    AND sitem.c_code = s.c_stockiest_item_code
                                                                    JOIN
                                                                cust_act_mst ac ON ac.c_c2code = cmb.c_c2code
                                                                    AND i.c_code =  code
                                                                    AND ac.c_code = cmb.c_chem_code
                                                                  LEFT JOIN
                                                                lc_user_seller_priority sp ON sp.n_mst_id = '8'
                                                                    AND sp.c_seller_code = cmb.c_c2code
                                                                    LEFT JOIN
                                                                lc_user_seller_priority sp2 ON sp2.n_mst_id = '8'
                                                                    AND sp2.c_seller_code = cmb.c_c2code
                                                                    AND sp2.c_buyer_code = cmb.c_chem_code
                                                                    JOIN
                                                                lc_c2code_mst stmst ON stmst.c_code = s.c_stockiest_code
                                                                    AND stmst.n_order_flag = 1
                                                                    LEFT JOIN
                                                                cust_category_mst cmst ON ac.c_cust_category_code = cmst.c_code
                                                                    AND ac.c_c2code = cmst.c_c2code
                                                                    LEFT JOIN
                                                                cust_scheme_det sdet ON sdet.c_c2code = sitem.c_c2code
                                                                    AND cmst.c_code = sdet.c_category
                                                                    AND sdet.c_item_code = sitem.c_code
                                                                    LEFT JOIN
                                                                (SELECT
                                                                    csm1.n_sch_qty_1,
                                                                        csm1.c_batch_no,
                                                                        csm1.n_free_qty_1,
                                                                        csm1.n_sch_disc_perc_1,
                                                                        csm1.n_sch_qty_2,
                                                                        csm1.n_free_qty_2,
                                                                        csm1.n_sch_disc_perc_2,
                                                                        csm1.n_sch_qty_3,
                                                                        csm1.n_free_qty_3,
                                                                        csm1.n_sch_disc_perc_3,
                                                                        csm1.d_sch_close_date,
                                                                        csm1.c_c2code,
                                                                        csm1.c_item_code
                                                                FROM
                                                                cust_scheme_mst  csm1
                                                                join (select csm2.c_c2code,csm2.c_item_code,max(csm2.d_ldate) d_ldate  from cust_scheme_mst csm2 where (csm2.c_c2code ,csm2.c_item_code) IN (SELECT c_stockiest_code, c_stockiest_item_code FROM  u_stockiest_item WHERE c_stockiest_code IN (seller) AND c_ucode =  code  ) group by c_c2code,c_item_code)
                                                               csm2  USING (c_c2code,c_item_code,d_ldate)
                                                            group by csm1.c_c2code,csm1.c_item_code  ) AS smst ON smst.c_c2code = s.c_stockiest_code
                                                                    AND smst.c_item_code = s.c_stockiest_item_code
                                                                    LEFT OUTER JOIN
                                                                live_scheme_mst sch ON sch.c_firm_code = s.c_stockiest_code
                                                                    AND sch.c_item_code = s.c_stockiest_item_code
                                                                    LEFT OUTER JOIN
                                                                cust_branch_item_stock st ON st.c_c2code = s.c_stockiest_code
                                                                    AND st.c_item_code = s.c_stockiest_item_code
                                                                    LEFT OUTER JOIN
                                                                u_item_pack_mst pk ON pk.c_code = i.c_item_pack_code
                                                                    LEFT JOIN
                                                                cust_item_mst sitm ON sitm.c_c2code = s.c_stockiest_code
                                                                    AND sitm.c_code = s.c_stockiest_item_code
                                                                    LEFT JOIN
                                                                cust_mfac_mst cmf ON cmf.c_c2code = sitm.c_c2code
                                                                    AND cmf.c_code = sitm.c_mfac_code
                                                                    LEFT JOIN
                                                                lc_item_filter lif ON lif.c_c2code = st.c_c2code
                                                                    AND lif.n_cancel_flag = 0
                                                                    AND CASE lif.n_item_type
                                                                    WHEN 2 THEN cmf.c_mfac_group_code = lif.c_item_type_code
                                                                    WHEN 3 THEN sitm.c_mfac_code = lif.c_item_type_code
                                                                    WHEN 4 THEN sitm.c_cat_code = lif.c_item_type_code
                                                                    WHEN 5 THEN sitm.c_cont_code = lif.c_item_type_code
                                                                    WHEN
                                                                        6
                                                                    THEN
                                                                        LEFT(sitm.c_name,
                                                                            LENGTH(lif.c_item_type_code)) = lif.c_item_type_code
                                                                    ELSE sitm.c_code = lif.c_item_type_code
                                                                END
                                                                    AND CASE lif.n_cust_type
                                                                    WHEN 2 THEN ac.c_sman_code = lif.c_cust_type_code
                                                                    WHEN 3 THEN ac.c_route_code = lif.c_cust_type_code
                                                                    WHEN 4 THEN ac.c_cust_category_code = lif.c_cust_type_code
                                                                    WHEN 5 THEN ac.c_dman_code = lif.c_cust_type_code
                                                                    WHEN 6 THEN ac.c_area_code = lif.c_cust_type_code
                                                                    ELSE ac.c_code = lif.c_cust_type_code
                                                                END
                                                            WHERE
                                                                sitm.c_name IS NOT NULL
                                                                    AND lif.n_item_type IS NULL
                                                                    AND lif.n_cust_type IS NULL
                                                            GROUP BY s.c_stockiest_code , s.c_stockiest_item_code , ac.c_code
                                                            ORDER BY (st.n_bal_qty) DESC , i.c_name ASC , stmst.c_name ASC

-------------------------------------------getSellerOnPrdt-------------------------------


                                                             SELECT
                                                                             s.c_stockiest_code AS sellerCode,
                                                                             CAP_FIRST(stmst.c_name) AS sellerName,
                                                                             s.c_stockiest_item_code AS itemCode,
                                                                             CAP_FIRST(sitm.c_name) AS itemName,
                                                                             sitm.c_pack_code AS pack,
                                                                             st.n_rate AS mrp,
                                                                             st.n_sale_rate AS ptr,
                                                                             coalesce(st.n_bal_qty,0) AS stock,
                                                                             sch.c_scheme AS scheme,
                                                                             loc.n_in_out_flag inOutFlag,
                                                                             CASE COALESCE(cmst.c_sch_category_code,
                                                                             COALESCE(cmst.c_code, 'RETAIL'))
                                                                             WHEN '' THEN 'RETAIL'
                                                                             ELSE c_sch_category_code
                                                                             END catCode,
                                                                             coalesce(sch.c_scheme,'') as liveSch
                                                                             FROM
                                                                             u_item_mst i
                                                                             LEFT OUTER JOIN
                                                                             u_item_mfac_mst m ON m.c_code = i.c_item_mfac_code
                                                                             LEFT JOIN
                                                                             u_stockiest_item s ON i.c_code = s.c_ucode
                                                                             AND s.c_stockiest_code IN ('121212','808080')
                                                                             JOIN
                                                                             lc_supp_chem_comb cmb ON s.c_stockiest_code = cmb.c_c2code
                                                                             AND cmb.c_supp_chem IN ('121212001110','808080DS001','808080DB001','808080000017','808080001116')
                                                                             LEFT JOIN
                                                                             lc_lo_c2code loc ON loc.c_c2code = s.c_stockiest_code
                                                                             LEFT JOIN
                                                                             cust_item_mst sitem ON sitem.c_c2code = s.c_stockiest_code
                                                                             AND sitem.c_code = s.c_stockiest_item_code
                                                                             JOIN
                                                                             cust_act_mst ac ON ac.c_c2code = cmb.c_c2code
                                                                             AND i.c_code =:itemCode
                                                                             AND ac.c_code = cmb.c_chem_code
                                                                             LEFT JOIN
                                                                             lc_user_seller_priority sp ON sp.n_mst_id = '8'
                                                                             AND sp.c_seller_code = cmb.c_c2code
                                                                             LEFT JOIN
                                                                             lc_user_seller_priority sp2 ON sp2.n_mst_id = '8'
                                                                             AND sp2.c_seller_code = cmb.c_c2code
                                                                             AND sp2.c_buyer_code = cmb.c_chem_code
                                                                             JOIN
                                                                             lc_c2code_mst stmst ON stmst.c_code = s.c_stockiest_code
                                                                             AND stmst.n_order_flag = 1
                                                                             LEFT JOIN
                                                                             cust_category_mst cmst ON ac.c_cust_category_code = cmst.c_code
                                                                             AND ac.c_c2code = cmst.c_c2code
                                                                             LEFT JOIN
                                                                             cust_scheme_det sdet ON sdet.c_c2code = sitem.c_c2code
                                                                             AND cmst.c_code = sdet.c_category
                                                                             AND sdet.c_item_code = sitem.c_code
                                                                             LEFT JOIN
                                                                             (SELECT
                                                                             csm1.n_sch_qty_1,
                                                                             csm1.c_batch_no,
                                                                             csm1.n_free_qty_1,
                                                                             csm1.n_sch_disc_perc_1,
                                                                             csm1.n_sch_qty_2,
                                                                             csm1.n_free_qty_2,
                                                                             csm1.n_sch_disc_perc_2,
                                                                             csm1.n_sch_qty_3,
                                                                             csm1.n_free_qty_3,
                                                                             csm1.n_sch_disc_perc_3,
                                                                             csm1.d_sch_close_date,
                                                                             csm1.c_c2code,
                                                                             csm1.c_item_code
                                                                             FROM
                                                                             cust_scheme_mst  csm1
                                                                             join (select csm2.c_c2code,csm2.c_item_code,max(csm2.d_ldate) d_ldate  from cust_scheme_mst csm2 where (csm2.c_c2code ,csm2.c_item_code) IN (SELECT c_stockiest_code, c_stockiest_item_code FROM  u_stockiest_item WHERE c_stockiest_code IN ('121212','808080') AND c_ucode = :itemCode) group by c_c2code,c_item_code)
                                                                             csm2  USING (c_c2code,c_item_code,d_ldate)
                                                                             group by csm1.c_c2code,csm1.c_item_code  ) AS smst ON smst.c_c2code = s.c_stockiest_code
                                                                             AND smst.c_item_code = s.c_stockiest_item_code
                                                                             LEFT OUTER JOIN
                                                                             live_scheme_mst sch ON sch.c_firm_code = s.c_stockiest_code
                                                                             AND sch.c_item_code = s.c_stockiest_item_code
                                                                             LEFT OUTER JOIN
                                                                             cust_branch_item_stock st ON st.c_c2code = s.c_stockiest_code
                                                                             AND st.c_item_code = s.c_stockiest_item_code
                                                                             LEFT OUTER JOIN
                                                                             u_item_pack_mst pk ON pk.c_code = i.c_item_pack_code
                                                                             LEFT JOIN
                                                                             cust_item_mst sitm ON sitm.c_c2code = s.c_stockiest_code
                                                                             AND sitm.c_code = s.c_stockiest_item_code
                                                                             LEFT JOIN
                                                                             cust_mfac_mst cmf ON cmf.c_c2code = sitm.c_c2code
                                                                             AND cmf.c_code = sitm.c_mfac_code
                                                                             LEFT JOIN
                                                                             lc_item_filter lif ON lif.c_c2code = st.c_c2code
                                                                             AND lif.n_cancel_flag = 0
                                                                             AND CASE lif.n_item_type
                                                                             WHEN 2 THEN cmf.c_mfac_group_code = lif.c_item_type_code
                                                                             WHEN 3 THEN sitm.c_mfac_code = lif.c_item_type_code
                                                                             WHEN 4 THEN sitm.c_cat_code = lif.c_item_type_code
                                                                             WHEN 5 THEN sitm.c_cont_code = lif.c_item_type_code
                                                                             WHEN
                                                                             6
                                                                             THEN
                                                                             LEFT(sitm.c_name,
                                                                             LENGTH(lif.c_item_type_code)) = lif.c_item_type_code
                                                                             ELSE sitm.c_code = lif.c_item_type_code
                                                                             END
                                                                             AND CASE lif.n_cust_type
                                                                             WHEN 2 THEN ac.c_sman_code = lif.c_cust_type_code
                                                                             WHEN 3 THEN ac.c_route_code = lif.c_cust_type_code
                                                                             WHEN 4 THEN ac.c_cust_category_code = lif.c_cust_type_code
                                                                             WHEN 5 THEN ac.c_dman_code = lif.c_cust_type_code
                                                                             WHEN 6 THEN ac.c_area_code = lif.c_cust_type_code
                                                                             ELSE ac.c_code = lif.c_cust_type_code
                                                                             END
                                                                             WHERE
                                                                             sitm.c_name IS NOT NULL
                                                                             AND lif.n_item_type IS NULL
                                                                             AND lif.n_cust_type IS NULL
                                                                             GROUP BY s.c_stockiest_code , s.c_stockiest_item_code , ac.c_code
                                                                             ORDER BY (st.n_bal_qty) DESC , i.c_name ASC , stmst.c_name ASC

                                                        ----------- New Launch Mongo Query --------------
        LocalDateTime date = helper.getCurrentTime();
        LocalDateTime date1 = date.minusDays(days);
        Criteria criteria = Criteria.where("_id").ne(null).and("d_adate").gte(date1).and("n_mrp").gt(0).and("c_gst_code").ne(null);

        org.springframework.data.mongodb.core.query.Query query = org.springframework.data.mongodb.core.query.Query.query(criteria);
        query.limit(request.getLimit());
        query.skip((long) request.getPage() *request.getLimit());
        query.with(Sort.by(Sort.Direction.DESC, "d_adate"));
        List<LcItem> lcItems = mongoOperations.find(query, LcItem.class);
        /*query.limit(request.getLimit());
        query.skip((long) request.getPage() *request.getLimit());*/

        if (lcItems.size() == 0) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        JsonArray det = (JsonArray) helper.getGson().toJsonTree(lcItems,
                new TypeToken<List<LcItem>>() {
                }.getType());
        JsonArray jsonArray  = itemsToNewLaunchOrderResponseMapper.toNewLaunchItemResponse(det, headerBO);
        return jsonArray;