package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.c2.lc.ms.master.models.MasterModel;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.google.gson.JsonArray;

import java.util.List;

public interface AreaService extends MasterBaseService {

    List<MasterModel> getListArea();

    JsonArray getArea(String areaCode) throws RecordNotFoundException;

    List<MasterModel> getAreaByStateCode(String stateCode);

    List<MasterModel> getAreaByCityCode(String cityCode);
}
