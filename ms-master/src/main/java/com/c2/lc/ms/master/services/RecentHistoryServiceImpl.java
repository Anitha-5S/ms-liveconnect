package com.c2.lc.ms.master.services;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.ItemBo;
import com.c2.lc.ms.master.bos.ManufacturerBo;
import com.c2.lc.ms.master.bos.MoleculeBo;
import com.c2.lc.ms.master.bos.SellerBo;
import com.c2.lc.ms.master.entities.mongo.LcItem;
import com.c2.lc.ms.master.entities.mongo.LcMolecule;
import com.c2.lc.ms.master.entities.mongo.Molecule;
import com.c2.lc.ms.master.entities.mongo.RecentHistory;
import com.c2.lc.ms.master.repos.mongo.LcItemRepository;
import com.c2.lc.ms.master.repos.mongo.MoleculeRepository;
import com.c2.lc.ms.master.repos.mongo.RecentHistoryRepository;
import com.c2.lc.ms.master.services.interfaces.RecentHistoryService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Order;
import java.util.List;

@Service
public class RecentHistoryServiceImpl extends BaseDBServiceImpl implements RecentHistoryService {

   @Autowired private RecentHistoryRepository recentHistoryRepository;
   @Autowired private LcItemRepository itemRepository;
   @Autowired private MoleculeRepository moleculeRepository;
   @Autowired private MongoTemplate mongoTemplate;

    @Override
    public void save(RecentHistory recentHistory) {
        recentHistoryRepository.save(recentHistory);
    }

    @Override
    public RecentHistory getById(long userId, String type, long firmId) throws RecordNotFoundException {
        Query query1 = new Query(Criteria.where("c_user_id").is(userId).and("c_type").is(type)
                .and("c_firm_id").is(firmId));
        List<RecentHistory> recentHistories = mongoTemplate.find(query1, RecentHistory.class);
        if (recentHistories.size() == 0) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return recentHistories.get(recentHistories.size() - 1);
    }

    @Override
    public void clearHistory(String type, long userId, long firmId) throws RecordNotFoundException {
        Query query1 = new Query(Criteria.where("c_user_id").is(userId).and("c_type").is(type)
                .and("c_firm_id").is(firmId));
        List<RecentHistory> recentHistories = mongoTemplate.find(query1, RecentHistory.class);
        if (recentHistories.size() == 0) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        recentHistoryRepository.deleteAll(mongoTemplate.find(query1, RecentHistory.class));
    }

