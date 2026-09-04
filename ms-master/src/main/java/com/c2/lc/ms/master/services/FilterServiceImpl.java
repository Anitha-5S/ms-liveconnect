package com.c2.lc.ms.master.services;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.ItemsSearchBO;
import com.c2.lc.ms.master.entities.mongo.LcItem;
import com.c2.lc.ms.master.repos.mysql.StatewiseFastmovingRepository;
import com.c2.lc.ms.master.repos.mysql.UStockiestItemRepository;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.FilterService;
import com.c2.lc.ms.master.services.interfaces.ShortBookWatchListService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FilterServiceImpl extends MasterBaseServiceImpl implements FilterService {

    @Value("${ms.cust.seller.service.api.url}")
    private String sellerUrl;
    @Autowired
    UStockiestItemRepository uStockiestItemRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SearchServiceImpl searchService;
    @Autowired
    private ShortBookWatchListService shortBookWatchListService;
    @Autowired
    private CatalogueServiceImpl catalogueService;
    @Autowired
    private StatewiseFastmovingRepository statewiseFastmovingRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public String getSellerCodeAndBuyerCodeQuery(LcHeaderBO lcHeaderBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        String c2Code = "";
        String buyerCode = "";
        String sql = "";
        Map<String, String> headers = new HashMap<>();
        headers.put("x-csquare-c2-code", helper.toString(lcHeaderBO.getFirmId()));
        headers.put("x-csquare-br-code", helper.toString(lcHeaderBO.getFirmId()));
        headers.put("x-csquare-firm-id",helper.toString(lcHeaderBO.getFirmId()));
        headers.put("x-csquare-terminal-id", helper.toString(lcHeaderBO.getUserId()));
        JsonArray jsonArray1 = searchService.callCustomerService(headers, sellerUrl);
        if (jsonArray1.size() > 0) {
            for (int i = 0; i < jsonArray1.size(); i++) {
                JsonObject jsonObject = jsonArray1.get(i).getAsJsonObject();
                c2Code = jsonObject.get("c_seller_code").getAsString();
                buyerCode = jsonObject.get("c_buyer_code").getAsString();
                sql += " select convert('" + c2Code + "' USING latin1) as seller_code, convert('" + buyerCode + "' USING latin1) as cust_code ";
                if (i != jsonArray1.size() - 1) {
                    sql += " union ";
                }
            }
        } else {
            sql += " select convert( NULL USING latin1) as seller_code, convert(NULL USING latin1) as cust_code ";
        }
        //System.out.println(sql);
        return sql;
    }

    @Override
    public JsonArray newLaunchFilter(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        String sql = getNewLaunchFilterQuery(lcHeader,searchBO);
        Query query = this.getQuery(sql);
        setCriteria(searchBO.getManufacturers(), searchBO.getBrands(), searchBO.getSellers(), query);
        JsonArray arr = new JsonArray();
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return arr;
            //throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }

        for (Object[] objects : resultList) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray1 = new JsonArray();
            JsonObject jobj = new JsonObject();
            String itemCode = helper.getString(objects[++i]);
            jsonObject.addProperty("c_item_code", itemCode);
            jsonObject.addProperty("c_item_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_pack_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_contains", helper.getString(objects[++i]));
            jsonObject.addProperty("n_mrp", Double.parseDouble(objects[++i].toString()));
            jsonObject.addProperty("c_scheme",helper.getString(objects[++i]));
            jobj.addProperty("c_thumbnail_image", helper.getString(objects[++i]));
            jsonArray1.add(jobj);
            jsonObject.add("ac_thumbnail_images", jsonArray1);
            jsonObject.addProperty("c_pack_type_name",helper.getString(objects[++i]));
            jsonObject.addProperty("c_discount_status", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_watchlist_status", Constants.STATUS_NO);
            jsonObject.addProperty("c_short_book_status", Constants.STATUS_NO);
//            jsonObject.addProperty("c_watchlist_status", shortBookWatchListService.isWatchList(lcHeader.getUserId(),
//                    lcHeader.getFirmId(), lcHeader.getBranchId(), itemCode));
//            jsonObject.addProperty("c_short_book_status", shortBookWatchListService.isShortBook(lcHeader.getUserId(),
//                    lcHeader.getFirmId(), lcHeader.getBranchId(), itemCode));
            arr.add(jsonObject);
        }
        return arr;
    }

    private String getNewLaunchFilterQuery(LcHeaderBO header,ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        String sql = "select distinct uim.c_code itemCode, uim.c_name itemName, uipm.c_name packName, uicm.c_name contentName," +
                " uim.n_last_mrp Mrp,  sch.c_scheme as scheme, uim.c_web_img_link acThumbnailImage," +
                " uiptm.c_name as packTypeName "+
                " from u_item_mst uim" +
                " join u_item_pack_mst uipm on uim.c_item_pack_code = uipm.c_code" +
                " join u_item_cont_mst uicm on uim.c_item_cont_code = uicm.c_code" ;
        if (!searchBO.getManufacturers().isEmpty()) {
            sql += " join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code"+
                    " and uimm.c_code in :searchMfc ";
        }
            sql += " join u_item_pack_type_mst uiptm on uim.c_pack_type_code = uiptm.c_code "+
                " left join u_fastmoving_items fm on uim.c_code = fm.c_ucode  " ;
        if (!searchBO.getBrands().isEmpty()) {
            sql += " join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code " +
                    " and uibm.c_code in :searchBrand ";
        }
        if (!searchBO.getSellers().isEmpty()) {
            sql += " join " +
                    " (select usi.c_ucode, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, coalesce(sch.n_sch_ratio, 0) as n_sch_ratio, coalesce(sch.n_sch_order, 9999999) as n_sch_order, sch.c_scheme, " +
                    "  row_number() over(partition by usi.c_ucode order by sch.n_sch_ratio desc, coalesce(sch.n_sch_order, 9999999) asc) as rowid " +
                    "  from u_stockiest_item usi " +
                    "  left join (" + getSellerCodeAndBuyerCodeQuery(header) +") t on t.seller_code = usi.c_stockiest_code " +
                    "  left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code " +
                    "  left join cust_category_mst ccm on ccm.c_c2code = ac.c_c2code and ccm.c_code = ac.c_cust_category_code " +
                    "  join lc_c2code_mst lccm on lccm.c_code = usi.c_stockiest_code " +
                    "  and lccm.c_code in :searchSeller ";
        }
        else {
            sql += " left join " +
                    " (select usi.c_ucode, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, coalesce(sch.n_sch_ratio, 0) as n_sch_ratio, coalesce(sch.n_sch_order, 9999999) as n_sch_order, sch.c_scheme, " +
                    " row_number() over(partition by usi.c_ucode order by sch.n_sch_ratio desc, coalesce(sch.n_sch_order, 9999999) asc) as rowid " +
                    " from u_stockiest_item usi " +
                    " join (" + getSellerCodeAndBuyerCodeQuery(header) + ") t on t.seller_code = usi.c_stockiest_code " +
                    " left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code " +
                    " left join cust_category_mst ccm on ccm.c_c2code = ac.c_c2code and ccm.c_code = ac.c_cust_category_code " +
                    " join lc_c2code_mst lccm on lccm.c_code = usi.c_stockiest_code  ";
        }
        sql +=  " left join cust_branch_item_stock cbis on cbis.c_c2code = usi.c_stockiest_code and cbis.c_item_code = usi.c_stockiest_item_code and cbis.c_br_code = lccm.c_cust_branch_code   " +
                " left join lo_view_seller_cat_item_scheme sch   " +
                " on sch.c_c2code = usi.c_stockiest_code  " +
                " and sch.c_cust_cat_code = if(trim(coalesce(ccm.c_sch_category_code, '')) = '', coalesce(ccm.c_code, '-'), ccm.c_sch_category_code)  " +
                " and sch.c_item_code = usi.c_stockiest_item_code ) sch on sch.c_ucode = uim.c_code and sch.rowid = 1 "+
                " where uim.d_adate >=DATE_ADD(NOW(),INTERVAL -90 DAY) " +
                " and uim.n_last_mrp > 0 and uim.n_active > 0 " +
                " and uim.c_gst_code is not null ";
        if (searchBO.getAvailability().equals(Constants.STATUS_YES)) {
            sql += " and coalesce(sch.n_bal_qty, 0) > 0 ";
        }
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_NEWEST)) {
            sql += " ORDER BY uim.d_adate desc";
        } else if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_RELEVANCE)) {
            sql += " ORDER BY coalesce(fm.n_count, 0) DESC";
        }
        if(searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_SCHEME))
        {
            sql += " and sch.c_scheme is not null "+
                    " order by" +
                    " coalesce(sch.n_sch_ratio, 0) desc, coalesce(sch.n_sch_order, 999999), " +
                    " coalesce(fm.n_count, 0) desc ";
        }
        return sql;
    }

    @Override
    public long newLaunchCount(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        BigInteger count = BigInteger.ZERO;
        String sql = getNewLaunchFilterQuery(lcHeader,searchBO);
        Query query = this.getQuery("SELECT COUNT(*) FROM (" + sql + ") DUMMY");
        setCriteria(searchBO.getManufacturers(), searchBO.getBrands(), searchBO.getSellers(), query);

        Object result = this.getSingleResult(query);
        if (result != null) {
            count = (BigInteger) result;
        }
        return count.intValue();
    }


    @Override
    public JsonArray newLaunchSellers(SearchBO searchBO) throws RecordNotFoundException {

        String sql;
        Query query;
        JsonArray jsonArray = new JsonArray();
        if (!helper.isEmpty(searchBO.getSearchTerm())) {
            sql = newLaunchSearchSellers();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        } else {
            sql = newLaunchSellers();
            query = this.getQuery(sql);
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
            //throw new RecordNotFoundException("'seller'", MsMessages.RECORD_NOT_FOUND);
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_seller_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_seller_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    @Override
    public long newLaunchSellersCount(SearchBO searchBO) {
        String sql;
        Query query;
        if (!helper.isEmpty(searchBO.getSearchTerm())) {
            sql = newLaunchSearchSellersCount();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        } else {
            sql = newLaunchSellersCount();
            query = this.getQuery(sql);
        }
        Object result = this.getSingleResult(query);
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray newLaunchMfc(SearchBO searchBO) throws RecordNotFoundException {
        String sql;
        Query query;
        JsonArray jsonArray = new JsonArray();
        if (!helper.isEmpty(searchBO.getSearchTerm())) {
            sql = newLaunchSearchMfc();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        } else {
            sql = newLaunchMfc();
            query = this.getQuery(sql);
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
           // throw new RecordNotFoundException("'manufacture'", MsMessages.RECORD_NOT_FOUND);
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_mfc_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_mfc_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    @Override
    public long newLaunchMfcCount(SearchBO searchBO) {
        String sql;
        Query query;
        if (!helper.isEmpty(searchBO.getSearchTerm())) {
            sql = newLaunchSearchMfcCount();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        } else {
            sql = newLaunchMfcCount();
            query = this.getQuery(sql);
        }
        Object result = this.getSingleResult(query);
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray newLaunchBrand(SearchBO searchBO) throws RecordNotFoundException {
        String sql;
        Query query;
        JsonArray jsonArray = new JsonArray();
        if (!helper.isEmpty(searchBO.getSearchTerm())) {
            sql = newLaunchSearchBrand();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        } else {
            sql = newLaunchBrand();
            query = this.getQuery(sql);
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
            //throw new RecordNotFoundException("'brand'", MsMessages.RECORD_NOT_FOUND);
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_brand_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_brand_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    @Override
    public long newLaunchBrandCount(SearchBO searchBO) {
        String sql;
        Query query;
        if (!helper.isEmpty(searchBO.getSearchTerm())) {
            sql = newLaunchSearchBrandCount();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        } else {
            sql = newLaunchBrandCount();
            query = this.getQuery(sql);
        }
        Object result = this.getSingleResult(query);
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray preferredSellerMfc(SearchBO searchBO) throws RecordNotFoundException {
        String sql;
        Query query;
        JsonArray jsonArray = new JsonArray();
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = preferredSellerSearchMfc();
            query = this.getQuery(sql);
            query.setParameter("insearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());
        } else {
            sql = preferredSellerMfc();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
        }
        query.setParameter("search", searchBO.getSearchTerm());
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
            //throw new RecordNotFoundException("'Manufacture'", MsMessages.RECORD_NOT_FOUND);
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_mfc_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_mfc_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    @Override
    public long preferredSellerMfcCount(SearchBO searchBO) {
        String sql;
        Query query;
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = preferredSellerMfcSearchCount();
            query = this.getQuery(sql);
            query.setParameter("insearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());

        } else {
            sql = preferredSellerMfcCount();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
        }
        Object result = this.getSingleResult(query);
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray preferredSellerBrand(SearchBO searchBO) throws RecordNotFoundException {
        String sql;
        Query query;
        JsonArray jsonArray = new JsonArray();
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = preferredSellerSearchBrand();
            query = this.getQuery(sql);
            query.setParameter("insearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());
        } else {
            sql = preferredSellerBrand();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
            //throw new RecordNotFoundException("'Brand'", MsMessages.RECORD_NOT_FOUND);
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_brand_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_brand_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    @Override
    public long preferredSellerBrandCount(SearchBO searchBO) {
        String sql;
        Query query;
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = preferredSellerBrandSearchCount();
            query = this.getQuery(sql);
            query.setParameter("insearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());

        } else {
            sql = preferredSellerBrandCount();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
        }
        Object result = this.getSingleResult(query);
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray shopByMfcFilter(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {

        JsonArray arr = new JsonArray();

        String sql = getShopByMfcFilterQuery(header,searchBO);
        Query query = this.getQuery(sql);
        query.setParameter("search", searchBO.getSearchTerm());
        setShopByMfcCriteria(searchBO.getBrands(), searchBO.getSellers(), query);

        //itemCodes = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return arr;
            //throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }

        for (Object[] objects : resultList) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray1 = new JsonArray();
            JsonObject jobj = new JsonObject();
            String itemCode = helper.getString(objects[++i]);
            jsonObject.addProperty("c_item_code", itemCode);
            jsonObject.addProperty("c_item_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_pack_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_contains", helper.getString(objects[++i]));
            jsonObject.addProperty("n_mrp", Double.parseDouble(objects[++i].toString()));
            jsonObject.addProperty("c_scheme",helper.getString(objects[++i]));
            jobj.addProperty("c_thumbnail_image", helper.getString(objects[++i]));
            jsonArray1.add(jobj);
            jsonObject.add("ac_thumbnail_images", jsonArray1);
            jsonObject.addProperty("c_pack_type_name",helper.getString(objects[++i]));
            jsonObject.addProperty("c_discount_status", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_watchlist_status", Constants.STATUS_NO);
            jsonObject.addProperty("c_short_book_status", Constants.STATUS_NO);
//            jsonObject.addProperty("c_watchlist_status", shortBookWatchListService.isWatchList(lcHeader.getUserId(),
//                    lcHeader.getFirmId(), lcHeader.getBranchId(), itemCode));
//            jsonObject.addProperty("c_short_book_status", shortBookWatchListService.isShortBook(lcHeader.getUserId(),
//                    lcHeader.getFirmId(), lcHeader.getBranchId(), itemCode));
            arr.add(jsonObject);
        }
        return arr;

    }

    private void setShopByMfcCriteria(List<String> brandList, List<String> sellerList, Query query) {
        if (!brandList.isEmpty()) {
            query.setParameter("searchBrand", brandList);
        }
        if (!sellerList.isEmpty()) {
            query.setParameter("searchSeller", sellerList);
        }
    }

    private String getShopByMfcFilterQuery(LcHeaderBO header,ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        String sql = " SELECT distinct uim.c_code, " +
                " uim.c_name itemName, uipm.c_name packName, " +
                " uicm.c_name contentName, " +
                " uim.n_last_mrp Mrp,  " +
                " sch.c_scheme as scheme, " +
                " uim.c_web_img_link acThumbnailImage, " +
                " uiptm.c_name as packTypeName "+
                " FROM u_item_mfac_mst uimm  " +
                " JOIN u_item_mst uim ON uim.c_item_mfac_code = uimm.c_code  " +
                " join u_item_pack_mst uipm on uim.c_item_pack_code = uipm.c_code  " +
                " join u_item_cont_mst uicm on uim.c_item_cont_code = uicm.c_code " +
                " join u_item_pack_type_mst uiptm on uim.c_pack_type_code = uiptm.c_code "+
                " join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code"+
                " left join u_fastmoving_items fm on uim.c_code = fm.c_ucode  " ;

        if (!searchBO.getBrands().isEmpty()) {
            sql += " AND uibm.c_code IN :searchBrand ";
        }
        if (!searchBO.getSellers().isEmpty()) {
            sql += " join " +
                    " (select usi.c_ucode, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, coalesce(sch.n_sch_ratio, 0) as n_sch_ratio, coalesce(sch.n_sch_order, 9999999) as n_sch_order, sch.c_scheme, " +
                    "  row_number() over(partition by usi.c_ucode order by sch.n_sch_ratio desc, coalesce(sch.n_sch_order, 9999999) asc) as rowid " +
                    "  from u_stockiest_item usi " +
                    "  left join (" + getSellerCodeAndBuyerCodeQuery(header) +") t on t.seller_code = usi.c_stockiest_code " +
                    "  left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code " +
                    "  left join cust_category_mst ccm on ccm.c_c2code = ac.c_c2code and ccm.c_code = ac.c_cust_category_code " +
                    "  join lc_c2code_mst lccm on lccm.c_code = usi.c_stockiest_code " +
                    "  and lccm.c_code in :searchSeller ";
        }
        else {
            sql += " left join " +
                    " (select usi.c_ucode, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, coalesce(sch.n_sch_ratio, 0) as n_sch_ratio, coalesce(sch.n_sch_order, 9999999) as n_sch_order, sch.c_scheme, " +
                    " row_number() over(partition by usi.c_ucode order by sch.n_sch_ratio desc, coalesce(sch.n_sch_order, 9999999) asc) as rowid " +
                    " from u_stockiest_item usi " +
                    " join (" + getSellerCodeAndBuyerCodeQuery(header) + ") t on t.seller_code = usi.c_stockiest_code " +
                    " left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code " +
                    " left join cust_category_mst ccm on ccm.c_c2code = ac.c_c2code and ccm.c_code = ac.c_cust_category_code " +
                    " join lc_c2code_mst lccm on lccm.c_code = usi.c_stockiest_code  ";
        }
        sql += " left join cust_branch_item_stock cbis on cbis.c_c2code = usi.c_stockiest_code and cbis.c_item_code = usi.c_stockiest_item_code and cbis.c_br_code = lccm.c_cust_branch_code   " +
                " left join lo_view_seller_cat_item_scheme sch   " +
                " on sch.c_c2code = usi.c_stockiest_code  " +
                " and sch.c_cust_cat_code = if(trim(coalesce(ccm.c_sch_category_code, '')) = '', coalesce(ccm.c_code, '-'), ccm.c_sch_category_code)  " +
                " and sch.c_item_code = usi.c_stockiest_item_code  ) sch on sch.c_ucode = uim.c_code and sch.rowid = 1 "+
                " where uim.n_active > 0 and uimm.c_code = :search " +
                " and uim.n_last_mrp > 0 "+
                " and uim.c_gst_code is not null ";
        if (searchBO.getAvailability().equals(Constants.STATUS_YES)) {
            sql += " and coalesce(sch.n_bal_qty, 0) > 0 ";
        }
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_NEWEST)) {
            sql += " ORDER BY uim.d_adate DESC";
        } else if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_RELEVANCE)) {
            sql += " ORDER BY coalesce(fm.n_count, 0) desc";
        }
        if(searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_SCHEME))
        {
            sql += " and sch.c_scheme is not null "+
                    " order by" +
                    " coalesce(sch.n_sch_ratio, 0) desc, coalesce(sch.n_sch_order, 999999), " +
                    " coalesce(fm.n_count, 0) desc ";
        }
        return sql;
    }

    @Override
    public long shopByMfcFilterCount(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        BigInteger count = BigInteger.ZERO;
        String sql = getShopByMfcFilterQuery(header,searchBO);
        Query query = this.getQuery("SELECT COUNT(*) FROM (" + sql + ") DUMMY");
        query.setParameter("search", searchBO.getSearchTerm());
        setShopByMfcCriteria(searchBO.getBrands(), searchBO.getSellers(), query);
        Object result = this.getSingleResult(query);

        if (result != null) {
            count = (BigInteger) result;
        }

        return count.intValue();
    }

    @Override
    public JsonArray shopByMfcBrand(SearchBO searchBO) throws RecordNotFoundException {
        String sql;
        Query query;
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObj;
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = shopByMfcSearchBrandQuery();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getInSearchTerm() + "%");
            query.setParameter("mfcCode", searchBO.getSearchTerm());
        } else {
            sql = shopByMfcBrandQuery();
            query = this.getQuery(sql);
            query.setParameter("mfcCode", searchBO.getSearchTerm());
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
            //throw new RecordNotFoundException(MsMessages.RECORD_NOT_FOUND);
        }
        for (Object[] objects : resultList) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("c_brand_code", helper.getString(objects[0]));
            jsonObj.addProperty("c_brand_name", helper.getString(objects[1]));
            jsonArray.add(jsonObj);
        }
        return jsonArray;
    }

    private String shopByMfcSearchBrandQuery() {
        return "select distinct uibm.c_code, uibm.c_name from u_item_mfac_mst uimm " +
                " join u_item_mst  uim  on uim.c_item_mfac_code = uimm.c_code " +
                " join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code and uibm.c_name like :search " +
                " where uimm.c_code = :mfcCode and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                " Order By uibm.c_name asc";
    }

    private String shopByMfcBrandQuery() {
        return "select distinct uibm.c_code, uibm.c_name from u_item_mfac_mst uimm " +
                "join u_item_mst  uim  on uim.c_item_mfac_code = uimm.c_code " +
                "join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code  " +
                "where uimm.c_code = :mfcCode and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                "Order By uibm.c_name asc";
    }

    @Override
    public long shopByMfcBrandCount(SearchBO searchBO) {
        String sql;
        Query query;
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = "SELECT COUNT(*) FROM (" +shopByMfcSearchBrandQuery()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getInSearchTerm() + "%");
            query.setParameter("mfcCode", searchBO.getSearchTerm());
        } else {
            sql = "SELECT COUNT(*) FROM (" +shopByMfcBrandQuery()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("mfcCode", searchBO.getSearchTerm());
        }
        Object result = this.getSingleResult(query);
        assert result != null;
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray topMostOrderMfc(SearchBO searchBO, String stateCode) throws RecordNotFoundException {
        String sql;
        Query query;
        JsonArray jsonArray = new JsonArray();
       // JsonObject jsonObj = new JsonObject();
        if (!helper.isEmpty(searchBO.getSearchTerm())) {
            sql = topMostOrderSearchMfc();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
            query.setParameter("stateCode", stateCode);
        } else {
            sql = topMostOrderMfc();
            query = this.getQuery(sql);
            query.setParameter("stateCode", stateCode);
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
            //throw new RecordNotFoundException(MsMessages.RECORD_NOT_FOUND);
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_mfc_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_mfc_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public long topMostOrderMfcCount(SearchBO searchBO, String stateCode) {
        String sql;
        Query query;
       // JsonObject jsonObj = new JsonObject();
        if (!helper.isEmpty(searchBO.getSearchTerm())) {
            sql = topMostOrderMfcSearchCount();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
            query.setParameter("stateCode", stateCode);
        } else {
            sql = topMostOrderMfcCount();
            query = this.getQuery(sql);
            query.setParameter("stateCode", stateCode);
        }
        Object result = this.getSingleResult(query);
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray topMostOrderBrand(SearchBO searchBO, String stateCode) throws RecordNotFoundException {
        String sql;
        Query query;
        JsonArray jsonArray = new JsonArray();
       // JsonObject jsonObj = new JsonObject();
        if (!helper.isEmpty(searchBO.getSearchTerm())) {
            sql = topMostOrderSearchBrand();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
            query.setParameter("stateCode", stateCode);
        } else {
            sql = topMostOrderBrand();
            query = this.getQuery(sql);
            query.setParameter("stateCode", stateCode);
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
            //throw new RecordNotFoundException(MsMessages.RECORD_NOT_FOUND);
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_brand_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_brand_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String topMostOrderBrand() {
        return "SELECT distinct uibm.c_code, uibm.c_name FROM u_statewise_fastmoving_items usfi " +
                " JOIN u_item_mst uim  ON uim.c_code = usfi.c_ucode " +
                " join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code " +
                " where usfi.c_state_code = :stateCode and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                "ORDER BY usfi.n_count DESC ";
    }

    private String topMostOrderSearchBrand() {
        return "SELECT distinct uibm.c_code, uibm.c_name FROM u_statewise_fastmoving_items usfi " +
                " JOIN u_item_mst uim  ON uim.c_code = usfi.c_ucode " +
                " join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code and uibm.c_name like :search" +
                " where usfi.c_state_code = :stateCode and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                " ORDER BY usfi.n_count DESC ";
    }

    @Override
    public long topMostOrderBrandCount(SearchBO searchBO, String stateCode) {
        String sql;
        Query query;
       // JsonObject jsonObj = new JsonObject();
        if (!helper.isEmpty(searchBO.getSearchTerm())) {
            sql = topMostOrderBrandSearchCount();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
            query.setParameter("stateCode", stateCode);
        } else {
            sql = topMostOrderBrandCount();
            query = this.getQuery(sql);
            query.setParameter("stateCode", stateCode);
        }
        Object result = this.getSingleResult(query);
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray topMostOrderSeller(SearchBO searchBO, String stateCode) throws RecordNotFoundException {
        String sql;
        Query query;
        JsonArray jsonArray = new JsonArray();
        //JsonObject jsonObj = new JsonObject();
        if (!helper.isEmpty(searchBO.getSearchTerm())) {
            sql = topMostOrderSearchSeller();
            query = this.getQuery(sql);
            //query.setParameter("mobileNo", mobileNo);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
            query.setParameter("stateCode", stateCode);
        } else {
            sql = topMostOrderSeller();
            query = this.getQuery(sql);
            //query.setParameter("mobileNo", mobileNo);
            query.setParameter("stateCode", stateCode);
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
            //throw new RecordNotFoundException(MsMessages.RECORD_NOT_FOUND);
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_seller_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_seller_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String topMostOrderSeller() {
        return "select distinct lccm.c_code, lccm.c_name from u_statewise_fastmoving_items usfi " +
                "left join u_item_mst uim on uim.c_code = usfi.c_ucode "+
                "join u_stockiest_item usi on usfi.c_ucode = usi.c_ucode " +
                "join lc_c2code_mst lccm on usi.c_stockiest_code = lccm.c_code " +
                " where usfi.c_state_code = :stateCode and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                " ORDER BY usfi.n_count DESC ";
    }

    private String topMostOrderSearchSeller() {
        return "select distinct lccm.c_code, lccm.c_name from u_statewise_fastmoving_items usfi " +
                "left join u_item_mst uim on uim.c_code = usfi.c_ucode "+
                "join u_stockiest_item usi on usfi.c_ucode = usi.c_ucode " +
                "join lc_c2code_mst lccm on usi.c_stockiest_code = lccm.c_code and lccm.c_name like :search " +
                " where usfi.c_state_code = :stateCode and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                " ORDER BY usfi.n_count DESC ";
    }

    @Override
    public long topMostOrderSellerCount(SearchBO searchBO, String stateCode) {
        String sql;
        Query query;
        //JsonObject jsonObj = new JsonObject();
        if (!helper.isEmpty(searchBO.getSearchTerm())) {
            sql = topMostOrderSearchSellerCount();
            query = this.getQuery(sql);
           // query.setParameter("mobileNo", mobileNo);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
            query.setParameter("stateCode", stateCode);
        } else {
            sql = topMostOrderSellerCount();
            query = this.getQuery(sql);
            //query.setParameter("mobileNo", mobileNo);
            query.setParameter("stateCode", stateCode);
        }
        Object result = this.getSingleResult(query);
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray shopByMfcSellerSearch( SearchBO searchBO) throws RecordNotFoundException {
        String sql;
        Query query;
        JsonArray jsonArray = new JsonArray();
        //JsonObject jsonObj = new JsonObject();
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = shopByMfcSellerSearch();
            query = this.getQuery(sql);
            //query.setParameter("mobileNo", mobileNo);
            query.setParameter("insearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());
        } else {
            sql = shopByMfcSeller();
            query = this.getQuery(sql);
           // query.setParameter("mobileNo", mobileNo);
            query.setParameter("search", searchBO.getSearchTerm());
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
            //throw new RecordNotFoundException(MsMessages.RECORD_NOT_FOUND);
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_seller_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_seller_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String shopByMfcSeller() {
        return "select distinct lccm.c_code, lccm.c_name from u_item_mfac_mst uimm " +
                "   join u_item_mst  uim  on uim.c_item_mfac_code = uimm.c_code " +
                "   join u_stockiest_item usi on uim.c_code = usi.c_ucode  " +
                "   join lc_c2code_mst lccm on usi.c_stockiest_code = lccm.c_code " +
                "   where uimm.c_code = :search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "   ORDER BY lccm.c_name ASC ";
    }

    @Override
    public long shopByMfcSellerCount(SearchBO searchBO) {
        String sql;
        Query query;
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = shopByMfcSellerSearchCount();
            query = this.getQuery(sql);
           // query.setParameter("mobileNo", mobileNo);
            query.setParameter("insearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());
        } else {
            sql = shopByMfcSellerCount();
            query = this.getQuery(sql);
           // query.setParameter("mobileNo", mobileNo);
            query.setParameter("search", searchBO.getSearchTerm());
        }
        Object result = this.getSingleResult(query);
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray moleculeMfc(SearchBO searchBO) {
        String sql;
        Query query;
        JsonArray jsonArray = new JsonArray();
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = moleculeMfcSearch();
            query = this.getQuery(sql);
            query.setParameter("inSearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());
        } else {
            sql = moleculeMfc();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_mfc_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_mfc_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String moleculeMfc() {
        return "select distinct uimm.c_code, uimm.c_name from u_item_molecule_list uiml  " +
                "  join u_item_mst uim on uiml.c_item_code = uim.c_code " +
                "  join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code " +
                "  where uiml.c_molecule_code =:search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                "  order by uimm.c_name asc ";
    }

    private String moleculeMfcSearch() {
        return "select distinct uimm.c_code, uimm.c_name from u_item_molecule_list uiml  " +
                "  join u_item_mst uim on uiml.c_item_code = uim.c_code " +
                "  join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code and uimm.c_name like :inSearch " +
                "  where uiml.c_molecule_code =:search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                "  order by uimm.c_name asc ";
    }

    @Override
    public long moleculeMfcCount(SearchBO searchBO) {
        String sql;
        Query query;
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = "SELECT COUNT(*) FROM (" +moleculeMfcSearch()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("inSearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());
        } else {
            sql = "SELECT COUNT(*) FROM (" +moleculeMfc()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
        }
        Object result = this.getSingleResult(query);
        assert result != null;
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray moleculeBrand(SearchBO searchBO) {
        String sql;
        Query query;
        JsonArray jsonArray = new JsonArray();
        //JsonObject jsonObj = new JsonObject();
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = moleculeBrandSearch();
            query = this.getQuery(sql);
            query.setParameter("inSearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());
        } else {
            sql = moleculeBrand();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_brand_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_brand_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String moleculeBrand() {
        return "select distinct uibm.c_code, uibm.c_name from u_item_molecule_list uiml  " +
                "  join u_item_mst uim on uiml.c_item_code = uim.c_code " +
                "  join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code " +
                "  where uiml.c_molecule_code =:search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                "  order by uibm.c_name asc" ;
    }

    private String moleculeBrandSearch() {
        return "select distinct uibm.c_code, uibm.c_name from u_item_molecule_list uiml  " +
                "  join u_item_mst uim on uiml.c_item_code = uim.c_code " +
                "  join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code and uibm.c_name like :inSearch " +
                "  where uiml.c_molecule_code =:search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                "  order by uibm.c_name asc";
    }

    @Override
    public long moleculeBrandCount(SearchBO searchBO) {
        String sql;
        Query query;
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = "SELECT COUNT(*) FROM (" +moleculeBrandSearch()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("inSearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());
        } else {
            sql = "SELECT COUNT(*) FROM (" +moleculeBrand()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
        }
        Object result = this.getSingleResult(query);
        assert result != null;
        return new BigInteger(String.valueOf(result)).longValue();

    }

    @Override
    public JsonArray moleculeSeller(SearchBO searchBO) {
        String sql;
        Query query;
        JsonArray jsonArray = new JsonArray();
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = moleculeSellerSearch();
            query = this.getQuery(sql);
            query.setParameter("inSearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());
        } else {
            sql = moleculeSeller();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_seller_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_seller_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String moleculeSeller() {
        return "select distinct lccm.c_code, lccm.c_name from u_item_molecule_list uiml  " +
                "  join u_item_mst uim on uiml.c_item_code = uim.c_code " +
                "  join u_stockiest_item usi2 on usi2.c_ucode =uim.c_code " +
                "  join lc_c2code_mst lccm on usi2.c_stockiest_code = lccm.c_code " +
                "  where uiml.c_molecule_code =:search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                "  order by lccm.c_name asc";
    }

    private String moleculeSellerSearch() {
        return "select distinct lccm.c_code, lccm.c_name from u_item_molecule_list uiml  " +
                "  join u_item_mst uim on uiml.c_item_code = uim.c_code " +
                "  join u_stockiest_item usi2 on usi2.c_ucode =uim.c_code " +
                "  join lc_c2code_mst lccm on usi2.c_stockiest_code = lccm.c_code and lccm.c_name like :inSearch " +
                "  where uiml.c_molecule_code =:search and uim.n_active > 0 and uim.c_gst_code is not null  " +
                "  and uim.n_last_mrp > 0 "+
                "  order by lccm.c_name asc";
    }

    @Override
    public long moleculeSellerCount(SearchBO searchBO) {
        String sql;
        Query query;
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = "SELECT COUNT(*) FROM (" +moleculeSellerSearch()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("inSearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());
        } else {
            sql = "SELECT COUNT(*) FROM (" +moleculeSeller()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
        }
        Object result = this.getSingleResult(query);
        assert result != null;
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray moleculeFilter(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        String sql = getMoleculeFilterQuery(header,searchBO);
        Query query = this.getQuery(sql);
        query.setParameter("molCode", searchBO.getSearchTerm());
        setCriteria(searchBO.getManufacturers(), searchBO.getBrands(), searchBO.getSellers(), query);
        JsonArray arr = new JsonArray();
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return arr;
        }

        for (Object[] objects : resultList) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray1 = new JsonArray();
            JsonObject jobj = new JsonObject();
            String itemCode = helper.getString(objects[++i]);
            jsonObject.addProperty("c_item_code", itemCode);
            jsonObject.addProperty("c_item_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_pack_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_contains", helper.getString(objects[++i]));
            jsonObject.addProperty("n_mrp", Double.parseDouble(objects[++i].toString()));
            jsonObject.addProperty("c_scheme",helper.getString(objects[++i]));
            jobj.addProperty("c_thumbnail_image", helper.getString(objects[++i]));
            jsonArray1.add(jobj);
            jsonObject.add("ac_thumbnail_images", jsonArray1);
            jsonObject.addProperty("c_pack_type_name",helper.getString(objects[++i]));
            jsonObject.addProperty("c_discount_status", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_watchlist_status", Constants.STATUS_NO);
            jsonObject.addProperty("c_short_book_status", Constants.STATUS_NO);
//            jsonObject.addProperty("c_watchlist_status", shortBookWatchListService.isWatchList(lcHeader.getUserId(),
//                    lcHeader.getFirmId(), lcHeader.getBranchId(), itemCode));
//            jsonObject.addProperty("c_short_book_status", shortBookWatchListService.isShortBook(lcHeader.getUserId(),
//                    lcHeader.getFirmId(), lcHeader.getBranchId(), itemCode));
            arr.add(jsonObject);
        }
        return arr;
    }

    private String getMoleculeFilterQuery(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        String sql = "select distinct uim.c_code as itemCode, " +
                "  uim.c_name as itemName, " +
                "  uipm.c_name as packName,  " +
                "  uicm.c_name as contentName, " +
                "  uim.n_last_mrp as Mrp,  " +
                "  sch.c_scheme as scheme, "+
                "  uim.c_web_img_link as acThumbnailImage, " +
                " uiptm.c_name as packTypeName "+
                " from u_item_mst uim " +
                " join u_item_molecule_list uiml on uim.c_code = uiml.c_item_code " +
                " join u_item_pack_mst uipm on uim.c_item_pack_code = uipm.c_code " +
                " join u_item_cont_mst uicm on uim.c_item_cont_code = uicm.c_code " +
                " join u_item_pack_type_mst uiptm on uim.c_pack_type_code = uiptm.c_code "+
                " left join u_fastmoving_items fm on uim.c_code = fm.c_ucode  " ;

        if (!searchBO.getManufacturers().isEmpty()) {
            sql += "  join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code "+
                    " AND uimm.c_code IN :searchMfc ";
        }
        if (!searchBO.getBrands().isEmpty()) {
            sql  += "JOIN u_item_brand_mst uibm ON uim.c_item_brand_code = uibm.c_code "+
                    " AND uibm.c_code IN :searchBrand ";
        }
        if (!searchBO.getSellers().isEmpty()) {
            sql += " join " +
                    " (select usi.c_ucode, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, coalesce(sch.n_sch_ratio, 0) as n_sch_ratio, coalesce(sch.n_sch_order, 9999999) as n_sch_order, sch.c_scheme, " +
                    "  row_number() over(partition by usi.c_ucode order by sch.n_sch_ratio desc, coalesce(sch.n_sch_order, 9999999) asc) as rowid " +
                    "  from u_stockiest_item usi " +
                    "  left join (" + getSellerCodeAndBuyerCodeQuery(header) +") t on t.seller_code = usi.c_stockiest_code " +
                    "  left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code " +
                    "  left join cust_category_mst ccm on ccm.c_c2code = ac.c_c2code and ccm.c_code = ac.c_cust_category_code " +
                    "  join lc_c2code_mst lccm on lccm.c_code = usi.c_stockiest_code " +
                    "  and lccm.c_code in :searchSeller ";
        }
        else {
            sql += " left join " +
                    " (select usi.c_ucode, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, coalesce(sch.n_sch_ratio, 0) as n_sch_ratio, coalesce(sch.n_sch_order, 9999999) as n_sch_order, sch.c_scheme, " +
                    " row_number() over(partition by usi.c_ucode order by sch.n_sch_ratio desc, coalesce(sch.n_sch_order, 9999999) asc) as rowid " +
                    " from u_stockiest_item usi " +
                    " join (" + getSellerCodeAndBuyerCodeQuery(header) + ") t on t.seller_code = usi.c_stockiest_code " +
                    " left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code " +
                    " left join cust_category_mst ccm on ccm.c_c2code = ac.c_c2code and ccm.c_code = ac.c_cust_category_code " +
                    " join lc_c2code_mst lccm on lccm.c_code = usi.c_stockiest_code  ";
        }
        sql +=  " left join cust_branch_item_stock cbis on cbis.c_c2code = usi.c_stockiest_code and cbis.c_item_code = usi.c_stockiest_item_code and cbis.c_br_code = lccm.c_cust_branch_code   " +
                " left join lo_view_seller_cat_item_scheme sch   " +
                " on sch.c_c2code = usi.c_stockiest_code  " +
                " and sch.c_cust_cat_code = if(trim(coalesce(ccm.c_sch_category_code, '')) = '', coalesce(ccm.c_code, '-'), ccm.c_sch_category_code)  " +
                " and sch.c_item_code = usi.c_stockiest_item_code ) sch on sch.c_ucode = uim.c_code and sch.rowid = 1 "+
                " WHERE uim.n_active > 0 and uiml.c_molecule_code = :molCode "+
                " and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 ";
        if (searchBO.getAvailability().equals(Constants.STATUS_YES)) {
            sql += " and coalesce(sch.n_bal_qty, 0) > 0 ";
        }
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_NEWEST)) {
            sql += " ORDER BY uim.d_adate DESC";
        } else if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_RELEVANCE)) {
            sql += " ORDER BY coalesce(fm.n_count, 0) desc";
        }
        if(searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_SCHEME))
        {
            sql += " and sch.c_scheme is not null "+
                    " order by" +
                    " coalesce(sch.n_sch_ratio, 0) desc, coalesce(sch.n_sch_order, 999999), " +
                    " coalesce(fm.n_count, 0) desc ";
        }
        return sql;
    }

    @Override
    public long moleculeFilterCount(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        BigInteger count = BigInteger.ZERO;
        String sql = getMoleculeFilterQuery(header, searchBO);
        Query query = this.getQuery("SELECT COUNT(*) FROM (" + sql + ") DUMMY");
        query.setParameter("molCode", searchBO.getSearchTerm());
        setCriteria(searchBO.getManufacturers(), searchBO.getBrands(), searchBO.getSellers(), query);
        Object result = this.getSingleResult(query);
        if (result != null) {
            count = (BigInteger) result;
        }
        return count.intValue();
    }

    @Override
    public long productMfcCount(SearchBO searchBO) {
        String sql;
        Query query;
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = "SELECT COUNT(*) FROM (" +productMfcSearch()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("insearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        } else {
            sql = "SELECT COUNT(*) FROM (" +productMfc()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        }
        Object result = this.getSingleResult(query);
        assert result != null;
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray productMfc(SearchBO searchBO) {
        String sql;
        Query query;
        JsonArray jsonArray = new JsonArray();
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = productMfcSearch();
            query = this.getQuery(sql);
            query.setParameter("insearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        } else {
            sql = productMfc();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_mfc_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_mfc_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public JsonArray productBrand(SearchBO searchBO) {
        String sql;
        Query query;
        JsonArray jsonArray = new JsonArray();
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = productBrandSearch();
            query = this.getQuery(sql);
            query.setParameter("insearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        } else {
            sql = productBrand();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_brand_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_brand_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String productBrand() {
        return "select distinct uibm.c_code, uibm.c_name from u_item_mst uim " +
                "  join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code and uim.c_name like :search " +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "  order by uibm.c_name asc ";
    }

    private String productBrandSearch() {
        return "select distinct uibm.c_code, uibm.c_name from u_item_mst uim " +
                "  join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code and uim.c_name like :search " +
                "  and uibm.c_name like :insearch " +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "  order by uibm.c_name asc ";
    }

    @Override
    public long productBrandCount(SearchBO searchBO) {
        String sql;
        Query query;
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = "SELECT COUNT(*) FROM (" +productBrandSearch()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("insearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        } else {
            sql = "SELECT COUNT(*) FROM (" +productBrand()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        }
        Object result = this.getSingleResult(query);
        assert result != null;
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray productSeller(SearchBO searchBO) {
        String sql;
        Query query;
        JsonArray jsonArray = new JsonArray();
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = productSellerSearch();
            query = this.getQuery(sql);
            query.setParameter("insearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        } else {
            sql = productSeller();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_seller_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_seller_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String productSeller() {
        return "select distinct lcm.c_code, lcm.c_name from u_stockiest_item usi " +
                "  join u_item_mst uim on usi.c_ucode = uim.c_code and uim.c_name like :search " +
                "  join lc_c2code_mst lcm on usi.c_stockiest_code = lcm.c_code" +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "  order by lcm.c_name asc";
    }

    private String productSellerSearch() {
        return "select distinct lcm.c_code, lcm.c_name from u_stockiest_item usi " +
                "  join u_item_mst uim on usi.c_ucode = uim.c_code and uim.c_name like :search " +
                "  join lc_c2code_mst lcm on usi.c_stockiest_code = lcm.c_code and lcm.c_name like :insearch " +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "  order by lcm.c_name asc";
    }

    @Override
    public long productSellerCount(SearchBO searchBO) {
        String sql;
        Query query;
        if (!helper.isEmpty(searchBO.getInSearchTerm())) {
            sql = "SELECT COUNT(*) FROM (" +productSellerSearch()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("insearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        } else {
            sql = "SELECT COUNT(*) FROM (" +productSeller()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm() + "%");
        }
        Object result = this.getSingleResult(query);
        assert result != null;
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray productFilter(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        String sql = getProductFilterQuery(searchBO,header);
        Query query = this.getQuery(sql);
        query.setParameter("search", searchBO.getSearchTerm() +"%");
        setCriteria(searchBO.getManufacturers(), searchBO.getBrands(), searchBO.getSellers(), query);
        JsonArray arr = new JsonArray();
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return arr;
            //throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }

        for (Object[] objects : resultList) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray1 = new JsonArray();
            JsonObject jobj = new JsonObject();
            String itemCode = helper.getString(objects[++i]);
            jsonObject.addProperty("c_item_code", itemCode);
            jsonObject.addProperty("c_item_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_pack_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_contains", helper.getString(objects[++i]));
            jsonObject.addProperty("n_mrp", Double.parseDouble(objects[++i].toString()));
            jsonObject.addProperty("c_scheme",helper.getString(objects[++i]));
            jobj.addProperty("c_thumbnail_image", helper.getString(objects[++i]));
            jsonArray1.add(jobj);
            jsonObject.add("ac_thumbnail_images", jsonArray1);
            jsonObject.addProperty("c_pack_type_name",helper.getString(objects[++i]));
            jsonObject.addProperty("c_discount_status", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_watchlist_status", Constants.STATUS_NO);
            jsonObject.addProperty("c_short_book_status", Constants.STATUS_NO);
//            jsonObject.addProperty("c_watchlist_status", shortBookWatchListService.isWatchList(lcHeader.getUserId(),
//                    lcHeader.getFirmId(), lcHeader.getBranchId(), itemCode));
//            jsonObject.addProperty("c_short_book_status", shortBookWatchListService.isShortBook(lcHeader.getUserId(),
//                    lcHeader.getFirmId(), lcHeader.getBranchId(), itemCode));
            arr.add(jsonObject);
        }
        return arr;
    }

    private String getProductFilterQuery(ItemsSearchBO searchBO, LcHeaderBO header) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        String sql = "select distinct uim.c_code itemCode, uim.c_name itemName, uipm.c_name packName, uicm.c_name contentName," +
                " uim.n_last_mrp Mrp,  sch.c_scheme as scheme, uim.c_web_img_link acThumbnailImage, " +
                " uiptm.c_name as packTypeName "+
                " from u_item_mst uim" +
                " join u_item_pack_mst uipm on uim.c_item_pack_code = uipm.c_code" +
                " join u_item_cont_mst uicm on uim.c_item_cont_code = uicm.c_code" +
                " join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code";
        if (!searchBO.getManufacturers().isEmpty()) {
            sql += " and uimm.c_code in :searchMfc ";
        }
        sql +=  " join u_item_pack_type_mst uiptm on uim.c_pack_type_code = uiptm.c_code "+
                " left join u_fastmoving_items fm on uim.c_code = fm.c_ucode  " ;

        if (!searchBO.getBrands().isEmpty()) {
            sql += " join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code " +
                    " and uibm.c_code in :searchBrand ";
        }
        if (!searchBO.getSellers().isEmpty()) {
            sql += " join " +
                    " (select usi.c_ucode, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, coalesce(sch.n_sch_ratio, 0) as n_sch_ratio, coalesce(sch.n_sch_order, 9999999) as n_sch_order, sch.c_scheme, " +
                    "  row_number() over(partition by usi.c_ucode order by sch.n_sch_ratio desc, coalesce(sch.n_sch_order, 9999999) asc) as rowid " +
                    "  from u_stockiest_item usi " +
                    "  left join (" + getSellerCodeAndBuyerCodeQuery(header) +") t on t.seller_code = usi.c_stockiest_code " +
                    "  left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code " +
                    "  left join cust_category_mst ccm on ccm.c_c2code = ac.c_c2code and ccm.c_code = ac.c_cust_category_code " +
                    "  join lc_c2code_mst lccm on lccm.c_code = usi.c_stockiest_code " +
                    "  and lccm.c_code in :searchSeller ";
        }
        else {
            sql += " left join " +
                    " (select usi.c_ucode, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, coalesce(sch.n_sch_ratio, 0) as n_sch_ratio, coalesce(sch.n_sch_order, 9999999) as n_sch_order, sch.c_scheme, " +
                    " row_number() over(partition by usi.c_ucode order by sch.n_sch_ratio desc, coalesce(sch.n_sch_order, 9999999) asc) as rowid " +
                    " from u_stockiest_item usi " +
                    " join (" + getSellerCodeAndBuyerCodeQuery(header) + ") t on t.seller_code = usi.c_stockiest_code " +
                    " left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code " +
                    " left join cust_category_mst ccm on ccm.c_c2code = ac.c_c2code and ccm.c_code = ac.c_cust_category_code " +
                    " join lc_c2code_mst lccm on lccm.c_code = usi.c_stockiest_code  ";
        }
        sql +=  " left join cust_branch_item_stock cbis on cbis.c_c2code = usi.c_stockiest_code and cbis.c_item_code = usi.c_stockiest_item_code and cbis.c_br_code = lccm.c_cust_branch_code   " +
                " left join lo_view_seller_cat_item_scheme sch   " +
                " on sch.c_c2code = usi.c_stockiest_code  " +
                " and sch.c_cust_cat_code = if(trim(coalesce(ccm.c_sch_category_code, '')) = '', coalesce(ccm.c_code, '-'), ccm.c_sch_category_code)  " +
                " and sch.c_item_code = usi.c_stockiest_item_code ) sch on sch.c_ucode = uim.c_code and sch.rowid = 1 "+
                " where uim.c_name like :search " +
                " and uim.n_active > 0 " +
                " and uim.n_last_mrp > 0 "+
                " and uim.c_gst_code is not null ";
        if (searchBO.getAvailability().equals(Constants.STATUS_YES)) {
            sql += " and coalesce(sch.n_bal_qty, 0) > 0 ";
        }
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_NEWEST)) {
            sql += " order by uim.d_adate desc";
        } else if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_RELEVANCE)) {
            sql += " order by coalesce(fm.n_count, 0) desc";
        }
        if(searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_SCHEME))
        {
            sql += " and sch.c_scheme is not null "+
                    " order by" +
                    " coalesce(sch.n_sch_ratio, 0) desc, coalesce(sch.n_sch_order, 999999), " +
                    " coalesce(fm.n_count, 0) desc ";
        }
        return sql;
    }

    @Override
    public long productFilterCount(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        BigInteger count = BigInteger.ZERO;
        String sql = getProductFilterQuery(searchBO,header);
        Query query = this.getQuery("SELECT COUNT(*) FROM (" + sql + ") DUMMY");
        query.setParameter("search", searchBO.getSearchTerm() +"%");
        setCriteria(searchBO.getManufacturers(), searchBO.getBrands(), searchBO.getSellers(), query);

        Object result = this.getSingleResult(query);
        if (result != null) {
            count = (BigInteger) result;
        }
        return count.intValue();
    }

    @Override
    public JsonArray categoryFilter(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
       // String sellerBuyer = getSellerCodeAndBuyerCodeQuery(header);
        String sql = getCategoryFilterSearchQuery(header,searchBO);
        Query query = this.getQuery(sql);
        if (searchBO.getSearchTerm() != null) {
            query.setParameter("search", searchBO.getSearchTerm());
        }
        setCriteria(searchBO.getManufacturers(), searchBO.getBrands(), searchBO.getSellers(), query);
        JsonArray arr = new JsonArray();
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return arr;
            //throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        for (Object[] objects : resultList) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray1 = new JsonArray();
            JsonObject jobj = new JsonObject();
            String itemCode = helper.getString(objects[++i]);
            jsonObject.addProperty("c_item_code", itemCode);
            jsonObject.addProperty("c_item_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_pack_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_contains", helper.getString(objects[++i]));
            jsonObject.addProperty("n_mrp", Double.parseDouble(objects[++i].toString()));
            jsonObject.addProperty("c_scheme",helper.getString(objects[++i]));
            jobj.addProperty("c_thumbnail_image", helper.getString(objects[++i]));
            jsonArray1.add(jobj);
            jsonObject.add("ac_thumbnail_images", jsonArray1);
            jsonObject.addProperty("c_pack_type_name",helper.getString(objects[++i]));
            jsonObject.addProperty("c_discount_status", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_watchlist_status", Constants.STATUS_NO);
            jsonObject.addProperty("c_short_book_status", Constants.STATUS_NO);
//            jsonObject.addProperty("c_watchlist_status", shortBookWatchListService.isWatchList(lcHeader.getUserId(),
//                    lcHeader.getFirmId(), lcHeader.getBranchId(), itemCode));
//            jsonObject.addProperty("c_short_book_status", shortBookWatchListService.isShortBook(lcHeader.getUserId(),
//                    lcHeader.getFirmId(), lcHeader.getBranchId(), itemCode));
            arr.add(jsonObject);
        }
        return arr;
    }

    private String getCategoryFilterSearchQuery(LcHeaderBO header,ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        String sql = "select  " +
                "  distinct uim.c_code itemCode,  " +
                "  uim.c_name itemName,  " +
                "  uipm.c_name packName,   " +
                "  uicm.c_name contentName,  " +
                "  uim.n_last_mrp lastMrp,   " +
                "  sch.c_scheme as scheme, " +
                "  uim.c_web_img_link acThumbnailImage, " +
                "  uiptm.c_name as packTypeName "+
                " from u_item_mst uim  " +
                " join u_item_pack_mst uipm on uim.c_item_pack_code = uipm.c_code  " +
                " join u_item_cont_mst uicm on uim.c_item_cont_code = uicm.c_code   " +
                " join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code  " ;
        if (!searchBO.getManufacturers().isEmpty()) {
            sql += " and uimm.c_code in :searchMfc ";
        }
        sql += " join u_item_cat_mst uicm2 on uicm2.c_code = uim.c_item_cat_code   " +
                " join u_item_cat_head_mst uichm FORCE INDEX(idx_c_item_category_class_code)  on uicm2.c_item_category_head_code = uichm.c_code "+
                " join u_item_pack_type_mst uiptm on uim.c_pack_type_code = uiptm.c_code "+
                " left join u_fastmoving_items fm on uim.c_code = fm.c_ucode  " ;
        if (!searchBO.getBrands().isEmpty()) {
            sql += " join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code " +
                    " and uibm.c_code in :searchBrand ";
        }
        if (!searchBO.getSellers().isEmpty()) {
            sql += " join " +
                    " (select usi.c_ucode, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, coalesce(sch.n_sch_ratio, 0) as n_sch_ratio, coalesce(sch.n_sch_order, 9999999) as n_sch_order, sch.c_scheme, " +
                    "  row_number() over(partition by usi.c_ucode order by sch.n_sch_ratio desc, coalesce(sch.n_sch_order, 9999999) asc) as rowid " +
                    "  from u_stockiest_item usi " +
                    "  left join (" + getSellerCodeAndBuyerCodeQuery(header) +") t on t.seller_code = usi.c_stockiest_code " +
                    "  left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code " +
                    "  left join cust_category_mst ccm on ccm.c_c2code = ac.c_c2code and ccm.c_code = ac.c_cust_category_code " +
                    "  join lc_c2code_mst lccm on lccm.c_code = usi.c_stockiest_code " +
                    "  and lccm.c_code in :searchSeller ";
        }
        else {
            sql += " left join " +
                    " (select usi.c_ucode, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, coalesce(sch.n_sch_ratio, 0) as n_sch_ratio, coalesce(sch.n_sch_order, 9999999) as n_sch_order, sch.c_scheme, " +
                    " row_number() over(partition by usi.c_ucode order by sch.n_sch_ratio desc, coalesce(sch.n_sch_order, 9999999) asc) as rowid " +
                    " from u_stockiest_item usi " +
                    " join (" + getSellerCodeAndBuyerCodeQuery(header) + ") t on t.seller_code = usi.c_stockiest_code " +
                    " left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code " +
                    " left join cust_category_mst ccm on ccm.c_c2code = ac.c_c2code and ccm.c_code = ac.c_cust_category_code " +
                    " join lc_c2code_mst lccm on lccm.c_code = usi.c_stockiest_code  ";
        }
         sql += " left join cust_branch_item_stock cbis on cbis.c_c2code = usi.c_stockiest_code and cbis.c_item_code = usi.c_stockiest_item_code and cbis.c_br_code = lccm.c_cust_branch_code  " +
                " left join lo_view_seller_cat_item_scheme sch  " +
                " on sch.c_c2code = usi.c_stockiest_code " +
                "   and sch.c_cust_cat_code = if(trim(coalesce(ccm.c_sch_category_code, '')) = '', coalesce(ccm.c_code, '-'), ccm.c_sch_category_code) " +
                "   and sch.c_item_code = usi.c_stockiest_item_code ) sch on sch.c_ucode = uim.c_code and sch.rowid = 1 "+
                " where uim.n_last_mrp > 0 " +
                " and uim.n_active > 0 and uim.c_gst_code is not null " ;
        if (searchBO.getSearchTerm() != null) {
            sql += " and uichm.c_item_category_class_code = :search ";
        }
        if (searchBO.getAvailability().equals(Constants.STATUS_YES)) {
            sql += " and coalesce(sch.n_bal_qty, 0) > 0 ";
        }
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_NEWEST)) {
            sql += " order by uim.d_adate desc";
        } else if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_RELEVANCE)) {
            sql += " order by coalesce(fm.n_count, 0) desc";
        }
        if(searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_SCHEME))
        {
            sql += " and sch.c_scheme is not null "+
                    " order by" +
                    " coalesce(sch.n_sch_ratio, 0) desc, coalesce(sch.n_sch_order, 999999), " +
                    " coalesce(fm.n_count, 0) desc ";
        }
        return sql;
    }

    @Override
    public long categoryFilterCount(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        String sql = "SELECT COUNT(*) FROM (" + getCategoryFilterSearchQuery(header,searchBO) + ") DUMMY";
       // String sql = categoryFilterPLPCount(header,searchBO);
        Query query = this.getQuery(sql);
        if (searchBO.getSearchTerm() != null) {
            query.setParameter("search", searchBO.getSearchTerm());
        }
        setCriteria(searchBO.getManufacturers(), searchBO.getBrands(), searchBO.getSellers(), query);
        Object result = this.getSingleResult(query);
        assert result != null;
        return new BigInteger(String.valueOf(result)).longValue();
    }

    private String categoryFilterPLPCount(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        String sql = "select  " +
                "  COUNT(distinct uim.c_code) " +
                " from u_item_mst uim FORCE INDEX(idx_combo) " +
                " join u_item_pack_mst uipm on uim.c_item_pack_code = uipm.c_code  " +
                " join u_item_cont_mst uicm on uim.c_item_cont_code = uicm.c_code   " +
                " join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code  " ;
        if (!searchBO.getManufacturers().isEmpty()) {
            sql += " and uimm.c_code in :searchMfc ";
        }
        sql += " join u_item_cat_mst uicm2 on uicm2.c_code = uim.c_item_cat_code   " +
                " join u_item_cat_head_mst uichm FORCE INDEX(idx_c_item_category_class_code)  on uicm2.c_item_category_head_code = uichm.c_code "+
                " join u_item_pack_type_mst uiptm on uim.c_pack_type_code = uiptm.c_code "+
                " left join u_fastmoving_items fm on uim.c_code = fm.c_ucode  " ;
        if (!searchBO.getBrands().isEmpty()) {
            sql += " join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code " +
                    " and uibm.c_code in :searchBrand ";
        }
        if (!searchBO.getSellers().isEmpty()) {
            sql += " join " +
                    " (select usi.c_ucode, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, coalesce(sch.n_sch_ratio, 0) as n_sch_ratio, coalesce(sch.n_sch_order, 9999999) as n_sch_order, sch.c_scheme, " +
                    "  row_number() over(partition by usi.c_ucode order by sch.n_sch_ratio desc, coalesce(sch.n_sch_order, 9999999) asc) as rowid " +
                    "  from u_stockiest_item usi " +
                    "  left join (" + getSellerCodeAndBuyerCodeQuery(header) +") t on t.seller_code = usi.c_stockiest_code " +
                    "  left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code " +
                    "  left join cust_category_mst ccm on ccm.c_c2code = ac.c_c2code and ccm.c_code = ac.c_cust_category_code " +
                    "  join lc_c2code_mst lccm on lccm.c_code = usi.c_stockiest_code " +
                    "  and lccm.c_code in :searchSeller ";
        }
        else {
            sql += " left join " +
                    " (select usi.c_ucode, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, coalesce(sch.n_sch_ratio, 0) as n_sch_ratio, coalesce(sch.n_sch_order, 9999999) as n_sch_order, sch.c_scheme, " +
                    " row_number() over(partition by usi.c_ucode order by sch.n_sch_ratio desc, coalesce(sch.n_sch_order, 9999999) asc) as rowid " +
                    " from u_stockiest_item usi " +
                    " join (" + getSellerCodeAndBuyerCodeQuery(header) + ") t on t.seller_code = usi.c_stockiest_code " +
                    " left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code " +
                    " left join cust_category_mst ccm on ccm.c_c2code = ac.c_c2code and ccm.c_code = ac.c_cust_category_code " +
                    " join lc_c2code_mst lccm on lccm.c_code = usi.c_stockiest_code  ";
        }
        sql += " left join cust_branch_item_stock cbis on cbis.c_c2code = usi.c_stockiest_code and cbis.c_item_code = usi.c_stockiest_item_code and cbis.c_br_code = lccm.c_cust_branch_code  " +
                " left join lo_view_seller_cat_item_scheme sch  " +
                " on sch.c_c2code = usi.c_stockiest_code " +
                "   and sch.c_cust_cat_code = if(trim(coalesce(ccm.c_sch_category_code, '')) = '', coalesce(ccm.c_code, '-'), ccm.c_sch_category_code) " +
                "   and sch.c_item_code = usi.c_stockiest_item_code ) sch on sch.c_ucode = uim.c_code and sch.rowid = 1 "+
                " where uim.n_last_mrp > 0 " +
                " and uim.n_active > 0 and uim.c_gst_code is not null " ;
        if (searchBO.getSearchTerm() != null) {
            sql += " and uichm.c_item_category_class_code = :search ";
        }
        if (searchBO.getAvailability().equals(Constants.STATUS_YES)) {
            sql += " and coalesce(sch.n_bal_qty, 0) > 0 ";
        }
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_NEWEST)) {
            sql += " order by uim.d_adate desc";
        } else if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_RELEVANCE)) {
            sql += " order by coalesce(fm.n_count, 0) desc";
        }
        if(searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_SCHEME))
        {
            sql += " and sch.c_scheme is not null "+
                    " order by" +
                    " coalesce(sch.n_sch_ratio, 0) desc, coalesce(sch.n_sch_order, 999999), " +
                    " coalesce(fm.n_count, 0) desc ";
        }
        return sql;
}

    private String productMfc() {
        return "select distinct uimm.c_code, uimm.c_name from u_item_mst uim " +
                "  join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code and uim.c_name like :search " +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "  order by uimm.c_name asc ";
    }

    private String productMfcSearch() {
        return "select distinct uimm.c_code, uimm.c_name from u_item_mst uim " +
                "  join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code and uim.c_name like :search " +
                "  and uimm.c_name like :insearch " +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "  order by uimm.c_name asc";
    }

    private String shopByMfcSellerCount() {
        return "select count(distinct lccm.c_code) from u_item_mfac_mst uimm " +
                "   join u_item_mst  uim  on uim.c_item_mfac_code = uimm.c_code " +
                "   join u_stockiest_item usi on uim.c_code = usi.c_ucode  " +
                "   join lc_c2code_mst lccm on usi.c_stockiest_code = lccm.c_code " +
                "   where uimm.c_code = :search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                "   ORDER BY lccm.c_name ASC ";
    }

    private String shopByMfcSellerSearchCount() {
        return "select count(distinct lccm.c_code) from u_item_mfac_mst uimm " +
                "   join u_item_mst  uim  on uim.c_item_mfac_code = uimm.c_code " +
                "   join u_stockiest_item usi on uim.c_code = usi.c_ucode  " +
                "   join lc_c2code_mst lccm on usi.c_stockiest_code = lccm.c_code and lccm.c_name like :insearch " +
                "   where uimm.c_code = :search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                "   ORDER BY lccm.c_name ASC ";
    }

    private String shopByMfcSellerSearch() {
        return "select distinct lccm.c_code, lccm.c_name from u_item_mfac_mst uimm " +
                "   join u_item_mst  uim  on uim.c_item_mfac_code = uimm.c_code " +
                "   join u_stockiest_item usi on uim.c_code = usi.c_ucode  " +
                "   join lc_c2code_mst lccm on usi.c_stockiest_code = lccm.c_code and lccm.c_name like :insearch " +
                "   where uimm.c_code = :search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                "   ORDER BY lccm.c_name ASC ";
    }

    private String topMostOrderSellerCount() {
        return "select count(distinct lccm.c_code) from u_statewise_fastmoving_items usfi " +
                "left join u_item_mst uim on uim.c_code = usfi.c_ucode "+
                "join u_stockiest_item usi on usfi.c_ucode = usi.c_ucode " +
                "join lc_c2code_mst lccm on usi.c_stockiest_code = lccm.c_code " +
                " where usfi.c_state_code = :stateCode and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                " ORDER BY usfi.n_count DESC ";
    }

    private String topMostOrderSearchSellerCount() {
        return "select count(distinct lccm.c_code) from u_statewise_fastmoving_items usfi " +
                "left join u_item_mst uim on uim.c_code = usfi.c_ucode  "+
                "join u_stockiest_item usi on usfi.c_ucode = usi.c_ucode " +
                "join lc_c2code_mst lccm on usi.c_stockiest_code = lccm.c_code and lccm.c_name like :search " +
                " where usfi.c_state_code = :stateCode and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                " ORDER BY usfi.n_count DESC ";
    }

    private String topMostOrderBrandSearchCount() {
        return "SELECT count(distinct uibm.c_name) FROM u_statewise_fastmoving_items usfi " +
                " JOIN u_item_mst uim  ON uim.c_code = usfi.c_ucode " +
                " join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code and uibm.c_name like :search" +
                " where usfi.c_state_code = :stateCode and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                " ORDER BY usfi.n_count DESC ";
    }

    private String topMostOrderBrandCount() {
        return "SELECT count(distinct uibm.c_name) FROM u_statewise_fastmoving_items usfi " +
                " JOIN u_item_mst uim  ON uim.c_code = usfi.c_ucode " +
                " join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code " +
                " where usfi.c_state_code = :stateCode and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                " ORDER BY usfi.n_count DESC ";

    }

    private String topMostOrderMfcCount() {
        return "SELECT count(distinct uimm.c_name) FROM u_statewise_fastmoving_items usfi " +
                " JOIN u_item_mst uim  ON uim.c_code = usfi.c_ucode " +
                " join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code " +
                " where usfi.c_state_code = :stateCode and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                " ORDER BY usfi.n_count DESC ";
    }

    private String topMostOrderMfcSearchCount() {
        return "SELECT count(distinct uimm.c_name) FROM u_statewise_fastmoving_items usfi " +
                " JOIN u_item_mst uim  ON uim.c_code = usfi.c_ucode " +
                " join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code and uimm.c_name like :search " +
                " where usfi.c_state_code = :stateCode and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                " ORDER BY usfi.n_count DESC ";
    }

    private String topMostOrderSearchMfc() {
        return "SELECT distinct uimm.c_code, uimm.c_name FROM u_statewise_fastmoving_items usfi " +
                " JOIN u_item_mst uim  ON uim.c_code = usfi.c_ucode " +
                " join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code and uimm.c_name like :search " +
                " where usfi.c_state_code = :stateCode and uim.n_active > 0 and uim.c_gst_code is not null " +
                " and uim.n_last_mrp > 0 "+
                " ORDER BY usfi.n_count DESC ";
    }

    private String topMostOrderMfc() {
        return "SELECT distinct uimm.c_code, uimm.c_name FROM u_statewise_fastmoving_items usfi " +
                " JOIN u_item_mst uim  ON uim.c_code = usfi.c_ucode " +
                " join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code " +
                " where usfi.c_state_code = :stateCode and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                " ORDER BY usfi.n_count DESC ";
    }

    private String preferredSellerBrandCount() {
        return "select count(distinct uibm.c_name) from u_stockiest_item usi" +
                "            join u_item_mst  uim  on usi.c_ucode = uim.c_code" +
                "            join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code " +
                "            where usi.c_stockiest_code = :search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "            and uim.n_last_mrp > 0 "+
                "            Order By uibm.c_name ASC";
    }

    private String preferredSellerBrandSearchCount() {
        return "select count(distinct uibm.c_name) from u_stockiest_item usi" +
                "            join u_item_mst  uim  on usi.c_ucode = uim.c_code" +
                "            join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code and uibm.c_name like :insearch " +
                "            where usi.c_stockiest_code = :search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "            and uim.n_last_mrp > 0 "+
                "            Order By uibm.c_name ASC";
    }

    private String preferredSellerBrand() {
        return "select distinct uibm.c_code, uibm.c_name from u_stockiest_item usi" +
                "            join u_item_mst  uim  on usi.c_ucode = uim.c_code" +
                "            join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code " +
                "            where usi.c_stockiest_code = :search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "            and uim.n_last_mrp > 0 "+
                "            Order By uibm.c_name ASC";
    }

    private String preferredSellerSearchBrand() {
        return "select distinct uibm.c_code, uibm.c_name from u_stockiest_item usi" +
                "            join u_item_mst  uim  on usi.c_ucode = uim.c_code" +
                "            join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code and uibm.c_name like :insearch " +
                "            where usi.c_stockiest_code = :search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "            and uim.n_last_mrp > 0 "+
                "            Order By uibm.c_name ASC";
    }

    private String newLaunchSellers() {
        return "select distinct lcm.c_code, lcm.c_name from u_stockiest_item usi " +
                "  join u_item_mst uim on usi.c_ucode = uim.c_code " +
                "  join lc_c2code_mst lcm on usi.c_stockiest_code = lcm.c_code " +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "  order by lcm.c_name asc";
    }

    private String newLaunchSearchSellers() {

        return "select distinct lcm.c_code, lcm.c_name from u_stockiest_item usi " +
                "  join u_item_mst uim on usi.c_ucode = uim.c_code " +
                "  join lc_c2code_mst lcm on usi.c_stockiest_code = lcm.c_code and lcm.c_name like :search "+
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "  order by lcm.c_name asc";
    }

    private String newLaunchSellersCount() {
        return "select count(distinct lcm.c_code) from u_stockiest_item usi " +
                "  join u_item_mst uim on usi.c_ucode = uim.c_code " +
                "  join lc_c2code_mst lcm on usi.c_stockiest_code = lcm.c_code " +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "   order by lcm.c_name asc";
    }

    private String newLaunchSearchSellersCount() {
        return "select count(distinct lcm.c_code) from u_stockiest_item usi " +
                "  join u_item_mst uim on usi.c_ucode = uim.c_code " +
                "  join lc_c2code_mst lcm on usi.c_stockiest_code = lcm.c_code and lcm.c_name like :search " +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "   order by lcm.c_name asc";
    }

    private String newLaunchMfc() {
        return "select distinct uimm.c_code, uimm.c_name from u_item_mst uim  " +
                "  join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code  " +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "  order by uimm.c_name asc";
    }

    private String newLaunchSearchMfc() {
        return "select distinct uimm.c_code, uimm.c_name from u_item_mst uim  " +
                "  join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code  " +
                "  and uimm.c_name like :search " +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "  order by uimm.c_name asc";
    }

    private String newLaunchMfcCount() {
        return "select count(distinct uimm.c_code) from u_item_mst uim  " +
                "  join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code  " +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "  order by uimm.c_name asc";
    }

    private String newLaunchSearchMfcCount() {
        return "select count(distinct uimm.c_code) from u_item_mst uim  " +
                "  join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code  " +
                "  and uimm.c_name like :search " +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "  order by uimm.c_name asc";
    }

    private String newLaunchBrand() {
        return "select distinct uibm.c_code, uibm.c_name from u_item_mst uim  " +
                "  join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code " +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "  order by uibm.c_name asc ";
    }

    private String newLaunchSearchBrand() {
        return "select distinct uibm.c_code, uibm.c_name from u_item_mst uim  " +
                "  join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code and uibm.c_name like :search " +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "  order by uibm.c_name asc ";
    }

    private String newLaunchBrandCount() {
        return "select count(distinct uibm.c_code) from u_item_mst uim  " +
                "  join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code " +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "  order by uibm.c_name asc ";

    }

    private String newLaunchSearchBrandCount() {
        return "select count(distinct uibm.c_code) from u_item_mst uim  " +
                "  join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code and uibm.c_name like :search " +
                "  where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                "  order by uibm.c_name asc ";

    }

    private String preferredSellerSearchMfc() {
        return "select distinct uimm.c_code, uimm.c_name from u_stockiest_item usi " +
                "  join u_item_mst uim on usi.c_ucode = uim.c_code " +
                "  join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code and uimm.c_name like :insearch " +
                "  where usi.c_stockiest_code = :search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                "  ORDER BY uimm.c_name ASC ";
    }

    private String preferredSellerMfc() {
        return "select distinct uimm.c_code, uimm.c_name from u_stockiest_item usi " +
                "  join u_item_mst uim on usi.c_ucode = uim.c_code " +
                "  join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code" +
                "  where usi.c_stockiest_code = :search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                "  ORDER BY uimm.c_name ASC ";
    }

    private String preferredSellerMfcSearchCount() {
        return "select COUNT(distinct uimm.c_name) from u_stockiest_item usi " +
                "  join u_item_mst uim on usi.c_ucode = uim.c_code " +
                "  join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code and uimm.c_name like :insearch " +
                "  where usi.c_stockiest_code = :search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                "  ORDER BY uimm.c_name ASC ";
    }

    private String preferredSellerMfcCount() {
        return "select COUNT(distinct uimm.c_name) from u_stockiest_item usi " +
                "  join u_item_mst uim on usi.c_ucode = uim.c_code " +
                "  join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code" +
                "  where usi.c_stockiest_code = :search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                "  ORDER BY uimm.c_name ASC ";
    }

    private JsonArray lcItemToJsonArray(LcHeaderBO headerBO, List<LcItem> lcItems) {
        JsonArray jsonArray = new JsonArray();
        for (LcItem lcItem : lcItems) {
            JsonObject jsonObject = new JsonObject();
            String itemCode = lcItem.getItemCode();
            jsonObject.addProperty("c_item_code", itemCode);
            jsonObject.addProperty("c_item_name", lcItem.getItemName());
            jsonObject.addProperty("c_item_mfg_code", lcItem.getMfgName());
            if (lcItem.getThumbnailImages() != null && !lcItem.getThumbnailImages().isEmpty())
                jsonObject.addProperty("ac_thumbnail_images", lcItem.getThumbnailImages().get(0));
            else
                jsonObject.addProperty("ac_thumbnail_images", "");
            jsonObject.addProperty("c_item_mfg_name", lcItem.getMfgName());
            jsonObject.addProperty("n_qty_per_box", lcItem.getPackSize());
            jsonObject.addProperty("n_mrp", lcItem.getMrp());
            jsonObject.addProperty("c_pack_name", lcItem.getPackName());
            jsonObject.addProperty("c_contain_name", lcItem.getContains());
            jsonObject.addProperty("c_discount_status", Constants.STATUS_NO);
           /* jsonObject.addProperty("c_watchlist_status", shortBookWatchListService.isWatchList(headerBO.getUserId(),
                    headerBO.getFirmId(), headerBO.getBranchId(), itemCode));
            jsonObject.addProperty("c_short_book_status", shortBookWatchListService.isShortBook(headerBO.getUserId(),
                    headerBO.getFirmId(), headerBO.getBranchId(), itemCode));*/

            jsonObject.addProperty("c_watchlist_status", Constants.STATUS_NO);
            jsonObject.addProperty("c_short_book_status", Constants.STATUS_NO);
            jsonObject.addProperty("c_variant_count", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_cart_status", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_sponsored", Constants.STATUS_NO); //TODO user query to update

            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public JsonArray topMostOrderFilter(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws CommunicationErrorException, InvalidRequestException, RecordNotFoundException {
        JsonArray arr = new JsonArray();

        String state = catalogueService.getFirmState(lcHeader.getFirmId());
        if (!helper.isEmpty(state)) {
            String sql = getTopMostOrderFilterQuery(lcHeader,searchBO);

            Query query = this.getQuery(sql);
            query.setParameter("state", state);
            setCriteria(searchBO.getManufacturers(), searchBO.getBrands(), searchBO.getSellers(), query);
           // searchBO.setLimit(100);
            List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
            if (resultList == null) {
                return arr;
            }

            for (Object[] objects : resultList) {
                int i = -1;
                JsonObject jsonObject = new JsonObject();
                JsonArray jsonArray1 = new JsonArray();
                JsonObject jobj = new JsonObject();
                String itemCode = helper.getString(objects[++i]);
                jsonObject.addProperty("c_item_code", itemCode);
                jsonObject.addProperty("c_item_name", helper.getString(objects[++i]));
                jsonObject.addProperty("c_pack_name", helper.getString(objects[++i]));
                jsonObject.addProperty("c_contains", helper.getString(objects[++i]));
                jsonObject.addProperty("n_mrp", Double.parseDouble(objects[++i].toString()));
                jsonObject.addProperty("c_scheme",helper.getString(objects[++i]));
                jobj.addProperty("c_thumbnail_image", helper.getString(objects[++i]));
                jsonArray1.add(jobj);
                jsonObject.add("ac_thumbnail_images", jsonArray1);
                jsonObject.addProperty("c_pack_type_name",helper.getString(objects[++i]));
                jsonObject.addProperty("c_discount_status", Constants.STATUS_NO); //TODO user query to update
                jsonObject.addProperty("c_watchlist_status", Constants.STATUS_NO);
                jsonObject.addProperty("c_short_book_status", Constants.STATUS_NO);
                arr.add(jsonObject);
            }
        }
        return arr;
    }

    private void setCriteria(List<String> mfcList, List<String> brandList, List<String> sellerList, Query query) {
        if (!mfcList.isEmpty()) {
            query.setParameter("searchMfc", mfcList);
        }
        if (!brandList.isEmpty()) {
            query.setParameter("searchBrand", brandList);
        }
        if (!sellerList.isEmpty()) {
            query.setParameter("searchSeller", sellerList);
        }
    }

    private String getTopMostOrderFilterQuery(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        String sql = "SELECT distinct usfi.c_ucode, uim.c_name as itemName, uipm.c_name as packName, uicm.c_name as contentName," +
                " uim.n_last_mrp as Mrp,sch.c_scheme as scheme, uim.c_web_img_link as acThumbnailImage, " +
                " uiptm.c_name as packTypeName "+
                " FROM u_statewise_fastmoving_items usfi " +
                " JOIN u_item_mst uim ON uim.c_code = usfi.c_ucode" +
                " JOIN u_item_pack_mst uipm ON uim.c_item_pack_code = uipm.c_code " +
                " JOIN u_item_cont_mst uicm ON uim.c_item_cont_code = uicm.c_code "+
                " JOIN u_item_pack_type_mst uiptm ON uim.c_pack_type_code = uiptm.c_code  " +
                " JOIN u_item_mfac_mst uimm ON uim.c_item_mfac_code = uimm.c_code ";
        if (!searchBO.getManufacturers().isEmpty()) {
            sql += " AND uimm.c_code IN :searchMfc ";
        }
        if (!searchBO.getBrands().isEmpty()) {
            sql += " JOIN u_item_brand_mst uibm ON uim.c_item_brand_code = uibm.c_code" +
                    " AND uibm.c_code IN :searchBrand ";
        }
        if (!searchBO.getSellers().isEmpty()) {
            sql += " join " +
                    " (select usi.c_ucode, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, coalesce(sch.n_sch_ratio, 0) as n_sch_ratio, coalesce(sch.n_sch_order, 9999999) as n_sch_order, sch.c_scheme, " +
                    "  row_number() over(partition by usi.c_ucode order by sch.n_sch_ratio desc, coalesce(sch.n_sch_order, 9999999) asc) as rowid " +
                    "  from u_stockiest_item usi " +
                    "  left join (" + getSellerCodeAndBuyerCodeQuery(header) +") t on t.seller_code = usi.c_stockiest_code " +
                    "  left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code " +
                    "  left join cust_category_mst ccm on ccm.c_c2code = ac.c_c2code and ccm.c_code = ac.c_cust_category_code " +
                    "  join lc_c2code_mst lccm on lccm.c_code = usi.c_stockiest_code " +
                    "  and lccm.c_code in :searchSeller ";
        }
        else {
            sql += " left join " +
                    " (select usi.c_ucode, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, coalesce(sch.n_sch_ratio, 0) as n_sch_ratio, coalesce(sch.n_sch_order, 9999999) as n_sch_order, sch.c_scheme, " +
                    " row_number() over(partition by usi.c_ucode order by sch.n_sch_ratio desc, coalesce(sch.n_sch_order, 9999999) asc) as rowid " +
                    " from u_stockiest_item usi " +
                    " join (" + getSellerCodeAndBuyerCodeQuery(header) + ") t on t.seller_code = usi.c_stockiest_code " +
                    " left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code " +
                    " left join cust_category_mst ccm on ccm.c_c2code = ac.c_c2code and ccm.c_code = ac.c_cust_category_code " +
                    " join lc_c2code_mst lccm on lccm.c_code = usi.c_stockiest_code  ";
        }
        sql += " left join cust_branch_item_stock cbis on cbis.c_c2code = usi.c_stockiest_code and cbis.c_item_code = usi.c_stockiest_item_code and cbis.c_br_code = lccm.c_cust_branch_code   " +
                " left join lo_view_seller_cat_item_scheme sch   " +
                " on sch.c_c2code = usi.c_stockiest_code  " +
                " and sch.c_cust_cat_code = if(trim(coalesce(ccm.c_sch_category_code, '')) = '', coalesce(ccm.c_code, '-'), ccm.c_sch_category_code)  " +
                " and sch.c_item_code = usi.c_stockiest_item_code ) sch on sch.c_ucode = uim.c_code and sch.rowid = 1 "+
                " WHERE uim.n_active > 0 and usfi.c_state_code = :state " +
                " and uim.n_last_mrp > 0 "+
                " and uim.c_gst_code is not null ";
        if (searchBO.getAvailability().equals(Constants.STATUS_YES)) {
            sql += " and coalesce(sch.n_bal_qty, 0) > 0 ";
        }
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_NEWEST)) {
            sql += " ORDER BY uim.d_adate DESC";
        } else if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_RELEVANCE)) {
            //sql += " ORDER BY coalesce(fm.n_count, 0) DESC";
            sql += " order by usfi.n_count desc ";
        }
        if(searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_SCHEME))
        {
            sql += " and sch.c_scheme is not null "+
                    " order by" +
                    " coalesce(sch.n_sch_ratio, 0) desc, coalesce(sch.n_sch_order, 999999), " +
                    " coalesce(usfi.n_count, 0) desc ";
        }
        return sql;
    }

    @Override
    public long topMostOrderCount(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws CommunicationErrorException, InvalidRequestException, RecordNotFoundException {
        BigInteger count = BigInteger.ZERO;
        String state = catalogueService.getFirmState(lcHeader.getFirmId());

        if (!helper.isEmpty(state)) {
            String sql = getTopMostOrderFilterQuery(lcHeader, searchBO);

            Query query = this.getQuery("SELECT COUNT(*) FROM (" + sql + " LIMIT 100) DUMMY ");
            query.setParameter("state", state);
            setCriteria(searchBO.getManufacturers(), searchBO.getBrands(),searchBO.getSellers(), query);
            Object result = this.getSingleResult(query);

            if (result != null) {
                count = (BigInteger) result;
            }
        }
        return count.intValue();
    }

    @Override
    public List<JsonObject> preferredSellerFilter(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        String sql = getPreferredSellerPLP(lcHeader,searchBO);
        Query query = this.getQuery(sql);
        query.setParameter("search", searchBO.getSearchTerm());
        if(!searchBO.getProductName().isEmpty()){
            query.setParameter("product_search", searchBO.getProductName() + "%");
        }
        setPreferredSellerCriteria(searchBO.getManufacturers(), searchBO.getBrands(), query);
        List<JsonObject> list = new ArrayList<>();
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return list;
            //throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }

        for (Object[] objects : resultList) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray1 = new JsonArray();
            JsonObject jobj = new JsonObject();
            String itemCode = helper.getString(objects[++i]);
            jsonObject.addProperty("c_item_code", itemCode);
            jsonObject.addProperty("c_item_name", helper.getString(objects[++i]));
            jsonObject.addProperty("n_mrp", Double.parseDouble(objects[++i].toString()));
            jsonObject.addProperty("c_pack_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_contains", helper.getString(objects[++i]));
            jobj.addProperty("c_thumbnail_image", helper.getString(objects[++i]));
            jsonArray1.add(jobj);
            jsonObject.add("ac_thumbnail_images", jsonArray1);
            jsonObject.addProperty("c_item_mfg_code", helper.getString(objects[++i]));
            jsonObject.addProperty("c_item_mfg_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_item_ucode", helper.getString(objects[++i]));
            jsonObject.addProperty("c_scheme",helper.getString(objects[++i]));
            jsonObject.addProperty("c_pack_type_name",helper.getString(objects[++i]));
            jsonObject.addProperty("c_discount_status", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_watchlist_status", Constants.STATUS_NO);
            jsonObject.addProperty("c_short_book_status", Constants.STATUS_NO);
            jsonObject.addProperty("c_variant_count", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_cart_status", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_sponsored", Constants.STATUS_NO);
            list.add(jsonObject);
        }
        return list;
    }

    private void setPreferredSellerCriteria(List<String> manufacturers, List<String> brands, Query query) {
        if (!manufacturers.isEmpty()) {
            query.setParameter("searchMfc", manufacturers);
        }
        if (!brands.isEmpty()) {
            query.setParameter("searchBrand", brands);
        }
    }

    private String getPreferredSellerPLP(LcHeaderBO header, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        String sql = "SELECT distinct cust.c_code itemCode, coalesce(cust.c_name, uim.c_name) itemName, coalesce(cbis.n_rate, uim.n_last_mrp) itemMrp,   " +
                "  coalesce(uipm.c_name, cpm.c_name) itemPackName,  " +
                "  coalesce(uicm.c_name, ccm.c_name) itemContentName, coalesce(uim.c_web_img_link, cust.c_web_img_link) as c_web_img_link,  " +
                "  coalesce(uim.c_item_mfac_code, cust.c_mfac_code) mfcCode, coalesce(uimm.c_name, cmm.c_name) mfcName,  " +
                "  uim.c_code as c_ucode ,  " +
                "  sch.c_scheme as scheme, "+
                " uiptm.c_name as packTypeName "+
                " FROM cust_item_mst cust " +
                " JOIN u_stockiest_item usi on cust.c_c2code = usi.c_stockiest_code and cust.c_code = usi.c_stockiest_item_code " +
                " JOIN u_item_mst uim ON uim.c_code = usi.c_ucode " +
                " JOIN u_item_cont_mst uicm on uim.c_item_cont_code = uicm.c_code   " +
                " JOIN cust_pack_mst cpm ON cpm.c_code = cust.c_pack_code and cpm.c_c2code = cust.c_c2code  " +
                " JOIN cust_mfac_mst cmm ON cmm.c_code = cust.c_mfac_code and cmm.c_c2code = cust.c_c2code  " +
                " JOIN cust_cont_mst ccm ON ccm.c_code = cust.c_cont_code and ccm.c_c2code = cust.c_c2code " +
                " JOIN u_item_pack_mst uipm on uim.c_item_pack_code = uipm.c_code    " +
                " join u_item_pack_type_mst uiptm on uim.c_pack_type_code = uiptm.c_code "+
                " JOIN u_item_mfac_mst uimm ON uim.c_item_mfac_code = uimm.c_code ";

        if (!searchBO.getManufacturers().isEmpty()) {
            sql += " AND uimm.c_code IN :searchMfc ";
        }
        if (!searchBO.getBrands().isEmpty()) {
            sql += " JOIN u_item_brand_mst uibm ON uim.c_item_brand_code = uibm.c_code" +
                    " AND uibm.c_code IN :searchBrand ";
        }
        sql += " LEFT JOIN cust_branch_item_stock cbis ON usi.c_stockiest_code = cbis.c_c2code  " +
                " AND usi.c_stockiest_item_code = cbis.c_item_code  " +
                " LEFT JOIN u_fastmoving_items fm on uim.c_code = fm.c_ucode " +
                " left join ("+getSellerCodeAndBuyerCodeQuery(header)+") t on t.seller_code = usi.c_stockiest_code  " +
                " left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code  " +
                " left join cust_category_mst ccm1 on ccm1.c_c2code = ac.c_c2code and ccm1.c_code = ac.c_cust_category_code  " +
                " left join lo_view_seller_cat_item_scheme sch  " +
                "   on sch.c_c2code = ac.c_c2code  " +
                "     and sch.c_cust_cat_code = if(trim(coalesce(ccm1.c_sch_category_code, '')) = '', coalesce(ccm1.c_code, 'RETAIL'), ccm1.c_sch_category_code) " +
                "     and sch.c_item_code = usi.c_stockiest_item_code "+
                "  WHERE uim.n_active > 0 and usi.c_stockiest_code = :search " +
                " and uim.n_last_mrp > 0 "+
                " AND uim.c_gst_code is not null "+
                " and left(cust.c_name, 1) not in ('.', '$', '#', '*', '~') "+
                " and cust.n_active = 1  ";
        if (searchBO.getAvailability().equals(Constants.STATUS_YES)) {
            sql += " AND coalesce(cbis.n_bal_qty, 0) > 0";
        }
        if(!searchBO.getProductName().isEmpty()){
            sql += " AND uim.c_name like :product_search ";
        }
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_NEWEST)) {
            sql += " ORDER BY uim.d_adate DESC";
        } else if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_RELEVANCE)) {
            sql += " ORDER BY coalesce(fm.n_count, 0) DESC";
        }
        if(searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_SCHEME))
        {
            sql += " and sch.c_scheme is not null "+
                    " order by" +
                    " coalesce(sch.n_sch_ratio, 0) desc, coalesce(sch.n_sch_order, 999999), " +
                    " coalesce(fm.n_count, 0) desc ";
        }
        return sql;
    }

    @Override
    public long preferredSellerCount(LcHeaderBO lcHeader, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        BigInteger count = BigInteger.ZERO;
        String sql = getPreferredSellerPLP(lcHeader, searchBO);
        Query query = this.getQuery("SELECT COUNT(*) FROM (" + sql + ") DUMMY");
        query.setParameter("search", searchBO.getSearchTerm());
        if(!searchBO.getProductName().isEmpty()) {
            query.setParameter("product_search", searchBO.getProductName() + "%");
        }
        setPreferredSellerCriteria(searchBO.getManufacturers(), searchBO.getBrands(), query);
        Object result = this.getSingleResult(query);

        if (result != null) {
            count = (BigInteger) result;
        }

        return count.intValue();
    }

    @Override
    public JsonArray categoryMfcList(SearchBO searchBO) {
        String sql = null;
        Query query = null;
        JsonArray jsonArray = new JsonArray();
        if (!helper.isEmpty(searchBO.getInSearchTerm()) && (!helper.isEmpty(searchBO.getSearchTerm()))) {
            sql = categoryMfcInSearch();
            query = this.getQuery(sql);
            query.setParameter("inSearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());
        } else if (helper.isEmpty(searchBO.getInSearchTerm()) && (!helper.isEmpty(searchBO.getSearchTerm()))) {
            sql = categoryMfcSearch();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
        } else {
            sql = categoryMfc(searchBO);
            query = this.getQuery(sql);
            if (searchBO.getInSearchTerm() != null) {
                query.setParameter("insearch", searchBO.getInSearchTerm() +"%");
            }
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_mfc_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_mfc_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String categoryMfc(SearchBO searchBO) {
        String sql = "   select distinct uimm.c_code, uimm.c_name from u_item_mfac_mst uimm " +
                " join u_item_mst uim on uim.c_item_mfac_code = uimm.c_code " +
                " join u_item_cat_mst uicm on uicm.c_code = uim.c_item_cat_code ";
        if (searchBO.getInSearchTerm() != null){
            sql += "and uimm.c_name LIKE :insearch ";
        }
            sql += " where uim.n_active > 0 and uim.c_gst_code is not null " +
                    "  and uim.n_last_mrp > 0 "+
                    " order by  uimm.c_name asc";
        return sql;
    }

    @Override
    public long categoryMfcCount(SearchBO searchBO) {
        String sql;
        Query query;
        if (!helper.isEmpty(searchBO.getInSearchTerm()) && (!helper.isEmpty(searchBO.getSearchTerm()))) {
            sql = "SELECT COUNT(*) FROM (" + categoryMfcInSearch() + ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("inSearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());
        } else if (helper.isEmpty(searchBO.getInSearchTerm()) && (!helper.isEmpty(searchBO.getSearchTerm()))) {
            sql = "SELECT COUNT(*) FROM (" + categoryMfcSearch() + ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
        } else {
            sql = "SELECT COUNT(*) FROM (" + categoryMfc(searchBO) + ") DUMMY";
            query = this.getQuery(sql);
            if (searchBO.getInSearchTerm() != null) {
                query.setParameter("insearch", searchBO.getInSearchTerm() + "%");
            }
        }
        Object result = this.getSingleResult(query);
        assert result != null;
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray categoryBrandList(SearchBO searchBO) {
        String sql = null;
        Query query = null;
        JsonArray jsonArray = new JsonArray();
        if (!helper.isEmpty(searchBO.getInSearchTerm()) && (!helper.isEmpty(searchBO.getSearchTerm()))) {
            sql = categoryBrandInSearch();
            query = this.getQuery(sql);
            query.setParameter("inSearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());
        } else if (helper.isEmpty(searchBO.getInSearchTerm()) && (!helper.isEmpty(searchBO.getSearchTerm()))){
            sql = categoryBrandSearch();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
        }
        else{
            sql = categoryBrand(searchBO);
            query = this.getQuery(sql);
            if (searchBO.getInSearchTerm() != null) {
                query.setParameter("insearch", searchBO.getInSearchTerm() +"%");
            }
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_brand_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_brand_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String categoryBrand(SearchBO searchBO) {
        String sql = "select distinct uibm.c_code, uibm.c_name from u_item_brand_mst uibm " +
                " join u_item_mst uim on uim.c_item_brand_code = uibm.c_code " +
                " join u_item_cat_mst uicm on uicm.c_code = uim.c_item_cat_code ";
        if (searchBO.getInSearchTerm() != null) {
            sql += " and uibm.c_name like :insearch ";
        }
        sql += " where uim.n_active > 0 and uim.c_gst_code is not null " +
                " and uim.n_last_mrp > 0 "+
                " order by  uibm.c_name asc";
        return sql;
    }

    @Override
    public long categoryBrandCount(SearchBO searchBO) {
        String sql;
        Query query;
        if (!helper.isEmpty(searchBO.getInSearchTerm()) && (!helper.isEmpty(searchBO.getSearchTerm()))) {
            sql = "SELECT COUNT(*) FROM (" + categoryBrandInSearch() + ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("inSearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());
        } else if (helper.isEmpty(searchBO.getInSearchTerm()) && (!helper.isEmpty(searchBO.getSearchTerm()))) {
            sql = "SELECT COUNT(*) FROM (" + categoryBrandSearch() + ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
        } else {
            sql = "SELECT COUNT(*) FROM (" + categoryBrand(searchBO) + ") DUMMY";
            query = this.getQuery(sql);
            if (searchBO.getInSearchTerm() != null) {
                query.setParameter("insearch", searchBO.getInSearchTerm() + "%");
            }
        }
        Object result = this.getSingleResult(query);
        assert result != null;
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray categorySellerList(SearchBO searchBO) {
        String sql = null;
        Query query = null;
        JsonArray jsonArray = new JsonArray();
        if (!helper.isEmpty(searchBO.getInSearchTerm()) && (!helper.isEmpty(searchBO.getSearchTerm()))) {
            sql = categorySellerInSearch();
            query = this.getQuery(sql);
            query.setParameter("inSearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());
           // query.setParameter("mobile",mobile_number);

        } else if (helper.isEmpty(searchBO.getInSearchTerm()) && (!helper.isEmpty(searchBO.getSearchTerm()))) {
            sql = categorySellerSearch();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
           // query.setParameter("mobile",mobile_number);
        }
        else {
            sql = categorySeller(searchBO);
            query = this.getQuery(sql);
           // query.setParameter("mobile",mobile_number);
            if (searchBO.getInSearchTerm() != null) {
                query.setParameter("insearch", searchBO.getInSearchTerm() + "%");
            }
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
        }
        for (Object[] objects : resultList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_seller_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_seller_name", helper.getString(objects[1]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String categorySeller(SearchBO searchBO) {
        String sql = " select distinct lccm.c_code, lccm.c_name from lc_c2code_mst lccm  " +
                " join u_stockiest_item usi on usi.c_stockiest_code = lccm.c_code  " +
                " join u_item_mst uim on uim.c_code = usi.c_ucode  " +
                " join u_item_cat_mst uicm on uim.c_item_cat_code = uicm.c_code " +
                " join u_item_cat_head_mst uichm on uicm.c_item_category_head_code = uichm.c_code " +
                " Where uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 ";
        if (searchBO.getInSearchTerm() != null) {
            sql += "and lccm.c_name like :insearch ";
        }
        sql += "order by lccm.c_name ASC ";
        return sql;
    }

    @Override
    public long categorySellerCount(SearchBO searchBO) {
        String sql;
        Query query;
        if (!helper.isEmpty(searchBO.getInSearchTerm()) && (!helper.isEmpty(searchBO.getSearchTerm()))) {
            sql = "SELECT COUNT(*) FROM (" +categorySellerInSearch()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("inSearch", searchBO.getInSearchTerm() + "%");
            query.setParameter("search", searchBO.getSearchTerm());
            //query.setParameter("mobile",mobile_number);
        } else if (helper.isEmpty(searchBO.getInSearchTerm()) && (!helper.isEmpty(searchBO.getSearchTerm()))) {
            sql = "SELECT COUNT(*) FROM (" +categorySellerSearch()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
            //query.setParameter("mobile",mobile_number);
        }else{
            sql = "SELECT COUNT(*) FROM (" +categorySeller(searchBO)+ ") DUMMY";
            query = this.getQuery(sql);
           // query.setParameter("mobile",mobile_number);
            if (searchBO.getInSearchTerm() != null) {
                query.setParameter("insearch", searchBO.getInSearchTerm() + "%");
            }
        }
        Object result = this.getSingleResult(query);
        assert result != null;
        return new BigInteger(String.valueOf(result)).longValue();
    }

    private String categorySellerSearch() {
        return " select distinct lccm.c_code, lccm.c_name from lc_c2code_mst lccm  " +
                " join u_stockiest_item usi on usi.c_stockiest_code = lccm.c_code  " +
                " join u_item_mst uim on uim.c_code = usi.c_ucode  " +
                " join u_item_cat_mst uicm on uim.c_item_cat_code = uicm.c_code " +
                " join u_item_cat_head_mst uichm on uicm.c_item_category_head_code = uichm.c_code " +
                " Where uichm.c_item_category_class_code = :search " +
                " and uim.n_active > 0 and uim.c_gst_code is not null "+
                "  and uim.n_last_mrp > 0 "+
                " order by lccm.c_name asc ";
    }

    private String categorySellerInSearch() {
        return " select distinct lccm.c_code, lccm.c_name from lc_c2code_mst lccm  " +
                " join u_stockiest_item usi on usi.c_stockiest_code = lccm.c_code  " +
                " join u_item_mst uim on uim.c_code = usi.c_ucode  " +
                " join u_item_cat_mst uicm on uim.c_item_cat_code = uicm.c_code " +
                " join u_item_cat_head_mst uichm on uicm.c_item_category_head_code = uichm.c_code and lccm.c_name like :inSearch " +
                " Where uichm.c_item_category_class_code = :search " +
                " and uim.n_active > 0 and uim.c_gst_code is not null  " +
                "  and uim.n_last_mrp > 0 "+
                " order by lccm.c_name asc";
    }

    private String categoryBrandSearch() {
        return "select distinct uibm.c_code, uibm.c_name from u_item_mst uim  " +
                " join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code " +
                " join u_item_cat_mst uicm on uim.c_item_cat_code = uicm.c_code " +
                " join u_item_cat_head_mst uichm on uicm.c_item_category_head_code = uichm.c_code " +
                " where uichm.c_item_category_class_code = :search and uim.n_active > 0 and uim.c_gst_code is not null " +
                " and uim.n_last_mrp > 0 "+
                " order by  uibm.c_name asc ";
    }

    private String categoryBrandInSearch() {
        return "select distinct uibm.c_code, uibm.c_name from u_item_mst uim  " +
                " join u_item_brand_mst uibm on uim.c_item_brand_code = uibm.c_code " +
                " join u_item_cat_mst uicm on uim.c_item_cat_code = uicm.c_code " +
                " join u_item_cat_head_mst uichm on uicm.c_item_category_head_code = uichm.c_code and uibm.c_name like :inSearch " +
                " where uichm.c_item_category_class_code = :search and uim.n_active > 0 and uim.c_gst_code is not null " +
                " and uim.n_last_mrp > 0 "+
                " order by  uibm.c_name asc ";
    }

    private String categoryMfcSearch() {
        return "select distinct uimm.c_code, uimm.c_name from u_item_mst uim  " +
                " join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code " +
                " join u_item_cat_mst uicm on uim.c_item_cat_code = uicm.c_code " +
                " join u_item_cat_head_mst uichm on uicm.c_item_category_head_code = uichm.c_code " +
                " Where uichm.c_item_category_class_code = :search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                " order by  uimm.c_name asc ";
    }

    private String categoryMfcInSearch() {
        return "select distinct uimm.c_code, uimm.c_name from u_item_mst uim  " +
                " join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code " +
                " join u_item_cat_mst uicm on uim.c_item_cat_code = uicm.c_code " +
                " join u_item_cat_head_mst uichm on uicm.c_item_category_head_code = uichm.c_code and uimm.c_name like :inSearch " +
                " Where uichm.c_item_category_class_code = :search and uim.n_active > 0 and uim.c_gst_code is not null " +
                "  and uim.n_last_mrp > 0 "+
                " order by  uimm.c_name asc ";
    }

    @Override
    public List<Object[]> prdFilter(LcHeaderBO header, SearchBO searchBO) {
        List<String> itemList = new ArrayList<>();
        String sql = getPrdFilterQuery(searchBO);
        Query query = this.getQuery(sql);
        query.setParameter("searchTerm", helper.getLikeQueryString(searchBO.getSearchTerm()));
        query.setParameter("c2Code", header.getC2Code());
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_DISCOUNT)||searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_DISCOUNT)){
            query.setParameter("start_today", helper.getCurrentDateTime("YYYY-MM-dd HH:mm:ss"));
            query.setParameter("start_today_date", helper.getString(helper.getCurrentDateTime("YYYY-MM-dd")) + '%');
        }
        return this.getResultList(query, searchBO.getPage(), searchBO.getLimit());

//        if (!resultList.isEmpty()) {
//            for (int i = 0; i < resultList.size(); i++) {
//                itemList.add(helper.getString(resultList.get(i)));
//            }
//        }
//        return itemList;
    }

    private String getPrdFilterQuery(SearchBO searchBO) {
        String sql = "SELECT usi.c_ucode, cbis.n_rate, usi.c_stockiest_item_code " +
                " FROM cust_item_mst cim " +
                " JOIN u_stockiest_item usi ON usi.c_stockiest_code = cim.c_c2code AND usi.c_stockiest_item_code = cim.c_code  " +
                " LEFT JOIN u_fastmoving_items fm ON usi.c_ucode = fm.c_ucode  " +
                " LEFT JOIN cust_branch_item_stock cbis FORCE INDEX(PRIMARY) ON cbis.c_c2code = usi.c_stockiest_code and cbis.c_item_code = usi.c_stockiest_item_code  " +
                "INNER JOIN lc_c2code_mst lccm on lccm.c_code = cbis.c_c2code and lccm.c_cust_branch_code = cbis.c_br_code ";
        String sql1= " WHERE cim.c_name like :searchTerm AND cim.c_c2code = :c2Code  ";
        //" GROUP BY usi.c_ucode ";
       /* if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_RELEVANCE)||searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_PRICE)||searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_PRICE)){
            sql +=" LEFT join deal_of_the_day dotd on dotd.c_c2code = cbis.c_c2code and dotd.c_item_code = cbis.c_item_code " +
                    " INNER JOIN lc_c2code_mst lccm on lccm.c_code = cbis.c_c2code and lccm.c_cust_branch_code = cbis.c_br_code "+
                    " WHERE cim.c_name like :searchTerm AND cim.c_c2code = :c2Code  ";
        }else{*/
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_DISCOUNT)||searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_DISCOUNT)){
            sql +=" INNER join deal_of_the_day dotd on dotd.c_c2code = cbis.c_c2code and dotd.c_item_code = cbis.c_item_code ";
            sql1+= " AND dotd.c_deal_status = 'Y' and ((dotd.t_start_date <= :start_today_date or dotd.t_start_date like :start_today_date) and dotd.t_start_date <=:start_today and dotd.t_end_date >=:start_today) ";
        }
        sql+=sql1;
        sql = setSortQuery(searchBO, sql);
        return sql;
    }

    @Override
    public int prdFilterCount(LcHeaderBO lcHeaderBO, SearchBO searchBO) {
        BigInteger count = BigInteger.ZERO;
        String sql = "SELECT COUNT(*) FROM ( "+ getPrdFilterQuery(searchBO) + " ) DUMMY";
        Query query = this.getQuery(sql);
        query.setParameter("searchTerm", helper.getLikeQueryString(searchBO.getSearchTerm()));
        query.setParameter("c2Code", lcHeaderBO.getC2Code());
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_DISCOUNT)||searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_DISCOUNT)){
            query.setParameter("start_today", helper.getCurrentDateTime("YYYY-MM-dd HH:mm:ss"));
            query.setParameter("start_today_date", helper.getString(helper.getCurrentDateTime("YYYY-MM-dd")) + '%');
        }
        Object result = this.getSingleResult(query);

        if (result != null) {
            count = (BigInteger) result;
        }
        return count.intValue();
    }

    @Override
    public List<Object[]> trendingPrdFilter(String c2Code, SearchBO searchBO) {
        List<String> itemList = new ArrayList<>();
        String sql = getTrendingPrdPlpQuery(searchBO);
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", c2Code);
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_DISCOUNT)||searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_DISCOUNT)){
            query.setParameter("start_today", helper.getCurrentDateTime("YYYY-MM-dd HH:mm:ss"));
            query.setParameter("start_today_date", helper.getString(helper.getCurrentDateTime("YYYY-MM-dd")) + '%');
        }
        return this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
//
//        if (!resultList.isEmpty()) {
//            for (int i = 0; i < resultList.size(); i++) {
//                itemList.add(helper.getString(resultList.get(i)));
//            }
//        }
//        return itemList;
    }

    @Override
    public int trendingPrdPlpCount(String c2Code, SearchBO searchBO) {
        BigInteger count = BigInteger.ZERO;
        String sql = "SELECT COUNT(*) FROM ( " +getTrendingPrdPlpQuery(searchBO)+ ") DUMMY";
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", c2Code);
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_DISCOUNT)||searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_DISCOUNT)){
            query.setParameter("start_today", helper.getCurrentDateTime("YYYY-MM-dd HH:mm:ss"));
            query.setParameter("start_today_date", helper.getString(helper.getCurrentDateTime("YYYY-MM-dd")) + '%');
        }
        Object result = this.getSingleResult(query);

        if (result != null) {
            count = (BigInteger) result;
        }
        return count.intValue();
    }

    private String getTrendingPrdPlpQuery(SearchBO searchBO) {
        String sql =" SELECT usi.c_ucode, cbis.n_rate, usi.c_stockiest_item_code "+
                "FROM cust_popular_items cpi " +
                "JOIN u_stockiest_item usi on cpi.c_c2code = usi.c_stockiest_code and cpi.c_item_code = usi.c_stockiest_item_code " +
                " LEFT JOIN u_fastmoving_items fm on usi.c_ucode = fm.c_ucode "+
                " LEFT JOIN cust_branch_item_stock cbis FORCE INDEX(PRIMARY) on cbis.c_c2code = usi.c_stockiest_code and cbis.c_item_code = usi.c_stockiest_item_code  "+
                "INNER JOIN lc_c2code_mst lccm on lccm.c_code = cbis.c_c2code and lccm.c_cust_branch_code = cbis.c_br_code ";
        String sql1 = " WHERE cpi.c_c2code = :c2Code ";
        /*if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_RELEVANCE)||searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_PRICE)||searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_PRICE)){
            sql +=" LEFT join deal_of_the_day dotd on dotd.c_c2code = cbis.c_c2code and dotd.c_item_code = cbis.c_item_code " +
                    " INNER JOIN lc_c2code_mst lccm on lccm.c_code = cbis.c_c2code and lccm.c_cust_branch_code = cbis.c_br_code "+
                    " WHERE cpi.c_c2code = :c2Code ";
        }else{*/
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_DISCOUNT)||searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_DISCOUNT)){
            sql +=" INNER join deal_of_the_day dotd on dotd.c_c2code = cbis.c_c2code and dotd.c_item_code = cbis.c_item_code " ;
            sql1 +=  " and dotd.c_deal_status = 'Y' and ((dotd.t_start_date <= :start_today_date or dotd.t_start_date like :start_today_date) and dotd.t_start_date <=:start_today and dotd.t_end_date >=:start_today) ";
        }
        sql +=sql1;
        sql = setSortQuery(searchBO, sql);
        return sql;
    }

    private String setSortQuery(SearchBO searchBO, String sql) {
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_RELEVANCE)) {
            sql += " ORDER BY coalesce(fm.n_count, 0) DESC ";
        }
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_PRICE)) {
            sql += "ORDER BY coalesce(cbis.n_rate, 0) ASC ";
        }

        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_PRICE)) {
            sql += "ORDER BY coalesce(cbis.n_rate, 0) DESC ";
        }

        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_DISCOUNT)) {
            sql += " ORDER BY coalesce(dotd.n_deal_rate, 0) ASC ";
        }

        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_DISCOUNT)) {
            sql += " ORDER BY coalesce(dotd.n_deal_rate, 0) DESC  ";
        }
        return sql;
    }

    @Override
    public JsonArray dealOfTheDayBrand(String c2Code, SearchBO searchBO, JsonObject jsonObject) {
        JsonArray result = new JsonArray();
        JsonObject obj;
        String categoryCode = jsonObject.has("c_category_code") ? jsonObject.get("c_category_code").getAsString() : "";
        String sql = getDealBrands(c2Code, searchBO, categoryCode);
        Query query = this.getQuery(sql);
        List<Object[]> list = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());

        if (list.size() > 0) {
            for (Object[] object : list) {
                obj = new JsonObject();
                obj.addProperty("c_brand_code",helper.getString(object[0]));
                obj.addProperty("c_brand_name",helper.getString(object[1]));
                result.add(obj);
            }
        }
        return result;
    }

    @Override
    public JsonArray dealOfTheDayPrdForms(String c2Code, SearchBO searchBO, JsonObject jsonObject) {
        JsonArray result = new JsonArray();
        List<String> itemCodes = new ArrayList<>();
        JsonObject obj;
        String categoryCode = jsonObject.has("c_category_code") ? jsonObject.get("c_category_code").getAsString() : "";
        String sql = getPrdForms(c2Code, searchBO, categoryCode);
        Query query = this.getQuery(sql);
        List<Object[]> list = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                itemCodes.add(helper.getString(list.get(i)));
            }

            Criteria criteria;
            if (searchBO.getInSearchTerm() != null && searchBO.getInSearchTerm().length() > 3) {
                criteria = Criteria.where("_id").in(itemCodes).and("c_pack_type_name").regex(helper.getMongoSearchParameter(searchBO.getInSearchTerm()), "i");
            } else {
                criteria = Criteria.where("_id").in(itemCodes);
            }
            org.springframework.data.mongodb.core.query.Query mongoQuery = new org.springframework.data.mongodb.core.query.Query().addCriteria(criteria);
            mongoQuery.with(Sort.by(Sort.Direction.ASC, "c_pack_type_name"));
            List<LcItem> lcItems = mongoOperations.find(mongoQuery, LcItem.class);

            if (lcItems.size() > 0) {
                for (LcItem item : lcItems) {
                    obj = new JsonObject();
                    obj.addProperty("c_pdt_form_code", item.getPackTypeCode());
                    obj.addProperty("c_pdt_form_name", item.getPackTypeName());
                    result.add(obj);
                }
            }
        }
        return result;
    }

    @Override
    public JsonArray dealOfTheDayUses(String c2Code, SearchBO searchBO, JsonObject jsonObject) {
        return null;
    }

    private String getPrdForms(String c2Code, SearchBO searchBO, String categoryCode) {
        String sql = " SELECT DISTINCT dt.c_item_code FROM deal_of_the_day dt " +
                "   JOIN u_stockiest_item usi ON usi.c_stockiest_item_code = dt.c_item_code and usi.c_stockiest_code = dt.c_c2code " +
                "   JOIN u_item_mst uim ON usi.c_ucode = uim.c_code " +
                "       AND uim.n_active > 0 AND uim.c_gst_code IS NOT NULL AND uim.n_last_mrp > 0 ";
        if (searchBO.getSearchTerm() != null && searchBO.getSearchTerm().length() > 3) {
            sql += "    AND LOWER(uim.c_name) LIKE LOWER('"+searchBO.getSearchTerm()+"%')";
        }
        if (!helper.isEmpty(categoryCode)) {
            sql +=  "   JOIN cust_item_mst cim ON cim.c_code = usi.c_stockiest_item_code " +
                    "       AND cim.c_c2code = dt.c_c2code AND cim.c_cat_code = '"+categoryCode+"' ";
        }
        sql += "   WHERE dt.c_c2code = '"+c2Code+"'";
        return sql;
    }

    private String getDealBrands(String c2Code, SearchBO searchBO, String categoryCode) {
        String sql = "SELECT DISTINCT uibm.c_code, uibm.c_name FROM deal_of_the_day dt " +
                "   JOIN u_stockiest_item usi ON usi.c_stockiest_item_code = dt.c_item_code and usi.c_stockiest_code = dt.c_c2code " +
                "   JOIN u_item_mst uim ON usi.c_ucode = uim.c_code " +
                "       AND uim.n_active > 0 AND uim.c_gst_code IS NOT NULL AND uim.n_last_mrp > 0 ";
        if (searchBO.getSearchTerm() != null && searchBO.getSearchTerm().length() > 3) {
            sql += "    AND LOWER(uim.c_name) LIKE LOWER('"+searchBO.getSearchTerm()+"%')";
        }
        sql +=  "   JOIN u_item_brand_mst uibm ON uim.c_item_brand_code = uibm.c_code ";
        if (searchBO.getInSearchTerm() != null && searchBO.getInSearchTerm().length() > 3) {
            sql += "   AND uibm.c_name LIKE '"+searchBO.getInSearchTerm()+"%' ";
        }
        if (!helper.isEmpty(categoryCode)) {
            sql +=  "   JOIN cust_item_mst cim ON cim.c_code = usi.c_stockiest_item_code " +
                    "       AND cim.c_c2code = dt.c_c2code AND cim.c_cat_code = '"+categoryCode+"' ";
        }
        sql +=  "   WHERE dt.c_c2code = '"+c2Code+"'" +
                "   ORDER BY uibm.c_name ASC ";
        return sql;
    }
}
