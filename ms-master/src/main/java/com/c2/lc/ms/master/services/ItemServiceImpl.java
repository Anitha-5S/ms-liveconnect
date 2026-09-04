package com.c2.lc.ms.master.services;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.DataFormatException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.*;
import com.c2.lc.ms.master.bos.customerbos.*;
import com.c2.lc.ms.master.entities.mongo.LcItem;
import com.c2.lc.ms.master.entities.mongo.LcMolecule;
import com.c2.lc.ms.master.entities.mongo.MostViewedProduct;
import com.c2.lc.ms.master.entities.mysql.*;
import com.c2.lc.ms.master.models.ItemDetailModel;
import com.c2.lc.ms.master.models.ItemSummaryModel;
import com.c2.lc.ms.master.repos.mongo.*;
import com.c2.lc.ms.master.repos.mysql.*;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.*;
import com.c2.lc.ms.master.utils.BlobFolder;
import com.c2.lc.ms.master.utils.ItemsToNewLaunchOrderResponseMapper;
import com.c2.lc.ms.master.utils.ItemsToTopMostOrderResponseMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;
import com.mongodb.BasicDBObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Query;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.time.LocalDate;
import java.util.List;
import java.util.*;


@Slf4j
@Service
public class ItemServiceImpl extends MasterBaseServiceImpl implements ItemService {

    @Autowired private ItemImpl item;
    @Autowired private ItemRepository itemRepository;
    @Autowired private MongoOperations mongoOperations;
    @Autowired private ItemPackService itemPackService;
    @Autowired private ItemMfacService itemMfacService;
    @Autowired private ItemBrandService itemBrandService;
    @Autowired private LcItemRepository lcItemRepository;
    @Autowired private MoleculeRepository moleculeRepository;
    @Autowired private CatalogueServiceImpl catalogueService;
    @Autowired private ItemContentService itemContentService;
    @Autowired private UItemPriceRepository uItemPriceRepository;
    @Autowired private CustItemMstRepository custItemMstRepository;
    @Autowired private CustPackMstRepository custPackMstRepository;
    @Autowired private CustMfacMstRepository custMfacMstRepository;
    @Autowired private CustContMstRepository custContMstRepository;
    @Autowired private ItemSummaryRepository itemSummaryRepository;
    @Autowired private CustBrandMstRepository custBrandMstRepository;
    @Autowired private ItemDetailsMongoService itemDetailsMongoService;
    @Autowired private HsnGstMappingRepository hsnGstMappingRepository;
    @Autowired private UStockiestItemRepository stockiestItemRepository;
    @Autowired private CustPackTypeMstRepository custPackTypeMstRepository;
    @Autowired private CustScheduleMstRepository custScheduleMstRepository;
    @Autowired private ShortBookWatchListService shortBookWatchListService;
    @Autowired private CustItemGroupMstRepository custItemGroupMstRepository;
    @Autowired private LcShortBookMongoRepository lcShortBookMongoRepository;
    @Autowired private LcWatchListMongoRepository lcWatchListMongoRepository;
    @Autowired private LcCustItemRecommendationRepository topSellItemRepository;
    @Autowired private CustItemCategoryMstRepository custItemCategoryMstRepository;
    @Autowired private StatewiseFastmovingRepository statewiseFastmovingRepository;
    @Autowired private UItemMoleculeDetailsRepository uItemMoleculeDetailsRepository;
    @Autowired private ItemsToTopMostOrderResponseMapper itemsToTopMostOrderResponseMapper;
    @Autowired private ItemsToNewLaunchOrderResponseMapper itemsToNewLaunchOrderResponseMapper;
    @Autowired private DealOfTheDayServiceImpl dealOfTheDayService;
    @Autowired private FilterService filterService;
    @Autowired private MongoTemplate mongoTemplate;
    @Autowired private MostViewedProductRepository mostViewedProductRepository;


    @Value("${mysql.jpa.properties.hibernate.jdbc.batch_size}") private int batchSize;
    @Value("${save.recent.items.api}")
    private String saveRecentItemUrl;
    @Value("${get.recent.items.api}")
    private String getRecentItemUrl;

    public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        Image resultingImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    @Override
    public UItemMstEntity findByItemCode(String itemCode) {
        return itemRepository.findByItemCode(itemCode);
    }

    @Override
    public List<String> findByMfacCode(String mfacCode, Long pageNumber, Long rowLimit) {
        Pageable page = PageRequest.of(pageNumber.intValue(), rowLimit.intValue(), Sort.by("cCode").ascending());
        return itemRepository.findByMfacCode(mfacCode, page);
    }

    public List<String> findByContCode(String contCode, Long pageNumber, Long rowLimit) {
        Pageable page = PageRequest.of(pageNumber.intValue(), rowLimit.intValue(), Sort.by("cCode").ascending());
        return itemRepository.findByContCode(contCode, page);
    }

    @Override
    public JsonArray getNewLaunched(PageBO request, Long days, LcHeaderBO headerBO) throws CommunicationErrorException, RecordNotFoundException, InvalidRequestException {
        JsonArray arr = new JsonArray();
        String sellerBuyerCombo = filterService.getSellerCodeAndBuyerCodeQuery(headerBO);
        String sql = getNewLaunchedItems(sellerBuyerCombo);
        Query query = this.getQuery(sql);
        List<Object[]> resultList = this.getResultList(query, request.getPage(), request.getLimit());
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
            jsonObject.addProperty("c_scheme", helper.getString(objects[++i]));
            jobj.addProperty("c_thumbnail_image", helper.getString(objects[++i]));
            jsonArray1.add(jobj);
            jsonObject.add("ac_thumbnail_images", jsonArray1);
            jsonObject.addProperty("c_pack_type_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_discount_status", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_watchlist_status", Constants.STATUS_NO);
            jsonObject.addProperty("c_short_book_status", Constants.STATUS_NO);
            arr.add(jsonObject);
        }

        return arr;
    }

    private String getNewLaunchedItems(String sellerBuyerCombo) {
        return " select distinct uim.c_code itemCode, uim.c_name itemName, uipm.c_name packName, uicm.c_name contentName, " +
                " uim.n_last_mrp Mrp,  sch.c_scheme as scheme, uim.c_web_img_link acThumbnailImage, uiptm.c_name as packTypeName" +
                " from u_item_mst uim " +
                " join u_item_pack_mst uipm on uim.c_item_pack_code = uipm.c_code " +
                " join u_item_cont_mst uicm on uim.c_item_cont_code = uicm.c_code " +
                " join u_item_pack_type_mst uiptm on uim.c_pack_type_code = uiptm.c_code " +
                " left join  (select usi.c_ucode, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, coalesce(sch.n_sch_ratio, 0) as n_sch_ratio, " +
                " coalesce(sch.n_sch_order, 9999999) as n_sch_order, sch.c_scheme, " +
                " row_number() over(partition by usi.c_ucode order by sch.n_sch_ratio desc," +
                " coalesce(sch.n_sch_order, 9999999) asc) as rowid  " +
                " from u_stockiest_item usi" +
                " join (" + sellerBuyerCombo + " ) t on t.seller_code = usi.c_stockiest_code" +
                " left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code " +
                " left join cust_category_mst ccm on ccm.c_c2code = ac.c_c2code and ccm.c_code = ac.c_cust_category_code " +
                " join lc_c2code_mst lccm on lccm.c_code = usi.c_stockiest_code " +
                " left join cust_branch_item_stock cbis on cbis.c_c2code = usi.c_stockiest_code " +
                " and cbis.c_item_code = usi.c_stockiest_item_code and cbis.c_br_code = lccm.c_cust_branch_code  " +
                " left join lo_view_seller_cat_item_scheme sch   on sch.c_c2code = usi.c_stockiest_code   " +
                " and sch.c_cust_cat_code = if(trim(coalesce(ccm.c_sch_category_code, '')) = '', coalesce(ccm.c_code, '-'), " +
                " ccm.c_sch_category_code)   and sch.c_item_code = usi.c_stockiest_item_code ) sch on sch.c_ucode = uim.c_code" +
                " and sch.rowid = 1 " +
                " where uim.d_adate >=DATE_ADD(NOW(),INTERVAL -90 DAY) " +
                " and uim.n_last_mrp > 0 and uim.n_active > 0  and uim.c_gst_code is not null  " +
                " ORDER BY uim.d_adate desc ";
    }

