package com.c2.lc.ms.master.transactions.interfaces;

import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.AdminBannerBo;
import com.c2.lc.ms.master.bos.BannerResponseBo;
import com.c2.lc.ms.master.models.BannerModel;
import com.c2.lc.ms.master.models.TypeWiseBannerModel;
import com.microsoft.azure.storage.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface BannerTransaction {

    List<BannerResponseBo> getBannersList(String type) throws RecordNotFoundException;
    List<BannerModel> getBannersByState(String state) throws RecordNotFoundException;

    List<BannerModel> getBannersByCity(String city) throws RecordNotFoundException;

    List<BannerModel> getBannersByZip(String zip) throws RecordNotFoundException;

    void saveBanner(BannerModel bannerModel) throws IOException, URISyntaxException, StorageException;

    void deleteBanner(String bannerId) throws RecordNotFoundException;

    BannerModel getByBannerId(String bannerId) throws RecordNotFoundException;

    List<AdminBannerBo> getBannersListTS(String c2code, SearchBO searchBO) throws RecordNotFoundException;

    void save(BannerModel bannerModel);

    AdminBannerBo getBannerTS(String bannerId) throws RecordNotFoundException;

    void updateBannerStatus(String bannerId, int status) throws RecordNotFoundException;

    void updateBanner(AdminBannerBo bannerBo) throws RecordNotFoundException;

    List<AdminBannerBo> getBanners(String c2Code) throws RecordNotFoundException;

    int getAdminBannerCount(String c2Code, SearchBO searchBO);

    List<TypeWiseBannerModel> getTypeWiseBanner(String offerType, PageBO pageBO);
}
