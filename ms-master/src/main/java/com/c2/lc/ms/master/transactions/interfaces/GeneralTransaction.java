package com.c2.lc.ms.master.transactions.interfaces;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.entities.mysql.LoGstTypeEntity;
import com.c2.lc.ms.master.models.MasterModel;
import com.c2.lc.ms.master.transactions.interfaces.base.MasterBaseTransaction;

import java.util.List;

public interface GeneralTransaction extends MasterBaseTransaction {

    List<MasterModel> getStateList();

    List<MasterModel> getCityList(String code);

    List<MasterModel> getAreaList(String code);

    List<LoGstTypeEntity> getGstTypeList();

    List<MasterModel> getStateSearch(String search) throws RecordNotFoundException;

    List<MasterModel> getCityListSearch(String code, String search) throws RecordNotFoundException;

    List<MasterModel> getAreaSearch(String code, String search) throws RecordNotFoundException;
}