    @Override
    public JsonArray geTopMostOrderItem(LcHeaderBO lcHeaderBO, ItemsSearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        JsonArray arr = new JsonArray();
        String state = catalogueService.getFirmState(lcHeaderBO.getFirmId());

        if (!helper.isEmpty(state)) {
            String sellerBuyerCombo = filterService.getSellerCodeAndBuyerCodeQuery(lcHeaderBO);
            String sql = getTopMostOrderItem(sellerBuyerCombo);
            Query query = this.getQuery(sql);
            query.setParameter("state", state);
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
                jobj.addProperty("c_thumbnail_image", helper.getString(objects[++i]));
                jsonArray1.add(jobj);
                jsonObject.add("ac_thumbnail_images", jsonArray1);
                jsonObject.addProperty("c_scheme",helper.getString(objects[++i]));
                jsonObject.addProperty("c_pack_type_name",helper.getString(objects[++i]));
                jsonObject.addProperty("c_discount_status", Constants.STATUS_NO); //TODO user query to update
                jsonObject.addProperty("c_watchlist_status", Constants.STATUS_NO);
                jsonObject.addProperty("c_short_book_status", Constants.STATUS_NO);
                arr.add(jsonObject);
            }
        }
        return arr;
    }

    private String getTopMostOrderItem(String sellerBuyerCombo) {
        String sql ="SELECT usfi.c_ucode, uim.c_name as itemName, uipm.c_name as packName, uicm.c_name as contentName, " +
                " uim.n_last_mrp as Mrp, uim.c_web_img_link as acThumbnailImage,sch.c_scheme as scheme, " +
                " uiptm.c_name as packTypeName "+
                " FROM u_statewise_fastmoving_items usfi " +
                " JOIN u_item_mst uim ON uim.c_code = usfi.c_ucode " +
                " LEFT JOIN u_item_pack_mst uipm on uim.c_item_pack_code = uipm.c_code " +
                " LEFT JOIN u_item_pack_type_mst uiptm on uim.c_pack_type_code = uiptm.c_code "+
                " LEFT JOIN u_item_cont_mst uicm on uim.c_item_cont_code = uicm.c_code " +
                "left join " +
                " (select usi.c_ucode, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, coalesce(sch.n_sch_ratio, 0) as n_sch_ratio, coalesce(sch.n_sch_order, 9999999) as n_sch_order, sch.c_scheme, " +
                " row_number() over(partition by usi.c_ucode order by sch.n_sch_ratio desc, coalesce(sch.n_sch_order, 9999999) asc) as rowid " +
                " from u_stockiest_item usi " +
                " join ( "+sellerBuyerCombo +" ) t on t.seller_code = usi.c_stockiest_code " +
                " left join cust_act_mst ac on usi.c_stockiest_code = ac.c_c2code and t.cust_code = ac.c_code " +
                " left join cust_category_mst ccm on ccm.c_c2code = ac.c_c2code and ccm.c_code = ac.c_cust_category_code " +
                " join lc_c2code_mst lccm on lccm.c_code = usi.c_stockiest_code  " +
                " left join cust_branch_item_stock cbis on cbis.c_c2code = usi.c_stockiest_code and cbis.c_item_code = usi.c_stockiest_item_code and cbis.c_br_code = lccm.c_cust_branch_code   " +
                " left join lo_view_seller_cat_item_scheme sch   " +
                " on sch.c_c2code = usi.c_stockiest_code  " +
                " and sch.c_cust_cat_code = if(trim(coalesce(ccm.c_sch_category_code, '')) = '', coalesce(ccm.c_code, '-'), ccm.c_sch_category_code)  " +
                " and sch.c_item_code = usi.c_stockiest_item_code ) sch on sch.c_ucode = uim.c_code and sch.rowid = 1 " +
                " WHERE uim.n_active > 0 and usfi.c_state_code = :state " +
                " and uim.n_last_mrp > 0 " +
                " and uim.c_gst_code is not null " +
                " ORDER BY usfi.n_count desc ";
        return sql;
    }

    @Override
    public List<ItemPLPResponseBO> getPLP(List<String> itemCodes, PageBO pageBO) {
        List<ItemPLPResponseBO> list = new ArrayList<>();
        Criteria criteria = Criteria.where("_id").in(itemCodes).and("c_gst_code").ne(null).and("n_mrp").gt(0); // add the in clause

        org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query().addCriteria(criteria);
        List<LcItem> lcItems = mongoOperations.find(query, LcItem.class);
        for (LcItem item : lcItems) {
            ItemPLPResponseBO itemPLPResponseBO = getPLPResponseBO(item);
            itemPLPResponseBO.setWatchListStatus(Constants.STATUS_NO);
            itemPLPResponseBO.setShortbookStatus(Constants.STATUS_NO);
            itemPLPResponseBO.setDiscountStatus(Constants.STATUS_NO);
            list.add(itemPLPResponseBO);
        }
        return list;
    }

    @Override
    public List<ItemPLPResponseBO> getTSPLP(List<Object[]> itemCodes, PageBO pageBO) {
        List<String> items = new ArrayList<>();
        Map<String, String> custItemMap = new HashMap<>();
        Map<String, BigDecimal> sellerRateMap = new HashMap<>();
        List<ItemPLPResponseBO> list = new ArrayList<>();
        List<String> custItemCodeFetch = new ArrayList<>();

        for (Object[] object: itemCodes ) {
            items.add(helper.getString(object[0]));
            sellerRateMap.put(helper.getString(object[2]),helper.getBigDecimal(object[1]));
            custItemMap.put(helper.getString(object[2]),helper.getString(object[0]));
        }

        Criteria criteria = Criteria.where("_id").in(items).and("c_gst_code").ne(null); // add the in clause

        org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query().addCriteria(criteria);
        List<LcItem> lcItems = mongoOperations.find(query, LcItem.class);
        for (String itemCode : items) {
            LcItem lcItem = lcItems.stream().filter(item -> itemCode.equals(item.getItemCode())).findAny().orElse(null);
            if (lcItem != null) {
                ItemPLPResponseBO plp = getPLPResponseBO(lcItem);
                setMrpForTSPLP(custItemMap, sellerRateMap, custItemCodeFetch, itemCode, plp);
                plp.setPackSize(lcItem.getPackSize());
                plp.setMfgCode(lcItem.getMfgCode());
                plp.setMfgName(lcItem.getMfgName());
                plp.setOfferRate(BigDecimal.ZERO);
                plp.setNDiscount(BigDecimal.ZERO);
                list.add(plp);
            }
        }
        return list;
    }

    public ItemPLPResponseBO getPLPResponseBO(LcItem item) {
        ItemPLPResponseBO bo = new ItemPLPResponseBO();
        bo.setItemCode(item.getItemCode());
        bo.setItemName(item.getItemName());
        bo.setPackName(item.getPackName());
        bo.setMrp(item.getMrp());
        bo.setContains(item.getContains());
        /*List<ThumbnailBO> imgList = new ArrayList<>();
        ThumbnailBO thumbnailBO = new ThumbnailBO();
        if (!helper.isEmpty(item.getImgUrl())){
            thumbnailBO.setThumbnailImage(item.getImgUrl());
        }
        if(helper.isEmpty(item.getImgUrl()) || item.getImgUrl().equals("NaN")){
            thumbnailBO.setThumbnailImage("");
        }
        imgList.add(thumbnailBO);*/

        List<ItemImageBO> itemImageBOS = new ArrayList<>();
        List<ThumbnailBO> thumbnailBOS = new ArrayList<>();
        ThumbnailBO thumbnailBO = new ThumbnailBO();
        ItemImageBO itemImageBO = new ItemImageBO();
        if (!helper.isEmpty(item.getImgUrl())){
            thumbnailBO.setThumbnailImage(item.getImgUrl());
            itemImageBO.setItemImage(item.getImgUrl());
        }
        itemImageBOS.add(itemImageBO);
        thumbnailBOS.add(thumbnailBO);
        bo.setImageBOS(thumbnailBOS);
        //bo.setThumbnail(itemImageBOS);
        return bo;
    }

    @Override
    public List<String> geTopMostOrderUItemCode(LcHeaderBO lcHeaderBO, PageBO pageBO) throws CommunicationErrorException, InvalidRequestException {
        List<String> list = new ArrayList<>();
        String state = catalogueService.getFirmState(lcHeaderBO.getFirmId());

        if (!helper.isEmpty(state)) {
            String sql = getTopMostOrderItemSql();
            JsonObject params = new JsonObject();
            params.addProperty("state", state);
            list = this.getSingleColumnResult(sql, params, pageBO);
        }
        return list;
    }

    @Override
    public List<Object[]> categoryWiseProducts(SearchBO searchBO, LcHeaderBO header, List<String> categoryList) {
        List<String> itemList = new ArrayList<>();
        String sql = categoryWiseProductsQuery(searchBO);
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", header.getC2Code());
        query.setParameter("categoryCode", categoryList);
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_DISCOUNT)||searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_DISCOUNT)){
            query.setParameter("start_today", helper.getCurrentDateTime("YYYY-MM-dd HH:mm:ss"));
            query.setParameter("start_today_date", helper.getString(helper.getCurrentDateTime("YYYY-MM-dd")) + '%');
        }
        return this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
    }

    public List<Object[]> categoryWiseProductsCount(SearchBO searchBO, LcHeaderBO header) {
        List<String> categoryList = new ArrayList<>();
        categoryList.add(searchBO.getSearchTerm());
        String sql = categoryWiseProductsQuery(searchBO);
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", header.getC2Code());
        query.setParameter("categoryCode",categoryList );
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_DISCOUNT)||searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_DISCOUNT)){
            query.setParameter("start_today", helper.getCurrentDateTime("YYYY-MM-dd HH:mm:ss"));
            query.setParameter("start_today_date", helper.getString(helper.getCurrentDateTime("YYYY-MM-dd")) + '%');
        }
        List<Object[]> list = this.getResultList(query);
        return list;
    }
    @Override
    public List<ItemPLPResponseBO> getTSCategoryPLP(List<Object[]> items, SearchBO searchBO) {
        List<ItemPLPResponseBO> list = new ArrayList<>();
        Map<String, String> custItemMap = new HashMap<>();
        Map<String, BigDecimal> sellerRateMap = new HashMap<>();
        List<String> itemCodes = new ArrayList<>();
        for (Object[] object: items ) {
            itemCodes.add(helper.getString(object[0]));
            sellerRateMap.put(helper.getString(object[2]),helper.getBigDecimal(object[1]));
            custItemMap.put(helper.getString(object[2]),helper.getString(object[0]));
        }
        Criteria criteria = Criteria.where("_id").in(itemCodes).and("c_gst_code").ne(null); // add the in clause

        org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query().addCriteria(criteria);
        List<LcItem> lcItems = mongoOperations.find(query, LcItem.class);
        List<String> custItemCodeFetch = new ArrayList<>();
        for (String itemCode : itemCodes) {
            LcItem lcItem = lcItems.stream().filter(item -> itemCode.equals(item.getItemCode())).findAny().orElse(null);
            if (lcItem != null) {
                ItemPLPResponseBO plp = getPLPResponseBO(lcItem);
                setMrpForTSPLP(custItemMap, sellerRateMap, custItemCodeFetch, itemCode, plp);
                plp.setPackSize(lcItem.getPackSize());
                plp.setMfgCode(lcItem.getMfgCode());
                plp.setMfgName(lcItem.getMfgName());
                list.add(plp);
            }
        }
        return list;
    }

    public void setMrpForTSPLP(Map<String, String> custItemMap, Map<String, BigDecimal> sellerRateMap, List<String> custItemCodeFetch, String itemCode, ItemPLPResponseBO plp) {
        int uCodeCount = 0;
        for(Map.Entry<String, String> custItemCodeKey : custItemMap.entrySet()){
            if(uCodeCount==0) {
                if (custItemCodeKey.getValue().equals(itemCode)) {
                    uCodeCount++;
                    if(!custItemCodeFetch.contains(custItemCodeKey.getKey())) {
                        custItemCodeFetch.add(custItemCodeKey.getKey());
                        for (Map.Entry<String, BigDecimal> SellerRateKey : sellerRateMap.entrySet()) {
                            if (SellerRateKey.getKey().equals(custItemCodeKey.getKey())) {
                                plp.setSellerItemCode(SellerRateKey.getKey());
                                plp.setMrp(SellerRateKey.getValue());
                            }
                        }
                    }
                }
            }

        }
    }


    @Override
    public long tsProductsCount(List<Object[]> items, SearchBO searchBO) {
        List<ItemPLPResponseBO> list = new ArrayList<>();
        List<String> itemCodes = new ArrayList<>();
        for (Object[] object: items ) {
            itemCodes.add(helper.getString(object[0]));
        }
        Criteria criteria = Criteria.where("_id").in(itemCodes).and("c_gst_code").ne(null); // add the in clause

        org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query().addCriteria(criteria);
        List<LcItem> lcItems = mongoOperations.find(query, LcItem.class);
        return lcItems.size();
    }

    //@Override
    /*public List<String> DealOfTheDayProducts(SearchBO searchBO, LcHeaderBO header) {
        List<String> itemList = new ArrayList<>();
        String sql = DealOfTheDayProductsQuery(searchBO);
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", header.getC2Code());
        query.setParameter("today", helper.getCurrentTime());
        List<Object[]> list = this.getResultList(query);
        for (Object[] object :list) {
            itemList.add(helper.getString(object[0]));
        }
        return itemList;
    }*/

    /*private String DealOfTheDayProductsQuery(SearchBO searchBO) {
        String sql = "select usi.c_ucode  " +
                "from deal_of_the_day im  " +
                "JOIN u_stockiest_item usi on im.c_c2code = usi.c_stockiest_code and im.item_code = usi.c_stockiest_item_code " +
                "LEFT JOIN u_fastmoving_items fm on im.item_code = fm.c_ucode " +
                "LEFT JOIN cust_fetured_items itemfeatured on itemfeatured.c_c2code = im.c_c2code and itemfeatured.c_item_code = im.item_code " +
                "LEFT JOIN cust_branch_item_stock cbis on cbis.c_c2code = im.c_c2code and cbis.c_item_code = im.item_code " +
                "where im.c_c2code= :c2Code and im.expiry_date <=:today  " ;

        if(searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_RELEVANCE)) {
            sql += "ORDER BY coalesce(fm.n_count, 0) DESC " ;
        }
        if(searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_PRICE)) {
            sql += "order by coalesce(cbis.n_rate, 0) ";
        }

        if(searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_PRICE)) {
            sql +=  "order by coalesce(cbis.n_rate, 0) DESC " ;
        }

        if(searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_DISCOUNT)){
            sql +="order by coalesce(itemfeatured.n_discount_amount, 0) ";
        }

        if(searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_DISCOUNT)) {
            sql += "order by coalesce(itemfeatured.n_discount_amount, 0) DESC  " ;
        }
        return sql;

    }*/

    private String categoryWiseProductsQuery(SearchBO searchBO) {
        String sql = "SELECT usi.c_ucode , cbis.n_rate, usi.c_stockiest_item_code   " +
                " FROM cust_item_mst cim    " +
                " join u_stockiest_item usi on cim.c_code = usi.c_stockiest_item_code and cim.c_c2code = usi.c_stockiest_code " +
                " LEFT JOIN u_fastmoving_items fm on usi.c_ucode = fm.c_ucode   " +
                " LEFT JOIN cust_branch_item_stock cbis FORCE INDEX(PRIMARY) on cbis.c_c2code = usi.c_stockiest_code and cbis.c_item_code = usi.c_stockiest_item_code   " +
                " INNER JOIN lc_c2code_mst lccm on lccm.c_code = cbis.c_c2code and lccm.c_cust_branch_code = cbis.c_br_code  " ;
        String sql1 = " WHERE cim.c_cat_code in :categoryCode  and  cim.c_c2code= :c2Code ";
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_DISCOUNT)||searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_DISCOUNT)){
            sql +=" INNER join deal_of_the_day dotd on dotd.c_c2code = cbis.c_c2code and dotd.c_item_code = cbis.c_item_code " ;
            sql1+=  " and dotd.c_deal_status = 'Y' and ((dotd.t_start_date <= :start_today_date or dotd.t_start_date like :start_today_date) and dotd.t_start_date <=:start_today and dotd.t_end_date >=:start_today) ";
        }
        sql +=sql1;

        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_RELEVANCE )) {
            sql += " ORDER BY coalesce(fm.n_count, 0) DESC ";
        }
        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_PRICE)) {
            sql += " ORDER BY coalesce(cbis.n_rate, 0) ";
        }

        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_PRICE)) {
            sql +=     " ORDER BY coalesce(cbis.n_rate, 0) DESC ";
        }

        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_LOW_TO_HIGH_DISCOUNT)) {
            sql += " ORDER BY coalesce(dotd.n_deal_rate, 0) ";
        }

        if (searchBO.getSort().equals(com.c2.lc.ms.master.utils.Constants.SORT_HIGH_TO_LOW_DISCOUNT)) {
            sql += " ORDER BY coalesce(dotd.n_deal_rate, 0) DESC  ";
        }
        return sql;
    }

    private String getTopMostOrderItemSql() {
         return "SELECT usfi.c_ucode " +
                 " FROM u_statewise_fastmoving_items usfi  " +
                 " JOIN u_item_mst uim ON uim.c_code = usfi.c_ucode  " +
                 " WHERE uim.n_active > 0 and usfi.c_state_code = :state " +
                 " AND uim.c_gst_code IS NOT NULL ORDER BY usfi.n_count DESC ";
    }

    @Override
    public JsonArray uploadProductImage(MultipartFile[] files) throws InvalidRequestException, StorageException, IOException, URISyntaxException, InvalidKeyException {

        if (files.length <= 0) {
            throw new InvalidRequestException("file", "empty");
        }
        JsonArray jsonArray = new JsonArray();
        for (MultipartFile file : files) {
            //   String ext = .getExtension(file.getOriginalFilename());

            String imageUrl = this.uploadToBlob(BlobFolder.C2_FOLDER + "/" + BlobFolder.PRODUCT_FOLDER + "/" + file.getOriginalFilename(),
                    file.getInputStream(), file.getSize());

            jsonArray.add(imageUrl);
        }
        return jsonArray;
    }

    @Override
    public void updateItemImage(ImageUpdateBo imageUpdateBo) throws RecordNotFoundException {

        Optional<LcItem> lcItem = lcItemRepository.findById(imageUpdateBo.getCCode());
        if (lcItem == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        Criteria criteria = Criteria.where("_id").is(imageUpdateBo.getCCode());
        org.springframework.data.mongodb.core.query.Query query = org.springframework.data.mongodb.core.query.Query.query(criteria);
        Update update = new Update();
        update.set("ac_item_images", imageUpdateBo.getAcImages());
        mongoOperations.upsert(query, update, LcItem.class);
    }

    @Override
    public Integer getTopCount(Long firmId) throws CommunicationErrorException, InvalidRequestException {
        String code = catalogueService.getFirmState(firmId);
         return getCountByUcode(code);

    }
    private Integer getCountByUcode(String code){
        /*String sql = getTopMostOrderItemSql();
        Query query = this.getQuery("SELECT COUNT(*) FROM (" + sql + ") DUMMY");
        JsonObject params = new JsonObject();
        params.addProperty("state", code);*/
        String sql = getTopMostOrderItem(code);
        Query query = this.getQuery("SELECT COUNT(*) FROM (" + sql + ") DUMMY");
        query.setParameter("state", code);
        BigInteger count = (BigInteger) this.getSingleResult(query);
        return helper.getInt(count.intValue());
    }


    @Override
    public Long getMasterActiveItemCount() {
        String sql = "SELECT COUNT(c_code) FROM u_item_mst WHERE n_active = 1";
        Query query = this.getQuery(sql);
        return ((BigInteger) this.getSingleResult(query)).longValue();
    }

    @Override
    public void saveCustItemMst(String c2Code, String brCode, List<RowItemBO> data) throws DataIntegrityViolationException {
        int size = data.size();
        int counter = 0;
        List<CustItemMstEntity> custItemMstEntities = new ArrayList<>();
        List<UStockiestItemEntity> uStockiestItemEntities = new ArrayList<>();

        for (RowItemBO datum : data) {
            CustItemMstEntity custItemMstEntity = new CustItemMstEntity();
            custItemMstEntity.setcC2Code(c2Code);
            custItemMstEntity.setcBrCode(brCode);
            custItemMstEntity.setcCode(datum.getCCode());
            custItemMstEntity.setcNmCode(datum.getCNmCode());
            custItemMstEntity.setcName(datum.getCName());
            custItemMstEntity.setcShName(datum.getCShName());
            custItemMstEntity.setcPackCode(datum.getCPackCode());
            custItemMstEntity.setcCatCode(datum.getCCatCode());
            custItemMstEntity.setcMfacCode(datum.getCMfacCode());
            custItemMstEntity.setnQtyPerBox(datum.getNQtyPerBox());
            custItemMstEntity.setcGroupCode(datum.getCGroupCode());
            custItemMstEntity.setnIncExcTax(datum.getNIncExcTax());
            custItemMstEntity.setcContCode(datum.getCContCode());
            custItemMstEntity.setnSchedule(datum.getNSchedule());
            custItemMstEntity.setcDiseaseCatCode(datum.getCDiseaseCatCode());
            custItemMstEntity.setcPackCode(datum.getCPackCode());
            custItemMstEntity.setcScheduleCode(datum.getCScheduleCode());
            custItemMstEntity.setnMinSaleQty(datum.getNMinSaleQty());
            custItemMstEntity.setnSelfBarcodeReq(datum.getNSelfBarcodeReq());
            custItemMstEntity.setnMinMarginPer(datum.getNMinMarginPer());
            custItemMstEntity.setnAudited(datum.getNAudited());
            custItemMstEntity.setnMaxDisPer(datum.getNMaxDisPer());
            custItemMstEntity.setnPredefined(datum.getNPredefined());
            custItemMstEntity.setnMaxMarginPer(datum.getNMinMarginPer());
            custItemMstEntity.setnLockPo(datum.getNLockPo());
            custItemMstEntity.setcBrandName(datum.getCBrandName());
            custItemMstEntity.setnServiceItem(datum.getNServiceItem());
            custItemMstEntity.setnBlockExpPrint(datum.getNBlockExpPrint());
            custItemMstEntity.setnGenItem(datum.getNGenItem());
            custItemMstEntity.setnOuterPackLot(datum.getNOuterPackLot());
            custItemMstEntity.setcEdiCode(datum.getCEdiCode());
            custItemMstEntity.setcStorageCode(datum.getCStorageCode());
            custItemMstEntity.setnItemLength(datum.getNItemLength());
            custItemMstEntity.setnItemWidth(datum.getNItemWidth());
            custItemMstEntity.setnItemHeight(datum.getNItemHeight());
            custItemMstEntity.setnItemWeight(datum.getNItemWeight());
            custItemMstEntity.setnInnerLength(datum.getNInnerLength());
            custItemMstEntity.setnInnerWidth(datum.getNInnerWidth());
            custItemMstEntity.setnInnerHeight(datum.getNInnerHeight());
            custItemMstEntity.setnInterWeight(datum.getNInterWeight());
            custItemMstEntity.setnOuterLength(datum.getNOuterLength());
            custItemMstEntity.setnOuterWidth(datum.getNOuterWidth());
            custItemMstEntity.setnOuterHeight(datum.getNOuterHeight());
            custItemMstEntity.setnOuterWeight(datum.getNOuterWeight());
            custItemMstEntity.setnBatchNoRule(datum.getNBatchNoRule());
            custItemMstEntity.setnInnerPackLot(datum.getNInnerPackLot());
            custItemMstEntity.setnExpDtRule(datum.getNExpDtRule());
            custItemMstEntity.setnShelfLife(datum.getNShelfLife());
            custItemMstEntity.setnLock(datum.getNLock());
            custItemMstEntity.setdAdate(LocalDate.parse(datum.getDAdate()));
            custItemMstEntity.setdLdate(LocalDate.parse(datum.getDLdate()));
            //custItemMstEntity.(json.get("c_modiuser").getAsInt());
            custItemMstEntity.setnPriceControlProduct(datum.getNPriceControlProduct());
            custItemMstEntity.setnMaxMrp(datum.getNMaxMrp());
            custItemMstEntity.setnRateItemBatchwise(datum.getNRateItemBatchwise());
            custItemMstEntity.setcBrandCode(datum.getCBrandCode());
            custItemMstEntity.setnType(datum.getNType());
            custItemMstEntity.settLtime(datum.getTLtime());
            custItemMstEntity.setnStkSerial(datum.getNStkSerial());
            custItemMstEntity.setnSalableOnline(datum.getNSalableOnline());
            custItemMstEntity.setnMrp(datum.getNMrp());
            custItemMstEntity.setcStorageCareCode(datum.getCStorageCareCode());
            custItemMstEntity.setnPtrBox(datum.getNPtrBox());
            custItemMstEntity.setnMrpBox(datum.getNMaxMrp());
            custItemMstEntity.setnAudited(datum.getNAudited());
            custItemMstEntity.setnDiscountRate(datum.getNDiscountRate());
            custItemMstEntity.setnExcludeAlternate(datum.getNExcludeAlternate());
            custItemMstEntity.setnPredefined(datum.getNPredefined());
            //custItemMstEntity.setnsale(json.get("n_sp_sale_qty_from").getAsInt());
            //custItemMstEntity.(json.get("n_non_returnable_item").getAsInt());

            custItemMstEntities.add(custItemMstEntity);

            if (datum.getCUcode() != null && !datum.getCUcode().isEmpty()) {
                UStockiestItemEntity uStockiestItemEntity = new UStockiestItemEntity();
                UStockiestItemEntityPK pk = new UStockiestItemEntityPK();

                pk.setCStockiestCode(c2Code);
                pk.setCStockiestItemCode(datum.getCCode());
                uStockiestItemEntity.setPK(pk);
                uStockiestItemEntity.setCUcode(datum.getCUcode());
                uStockiestItemEntity.setDLdate(helper.getCurrentTime());
                uStockiestItemEntity.setNSuppQb(datum.getNQtyPerBox());
                uStockiestItemEntities.add(uStockiestItemEntity);
            }

            if ((counter + 1) % batchSize == 0 || (counter + 1) == size) {
                custItemMstRepository.saveAll(custItemMstEntities);
                custItemMstEntities.clear();
                if (!uStockiestItemEntities.isEmpty()) {
                    stockiestItemRepository.saveAll(uStockiestItemEntities);
                    uStockiestItemEntities.clear();
                }
                log.debug("counter -> {}", counter + 1);
            }
            counter++;
        }
    }

    @Override
    public void saveCustPackMst(String c2Code, String brCode, List<RowPackBO> data) throws DataIntegrityViolationException {
        int size = data.size();
        int counter = 0;
        List<CustPackMstEntity> custPackMstEntities = new ArrayList<>();

        for (RowPackBO datum : data) {
            CustPackMstEntity custPackMstEntity = new CustPackMstEntity();
            custPackMstEntity.setcC2code(c2Code);
            custPackMstEntity.setcCode(datum.getCCode());
            custPackMstEntity.setcName(datum.getCName());
            custPackMstEntity.setdLdate(helper.getCurrentDate());
            custPackMstEntity.setdAdate(helper.convertStringToDate(datum.getDAdate()));
            custPackMstEntity.setcCreateuser(datum.getCCreateuser());
            custPackMstEntity.setnAudited(datum.getNAudited());
            custPackMstEntity.setnPredefined(datum.getNPredefined());
            custPackMstEntity.settLtime(helper.getCurrentTime());
            custPackMstEntity.setcModiuser(datum.getCModiuser());

            custPackMstEntities.add(custPackMstEntity);
            if ((counter + 1) % batchSize == 0 || (counter + 1) == size) {
                custPackMstRepository.saveAll(custPackMstEntities);
                custPackMstEntities.clear();
                log.debug("counter -> {}", counter + 1);
            }
            counter++;
        }
    }

    @Override
    public void saveCustMfacMst(String c2Code, String brCode, List<RowMfacBO> row) {
        int size = row.size();
        int counter = 0;
        List<CustMfacMstEntity> custPackMstEntities = new ArrayList<>();

        for (RowMfacBO datum : row) {
            CustMfacMstEntity custMfacMstEntity = new CustMfacMstEntity();
            custMfacMstEntity.setcC2code(c2Code);
            custMfacMstEntity.setcCode(datum.getCCcode());
            custMfacMstEntity.setcNname(datum.getCNname());
            custMfacMstEntity.setcShName(datum.getCShName());
            custMfacMstEntity.setcAdd1(datum.getCAdd1());
            custMfacMstEntity.setcAdd2(datum.getCAdd2());
            custMfacMstEntity.setcAdd3(datum.getCAdd3());
            custMfacMstEntity.setcCity(datum.getCCity());
            custMfacMstEntity.setcPincode(datum.getCPincode());
            custMfacMstEntity.setcPhone1(datum.getCPhone1());
            custMfacMstEntity.setcPhone2(datum.getCPhone2());
            custMfacMstEntity.setcFax(datum.getCFax());
            custMfacMstEntity.setcContactPerson(datum.getCContactPerson());
            custMfacMstEntity.setnLock(datum.getNLock());
            custMfacMstEntity.setcDrugLicenceNo1(datum.getCDrugLicenceNo1());
            custMfacMstEntity.setcDrugLicenceNo2(datum.getCDrugLicenceNo2());
            custMfacMstEntity.setcStNo(datum.getCStNo());
            custMfacMstEntity.setcCstNo(datum.getCCstNo());
            custMfacMstEntity.setcEmail(datum.getCEmail());
            custMfacMstEntity.setdLdate(helper.getCurrentDate());
            custMfacMstEntity.setdAdate(helper.convertStringToDate(datum.getDAdate()));
            custMfacMstEntity.setcCreateuser(datum.getCCreateuser());
            custMfacMstEntity.setnAudited(datum.getNAudited());
            custMfacMstEntity.setnPredefined(datum.getNPredefined());
            custMfacMstEntity.setcGeoLat(datum.getCGeoLat());
            custMfacMstEntity.setcGeoLon(datum.getCGeoLon());
            custMfacMstEntity.setcMfacGroupCode(datum.getCMfacGroupCode());
            custMfacMstEntity.setcAreaCode(datum.getCAreaCode());
            custMfacMstEntity.setcModifyUser(datum.getCModifyUser());
            custMfacMstEntity.setcFullName(datum.getCFullName());
            custMfacMstEntity.setnSalableOnline(datum.getNSalableOnline());
            custMfacMstEntity.setnStocksale(datum.getNStocksale());
            custMfacMstEntity.setnBlockCrnt(datum.getNBlockCrnt());

            custPackMstEntities.add(custMfacMstEntity);

            if ((counter + 1) % batchSize == 0 || (counter + 1) == size) {
                custMfacMstRepository.saveAll(custPackMstEntities);
                custPackMstEntities.clear();
                log.debug("counter -> {}", counter + 1);
            }
            counter++;
        }
    }

    @Override
    public void saveCustBrandMst(String c2Code, String brCode, List<RowBrandBO> row) {
        int size = row.size();
        int counter = 0;
        List<CustBrandMstEntity> custBransMstEntites = new ArrayList<>();

        for (RowBrandBO datum : row) {
            CustBrandMstEntity custBransMstEntity = new CustBrandMstEntity();
            custBransMstEntity.setcC2code(c2Code);
            custBransMstEntity.setcCode(datum.getCCcode());
            custBransMstEntity.setcName(datum.getCName());
            custBransMstEntity.setdLdate(helper.getCurrentDate());
            custBransMstEntity.setdAdate(helper.convertStringToDate(datum.getDAdate()));
            custBransMstEntity.setcCreateuser(datum.getCCreateuser());
            custBransMstEntity.setnAudited(datum.getNAudited());
            custBransMstEntity.setnPredefined(datum.getNPredefined());
            custBransMstEntity.settLtime(helper.getCurrentTime());
            custBransMstEntity.setcModiuser(datum.getCModiuser());
            custBransMstEntity.setcShName(datum.getCShName());

            custBransMstEntites.add(custBransMstEntity);

            if ((counter + 1) % batchSize == 0 || (counter + 1) == size) {
                custBrandMstRepository.saveAll(custBransMstEntites);
                custBransMstEntites.clear();
                log.debug("counter -> {}", counter + 1);
            }
            counter++;
        }
    }

    @Override
    public void saveCustScheduleMst(String c2Code, String brCode, List<RowScheduleBO> row) {

        int size = row.size();
        int counter = 0;
        List<CustScheduleMstEntity> custScheduleMstEntites = new ArrayList<>();

        for (RowScheduleBO datum : row) {
            CustScheduleMstEntity custScheduleMstEntity = new CustScheduleMstEntity();
            custScheduleMstEntity.setcC2code(c2Code);
            custScheduleMstEntity.setcCode(datum.getCCode());
            custScheduleMstEntity.setcName(datum.getCName());
            custScheduleMstEntity.setnWarning(datum.getNWarning());
            custScheduleMstEntity.setcMessage(datum.getCMessage());
            custScheduleMstEntity.setdLdate(helper.getCurrentDate());
            custScheduleMstEntity.setdAdate(helper.convertStringToDate(datum.getDAdate()));
            custScheduleMstEntity.setcCreateuser(datum.getCCreateuser());
            custScheduleMstEntity.setnAudited(datum.getNAudited());
            custScheduleMstEntity.setnPredefined(datum.getNPredefined());
            custScheduleMstEntity.setcShName(datum.getCShName());
            custScheduleMstEntity.settLtime(helper.getCurrentTime());
            custScheduleMstEntity.setcModiuser(datum.getCModiuser());
            custScheduleMstEntity.setnColor(datum.getNColor());
            custScheduleMstEntity.setnDocScan(datum.getNDocScan());
            custScheduleMstEntity.setnKeepScheduleRegister(datum.getNKeepScheduleRegister());
            custScheduleMstEntity.setnRxNonrxFlag(datum.getNRxNonrxFlag());

            custScheduleMstEntites.add(custScheduleMstEntity);

            if ((counter + 1) % batchSize == 0 || (counter + 1) == size) {
                custScheduleMstRepository.saveAll(custScheduleMstEntites);
                custScheduleMstEntites.clear();
                log.debug("counter -> {}", counter + 1);
            }
            counter++;
        }
    }

    @Override
    public void saveCustContMst(String c2Code, String brCode, List<RowContBO> row) {

        int size = row.size();
        int counter = 0;
        List<CustContMstEntity> custContMstEntites = new ArrayList<>();

        for (RowContBO datum : row) {
            CustContMstEntity custContMstEntity = new CustContMstEntity();
            custContMstEntity.setcC2Code(c2Code);
            custContMstEntity.setcCode(datum.getCCode());
            custContMstEntity.setcName(datum.getCName());
            custContMstEntity.setcShName(datum.getCShName());
            custContMstEntity.setcNote(datum.getCNote());
            custContMstEntity.setcNote1(datum.getCNote1());
            custContMstEntity.setcNote2(datum.getCNote2());
            custContMstEntity.setcNote3(datum.getCNote3());
            custContMstEntity.setcNote4(datum.getCNote4());
            custContMstEntity.setdLdate(helper.getCurrentTime());
            custContMstEntity.setdAdate(helper.getCurrentTime());
            custContMstEntity.setcCreateuser(datum.getCCreateuser());
            custContMstEntity.setnAudited(datum.getNAudited());
            custContMstEntity.setnPredefined(datum.getNPredefined());
            custContMstEntity.settLtime(helper.getCurrentTime());
            custContMstEntity.setcDiseaseCatCode(datum.getCDiseaseCatCode());
            custContMstEntity.setcScheduleCode(datum.getCScheduleCode());
            custContMstEntity.setnLock(datum.getNLock());
            custContMstEntity.setcModiuser(datum.getCModiuser());

            custContMstEntites.add(custContMstEntity);

            if ((counter + 1) % batchSize == 0 || (counter + 1) == size) {
                custContMstRepository.saveAll(custContMstEntites);
                custContMstEntites.clear();
                log.debug("counter -> {}", counter + 1);
            }
            counter++;
        }
    }

    @Override
    public void saveCustItemGroupMst(String c2Code, String brCode, List<RowItemGroupBO> row) {

        int size = row.size();
        int counter = 0;
        List<CustItemGroupMstEntity> custItemGroupMstEntites = new ArrayList<>();

        for (RowItemGroupBO datum : row) {
            CustItemGroupMstEntity custItemGroupMstEntity = new CustItemGroupMstEntity();
            custItemGroupMstEntity.setcC2Code(c2Code);
            custItemGroupMstEntity.setcCode(datum.getCCode());
            custItemGroupMstEntity.setcName(datum.getCName());
            custItemGroupMstEntity.setcShName(datum.getCShName());
            custItemGroupMstEntity.setdLdate(helper.getCurrentDate());
            custItemGroupMstEntity.setdAdate(helper.convertStringToDate(datum.getDAdate()));
            custItemGroupMstEntity.setcCreateuser(datum.getCCreateuser());
            custItemGroupMstEntity.setnAudited(datum.getNAudited());
            custItemGroupMstEntity.setnPredefined(datum.getNPredefined());
            custItemGroupMstEntity.setnPurExpDays(datum.getNPurExpDays());
            custItemGroupMstEntity.setnSaleExpDays(datum.getNSaleExpDays());
            custItemGroupMstEntity.setnGdnExpDays(datum.getNGdnExpDays());
            custItemGroupMstEntity.settLtime(helper.getCurrentTime());
            custItemGroupMstEntity.setcModiuser(datum.getCModiuser());

            custItemGroupMstEntites.add(custItemGroupMstEntity);

            if ((counter + 1) % batchSize == 0 || (counter + 1) == size) {
                custItemGroupMstRepository.saveAll(custItemGroupMstEntites);
                custItemGroupMstEntites.clear();
                log.debug("counter -> {}", counter + 1);
            }
            counter++;
        }
    }

    @Override
    public void saveCustItemCategoryMst(String c2Code, String brCode, List<RowItemCategoryBO> row) {

        int size = row.size();
        int counter = 0;
        List<CustItemCategoryMstEntity> custItemCategoryMstEntites = new ArrayList<>();

        for (RowItemCategoryBO datum : row) {
            CustItemCategoryMstEntity custItemCategoryMstEntity = new CustItemCategoryMstEntity();
            custItemCategoryMstEntity.setC2Code(c2Code);
            custItemCategoryMstEntity.setcCode(datum.getCCode());
            custItemCategoryMstEntity.setName(datum.getCName());
            custItemCategoryMstEntity.setShName(datum.getCShName());
            custItemCategoryMstEntity.setRst(datum.getNRst());
            custItemCategoryMstEntity.setlDate(helper.getCurrentDate());
            custItemCategoryMstEntity.setaDate(helper.convertStringToDate(datum.getDAdate()));
            custItemCategoryMstEntity.setCreateUser(datum.getCCreateuser());
            custItemCategoryMstEntity.setAudited(datum.getNAudited());
            custItemCategoryMstEntity.setnPredefined(datum.getNPredefined());
            custItemCategoryMstEntity.setDiscount(datum.getNDiscount());
            custItemCategoryMstEntity.setPoints(datum.getNPoints());
            custItemCategoryMstEntity.setlTime(helper.getCurrentTime());
            custItemCategoryMstEntity.setItemCategoryHeadCode(datum.getCItemCategoryHeadCode());
            custItemCategoryMstEntity.setAgePer(datum.getNAgePer());
            custItemCategoryMstEntity.setModiUser(datum.getCCreateuser());
            custItemCategoryMstEntity.setImageUrl(datum.getCImageUrl());
            custItemCategoryMstEntity.setSalableOnline(datum.getNSalableOnline());
            custItemCategoryMstEntity.setDisplayOnline(datum.getNDisplayOnline());
            custItemCategoryMstEntity.setActive(datum.getNActive());

            custItemCategoryMstEntites.add(custItemCategoryMstEntity);

            if ((counter + 1) % batchSize == 0 || (counter + 1) == size) {
                custItemCategoryMstRepository.saveAll(custItemCategoryMstEntites);
                custItemCategoryMstEntites.clear();
                log.debug("counter -> {}", counter + 1);
            }
            counter++;
        }
    }


    private String getProductDetailsByItemCodeAndC2code() {
        return "select im.c_name as item_name,im.c_code as item_code,mfac.c_name as Mf_name, mfac.c_code as mfcf_code,packmst.c_name as pack_name,im.n_mrp as original_price,itemfeatured.n_discount_amount as discount_amount,itemfeatured.n_discount_per as discount_per,moletbl.c_molecule_name as molecule_name, cwin.c_web_img_link as image_link from cust_item_mst im left join cust_mfac_mst mfac on im.c_c2code = mfac.c_c2code and im.c_mfac_code = mfac.c_code left join cust_pack_mst packmst on packmst.c_c2code = im.c_c2code and packmst.c_code = im.c_mfac_code left join cust_fetured_items itemfeatured on itemfeatured.c_c2code = im.c_c2code and itemfeatured.c_item_code = im.c_code " +
                "left join cust_molecule_det moletbl on moletbl.c_c2code = im.c_c2code and moletbl.c_item_code = im.c_code left join cust_win_image cwin on cwin.c_c2code = im.c_c2code and cwin.c_key_code = im.c_code where im.c_code = :itemCode and im.c_c2code=:c2Code and cwin.c_image_type = 'APK'";
    }

    @Override
    public void saveCustPackTypeMst(String c2Code, String brCode, List<RowPackTypeBO> data) throws DataIntegrityViolationException {
        int size = data.size();
        int counter = 0;
        List<CustPackTypeMstEntity> custPackTypeMstEntities = new ArrayList<>();

        for (RowPackTypeBO datum : data) {
            CustPackTypeMstEntity custPackTypeMstEntity = new CustPackTypeMstEntity();
            custPackTypeMstEntity.setcC2code(c2Code);
            custPackTypeMstEntity.setcCode(datum.getCCode());
            custPackTypeMstEntity.setcName(datum.getCName());
            custPackTypeMstEntity.setdLdate(helper.getCurrentDate());
            custPackTypeMstEntity.setdAdate(helper.convertStringToDate(datum.getDAdate()));
            custPackTypeMstEntity.setcCreateuser(datum.getCCreateUser());
            custPackTypeMstEntity.setnAudited(datum.getNAudited());
            custPackTypeMstEntity.setnPredefined(datum.getNPredefined());
            custPackTypeMstEntity.setcShName(datum.getCShName());
            custPackTypeMstEntity.settLtime(helper.getCurrentTime());
            custPackTypeMstEntity.setcModiuser(datum.getCModiuser());

            custPackTypeMstEntities.add(custPackTypeMstEntity);

            if ((counter + 1) % batchSize == 0 || (counter + 1) == size) {
                custPackTypeMstRepository.saveAll(custPackTypeMstEntities);
                custPackTypeMstEntities.clear();
                log.debug("counter -> {}", counter + 1);
            }
            counter++;
        }
    }

    @Override
    public JsonArray fetchConvertedItemList(String c2Code, String customerCode, JsonArray data) {
        JsonArray response = new JsonArray();
        for (JsonElement element : data) {
            String itemCode = element.getAsJsonObject().get("itemCode").getAsString();
            JsonObject itemObj;
            if (element.getAsJsonObject().get("itemName") != null &&
                    !element.getAsJsonObject().get("itemName").isJsonNull()) {
                itemObj = fetchConvertedItemDetails(c2Code, customerCode, itemCode,
                        element.getAsJsonObject().get("itemName").getAsString());
            } else {
                itemObj = fetchConvertedItemDetails(c2Code, customerCode, itemCode);
            }
            response.add(itemObj);
        }
        return response;
    }

    @Override
    public ItemPDPResponseBO getById(String itemCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        LcItem lcItem = item.getById(itemCode);
        // lcItem.setMoleculeInfo(moleculeRepository.getById(lcItem.getMoleculeCode()));
        if (lcItem == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        ItemPDPResponseBO pdp = getPDPResponse(lcItem);
        pdp.setShortbookStatus(lcShortBookMongoRepository.getByItemAndBranch(itemCode, helper.toString(lcHeaderBO.getFirmId()), lcHeaderBO.getUserId(), lcHeaderBO.getFirmId()) == null
                ? Constants.STATUS_NO : Constants.STATUS_YES);
        pdp.setWatchListStatus(lcWatchListMongoRepository.getByItemAndBranch(itemCode, helper.toString(lcHeaderBO.getFirmId()), lcHeaderBO.getUserId(), lcHeaderBO.getFirmId()) == null
                ? Constants.STATUS_NO : Constants.STATUS_YES);
        pdp.setRelatedItem(new ArrayList<>());
        pdp.setDiscountStatus(Constants.STATUS_NO);
        return pdp;

    }

    private ItemPDPResponseBO getPDPResponse(LcItem lcItem) {
        ItemPDPResponseBO pdp = new ItemPDPResponseBO();
        pdp.setItemCode(lcItem.getItemCode());
        pdp.setItemName(lcItem.getItemName());
        pdp.setContains(lcItem.getContains());
        pdp.setPackName(lcItem.getPackName());
        pdp.setMfgCode(lcItem.getMfgCode());
        pdp.setMfgName(lcItem.getMfgName());
        pdp.setMrp(lcItem.getMrp());
        pdp.setGst(lcItem.getGstCode());
        pdp.setPackTypeName(lcItem.getPackTypeName());
        //pdp.setImageUrl(lcItem.getImageUrl());
        List<ItemMoleculeBo> moleculeBos = new ArrayList<>();
        if (lcItem.getMolecules() != null) {
            for (LcMolecule lcMolecule : lcItem.getMolecules()) {
                ItemMoleculeBo itemMoleculeBo = new ItemMoleculeBo();

                itemMoleculeBo.setMoleculeCode(lcMolecule.getMoleculeCode());
                itemMoleculeBo.setMoleculeName(lcMolecule.getDrugName());
                itemMoleculeBo.setContraIndications(lcMolecule.getContraIndications());
                itemMoleculeBo.setDescription(lcMolecule.getTherapeuticClass());
                itemMoleculeBo.setNote(lcMolecule.getIndications());
                itemMoleculeBo.setUsage(lcMolecule.getDosageForms());
                itemMoleculeBo.setSideEfforts(lcMolecule.getPregnancyCategory());
                moleculeBos.add(itemMoleculeBo);
            }
        }
        pdp.setGst(lcItem.getGstCode());
        pdp.setPackSize(lcItem.getPackSize());
        pdp.setBarcode(lcItem.getBarCode());
        pdp.setHsnCode(lcItem.getHsnCode());
        pdp.setMolecules(moleculeBos);
        List<ItemImageBO> itemImageBOS = new ArrayList<>();
        List<ThumbnailBO> thumbnailBOS = new ArrayList<>();
        ThumbnailBO thumbnailBO = new ThumbnailBO();
        ItemImageBO itemImageBO = new ItemImageBO();
        if (!helper.isEmpty(lcItem.getImgUrl())){
            thumbnailBO.setThumbnailImage(lcItem.getImgUrl());
            itemImageBO.setItemImage(lcItem.getImgUrl());
        }
        itemImageBOS.add(itemImageBO);
        thumbnailBOS.add(thumbnailBO);
        pdp.setImageBOS(thumbnailBOS);
        pdp.setThumbnail(itemImageBOS);
        return pdp;
    }

    @Override
    public ItemPDPResponseBO getByBarCode(String barCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        LcItem lcItem = lcItemRepository.getByBarCode(barCode);
        if (lcItem == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        // lcItem.setMoleculeInfo(moleculeRepository.getById(lcItem.getMoleculeCode()));
        ItemPDPResponseBO pdp = new ItemPDPResponseBO();
        pdp.setItemCode(lcItem.getItemCode());
        pdp.setItemName(lcItem.getItemName());
        pdp.setContains(lcItem.getContains());
        pdp.setPackName(lcItem.getPackName());
        pdp.setMfgCode(lcItem.getMfgCode());
        pdp.setMfgName(lcItem.getMfgName());
        pdp.setMrp(lcItem.getMrp());
        List<ItemMoleculeBo> moleculeBos = new ArrayList<>();
        if (lcItem.getMolecules() != null) {
            for (LcMolecule lcMolecule : lcItem.getMolecules()) {
                ItemMoleculeBo itemMoleculeBo = new ItemMoleculeBo();

                itemMoleculeBo.setMoleculeCode(lcMolecule.getMoleculeCode());
                itemMoleculeBo.setMoleculeName(lcMolecule.getDrugName());
                itemMoleculeBo.setContraIndications(lcMolecule.getContraIndications());
                itemMoleculeBo.setDescription(lcMolecule.getTherapeuticClass());
                itemMoleculeBo.setNote(lcMolecule.getIndications());
                itemMoleculeBo.setUsage(lcMolecule.getDosageForms());
                itemMoleculeBo.setSideEfforts(lcMolecule.getPregnancyCategory());
                moleculeBos.add(itemMoleculeBo);
            }
        }

        pdp.setGst(lcItem.getGstCode());
        pdp.setPackSize(lcItem.getPackSize());
        pdp.setBarcode("");
        pdp.setHsnCode(lcItem.getHsnCode());
        pdp.setShortbookStatus(lcShortBookMongoRepository.getByItemAndBranch(lcItem.getItemCode(), helper.toString(lcHeaderBO.getFirmId()), lcHeaderBO.getUserId(), lcHeaderBO.getFirmId()) == null
                ? Constants.STATUS_NO : Constants.STATUS_YES);
        pdp.setWatchListStatus(lcWatchListMongoRepository.getByItemAndBranch(lcItem.getItemCode(), helper.toString(lcHeaderBO.getFirmId()), lcHeaderBO.getUserId(), lcHeaderBO.getFirmId()) == null
                ? Constants.STATUS_NO : Constants.STATUS_YES);
        pdp.setMolecules(moleculeBos);
        pdp.setImageBOS(new ArrayList<>());
        pdp.setThumbnail(new ArrayList<>());
        pdp.setRelatedItem(new ArrayList<>());
        return pdp;
    }

    private JsonObject fetchConvertedItemDetails(String c2Code, String customerCode, String itemCode) {
        String sql = " SELECT c_stockiest_item_code FROM u_stockiest_item WHERE c_stockiest_code = :customerCode " +
                " AND c_ucode = (SELECT c_ucode FROM u_stockiest_item WHERE c_stockiest_code = :c2code " +
                " AND c_stockiest_item_code = :itemCode ) ORDER BY d_ldate DESC LIMIT 1 ";
        Query query = this.getQuery(sql);
        query.setParameter("c2code", c2Code);
        query.setParameter("itemCode", itemCode);
        query.setParameter("customerCode", customerCode);
        String convertedItemCode = this.getSingleResultNull(query);

        JsonObject res = new JsonObject();
        res.addProperty("itemCode", itemCode);
        res.addProperty("convertedItemCode", convertedItemCode);
        return res;
    }

    private JsonObject fetchConvertedItemDetails(String c2Code, String customerCode, String itemCode, String itemName) {
        String sql = " SELECT usi.c_stockiest_item_code, cim.c_name FROM u_stockiest_item usi INNER JOIN cust_item_mst cim ON " +
                "( usi.c_stockiest_item_code = cim.c_code AND usi.c_stockiest_code = cim.c_c2code ) " +
                "WHERE usi.c_stockiest_code = :customerCode " +
                " AND usi.c_ucode = (SELECT c_ucode FROM u_stockiest_item WHERE c_stockiest_code = :c2code " +
                " AND c_stockiest_item_code = :itemCode ) ORDER BY usi.d_ldate DESC LIMIT 1 ";
        Query query = this.getQuery(sql);
        query.setParameter("c2code", c2Code);
        query.setParameter("itemCode", itemCode);
        query.setParameter("customerCode", customerCode);
        Object convertedItemCode = this.getSingleResult(query);

        JsonObject res = new JsonObject();

        res.addProperty("itemCode", itemCode);
        res.addProperty("itemName", itemName);
        if (convertedItemCode instanceof Object[]) {
            res.addProperty("convertedItemCode", (String) (((Object[]) convertedItemCode))[0]);
            res.addProperty("convertedItemName", (String) (((Object[]) convertedItemCode))[1]);
        }
        // res.addProperty("convertedItemCode", convertedItemCode);
        return res;
    }

    @Override
    public JsonArray getItemSummary(JsonObject data) throws RecordNotFoundException {
        JsonArray jsonArray = data.getAsJsonArray("itemList");
        List<ItemDetailModel> itemList = new ArrayList<>();
        JsonArray itemSummaryArray = new JsonArray();

        for (JsonElement jsonElement : jsonArray) {
            String itemCode = jsonElement.getAsString();
            ItemDetailModel itemDetailModel = updateSummary(itemCode);
            JsonObject jsonObject = helper.getJsonObject(itemDetailModel.getData());
            itemSummaryArray.add(jsonObject);
        }
        return itemSummaryArray;
    }

    @Override
    public ItemDetailModel updateSummary(String itemCode) throws RecordNotFoundException {
        UItemMstEntity uItemMstEntity = itemRepository.findByItemCode(itemCode);
        if (uItemMstEntity == null) {
            throw new RecordNotFoundException("Item Not Found!");
        }

        //get sub masters and price details ,gst
        JsonObject itemJson = getGeneralDetails(uItemMstEntity);

        //set image urls
        setImageUrls(uItemMstEntity, itemJson);
        setThumbNailUrls(uItemMstEntity, itemJson);

        ItemDetailModel itemDetailMongoModel = new ItemDetailModel();
        itemDetailMongoModel.setCItemCode(itemCode);
        //itemDetailMongoModel.setData(itemJson.toString());
        itemDetailMongoModel.setData(BasicDBObject.parse(itemJson.toString()));

        //save
        ItemSummaryModel itemSummaryModel = new ItemSummaryModel();
        BeanUtils.copyProperties(itemDetailMongoModel, itemSummaryModel);
        itemSummaryRepository.save(itemSummaryModel);
        return itemDetailMongoModel;
    }

    private JsonObject getGeneralDetails(UItemMstEntity uItemMstEntity) throws RecordNotFoundException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("c_item_code", uItemMstEntity.getcCode());
        jsonObject.addProperty("c_item_name", uItemMstEntity.getcName());
        //sub masters
        JsonObject subDetails = new JsonObject();
        setItemSubDetailsMfac(uItemMstEntity.getcItemMfacCode(), subDetails);
        setItemSubDetailsPack(uItemMstEntity.getcItemPackCode(), subDetails);
        setItemSubDetailsContent(uItemMstEntity.getcItemContCode(), subDetails);
        setItemSubDetailsBrand(uItemMstEntity.getcItemBrandCode(), subDetails);
        setItemSubDetailsMolecule(uItemMstEntity.getcCode(), subDetails);
        jsonObject.add("j_sub_detail", subDetails);
        //MRP GST HSN
        setExtraDetails(uItemMstEntity, jsonObject);
        return jsonObject;
    }

    private void setItemSubDetailsMfac(String mfacCode, JsonObject subDetails) throws RecordNotFoundException {
        UItemMfacMstEntity uItemMfacMstEntity = itemMfacService.getMfacMst(mfacCode);
        if (uItemMfacMstEntity == null) {
            throw new RecordNotFoundException("Manufacturer Not Found!");
        }
        subDetails.addProperty("c_mfac_code", uItemMfacMstEntity.getcCode());
        subDetails.addProperty("c_mfac_name", uItemMfacMstEntity.getcName());
    }

    private void setItemSubDetailsPack(String packCode, JsonObject subDetails) throws RecordNotFoundException {
        UItemPackMstEntity uItemPackMstEntity = itemPackService.getPackMst(packCode);
        if (uItemPackMstEntity == null) {
            throw new RecordNotFoundException("Packing Not Found!");
        }
        subDetails.addProperty("c_pack_code", uItemPackMstEntity.getcCode());
        subDetails.addProperty("c_pack_name", uItemPackMstEntity.getcName());
    }

    private void setItemSubDetailsContent(String contCode, JsonObject subDetails) throws RecordNotFoundException {
        UItemContMstEntity uItemContMstEntity = itemContentService.getContMst(contCode);
        if (uItemContMstEntity == null) {
            throw new RecordNotFoundException("Content Not Found!");
        }
        subDetails.addProperty("c_cont_code", uItemContMstEntity.getcCode());
        subDetails.addProperty("c_cont_name", uItemContMstEntity.getcName());
    }

    private void setItemSubDetailsBrand(String brandCode, JsonObject subDetails) throws RecordNotFoundException {
        UItemBrandMstEntity uItemContMstEntity = itemBrandService.getBrandMst(brandCode);
        if (uItemContMstEntity == null) {
            subDetails.addProperty("c_brand_code", "NA");
            subDetails.addProperty("c_brand_name", "NA");
            return;
            //throw new RecordNotFoundException("Brand Not Found!");
        }
        subDetails.addProperty("c_brand_code", uItemContMstEntity.getcCode());
        subDetails.addProperty("c_brand_name", uItemContMstEntity.getcName());
    }

    private void setItemSubDetailsMolecule(String itemCode, JsonObject subDetails) throws RecordNotFoundException {
        UItemMoleculeDetailsEntity uItemMoleculeDetailsEntity = uItemMoleculeDetailsRepository.findByItemCode(itemCode);
        if (uItemMoleculeDetailsEntity == null) {
            subDetails.addProperty("c_molecule_code", "NA");
            subDetails.addProperty("c_molecule_name", "NA");
            return;
            //throw new RecordNotFoundException("Content Not Found!");
        }
        subDetails.addProperty("c_molecule_code", uItemMoleculeDetailsEntity.getcMoleculeCode());
        subDetails.addProperty("c_molecule_name", uItemMoleculeDetailsEntity.getcMoleculeName());
    }

    private void setExtraDetails(UItemMstEntity uItemMstEntity, JsonObject itemJson) {
        Double d_mrp = getItemMrp(uItemMstEntity.getcCode());
        d_mrp = (d_mrp == 0.00) ? helper.getBigDecimal(uItemMstEntity.getnMaxMrp()).doubleValue() : d_mrp;
        String c_gst = helper.isEmpty(uItemMstEntity.getcGstCode()) ? getGstCode(uItemMstEntity.getcHsnCode()) : uItemMstEntity.getcGstCode();
        itemJson.addProperty("n_mrp", d_mrp);
        itemJson.addProperty("n_gst", c_gst);
        if (!helper.isEmpty(uItemMstEntity.getcHsnCode())) {
            itemJson.addProperty("c_hsn_sac", uItemMstEntity.getcHsnCode());
        }
    }

    private Double getItemMrp(String itemCode) {
        UItemPriceEntity uItemPriceEntity = uItemPriceRepository.findByItemCode(itemCode);
        return (uItemPriceEntity == null) ? 0.00 : uItemPriceEntity.getnMrp().doubleValue();
    }

    private String getGstCode(String HSNSAC) {
        HsnGstMappingEntity hsnGstMappingEntity = hsnGstMappingRepository.findByHsnCode(HSNSAC);
        return (hsnGstMappingEntity == null) ? "NA" : hsnGstMappingEntity.getcGstCode();
    }

    private void setImageUrls(UItemMstEntity uItemMstEntity, JsonObject itemJson) {
        //need item image data //will fetch from repo here
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(uItemMstEntity.getcWebImgLink());
        itemJson.add("a_image_urls", jsonArray);
    }

    private void setThumbNailUrls(UItemMstEntity uItemMstEntity, JsonObject itemJson) {
        //need item image data //will fetch from repo here
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(uItemMstEntity.getcWebImgLink());
        itemJson.add("a_thumbnail_urls", jsonArray);
    }

    @Override
    public ItemPDPResponseBO getItemSummary(String itemCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {

        LcItem lcItem = item.getById(itemCode);
        if (lcItem == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        ItemPDPResponseBO pdp = new ItemPDPResponseBO();
        pdp.setItemCode(lcItem.getItemCode());
        pdp.setItemName(lcItem.getItemName());
        pdp.setContains(lcItem.getContains());
        pdp.setPackName(lcItem.getPackName());
        pdp.setMfgCode(lcItem.getMfgCode());
        pdp.setMfgName(lcItem.getMfgName());
        pdp.setMrp(lcItem.getMrp());
        pdp.setGst(lcItem.getGstCode());
        pdp.setGst(lcItem.getGstCode());
        pdp.setPackSize(lcItem.getPackSize());
        pdp.setBarcode(lcItem.getBarCode());
        pdp.setHsnCode(lcItem.getHsnCode());
        pdp.setShortbookStatus(lcShortBookMongoRepository.getByItemAndBranch(itemCode, helper.toString(lcHeaderBO.getFirmId()), lcHeaderBO.getUserId(), lcHeaderBO.getFirmId()) == null
                ? Constants.STATUS_NO : Constants.STATUS_YES);
        pdp.setWatchListStatus(lcWatchListMongoRepository.getByItemAndBranch(itemCode, helper.toString(lcHeaderBO.getFirmId()), lcHeaderBO.getUserId(), lcHeaderBO.getFirmId()) == null
                ? Constants.STATUS_NO : Constants.STATUS_YES);
        List<ItemImageBO> itemImageBOS = new ArrayList<>();
        List<ThumbnailBO> thumbnailBOS = new ArrayList<>();
        ThumbnailBO thumbnailBO = new ThumbnailBO();
        ItemImageBO itemImageBO = new ItemImageBO();
        if (!helper.isEmpty(lcItem.getImgUrl())) {
            thumbnailBO.setThumbnailImage(lcItem.getImgUrl());
            itemImageBO.setItemImage(lcItem.getImgUrl());
        }
        itemImageBOS.add(itemImageBO);
        thumbnailBOS.add(thumbnailBO);
        pdp.setImageBOS(thumbnailBOS);
        pdp.setThumbnail(itemImageBOS);
        pdp.setRelatedItem(new ArrayList<>());
        return pdp;
    }

    @Override
    public List<Object[]> getTrendingProducts(String c2Code, PageBO pageBO) throws RecordNotFoundException {
        List<String> itemList = new ArrayList<>();
        String sql = getTrendingProdUCode();
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", c2Code);
        return this.getResultList(query, pageBO.getPage(), pageBO.getLimit());

//        if (resultList.size() > 0) {
//            for (int i = 0; i < resultList.size(); i++) {
//                item = helper.getString(resultList.get(i));
//                list.add(item);
//            }
//        } else {
//            throw new RecordNotFoundException("No Trending Products!");
//        }
//        return list;
    }

    @Override
    public int getTrendingProdCount(String c2Code) {
        BigInteger count = BigInteger.ZERO;
        String sql = "SELECT COUNT(*) FROM ("+getTrendingProdUCode()+") DUMMY";
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", c2Code);
        Object obj = this.getSingleResult(query);

        if (obj != null) {
            count = (BigInteger) obj;
        }
        return count.intValue();
    }

    private String getTrendingProdUCode() {
        return "select usi.c_ucode, cbis.n_rate, usi.c_stockiest_item_code " +
                "from cust_popular_items cpi " +
                "JOIN u_stockiest_item usi on cpi.c_c2code = usi.c_stockiest_code and cpi.c_item_code = usi.c_stockiest_item_code " +
                " LEFT JOIN u_fastmoving_items fm on usi.c_ucode = fm.c_ucode "+
                " LEFT JOIN cust_branch_item_stock cbis FORCE INDEX(PRIMARY) on cbis.c_c2code = usi.c_stockiest_code and cbis.c_item_code = usi.c_stockiest_item_code  "+
               //" LEFT join deal_of_the_day dotd on dotd.c_c2code = cbis.c_c2code and dotd.c_item_code = cbis.c_item_code " +
                " INNER JOIN lc_c2code_mst lccm on lccm.c_code = cbis.c_c2code and lccm.c_cust_branch_code = cbis.c_br_code "+
                " WHERE cpi.c_c2code = :c2Code ORDER BY coalesce(fm.n_count, 0) DESC ";
    }

    @Override
    public List<Object[]> getRecentSearch(LcHeaderBO lcHeaderBO, PageBO pageBO) throws CommunicationErrorException, RecordNotFoundException {
        List<Object[]> resultArray = new ArrayList<>();
        List<Object[]> finalResult = new ArrayList<>();
        String item;
        Map<String, String> headers = new HashMap<>();
        headers.put("x-csquare-terminal-id", helper.getLongStringValue(lcHeaderBO.getUserId()));
        JsonObject jsonObject = callCustomerService(headers, getRecentItemUrl);
        JsonArray array = new JsonArray();
        if (jsonObject!= null && jsonObject.has("j_item_codes")) {
            array = jsonObject.get("j_item_codes").getAsJsonArray();
        }

        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                //Object result = getUCode(array.get(i).getAsString(), lcHeaderBO);
                String sql = getRecentSearchProducts();
                Query query = this.getQuery(sql);
                query.setParameter("customer_item_codes", array.get(i).getAsString());
                query.setParameter("c2code", lcHeaderBO.getC2Code());
                resultArray =  this.getResultList(query, pageBO.getPage(), pageBO.getLimit());
                finalResult.addAll(resultArray);
            }
        } else {
            throw new RecordNotFoundException("No Recent Searched Items!");
        }
      return finalResult;

    }

    private String getRecentSearchProducts() {
        String sql = "  SELECT usi.c_ucode, cbis.n_rate, usi.c_stockiest_item_code   " +
                " FROM u_stockiest_item usi " +
                " LEFT JOIN cust_branch_item_stock cbis FORCE INDEX(PRIMARY) on cbis.c_c2code = usi.c_stockiest_code and cbis.c_item_code = usi.c_stockiest_item_code " +
                //" LEFT join deal_of_the_day dotd on dotd.c_c2code = cbis.c_c2code and dotd.c_item_code = cbis.c_item_code  " +
                " INNER JOIN lc_c2code_mst lccm on lccm.c_code = cbis.c_c2code and lccm.c_cust_branch_code = cbis.c_br_code " +
                " WHERE usi.c_stockiest_item_code = :customer_item_codes and usi.c_stockiest_code=:c2code  " ;
        return sql;
    }

    @Override
    public int getRecentItemsCount(LcHeaderBO lcHeaderBO) throws CommunicationErrorException {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-csquare-terminal-id", helper.getLongStringValue(lcHeaderBO.getUserId()));
        JsonObject jsonObject = callCustomerService(headers, getRecentItemUrl);
        JsonArray array = jsonObject.get("j_item_codes").getAsJsonArray();
        return array.size();
    }

    private JsonObject callCustomerService(Map<String, String> headers, String getRecentItemUrl) throws CommunicationErrorException {
        String result = callWebClientGetSyncApi(getRecentItemUrl, headers);
        log.debug("Customer Service Response : {}" + result);

        JsonObject responseObject;
        if (result == null || result.isEmpty()) {
            log.error("Result is null API {} -- Response {}", getRecentItemUrl, result);
            throw new CommunicationErrorException("", "Error connecting to Seller Detail!");
        } else {
            responseObject = helper.getJsonObject(result);
            if (responseObject.get("appStatusCode").getAsInt() != 0) {
                log.debug("Response {}", result);
            }
        }
        return responseObject.get("payloadJson").getAsJsonObject();
    }

    @Override
    public ItemPDPResponseBO getProductDetail(String itemCode, LcHeaderBO lcHeaderBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException, DataFormatException {
        saveIntoRecentSearchedItem(itemCode, lcHeaderBO);
        ItemPDPResponseBO pdp = new ItemPDPResponseBO();
        List<Object[]> result = getUCodeWithDeal(itemCode, lcHeaderBO);
       // Object result = getUCode(itemCode,lcHeaderBO);
        if (!result.isEmpty()) {
            for (Object[] res : result) {
                LcItem lcItem = item.getById(helper.getString(res[0]));
                if (lcItem == null) {
                    throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
                }
                pdp = getPDPResponse(lcItem);
                pdp.setMrp(helper.getBigDecimal(res[1]));
                pdp.setSellerItemCode(helper.getString(res[2]));
                List<Object[]> dealItems = dealOfTheDayService.getDealItems(lcHeaderBO);
                for (Object[] object1 : dealItems){
                    if(helper.getString(object1[0]).equals(helper.getString(res[2]))){
                        pdp.setOfferRate(helper.getBigDecimal(object1[1]));
                        pdp.setStartDateTime(helper.getString(object1[2]));
                        pdp.setEndDateTime(helper.getString(object1[3]));
                        pdp.setDiscType(helper.getString(object1[4]));
                        pdp.setDiscAmount(helper.getBigDecimal(object1[6]));
                        pdp.setDiscPercentage(helper.getBigDecimal(object1[5]));
                    }
                }

            }

        } else {
            throw new RecordNotFoundException("RECORD NOT FOUND!");
        }
        addToMostViewedProducts(lcHeaderBO.getBrCode(),lcHeaderBO.getC2Code(),itemCode,pdp);
        return pdp;
    }

    private void addToMostViewedProducts(String brCode, String c2Code, String itemCode, ItemPDPResponseBO pdp) {
        Criteria criteria = Criteria.where("c_c2_code").is(c2Code).and("c_br_code").is(brCode)
                .and("c_seller_item_code").is(itemCode);
        org.springframework.data.mongodb.core.query.Query query = org.springframework.data.mongodb.core.query.Query.query(criteria);
        MostViewedProduct mostViewedPrd = mongoTemplate.findOne(query, MostViewedProduct.class);
        if (mostViewedPrd != null) {
            mostViewedPrd.setNViewedCount(mostViewedPrd.getNViewedCount()+1);
            mostViewedProductRepository.save(mostViewedPrd);
        } else {
            MostViewedProduct mostViewedProduct = new MostViewedProduct();
            mostViewedProduct.setCBrCode(brCode);
            mostViewedProduct.setCSellerItemCode(itemCode);
            mostViewedProduct.setCItemCode(pdp.getItemCode());
            mostViewedProduct.setCC2Code(c2Code);
            mostViewedProduct.setCItemName(pdp.getItemName());
            mostViewedProduct.setCPacking(pdp.getPackName());
            mostViewedProduct.setNViewedCount(1);
            mostViewedProduct.setCItemImg(pdp.getImageBOS());
            mostViewedProductRepository.save(mostViewedProduct);
        }
    }

    public long chkStockAvailability(String c2Code, String brCode, String itemCode) {
        BigInteger count = BigInteger.ZERO;
        String sql = "SELECT coalesce(cbis.n_bal_qty, 0) as n_bal_qty " +
                " From cust_branch_item_stock cbis  " +
                " Where cbis.c_c2code = '"+c2Code+"' AND cbis.c_item_code = '"+itemCode+"' " +
                " AND cbis.c_br_code = '"+brCode+"' ";
        Query query = this.getQuery(sql);
        Object result = this.getSingleResult(query);
        if (result != null) {
            count = (BigInteger) result;
        }
        return count.intValue();
    }

    private List<Object[]> getUCodeWithDeal(String itemCode, LcHeaderBO lcHeaderBO) {
       String sql = "  SELECT usi.c_ucode, cbis.n_rate, usi.c_stockiest_item_code  " +
                " FROM u_stockiest_item usi " +
                " LEFT JOIN cust_branch_item_stock cbis FORCE INDEX(PRIMARY) on cbis.c_c2code = usi.c_stockiest_code and cbis.c_item_code = usi.c_stockiest_item_code " +
                " INNER JOIN lc_c2code_mst lccm on lccm.c_code = cbis.c_c2code and lccm.c_cust_branch_code = cbis.c_br_code " +
                " where usi.c_stockiest_code = :c2Code and usi.c_stockiest_item_code = :itemCode " ;
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", lcHeaderBO.getC2Code());
        query.setParameter("itemCode", itemCode);
        return this.getResultList(query);
    }

    private Object getUCode(String itemCode, LcHeaderBO lcHeaderBO) {
        String sql = "select c_ucode from u_stockiest_item usi where c_stockiest_code = :c2Code and c_stockiest_item_code = :itemCode";
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", lcHeaderBO.getC2Code());
        query.setParameter("itemCode", itemCode);
        return this.getSingleResult(query);
    }

    private void saveIntoRecentSearchedItem(String itemCode, LcHeaderBO lcHeaderBO) throws CommunicationErrorException, InvalidRequestException, RecordNotFoundException {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-csquare-terminal-id", helper.getLongStringValue(lcHeaderBO.getUserId()));
        callCustomerService(headers, saveRecentItemUrl, itemCode);
    }

    @Override
    public List<String> getItemsByCategory(LcHeaderBO header, JsonObject request) {
        List<String> categoryList = new ArrayList<>();
        List<String> itemList = new ArrayList<>();
        JsonArray arr = request.get("j_item_codes").getAsJsonArray();

        for (int i = 0; i<arr.size(); i++) {
            itemList.add(arr.get(i).getAsString());
        }

        Criteria criteria = Criteria.where("_id").in(itemList).and("c_gst_code").ne(null);
        org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query().addCriteria(criteria);
        List<LcItem> lcCategories = mongoOperations.find(query, LcItem.class);
        if (!lcCategories.isEmpty()) {
            for (LcItem lcItem : lcCategories) {
                categoryList.add(lcItem.getCategoryCode());
            }
        }

        return categoryList;

//        criteria = Criteria.where("c_item_cat_code").in(categoryList).and("_id").nin(itemList);
//        query = new org.springframework.data.mongodb.core.query.Query().addCriteria(criteria);
//        query.limit(request.get("n_limit").getAsInt());
//        query.skip((long) request.get("n_page").getAsInt() * request.get("n_limit").getAsInt());
//        List<LcItem> lcItems = mongoOperations.find(query, LcItem.class);
//
//        for (LcItem lcItem : lcItems) {
//            ItemPLPResponseBO plp = getPLPResponseBO(lcItem);
//            plp.setPackSize(lcItem.getPackSize());
//            plp.setMfgCode(lcItem.getMfgCode());
//            plp.setMfgName(lcItem.getMfgName());
//            plpList.add(plp);
//        }
//        return plpList;
    }

    public void callCustomerService(Map<String, String> headers, String url, String itemCode) throws CommunicationErrorException, InvalidRequestException, RecordNotFoundException {
        JsonObject request = new JsonObject();
        request.addProperty("c_item_code", itemCode);

        String result = callWebClientPostSyncApiWithHeader(url, request.toString(), headers);
        log.debug("Customer Service Response : {}" + result);

        JsonObject responseObject;
        if (result == null || result.isEmpty()) {
            log.error("Result is null API {} -- Response {}", url, result);
            throw new CommunicationErrorException("", "Error connecting to Seller Detail!");
        } else {
            responseObject = helper.getJsonObject(result);
            if (responseObject.get("appStatusCode").getAsInt() != 0) {
                log.debug("Response {}", result);
            }
        }
    }

    @Override
    public List<ItemPLPResponseBO> getRecentSearchPLP(List<String> items, PageBO pageBo) {
        List<ItemPLPResponseBO> list = new ArrayList<>();
        Criteria criteria = Criteria.where("_id").in(items).and("c_gst_code").ne(null); // add the in clause

        org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query().addCriteria(criteria);
        List<LcItem> lcItems = mongoOperations.find(query, LcItem.class);
        for (String itemCode : items) {
            LcItem lcItem = lcItems.stream().filter(item -> itemCode.equals(item.getItemCode())).findAny().orElse(null);
            if (lcItem != null) {
                ItemPLPResponseBO plp = getPLPResponseBO(lcItem);
                plp.setPackSize(lcItem.getPackSize());
                plp.setMfgCode(lcItem.getMfgCode());
                plp.setMfgName(lcItem.getMfgName());
                plp.setOfferRate(BigDecimal.ZERO);
                plp.setNDiscount(BigDecimal.ZERO);
                list.add(plp);
            }
        }
        return list;
    }
}