    @Override
    public void addRecentHistory(LcHeaderBO header, String type, String code) throws RecordNotFoundException, DuplicateRecordException {
        //  RecentHistory recentHistory = getById(header.getUserId(), type, header.getFirmId());
        Criteria criteria = null;
        org.springframework.data.mongodb.core.query.Query query = null;
        RecentHistory recentHistory = new RecentHistory();
        recentHistory.setFirmId(header.getFirmId());
        recentHistory.setUserId(header.getUserId());
        recentHistory.setType(type);
        recentHistory.setCCode(code);
        recentHistory.setADateTime(helper.getCurrentTime());

        switch (type) {
            case com.c2.lc.ms.master.utils.Constants.MANUFACTURE:
                criteria = Criteria.where("c_user_id").is(header.getUserId()).and("c_firm_id").is(header.getFirmId()).and("c_type")
                        .is(type).and("c_manufacturer_detail.mfgCode").is(code);
                query = org.springframework.data.mongodb.core.query.Query.query(criteria);
                RecentHistory recentHistoryMfc = mongoTemplate.findOne(query, RecentHistory.class);
                if (recentHistoryMfc != null) {
                    recentHistoryRepository.delete(recentHistoryMfc);
                }
                recentHistory.setManufacturerBo(addMfcDetails(code));
                recentHistoryRepository.save(recentHistory);
                break;
            case com.c2.lc.ms.master.utils.Constants.MOLECULE:
                criteria = Criteria.where("c_user_id").is(header.getUserId()).and("c_firm_id").is(header.getFirmId()).and("c_type")
                        .is(type).and("c_molecule_detail.moleculeCode").is(code);
                query = org.springframework.data.mongodb.core.query.Query.query(criteria);
                RecentHistory recentHistoryMol = mongoTemplate.findOne(query, RecentHistory.class);
                if (recentHistoryMol != null) {
                    recentHistoryRepository.delete(recentHistoryMol);
                }
                recentHistory.setMoleculeBo(addMoleculeDetails(code));
                recentHistoryRepository.save(recentHistory);
                break;
            case com.c2.lc.ms.master.utils.Constants.SELLER:
                criteria = Criteria.where("c_user_id").is(header.getUserId()).and("c_firm_id").is(header.getFirmId()).and("c_type")
                        .is(type).and("c_seller_detail.sellerCode").is(code);
                query = org.springframework.data.mongodb.core.query.Query.query(criteria);
                RecentHistory recentHistorySeller = mongoTemplate.findOne(query, RecentHistory.class);
                if (recentHistorySeller != null) {
                    recentHistoryRepository.delete(recentHistorySeller);
                }
                recentHistory.setSellerBo(addSellerDetails(type, header, code));
                recentHistoryRepository.save(recentHistory);
                break;
            case com.c2.lc.ms.master.utils.Constants.PRODUCT:
                criteria = Criteria.where("c_user_id").is(header.getUserId()).and("c_firm_id").is(header.getFirmId()).and("c_type")
                        .is(type).and("c_item_detail.itemUCode").is(code);
                query = org.springframework.data.mongodb.core.query.Query.query(criteria);
                RecentHistory recentHistoryPrd = mongoTemplate.findOne(query, RecentHistory.class);
                if (recentHistoryPrd != null) {
                    recentHistoryRepository.delete(recentHistoryPrd);
                }
                recentHistory.setItemBo(addItemDetails(code));
                recentHistoryRepository.save(recentHistory);
                break;
        }
    }

