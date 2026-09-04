package com.c2.lc.ms.master.services;

import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.ms.master.entities.mongo.LcManufacture;
import com.c2.lc.ms.master.entities.mongo.LcPreferredMfc;
import com.c2.lc.ms.master.repos.mongo.BannerRepository;
import com.c2.lc.ms.master.repos.mongo.LcPreferredMfcRepository;
import com.c2.lc.ms.master.repos.mongo.ManufactureRepository;
import com.c2.lc.ms.master.repos.mongo.RoadBlockRepository;
/*import com.c2.lc.ms.master.repos.mysql.CustItemCategoryMstRepository;*/
import com.c2.lc.ms.master.repos.mysql.LcImagesMstRepository;
import com.c2.lc.ms.master.services.interfaces.CatalogueService;
import com.c2.lc.ms.master.utils.ItemsToTopMostOrderResponseMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class CatalogueServiceImpl extends BaseDBServiceImpl implements CatalogueService {

    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private RoadBlockRepository roadBlockRepository;
    @Autowired
    private ManufactureRepository manufactureRepository;
    @Autowired
    private LcImagesMstRepository lcImagesMstRepository;
    /*@Autowired
    private CustItemCategoryMstRepository categoryMstRepository;*/
    @Autowired
    private ItemsToTopMostOrderResponseMapper itemsToTopMostOrderResponseMapper;

    @Autowired
    private LcPreferredMfcRepository preferredMfcRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${ms.firm.service.api.url}")
    private String msFirmServiceApiUrl;

    @Override
    public List<LcManufacture> getManufacturerList(PageBO pageBO) throws RecordNotFoundException {
        Pageable pageable = PageRequest.of(pageBO.getPage(), pageBO.getLimit());
        Page<LcManufacture> manufactures = manufactureRepository.findAll(pageable);
        if (manufactures.getContent().isEmpty()) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return manufactures.getContent();
    }

    @Override
    public void saveManufacture(LcManufacture lcManufacture) {
        manufactureRepository.save(lcManufacture);
    }

    @Override
    public long manufactureCount() {
        return manufactureRepository.count();
    }

    @Override
    public JsonArray getLimitedOfferList(PageBO pageBO) {
        int limit = pageBO.getLimit();
        int page = pageBO.getPage();
        Pageable pageable = PageRequest.of(page - 1, limit);
        JsonArray respArr = new JsonArray();
        lcImagesMstRepository.findOffers(pageable).forEach(objects -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_offer_image", helper.toString(objects[0]));
            jsonObject.addProperty("c_offer_code", helper.toString(objects[1]));
            jsonObject.addProperty("c_seller_code", helper.toString(objects[2]));
            respArr.add(jsonObject);
        });
        return respArr;
    }

    @Override
    public String getFirmState(Long firmId) throws CommunicationErrorException, InvalidRequestException {
        String stateCode = null;
        JsonObject service = getFirm(firmId);
        if (service.has("cstateCode")) {
            stateCode = service.get("cstateCode").getAsString();
        } else if (service.has("c_state_code")) {
            stateCode = service.get("c_state_code").getAsString();
        }
        if(helper.isEmpty(stateCode)){
            return stateCode ;
        }
        else{
            return stateCode.substring(stateCode.length() - 2);
        }
    }

    @Override
    public JsonObject getFirm(Long firmId) throws CommunicationErrorException, InvalidRequestException {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-csquare-c2-code", firmId + "");
        headers.put("x-csquare-firm-id",firmId + "");
        return callCustomerService(headers, msFirmServiceApiUrl);
    }

    @Override
    public List<LcPreferredMfc> preferredMfc(String stateCode , PageBO pageBO, JsonObject jsonObject) {
       // Pageable pageable = PageRequest.of(searchBO.getPage(), searchBO.getLimit(),Sort.by(Sort.Direction.DESC,"n_count"));
        //List<LcPreferredMfc> preferredMfcList = preferredMfcRepository.preferredMfc(stateCode,pageable );
        Criteria criteria = null;
        if ((jsonObject.has("c_search_term")) && !jsonObject.get("c_search_term").getAsString().isEmpty()) {
            String searchKey = jsonObject.get("c_search_term").getAsString();
            if(searchKey.contains("(")){
                searchKey = searchKey.replace("(","\\(");
            }
            if(searchKey.contains(")")){
                searchKey = searchKey.replace(")","\\)");
            }
            criteria = Criteria.where("c_state_code").is(stateCode).and("c_mfac_name").regex(searchKey, "i");
        } else {
            criteria = Criteria.where("c_state_code").is(stateCode);
        }
        Query query = Query.query(criteria);
        query.limit(pageBO.getLimit());
        query.skip((long) pageBO.getPage() * pageBO.getLimit());
        query.with(Sort.by(Sort.Direction.DESC, "n_count"));
        return mongoTemplate.find(query, LcPreferredMfc.class);
    }

    @Override
    public long preferredMfcCount(String stateCode, JsonObject jsonObject) {
        Criteria criteria = null;
        if ((jsonObject.has("c_search_term")) && !jsonObject.get("c_search_term").getAsString().isEmpty()) {
            String searchKey = jsonObject.get("c_search_term").getAsString();
            if(searchKey.contains("(")){
                searchKey = searchKey.replace("(","\\(");
            }
            if(searchKey.contains(")")){
                searchKey = searchKey.replace(")","\\)");
            }
            criteria = Criteria.where("c_state_code").is(stateCode).and("c_mfac_name").regex(searchKey, "i");
        } else {
            criteria = Criteria.where("c_state_code").is(stateCode);
        }
        Query query = Query.query(criteria);
        query.with(Sort.by(Sort.Direction.DESC, "n_count"));
        return mongoTemplate.count(query, LcPreferredMfc.class);
        //return preferredMfcRepository.preferredMfcCount(stateCode);
    }

    private JsonObject callCustomerService(Map<String, String> headers, String url) throws CommunicationErrorException, InvalidRequestException {

        String result = callWebClientGetSyncApi(url, headers);
        log.debug("C2 Service Response : {}" + result);

        JsonObject responseObject;
        if (result == null || result.isEmpty()) {
            log.error("Result is null API {} -- Response {}", url, result);
            throw new CommunicationErrorException("", "Error connecting to Contact Detail!");
        } else {
            responseObject = helper.getJsonObject(result);
            if (responseObject.get("appStatusCode").getAsInt() != 0) {
                log.error("API {} -- Response {}", url, result);
                throw new InvalidRequestException("", "Invalid Request!");
            } else {
                log.debug("Response {}", result);

            }
        }

        return responseObject.get("payloadJson").getAsJsonObject().get("data").getAsJsonObject();
    }

}
