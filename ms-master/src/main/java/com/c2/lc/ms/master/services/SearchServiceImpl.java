package com.c2.lc.ms.master.services;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.MostViewedPrdsBO;
import com.c2.lc.ms.master.bos.ThumbnailBO;
import com.c2.lc.ms.master.entities.elastic.ElLcItem;
import com.c2.lc.ms.master.entities.mongo.LcCart;
import com.c2.lc.ms.master.entities.mongo.LcItem;
import com.c2.lc.ms.master.entities.mongo.MostViewedProduct;
import com.c2.lc.ms.master.repos.elastic.LcItemElRepository;
import com.c2.lc.ms.master.repos.mongo.CartRepository;
import com.c2.lc.ms.master.repos.mongo.LcItemRepository;
import com.c2.lc.ms.master.repos.mongo.MostViewedProductRepository;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.CatalogueService;
import com.c2.lc.ms.master.services.interfaces.SearchService;
import com.c2.lc.ms.master.services.interfaces.ShortBookWatchListService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
@Service
public class SearchServiceImpl extends MasterBaseServiceImpl implements SearchService {

    @Autowired private ShortBookWatchListService shortBookWatchListService;
    @Autowired private MongoOperations mongoOperations;
    @Autowired private LcItemRepository lcItemRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private CatalogueService catalogueService;
    @Autowired private LcItemElRepository elRepository;
    @Autowired private MongoTemplate mongoTemplate;
    @Autowired private MostViewedProductRepository mostViewedProductRepository;


    @Value("${ms.cust.mapped.service.api.url}")
    private String mappedSellerUrl;
    @Value("${ms.cust.seller.service.api.url}")
    private String sellerUrl;

