package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.ms.master.entities.mysql.UItemMfacMstEntity;
import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;

import java.util.List;

public interface ItemMfacService extends MasterBaseService {

    UItemMfacMstEntity getMfacMst(String mfacCode);

    List<UItemMfacMstEntity> getAllMfacMst();

}
