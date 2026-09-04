package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.ms.master.entities.mysql.UItemPackMstEntity;
import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;

import java.util.List;

public interface ItemPackService extends MasterBaseService {

    UItemPackMstEntity getPackMst(String packCode);

    List<UItemPackMstEntity> getAllPackMst();
}
