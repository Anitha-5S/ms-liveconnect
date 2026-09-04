package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.ms.master.entities.mysql.UItemBrandMstEntity;
import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;

import java.util.List;

public interface ItemBrandService extends MasterBaseService {

    UItemBrandMstEntity getBrandMst(String contCode);

    List<UItemBrandMstEntity> getAllBrandMst();
}
