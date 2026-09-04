package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.c2.lc.ms.master.entities.mysql.UGeoCityMstEntity;
import com.c2.lc.ms.master.entities.mysql.UGeoDistrictMstEntity;
import com.c2.lc.ms.master.models.MasterModel;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.google.gson.JsonArray;

import java.util.List;

public interface CityService extends MasterBaseService {
    List<MasterModel> getListCity();

    JsonArray getCity(String cityId) throws RecordNotFoundException;

    List<UGeoCityMstEntity> getByDistrictCode(String districtCode);

    List<UGeoCityMstEntity> getByDistrictCode(List<UGeoDistrictMstEntity> districtList);

    List<MasterModel> getCityByStateCode(String stateCode);


}
