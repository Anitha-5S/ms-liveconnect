package com.c2.lc.ms.master.services;

import com.c2.lc.ms.master.entities.mysql.UItemMfacMstEntity;
import com.c2.lc.ms.master.repos.mysql.ItemMfacRepository;
import com.c2.lc.ms.master.services.interfaces.ItemMfacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemMfacServiceImpl implements ItemMfacService {

    @Autowired
    ItemMfacRepository itemMfacRepository;

    @Override
    public UItemMfacMstEntity getMfacMst(String mfacCode) {
        return itemMfacRepository.findByMfacCode(mfacCode);
    }

    @Override
    public List<UItemMfacMstEntity> getAllMfacMst() {
        return itemMfacRepository.findAllMfacs();
    }
}
