package com.c2.lc.ms.master.services;

import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.ms.master.bos.AdminBannerBo;
import com.c2.lc.ms.master.models.BannerModel;
import com.c2.lc.ms.master.models.TypeWiseBannerModel;
import com.c2.lc.ms.master.repos.mongo.BannerRepository;
import com.c2.lc.ms.master.repos.mongo.TypeWiseBannerRepository;
import com.c2.lc.ms.master.services.interfaces.BannerService;
import com.c2.lc.ms.master.utils.BlobFolder;
import com.microsoft.azure.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BannerServiceImpl  extends BaseDBServiceImpl implements BannerService {

    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private TypeWiseBannerRepository typeWiseBannerRepository;

    @Autowired private
    MongoTemplate mongoTemplate;


    @Override
    public List<BannerModel> getBannersList(String type) {
        Query query=new Query();
        query.addCriteria(Criteria.where("c_c2code").is(null).and("c_platform").is(type));
        return mongoTemplate.find(query, BannerModel.class);
    }

    @Override
    public List<BannerModel> getBannerByState(String state) throws RecordNotFoundException {
        List<BannerModel> bannerModelList = bannerRepository.getByState(state);
        if (bannerModelList.isEmpty()) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return bannerModelList;
    }

    @Override
    public List<BannerModel> getBannerByCity(String city) throws RecordNotFoundException {
        List<BannerModel> bannerModelList = bannerRepository.getByCity(city);
        if (bannerModelList.size() == 0) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return bannerModelList;
    }

    @Override
    public void saveBanner(BannerModel entity) throws IOException, URISyntaxException, StorageException {
       /* File convFile = new File(entity.getImageUrl());
        String fileName = convFile.getName().replace("[.][^.]+$", "");
        BufferedImage bufferedImage = ImageIO.read(convFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, MasterConstants.IMAGE_FORMAT, baos);
        byte[] bytes = baos.toByteArray();
        String data = Base64.getMimeEncoder().encodeToString(bytes);*/
        String fileName = entity.getFileName();
        String url=this.uploadToBlob(BlobFolder.C2_FOLDER+"/"+BlobFolder.BANNER_FOLDER+"/"+fileName,entity.getFileData());
        entity.setAwsUrl(url);
        entity.setImageType(BlobFolder.IMAGE_FORMAT);
        entity.setFileName(fileName);
        entity.setFileData("");
        entity.setCreatedAt(helper.getCurrentTime());
        bannerRepository.save(entity);
    }

    @Override
    public void deleteBanner(String bannerId) throws RecordNotFoundException {
        getByBannerId(bannerId);
        bannerRepository.deleteById(bannerId);
    }

    @Override
    @Cacheable(value = "lc_banner", key = "#bannerId")
    public BannerModel getByBannerId(String bannerId) throws RecordNotFoundException {
        BannerModel bannerModel = bannerRepository.getById(bannerId);
        if (bannerModel == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return bannerModel;
    }

    @Override
    public List<BannerModel> getBannersListByC2Code(String c2code) {
        return bannerRepository.findByC2CodeAndActive(c2code);
    }

    @Override
    public void save(BannerModel bannerModel) {
        bannerModel.setStatus(1);
        BannerModel model = bannerRepository.findByOfferType(bannerModel.getOfferType(), bannerModel.getC2Code());
        if (model != null) {
            bannerModel.setId(model.getId());
        } else {
            bannerModel.setCreatedAt(helper.getCurrentTime());
        }
        bannerRepository.save(bannerModel);
    }

    @Override
    public BannerModel getBannerTS(String bannerId) throws RecordNotFoundException {
        BannerModel bannerModel = bannerRepository.getById(bannerId);
        if (bannerModel == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return bannerModel;
    }

    @Override
    public void updateStatus(String bannerId, int status) throws RecordNotFoundException {
        BannerModel bannerModel = bannerRepository.getById(bannerId);
        if (bannerModel == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        bannerModel.setStatus(status);
        bannerRepository.save(bannerModel);
    }

    @Override
    public void updateBanner(AdminBannerBo bannerBo) throws RecordNotFoundException {
        BannerModel bannerModel = bannerRepository.getById(bannerBo.getBannerId());
        if (bannerModel == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        setBannerModel(bannerModel, bannerBo);
        bannerRepository.save(bannerModel);
    }

    private void setBannerModel(BannerModel bannerModel, AdminBannerBo bannerBo) {
        bannerModel.setBannerTitle(bannerBo.getBannerTitle());
        bannerModel.setDisplayName(bannerBo.getDisplayName());
        bannerModel.setDealOption(bannerBo.getDealOption());
        bannerModel.setBannerTempId(bannerBo.getBannerTempId());
        bannerModel.setContentAlign(bannerBo.getContentAlign());
        bannerModel.setBannerDesc(bannerBo.getBannerDesc());
        bannerModel.setItemCode(bannerBo.getItemCode());
        bannerModel.setItemName(bannerBo.getItemName());
        bannerModel.setItemQty(bannerBo.getItemQty());
        bannerModel.setItemFreeQty(bannerBo.getItemFreeQty());
        bannerModel.setBillValue(bannerBo.getBillValue());
        bannerModel.setDiscType(bannerBo.getDiscType());
        bannerModel.setDiscPercentage(bannerBo.getDiscPercentage());
        bannerModel.setDiscAmount(bannerBo.getDiscAmount());
        bannerModel.setStatus(bannerBo.getStatus());
        bannerModel.setOfferType(bannerBo.getOfferType());
        bannerModel.setStartDate(bannerBo.getStartDate());
        bannerModel.setEndDate(bannerBo.getEndDate());
        bannerModel.setDealWiseDetails(bannerBo.getDealWiseDetails());
    }

    @Override
    public List<BannerModel> getBannersListByC2CodeAndSearch(String c2code, SearchBO searchBO) {
        List<BannerModel> list = new ArrayList<>();
        if (!helper.isEmpty(searchBO.getSearchTerm())) {
            Criteria criteria = Criteria.where("c_c2code").is(c2code)
                    .and("c_banner_title").regex(helper.getMongoSearchParameter(searchBO.getSearchTerm()), "i");
            org.springframework.data.mongodb.core.query.Query query = this.getMongoQuery(searchBO.getSearchTerm(), searchBO, criteria);
            query.skip((long) searchBO.getPage() * searchBO.getLimit());
            query.limit(searchBO.getLimit());
            list = mongoTemplate.find(query, BannerModel.class);
        } else {
            Pageable pageable = PageRequest.of(searchBO.getPage(), searchBO.getLimit());
            list = bannerRepository.findByC2Code(c2code, pageable);
        }
        return list;
    }

    @Override
    public int getAdminBannerCount(String c2code, SearchBO searchBO) {
        List<BannerModel> list = new ArrayList<>();
        if (!helper.isEmpty(searchBO.getSearchTerm())) {
            Criteria criteria = Criteria.where("c_c2code").is(c2code)
                    .and("c_banner_title").regex(helper.getMongoSearchParameter(searchBO.getSearchTerm()), "i");
            org.springframework.data.mongodb.core.query.Query query = this.getMongoQuery(searchBO.getSearchTerm(), searchBO, criteria);
            list = mongoTemplate.find(query, BannerModel.class);
        } else {
            list = bannerRepository.findByC2Code(c2code);
        }
        return list.size();
    }

    @Override
    public List<TypeWiseBannerModel> getTypeWiseBanner(String offerType, PageBO pageBO) {
        Pageable pageable = PageRequest.of(pageBO.getPage(), pageBO.getLimit());
        return typeWiseBannerRepository.getByOfferType(offerType, pageable);
    }

    @Override
    public TypeWiseBannerModel getTypeWiseBannerByTempId(int bannerTempId) {
        return typeWiseBannerRepository.getTypeWiseBannerByTempId(bannerTempId);
    }
}
