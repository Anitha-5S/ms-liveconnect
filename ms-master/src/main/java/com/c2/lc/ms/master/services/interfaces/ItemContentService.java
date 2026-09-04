package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.ms.master.entities.mysql.UItemContMstEntity;
import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;

import java.util.List;

public interface ItemContentService extends MasterBaseService {

    UItemContMstEntity getContMst(String contCode);

    List<UItemContMstEntity> getAllContMst();
}