    @Override
    public JsonArray getManufactureDetails(LcHeaderBO header, String type) {
        List<RecentHistory> recentHistories = getRecentHistories(header, type);

        JsonArray jsonArray = new JsonArray();
        for (RecentHistory objects : recentHistories) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_manufacture_code", objects.getManufacturerBo().getMfgCode());
            jsonObject.addProperty("c_manufacture_name", objects.getManufacturerBo().getMfcName());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public JsonArray getMolecules(LcHeaderBO header, String type) {
        List<RecentHistory> recentHistories = getRecentHistories(header, type);

        JsonArray jsonArray = new JsonArray();
        for (RecentHistory objects : recentHistories) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_molecule_code", objects.getMoleculeBo().getMoleculeCode());
            jsonObject.addProperty("c_molecule_name", objects.getMoleculeBo().getMoleculeName());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public JsonArray getSellerDetails(LcHeaderBO header, String type) {
        List<RecentHistory> recentHistories = getRecentHistories(header, type);

        JsonArray jsonArray = new JsonArray();
        for (RecentHistory objects : recentHistories) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_seller_code", objects.getSellerBo().getSellerCode());
            jsonObject.addProperty("c_seller_name", objects.getSellerBo().getSellerName());
            jsonObject.addProperty("c_seller_city", objects.getSellerBo().getSellerCity());
            jsonObject.addProperty("n_schemes", objects.getSellerBo().getScheme());
            jsonObject.addProperty("c_sponsored", objects.getSellerBo().getSponsored());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public JsonArray getProductDetails(LcHeaderBO header, String type) {
        List<RecentHistory> recentHistories = getRecentHistories(header, type);

        JsonArray jsonArray = new JsonArray();
        for (RecentHistory objects : recentHistories) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_item_code", objects.getItemBo().getItemCode());
            jsonObject.addProperty("c_item_ucode", objects.getItemBo().getItemUCode());
            jsonObject.addProperty("c_item_name", objects.getItemBo().getItemName());
            jsonObject.addProperty("c_mfg_code", objects.getItemBo().getMfgCode());
            jsonObject.addProperty("c_mfg_name", objects.getItemBo().getMfgName());
            jsonObject.addProperty("n_mrp", objects.getItemBo().getMrp());
            jsonObject.addProperty("c_pack_name", objects.getItemBo().getPackName());
            jsonObject.addProperty("c_variant_count", objects.getItemBo().getVariantCount());
            jsonObject.addProperty("c_sponsored", objects.getItemBo().getSponsored());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private List<RecentHistory> getRecentHistories(LcHeaderBO header, String type) {
        Criteria criteria = Criteria.where("c_user_id").is(header.getUserId()).and("c_type").is(type)
                .and("c_firm_id").is(header.getFirmId());

        Query query = Query.query(criteria);
        query.limit(10);
        query.skip(0);
        query.with(Sort.by(Sort.Direction.DESC, "d_aDate"));
        return mongoTemplate.find(query, RecentHistory.class);
    }

    private ManufacturerBo addMfcDetails(String code) {
        PageBO pageBO = new PageBO();
        String mfcSql = " SELECT " +
                "   uimm.c_code mfacCode, " +
                "   uimm.c_name mfacName " +
                "   FROM u_item_mfac_mst uimm " +
                "   WHERE uimm.c_code = '" + code + "' ORDER BY uimm.c_name ASC";
        javax.persistence.Query query = this.getQuery(mfcSql);
        ManufacturerBo manufacturerBo = new ManufacturerBo();
        List<Object[]> resultList = this.getResultList(query, pageBO.getPage(), pageBO.getLimit());
        if (resultList == null) {
            return manufacturerBo;
            //throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }

        for (Object[] objects : resultList) {
            int i = -1;
            manufacturerBo.setMfgCode(helper.getString(objects[0]));
            manufacturerBo.setMfcName(helper.getString(objects[1]));
        }
        return manufacturerBo;
    }

    private MoleculeBo addMoleculeDetails(String code) {
        Criteria criteria = Criteria.where("_id").is(code);
        Query query = Query.query(criteria);
        Molecule molecules = mongoTemplate.findOne(query, Molecule.class);
        MoleculeBo moleculeBo = new MoleculeBo();
        assert molecules != null;
        moleculeBo.setMoleculeCode(molecules.getMoleculeCode());
        moleculeBo.setMoleculeName(molecules.getMoleculeName());
        return moleculeBo;
    }

    private ItemBo addItemDetails(String code) {
        LcItem lcItem = itemRepository.getById(code);
        ItemBo itemBo = new ItemBo();
        itemBo.setItemCode(code);
        itemBo.setItemUCode(lcItem.getItemCode());
        itemBo.setItemName(lcItem.getItemName());
        itemBo.setMfgCode(lcItem.getMfgCode());
        itemBo.setMfgName(lcItem.getMfgName());
        itemBo.setMrp(lcItem.getMrp());
        itemBo.setPackName(lcItem.getPackName());
        return itemBo;
    }

    private SellerBo addSellerDetails(String type, LcHeaderBO header, String code) {
        PageBO pageBO = new PageBO();
        String sellerInfo = " SELECT " +
                "    lcm.c_code sellerCode, " +
                "    lcm.c_name sellerName, " +
                "    lcm.c_city sellerCity " +
                " FROM lc_c2code_mst lcm " +
                " WHERE lcm.c_code = '" + code + "' ORDER BY lcm.c_name ASC";
        javax.persistence.Query query = this.getQuery(sellerInfo);
        SellerBo sellerBo = new SellerBo();
        List<Object[]> resultList = this.getResultList(query, pageBO.getPage(), pageBO.getLimit());
        if (resultList == null) {
            return sellerBo;
            //throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }

        for (Object[] objects : resultList) {
            int i = -1;
            sellerBo.setSellerCode(helper.getString(objects[0]));
            sellerBo.setSellerName(helper.getString(objects[1]));
            sellerBo.setSellerCity(helper.getString(objects[2]));
        }
        return sellerBo;
    }
}
