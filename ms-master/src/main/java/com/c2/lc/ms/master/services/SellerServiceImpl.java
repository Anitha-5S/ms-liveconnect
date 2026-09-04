package com.c2.lc.ms.master.services;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.ImageUpdateBo;
import com.c2.lc.ms.master.bos.SellerDetailBO;
import com.c2.lc.ms.master.bos.SellerImageBo;
import com.c2.lc.ms.master.entities.mongo.LcSupplier;
import com.c2.lc.ms.master.entities.mysql.*;
import com.c2.lc.ms.master.models.ItemSellersList;
import com.c2.lc.ms.master.models.MasterModel;
import com.c2.lc.ms.master.repos.mongo.LcSupplierRepository;
import com.c2.lc.ms.master.repos.mysql.*;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.CatalogueService;
import com.c2.lc.ms.master.services.interfaces.SchemeService;
import com.c2.lc.ms.master.services.interfaces.SellerService;
import com.c2.lc.ms.master.services.interfaces.ShortBookWatchListService;
import com.c2.lc.ms.master.utils.BlobFolder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SellerServiceImpl extends MasterBaseServiceImpl implements SellerService {

    @Autowired
    private UStockiestItemRepository uStockiestItemRepository;
    @Autowired
    private CustBranchItemRepository custBranchItemRepository;
    @Autowired
    private LcC2CodeMstRepository lcC2CodeMstRepository;
    @Autowired
    private LcImagesMstRepository lcImagesMstRepository;
    @Autowired
    private LcOffersMstRepository lcOffersMstRepository;

    @Autowired
    private SchemeService schemeService;
    @Autowired
    private CatalogueService catalogueService;

    @Autowired
    private LcSupplierRepository supplierRepository;

    @Autowired
    private ShortBookWatchListService shortBookWatchListService;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private SearchServiceImpl searchService;

    @Autowired private LcUserSellerPriorityRepository priorityRepository;

    @Value("${ms.cust.seller.service.api.url}")
    private String sellerUrl;

    @Override
    public List<ItemSellersList> getSellerDetailsByItem(String buyerCode, String itemUCode) {
        //Pageable page = PageRequest.of(0,10);
        //List<CustBranchItemStockEntity> itemLIst = custBranchItemRepository.findByUcode(itemCode,page);
        List<ItemSellersList> itemLIst = getSellerItems(buyerCode, itemUCode);
        //custBranchItemRepository.findByUcode(itemUCode);
        log.info("Scheme Debug");
        itemLIst.stream().map(item -> {

            log.info("SellerCode : {}, Seller Item Code : {}", item.getC_seller_code(), item.getC_seller_item_code());
            JsonObject schemeJson = schemeService.getSellerScheme(item);
            item.setC_scheme(schemeJson.get("c_scheme").getAsString());
            item.setD_scheme_max_value(schemeJson.get("d_scheme_max_value").getAsDouble());
            //change rates if available here for seller
            return item;
        }).collect(Collectors.toList());

        itemLIst.sort(Comparator.comparing(ItemSellersList::getD_scheme_max_value).reversed());
        //itemLIst.sort((Comparator.comparing(SellerItemList::getC_scheme)));
        log.info("seller data -> {}", itemLIst.toString());
        return itemLIst;
    }

    private List<ItemSellersList> getSellerItems(String buyerCode, String itemUCode) {
        List<ItemSellersList> itemSellersLists = new ArrayList<>();
        String sql = " SELECT " +
                " lccm.c_code,lccm.c_name,usi.c_stockiest_item_code,cbis.n_rate,cbis.n_sale_rate,'NA',sum(COALESCE(cbis.n_bal_qty,0)), " +
                " 0.00, " +
                " COALESCE(llc.n_in_out_flag,0) as n_in_out_flag, " +
                " COALESCE(nullif(ccm.c_sch_category_code,''),nullif(ccm.c_code,''),'RETAIL') as c_cat_code " +
                " FROM cust_branch_item_stock cbis " +
                " JOIN u_stockiest_item usi " +
                " ON usi.c_stockiest_item_code = cbis.c_item_code AND " +
                " usi.c_stockiest_code = cbis.c_c2code " +
                " JOIN lc_c2code_mst lccm " +
                " ON lccm.c_code = usi.c_stockiest_code " +
                " LEFT OUTER JOIN lc_lo_c2code llc ON llc.c_c2code = lccm.c_code " +
                " LEFT OUTER JOIN cust_act_mst cam ON cam.c_c2code = lccm.c_code and cam.c_code = :buyerCode " +
                " LEFT OUTER JOIN cust_category_mst ccm ON ccm.c_c2code = cam.c_c2code and ccm.c_code = cam.c_cust_category_code " +
                " WHERE usi.c_ucode = :c_ucode AND " +
                " cbis.n_bal_qty > 0 AND cbis.n_sale_rate > 0 AND lccm.n_order_flag = 1 " +
                " GROUP BY lccm.c_code,lccm.c_name,usi.c_stockiest_item_code,cbis.n_rate,cbis.n_sale_rate " +
                " ORDER BY cbis.n_sale_rate desc";

        Query query = this.getQuery(sql);
        query.setParameter("c_ucode", itemUCode);
        query.setParameter("buyerCode", buyerCode);
        List<Object[]> resultList = this.getResultList(query);

        for (Object[] obj : resultList) {
            ItemSellersList itemSellersList = new ItemSellersList();
            int i = -1;
            itemSellersList.setC_seller_code(helper.getString(obj[++i]));
            itemSellersList.setC_seller_name(helper.getString(obj[++i]));
            itemSellersList.setC_seller_item_code(helper.getString(obj[++i]));
            itemSellersList.setN_mrp(helper.getBigDecimal(obj[++i]).doubleValue());
            itemSellersList.setN_rate(helper.getBigDecimal(obj[++i]).doubleValue());
            itemSellersList.setC_scheme(helper.getString(obj[++i]));
            itemSellersList.setN_stock_qty(helper.getBigDecimal(obj[++i]).longValue());
            itemSellersList.setD_scheme_max_value(helper.getBigDecimal(obj[++i]).doubleValue());
            itemSellersList.setN_in_out_flag(helper.getInt(obj[++i]));
            itemSellersList.setC_cat_code(helper.getString(obj[++i]));
            itemSellersLists.add(itemSellersList);
        }

        return itemSellersLists;
    }

    private LcC2CodeMstEntity getSellerDetails(String sellerCode) {
        return lcC2CodeMstRepository.findBySellerCode(sellerCode);
    }

    @Override
    public JsonArray getStockiestDetails(String sellerC2Code, String code) {
        List<UStockiestItemEntity> stockiestItemList = uStockiestItemRepository.findByC2codeAndItemCode(sellerC2Code, code);
        JsonArray response = new JsonArray();
        for (UStockiestItemEntity item : stockiestItemList) {
            response.add(item.getCUcode());
            response.add(item.getPK().getCStockiestItemCode());
        }
        return response;
    }

    @Override
    public JsonArray getSellerPreferred(PageBO pageBO, LcHeaderBO lcHeaderBO) throws CommunicationErrorException, InvalidRequestException, RecordNotFoundException {
        //TODO image implementation implementation
        int pageNumber = pageBO.getPage();
        int rowLimit = pageBO.getLimit();
        Pageable page = PageRequest.of(pageNumber, rowLimit);


        List<Object[]> sList = new ArrayList<>();
        String pin = null;
        JsonObject firm = null;
        String mobile_no = null ;
       /* List<String> seller_code = new ArrayList<String>();
        List<String> buyer_code = new ArrayList<String>();
        JsonArray sellerBuyer = getSellerCodeAndBuyerCodeQuery(lcHeaderBO);
        for(int i=0; i<sellerBuyer.size() ;i++)
        {
            JsonObject jsonObject = sellerBuyer.get(i).getAsJsonObject();
            seller_code.add(jsonObject.get("c_seller_code").getAsString());
            buyer_code.add(jsonObject.get("c_buyer_code").getAsString());
        }*/

            sList = lcImagesMstRepository.findPreferredSellerCodeByInvoice(lcHeaderBO.getFirmId(), page);

            if (CollectionUtils.isEmpty(sList)){
                firm = catalogueService.getFirm(lcHeaderBO.getFirmId());
                log.info(firm.toString());

                if (firm.has("cmobileNo"))
                    mobile_no = firm.get("cmobileNo").getAsString();
                if(helper.isEmpty(mobile_no)) {
                    if (firm.has("c_mobile_no"))
                        mobile_no = firm.get("c_mobile_no").getAsString();
                }
            if (firm.has("cpin"))
                pin = firm.get("cpin").getAsString();
            if(helper.isEmpty(pin)) {
                if (firm.has("c_pincode"))
                    pin = firm.get("c_pincode").getAsString();
            }
        }

        if (CollectionUtils.isEmpty(sList) && !helper.isEmpty(pin)) {
            sList = lcImagesMstRepository.findPreferredSellerCodeByPincode(pin, page);
        }

        if (CollectionUtils.isEmpty(sList) && firm.has("ccityName")) {
            String city = firm.get("ccityName").getAsString().toUpperCase();
            sList = lcImagesMstRepository.findPreferredSellerCodeByCity(city, page);
        }
        JsonArray respArr = new JsonArray();
        if (CollectionUtils.isEmpty(sList)) {
            return respArr;
            //throw new RecordNotFoundException("Oops Sellers are not available in your location");
        }

        sList.forEach(s -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_seller_code", helper.toString(s[0]));
            jsonObject.addProperty("c_seller_name", helper.toString(s[1]));
            //String sellerImage = helper.toString(s[2]);
           /* if (sellerImage == null) {
                sellerImage = "http://ec2.images-amazon.com/images/I/51ZWY8CoxqL._AA300_.jpg";
            }*/
            JsonArray jsonArray = new JsonArray();
            JsonObject jObj = new JsonObject();
           // if (s.length>2)
            if(!helper.isEmpty(helper.toString(s[2])))
            {
                jObj.addProperty("c_seller_image",helper.toString(s[2]));
                jsonArray.add(jObj);
            }
            jsonObject.add("ac_seller_images", jsonArray);
            respArr.add(jsonObject);
        });

        return respArr;
    }

    public JsonArray getSellerCodeAndBuyerCodeQuery(LcHeaderBO lcHeaderBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
       /* Map<String, String> headers = new HashMap<>();
        headers.put("x-csquare-c2-code", helper.toString(lcHeaderBO.getFirmId()));
        headers.put("x-csquare-br-code", helper.toString(lcHeaderBO.getFirmId()));
        headers.put("x-csquare-firm-id",helper.toString(lcHeaderBO.getFirmId()));
        headers.put("x-csquare-terminal-id", helper.toString(lcHeaderBO.getUserId()));
        return searchService.callCustomerService(headers, sellerUrl);*/
        JsonArray jsonArray = new JsonArray();
        List<LcUserSellerPriorityEntity>  sellerBuyer = priorityRepository.getByFirmId(lcHeaderBO.getFirmId());
        if (sellerBuyer.size() >0) {
            for (LcUserSellerPriorityEntity entity : sellerBuyer) {
                JsonObject object = new JsonObject();
                object.addProperty("c_seller_code",entity.getCSellerCode());
                object.addProperty("c_buyer_code",entity.getCBuyerCode());
                jsonArray.add(object);
            }
        }
        return jsonArray;
    }

    private String getMappedSellerAndBuyerOnMobileNumber() {
        return "select d.c_seller_code, d.c_buyer_code " +
                "from lc_mobile_user_mst m  " +
                "join lc_mobile_user_det d on m.n_id = d.n_mst_id  " +
                "join lc_c2code_mst lcm ON d.c_seller_code = lcm.c_code " +
                "join cust_act_mst act ON act.c_code = d.c_buyer_code " +
                "and (act.c_c2code = lcm.c_code OR act.c_c2code = lcm.c_parent_act_code) " +
                "where m.n_mobile_no = :mobile_no and n_buyer_flag = 1 and d.n_type = 2 and act.n_lock = 0 " +
                "and lcm.n_order_flag = 1 and lcm.n_non_visible_flag=0 " +
                " GROUP BY lcm.c_code ";
    }

    @Override
    public Integer getSellerCount(Long firmId) throws CommunicationErrorException, InvalidRequestException {

        List<Object[]> sList = new ArrayList<>();
        //sList =  lcImagesMstRepository.findPreferredSellerCodeByInvoice(helper.getString(firmId));


        String pin = null;
        JsonObject firm = null;
        String mobile_no = null ;
        firm = catalogueService.getFirm(firmId);
        if (firm.has("cmobileNo"))
            mobile_no = firm.get("cmobileNo").getAsString();
        if(helper.isEmpty(mobile_no)) {
            if (firm.has("c_mobile_no"))
                mobile_no = firm.get("c_mobile_no").getAsString();
        }
        String sql = getMappedSellerAndBuyerOnMobileNumber();
        Query query = this.getQuery(sql);
        query.setParameter("mobile_no",mobile_no);
        List<Object[]> mappedSellers = this.getResultList(query);
        //query.setParameter("mobile_no", mobile_no);
        List<String> seller_code = new ArrayList<String>();
        List<String> buyer_code = new ArrayList<String>();
        for (Object[] objects: mappedSellers) {
            seller_code.add(helper.getString(objects[0]));
            buyer_code.add(helper.getString(objects[1]));
        }
        if(!seller_code.isEmpty()&&!buyer_code.isEmpty()) {
            sList = lcImagesMstRepository.findPreferredSellerCodeByInvoice(buyer_code, seller_code);
        }

        if (CollectionUtils.isEmpty(sList)){
          //  firm.addProperty("c_pincode",firm.get("cpin").getAsString());
            if (firm.has("cpin"))
                pin = firm.get("cpin").getAsString();
            if(helper.isEmpty(pin)) {
                if (firm.has("c_pincode"))
                    pin = firm.get("c_pincode").getAsString();
            }
        }

        if (CollectionUtils.isEmpty(sList) && !helper.isEmpty(pin)) {
            sList = lcImagesMstRepository.findPreferredSellerCodeByPincode(pin);
        }

        if (CollectionUtils.isEmpty(sList) && firm.has("ccityName")) {
            String city = firm.get("ccityName").getAsString().toUpperCase();
            sList = lcImagesMstRepository.findPreferredSellerCodeByCity(city);
        }

        return sList.isEmpty() ? 0 : sList.size();
    }
    private Integer getCountByInvoice(String code){
        String sql = " select count(usfi.c_ucode) as count from u_statewise_fastmoving_items usfi join u_item_mst uim on uim.c_code = usfi.c_ucode where usfi.c_state_code = :code";
        Query query = this.getQuery(sql);
        query.setParameter("code", code);
        BigInteger count = (BigInteger) this.getSingleResult(query);
        return helper.getInt(count.intValue());
    }

    @Override
    public JsonArray getLimitedOfferList(PageBO pageBO) {
        int limit = pageBO.getLimit();
        int page = pageBO.getPage();
        Pageable pageable = PageRequest.of(page, limit);
        JsonArray respArr = new JsonArray();

        lcOffersMstRepository.findAvailableOffers(LocalDateTime.now(), pageable).forEach(offer -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_offer_image", helper.toString(offer.getImageUrl()));
            jsonObject.addProperty("c_offer_code", helper.toString(offer.getOfferCode()));
            jsonObject.addProperty("c_seller_c2code", helper.toString(offer.getSellerC2Code()));
            jsonObject.addProperty("c_item_code", helper.toString(offer.getItemCode()));
            respArr.add(jsonObject);
        });
        return respArr;
    }


    @Override
    public Integer getOffersCount() {

        List<LcOfferMstEntity> list = lcOffersMstRepository.findOffersCount(LocalDateTime.now());

        return list.isEmpty() ? 0 : list.size();
    }

    @Override
    public JsonArray uploadSellerImage(MultipartFile[] files) throws InvalidRequestException, StorageException, IOException, URISyntaxException {

        if (files.length <= 0) {
            throw new InvalidRequestException("file", "not empty");
        }
        JsonArray jsonArray = new JsonArray();
        for (MultipartFile file : files) {
            String imageUrl = this.uploadToBlob(BlobFolder.C2_FOLDER + "/" + BlobFolder.SELLER_FOLDER + "/" + file.getOriginalFilename(), file);
            jsonArray.add(imageUrl);
        }
        return jsonArray;
    }

    @Override
    public void updateSellerImage(ImageUpdateBo imageUpdateBo) throws RecordNotFoundException {

        Optional<LcSupplier> lcSupplier = supplierRepository.findById(imageUpdateBo.getCCode());
        if (lcSupplier == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        Criteria criteria = Criteria.where("_id").is(imageUpdateBo.getCCode());
        org.springframework.data.mongodb.core.query.Query query = org.springframework.data.mongodb.core.query.Query.query(criteria);
        Update update = new Update();
        update.set("ac_images", imageUpdateBo.getAcImages());
        mongoOperations.upsert(query, update, LcSupplier.class);
    }

    @Override
    public JsonArray fetchUnmappedSellers() {
        return null;
    }

    @Override
    public JsonArray sendReqToSeller() {
        return null;
    }

    @Override
    public JsonArray fetchWithFilters() {
        return null;
    }

    @Override
    public JsonObject getSellerLogo(String sellerCode) {
            String sellerLogoQuery = "select lccm.c_name, lid.t_aws_url from lc_c2code_mst lccm  " +
                    " left join lc_images_mst lim on lccm.c_code = lim.c_c2code and lim.c_c2code = lim.c_code and lim.c_type = 'LOGO' " +
                    " left join lc_images_det lid on lim.n_id = lid.n_mstId and lid.n_status = 1 " +
                    " where lccm.c_code = '"+sellerCode+"'";

            javax.persistence.Query query = this.getQuery(sellerLogoQuery);
            List<Object[]> resultList = this.getResultList(query);
            JsonObject jsonObject = new JsonObject();
            if (resultList != null){
                for (Object[] obj : resultList){
                    jsonObject.addProperty("c_seller_name",helper.getString(obj[0]));
                    jsonObject.addProperty("c_seller_logo",helper.getString(obj[1]));
                }
            }
        return jsonObject;
    }

    @Override
    public JsonObject getUitemCode(String sellerCode, String sellerItemCode) {
        String sellerLogoQuery = " select usi.c_ucode from u_stockiest_item usi " +
                "where usi.c_stockiest_code ='"+sellerCode+"' and usi.c_stockiest_item_code = '"+sellerItemCode+"'";

        javax.persistence.Query query = this.getQuery(sellerLogoQuery);
        Object result = this.getSingleResult(query);
        JsonObject jsonObject = new JsonObject();
        if (result != null){
                jsonObject.addProperty("c_item_code",result.toString());
        }
        return jsonObject;
    }

    @Override
    public JsonObject createOffer(LcOfferMstEntity offer) throws DuplicateRecordException, URISyntaxException, IOException, StorageException {

        LcOfferMstEntityPK lcOfferMstEntityPK =
                new LcOfferMstEntityPK(offer.getItemCode(), offer.getOfferCode(), offer.getSellerC2Code());

        Optional<LcOfferMstEntity> optionalLcOfferMstEntity = lcOffersMstRepository.findById(lcOfferMstEntityPK);

        if (optionalLcOfferMstEntity.isPresent()) {
            throw new DuplicateRecordException(lcOfferMstEntityPK + " : Offer already exists!");
        }

        String fileName = offer.getImageFilename();
        String url = this.uploadToBlob(BlobFolder.C2_FOLDER + "/" + BlobFolder.OFFER_FOLDER + fileName + "." + offer.getImageExtension(), offer.getImageFiledata());
        offer.setImageUrl(url);
        offer.setImageExtension(offer.getImageExtension());
        offer.setImageFilename(fileName);

        lcOffersMstRepository.save(offer);

        JsonObject data = new JsonObject();
        data.addProperty("c_item_code", offer.getItemCode());
        data.addProperty("c_offer_code", offer.getOfferCode());
        data.addProperty("c_seller_c2code", offer.getSellerC2Code());
        data.addProperty("c_image_url", offer.getImageUrl());
        LocalDateTime startDate = offer.getStartDate();
        data.addProperty("t_start_date",
                startDate.getYear() + "-" + startDate.getMonth() + "-" + startDate.getDayOfMonth());
        LocalDateTime endDate = offer.getEndDate();
        data.addProperty("t_end_date",
                endDate.getYear() + "-" + endDate.getMonth() + "-" + endDate.getDayOfMonth());
        data.addProperty("c_status", offer.getStatus());

        return data;
    }

    @Override
    public JsonObject updateOffer(LcOfferMstEntity offer) throws RecordNotFoundException, URISyntaxException, IOException, StorageException {
        LcOfferMstEntityPK lcOfferMstEntityPK =
                new LcOfferMstEntityPK(offer.getItemCode(), offer.getOfferCode(), offer.getSellerC2Code());

        Optional<LcOfferMstEntity> optionalLcOfferMstEntity = lcOffersMstRepository.findById(lcOfferMstEntityPK);

        if (optionalLcOfferMstEntity.isEmpty()) {
            throw new RecordNotFoundException(lcOfferMstEntityPK + " : Offer not found");
        }

        LcOfferMstEntity existingOffer = optionalLcOfferMstEntity.get();

        if (offer.getEndDate() != null && !offer.getEndDate().equals(existingOffer.getEndDate())) {
            existingOffer.setEndDate(offer.getEndDate());
        }

        if (offer.getStartDate() != null && !offer.getStartDate().equals(existingOffer.getStartDate())) {
            existingOffer.setStartDate(offer.getStartDate());
        }

        if (offer.getStatus() != null && !offer.getStatus().equals(existingOffer.getStatus())) {
            existingOffer.setStatus(offer.getStatus());
        }

        if (offer.getImageFilename() != null && !offer.getImageFilename().equals(existingOffer.getImageFilename())) {
            String fileName = offer.getImageFilename();
            String url = this.uploadToBlob(BlobFolder.C2_FOLDER + "/" + BlobFolder.OFFER_FOLDER + fileName + "." + offer.getImageExtension(), offer.getImageFiledata());
            existingOffer.setImageUrl(url);
            existingOffer.setImageExtension(offer.getImageExtension());
            existingOffer.setImageFilename(fileName);
        }


        offer = lcOffersMstRepository.save(existingOffer);

        JsonObject data = new JsonObject();
        data.addProperty("c_item_code", offer.getItemCode());
        data.addProperty("c_offer_code", offer.getOfferCode());
        data.addProperty("c_seller_c2code", offer.getSellerC2Code());
        data.addProperty("c_image_url", offer.getImageUrl());
        LocalDateTime startDate = offer.getStartDate();
        data.addProperty("t_start_date",
                startDate.getYear() + "-" + startDate.getMonth() + "-" + startDate.getDayOfMonth());
        LocalDateTime endDate = offer.getEndDate();
        data.addProperty("t_end_date",
                endDate.getYear() + "-" + endDate.getMonth() + "-" + endDate.getDayOfMonth());
        data.addProperty("c_status", offer.getStatus());

        return data;

    }

    @Override
    public void deleteOffer(LcOfferMstEntity offer) throws RecordNotFoundException {

        LcOfferMstEntityPK lcOfferMstEntityPK =
                new LcOfferMstEntityPK(offer.getItemCode(), offer.getOfferCode(), offer.getSellerC2Code());

        Optional<LcOfferMstEntity> optionalLcOfferMstEntity = lcOffersMstRepository.findById(lcOfferMstEntityPK);

        if (optionalLcOfferMstEntity.isEmpty()) {
            throw new RecordNotFoundException(lcOfferMstEntityPK + " : Offer not found");
        }

        lcOffersMstRepository.deleteById(lcOfferMstEntityPK);

    }

    //need to write other function to check list of mapped seller.
    @Override
    public List<SellerImageBo> getListOfMappedSeller(Long firmId, Long userId, int page, int limit) {
        List<SellerImageBo> it = new ArrayList<>();
        return it;
    }


    //for this function code has to be changed accordingly inside the function
    @Override
    public JsonArray mappedSellerSearch(String searchString, int page, int size, long userId, long firmId, long branchId) throws RecordNotFoundException {
        String sql = preparedStatementForMappedSeller();
        Query query = this.getQuery(sql);
        query.setParameter("search", searchString + "%");

        List<Object[]> resultList = this.getResultList(query, page, size);
        JsonArray jsonArray = new JsonArray();
        if (resultList.size() == 0) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        for (Object[] objects : resultList) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            String itemCode = helper.getString(objects[++i]);
            jsonObject.addProperty("c_item_code", itemCode);
            jsonObject.addProperty("c_item_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_item_mfg_code", helper.getString(objects[++i]));
            jsonObject.addProperty("ac_thumbnail_images", helper.getString(objects[++i]));
            jsonObject.addProperty("c_item_mfg_name", helper.getString(objects[++i]));
            jsonObject.addProperty("n_qty_per_box", helper.getString(objects[++i]));
            jsonObject.addProperty("n_max_mrp", helper.getString(objects[++i]));
            jsonObject.addProperty("c_pack_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_contains", helper.getString(objects[++i]));
            jsonObject.addProperty("c_discount_status", Constants.STATUS_NO);
            jsonObject.addProperty("c_watchlist_status", shortBookWatchListService.isWatchList(userId, firmId, branchId, itemCode));
            jsonObject.addProperty("c_short_book_status", shortBookWatchListService.isShortBook(userId, firmId, branchId, itemCode));
            jsonObject.addProperty("c_variant_count", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_cart_status", Constants.STATUS_NO); //TODO user query to update
            jsonObject.addProperty("c_sponsored", Constants.STATUS_NO); //TODO user query to update

            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String preparedStatementForMappedSeller() {
        return "";
    }


}
