package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.AdminBannerBo;
import com.c2.lc.ms.master.models.BannerModel;
import com.c2.lc.ms.master.models.TypeWiseBannerModel;
import com.microsoft.azure.storage.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface BannerService {

    List<BannerModel> getBannerByState(String state) throws RecordNotFoundException;

    List<BannerModel> getBannerByCity(String city) throws RecordNotFoundException;

    void saveBanner(BannerModel bannerModel) throws IOException, URISyntaxException, StorageException;


    void deleteBanner(String bannerId) throws RecordNotFoundException;

    BannerModel getByBannerId(String bannerId) throws RecordNotFoundException;

    List<BannerModel> getBannersList(String type);

    List<BannerModel> getBannersListByC2Code(String c2code);

    void save(BannerModel bannerModel);

    BannerModel getBannerTS(String bannerId) throws RecordNotFoundException;

    void updateStatus(String bannerId, int status) throws RecordNotFoundException;

    void updateBanner(AdminBannerBo bannerBo) throws RecordNotFoundException;

    List<BannerModel> getBannersListByC2CodeAndSearch(String c2code, SearchBO searchBO);

    int getAdminBannerCount(String c2code, SearchBO searchBO);

    List<TypeWiseBannerModel> getTypeWiseBanner(String offerType, PageBO pageBO);

    TypeWiseBannerModel getTypeWiseBannerByTempId(int bannerTempId);
}