    @Override
    public JsonArray getProductDetails(LcHeaderBO headerBO, SearchBO searchBO) throws RecordNotFoundException {

       // org.springframework.data.mongodb.core.query.Query query = this.getMongoSearchParameter("c_item_name", searchBO);
        Criteria criteria = Criteria.where("c_item_name").regex(helper.getMongoSearchParameter(searchBO.getSearchTerm()), "i")
                .and("n_mrp").gt(0);

        org.springframework.data.mongodb.core.query.Query query = org.springframework.data.mongodb.core.query.Query.query(criteria).with(Sort.by(Sort.Direction.DESC, "selling_count"));
        query.limit(searchBO.getLimit());
        query.skip((long) searchBO.getPage() * searchBO.getLimit());

        List<LcItem> lcItems = mongoOperations.find(query, LcItem.class);
        JsonArray jsonArray = new JsonArray();
        if (lcItems.isEmpty()) {
            return jsonArray;
            //throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }

        for (LcItem lcItem : lcItems) {
            JsonObject jsonObject = new JsonObject();
            String itemCode = lcItem.getItemCode();
            jsonObject.addProperty("c_item_code", itemCode);
            jsonObject.addProperty("c_item_ucode", itemCode);
            jsonObject.addProperty("c_item_name", lcItem.getItemName());
            jsonObject.addProperty("c_item_mfg_code", lcItem.getMfgCode());
            jsonObject.addProperty("c_item_mfg_name", lcItem.getMfgName());
            jsonObject.addProperty("n_mrp", lcItem.getMrp());
            jsonObject.addProperty("c_pack_name", lcItem.getPackName());
            jsonObject.addProperty("c_variant_count", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_sponsored", Constants.STATUS_NO); //TODO user query to update

            // For TS
            List<ThumbnailBO> thumbnailBOS = new ArrayList<>();
            ThumbnailBO thumbnailBO = new ThumbnailBO();
            if (!helper.isEmpty(lcItem.getImgUrl())){
                thumbnailBO.setThumbnailImage(lcItem.getImgUrl());
            }
            else {
                thumbnailBO.setThumbnailImage("");
            }
            thumbnailBOS.add(thumbnailBO);

            jsonObject.add("ac_thumbnail_images",helper.toJsonArrayTree(thumbnailBOS,List.class));
            jsonObject.addProperty("c_gst_code", lcItem.getGstCode());

            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String preparedStatementForProduct() {
        return " SELECT " +
                "   lusr.c_ucode itemCode, " +
                "   uim.c_name itemName, " +
                "   uim.c_item_mfac_code, " +
                "   uim.c_web_img_link, " +
                "   uimm.c_name itemMfac, " +
                "   uipm.n_qty_per_box itemQty, " +
                "   uim.n_max_mrp itemMrp, " +
                "   uipm.c_name packSize,  " +
                "   uicm.c_name contentName  " +
                " FROM " +
                "   lc_ucode_search_recommendation lusr  " +
                "   JOIN " +
                "      u_item_mst uim  " +
                "      ON uim.c_code = lusr.c_ucode  " +
                "   LEFT JOIN " +
                "      u_item_mfac_mst uimm  " +
                "      ON uimm.c_code = uim.c_item_mfac_code  " +
                "   LEFT JOIN " +
                "      u_item_pack_mst uipm  " +
                "      ON uipm.c_code = uim.c_item_pack_code  " +
                "    LEFT JOIN " +
                "     u_item_cont_mst uicm " +
                " on uicm.c_code = uim.c_item_cont_code " +
                "   LEFT JOIN " +
                "      u_stockiest_item usi  " +
                "      ON usi.c_stockiest_item_code = uim.c_item_storage_code  " +
                " WHERE lusr.c_name LIKE :search " +
                " and uim.c_code NOT IN (select c_item_code from  lc_blocked_items)   " +
                " GROUP BY itemName ";
    }

    @Override
    public JsonArray getProductsOnMolecule(LcHeaderBO headerBO, SearchBO searchBO) throws RecordNotFoundException {

        org.springframework.data.mongodb.core.query.Query query = this.getMongoEqualsQuery("molecules.c_molecule_code", searchBO);

        List<LcItem> lcItems = mongoOperations.find(query, LcItem.class);
        JsonArray jsonArray = new JsonArray();
        if (lcItems.isEmpty()) {
            return jsonArray;
           // throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }

        log.debug(lcItems.toString());
        for (LcItem lcItem : lcItems) {
            JsonObject jsonObject = new JsonObject();
/*            for (LcMolecule lcMolecule : lcItem.getMolecules()) {
                if (lcMolecule.getMoleculeName().toLowerCase().contains(searchBO.getSearchTerm().toLowerCase())) {
                    jsonObject.addProperty("c_contains", lcMolecule.getMoleculeName());
                }
            }*/
            jsonObject.addProperty("c_contains", lcItem.getContains());
          /*  if (lcItem.getThumbnailImages() != null && !lcItem.getThumbnailImages().isEmpty())
                jsonObject.addProperty("ac_thumbnail_images", lcItem.getThumbnailImages().get(0));
            else
        
                jsonObject.addProperty("ac_thumbnail_images", "");*/
            List<ThumbnailBO> thumbnailBOS = new ArrayList<>();
            ThumbnailBO thumbnailBO = new ThumbnailBO();
            if (!helper.isEmpty(lcItem.getImgUrl())){
                thumbnailBO.setThumbnailImage(lcItem.getImgUrl());
            }
            else {
                thumbnailBO.setThumbnailImage("");
            }
            thumbnailBOS.add(thumbnailBO);

            jsonObject.add("ac_thumbnail_images",helper.toJsonArrayTree(thumbnailBOS,List.class));
            jsonObject.addProperty("c_item_name", lcItem.getItemName());
            String itemCode = lcItem.getItemCode();
            jsonObject.addProperty("c_item_code", itemCode);
            jsonObject.addProperty("n_mrp", lcItem.getMrp());
            jsonObject.addProperty("c_item_mfac_code", lcItem.getMfgCode());
            jsonObject.addProperty("c_item_mfac_name", lcItem.getMfgName());
            jsonObject.addProperty("c_item_pack_size", lcItem.getPackSize());
            jsonObject.addProperty("c_watchlist_status", Constants.STATUS_NO);
            jsonObject.addProperty("c_short_book_status", Constants.STATUS_NO);
/*
            jsonObject.addProperty("c_watchlist_status", shortBookWatchListService.isWatchList(headerBO.getUserId(),
                    headerBO.getFirmId(), headerBO.getBranchId(), itemCode));
            jsonObject.addProperty("c_short_book_status", shortBookWatchListService.isShortBook(headerBO.getUserId(),
                    headerBO.getFirmId(), headerBO.getBranchId(), itemCode));
*/
            jsonObject.addProperty("c_variant_count", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_cart_status", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_sponsored", Constants.STATUS_NO); //TODO user query to update

            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }


    private String preparedStatementForMolecule() {
        return " SELECT " +
                "   uimm.c_name moleName, " +
                "   uim.c_name itemName, " +
                "   uiml.c_item_code, " +
                "   uim.n_max_mrp itemMrp, " +
                "   uim.c_item_mfac_code," +
                "   uimf.c_name itemMfac, " +
                "   uipm.c_name packSize  " +
                " FROM u_item_molecule_list uiml  " +
                "   LEFT JOIN " +
                "      u_item_molecule_mst uimm  " +
                "      ON uimm.c_code = uiml.c_molecule_code  " +
                "   LEFT JOIN " +
                "      u_item_mst uim  " +
                "      ON uim.c_code = uiml.c_item_code  " +
                "   LEFT JOIN " +
                "      u_item_mfac_mst uimf  " +
                "      On uimf.c_code = uim.c_item_mfac_code  " +
                "   LEFT JOIN " +
                "      u_item_pack_mst uipm  " +
                "      on uipm.c_code = uim.c_item_pack_code  " +
                " WHERE uimm.c_name LIKE :search " +
                " and uim.c_code NOT IN (select c_item_code from  lc_blocked_items) ";
    }

    @Override
    public JsonArray getSellerDetails(SearchBO searchBO) throws RecordNotFoundException {
        // OngoingSchemes to be included for Sellers / Distributers
        String sql = preparedStatementForSellers();
        Query query = this.getQuery(sql);
        query.setParameter("search", searchBO.getSearchTerm()+ "%");

        JsonArray jsonArray = new JsonArray();
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
            //throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }

        for (Object[] objects : resultList) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_seller_code", helper.getString(objects[++i]));
            jsonObject.addProperty("c_seller_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_seller_city", helper.getString(objects[++i]));
            jsonObject.addProperty("n_schemes", 0);
            jsonObject.addProperty("c_sponsored", Constants.STATUS_NO);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String preparedStatementForSellers() {
        return " SELECT " +
                "    lcm.c_code sellerCode, " +
                "    lcm.c_name sellerName, " +
                "    lcm.c_city sellerCity " +
                " FROM lc_c2code_mst lcm " +
                " WHERE lcm.c_name LIKE :search ORDER BY lcm.c_name ASC";
    }

    @Override
    public JsonArray getManufactureDetails(SearchBO searchBO) throws RecordNotFoundException {
        // OngoingOffers to be included for Manufacturers
        String sql = preparedStatementForManufacturers();
        Query query = this.getQuery(sql);
        query.setParameter("search", searchBO.getSearchTerm() + "%");

        JsonArray jsonArray = new JsonArray();
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            return jsonArray;
            //throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }

        for (Object[] objects : resultList) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_manufacture_code", helper.getString(objects[++i]));
            jsonObject.addProperty("c_manufacture_name", helper.getString(objects[++i]));
            jsonObject.addProperty("n_offers", 0);
            jsonObject.addProperty("c_sponsored", Constants.STATUS_NO);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public JsonArray getItemListByCategory(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws RecordNotFoundException {

        String sql = getAllProductByCategory();
        Query  query = this.getQuery(sql);
        if (!helper.isEmpty(searchBO.getSearchTerm())){
            sql = preparedStatementForCategoryProduct();
            query = this.getQuery(sql);
            query.setParameter("search", searchBO.getSearchTerm());
        }
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        JsonArray jsonArray = new JsonArray();
        for (Object[] objects : resultList) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray1 = new JsonArray();
            JsonObject jobj = new JsonObject();
            jsonObject.addProperty("c_item_name", helper.getString(objects[++i]));
            String itemCode = helper.getString(objects[++i]);
            jsonObject.addProperty("c_item_code", itemCode);
            jsonObject.addProperty("n_mrp", Double.parseDouble(objects[++i].toString()));
            jsonObject.addProperty("c_pack_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_contains", helper.getString(objects[++i]));
           // jsonObject.addProperty("ac_thumbnail_images", helper.getString(objects[++i]));
            jobj.addProperty("c_thumbnail_image",helper.getString(objects[++i]));
            jsonArray1.add(jobj);
            jsonObject.add("ac_thumbnail_images",jsonArray1);
           /* jsonObject.addProperty("c_watchlist_status", shortBookWatchListService.isWatchList(lcHeaderBO.getUserId(),
                    lcHeaderBO.getFirmId(), lcHeaderBO.getBranchId(), itemCode));
            jsonObject.addProperty("c_short_book_status", shortBookWatchListService.isShortBook(lcHeaderBO.getUserId(),
                    lcHeaderBO.getFirmId(), lcHeaderBO.getBranchId(), itemCode));*/
            jsonObject.addProperty("c_watchlist_status", Constants.STATUS_NO);
            jsonObject.addProperty("c_short_book_status", Constants.STATUS_NO);
            jsonObject.addProperty("c_scheme_status", Constants.STATUS_NO); //TODO user query to update if scheme is present in preferred seller
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String preparedStatementForCategoryProduct() {

        return " SELECT  " +
                "   uim.c_name itemName,  " +
                "   uichm.c_name categoryName, " +
                "   uim.c_code itemCode,  " +
                "   uim.n_max_mrp itemMrp,  " +
                "   uipm.c_name itemPackName, " +
                "   uicm.c_name itemContentName, " +
                "   uim.c_web_img_link " +
                " FROM u_item_mst uim   " +
                " LEFT JOIN u_item_pack_mst uipm on uipm.c_code = uim.c_item_pack_code   " +
                " LEFT JOIN u_item_cont_mst uicm on uicm.c_code = uim.c_item_cont_code  " +
                " JOIN u_item_cat_mst uicm2 on uim.c_item_cat_code = uicm2.c_code " +
                " JOIN u_item_cat_head_mst uichm on uicm2.c_item_category_head_code = uichm.c_code  " +
                " WHERE uichm.c_item_category_class_code = :search ORDER BY uim.c_name ASC";
    }

    private String getAllProductByCategory() {

        return " SELECT " +
                "   uim.c_name itemName, " +
                "   uim.c_code itemCode, " +
                "   uim.n_max_mrp itemMrp, " +
                "   uipm.c_name itemPackName," +
                "   uicm.c_name itemContentName," +
                "   uim.c_web_img_link" +
                " FROM u_item_mst uim  " +
                "   LEFT JOIN " +
                "      u_item_pack_mst uipm  " +
                "      on uipm.c_code = uim.c_item_pack_code  " +
                "    LEFT JOIN " +
                "     u_item_cont_mst uicm " +
                " on uicm.c_code = uim.c_item_cont_code ORDER BY uim.c_name ASC";
    }


    @Override
    public long countProductsOnCategory(String searchString) {
        String sql = null ;
        Query  query = null;
        if(searchString == null){
             sql = getAllProductByCategory();
             query = this.getQuery(sql);
            return this.getResultList(query).size();
        }
        sql = preparedStatementForCategoryProduct();
        query = this.getQuery(sql);
        query.setParameter("search", searchString);

        return this.getResultList(query).size();
    }

    private String preparedStatementForManufacturers() {
        return " SELECT " +
                "   uimm.c_code mfacCode, " +
                "   uimm.c_name mfacName " +
                " FROM u_item_mfac_mst uimm " +
                " WHERE uimm.c_name LIKE :search ORDER BY uimm.c_name ASC";
    }


    private String preparedStatementForBaseItemDetails() {
        return " SELECT " +
                "   lusr.c_ucode itemCode, " +
                "   uim.c_name itemName, " +
                "   uimm.c_name itemMfac, " +
                "   uipm.n_qty_per_box itemQty, " +
                "   uim.n_max_mrp itemMrp, " +
                "   uipm.c_name packSize  " +
                " FROM " +
                "   lc_ucode_search_recommendation lusr  " +
                "   JOIN " +
                "      u_item_mst uim  " +
                "      ON uim.c_code = lusr.c_ucode  " +
                "   LEFT JOIN " +
                "      u_item_mfac_mst uimm  " +
                "      ON uimm.c_code = uim.c_item_mfac_code  " +
                "   LEFT JOIN " +
                "      u_item_pack_mst uipm  " +
                "      ON uipm.c_code = uim.c_item_pack_code  " +
                "   LEFT JOIN " +
                "      u_stockiest_item usi  " +
                "      ON usi.c_stockiest_item_code = uim.c_item_storage_code  ";

    }

    private String preparedStatementItemDetailsForSeller() {
        return preparedStatementForBaseItemDetails() + " WHERE uim.c_code= :code" +
                "                     GROUP BY itemName";
    }

    private String preparedStatementItemDetailsForMfg() {
        return preparedStatementForBaseItemDetails() + " WHERE uim.c_item_mfac_code=:code" +
                "                     GROUP BY itemName";
    }

    public JsonArray getItemDetails(String type, String code) {
        String sql = null;
        if ("mfc".equals(type))
            sql = preparedStatementItemDetailsForMfg();
        else
            sql = preparedStatementItemDetailsForSeller();

        Query query = this.getQuery(sql);
        query.setParameter("code", code);

        List<Object[]> resultList = this.getResultList(query);
        JsonArray jsonArray = new JsonArray();
        for (Object[] objects : resultList) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_item_code", helper.getString(objects[++i]));
            jsonObject.addProperty("c_item_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_item_mfac", helper.getString(objects[++i]));
            jsonObject.addProperty("n_qty_per_box", helper.getString(objects[++i]));
            jsonObject.addProperty("n_mrp", Double.parseDouble(objects[++i].toString()));
            jsonObject.addProperty("c_item_pack_size", helper.getString(objects[++i]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String preparedStatementMultiItemsForSeller() {
        return " SELECT " +
                "   lusr.c_ucode itemCode, " +
                "   uim.c_name itemName, " +
                "   uimm.c_name itemMfac, " +
                "   uipm.n_qty_per_box itemQty, " +
                "   uim.n_max_mrp itemMrp, " +
                "   uipm.c_name packSize  " +
                " FROM " +
                "   lc_ucode_search_recommendation lusr  " +
                "   JOIN " +
                "      u_item_mst uim  " +
                "      ON uim.c_code = lusr.c_ucode  " +
                "   JOIN  lc_c2code_mst lcm " +
                "       ON uim.c_code = lcm.c_code " +
                "   LEFT JOIN " +
                "      u_item_mfac_mst uimm  " +
                "      ON uimm.c_code = uim.c_item_mfac_code  " +
                "   LEFT JOIN " +
                "      u_item_pack_mst uipm  " +
                "      ON uipm.c_code = uim.c_item_pack_code  " +
                "   LEFT JOIN " +
                "      u_stockiest_item usi  " +
                "      ON usi.c_stockiest_item_code = uim.c_item_storage_code  " +
                " WHERE lcm.c_name LIKE :search " +
                " GROUP BY itemName ";
    }

    private String preparedStatementMultiItemsForMfg() {
        return " SELECT " +
                "   lusr.c_ucode itemCode, " +
                "   uim.c_name itemName, " +
                "   uimm.c_name itemMfac, " +
                "   uipm.n_qty_per_box itemQty, " +
                "   uim.n_max_mrp itemMrp, " +
                "   uipm.c_name packSize  " +
                " FROM " +
                "   lc_ucode_search_recommendation lusr  " +
                "   JOIN " +
                "      u_item_mst uim  " +
                "      ON uim.c_code = lusr.c_ucode  " +
                "   LEFT JOIN " +
                "      u_item_mfac_mst uimm  " +
                "      ON uimm.c_code = uim.c_item_mfac_code  " +
                "   LEFT JOIN " +
                "      u_item_pack_mst uipm  " +
                "      ON uipm.c_code = uim.c_item_pack_code  " +
                "   LEFT JOIN " +
                "      u_stockiest_item usi  " +
                "      ON usi.c_stockiest_item_code = uim.c_item_storage_code  " +
                " WHERE uimm.c_name LIKE :search " +
                " GROUP BY itemName ";
    }

    @Override
    public long countManufacture(String searchString) {
        String sql = preparedStatementForManufacturers();
        Query query = this.getQuery(sql);
        query.setParameter("search", searchString + "%");
        return this.getResultList(query).size();
    }

    @Override
    public long countSeller(String searchString) {
        String sql = preparedStatementForSellers();
        Query query = this.getQuery(sql);
        query.setParameter("search", searchString + "%");

        return this.getResultList(query).size();
    }

    @Override
    public long countMolecule(String searchString) {
        Criteria regex = Criteria.where("molecules.c_molecule_name").regex(".*" + searchString + ".*", "i");
        org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query(regex);
        return mongoOperations.count(query, LcItem.class);
    }

    @Override
    public long countProduct(SearchBO searchBO) {

        Criteria criteria = Criteria.where("c_item_name").regex(helper.getMongoSearchParameter(searchBO.getSearchTerm()), "i")
                .and("n_mrp").gt(0);
        org.springframework.data.mongodb.core.query.Query query = org.springframework.data.mongodb.core.query.Query.query(criteria).with(Sort.by(Sort.Direction.ASC, "selling_count"));
        return mongoOperations.count(query, LcItem.class);
    }

    @Override
    public JsonArray getProductOnManufacture(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws RecordNotFoundException {

        Criteria regex = Criteria.where("c_mfg_code").is(searchBO.getSearchTerm());
        org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query(regex);
        query.skip((long) searchBO.getPage() *searchBO.getLimit());
        query.limit(searchBO.getLimit());
        query.with(Sort.by(Sort.Direction.DESC, "c_item_name"));
        List<LcItem> lcItems = mongoOperations.find(query, LcItem.class);
        JsonArray jsonArray = new JsonArray();
        if (lcItems.size() == 0) {
            return jsonArray;
            //throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }

        for (LcItem lcItem : lcItems) {
            JsonObject jsonObject = new JsonObject();
            String itemCode = lcItem.getItemCode();
            jsonObject.addProperty("c_item_code", itemCode);
            jsonObject.addProperty("c_item_name", lcItem.getItemName());
            jsonObject.addProperty("n_mrp", lcItem.getMrp());
            jsonObject.addProperty("c_pack_name", lcItem.getPackName());
            jsonObject.addProperty("c_contains", lcItem.getContains());
            /*if (lcItem.getThumbnailImages() != null && lcItem.getThumbnailImages().size() > 0)
                jsonObject.addProperty("ac_thumbnail_images", lcItem.getThumbnailImages().get(0));
            else
                jsonObject.addProperty("ac_thumbnail_images", "");*/
            List<ThumbnailBO> thumbnailBOS = new ArrayList<>();
            ThumbnailBO thumbnailBO = new ThumbnailBO();
            if (!helper.isEmpty(lcItem.getImgUrl())){
                thumbnailBO.setThumbnailImage(lcItem.getImgUrl());
            }
            else {
                thumbnailBO.setThumbnailImage("");
            }
            thumbnailBOS.add(thumbnailBO);

            jsonObject.add("ac_thumbnail_images",helper.toJsonArrayTree(thumbnailBOS,List.class));
            /*jsonObject.addProperty("c_watchlist_status", shortBookWatchListService.isWatchList(lcHeaderBO.getUserId(),
                    lcHeaderBO.getFirmId(), lcHeaderBO.getBranchId(), itemCode));
            jsonObject.addProperty("c_short_book_status", shortBookWatchListService.isShortBook(lcHeaderBO.getUserId(),
                    lcHeaderBO.getFirmId(), lcHeaderBO.getBranchId(), itemCode));*/
            jsonObject.addProperty("c_watchlist_status", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_short_book_status", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_discount_status", Constants.STATUS_NO); //TODO user query to update
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public long countProductsOnManufacture(SearchBO searchBO) {
        var query = this.getMongoCount("c_mfg_code", searchBO);
        return mongoOperations.count(query, LcItem.class);
    }

    @Override
    public JsonArray getProductOnSeller(LcHeaderBO lcHeaderBO,SearchBO searchBO) throws RecordNotFoundException {
        String sql = getProductOnSeller();
        Query query = this.getQuery(sql);
        query.setParameter("search", searchBO.getSearchTerm());

        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        JsonArray jsonArray = new JsonArray();
        if (resultList.size() == 0) {
            return jsonArray;
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
            jobj.addProperty("c_thumbnail_image",helper.getString(objects[++i]));
            jsonArray1.add(jobj);
            jsonObject.add("ac_thumbnail_images",jsonArray1);
            //jsonObject.addProperty("ac_thumbnail_images", helper.getString(objects[++i]));
            jsonObject.addProperty("c_item_mfg_code", helper.getString(objects[++i]));
            jsonObject.addProperty("c_item_mfg_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_item_ucode", helper.getString(objects[++i]));
            jsonObject.addProperty("c_discount_status", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_watchlist_status", Constants.STATUS_NO);
            jsonObject.addProperty("c_short_book_status", Constants.STATUS_NO);
           /* jsonObject.addProperty("c_watchlist_status", shortBookWatchListService.isWatchList(lcHeaderBO.getUserId(),
                    lcHeaderBO.getFirmId(), lcHeaderBO.getBranchId(), itemCode));
            jsonObject.addProperty("c_short_book_status", shortBookWatchListService.isShortBook(lcHeaderBO.getUserId(),
                    lcHeaderBO.getFirmId(), lcHeaderBO.getBranchId(), itemCode));*/
            jsonObject.addProperty("c_variant_count", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_cart_status", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_sponsored", Constants.STATUS_NO);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String getProductOnSeller() {
        return " SELECT cust.c_code itemCode, coalesce(uim.c_name, cust.c_name) itemName, cust.n_max_mrp itemMrp, " +
                "   coalesce(uipm.c_name, cpm.c_name) itemPackName," +
                "        coalesce(uicm.c_name, ccm.c_name) itemContentName, coalesce(uim.c_web_img_link, cust.c_web_img_link) as c_web_img_link," +
                "        coalesce(uim.c_item_mfac_code, cust.c_mfac_code) mfcCode, coalesce(uimm.c_name, cmm.c_name) mfcName," +
                "        uim.c_code as c_ucode" +
                "   FROM" +
                "       cust_item_mst cust" +
                "       left join u_stockiest_item usi on cust.c_c2code = usi.c_stockiest_code and cust.c_code = usi.c_stockiest_item_code " +
                "       left join u_item_mst uim on usi.c_ucode = uim.c_code" +
                "       left join u_item_pack_mst uipm on uim.c_item_pack_code = uipm.c_code " +
                "       left join u_item_mfac_mst uimm on uim.c_item_mfac_code = uimm.c_code " +
                "       left join u_item_cont_mst uicm on uim.c_item_cont_code = uicm.c_code " +
                "       LEFT JOIN" +
                "           cust_pack_mst cpm ON cpm.c_code = cust.c_pack_code and cpm.c_c2code=cust.c_c2code" +
                "       LEFT JOIN" +
                "           cust_mfac_mst cmm ON cmm.c_code = cust.c_mfac_code and cmm.c_c2code=cust.c_c2code" +
                "       LEFT JOIN" +
                "           cust_cont_mst ccm ON ccm.c_code = cust.c_cont_code and ccm.c_c2code=cust.c_c2code" +
                "   WHERE cust.c_c2code = :search ORDER BY uim.c_name ASC";
    }

    @Override
    public long countProductsOnSeller(String searchString) {
        String sql = getProductOnSeller();
        Query query = this.getQuery(sql);
        query.setParameter("search", searchString);

        return this.getResultList(query).size();
    }

    @Override
    public JsonArray getProductOnMolecule(SearchBO searchBO) throws RecordNotFoundException {
        Criteria criteria = Criteria.where("molecules.c_molecule_code").is(searchBO.getSearchTerm());
        org.springframework.data.mongodb.core.query.Query query = org.springframework.data.mongodb.core.query.Query.query(criteria);
        query.limit(searchBO.getLimit());
        query.skip((long) searchBO.getPage() *searchBO.getLimit());
        query.with(Sort.by(Sort.Direction.DESC, "selling_count"));
        List<LcItem> lcItems = mongoOperations.find(query, LcItem.class);
        JsonArray jsonArray = new JsonArray();
        if (lcItems.size() == 0) {
            return jsonArray;
            //throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        for (LcItem lcItem : lcItems) {
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray1 = new JsonArray();
            JsonObject jobj = new JsonObject();
            String itemCode = lcItem.getItemCode();
            jsonObject.addProperty("c_item_code", itemCode);
            jsonObject.addProperty("c_item_name", lcItem.getItemName());
            jsonObject.addProperty("c_mfg_code", lcItem.getMfgName());
            jobj.addProperty("c_thumbnail_image", lcItem.getImgUrl());
            jsonArray1.add(jobj);
            jsonObject.add("ac_thumbnail_images", jsonArray1);
            jsonObject.addProperty("c_mfg_name", lcItem.getMfgName());
            jsonObject.addProperty("n_pack_size", lcItem.getPackSize());
            jsonObject.addProperty("n_mrp", lcItem.getMrp());
            jsonObject.addProperty("c_pack_name", lcItem.getPackName());
            jsonObject.addProperty("c_contain_name", lcItem.getContains());
            jsonObject.addProperty("c_gst_code",lcItem.getGstCode());
            jsonObject.addProperty("c_hsn_code",lcItem.getHsnCode());
            if(lcItem.getPackTypeName() == null ){
                jsonObject.addProperty("c_pack_type_name","");
            }
            jsonObject.addProperty("c_pack_type_name",lcItem.getPackTypeName());
            jsonObject.add("molecules",helper.toJsonArrayTree(lcItem.getMolecules(),List.class));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public long countProductsOnMolecule(SearchBO searchBO) {
        var query = this.getMongoCount("molecules.c_molecule_code", searchBO);
        return mongoOperations.count(query, LcItem.class);
    }

    @Override
    public JsonArray getSellerOnProduct(LcHeaderBO lcHeaderBO, SearchBO searchBO,String sellerCode) throws RecordNotFoundException, InvalidRequestException, CommunicationErrorException {
        String cCode = null;
        String sql;
        if (!helper.isEmpty(sellerCode)) {
           // cCode = getCustCode(lcHeaderBO.getFirmId(), sellerCode);
            sql = "  select c_buyer_code from lc_seller_buyer_priority lsbp where lsbp.n_firm_id = "+lcHeaderBO.getFirmId()+" and lsbp.c_seller_code = '"+sellerCode+"' ";
            Query query = this.getQuery(sql);
            Object result = this.getSingleResult(query);
            cCode = helper.getString(result);
        }
        if(cCode != null) {
            String sellerBuyerCode = "'" +sellerCode+cCode+ "'";
            sellerCode = "'" +sellerCode+ "'";
            sql = getSellerByItemCode(searchBO.getSearchTerm(), sellerCode, sellerBuyerCode, lcHeaderBO.getFirmId().toString());
        } else {
            sql = getSellerCodeAndBuyerCodeQuery(lcHeaderBO, searchBO);
        }
        Query query = this.getQuery(sql);
        query.setParameter("itemCode", searchBO.getSearchTerm());
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(),searchBO.getLimit());

        if (resultList == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        JsonArray jsonArray = new JsonArray();
        /*Criteria criteria = Criteria.where("_id").is(searchBO.getSearchTerm());
        org.springframework.data.mongodb.core.query.Query mq = org.springframework.data.mongodb.core.query.Query.query(criteria);
        LcItem lcItem = mongoOperations.findOne(mq, LcItem.class);

        if (lcItem == null) {
            return jsonArray;
            //throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }*/
        for (Object[] objects : resultList) {
           // int i = -1;
            JsonObject jsonObject = new JsonObject();
          //  String sellerCode = helper.getString(objects[++i]);
            jsonObject.addProperty("c_seller_code", helper.getString(objects[0]));
            jsonObject.addProperty("c_seller_name", helper.getString(objects[1]));
            jsonObject.addProperty("c_buyer_code", helper.getString(objects[2]));
            jsonObject.addProperty("c_buyer_name", helper.getString(objects[3]));
            jsonObject.addProperty("c_item_code", searchBO.getSearchTerm());
           // jsonObject.addProperty("c_seller_item_code", helper.getString(objects[++i]));
           // jsonObject.addProperty("c_item_name", lcItem.getItemName());
           // jsonObject.addProperty("c_pack_name", lcItem.getPackName());
//            jsonObject.addProperty("n_mrp", lcItem.getMrp());
            jsonObject.addProperty("n_seller_rate", objects[4] == null ? 0 : Double.parseDouble(objects[4].toString()));
          //  jsonObject.addProperty("c_contains", lcItem.getContains());
            /*if (lcItem.getThumbnailImages() == null || lcItem.getThumbnailImages().size() < 1)
                jsonObject.addProperty("ac_thumbnail_images", "");
            else
                jsonObject.addProperty("ac_thumbnail_images", lcItem.getThumbnailImages().get(0));*/
            /*List<ThumbnailBO> thumbnailBOS = new ArrayList<>();
            ThumbnailBO thumbnailBO = new ThumbnailBO();
            if (!helper.isEmpty(lcItem.getImgUrl())){
                thumbnailBO.setThumbnailImage(lcItem.getImgUrl());
            }
            else {
                thumbnailBO.setThumbnailImage("");
            }
            thumbnailBOS.add(thumbnailBO);*/

            //jsonObject.add("ac_thumbnail_images",helper.toJsonArrayTree(thumbnailBOS,List.class));
            jsonObject.addProperty("n_seller_stock", objects[5] == null ? 0 : Double.parseDouble(objects[5].toString()));
            jsonObject.addProperty("c_seller_item_code", helper.getString(objects[6]));
            jsonObject.addProperty("n_mrp", helper.getBigDecimal(objects[7]));
            jsonObject.addProperty("c_pack_code", helper.getString(objects[8]));
            jsonObject.addProperty("c_schemes",  helper.getString(objects[9]));
            jsonObject.addProperty("n_min_sale_qty",  helper.getString(objects[10]));
        /*    LcCart cart = ifItemExists(lcHeaderBO.getUserId(), lcHeaderBO.getFirmId(), lcHeaderBO.getBranchId(), sellerCode, searchBO.getSearchTerm());
            jsonObject.addProperty("c_cart_status", (cart == null) ? Constants.STATUS_NO : Constants.STATUS_YES); //TODO user query to update
*/
            //TODO get cart qty using query
           /* int qty = 0;
            if (cart != null) {
                for (SellerInfo sellerInfo : cart.getSupplier()) {
                    if (sellerInfo.getSellerCode().equals(sellerCode)) {
                        for (CartItemBo cartItemBo : sellerInfo.getItems()) {
                            if (cartItemBo.getItemCode().equals(searchBO.getSearchTerm())) {
                                qty = cartItemBo.getQty();
                            }
                        }
                    }
                }
                jsonObject.addProperty("c_cart_code", cart.getCartCode());
                jsonObject.addProperty("n_qty", qty);
            } else {
                jsonObject.addProperty("c_cart_code", "");
                jsonObject.addProperty("n_qty", qty);
            }*/
            //jsonObject.addProperty("seller_priority_status", Constants.STATUS_NO);
            jsonObject.addProperty("c_offer_status", Constants.STATUS_NO);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String getCustCode(Long firmId, String sellerCode) throws CommunicationErrorException, InvalidRequestException {
        String cCode = null;
        String mobile_no = null ;
        JsonObject firm = catalogueService.getFirm(firmId);
        log.info(firm.toString());

        if (firm.has("cmobileNo"))
            mobile_no = firm.get("cmobileNo").getAsString();
        if(helper.isEmpty(mobile_no)) {
            if (firm.has("c_mobile_no"))
                mobile_no = firm.get("c_mobile_no").getAsString();
        }

        String sql = "SELECT c_c2code, c_code FROM cust_act_mst cam WHERE cam.c_c2code = :sellerCode AND c_mobile = :mobile ";
        Query query = this.getQuery(sql);
        query.setParameter("sellerCode", sellerCode);
        query.setParameter("mobile", mobile_no);
        List<Object[]> resultList = this.getResultList(query);

        if (resultList != null){
            for (Object[] obj : resultList) {
                cCode = helper.getString(obj[1]);
            }
        }
        return cCode;
    }

    private String getSellerCodeAndBuyerCodeQuery(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws CommunicationErrorException, InvalidRequestException, RecordNotFoundException {
        String c2Code = "";
        String sellerBuyerCode = "";
        Map<String, String> headers = new HashMap<>();
        headers.put("x-csquare-c2-code", helper.toString(lcHeaderBO.getFirmId()));
        headers.put("x-csquare-br-code", helper.toString(lcHeaderBO.getFirmId()));
        headers.put("x-csquare-firm-id",helper.toString(lcHeaderBO.getFirmId()));
        headers.put("x-csquare-terminal-id", helper.toString(lcHeaderBO.getUserId()));
        JsonArray jsonArray1 = callCustomerService(headers, sellerUrl);
        if (jsonArray1.size() > 0) {
            for (int i = 0; i < jsonArray1.size(); i++) {
                JsonObject jsonObject = jsonArray1.get(i).getAsJsonObject();
                c2Code = c2Code + "'" + jsonObject.get("c_seller_code").getAsString() + "'";
                sellerBuyerCode = sellerBuyerCode + "'" + jsonObject.get("c_seller_code").getAsString() + jsonObject.get("c_buyer_code").getAsString() + "'";
                if (i != jsonArray1.size() - 1) {
                    c2Code = c2Code + ",";
                    sellerBuyerCode = sellerBuyerCode + ",";
                }
            }
        }
        if(helper.isEmpty(c2Code)){
            c2Code = null ;
        }
        if(helper.isEmpty(sellerBuyerCode))
        {
            sellerBuyerCode = null;
        }
        return getSellerByItemCode(searchBO.getSearchTerm(), c2Code, sellerBuyerCode, lcHeaderBO.getFirmId().toString());
    }

    private String getSellerByItemCode(String searchTerm, String c2Code, String sellerBuyerCode, String firmId) {
        String sql = "SELECT  " +
                "    s.c_stockiest_code AS sellerCode, " +
                "    CAP_FIRST(stmst.c_name) AS sellerName, " +
                "    ac.c_code buyerCode, " +
                "    CAP_FIRST(ac.c_name) buyerName, " +
                "    st.n_sale_rate AS ptr, " +
                "    coalesce(st.n_bal_qty,0) AS stock, " +
                "    s.c_stockiest_item_code AS sellerItemCode, " +
                "    st.n_rate AS mrp, " +
                "     cpm.c_name AS packName,  " +
                "    if(CASE COALESCE(cmst.c_sch_category_code, " +
                "  COALESCE(cmst.c_code, 'RETAIL')) " +
                " WHEN '' THEN 'RETAIL' " +
                " ELSE cmst.c_sch_category_code " +
                " END <> 'RETAIL' and flgChk.n_in_out_flag = 1,  " +
                "  concat(if(schdet.scheme1 <> '', schdet.scheme1, ''), if(schdet.scheme2 <> '', concat(',', schdet.scheme2), ''), if(schdet.scheme3 <> '', concat(',', schdet.scheme3), '')) " +
                "  , cast(lvsch.c_scheme as char) " +
                " ) as scheme, " +
                "    sitem.n_min_sale_qty as minSaleQty "+
                " FROM " +
                "    u_item_mst i " +
                " LEFT OUTER JOIN " +
                "    u_item_mfac_mst m ON m.c_code = i.c_item_mfac_code " +
                " LEFT JOIN " +
                "    u_stockiest_item s ON i.c_code = s.c_ucode " +
                "    AND s.c_stockiest_code IN (" + c2Code + ") " +
                " JOIN " +
                "    lc_supp_chem_comb cmb ON s.c_stockiest_code = cmb.c_c2code " +
                "    AND cmb.c_supp_chem IN (" + sellerBuyerCode + ") " +
                " LEFT JOIN " +
                "    lc_lo_c2code loc ON loc.c_c2code = s.c_stockiest_code " +
                " LEFT JOIN " +
                "    cust_item_mst sitem ON sitem.c_c2code = s.c_stockiest_code " +
                "        AND sitem.c_code = s.c_stockiest_item_code " +
                " JOIN " +
                "    cust_act_mst ac ON ac.c_c2code = cmb.c_c2code " +
                "        AND ac.c_code = cmb.c_chem_code " +
                "        AND i.c_code = :itemCode " +
                " LEFT JOIN " +
                "    lc_seller_buyer_priority sp ON sp.n_firm_id = '"+firmId+"' " +
                "        AND sp.c_seller_code = cmb.c_c2code " +
                "        AND sp.c_buyer_code = cmb.c_chem_code " +
                "        JOIN " +
                "    lc_c2code_mst stmst ON stmst.c_code = s.c_stockiest_code " +
                "        AND stmst.n_order_flag = 1 " +
                "        LEFT JOIN " +
                "    cust_category_mst cmst ON ac.c_cust_category_code = cmst.c_code " +
                "        AND ac.c_c2code = cmst.c_c2code " +
                "        LEFT JOIN " +
                "    cust_scheme_det sdet ON sdet.c_c2code = sitem.c_c2code " +
                "        AND cmst.c_code = sdet.c_category " +
                "        AND sdet.c_item_code = sitem.c_code " +
                "        left join live_scheme_mst lvsch on lvsch.c_firm_code = sitem.c_c2code and lvsch.c_item_code = sitem.c_code " +
                "        left join lc_lo_c2code flgChk on flgChk.c_c2code = ac.c_c2code " +
                "        left join  " +
                "        (select " +
                "         if(schdet.n_sch_qty_1 > 0, concat(schdet.n_sch_qty_1, '+', if(schdet.n_free_qty_1 > 0, schdet.n_free_qty_1, ''), if(schdet.n_sch_disc_perc_1 > 0, concat(schdet.n_sch_disc_perc_1, '%'), '')) , '') as scheme1, " +
                "   if(schdet.n_sch_qty_2 > 0, concat(schdet.n_sch_qty_2, '+', if(schdet.n_free_qty_2 > 0, schdet.n_free_qty_2, ''), if(schdet.n_sch_disc_perc_2 > 0, concat(schdet.n_sch_disc_perc_2, '%'), '')) , '') as scheme2, " +
                "   if(schdet.n_sch_qty_3 > 0, concat(schdet.n_sch_qty_3, '+', if(schdet.n_free_qty_3 > 0, schdet.n_free_qty_3, ''), if(schdet.n_sch_disc_perc_3 > 0, concat(schdet.n_sch_disc_perc_3, '%'), '')) , '') as scheme3, " +
                "   schdet.c_c2code, " +
                "   schdet.c_item_code, " +
                "   schdet.c_category, " +
                "   schmst.d_sch_close_date, " +
                "   row_number() over(partition by im.c_c2code, im.c_code, schdet.c_category order by schmst.d_ldate desc) as rowid " +
                "   from u_stockiest_item uit    " +
                "   join lc_c2code_mst lccm on uit.c_stockiest_code = lccm.c_code  " +
                "   join cust_item_mst im on (im.c_c2code = lccm.c_code or im.c_c2code = if(trim(lccm.c_parent_act_code) = '', null, lccm.c_parent_act_code)) and im.c_code = uit.c_stockiest_item_code  " +
                "   join cust_scheme_mst schmst on schmst.c_c2code = im.c_c2code and schmst.c_item_code = im.c_code " +
                "   join cust_scheme_det schdet on schmst.c_c2code = schdet.c_c2code and schmst.c_item_code = schdet.c_item_code and schmst.c_batch_no = schdet.c_batch_no  " +
                "   where uit.c_stockiest_code IN (" + c2Code + ") and uit.c_ucode = :itemCode " +
                "   ) schdet on schdet.c_c2code = sitem.c_c2code  " +
                "     and schdet.c_item_code = sitem.c_code  " +
                "     and schdet.c_category = CASE COALESCE(cmst.c_sch_category_code, " +
                "     COALESCE(cmst.c_code, 'RETAIL')) " +
                "     WHEN '' THEN 'RETAIL' " +
                "     ELSE cmst.c_sch_category_code " +
                "     end " +
                "     and schdet.rowid = 1 " +
                "     and (schdet.d_sch_close_date = '0000-00-00'  " +
                "     or schdet.d_sch_close_date = '1900-01-01'  " +
                "     or schdet.d_sch_close_date >= curdate()) " +
                " LEFT OUTER JOIN " +
                "    live_scheme_mst sch ON sch.c_firm_code = s.c_stockiest_code " +
                "        AND sch.c_item_code = s.c_stockiest_item_code " +
                "        LEFT OUTER JOIN " +
                "    cust_branch_item_stock st ON st.c_c2code = s.c_stockiest_code " +
                "        AND st.c_item_code = s.c_stockiest_item_code " +
                "        LEFT OUTER JOIN " +
                "    u_item_pack_mst pk ON pk.c_code = i.c_item_pack_code " +
                " LEFT JOIN cust_pack_mst cpm on (cpm.c_code = sitem.c_pack_code and sitem.c_c2code = cpm.c_c2code) " +
                " GROUP BY s.c_stockiest_code , s.c_stockiest_item_code , ac.c_code " +
                " ORDER BY sp.n_buyer_seller_priority ASC, (st.n_bal_qty) DESC , i.c_name ASC , stmst.c_name ASC ";
            return sql;
    }

    @Override
    public long getSellerOnProductCount(LcHeaderBO lcHeaderBo, SearchBO searchBO, String sellerCode) throws CommunicationErrorException, InvalidRequestException, RecordNotFoundException {
//       JsonObject ser = catalogueService.getFirm(lcHeaderBo.getFirmId());
//        String mobile = null;
//        if (ser.has("cmobileNo")) {
//            mobile = ser.get("cmobileNo").getAsString();
//        } else if (ser.has("c_mobile_no")) {
//            mobile = ser.get("c_mobile_no").getAsString();
//        }
        String cCode = null;
        String sql;
        if (!helper.isEmpty(sellerCode)) {
            //cCode = getCustCode(lcHeaderBo.getFirmId(), sellerCode);
            sql = "  select c_buyer_code from lc_seller_buyer_priority lsbp where lsbp.n_firm_id = "+lcHeaderBo.getFirmId()+" and lsbp.c_seller_code = '"+sellerCode+"' ";
            Query query = this.getQuery(sql);
            Object result = this.getSingleResult(query);
            cCode = helper.getString(result);
        }
        if(cCode != null) {
            String sellerBuyerCode = "'" +sellerCode+cCode+ "'";
            sellerCode = "'" +sellerCode+ "'";
            sql = getSellerByItemCode(searchBO.getSearchTerm(), sellerCode, sellerBuyerCode, lcHeaderBo.getFirmId().toString());
        } else {
            sql = getSellerCodeAndBuyerCodeQuery(lcHeaderBo, searchBO);
        }
        Query query = this.getQuery("SELECT COUNT(*) FROM (" + sql + ") DUMMY");
        query.setParameter("itemCode", searchBO.getSearchTerm());
        Object result = this.getSingleResult(query);
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray getElProductDetails(LcHeaderBO headerBO, SearchBO searchBO) throws RecordNotFoundException {
        Pageable pageable = PageRequest.of(searchBO.getPage(),searchBO.getLimit()
                ,Sort.by("sellingCount").descending());

        //Pageable pageable = PageRequest.of(searchBO.getPage(),searchBO.getLimit());
       /* ElLcItem elLcItem = new ElLcItem();
        elLcItem.setBarCode("ssdfd");
        elLcItem.setItemCode("q43253");
        elLcItem.setItemName("qwerty");
        elRepository.save(elLcItem);
        System.out.println("nnsdkfj");
        Iterable<ElLcItem> lcItems1 = elRepository.findAll();
        for (ElLcItem elLcItem1:lcItems1){
            System.out.println(elLcItem1);
        }

        System.out.println("szfs");*/
        // System.out.println(elRepository.findByName(searchBO.getSearchTerm(), pageable));
        // RegexpQueryBuilder filterQuery = QueryBuilders.regexpQuery("itemName", ".*" + searchBO.getSearchTerm() + ".*");
        /*BoolQueryBuilder fq = QueryBuilders.boolQuery()
                .must(QueryBuilders.regexpQuery("itemName", ".*" + searchBO.getSearchTerm() + ".*"))
                ;
         SearchRequestBuilder srb = transportClient.prepareSearch("el_lc_item")
                .setQuery(fq)
                 .addSort("sellingCount", SortOrder.DESC)
                .setFrom(searchBO.getPage())
                .setSize(searchBO.getLimit());

         srb.get().getHits();
         System.out.println( srb.get().getHits());*/
        List<ElLcItem> lcItems = elRepository.findByName(searchBO.getSearchTerm(), pageable);
        JsonArray jsonArray = new JsonArray();
        for (ElLcItem lcItem : lcItems) {
            JsonObject jsonObject = new JsonObject();
            String itemCode = lcItem.getItemCode();
            jsonObject.addProperty("c_item_code", itemCode);
            jsonObject.addProperty("c_item_ucode", itemCode);
            jsonObject.addProperty("c_item_name", lcItem.getItemName());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public void syncLcItemToElItem(PageBO pageBO) {

        Criteria criteria = Criteria.where("_id").ne(null);

        org.springframework.data.mongodb.core.query.Query query = org.springframework.data.mongodb.core.query.Query.query(criteria);
        query.limit(pageBO.getLimit());
        query.skip((long) pageBO.getPage() * pageBO.getLimit());

        List<LcItem> lcItems = mongoOperations.find(query, LcItem.class);
        for (LcItem lcItem: lcItems){
            ElLcItem elLcItem = new ElLcItem();
            elLcItem.setItemCode(lcItem.getItemCode());
            elLcItem.setItemName(lcItem.getItemName());
            elLcItem.setGstCode(lcItem.getGstCode());
            elLcItem.setMrp(lcItem.getMrp());
            elLcItem.setSellingCount(lcItem.getSellingCount());
            elLcItem.setSellingQty(lcItem.getSellingQty());
            elRepository.save(elLcItem);
        }

    }

    @Override
    public long elProductCount(SearchBO searchBO) throws RecordNotFoundException {
        return elRepository.elCount(searchBO.getSearchTerm()).size();
    }

    public String getMongoContainsQueryParameter(String str) {
        String var10000 = getMongoStartQueryParameter(str);
        return ".*" + var10000;
    }

    public String getMongoStartQueryParameter(String str) {
        String var10000 = str.trim().replaceAll("\\*", " ").replaceAll("-", ".*");
        return "^" + var10000.replaceAll(" ", ".*") + ".*";
    }

    public String getMongoSearchParameter(String str) {
        return str.startsWith(" ") ? this.getMongoContainsQueryParameter(str) : this.getMongoStartQueryParameter(str);
    }


   /* private String getSellerOnProduct(String sellerCode){
        String sellerInfo = "";
        if (!helper.isEmpty(sellerCode)){
            sellerInfo = "and lccm.c_code ='"+sellerCode+"'";
        }


        return "select d.c_seller_code AS sellerCode, lccm.c_name AS sellerName, d.c_buyer_code AS buyerCode,  " +
                "   cam.c_name as buyername," +
                "   cbis.n_sale_rate AS sellerRate,  " +
                "   cbis.n_bal_qty as stock, uit.c_stockiest_item_code as sellerItemCode,  " +
                "   cbis.n_rate as mrp," +
                "   CASE COALESCE(cat.c_sch_category_code," +
                "   COALESCE(cat.c_code, 'RETAIL'))" +
                "   WHEN '' THEN 'RETAIL'" +
                "   ELSE cat.c_sch_category_code" +
                "   END catCode," +
                "   if(CASE COALESCE(cat.c_sch_category_code," +
                "   COALESCE(cat.c_code, 'RETAIL'))" +
                "   WHEN '' THEN 'RETAIL'" +
                "   ELSE cat.c_sch_category_code" +
                "   END <> 'RETAIL' and flgChk.n_in_out_flag = 1, " +
                "   concat(if(schdet.scheme1 <> '', schdet.scheme1, ''), if(schdet.scheme2 <> '', concat(',', schdet.scheme2), ''), if(schdet.scheme3 <> '', concat(',', schdet.scheme3), ''))" +
                "   , cast(lvsch.c_scheme as char)" +
                ") as scheme" +
                "   from lc_mobile_user_mst m    " +
                "   join lc_mobile_user_det d   " +
                "   on m.n_id = d.n_mst_id   " +
                "   join u_stockiest_item uit   " +
                "   on d.c_seller_code = uit.c_stockiest_code  " +
                "   and uit.c_ucode = :itemCode" +
                "   join lc_c2code_mst lccm on uit.c_stockiest_code = lccm.c_code  " + sellerInfo +
                "   left join cust_branch_item_stock cbis   " +
                "   on uit.c_stockiest_code = cbis.c_c2code   " +
                "        and uit.c_stockiest_item_code = cbis.c_item_code  " +
                "        and cbis.c_br_code = lccm.c_cust_branch_code  " +
                "    join cust_act_mst cam on (cam.c_c2code = lccm.c_code OR cam.c_c2code = if(trim(lccm.c_parent_act_code) = '', null, lccm.c_parent_act_code)) and cam.c_code = d.c_buyer_code" +
                "    join cust_item_mst citem on citem.c_c2code = cam.c_c2code and citem.c_code = uit.c_stockiest_item_code" +
                "    left join cust_mfac_mst cmf on citem.c_c2code = cmf.c_c2code and citem.c_mfac_code = cmf.c_code" +
                "    left join live_scheme_mst lvsch on lvsch.c_firm_code = citem.c_c2code and lvsch.c_item_code = citem.c_code" +
                "    left join cust_category_mst cat on cam.c_c2code = cat.c_c2code and cam.c_cust_category_code = cat.c_code" +
                "    left join (" +
                "    select im.c_c2code, im.c_code as c_item_code, schdet.c_category, schmst.d_ldate, " +
                "    schmst.d_sch_close_date," +
                "   row_number() over(partition by im.c_c2code, im.c_code, schdet.c_category order by schmst.d_ldate desc) as rowid," +
                "   if(schdet.n_sch_qty_1 > 0, concat(schdet.n_sch_qty_1, '+', if(schdet.n_free_qty_1 > 0, schdet.n_free_qty_1, ''), if(schdet.n_sch_disc_perc_1 > 0, concat(schdet.n_sch_disc_perc_1, '%'), '')) , '') as scheme1," +
                "   if(schdet.n_sch_qty_2 > 0, concat(schdet.n_sch_qty_2, '+', if(schdet.n_free_qty_2 > 0, schdet.n_free_qty_2, ''), if(schdet.n_sch_disc_perc_2 > 0, concat(schdet.n_sch_disc_perc_2, '%'), '')) , '') as scheme2," +
                "   if(schdet.n_sch_qty_3 > 0, concat(schdet.n_sch_qty_3, '+', if(schdet.n_free_qty_3 > 0, schdet.n_free_qty_3, ''), if(schdet.n_sch_disc_perc_3 > 0, concat(schdet.n_sch_disc_perc_3, '%'), '')) , '') as scheme3" +
                "   from lc_mobile_user_mst m    " +
                "   join lc_mobile_user_det d  " +
                "   on m.n_id = d.n_mst_id " +
                "   join u_stockiest_item uit   " +
                "   on d.c_seller_code = uit.c_stockiest_code  " +
                "   and uit.c_ucode = :itemCode" +
                "   join lc_c2code_mst lccm on d.c_seller_code = lccm.c_code " +
                "   join cust_item_mst im on (im.c_c2code = lccm.c_code or im.c_c2code = if(trim(lccm.c_parent_act_code) = '', null, lccm.c_parent_act_code)) and im.c_code = uit.c_stockiest_item_code " +
                "   join cust_scheme_mst schmst on schmst.c_c2code = im.c_c2code and schmst.c_item_code = im.c_code" +
                "   join cust_scheme_det schdet on schmst.c_c2code = schdet.c_c2code and schmst.c_item_code = schdet.c_item_code and schmst.c_batch_no = schdet.c_batch_no " +
                "   where m.n_mobile_no = :mobile" +
                "   ) schdet " +
                "   on schdet.c_c2code = citem.c_c2code " +
                "   and schdet.c_item_code = citem.c_code " +
                "   and schdet.c_category = CASE COALESCE(cat.c_sch_category_code," +
                "   COALESCE(cat.c_code, 'RETAIL'))" +
                "   WHEN '' THEN 'RETAIL'" +
                "   ELSE cat.c_sch_category_code" +
                "   end" +
                "   and schdet.rowid = 1" +
                "   and (schdet.d_sch_close_date = '0000-00-00' " +
                "   or schdet.d_sch_close_date = '1900-01-01' " +
                "   or schdet.d_sch_close_date >= curdate() " +
                "   )" +
                "   left join lc_lo_c2code flgChk on flgChk.c_c2code = cam.c_c2code" +
                "   left join lc_user_seller_priority lusp on d.n_mst_id = lusp.n_mst_id and d.c_seller_code = lusp.c_seller_code and d.c_buyer_code = lusp.c_buyer_code" +
                "   LEFT JOIN" +
                "        lc_item_filter lif ON lif.c_c2code = cbis.c_c2code" +
                "            AND lif.n_cancel_flag = 0" +
                "            AND CASE lif.n_item_type" +
                "            WHEN 2 THEN cmf.c_mfac_group_code = lif.c_item_type_code" +
                "            WHEN 3 THEN citem.c_mfac_code = lif.c_item_type_code" +
                "            WHEN 4 THEN citem.c_cat_code = lif.c_item_type_code" +
                "            WHEN 5 THEN citem.c_cont_code = lif.c_item_type_code" +
                "            WHEN" +
                "                6" +
                "            THEN" +
                "                LEFT(citem.c_name," +
                "                    LENGTH(lif.c_item_type_code)) = lif.c_item_type_code" +
                "            ELSE citem.c_code = lif.c_item_type_code" +
                "        END" +
                "            AND CASE lif.n_cust_type" +
                "            WHEN 2 THEN cam.c_sman_code = lif.c_cust_type_code" +
                "            WHEN 3 THEN cam.c_route_code = lif.c_cust_type_code" +
                "            WHEN 4 THEN cam.c_cust_category_code = lif.c_cust_type_code" +
                "            WHEN 5 THEN cam.c_dman_code = lif.c_cust_type_code" +
                "            WHEN 6 THEN cam.c_area_code = lif.c_cust_type_code" +
                "            ELSE cam.c_code = lif.c_cust_type_code" +
                "        END " +
                "   where n_buyer_flag = 1" +
                "   and m.n_mobile_no = :mobile" +
                "   AND lif.n_item_type IS NULL" +
                "   AND lif.n_cust_type IS NULL" +
                "   order by if(cbis.n_bal_qty > 0, 0, 1), " +
                "   coalesce(lusp.n_buyer_seller_priority, 9999)";
    }*/

    private LcCart ifItemExists(long userId, long firmId, long branchId, String sellerCode, String itemCode) {
        Criteria criteria = Criteria.where("n_user_id").is(userId).and("n_firm_id").is(firmId).and("n_branch_id").is(branchId)
                .and("j_supplier.j_items.c_combine_code").is(sellerCode + "|" + itemCode);
        org.springframework.data.mongodb.core.query.Query query = org.springframework.data.mongodb.core.query.Query.query(criteria);
        return mongoOperations.findOne(query, LcCart.class);
    }

    public JsonArray callCustomerService(Map<String, String> headers, String url) throws CommunicationErrorException, InvalidRequestException, RecordNotFoundException {

        String result = callWebClientGetSyncApi(url, headers);
        log.debug("C2 Service Response : {}" + result);

        JsonObject responseObject;
        JsonArray jsonArray = new JsonArray();
        if (result == null || result.isEmpty()) {
            log.error("Result is null API {} -- Response {}", url, result);
            throw new CommunicationErrorException("", "Error connecting to Seller Detail!");
        } else {
            responseObject = helper.getJsonObject(result);
            if (responseObject.get("appStatusCode").getAsInt() == 0) {
                jsonArray = responseObject.get("payloadJson").getAsJsonObject()
                        .get("data").getAsJsonArray();
            } else {
                log.debug("Response {}", result);
            }
        }
       /* if (jsonArray.size() < 1) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }*/
        return jsonArray;
    }

    @Override
    public JsonArray getTSPrd(Long userId, SearchBO searchBO, String c2Code) throws RecordNotFoundException {
        JsonArray array = new JsonArray();
        String sql = " select cim.c_code, um.c_name, cbis.n_sale_rate, coalesce(cbis.n_bal_qty, 0) as n_bal_qty, lccm.c_name as customer_name , usi.c_ucode from cust_item_mst cim " +
                " JOIN u_stockiest_item usi on usi.c_stockiest_code = cim.c_c2code and usi.c_stockiest_item_code = cim.c_code  " +
                " JOIN u_item_mst um on um.c_code = usi.c_ucode " +
                " LEFT JOIN cust_branch_item_stock cbis  FORCE INDEX(PRIMARY) on cbis.c_c2code = usi.c_stockiest_code and cbis.c_item_code = usi.c_stockiest_item_code "+
                " INNER JOIN lc_c2code_mst lccm on lccm.c_code = cbis.c_c2code and lccm.c_cust_branch_code = cbis.c_br_code " +
                " WHERE um.c_name like :searchTerm AND cim.c_c2code = :c2Code  order by um.c_name asc ";
        Query query = this.getQuery(sql);
        query.setParameter("searchTerm", helper.getLikeQueryString(searchBO.getSearchTerm()));
        query.setParameter("c2Code", c2Code);
        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());

        List<JsonObject> list = new ArrayList<>();
        Map<String, String> custItemMap = new HashMap<>();
        Map<String, BigDecimal> sellerRateMap = new HashMap<>();
        Map<String, String> custItemNameMap = new HashMap<>();
        Map<String, BigDecimal> custItemQtyMap = new HashMap<>();
        Map<String, String> StoreNameMap = new HashMap<>();
        List<String> itemCodes = new ArrayList<>();
        if (!resultList.isEmpty()) {
            for (Object[] object : resultList) {
                itemCodes.add(helper.getString(object[5]));
                custItemMap.put(helper.getString(object[0]), helper.getString(object[5]));
               // custItemNameMap.put(helper.getString(object[0]), helper.getString(object[1]));
                sellerRateMap.put(helper.getString(object[0]), helper.getBigDecimal(object[2]));
                custItemQtyMap.put(helper.getString(object[0]), helper.getBigDecimal(object[3]));
                StoreNameMap.put(helper.getString(object[0]), helper.getString(object[4]));
            }
        } else {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
                Criteria criteria = Criteria.where("_id").in(itemCodes).and("c_gst_code").ne(null); // add the in clause
                org.springframework.data.mongodb.core.query.Query query1 = new org.springframework.data.mongodb.core.query.Query().addCriteria(criteria);
                List<LcItem> lcItems = mongoOperations.find(query1, LcItem.class);
                List<String> custItemCodeFetch = new ArrayList<>();
                for (String itemCode : itemCodes) {
                    JsonObject jsonObject = new JsonObject();
                    LcItem lcItem = lcItems.stream().filter(item -> itemCode.equals(item.getItemCode())).findAny().orElse(null);
                    if (lcItem != null) {
                        int uCodeCount = 0;
                        for (Map.Entry<String, String> custItemCodeKey : custItemMap.entrySet()) {
                            //if (uCodeCount == 0) {
                            if (custItemCodeKey.getValue().equals(itemCode)) {
                                jsonObject.addProperty("c_item_code", lcItem.getItemCode());
                                jsonObject.addProperty("c_item_name", lcItem.getItemName());
                                jsonObject.addProperty("n_price", lcItem.getMrp());
                                jsonObject.addProperty("c_pack_type_name", lcItem.getPackTypeName());
                                jsonObject.addProperty("c_cust_item_code", custItemCodeKey.getKey());
                                // uCodeCount++;
                                //  if (!custItemCodeFetch.contains(custItemCodeKey.getKey())) {
                                // custItemCodeFetch.add(custItemCodeKey.getKey());
                                for (Map.Entry<String, BigDecimal> SellerRateKey : sellerRateMap.entrySet()) {
                                    if (SellerRateKey.getKey().equals(custItemCodeKey.getKey())) {
                                        jsonObject.addProperty("c_cust_rate", SellerRateKey.getValue());
                                    }
                                }
                             /*   for (Map.Entry<String, String> custItemNameKey : custItemNameMap.entrySet()) {
                                    if (custItemNameKey.getKey().equals(custItemCodeKey.getKey())) {
                                        jsonObject.addProperty("c_item_name", custItemNameKey.getValue());
                                    }
                                }*/
                                for (Map.Entry<String, String> storeNameKey : StoreNameMap.entrySet()) {
                                    if (storeNameKey.getKey().equals(custItemCodeKey.getKey())) {
                                        jsonObject.addProperty("c_cust_name", storeNameKey.getValue());
                                    }
                                }
                                for (Map.Entry<String, BigDecimal> custItemNQtyKey : custItemQtyMap.entrySet()) {
                                    if (custItemNQtyKey.getKey().equals(custItemCodeKey.getKey())) {
                                        if(custItemNQtyKey.getValue().intValue()==0){
                                            jsonObject.addProperty("c_stock_availabilty", "No");
                                        }else{
                                            jsonObject.addProperty("c_stock_availabilty", "Yes");
                                        }
                                    }
                                }
                            }
                        }
                        array.add(jsonObject);
                    }
                }
        return array;
    }

    @Override
    public long mostViewedPrdsCount(MostViewedPrdsBO viewedPrdBO, LcHeaderBO header) throws InputPayloadException {
        long count = 0;
        Criteria criteria = null;
        org.springframework.data.mongodb.core.query.Query query = null;

        if (Objects.equals(viewedPrdBO.getCBrCode(), Constants.STRING_VALUE_ONE)) {
            if (!helper.isEmpty(viewedPrdBO.getCSearchTerm()))
                criteria = Criteria.where("c_c2_code").is(viewedPrdBO.getCC2Code()).and("c_item_name").regex(viewedPrdBO.getCSearchTerm(), "i");
            else
                criteria = Criteria.where("c_c2_code").is(viewedPrdBO.getCC2Code());

        } else {
            if (!helper.isEmpty(viewedPrdBO.getCSearchTerm()))
                criteria = Criteria.where("c_c2_code").is(viewedPrdBO.getCC2Code()).and("c_br_code").is(viewedPrdBO.getCBrCode())
                        .and("c_item_name").regex(viewedPrdBO.getCSearchTerm(), "i");
            else
                criteria = Criteria.where("c_c2_code").is(viewedPrdBO.getCC2Code()).and("c_br_code").is(viewedPrdBO.getCBrCode());

        }
        query = org.springframework.data.mongodb.core.query.Query.query(criteria);
      //  query.skip((long) viewedPrdBO.getPage() * viewedPrdBO.getLimit());
       // query.limit(viewedPrdBO.getLimit());
        count = mongoTemplate.count(query, MostViewedProduct.class);

        return count;
    }

    @Override
    public String updateSalesCount(JsonObject inputJson) throws RecordNotFoundException {
        log.debug("payload-->"+inputJson.toString()+"");
        JsonArray itemList = inputJson.get("j_item_list").getAsJsonArray();
        List<String> itemCodes = new ArrayList<>();
        for (JsonElement itemCode : itemList)
            itemCodes.add(itemCode.getAsString());
        Criteria criteria = Criteria.where("c_item_code").in(itemCodes).and("c_br_code").is(inputJson.get("c_br_code").getAsString())
                .and("c_c2_code").is(inputJson.get("c_c2_code").getAsString());
        org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query().addCriteria(criteria);
       System.out.println(query);
        List<MostViewedProduct> viewedProducts = mongoOperations.find(query, MostViewedProduct.class);
        if (viewedProducts.size()== 0)
            throw new RecordNotFoundException("Record not Found");
        else {
            for (MostViewedProduct viewedProduct : viewedProducts) {
                viewedProduct.setNSalesCount(viewedProduct.getNSalesCount() + 1);
                mostViewedProductRepository.save(viewedProduct);
            }
        }
        return Constants.STATUS_YES;
    }

    @Override
    public JsonArray mostViewedPrds(MostViewedPrdsBO viewedPrdBO, LcHeaderBO header) throws InputPayloadException, RecordNotFoundException {
        JsonArray sellerItemArray = new JsonArray();
        JsonArray stockResult = new JsonArray();
        BigDecimal salesRatio;
        BigDecimal multiply = new BigDecimal(100);
        Criteria criteria = null;
        org.springframework.data.mongodb.core.query.Query query = null;

        if (Objects.equals(viewedPrdBO.getCBrCode(), Constants.STRING_VALUE_ONE)) {
            if (!helper.isEmpty(viewedPrdBO.getCSearchTerm()))
                criteria = Criteria.where("c_c2_code").is(viewedPrdBO.getCC2Code()).and("c_item_name").regex(viewedPrdBO.getCSearchTerm(), "i");
            else
                criteria = Criteria.where("c_c2_code").is(viewedPrdBO.getCC2Code());

        } else {
            if (!helper.isEmpty(viewedPrdBO.getCSearchTerm()))
                criteria = Criteria.where("c_c2_code").is(viewedPrdBO.getCC2Code()).and("c_br_code").is(viewedPrdBO.getCBrCode())
                        .and("c_item_name").regex(viewedPrdBO.getCSearchTerm(), "i");
            else
                criteria = Criteria.where("c_c2_code").is(viewedPrdBO.getCC2Code()).and("c_br_code").is(viewedPrdBO.getCBrCode());

        }
        query = org.springframework.data.mongodb.core.query.Query.query(criteria);
        query.skip((long) viewedPrdBO.getPage() * viewedPrdBO.getLimit());
        query.limit(viewedPrdBO.getLimit());
        System.out.println(query);
        List<MostViewedProduct> allBranchViewedPrds = mongoTemplate.find(query, MostViewedProduct.class);
        for (MostViewedProduct prd : allBranchViewedPrds) {
            sellerItemArray.add(prd.getCSellerItemCode());
        }
        stockResult = checkProductsStock(sellerItemArray, viewedPrdBO.getCC2Code(), viewedPrdBO.getCBrCode());
        for (MostViewedProduct prd : allBranchViewedPrds) {
            for (int i = 0; i < stockResult.size(); i++) {
                JsonObject payload = stockResult.get(i).getAsJsonObject();
                if (prd.getCItemCode().equals(payload.get("c_cust_item_code").getAsString()))
                {
                    prd.setNStockAvailability(payload.get("c_stock_availability").getAsInt());
                    mostViewedProductRepository.save(prd);
                }
            }
            BigDecimal dividend = new BigDecimal(prd.getNSalesCount());
            BigDecimal divisor = new BigDecimal(prd.getNViewedCount());
            salesRatio = dividend.divide(divisor, 2, RoundingMode.HALF_UP);
//            System.out.println(salesRatio.multiply(multiply));
            prd.setCSalesRatio(salesRatio.multiply(multiply));
            mostViewedProductRepository.save(prd);
        }
        switch (viewedPrdBO.getCSortByCol()) {
            case com.c2.lc.ms.master.utils.Constants.SORT_BY_PRODUCT_NAME:
                if (viewedPrdBO.getCSortType().equals(com.c2.lc.ms.master.utils.Constants.SORT_BY_ASC))
                    query.with(Sort.by(Sort.Direction.ASC, "c_item_name"));
                else
                    query.with(Sort.by(Sort.Direction.DESC, "c_item_name"));
                break;
            case com.c2.lc.ms.master.utils.Constants.SORT_BY_AVAILABILITY:
                if (viewedPrdBO.getCSortType().equals(com.c2.lc.ms.master.utils.Constants.SORT_BY_ASC))
                    query.with(Sort.by(Sort.Direction.ASC, "n_stock_availability"));
                else
                    query.with(Sort.by(Sort.Direction.DESC, "n_stock_availability"));
                break;
            case com.c2.lc.ms.master.utils.Constants.SORT_BY_VIEW_COUNT:
                if (viewedPrdBO.getCSortType().equals(com.c2.lc.ms.master.utils.Constants.SORT_BY_ASC))
                    query.with(Sort.by(Sort.Direction.ASC, "n_viewed_count"));
                else
                    query.with(Sort.by(Sort.Direction.DESC, "n_viewed_count"));
                break;
            case com.c2.lc.ms.master.utils.Constants.SORT_BY_NO_OF_SALES:
                if (viewedPrdBO.getCSortType().equals(com.c2.lc.ms.master.utils.Constants.SORT_BY_ASC))
                    query.with(Sort.by(Sort.Direction.ASC, "n_sales_count"));
                else
                    query.with(Sort.by(Sort.Direction.DESC, "n_sales_count"));
                break;
            case com.c2.lc.ms.master.utils.Constants.SORT_BY_SALES_RATIO:
                if (viewedPrdBO.getCSortType().equals(com.c2.lc.ms.master.utils.Constants.SORT_BY_ASC))
                    query.with(Sort.by(Sort.Direction.ASC, "c_sales_ratio"));
                else
                    query.with(Sort.by(Sort.Direction.DESC, "c_sales_ratio"));
                break;
            default:
                throw new InputPayloadException("c_sort_by_column","Please enter valid input");
        }
        List<MostViewedProduct> allBranchViewedPrd = mongoTemplate.find(query, MostViewedProduct.class);
        JsonArray jsonArray = new JsonArray();
        if (allBranchViewedPrd.size() == 0) {
            throw new RecordNotFoundException("Record not Found");
        } else {
            for (MostViewedProduct objects : allBranchViewedPrd) {
                JsonObject jsonObject = new JsonObject();
                JsonObject jobj = new JsonObject();
                JsonArray jsonArray1 = new JsonArray();
                jsonObject.addProperty("c_item_code", objects.getCItemCode());
                jsonObject.addProperty("c_item_name", objects.getCItemName());
                jobj.addProperty("c_thumbnail_image", objects.getCItemImg().get(0).getThumbnailImage());
                jsonArray1.add(jobj);
                jsonObject.add("ac_thumbnail_images", jsonArray1);
                jsonObject.addProperty("n_viewed_count", objects.getNViewedCount());
                jsonObject.addProperty("n_stock_availability", objects.getNStockAvailability());
                jsonObject.addProperty("c_pack_name", objects.getCPacking());
                jsonObject.addProperty("c_sales_count",objects.getNSalesCount());
                jsonObject.addProperty("c_sales_ratio",objects.getCSalesRatio());
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    @Override
    public JsonArray checkProductsStock(JsonArray j_item_codes, String c2Code, String brCode) throws RecordNotFoundException {
        JsonArray js1 = new JsonArray();
        String sql = null;
        for (int i=0; i<j_item_codes.size(); i++) {
            JsonObject jsonObject = new JsonObject();
            if(brCode.equals(Constants.STRING_VALUE_ONE)) {
                sql = "select coalesce(cbis.n_bal_qty, 0) as n_bal_qty, cbis.c_item_code From cust_branch_item_stock cbis   Where cbis.c_c2code = '" + c2Code + "' " +
                        " and cbis.c_item_code = " + j_item_codes.get(i);
            } else {
                sql = "select coalesce(cbis.n_bal_qty, 0) as n_bal_qty, cbis.c_item_code From cust_branch_item_stock cbis   Where cbis.c_c2code = '" + c2Code + "' " +
                        " AND cbis.c_br_code = '" + brCode + "' and cbis.c_item_code = " + j_item_codes.get(i) ;
            }
            Query query = this.getQuery(sql);
            log.debug(query.toString());
           List<Object[]> obj = this.getResultList(query);
           if(obj.isEmpty()){
               throw new RecordNotFoundException("Record not found for "+j_item_codes.get(i));
           }
            //BigDecimal qty = (BigDecimal) obj;
            for (Object[] objects:obj) {
                jsonObject.addProperty("c_cust_item_code",j_item_codes.get(i).getAsString());
                if(helper.getBigDecimal(objects[0]).intValue()==0){
                    jsonObject.addProperty("c_stock_availability",0);
                }else{
                    jsonObject.addProperty("c_stock_availability",helper.getBigDecimal(objects[0]).intValue());
                }
                js1.add(jsonObject);
            }

        }

        return js1;
    }
}
