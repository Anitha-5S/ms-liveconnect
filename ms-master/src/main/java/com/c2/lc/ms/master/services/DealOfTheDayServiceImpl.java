package com.c2.lc.ms.master.services;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.ms.master.bos.DealOfTheDayBO;
import com.c2.lc.ms.master.bos.DealSearchBo;
import com.c2.lc.ms.master.bos.ItemPLPResponseBO;
import com.c2.lc.ms.master.entities.mongo.LcItem;
import com.c2.lc.ms.master.entities.mysql.DealOfTheDayEntity;
import com.c2.lc.ms.master.repos.mysql.DealOfTheDayRepository;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.DealOfTheDayService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;


@Service
public class DealOfTheDayServiceImpl extends MasterBaseServiceImpl implements DealOfTheDayService {
    @Autowired
    private DealOfTheDayRepository dealOfTheDayRepository;
    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private ItemServiceImpl itemService;


    @Override
    public DealOfTheDayEntity save(LcHeaderBO header, DealOfTheDayBO dealOfTheDayBO) throws DuplicateRecordException {

        List<Object[]> list = getSimilarItem(dealOfTheDayBO.getItemCode(),header.getC2Code());
        if(!list.isEmpty()){
            throw new DuplicateRecordException("This item is already in deal");
        }
        DealOfTheDayEntity dealEntity = new DealOfTheDayEntity();
        setdealEntity(dealEntity, dealOfTheDayBO, header);
        if (dealOfTheDayBO.getStatus() == null) {
            dealEntity.setStatus("Y");
        } else {
            dealEntity.setStatus(dealOfTheDayBO.getStatus());
        }
        dealEntity.setIdTime(header.getUserId(), helper.getCurrentTime());
        dealOfTheDayRepository.save(dealEntity);
        return dealEntity;
    }

    private List<Object[]> getSimilarItem(String itemCode, String c2Code) {
        String sql = "SELECT  dod.c_item_code, dod.n_deal_rate " +
                "                FROM deal_of_the_day dod    " +
                "                WHERE dod.c_c2code=:c2Code and dod.c_item_code= :item_code and ((dod.t_start_date <= :start_today_date or dod.t_start_date like :start_today_date) and dod.t_start_date <=:start_today and dod.t_end_date >=:start_today)  ";
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", c2Code);
        query.setParameter("start_today", helper.getCurrentDateTime("YYYY-MM-dd HH:mm:ss"));
        query.setParameter("start_today_date", helper.getString(helper.getCurrentDateTime("YYYY-MM-dd")) + '%');
        query.setParameter("item_code", itemCode);
        // System.out.println(helper.getCurrentDateTime("YYYY-MM-dd HH:mm:ss"));
        return this.getResultList(query);
    }

