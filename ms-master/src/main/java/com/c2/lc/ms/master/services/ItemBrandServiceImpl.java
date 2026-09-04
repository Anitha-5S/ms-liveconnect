package com.c2.lc.ms.master.services;

import com.c2.lc.ms.master.entities.mysql.UItemBrandMstEntity;
import com.c2.lc.ms.master.repos.mysql.ItemBrandRepository;
import com.c2.lc.ms.master.services.interfaces.ItemBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemBrandServiceImpl implements ItemBrandService {

    @Autowired
    ItemBrandRepository itemBrandRepository;

    @Override
    public UItemBrandMstEntity getBrandMst(String contCode) {
        return itemBrandRepository.findByBrandCode(contCode);
    }

    @Override
    public List<UItemBrandMstEntity> getAllBrandMst() {
        return null;
    }
}
