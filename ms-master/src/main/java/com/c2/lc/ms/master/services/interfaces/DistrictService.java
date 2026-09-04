package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.c2.lc.ms.master.entities.mysql.UGeoDistrictMstEntity;
import com.c2.lc.lib.exceptions.RecordNotFoundException;

import java.util.List;

public interface DistrictService extends MasterBaseService {
    List<UGeoDistrictMstEntity> getByStateCode(String stateCode) throws RecordNotFoundException;
}