    @Override
    public void updateDealStatus(JsonObject jsonObject, LcHeaderBO header) throws RecordNotFoundException {
        Long deal_id = jsonObject.get("c_deal_id").getAsLong();

        DealOfTheDayEntity dealOfTheDayEntity = dealOfTheDayRepository.getByDealId(deal_id);
        if (dealOfTheDayEntity == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        dealOfTheDayEntity.setStatus(jsonObject.get("c_deals_status").getAsString());
        dealOfTheDayEntity.setTLastUpdatedAt(helper.getCurrentTime());
        dealOfTheDayEntity.setNLastUpdatedBy(header.getUserId());
        dealOfTheDayRepository.save(dealOfTheDayEntity);
    }

    @Override
    public void editDeal(LcHeaderBO header, DealOfTheDayBO dealOfTheDayBO) throws RecordNotFoundException {
        DealOfTheDayEntity dealOfTheDayEntity = dealOfTheDayRepository.getByDealId(dealOfTheDayBO.getDealId());
        if (dealOfTheDayEntity == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        setdealEntity(dealOfTheDayEntity, dealOfTheDayBO, header);
        if (dealOfTheDayBO.getStatus() != null) {
            dealOfTheDayEntity.setStatus(dealOfTheDayBO.getStatus());
        }
        dealOfTheDayEntity.setTLastUpdatedAt(helper.getCurrentTime());
        dealOfTheDayEntity.setNLastUpdatedBy(header.getUserId());
        dealOfTheDayRepository.save(dealOfTheDayEntity);
    }

    @Override
    public DealOfTheDayBO singleDeal(JsonObject jsonDeal) throws RecordNotFoundException {
        DealOfTheDayBO dealOfTheDayBO = new DealOfTheDayBO();
        DealOfTheDayEntity dealEntity = dealOfTheDayRepository.getByDealId(jsonDeal.get("n_deal_id").getAsLong());
        if (dealEntity == null) {
            throw new RecordNotFoundException("Record Not Found!");
        }
        setDealBOResponse(dealOfTheDayBO, dealEntity);
        return dealOfTheDayBO;
    }

    @Override
    public List<DealOfTheDayBO> fetchDeals(String c2Code, DealSearchBo dealSearchBo) throws RecordNotFoundException {
        Pageable pageable = PageRequest.of(dealSearchBo.getPage(), dealSearchBo.getLimit());
        List<Long> dealIdList = new ArrayList<>();
        //CouponUsedCountEntity detEntity;
        List<DealOfTheDayBO> resultList = new ArrayList<>();
        List<DealOfTheDayEntity> list;
        if (dealSearchBo.getSearchTerm() != null) {
            list = dealOfTheDayRepository.getAllByC2CodeAndSearch(c2Code, dealSearchBo.getSearchTerm(), pageable);
        } else {
            list = dealOfTheDayRepository.getAllByC2Code(c2Code, pageable);
        }
        setResultListForAdminDeal(list,resultList, dealIdList, dealSearchBo);
        return resultList;
    }

    private void setResultListForAdminDeal(List<DealOfTheDayEntity> list, List<DealOfTheDayBO> resultList, List<Long> dealIdList, DealSearchBo dealSearchBo) throws RecordNotFoundException {
        DealOfTheDayBO dealOfTheDayBO;
        if (list.size() > 0) {
            if (dealSearchBo.getSearchTerm() != null) {
                for (DealOfTheDayEntity mst : list) {
                    dealIdList.add(mst.getDealId());
                }
                List<DealOfTheDayEntity> dealList = dealOfTheDayRepository.getByItemName(dealIdList, dealSearchBo.getSearchTerm());

                resultList = getDealsResultList(dealList, resultList);

            } else if (dealSearchBo.getStartDate() != null && dealSearchBo.getEndDate() != null) {
                for (DealOfTheDayEntity mst : list) {
                    dealIdList.add(mst.getDealId());
                }
                List<DealOfTheDayEntity> dealList = dealOfTheDayRepository.getByDateRange(dealIdList, dealSearchBo.getStartDate(), dealSearchBo.getEndDate());
                resultList = getDealsResultList(dealList, resultList);

            }
//            else if (dealSearchBo.getStartDate() != null && dealSearchBo.getEndDate() != null && dealSearchBo.getSearchTerm() != null) {
//                for (DealOfTheDayEntity mst : list) {
//                    dealIdList.add(mst.getDealId());
//                }
//                List<DealOfTheDayEntity> dealList = dealOfTheDayRepository.getByDateRangeAndItemName(dealIdList, dealSearchBo.getStartDate(), dealSearchBo.getEndDate(), dealSearchBo.getSearchTerm());
//                resultList = getDealsResultList(dealList, resultList);
//            }
            else {
                for (DealOfTheDayEntity mst : list) {
                    dealOfTheDayBO = new DealOfTheDayBO();
                    setDealBOResponse(dealOfTheDayBO, mst);
                    resultList.add(dealOfTheDayBO);
                }
            }
        } else {
            throw new RecordNotFoundException("Record Not Found!");
        }
    }

    @Override
    public List<Object[]> DealOfTheDayProducts(SearchBO searchBO, LcHeaderBO header, JsonObject request) {
        List<String> itemList = new ArrayList<>();
        JsonArray brandsArray = request.has("j_brands") ? request.get("j_brands").getAsJsonArray() : new JsonArray();
        String sql = DealOfTheDayProductsQuery(searchBO, brandsArray);
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", header.getC2Code());
        if (brandsArray.size() > 0) {
            List<String> list = new ArrayList<>();
            for (JsonElement el : brandsArray) {
                list.add(el.getAsString());
            }
            query.setParameter("brandsList", list);
        }
        query.setParameter("start_today", helper.getCurrentDateTime("YYYY-MM-dd HH:mm:ss"));
        query.setParameter("start_today_date", helper.getString(helper.getCurrentDateTime("YYYY-MM-dd")) + '%');
        // System.out.println(helper.getCurrentDateTime("YYYY-MM-dd HH:mm:ss"));
        List<Object[]> list = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
       /* for (Object[] object :list) {
            itemList.add(helper.getString(object[0]));
        }*/
        return list;
    }

    private String DealOfTheDayProductsQuery(SearchBO searchBO, JsonArray brandsArray) {
        String sql = "SELECT  usi.c_ucode, cbis.n_rate, dod.c_item_code, dod.n_deal_rate ,dod.t_start_date, dod.t_end_date, dod.c_discount_type, dod.n_discount_percentage, dod.n_discount_amount    " +
                "                FROM deal_of_the_day dod    " +
                "                INNER JOIN u_stockiest_item usi on dod.c_c2code = usi.c_stockiest_code and dod.c_item_code = usi.c_stockiest_item_code    " ;
        if (brandsArray.size() > 0) {
            sql +=  "   JOIN u_item_mst uim on usi.c_ucode = uim.c_code " +
                    "   JOIN u_item_brand_mst uibm ON uim.c_item_brand_code = uibm.c_code AND uibm.c_code IN :brandsList ";
        }
        sql +=  "                LEFT JOIN u_fastmoving_items fm on usi.c_ucode = fm.c_ucode   " +
                "                LEFT JOIN cust_branch_item_stock cbis FORCE INDEX(PRIMARY) on cbis.c_c2code = dod.c_c2code and cbis.c_item_code = dod.c_item_code    " +
                "                INNER JOIN lc_c2code_mst lccm on lccm.c_code = cbis.c_c2code and lccm.c_cust_branch_code = cbis.c_br_code " +
                "                WHERE dod.c_c2code=:c2Code and dod.c_deal_status = 'Y' and ((dod.t_start_date <= :start_today_date or dod.t_start_date like :start_today_date) and dod.t_start_date <=:start_today and dod.t_end_date >=:start_today)  ";

        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_RELEVANCE)) {
            sql += "ORDER BY coalesce(fm.n_count, 0) DESC ";
        }
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_PRICE)) {
            sql += "order by coalesce(cbis.n_rate, 0) ";
        }

        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_PRICE)) {
            sql += "order by coalesce(cbis.n_rate, 0) DESC ";
        }

        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_DISCOUNT)) {
            sql += "order by coalesce(dod.n_deal_rate, 0) ";
        }

        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_DISCOUNT)) {
            sql += "order by coalesce(dod.n_deal_rate, 0) DESC  ";
        }
        return sql;

    }

    @Override
    public List<ItemPLPResponseBO> getTSDealPLP(List<Object[]> items, SearchBO searchBO, JsonObject request) {
        List<ItemPLPResponseBO> list = new ArrayList<>();
        Map<String, String> custItemMap = new HashMap<>();
        Map<String, BigDecimal> sellerRateMap = new HashMap<>();
        Map<String, BigDecimal> discountMap = new HashMap<>();
        Map<String, String> startDateMap = new HashMap<>();
        Map<String, String> endDateMap = new HashMap<>();
        Map<String, String> discTypeMap = new HashMap<>();
        Map<String, BigDecimal> discPerMap = new HashMap<>();
        Map<String, BigDecimal> discAmountMap = new HashMap<>();
        List<String> itemCodes = new ArrayList<>();
        for (Object[] object : items) {
            itemCodes.add(helper.getString(object[0]));
            sellerRateMap.put(helper.getString(object[2]), helper.getBigDecimal(object[1]));
            custItemMap.put(helper.getString(object[2]), helper.getString(object[0]));
            discountMap.put(helper.getString(object[2]), helper.getBigDecimal(object[3]));
            startDateMap.put(helper.getString(object[2]), helper.toString(helper.getString(object[4])));
            endDateMap.put(helper.getString(object[2]), helper.toString(helper.getString(object[5])));
            discTypeMap.put(helper.getString(object[2]), helper.toString(helper.getString(object[6])));
            discPerMap.put(helper.getString(object[2]), helper.getBigDecimal(object[7]));
            discAmountMap.put(helper.getString(object[2]), helper.getBigDecimal(object[8]));
        }
        Criteria criteria = Criteria.where("_id").in(itemCodes).and("c_gst_code").ne(null); // add the in clause

        org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query().addCriteria(criteria);
        List<LcItem> lcItems = mongoOperations.find(query, LcItem.class);
        List<String> custItemCodeFetch = new ArrayList<>();
        for (String itemCode : itemCodes) {
            LcItem lcItem = lcItems.stream().filter(item -> itemCode.equals(item.getItemCode())).findAny().orElse(null);
            if (lcItem != null) {
                ItemPLPResponseBO plp = itemService.getPLPResponseBO(lcItem);
                //itemService.setMrpForTSPLP(custItemMap, sellerRateMap, custItemCodeFetch, itemCode, plp);
                setOtherDetailsForDealTSPLP(custItemMap, sellerRateMap, custItemCodeFetch, itemCode, plp, discountMap, startDateMap, endDateMap, discTypeMap, discPerMap, discAmountMap);
                plp.setPackSize(lcItem.getPackSize());
                plp.setMfgCode(lcItem.getMfgCode());
                plp.setMfgName(lcItem.getMfgName());
                plp.setPackTypeName(lcItem.getPackTypeName());
                list.add(plp);
            }
        }
        return list;
    }

    @Override
    public List<Object[]> DealOfTheDayProductsCount(SearchBO searchBO, LcHeaderBO header, JsonObject request) {
        List<String> itemList = new ArrayList<>();
        JsonArray brandsArray = request.has("j_brands") ? request.get("j_brands").getAsJsonArray() : new JsonArray();
        String sql = DealOfTheDayProductsQuery(searchBO, brandsArray);
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", header.getC2Code());
        if (brandsArray.size() > 0) {
            List<String> list = new ArrayList<>();
            for (JsonElement el : brandsArray) {
                list.add(el.getAsString());
            }
            query.setParameter("brandsList", list);
        }
        query.setParameter("start_today", helper.getCurrentDateTime("YYYY-MM-dd HH:mm:ss"));
        query.setParameter("start_today_date", helper.getString(helper.getCurrentDateTime("YYYY-MM-dd")) + '%');
        // System.out.println(helper.getCurrentDateTime("YYYY-MM-dd HH:mm:ss"));
        List<Object[]> list = this.getResultList(query);
        return list;
    }

    @Override
    public int DealOfTheDayListCount(String c2Code, DealSearchBo dealSearchBo) throws RecordNotFoundException {
        List<Long> dealIdList = new ArrayList<>();
        List<DealOfTheDayBO> resultList = new ArrayList<>();
        List<DealOfTheDayEntity> list = dealOfTheDayRepository.getAllByC2CodeWithouPage(c2Code);
        setResultListForAdminDeal(list,resultList, dealIdList, dealSearchBo);
        return resultList.size();
    }

    @Override
    //public List<String> getDealItems(LcHeaderBO header)
    public List<Object[]> getDealItems(LcHeaderBO header){
        List<String> dealItemList = new ArrayList<>();
        String sql = DealItemsForDay();
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", header.getC2Code());
        query.setParameter("start_today", helper.getCurrentDateTime("YYYY-MM-dd HH:mm:ss"));
        query.setParameter("start_today_date", helper.getString(helper.getCurrentDateTime("YYYY-MM-dd")) + '%');
        // System.out.println(helper.getCurrentDateTime("YYYY-MM-dd HH:mm:ss"));
        List<Object[]> list = this.getResultList(query);
        /*for (Object[] object :list) {
            dealItemList.add(helper.getString(object[0]));
        }*/
        return list;
    }

    @Override
    public List<ItemPLPResponseBO> getTSPLP(List<Object[]> items,  List<Object[]> dealItems) {
        List<ItemPLPResponseBO> list = new ArrayList<>();
        Map<String, String> custItemMap = new HashMap<>();
        Map<String, BigDecimal> sellerRateMap = new HashMap<>();
        Map<String, BigDecimal> discountMap = new HashMap<>();
        Map<String, String> startDateMap = new HashMap<>();
        Map<String, String> endDateMap = new HashMap<>();
        Map<String, String> discTypeMap = new HashMap<>();
        Map<String, BigDecimal> discPerMap = new HashMap<>();
        Map<String, BigDecimal> discAmountMap = new HashMap<>();
        List<String> itemCodes = new ArrayList<>();
        for (Object[] object : items) {
            itemCodes.add(helper.getString(object[0]));
            sellerRateMap.put(helper.getString(object[2]), helper.getBigDecimal(object[1]));
            custItemMap.put(helper.getString(object[2]), helper.getString(object[0]));
            /*if(dealItems.contains(helper.getString(object[2]))){
                discountMap.put(helper.getString(object[2]), helper.getBigDecimal(object[3]));
                startDateMap.put(helper.getString(object[2]), helper.toString(helper.getString(object[4])));
                endDateMap.put(helper.getString(object[2]), helper.toString(helper.getString(object[5])));
                discTypeMap.put(helper.getString(object[2]), helper.toString(helper.getString(object[6])));
                discPerMap.put(helper.getString(object[2]), helper.getBigDecimal(object[7]));
                discAmountMap.put(helper.getString(object[2]), helper.getBigDecimal(object[8]));
            }*/
            for (Object[] object1 : dealItems){
                if(helper.getString(object1[0]).equals(helper.getString(helper.getString(object[2])))){
                    discountMap.put(helper.getString(object[2]), helper.getBigDecimal(object1[1]));
                    startDateMap.put(helper.getString(object[2]), helper.toString(helper.getString(object1[2])));
                    endDateMap.put(helper.getString(object[2]), helper.toString(helper.getString(object1[3])));
                    discTypeMap.put(helper.getString(object[2]), helper.toString(helper.getString(object1[4])));
                    discPerMap.put(helper.getString(object[2]), helper.getBigDecimal(object1[5]));
                    discAmountMap.put(helper.getString(object[2]), helper.getBigDecimal(object1[6]));
                }

            }
        }
        Criteria criteria = Criteria.where("_id").in(itemCodes).and("c_gst_code").ne(null); // add the in clause

        org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query().addCriteria(criteria);
        List<LcItem> lcItems = mongoOperations.find(query, LcItem.class);
        List<String> custItemCodeFetch = new ArrayList<>();
        for (String itemCode : itemCodes) {
            LcItem lcItem = lcItems.stream().filter(item -> itemCode.equals(item.getItemCode())).findAny().orElse(null);
            if (lcItem != null) {
                ItemPLPResponseBO plp = itemService.getPLPResponseBO(lcItem);
                setOtherDetailsForDealTSPLP(custItemMap, sellerRateMap, custItemCodeFetch, itemCode, plp, discountMap, startDateMap, endDateMap, discTypeMap, discPerMap, discAmountMap);
                plp.setPackSize(lcItem.getPackSize());
                plp.setMfgCode(lcItem.getMfgCode());
                plp.setMfgName(lcItem.getMfgName());
                plp.setPackTypeName(lcItem.getPackTypeName());
                list.add(plp);
            }
        }
        return list;
    }

    /*private String DealItemsForDay() {
        String sql = "SELECT  dod.c_item_code, dod.n_deal_rate " +
                "                FROM deal_of_the_day dod    " +
                "                WHERE dod.c_c2code=:c2Code and dod.c_deal_status = 'Y' and ((dod.t_start_date <= :start_today_date or dod.t_start_date like :start_today_date) and dod.t_start_date <=:start_today and dod.t_end_date >=:start_today)  ";
    return sql;
    }*/
    private String DealItemsForDay() {
        String sql = "SELECT  dotd.c_item_code, dotd.n_deal_rate, dotd.t_start_date, dotd.t_end_date, dotd.c_discount_type, dotd.n_discount_percentage, dotd.n_discount_amount " +
                "                FROM deal_of_the_day dotd    " +
                "                WHERE dotd.c_c2code=:c2Code and dotd.c_deal_status = 'Y' and ((dotd.t_start_date <= :start_today_date or dotd.t_start_date like :start_today_date) and dotd.t_start_date <=:start_today and dotd.t_end_date >=:start_today)  ";
        return sql;
    }


    private String getItemsOnC2Code() {
        return "select dod.n_deal_id from deal_of_the_day dod where dod.c_c2code = :c2Code  ";
    }

    private List<DealOfTheDayBO> getDealsResultList(List<DealOfTheDayEntity> dealList, List<DealOfTheDayBO> resultList) throws RecordNotFoundException {
        if (!dealList.isEmpty()) {
            for (DealOfTheDayEntity mst : dealList) {
                DealOfTheDayBO dealOfTheDayBO = new DealOfTheDayBO();
                setDealBOResponse(dealOfTheDayBO, mst);
                resultList.add(dealOfTheDayBO);
            }
        } else {
            throw new RecordNotFoundException("Record Not Found!");
        }
        return resultList;
    }

    private void setDealBOResponse(DealOfTheDayBO dealOfTheDayBO, DealOfTheDayEntity dealEntity) {
        dealOfTheDayBO.setDealId(dealEntity.getDealId());
        dealOfTheDayBO.setItemCode(dealEntity.getItemCode());
        dealOfTheDayBO.setItemName(dealEntity.getItemName());
        dealOfTheDayBO.setStartDateTime(dealEntity.getStartDate());
        dealOfTheDayBO.setEndDateTime(dealEntity.getEndDate());
        dealOfTheDayBO.setDiscType(dealEntity.getDiscType());
        if (dealEntity.getDiscType().equals("Percentage")) {
            dealOfTheDayBO.setDiscPercentage(dealEntity.getDiscPercentage());
        } else {
            dealOfTheDayBO.setDiscAmount(dealEntity.getDiscAmount());
        }
        dealOfTheDayBO.setStatus(dealEntity.getStatus());
    }

    private DealOfTheDayEntity setdealEntity(DealOfTheDayEntity dealEntity, DealOfTheDayBO dealOfTheDayBO, LcHeaderBO header) {
        List<Object[]> result = getMrpByItemCode(header.getC2Code(), dealOfTheDayBO.getItemCode());
        BigDecimal n_rate = BigDecimal.valueOf(0.00);
        for (Object[] objects : result) {
            n_rate = helper.getBigDecimal(objects[0]);
        }
        dealEntity.setC2Code(header.getC2Code());
        dealEntity.setItemCode(dealOfTheDayBO.getItemCode());
        if (dealOfTheDayBO.getItemName() != null) {
            dealEntity.setItemName(dealOfTheDayBO.getItemName());
        }
        if (dealOfTheDayBO.getDiscPercentage() != null) {
            dealEntity.setDiscPercentage(dealOfTheDayBO.getDiscPercentage());
            BigDecimal discount_price = (n_rate.multiply(dealOfTheDayBO.getDiscPercentage())).divide(BigDecimal.valueOf(100));
            BigDecimal deal_price = n_rate.subtract(discount_price);
            dealEntity.setDealRate(deal_price);
            dealEntity.setDiscAmount(null);
        }
        if (dealOfTheDayBO.getDiscAmount() != null) {
            dealEntity.setDiscAmount(dealOfTheDayBO.getDiscAmount());
            BigDecimal deal_price = n_rate.subtract(dealOfTheDayBO.getDiscAmount());
            dealEntity.setDealRate(deal_price);
            dealEntity.setDiscPercentage(null);
        }
        if (dealOfTheDayBO.getStartDateTime() != null) {
            dealEntity.setStartDate(dealOfTheDayBO.getStartDateTime());
        }
        if (dealOfTheDayBO.getEndDateTime() != null) {
            dealEntity.setEndDate(dealOfTheDayBO.getEndDateTime());
        }
        if (dealOfTheDayBO.getDiscType() != null) {
            dealEntity.setDiscType(dealOfTheDayBO.getDiscType());
        }
        return dealEntity;
    }


    public List<Object[]> getMrpByItemCode(String c2Code, String itemCode) {
        String sql = "SELECT cbis.n_rate, cbis.c_c2code  FROM cust_item_mst cim  " +
                "LEFT JOIN cust_branch_item_stock cbis FORCE INDEX(PRIMARY) on cbis.c_c2code = cim.c_c2code and cbis.c_item_code = cim.c_code " +
                "INNER JOIN lc_c2code_mst lccm on lccm.c_code = cbis.c_c2code and lccm.c_cust_branch_code = cbis.c_br_code " +
                "WHERE cim.c_c2code= :c2Code and cim.c_code= :itemCode ";
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", c2Code);
        query.setParameter("itemCode", itemCode);
        return this.getResultList(query);
        //return BigDecimal.valueOf(((BigDecimal)this.getSingleResult(query)).longValue());
    }

    public void setOtherDetailsForDealTSPLP(Map<String, String> custItemMap, Map<String, BigDecimal> sellerRateMap, List<String> custItemCodeFetch, String itemCode, ItemPLPResponseBO plp, Map<String, BigDecimal> discountMap, Map<String, String> startDateMap, Map<String, String> endDateMap, Map<String, String> discTypeMap, Map<String, BigDecimal> discPerMap, Map<String, BigDecimal> discAmountMap) {
        int uCodeCount = 0;
        for (Map.Entry<String, String> custItemCodeKey : custItemMap.entrySet()) {
            if (uCodeCount == 0) {
                if (custItemCodeKey.getValue().equals(itemCode)) {
                    uCodeCount++;
                    if (!custItemCodeFetch.contains(custItemCodeKey.getKey())) {
                        custItemCodeFetch.add(custItemCodeKey.getKey());
                        for (Map.Entry<String, BigDecimal> SellerRateKey : sellerRateMap.entrySet()) {
                            if (SellerRateKey.getKey().equals(custItemCodeKey.getKey())) {
                                plp.setSellerItemCode(SellerRateKey.getKey());
                                plp.setMrp(SellerRateKey.getValue());
                                for (Map.Entry<String, BigDecimal> dealrateKey : discountMap.entrySet()) {
                                    if (dealrateKey.getKey().equals(custItemCodeKey.getKey())) {
                                        plp.setOfferRate(dealrateKey.getValue());
                                    }
                                }
                                for (Map.Entry<String, String> startDateKey : startDateMap.entrySet()) {
                                    if (startDateKey.getKey().equals(custItemCodeKey.getKey())) {
                                        plp.setStartDateTime(helper.getString(startDateKey.getValue()));
                                    }
                                }
                                for (Map.Entry<String, String> endDateKey : endDateMap.entrySet()) {
                                    if (endDateKey.getKey().equals(custItemCodeKey.getKey())) {
                                        plp.setEndDateTime(helper.getString(endDateKey.getValue()));
                                    }
                                }
                                for (Map.Entry<String, String> discTypeeKey : discTypeMap.entrySet()) {
                                    if (discTypeeKey.getKey().equals(custItemCodeKey.getKey())) {
                                        plp.setDiscType(helper.getString(discTypeeKey.getValue()));
                                    }
                                }
                                for (Map.Entry<String, BigDecimal> discPerKey : discPerMap.entrySet()) {
                                    if (discPerKey.getKey().equals(custItemCodeKey.getKey())) {
                                        plp.setDiscPercentage(discPerKey.getValue());
                                    }
                                }
                                for (Map.Entry<String, BigDecimal> discAmountKey : discAmountMap.entrySet()) {
                                    if (discAmountKey.getKey().equals(custItemCodeKey.getKey())) {
                                        plp.setDiscAmount(discAmountKey.getValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }


}
