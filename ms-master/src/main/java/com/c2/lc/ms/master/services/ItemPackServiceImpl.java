package com.c2.lc.ms.master.services;

import com.c2.lc.ms.master.entities.mysql.UItemPackMstEntity;
import com.c2.lc.ms.master.repos.mysql.ItemPackRepository;
import com.c2.lc.ms.master.services.interfaces.ItemPackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemPackServiceImpl implements ItemPackService {

    @Autowired
    ItemPackRepository itemPackRepository;

    @Override
    public UItemPackMstEntity getPackMst(String packCode) {
        return itemPackRepository.findByPackCode(packCode);
    }

    @Override
    public List<UItemPackMstEntity> getAllPackMst() {
        return  itemPackRepository.findAllPacks();
    }
}
