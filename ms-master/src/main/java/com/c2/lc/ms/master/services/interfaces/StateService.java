package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.c2.lc.ms.master.entities.mysql.UGeoStateMstEntity;
import com.c2.lc.ms.master.models.MasterModel;
import com.c2.lc.lib.exceptions.RecordNotFoundException;

import java.util.List;

public interface StateService extends MasterBaseService {
    List<MasterModel> getListState();

    MasterModel getState(String stateCode) throws RecordNotFoundException;

    UGeoStateMstEntity findById(String stateCode) throws RecordNotFoundException;
}
