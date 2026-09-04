package com.c2.lc.ms.master.services;

import com.c2.lc.ms.master.entities.mysql.UItemContMstEntity;
import com.c2.lc.ms.master.repos.mysql.ItemContentRepository;
import com.c2.lc.ms.master.services.interfaces.ItemContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemContentServiceImpl implements ItemContentService {

    @Autowired
    ItemContentRepository itemContentRepository;

    @Override
    public UItemContMstEntity getContMst(String contCode) {
        return itemContentRepository.findByContCode(contCode);
    }

    @Override
    public List<UItemContMstEntity> getAllContMst() {
        return itemContentRepository.findAllContents();
    }
}
