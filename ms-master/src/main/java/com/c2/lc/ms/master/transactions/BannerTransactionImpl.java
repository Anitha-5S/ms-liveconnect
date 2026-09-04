package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.ms.master.bos.AdminBannerBo;
import com.c2.lc.ms.master.bos.BannerResponseBo;
import com.c2.lc.ms.master.models.BannerModel;
import com.c2.lc.ms.master.models.TypeWiseBannerModel;
import com.c2.lc.ms.master.services.interfaces.BannerService;
import com.c2.lc.ms.master.transactions.interfaces.BannerTransaction;
import com.microsoft.azure.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class BannerTransactionImpl extends BaseTransactionImpl implements BannerTransaction {

    @Autowired
    private BannerService bannerService;

    @Override
    public List<BannerResponseBo> getBannersList(String type) throws RecordNotFoundException {
        List<BannerModel> models =  bannerService.getBannersList(type);
        List<BannerResponseBo> responseBos = new ArrayList<>();
        if (models.size()==0){
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        for (BannerModel model:models){
            BannerResponseBo responseBo = getBannerResponseBo(model);
            responseBos.add(responseBo);
        }
        return responseBos;
    }

    private BannerResponseBo getBannerResponseBo(BannerModel model) {
        BannerResponseBo responseBo = new BannerResponseBo();
        responseBo.setBannerId(model.getId());
        responseBo.setImageUrl(model.getAwsUrl());
        responseBo.setReDirectUrl(model.getRedirectUrl());
        responseBo.setType(model.getType());
        responseBo.setHeightWidth(model.getHeightWidth());
        return responseBo;
    }

    @Override
    public List<BannerModel> getBannersByZip(String zip) throws RecordNotFoundException {
        return new ArrayList<>();
    }

    @Override
    public List<BannerModel> getBannersByState(String state) throws RecordNotFoundException {
        return bannerService.getBannerByState(state);
    }

    @Override
    public List<BannerModel> getBannersByCity(String city) throws RecordNotFoundException {
        return bannerService.getBannerByCity(city);
    }

    @Override
    public void saveBanner(BannerModel bannerModel) throws IOException, URISyntaxException, StorageException {
            bannerService.saveBanner(bannerModel);
    }

    @Override
    public void deleteBanner(String bannerId) throws RecordNotFoundException {
        bannerService.deleteBanner(bannerId);
    }

    @Override
    public BannerModel getByBannerId(String bannerId) throws RecordNotFoundException{
        return bannerService.getByBannerId(bannerId);
    }

    @Override
    public List<AdminBannerBo> getBannersListTS(String c2code, SearchBO searchBO) throws RecordNotFoundException {
        List<BannerModel> models = bannerService.getBannersListByC2CodeAndSearch(c2code, searchBO);
        List<AdminBannerBo> responseBos = new ArrayList<>();
        if (models.size() == 0) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        for (BannerModel model : models) {
            AdminBannerBo responseBo = getAdminBannerBo(model);
            responseBos.add(responseBo);
        }
        return responseBos;
    }

    private AdminBannerBo getAdminBannerBo(BannerModel model) {

        TypeWiseBannerModel offerTypeModel = bannerService.getTypeWiseBannerByTempId(model.getBannerTempId());

        AdminBannerBo responseBo = new AdminBannerBo();
        responseBo.setBannerId(model.getId());
        responseBo.setDisplayName(model.getDisplayName());
        responseBo.setBannerTitle(model.getBannerTitle());
        responseBo.setDealOption(model.getDealOption());
        responseBo.setBannerTempId(model.getBannerTempId());
        responseBo.setContentAlign(model.getContentAlign());
        responseBo.setBannerDesc(model.getBannerDesc());
        responseBo.setItemCode(model.getItemCode());
        responseBo.setItemName(model.getItemName());
        responseBo.setItemQty(model.getItemQty());
        responseBo.setItemFreeQty(model.getItemFreeQty());
        responseBo.setBillValue(model.getBillValue());
        responseBo.setDiscType(model.getDiscType());
        responseBo.setDiscPercentage(model.getDiscPercentage());
        responseBo.setDiscAmount(model.getDiscAmount());
        responseBo.setStatus(model.getStatus());
        responseBo.setBannerTitle(model.getBannerTitle());
        responseBo.setOfferType(model.getOfferType());
        responseBo.setStartDate(model.getStartDate());
        responseBo.setEndDate(model.getEndDate());
        responseBo.setDealWiseDetails(model.getDealWiseDetails());
        if (offerTypeModel != null) {
            responseBo.setBannerURL(offerTypeModel.getImgURL());
            responseBo.setContentAlign(offerTypeModel.getAlignment());
        }
        return responseBo;
    }

    @Override
    public void save(BannerModel bannerModel) {
        bannerService.save(bannerModel);
    }

    @Override
    public AdminBannerBo getBannerTS(String bannerId) throws RecordNotFoundException {
        BannerModel model = bannerService.getBannerTS(bannerId);
        return getAdminBannerBo(model);
    }

    @Override
    public void updateBannerStatus(String bannerId, int status) throws RecordNotFoundException {
        bannerService.updateStatus(bannerId, status);
    }

    @Override
    public void updateBanner(AdminBannerBo bannerBo) throws RecordNotFoundException {
        bannerService.updateBanner(bannerBo);
    }

    @Override
    public List<AdminBannerBo> getBanners(String c2Code) throws RecordNotFoundException {
        List<BannerModel> models = bannerService.getBannersListByC2Code(c2Code);
        List<AdminBannerBo> responseBos = new ArrayList<>();
        if (models.size() == 0) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        for (BannerModel model : models) {
            LocalDate endDate = helper.convertStringToDate(model.getEndDate());
            if (helper.getCurrentDate().isBefore(endDate) || helper.getCurrentDate().isEqual(endDate)) {
                AdminBannerBo responseBo = getTSBannerResponseBo(model);
                responseBos.add(responseBo);
            }
        }
        return responseBos;
    }

    private AdminBannerBo getTSBannerResponseBo(BannerModel model) {
        TypeWiseBannerModel offerTypeModel = bannerService.getTypeWiseBannerByTempId(model.getBannerTempId());

        AdminBannerBo responseBo = new AdminBannerBo();
        responseBo.setDisplayName(model.getDisplayName());
        responseBo.setBannerTitle(model.getBannerTitle());
        responseBo.setDealOption(model.getDealOption());
        responseBo.setContentAlign(model.getContentAlign());
        responseBo.setBannerDesc(model.getBannerDesc());
        responseBo.setItemCode(model.getItemCode());
        responseBo.setItemName(model.getItemName());
        responseBo.setItemQty(model.getItemQty());
        responseBo.setItemFreeQty(model.getItemFreeQty());
        responseBo.setBillValue(model.getBillValue());
        responseBo.setDiscType(model.getDiscType());
        responseBo.setDiscPercentage(model.getDiscPercentage());
        responseBo.setDiscAmount(model.getDiscAmount());
        responseBo.setStatus(model.getStatus());
        responseBo.setBannerTitle(model.getBannerTitle());
        responseBo.setOfferType(model.getOfferType());
        responseBo.setStartDate(model.getStartDate());
        responseBo.setEndDate(model.getEndDate());
        responseBo.setDealWiseDetails(model.getDealWiseDetails());
        responseBo.setBannerURL(offerTypeModel.getImgURL());
        responseBo.setContentAlign(offerTypeModel.getAlignment());
        return responseBo;
    }

    @Override
    public int getAdminBannerCount(String c2Code, SearchBO searchBO) {
        return bannerService.getAdminBannerCount(c2Code, searchBO);
    }

    @Override
    public List<TypeWiseBannerModel> getTypeWiseBanner(String offerType, PageBO pageBO) {
        return bannerService.getTypeWiseBanner(offerType, pageBO);
    }
}
