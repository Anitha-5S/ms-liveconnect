package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.entities.mysql.LoGstTypeEntity;
import com.c2.lc.ms.master.models.MasterModel;
import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;

import java.util.List;

public interface GeneralService extends MasterBaseService {
    List<MasterModel> getStateList();

    List<MasterModel> getCityList(String code);

    List<MasterModel> getAreaList(String code);

    List<LoGstTypeEntity> geGstTypeList();

    List<MasterModel> getStateSearch(String search) throws RecordNotFoundException;

    List<MasterModel> getCityListSearch(String code, String search) throws RecordNotFoundException;

    List<MasterModel> getAreaSearch(String code, String search) throws RecordNotFoundException;
}
