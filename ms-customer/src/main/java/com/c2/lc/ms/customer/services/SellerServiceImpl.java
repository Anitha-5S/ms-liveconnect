package com.c2.lc.ms.customer.services;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.ms.customer.bos.SellerCreationBO;
import com.c2.lc.ms.customer.bos.SellerDetailBO;
import com.c2.lc.ms.customer.bos.SellerPriorityBo;
import com.c2.lc.ms.customer.entities.customer.*;
import com.c2.lc.ms.customer.entities.seller.LcC2CodeMstEntity;
import com.c2.lc.ms.customer.entities.seller.LcSellerBuyerPriorityEntity;
import com.c2.lc.ms.customer.repos.customer.FirmRepo;
import com.c2.lc.ms.customer.repos.customer.FirmSellersRepo;
import com.c2.lc.ms.customer.repos.customer.NewLaunchNotificationRepo;
import com.c2.lc.ms.customer.repos.seller.LcUserSellerPriority;
import com.c2.lc.ms.customer.repos.seller.LcC2CodeMstRepository;
import com.c2.lc.ms.customer.services.interfaces.SellerService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class SellerServiceImpl extends BaseDBServiceImpl implements SellerService {
    @Autowired
    private LcC2CodeMstRepository lcC2CodeMstRepository;
    @Autowired private
    LcUserSellerPriority userSellerPriority;
    @Autowired
    private FirmRepo firmRepo;
    @Autowired
    private NewLaunchNotificationRepo newLaunchNotificationRepo;
    @Autowired
    private FirmSellersRepo firmSellersRepo;
    @Value("${notification.api.url}")
    private String notificationUrl;

    @Value("${lost.stock.days}")
    private int lostDays;

    @PersistenceContext(unitName = "mysql")
    @Autowired
    EntityManager entityManager;

    @Override
    public JsonArray sendReqToSeller() {
        return null;
    }

    @Override
    public JsonArray fetchWithFilters() {
        return null;
    }

    @Override
    public JsonArray getUnmappedSellerList(Pageable page) {
        List<LcC2CodeMstEntity> fetchResults = lcC2CodeMstRepository.getUnmappedSellerList(page).getContent();
        JsonArray resultsArray = new JsonArray();
        for (LcC2CodeMstEntity object : fetchResults) {
            JsonObject sellerDetails = new JsonObject();
            sellerDetails.addProperty("cCode", object.getcCode());
            sellerDetails.addProperty("cName", object.getcName());
            sellerDetails.addProperty("cCity", object.getcCity());
            sellerDetails.addProperty("cState", object.getPinCodeObj().getcState());
            resultsArray.add(sellerDetails);
        }
        return resultsArray;
    }//    private JsonObject getUnmappedSellerMapping(LcC2CodeMstEntity sellerEntityObj){ JsonObject sellerObj = new
//        JsonObject();
//        sellerObj.addProperty("",sellerEntityObj.get);
    /*    }*/

    @Override
    public List<JsonObject> fetchMappedSellers(LcHeaderBO lcHeaderBO, PageBO pageBO, String mobile) throws RecordNotFoundException {
        List<JsonObject> list = new ArrayList<>();
        JsonObject sellerDetails;
        List<Object[]> mappedSellers;
        String id = sellerBuyerCombine(lcHeaderBO);
        if (helper.isEmpty(id) || id == null ) {
            throw new RecordNotFoundException("Firm seller not found");
        }
        String mappedSql = getMappedSeller(id, String.valueOf(lcHeaderBO.getFirmId()));
        Query mappedQuery = entityManager.createNativeQuery(mappedSql);
        mappedSellers = this.getResultList(mappedQuery, pageBO.getPage(), pageBO.getLimit());
        if (mappedSellers.isEmpty()) {
            return list;
            /*throw new RecordNotFoundException("No Record Found");*/
        } else {
            for (Object[] objects : mappedSellers) {
                sellerDetails = new JsonObject();
                sellerDetails.addProperty("c_seller_code", helper.getString(objects[0]));
                sellerDetails.addProperty("c_seller_name", helper.getString(objects[1]));
                sellerDetails.addProperty("c_priority", helper.getString(objects[2]));
                sellerDetails.addProperty("c_buyer_code", helper.getString(objects[3]));
                sellerDetails.addProperty("c_pending_credit", helper.getString(objects[4]));
                sellerDetails.addProperty("n_product_scheme", helper.getString(objects[5]));
                list.add(sellerDetails);
            }
        }
        return list;
    }

    @Override
    public void updatePriority(LcHeaderBO lcHeaderBO, SellerPriorityBo sellerPriorityBo) throws RecordNotFoundException {
       // String mobile = getMobileNumber(lcHeaderBO.getUserId(), lcHeaderBO.getFirmId());
       // int mstId = 0;
       /* if (!sellerPriorityBo.getSellerCode().matches("^[a-zA-Z0-9\\s]*$")) {
            throw new IllegalArgumentException("Enter valid Seller code");
        } else if (!sellerPriorityBo.getBuyerCode().matches("^[a-zA-Z0-9\\s]*$")) {
            throw new IllegalArgumentException("Enter valid Buyer code");
        }*/
       /* String seller = null ;
        String buyer = null ;
        List<JsonObject> sellerBuyer = getFirmSeller(lcHeaderBO);
        List<Object> sellerBuyerList = new ArrayList<>();
        for(JsonObject jsonObject: sellerBuyer){
            seller = sellerBuyer.get(0).getAsString();
            buyer = sellerBuyer.get(1).getAsString();
           sellerBuyerList.add("'" +seller+buyer+ "'");
        }*/
        //String sql = mappedSeller(sellerBuyerCombine(lcHeaderBO), sellerPriorityBo.getMobile());
        String sql = getMappedSeller(sellerBuyerCombine(lcHeaderBO), String.valueOf(lcHeaderBO.getFirmId()));
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = this.getResultList(query);
        int i =0;
        //int j = 1;
        if(sellerPriorityBo.getPriority() < sellerPriorityBo.getCurrentPriority()) {
            for ( i = sellerPriorityBo.getPriority(); i <= sellerPriorityBo.getCurrentPriority(); i++) {
                Object[] resultSet = resultList.get(i-1);
                LcSellerBuyerPriorityEntity sellerPriorityEntity = new LcSellerBuyerPriorityEntity();
                sellerPriorityEntity.setCSellerCode(helper.getString(resultSet[0]));
                if(i == sellerPriorityBo.getCurrentPriority() )
                {
                    sellerPriorityEntity.setNBuyerSellerPriority(sellerPriorityBo.getPriority());
                }
                else {
                    sellerPriorityEntity.setNBuyerSellerPriority(sellerPriorityBo.getPriority() + i);
                }
                    sellerPriorityEntity.setCBuyerCode(helper.getString(resultSet[3]));
                    sellerPriorityEntity.setNFirmId(lcHeaderBO.getFirmId());
                    sellerPriorityEntity.setDTime(helper.getCurrentTime());
                    userSellerPriority.save(sellerPriorityEntity);
                    //j +=1;
            }

        }
        else
        {
          //  j = sellerPriorityBo.getPriority();
            for ( i = sellerPriorityBo.getCurrentPriority(); i <= sellerPriorityBo.getPriority(); i++) {
                Object[] resultSet = resultList.get(i-1);
                LcSellerBuyerPriorityEntity sellerPriorityEntity = new LcSellerBuyerPriorityEntity();
                sellerPriorityEntity.setCSellerCode(helper.getString(resultSet[0]));
                if(i == sellerPriorityBo.getCurrentPriority() )
                {
                    sellerPriorityEntity.setNBuyerSellerPriority(sellerPriorityBo.getPriority());
                }
                else {
                    sellerPriorityEntity.setNBuyerSellerPriority(i-1);
                }
                sellerPriorityEntity.setCBuyerCode(helper.getString(resultSet[3]));
                sellerPriorityEntity.setNFirmId(lcHeaderBO.getFirmId());
                sellerPriorityEntity.setDTime(helper.getCurrentTime());
                userSellerPriority.save(sellerPriorityEntity);
               // j -=1;
            }

        }



       /* String sql = "select n_id from lc_mobile_user_mst lmum where n_mobile_no = :mobile ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("mobile", mobile);
        Object result = this.getSingleResult(query);

        if (result != null) {
            mstId = helper.getInt(result);
        }

        LcUserSellerPriorityEntity sellerPriorityEntity = userSellerPriority.getPriority(sellerPriorityBo.getSellerCode(),
                sellerPriorityBo.getBuyerCode(), mobile);
        if (sellerPriorityEntity == null) {
            sellerPriorityEntity = new LcUserSellerPriorityEntity();
            sellerPriorityEntity.setNMstId(mstId);
            sellerPriorityEntity.setCSellerCode(sellerPriorityBo.getSellerCode());
            sellerPriorityEntity.setCBuyerCode(sellerPriorityBo.getBuyerCode());
        }
        *//*for( JsonObject jsonObject: sellerList) {
            if (sellerPriorityBo.getCurrentPriority() > sellerPriorityBo.getPriority()) {
                String sql1 = reorderPriority();
                Query query1 = entityManager.createQuery(sql);
                query1.setParameter("mstId", mstId);
                query1.setParameter("sellerCode", jsonObject.get("c_seller_code").getAsString());
                query1.setParameter("buyerCode", sellerList.get(3).getAsString());
            }
            else if (sellerPriorityBo.getCurrentPriority() < sellerPriorityBo.getPriority()) {
                String sql1 = updateSellerPriorityDesc();
                Query query1 = entityManager.createQuery(sql);
                query1.setParameter("mstId", mstId);
                query1.setParameter("sellerCode", sellerList.get(0).getAsString());
                query1.setParameter("buyerCode", sellerList.get(3).getAsString());
            }
        }*//*
        sellerPriorityEntity.setNBuyerSellerPriority(sellerPriorityBo.getPriority());
        userSellerPriority.save(sellerPriorityEntity);*/
    }


    @Override
    public SellerDetailBO fetchSellers(String sellerCode) throws RecordNotFoundException {
        SellerDetailBO sellerDetailBO = null;
        List<Object[]> sellers;

        String sql = "SELECT lccm.c_code c_seller_code, lccm.c_mobile, lccm.c_name c_seller_name, " +
                "    lccm.c_add_1, lccm.c_add_2, lccm.c_contact_person, lccm.c_pin, lccm.c_drug_licence_no_1," +
                "    lccm.d_dl_expiry_date," +
                "    lccm.c_city, lccm.c_area_code, pm.c_state, pm.c_state_code, ugam.c_name, ugcm.c_code, " +
                "    lccm.c_email" +
                "    FROM lc_c2code_mst lccm JOIN pincode_mst pm ON pm.c_code = lccm.c_pin" +
                "    LEFT JOIN u_geo_city_mst ugcm ON ugcm.c_name = lccm.c_city" +
                "    LEFT JOIN u_geo_area_mst ugam ON ugam.c_code = lccm.c_area_code WHERE lccm.c_code = :sellerCode";

        Query sellerDetailQuery = entityManager.createNativeQuery(sql);
        sellerDetailQuery.setParameter("sellerCode", sellerCode);
        sellers = this.getResultList(sellerDetailQuery);

        if (sellers.isEmpty()) {
            throw new RecordNotFoundException("No Records found");
        } else {
            String[] mail;
            JsonArray arr;
            for (Object[] objects : sellers) {
                sellerDetailBO = new SellerDetailBO();
                arr = new JsonArray();
                sellerDetailBO.setSellerCode(helper.getString(objects[0]));
                sellerDetailBO.setMobileNo(helper.getString(objects[1]));
                sellerDetailBO.setSellerName(helper.getString(objects[2]));
                sellerDetailBO.setAddress1(helper.getString(objects[3]));
                sellerDetailBO.setAddress2(helper.getString(objects[4]));
                sellerDetailBO.setContactName(helper.getString(objects[5]));
                sellerDetailBO.setPinCode(helper.getString(objects[6]));
                sellerDetailBO.setDrugLicenseNo(helper.getString(objects[7]));
                sellerDetailBO.setDrugLicenseNoExpiryDate(helper.getString(objects[8]));
                sellerDetailBO.setCityName(helper.getString(objects[9]));
                sellerDetailBO.setAreaCode(helper.getString(objects[10]));
                sellerDetailBO.setStateName(helper.getString(objects[11]));
                sellerDetailBO.setStateCode(helper.getString(objects[12]));
                sellerDetailBO.setAreaName(helper.getString(objects[13]));
                sellerDetailBO.setCityCode(helper.getString(objects[14]));
                mail = helper.getString(objects[15]).split(";|\\,");
                for (String mailId : mail) {
                    arr.add(mailId);
                }
                sellerDetailBO.setJEmail(arr);
            }
        }
        return sellerDetailBO;
    }

    @Override
    public void createSeller(SellerCreationBO sellerCreationBO) {
        FirmEntity firmEntity = new FirmEntity();
        firmEntity.setCName(sellerCreationBO.getFirmName());
        firmEntity.setCType(sellerCreationBO.getCType());
        firmEntity.setCMobileNo(sellerCreationBO.getMobileNo());
        firmEntity.setCPin(sellerCreationBO.getPinCode());
        firmEntity.setCGstNo(sellerCreationBO.getGstNumber());
        firmEntity.setCStateCode(sellerCreationBO.getStateCode());
        firmEntity.setCStateName(sellerCreationBO.getStateName());
        firmEntity.setCCityCode(sellerCreationBO.getCityCode());
        firmEntity.setCCityName(sellerCreationBO.getCityName());
        firmEntity.setContactDetail(getContact(sellerCreationBO));
        firmEntity.setLegalIdentities(getLegalIdentities(sellerCreationBO));
        firmRepo.save(firmEntity);
    }

    private LegalIdentitiesEntity getLegalIdentities(SellerCreationBO sellerCreationBO) {
        LegalIdentitiesEntity legalIdentitiesEntity = new LegalIdentitiesEntity();
        legalIdentitiesEntity.setCDrugLicenseNo1(sellerCreationBO.getDrugLicenseNo1());
        return legalIdentitiesEntity;
    }

    private ContactDetailEntity getContact(SellerCreationBO sellerCreationBO) {
        ContactDetailEntity contact = new ContactDetailEntity();
        contact.setCContactName(sellerCreationBO.getContactName());
        contact.setCStateCode(sellerCreationBO.getStateCode());
        contact.setCStateName(sellerCreationBO.getStateName());
        contact.setCCityCode(sellerCreationBO.getCityCode());
        contact.setCCityName(sellerCreationBO.getCityName());
        return contact;
    }

    @Override
    public List<SellerDetailBO> fetchUnmappedSellers(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws RecordNotFoundException {
        PageBO pageBO = new PageBO();
        FirmEntity firm = getMobileAndState(lcHeaderBO.getUserId(), lcHeaderBO.getFirmId());
       // List<JsonObject> firmSellers = getFirmSeller(lcHeaderBO);
        String id = getBranchSeller(lcHeaderBO);
        String sql = null;
        Query query = null;
        int i= 0;
        if(!helper.isEmpty(searchBO.getSearchTerm())){
            i = 1;
            sql = getUnmappedSellerOnProduct(id);
            query = entityManager.createNativeQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
            query.setParameter("pinCode",helper.getString(firm.getCPin()));
        }
        else {
            sql = getUnMappedSellerQuery(id);
            query = entityManager.createNativeQuery(sql);
            query.setParameter("state", helper.getContainLikeQueryString(firm.getCStateName()));
        }
        List<Object[]> allSellers = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        return getUnmappedSellerList(allSellers,i);
    }
    private String getUnmappedSellerOnProduct(String sellerCode) {
        String sql = " SELECT  distinct lccm.c_code as sellerCode, " +
                " lccm.c_name as sellerName, " +
                " pm.c_district as cityName, " +
                " lccm.c_area_code as areaCode, " +
                " pm.c_name as areaName, " +
                " pm.c_state as stateName, " +
                " coalesce(cbis.n_sale_rate,0) as sellerRate," +
                " coalesce(cbis.n_bal_qty, 0) as stock" +
                " from pincode_mst pm " +
                " left join pincode_mst pm2 on pm2.c_c2code = pm.c_c2code and pm.c_state_code = pm2.c_state_code and pm.c_district = pm2.c_district " +
                " left join pincode_mst pm3 on pm3.c_c2code = pm.c_c2code and pm.c_state_code = pm3.c_state_code " +
                " join lc_c2code_mst lccm on pm2.c_code = lccm.c_pin " +
                " join u_stockiest_item usi on usi.c_stockiest_code = lccm.c_code and usi.c_ucode = :search" +
                " left join cust_branch_item_stock cbis on cbis.c_c2code = usi.c_stockiest_code and usi.c_stockiest_item_code = cbis.c_item_code ";
        sql += "  where pm.c_c2code = 'C2INFO' and pm.c_code = :pinCode " +
                " and (pm2.c_c2code is not null or pm3.c_c2code is not null) ";
        if (!helper.isEmpty(sellerCode)) {
            sql += " and lccm.c_code not in (" + sellerCode + ")  ";
        }
        sql += " and cbis.n_bal_qty > 0 " +
                " order by if(pm2.c_c2code is not null, 0, 1), if(pm3.c_c2code is not null, 0, 1) asc ";
        return sql;
    }

    private List<SellerDetailBO> getUnmappedSellerList(List<Object[]> resultList, int j) throws RecordNotFoundException {
        List<SellerDetailBO> list = new ArrayList<>();
        /*FirmEntity firm = getMobileAndState(lcHeaderBO.getUserId(), lcHeaderBO.getFirmId());

        Query allSellersQuery = entityManager.createNativeQuery(sql);
        //allSellersQuery.setParameter("mobile", firm.getCMobileNo());
        allSellersQuery.setParameter("state", helper.getContainLikeQueryString(firm.getCStateName()));
        List<Object[]> allSellers = this.getResultList(allSellersQuery, pageBO.getPage(), pageBO.getLimit());*/

        if (resultList.isEmpty()) {
            return list;
            /*throw new RecordNotFoundException("No Records found");*/
        } else {
            SellerDetailBO sellerDetailBO;
            for (Object[] objects : resultList) {
                int i = -1;
                sellerDetailBO = new SellerDetailBO();
                sellerDetailBO.setSellerCode(helper.getString(objects[++i]));
                sellerDetailBO.setSellerName(helper.getString(objects[++i]));
                sellerDetailBO.setCityName(helper.getString(objects[++i]));
                sellerDetailBO.setAreaCode(helper.getString(objects[++i]));
                sellerDetailBO.setAreaName(helper.getString(objects[++i]));
                sellerDetailBO.setStateName(helper.getString(objects[++i]));
                if(j==1){
                sellerDetailBO.setSellerRate(helper.getBigDecimal(objects[++i]).doubleValue());
                sellerDetailBO.setSellerStock(helper.getBigDecimal(objects[++i]).intValue());
                }
                else
                {
                    sellerDetailBO.setSellerRate(0);
                    sellerDetailBO.setSellerStock(0);
                }
                list.add(sellerDetailBO);
            }
        }
        return list;
    }

    private FirmEntity getMobileAndState(Long userId, Long firmId) {
        return firmRepo.getOne(firmId);
    }


    @Override
    public int getCount(LcHeaderBO lcHeaderBO, String mobileNumber) throws NoSuchFieldException, RecordNotFoundException {
        /*String mobile = getMobileNumber(userId, firmId);*/

       // List<JsonObject> firmSellers = getFirmSeller(lcHeaderBO);
        String id = getBranchSeller(lcHeaderBO);
        String mappedSql = getMappedSeller(id, String.valueOf(lcHeaderBO.getFirmId()));
        Object countList;
        BigInteger count = BigInteger.ZERO;
        String countQuery = "SELECT COUNT(*) FROM (" + mappedSql + ")AS DUMMY";

        Query query = entityManager.createNativeQuery(countQuery);
        countList = this.getSingleResult(query);

        if (countList != null) {
            count = (BigInteger) countList;
        }

        return count.intValue();
    }

    @Override
    public int getUnmappedCount(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws RecordNotFoundException {
        Object countList;
        FirmEntity firm = getMobileAndState(lcHeaderBO.getUserId(), lcHeaderBO.getFirmId());
       // List<JsonObject> firmSellers = getFirmSeller(lcHeaderBO);
        String id = getBranchSeller(lcHeaderBO);
        BigInteger count = BigInteger.ZERO;
        String sql = null;
        Query query = null;
        if(!helper.isEmpty(searchBO.getSearchTerm())){
            sql = "SELECT COUNT(*) FROM (" + getUnmappedSellerOnProduct(id) + ")AS DUMMY";
            query = entityManager.createNativeQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
            query.setParameter("pinCode",helper.getString(firm.getCPin()));
        }
        else {
            sql = "SELECT COUNT(*) FROM (" +getUnMappedSellerQuery(id)+ ")AS DUMMY";
            query = entityManager.createNativeQuery(sql);
            query.setParameter("state", helper.getContainLikeQueryString(firm.getCStateName()));
        }
        countList = this.getSingleResult(query);
        if (countList != null) {
            count = (BigInteger) countList;
        }
        return count.intValue();
    }

    private String getUnMappedSellerQuery(String sellerCode) {
        String sql = "SELECT    lccm.c_code, lccm.c_name, pm.c_district as cityName, " +
                "                lccm.c_area_code, " +
                "                pm.c_name as area_name," +
                "                pm.c_state, " +
                "                lccm.c_mobile " +
                "                FROM lc_c2code_mst lccm   " +
                "                JOIN pincode_mst pm on pm.c_code = lccm.c_pin " +
                "                and pm.c_c2code = 'C2INFO'  " +
                "                LEFT JOIN u_geo_area_mst ugam ON ugam.c_code = lccm.c_area_code ";
        if (!helper.isEmpty(sellerCode)) {
            sql += " where lccm.c_code not in (" + sellerCode + ") ";
        }
        sql += "  GROUP BY lccm.c_code   " +
                "  ORDER BY CASE WHEN pm.c_state LIKE :state " +
                "  THEN 0 ELSE 1 END, pm.c_state ASC";
        return sql;
    }

    private String getMappedSeller(String selleBuyerCode, String firmId) {
        return "    SELECT DUMMY.c_code, DUMMY.c_name, DUMMY.sellerPriority, DUMMY.buyerCode, sum(DUMMY.pending_credit), sum(DUMMY.no_prd_on_scheme) " +
                "   FROM (SELECT lcm.c_code ,lcm.c_name ,  " +
                "       lsbp.n_buyer_seller_priority AS sellerPriority,  " +
                "       act.c_code AS buyerCode," +
                "       (SELECT COALESCE(SUM(co.n_amount),0) FROM cust_outstanding co where co.c_c2_code = lcm.c_code and co.c_cust_code = act.c_code and co.n_amount < 0) as pending_credit," +
                "       (SELECT COALESCE(count(lo.c_scheme),0) from lo_view_seller_cat_item_scheme lo where act.c_c2code = lo.c_c2code and lo.c_cust_cat_code = 'RETAIL'" +
                "           AND lo.c_scheme IS NOT NULL or '') as no_prd_on_scheme" +
                "       FROM lc_c2code_mst lcm   " +
                "       LEFT JOIN cust_act_mst act ON act.c_c2code = lcm.c_code   " +
                "       LEFT JOIN lc_seller_buyer_priority lsbp ON lsbp.c_seller_code = act.c_c2code AND lsbp.c_buyer_code = act.c_code " +
                "       WHERE " +
                "       CONCAT(act.c_c2code,act.c_code) IN ("+selleBuyerCode+") " +
                "       AND lsbp.n_firm_id = '"+firmId+"'" +
                "       GROUP BY lcm.c_code, act.c_code " +
                "       ORDER BY COALESCE(lsbp.n_buyer_seller_priority, 9999) asc) DUMMY" +
                " GROUP BY DUMMY.c_code";
    }

    private String getMobileNumber(Long userId, Long firmId) {
        FirmEntity firm = firmRepo.getOne(firmId);
        return firm.getCMobileNo();
    }

    /*Search By name*/
    @Override
    public List<SellerDetailBO> unMappedSellersSearch(String searchString,LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException, InvalidRequestException {
        List<SellerDetailBO> list = new ArrayList<>();
        SellerDetailBO sellerDetailBO;
        List<Object[]> allSellers;

        String unMappedSql = unMappedSellerSearch(getBranchSeller(lcHeaderBO));
        Query allSellersQuery = entityManager.createNativeQuery(unMappedSql);
        allSellersQuery.setParameter("searchString", helper.getContainLikeQueryString(searchString));
        allSellers = this.getResultList(allSellersQuery, pageBO.getPage(), pageBO.getLimit());

        if (allSellers.isEmpty()) {
            return list;
            /*throw new RecordNotFoundException("No Records found");*/
        } else {
            for (Object[] objects : allSellers) {
                sellerDetailBO = new SellerDetailBO();
                sellerDetailBO.setSellerCode(helper.getString(objects[0]));
                sellerDetailBO.setSellerName(helper.getString(objects[1]));
                sellerDetailBO.setCityName(helper.getString(objects[2]));
                sellerDetailBO.setAreaCode(helper.getString(objects[3]));
                sellerDetailBO.setAreaName(helper.getString(objects[4]));
                sellerDetailBO.setStateName(helper.getString(objects[5]));
                list.add(sellerDetailBO);
            }
        }
        return list;
    }

    private String unMappedSellerSearch(String firmSeller) {
        return "  SELECT    lccm.c_code, lccm.c_name, pm.c_district " +
                "                as cityName, lccm.c_area_code, " +
                "                pm.c_name as area_name, pm.c_state, " +
                "                lccm.c_mobile  FROM lc_c2code_mst lccm     " +
                "                JOIN pincode_mst pm on pm.c_code = lccm.c_pin " +
                "                and pm.c_c2code = 'C2INFO'   " +
                "                LEFT JOIN u_geo_area_mst ugam ON ugam.c_code = lccm.c_area_code " +
                "                where lccm.c_code not in ("+firmSeller+")     " +
                "                AND concat(lccm.c_name,pm.c_district, pm.c_state) LIKE :searchString " +
                "                GROUP BY lccm.c_code ";
    }

    /*Search By City*/
    @Override
    public List<SellerDetailBO> unMappedSellersSearchByCity(String searchCity, String searchState, String searchArea, LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException, InvalidRequestException {
        List<SellerDetailBO> list = new ArrayList<>();
        SellerDetailBO sellerDetailBO;
        List<Object[]> allSellers;
        //String mobile = getMobileNumber(userId, firmId);
        if(helper.isEmpty(searchState) ){
            throw new InvalidRequestException("State", "Can't be Empty");
        }
        String unMappedSql = unMappedSellerSearchByCity(getBranchSeller(lcHeaderBO), searchCity, searchState, searchArea);
        Query allSellersQuery = entityManager.createNativeQuery(unMappedSql);

        setCriteria(searchCity, searchState, searchArea, allSellersQuery);

       // allSellersQuery.setParameter("mobile", mobile);
        /*allSellersQuery.setParameter("searchString",helper.getLikeQueryString(searchCity));*/
        allSellers = this.getResultList(allSellersQuery, pageBO.getPage(), pageBO.getLimit());

        if (allSellers.isEmpty()) {
            return list;
            /*throw new RecordNotFoundException("No Records found");*/
        } else {
            for (Object[] objects : allSellers) {
                sellerDetailBO = new SellerDetailBO();
                sellerDetailBO.setSellerCode(helper.getString(objects[0]));
                sellerDetailBO.setSellerName(helper.getString(objects[1]));
                sellerDetailBO.setCityName(helper.getString(objects[2]));
                sellerDetailBO.setAreaCode(helper.getString(objects[3]));
                sellerDetailBO.setAreaName(helper.getString(objects[4]));
                sellerDetailBO.setStateName(helper.getString(objects[5]));
                list.add(sellerDetailBO);
            }
        }
        return list;
    }

    private void setCriteria(String searchCity, String searchState, String searchArea, Query allSellersQuery) {
        if (searchCity != null && !searchCity.trim().isEmpty()) {
            allSellersQuery.setParameter("searchCity", helper.getContainLikeQueryString(searchCity));
        }

        if (searchArea != null && !searchArea.trim().isEmpty()) {
            allSellersQuery.setParameter("searchArea", helper.getContainLikeQueryString(searchArea));
        }

        if (searchState != null && !searchState.trim().isEmpty()) {
            allSellersQuery.setParameter("searchState", helper.getContainLikeQueryString(searchState));
        }
    }

    private String unMappedSellerSearchByCity(String firmSeller,String searchCity, String searchState, String searchArea) {
        String sql = " SELECT    lccm.c_code, lccm.c_name, pm.c_district " +
                "                               as cityName, lccm.c_area_code, " +
                "                           pm.c_name as area_name, pm.c_state, " +
                "                            lccm.c_mobile  FROM lc_c2code_mst lccm    " +
                "                               JOIN pincode_mst pm on pm.c_code = lccm.c_pin " +
                "                               and pm.c_c2code = 'C2INFO'    " +
                "                                LEFT JOIN u_geo_area_mst ugam ON ugam.c_code = lccm.c_area_code " +
                "                               where lccm.c_code not in ("+firmSeller+")     AND (";

        if (!searchCity.trim().isEmpty() && !searchState.trim().isEmpty() && !searchArea.trim().isEmpty()) {
            sql += " pm.c_district LIKE :searchCity AND pm.c_state LIKE :searchState AND pm.c_name LIKE :searchArea ";
        } else if (!searchCity.trim().isEmpty() && !searchState.trim().isEmpty()) {
            sql += " pm.c_district LIKE :searchCity AND pm.c_state LIKE :searchState ";
        } else if (!searchState.trim().isEmpty() && !searchArea.trim().isEmpty()) {
            sql += " pm.c_state LIKE :searchState AND pm.c_name LIKE :searchArea ";
        } else if (!searchCity.trim().isEmpty() && !searchArea.trim().isEmpty()) {
            sql += " pm.c_district LIKE :searchCity AND pm.c_name LIKE :searchArea ";
        } else if (!searchCity.trim().isEmpty()) {
            sql += " pm.c_district LIKE :searchCity";
        } else if (!searchState.trim().isEmpty()) {
            sql += " pm.c_state LIKE :searchState";
        } else if (!searchArea.trim().isEmpty()) {
            sql += " pm.c_name LIKE :searchArea";
        }
        sql += "   )GROUP BY lccm.c_code ";

        return sql;
    }


    @Override
    public List<JsonObject> mappedSellersSearch(JsonObject jsonObject, LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException, InvalidRequestException {
        List<JsonObject> list = new ArrayList<>();
        JsonObject sellerDetails;
        List<Object[]> mappedSellers;
     //   String mobile = getMobileNumber(userId, firmId);
        String searchString = jsonObject.get("c_name").getAsString();
        String sellerCode = jsonObject.has("c_seller_code") ? jsonObject.get("c_seller_code").getAsString() : "";
        String sellerBuyerCodes = sellerBuyerCombine(lcHeaderBO);
        if(helper.isEmpty(sellerBuyerCodes)){
            throw new RecordNotFoundException("Firm sellers not Found");
        }
        String mappedSql = mappedSellerSearch(sellerCode,sellerBuyerCodes);
        Query mappedQuery = entityManager.createNativeQuery(mappedSql);
        if (searchString != null && !searchString.trim().isEmpty() && searchString.length() > 3) {
            mappedQuery.setParameter("searchString", helper.getContainLikeQueryString(searchString));
        } else {
            throw new InvalidRequestException("", "Search key length should be greater than 3");
        }
        if (!helper.isEmpty(sellerCode)) {
            mappedQuery.setParameter("sellerCode", sellerCode);
        }
        mappedSellers = this.getResultList(mappedQuery,pageBO.getPage(), pageBO.getLimit());

        if (mappedSellers.isEmpty()) {
            return list;
            /*throw new RecordNotFoundException("No Record Found");*/
        } else {
            for (Object[] objects : mappedSellers) {
                sellerDetails = new JsonObject();
                sellerDetails.addProperty("c_seller_code", helper.getString(objects[0]));
                sellerDetails.addProperty("c_seller_name", helper.getString(objects[1]));
                sellerDetails.addProperty("c_priority", helper.getString(objects[2]));
                sellerDetails.addProperty("c_buyer_code", helper.getString(objects[3]));
                sellerDetails.addProperty("c_pending_credit", helper.getString(objects[4]));
                sellerDetails.addProperty("n_product_scheme", helper.getString(objects[5]));
                list.add(sellerDetails);
            }
        }
        return list;
    }

    @Override
    public int getUnmappedSearchCount(String searchCity, String searchState, String searchArea, LcHeaderBO lcHeaderBO) {
        int count = 0;
        List<Object[]> allSellers;
        //String mobile = getMobileNumber(userId, firmId);

        String unMappedSql = unMappedSellerSearchByCity(getBranchSeller(lcHeaderBO),searchCity, searchState, searchArea);

        Query allSellersQuery = entityManager.createNativeQuery(unMappedSql);

        setCriteria(searchCity, searchState, searchArea, allSellersQuery);

       // allSellersQuery.setParameter("mobile", mobile);
        allSellers = this.getResultList(allSellersQuery);

        if (!allSellers.isEmpty()) {
            count = allSellers.size();
        }
        return count;
    }

    @Override
    public int getMappedSearchCount(JsonObject jsonObject, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        int count = 0;
        List<Object[]> mappedSellers;
       // String mobile = getMobileNumber(lcHeaderBO.getUserId(), lcHeaderBO.getFirmId());
        String sellerCode = jsonObject.has("c_seller_code") ? jsonObject.get("c_seller_code").getAsString() : "";
        String sellerBuyerCodes = sellerBuyerCombine(lcHeaderBO);
        if(helper.isEmpty(sellerBuyerCodes)){
            throw new RecordNotFoundException("Firm sellers not Found");
        }
        String mappedSql = mappedSellerSearch(sellerCode,sellerBuyerCodes);
        Query mappedQuery = entityManager.createNativeQuery(mappedSql);
        mappedQuery.setParameter("searchString", jsonObject.get("c_name").getAsString());
        if (!helper.isEmpty(sellerCode)) {
            mappedQuery.setParameter("sellerCode", sellerCode);
        }
        mappedSellers = this.getResultList(mappedQuery);

        if (!mappedSellers.isEmpty()) {
            count = mappedSellers.size();
        }
        return count;
    }

    @Override
    public int getUnmappedCountByName(String searchString, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        int count = 0;
        List<Object[]> allSellers;
        //String mobile = getMobileNumber(lcHeaderBO.getUserId(), lcHeaderBO.getFirmId());
        String unMappedSql = unMappedSellerSearch(getBranchSeller(lcHeaderBO));

        Query allSellersQuery = entityManager.createNativeQuery(unMappedSql);

       // allSellersQuery.setParameter("mobile", mobile);
        allSellersQuery.setParameter("searchString", helper.getContainLikeQueryString(searchString));
        allSellers = this.getResultList(allSellersQuery);

        if (!allSellers.isEmpty()) {
            count = allSellers.size();
        }
        return count;
    }

    private String mappedSellerSearch(String sellerCode, String sellerBuyerCodes) {
        String sql = "    SELECT DUMMY.c_code, DUMMY.c_name, DUMMY.sellerPriority, DUMMY.buyerCode, sum(DUMMY.pending_credit), sum(DUMMY.no_prd_on_scheme) " +
                "   FROM (SELECT lcm.c_code ,lcm.c_name ,  " +
                "       lsbp.n_buyer_seller_priority AS sellerPriority,  " +
                "       act.c_code AS buyerCode," +
                "       (SELECT COALESCE(SUM(co.n_amount),0) FROM cust_outstanding co where co.c_c2_code = lcm.c_code and co.c_cust_code = act.c_code and co.n_amount < 0) as pending_credit," +
                "       (SELECT COALESCE(count(lo.c_scheme),0) from lo_view_seller_cat_item_scheme lo where act.c_c2code = lo.c_c2code and lo.c_cust_cat_code = 'RETAIL'" +
                "           AND lo.c_scheme IS NOT NULL or '') as no_prd_on_scheme" +
                "       FROM lc_c2code_mst lcm   " +
                "       LEFT JOIN cust_act_mst act ON act.c_c2code = lcm.c_code   " +
                "       LEFT JOIN lc_seller_buyer_priority lsbp ON lsbp.c_seller_code = act.c_c2code AND lsbp.c_buyer_code = act.c_code " +
                "       WHERE " +
                "       CONCAT(act.c_c2code,act.c_code) IN ("+sellerBuyerCodes+") " +
                "       AND lcm.c_name LIKE :searchString   ";
        if (!helper.isEmpty(sellerCode)) {
            sql+= "    AND lcm.c_code = :sellerCode ";
        }
        sql+=   "       GROUP BY lcm.c_code, act.c_code " +
                "       ORDER BY COALESCE(lsbp.n_buyer_seller_priority, 9999) asc) DUMMY" +
                " GROUP BY DUMMY.c_code";
        return sql;
    }

    @Override
    public JsonArray getCustcode(LcHeaderBO lcHeaderBO, PageBO pageBO) throws RecordNotFoundException{

        //String mobile = getMobileNumber(lcHeaderBO.getUserId(), lcHeaderBO.getFirmId());
        String id = sellerBuyerCombine(lcHeaderBO);
        if (helper.isEmpty(id) && id == null ) {
            throw new RecordNotFoundException("Firm seller not found");
        }
        /*List<String> codes = new ArrayList<>();*/
        JsonArray jsonArray = new JsonArray();
        Query custQuery = entityManager.createNativeQuery(getCustCode(id));
        List<Object[]>  mappedSellers = this.getResultList(custQuery);
        if (mappedSellers.isEmpty()) {
            return jsonArray;
            /*throw new RecordNotFoundException("No buyer code found");*/
        } else {
            for (Object[] objects: mappedSellers) {
                    JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("sellercode",helper.getString(objects[0]));
                jsonObject.addProperty("buyercode",helper.getString(objects[2]));
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    private String getCustCode(String sellerBuyerCodes){
        return "SELECT  lcm.c_code, lcm.c_name, act.c_code , act.c_name " +
                "From  lc_c2code_mst lcm " +
                "JOIN  cust_act_mst act ON act.c_c2code = lcm.c_code OR act.c_c2code = lcm.c_parent_act_code " +
                "LEFT JOIN  lc_supp_chem_comb lscc ON lcm.c_code = lscc.c_c2code and act.c_code = lscc.c_chem_code" +
                "WHERE  lscc.c_supp_chem IN ("+sellerBuyerCodes+") " +
                "ORDER BY lcm.c_name ASC ";

    }

    @Override
    public List<JsonObject> getFirmSeller(LcHeaderBO lcHeaderBO)  {
        List<JsonObject> list = new ArrayList<>();
        JsonObject json;
        List<FirmSellersEntity> firmSellersList = firmSellersRepo.getByFirmId(lcHeaderBO.getFirmId());
        if(firmSellersList.size() > 0) {
            for (FirmSellersEntity firmSellersEntity : firmSellersList) {
                json = new JsonObject();
                json.addProperty("c_seller_code", firmSellersEntity.getCSellerCode());
                json.addProperty("c_buyer_code", firmSellersEntity.getCBuyerCode());
                list.add(json);
            }
        }
        return list;
    }

    @Override
    public JsonObject getItemMappingInfo(String itemUcode) {
        JsonObject jObj = new JsonObject();
        String sql = getItemMappingDetails();
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("itemUcode",itemUcode);
        List<Object[]>  resultList = this.getResultList(query);
        if (resultList.isEmpty()) {
            return jObj;

        } else {
            for (Object[] objects : resultList) {
                jObj.addProperty("c_full_name", helper.getString(objects[0]));
                jObj.addProperty("c_csquare_mfac_code", helper.getString(objects[1]));
                jObj.addProperty("c_csquare_pack_code", helper.getString(objects[2]));
                jObj.addProperty("c_csquare_pack_type_code", helper.getString(objects[3]));
                jObj.addProperty("c_csquare_cat_code", helper.getString(objects[4]));
                jObj.addProperty("c_csquare_cont_code", helper.getString(objects[5]));
                jObj.addProperty("c_csquare_mfac_name", helper.getString(objects[6]));
                jObj.addProperty("c_csquare_pack_name", helper.getString(objects[7]));
                jObj.addProperty("c_csquare_pack_type_name", helper.getString(objects[8]));
                jObj.addProperty("c_csquare_cat_name", helper.getString(objects[9]));
                jObj.addProperty("c_csquare_cont_name", helper.getString(objects[10]));
                jObj.addProperty("c_schedule_name",helper.getString(objects[11]));
                jObj.addProperty("c_hsn_code",helper.getString(objects[12]));
            }
        }
        return jObj;
    }

    private String getItemMappingDetails() {
        return "SELECT distinct  " +
                "order_buk_new.u_item_mst.c_full_name as c_full_name,  " +
                "order_buk_new.u_item_mst.c_item_mfac_code as c_csquare_mfac_code,  " +
                "order_buk_new.u_item_mst.c_item_pack_code as c_csquare_pack_code,  " +
                "order_buk_new.u_item_mst.c_pack_type_code as c_csquare_pack_type_code,  " +
                "order_buk_new.u_item_mst.c_item_cat_code as c_csquare_cat_code,  " +
                "order_buk_new.u_item_mst.c_item_cont_code as c_csquare_cont_code,  " +
                "order_buk_new.u_item_mfac_mst.c_name as c_csquare_mfac_name,  " +
                "order_buk_new.u_item_pack_mst.c_name as c_csquare_pack_name,  " +
                "order_buk_new.u_item_pack_type_mst.c_name as c_csquare_pack_type_name,  " +
                "order_buk_new.u_item_cat_mst.c_name as c_csquare_cat_name,  " +
                "order_buk_new.u_item_cont_mst.c_name as c_csquare_cont_name,  " +
                "order_buk_new.u_item_sch_mst.c_name as c_schedule_name,  " +
                "order_buk_new.u_item_mst.c_hsn_code as c_hsn_code  " +
                "FROM order_buk_new.u_stockiest_item  " +
                "left join order_buk_new.u_item_mst on order_buk_new.u_stockiest_item.c_ucode = order_buk_new.u_item_mst.c_code  " +
                "join order_buk_new.u_item_mfac_mst on order_buk_new.u_item_mst.c_item_mfac_code = order_buk_new.u_item_mfac_mst.c_code  " +
                "join order_buk_new.u_item_pack_mst on order_buk_new.u_item_mst.c_item_pack_code = order_buk_new.u_item_pack_mst.c_code  " +
                "join order_buk_new.u_item_pack_type_mst on order_buk_new.u_item_mst.c_pack_type_code = order_buk_new.u_item_pack_type_mst.c_code  " +
                "join order_buk_new.u_item_cat_mst on order_buk_new.u_item_mst.c_item_cat_code = order_buk_new.u_item_cat_mst.c_code  " +
                "join order_buk_new.u_item_cont_mst on order_buk_new.u_item_mst.c_item_cont_code = order_buk_new.u_item_cont_mst.c_code  " +
                "join order_buk_new.u_item_sch_mst on order_buk_new.u_item_sch_mst.c_code = order_buk_new.u_item_cont_mst.c_item_schedule_code "+
                "where " +
                "order_buk_new.u_stockiest_item.c_ucode = :itemUcode ";
    }

    private String getSellerIn(LcHeaderBO lcHeaderBO) {

        List<JsonObject> firmSellers = getFirmSeller(lcHeaderBO);
        String id = "";
        if (firmSellers.size()>0){
            for (int i = 0; i < firmSellers.size(); i++) {
                id = id + "'" + firmSellers.get(i).get("c_seller_code").getAsString() + "',";
            }
            id = id.substring(0, id.length() - 1);
        }
        return id;
    }

    @Override
    public String getBranchSeller(LcHeaderBO lcHeaderBO){

        List<FirmSellersEntity> firmSellersList = firmSellersRepo.getByFirmId(lcHeaderBO.getFirmId());
        String id = "";
        if(firmSellersList.size() > 0) {
            for (FirmSellersEntity firmSellersEntity : firmSellersList) {
                id = id + "'" + firmSellersEntity.getCSellerCode() + "',";
            }
            id = id.substring(0, id.length() - 1);
        }
        return id;
    }
    @Override
    public String sellerBuyerCombine(LcHeaderBO lcHeaderBO){

        List<JsonObject> firmSellers = getFirmSeller(lcHeaderBO);
        String c2Code = "";
        String sellerBuyerCode = "";
        if (firmSellers.size() > 0) {
            for (int i = 0; i < firmSellers.size(); i++) {
                JsonObject jsonObject = firmSellers.get(i).getAsJsonObject();
                c2Code = c2Code + "'" + jsonObject.get("c_seller_code").getAsString() + "'";
                sellerBuyerCode = sellerBuyerCode + "'" + jsonObject.get("c_seller_code").getAsString() + jsonObject.get("c_buyer_code").getAsString() + "'";
                if (i != firmSellers.size() - 1) {
                    c2Code = c2Code + ",";
                    sellerBuyerCode = sellerBuyerCode + ",";
                }
            }
        }
        return sellerBuyerCode;
    }

/*    @Override
    public JsonArray getSellerNewLaunched(List<JsonObject> firmSeller, Long sellerNewLaunchDays) {
        JsonArray jsonArray = new JsonArray();
        JsonObject newLaunchItems;
        LocalDate toDate = LocalDate.now().atTime(LocalTime.MAX);
        LocalDateTime fromDate = toDate.minusDays(sellerNewLaunchDays);
        System.out.println(firmSeller);
        for (int i = 0; i < firmSeller.size(); i++) {
            JsonObject jsonObject = firmSeller.get(i).getAsJsonObject();
            String sql = sellerNewLaunchedItems(jsonObject.get("c_seller_code").getAsString(), jsonObject.get("c_buyer_code").getAsString(), toDate, fromDate);
            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> resultList = this.getResultList(query);
            for (Object[] objects : resultList) {
                newLaunchItems = new JsonObject();
                newLaunchItems.addProperty("c_seller_code", helper.getString(objects[0]));
                newLaunchItems.addProperty("c_buyer_code", helper.getString(objects[1]));
                newLaunchItems.addProperty("c_mfac_name", helper.getString(objects[2]));
                newLaunchItems.addProperty("c_item_code", helper.getString(objects[3]));
                newLaunchItems.addProperty("c_item_name", helper.getString(objects[4]));
                newLaunchItems.addProperty("n_mrp",Double.parseDouble(helper.getString(objects[5])));
                newLaunchItems.addProperty("n_rate", Double.parseDouble(helper.getString(objects[6])));
                newLaunchItems.addProperty("itemPurCount",BigInteger.valueOf(helper.getLong(helper.getString(objects[7]))));
                newLaunchItems.addProperty("stock", BigInteger.valueOf(helper.getLong(helper.getString(objects[8]))));
                newLaunchItems.addProperty("flag", BigInteger.valueOf(helper.getLong(helper.getString(objects[9]))));
                newLaunchItems.addProperty("qty", BigInteger.valueOf(helper.getLong(helper.getString(objects[10]))));
                newLaunchItems.addProperty("contentName", helper.getString(objects[11]));
                jsonArray.add(newLaunchItems);
            }
            jsonObject.get("n_firm_id").getAsLong();
            jsonObject.get("n_user_id").getAsString();
        }
        return jsonArray;
    }*/

    @Override
    public void getFirmSellerBuyer(int count, Long sellerNewLaunchDays) throws RecordNotFoundException {

        LocalDate toDate = helper.getCurrentDate();
        LocalDate fromDate = toDate.minusDays(sellerNewLaunchDays);

        for (int i = 0; ; i++) {
            Pageable pageable = PageRequest.of(i, count);
            log.debug(i + "___________" + count);
            JsonObject jsonObject;
            List<FirmSellersEntity> firmSellers = firmSellersRepo.getFirmSellers(pageable);
            if (firmSellers.size() == 0)
                break;
            for (FirmSellersEntity firmEntity : firmSellers) {
                jsonObject = new JsonObject();
                jsonObject.addProperty("n_firm_id", firmEntity.getNFirmId());
                jsonObject.addProperty("c_seller_code", firmEntity.getCSellerCode());
                jsonObject.addProperty("c_buyer_code", firmEntity.getCBuyerCode());
                jsonObject.addProperty("n_user_id", firmEntity.getNCreatedBy());

                if (!helper.isEmpty(jsonObject)) {
                    sendNewLaunchNotification(firmEntity, jsonObject,toDate,fromDate);
                }
            }
        }
    }

    public void sendNewLaunchNotification(FirmSellersEntity firmEntity, JsonObject jsonObject, LocalDate toDate, LocalDate fromDate) {
        try {
            log.debug("SendNewLaunchNotification method initiated");
            String sql = sellerNewLaunchedItems(firmEntity.getCSellerCode(), firmEntity.getCBuyerCode(), toDate, fromDate);
            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> resultList = this.getResultList(query);
            if (resultList.size() != 0) {
                for (Object[] objects : resultList) {
                    NewLaunchNotificationEntity newEntity = newLaunchNotificationRepo.findRecord(helper.getString(objects[2]),
                            helper.getString(objects[0]), firmEntity.getNCreatedBy(), helper.getString(objects[4]), "NEW");

                    if (newEntity == null && helper.isEmpty((Object) null)) {

                        NewLaunchNotificationEntity entity = new NewLaunchNotificationEntity();
                        entity.setCBuyerCode(helper.getString(objects[2]));
                        entity.setCSellerCode(helper.getString(objects[0]));
                        entity.setCSellerName(helper.getString(objects[1]));
                        entity.setCType("NEW");
                        entity.setCItemCode(helper.getString(objects[4]));
                        entity.setNUserId(firmEntity.getNCreatedBy());
                        entity.setADate(helper.getCurrentDate());
                        newLaunchNotificationRepo.save(entity);
                        //notification
                        JsonObject data = new JsonObject();
                        JsonArray jArray = new JsonArray();
                        JsonObject jObject = new JsonObject();
                        jObject.addProperty("c_product_key", "LO");
                        jObject.addProperty("c_customer_id", String.valueOf(entity.getNUserId()));
                        jObject.addProperty("c_category", "ALERT");
                        jObject.addProperty("c_topic", "New Launch");
                        jObject.addProperty("c_action_id", entity.getCItemCode());
                        jObject.addProperty("c_message", "Hurray !!! " + entity.getCSellerName() + " is glad to share Newly Launched product. " +
                                " " + System.getProperty("line.separator") + " " + helper.getString(objects[3]) + " " + System.getProperty("line.separator") + " " + helper.getString(objects[5]) + " [Mrp: " + Double.parseDouble(helper.getString(objects[6])) + " , Contents: " + helper.getString(objects[10]) + "]");
                        jObject.addProperty("c_title", "Newly Launched");
                        jObject.addProperty("c_from", "WebApp");
                        jObject.addProperty("c_options", "Accept");
                        jArray.add(jObject);
                        data.add("data", jArray);
                        try {
                            String response = this.callWebClientPostSyncApi(notificationUrl, data.toString());
                        } catch (Exception e) {
                            log.debug("Error while sending notification to " + entity.getNUserId() + " userId ");
                            //remove record from DB
                            newLaunchNotificationRepo.delete(entity);
                        }
                    }
                }
            } else
                log.debug("New launch item not found for this seller_buyer combo" + firmEntity.getCSellerCode() + ":" + firmEntity.getCBuyerCode() + "");
        } catch (Exception e) {
            log.debug("Error while fetching firm_sellers for this firmId " + firmEntity.getNFirmId() + " and seller " + firmEntity.getCSellerCode() + " ");
        }
    }

    private String sellerNewLaunchedItems(String sellerCode, String buyerCode, LocalDate toDate, LocalDate fromDate) {
        return "SELECT  " +
                "                    lcm.c_code sellerCode, " +
                "                    lcm.c_name sellerName," +
                "                    cam.c_code buyerCode, " +
                "                    replace(replace(replace(replace(cmm.c_name, '$', ''), '#', ''), '*', ''), '~', '') as mfacName, " +
                "                    cim.c_code ItemCode, " +
                "                    replace(replace(replace(replace(cim.c_name, '$', ''), '#', ''), '*', ''), '~', '') as itemName, " +
                "                    COALESCE(rec.n_mrp_value,cb.n_rate,'0') mrp, " +
                "                    COALESCE(rec.n_sale_value,cb.n_sale_rate,'0') rate, " +
                "                    COALESCE(SUM(rec.n_count), 0) itemPurCount, " +
                "                    COALESCE(cb.n_bal_qty,'0') stock, " +
                "                    COALESCE(ccm.c_name, '') as contentName " +
                "                FROM " +
                "                    lc_c2code_mst lcm " +
                "                        LEFT JOIN " +
                "                    cust_act_mst cam ON cam.c_c2code = lcm.c_code " +
                "                        JOIN " +
                "                    cust_item_mst cim ON cim.c_c2code = lcm.c_code and left(cim.c_name, 1) not in ('.', '$', '#', '*') " +
                "                            and left(cim.c_name, 3) <> 'ZZZ' " +
                "                        LEFT JOIN " +
                "                    lc_cust_item_recommendation rec ON rec.c_c2code = lcm.c_code " +
                "                        AND rec.c_cust_code = cam.c_code " +
                "                        AND rec.c_item_code = cim.c_code " +
                "                        LEFT JOIN " +
                "                    cust_branch_item_stock cb ON lcm.c_code = cb.c_c2code " +
                "                        AND cb.c_item_code = cim.c_code " +
                "                        AND lcm.c_cust_branch_code = cb.c_br_code " +
                "                        JOIN  " +
                "                    cust_mfac_mst cmm on cim.c_c2code = cmm.c_c2code  " +
                "                      AND cim.c_mfac_code = cmm.c_code " +
                "                    left join cust_cont_mst ccm  " +
                "                        on cim.c_c2code = ccm.c_c2code and cim.c_cont_code = ccm.c_code " +
                "                                and ccm.c_code not in ('UNSURE', '-') AND ccm.c_name not in ('UNSURE', '-') " +
                "                WHERE " +
                "                    cim.c_c2code = '"+sellerCode+"' " +
                "                        AND cam.c_code = '"+buyerCode+"' " +
                "                        AND cim.d_adate >= '"+fromDate+"' " +
                "                        AND cim.d_adate <= '"+toDate+"' " +
                "                GROUP BY cim.c_code " +
                "                ORDER BY cmm.c_name, itemPurCount ";
    }

    @Override
    public JsonArray getLostOrder(String seller, String itemCode) {

        LocalDate tDate = helper.getCurrentDate();
        LocalDate fDate = tDate.minusDays(lostDays);

        String sql = "SELECT  clom.c_c2code, clom.c_cust_code, clom.c_item_code," +
                " uim.c_name, clom.n_seq FROM cust_lost_order_master clom " +
                "LEFT JOIN u_item_mst uim on uim.c_code = clom.c_item_code "+
                " WHERE  " +
                " clom.c_item_code = '"+itemCode+"' " +
                "AND clom.c_c2code = '"+seller+"' AND clom.d_date  " +
                "BETWEEN '"+fDate+"' AND '"+tDate+"' " +
                "AND clom.d_notification_date IS NULL " +
                "ORDER BY clom.d_date ASC";

        List<Object[]> resultList = this.getResultList(entityManager.createNativeQuery(sql));
        JsonArray jsonArray = new JsonArray();
        if (resultList.size()>0){

            for (Object[] obj: resultList){
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("c_seller_code",helper.getString(obj[0]));
                jsonObject.addProperty("c_buyer_code", helper.getString(obj[1]));
                jsonObject.addProperty("c_item_name", helper.getString(obj[3]));
                jsonObject.addProperty("n_seq", helper.getString(obj[4]));
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }


    @Override
    public List<FirmSellersEntity> getBySellerBuyer(String seller, String buyer) {
        return firmSellersRepo.getBySellerBuyer(seller, buyer);
    }

    @Override
    public void sendStockNotification(String itemName, String userId, String itemCode) {

        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("c_product_key","LO");
        jsonObject.addProperty("c_customer_id", userId);
        jsonObject.addProperty("n_category_id", 33);
        jsonObject.addProperty("c_topic", "Back in stock");
        jsonObject.addProperty("c_action_id",itemCode);
        jsonObject.addProperty("c_message","Hey !!! Dear Buyer your enquiry for "+itemName+
                " is back in stock & we are ready to serve you.");
        jsonObject.addProperty("c_title","Back in stock");
        jsonObject.addProperty("c_options","Accept");
        jsonObject.addProperty("c_from","WebApp");
        jsonArray.add(jsonObject);
        data.add("data",jsonArray);
        String response = this.callWebClientPostSyncApi(notificationUrl,data.toString());
    }

    @Override
    public String getSellerByItemCode(String sellerItem, String uItem) {
        String sql = "select usi.c_stockiest_code from u_stockiest_item usi where " +
                "usi.c_stockiest_item_code = '"+sellerItem+"'  " +
                "and usi.c_ucode = '"+uItem+"'";

        return this.getSingleResultNull(entityManager.createNativeQuery(sql));

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateLostDate(String nSeq, LocalDate date) {

        String sql = "UPDATE cust_lost_order_master SET d_notification_date = "+date+" WHERE n_seq = "+nSeq ;
        entityManager.createNativeQuery(sql).executeUpdate();

    }

}
