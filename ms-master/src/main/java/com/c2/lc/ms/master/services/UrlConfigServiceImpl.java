package com.c2.lc.ms.master.services;

import com.c2.lc.ms.master.services.interfaces.UrlConfigService;
import com.c2.lc.ms.master.entities.mysql.UrlConfig;
import com.c2.lc.ms.master.entities.mysql.UrlConfigPK;
import com.c2.lc.ms.master.repos.mysql.UrlConfigRepository;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlConfigServiceImpl implements UrlConfigService {

    @Autowired private UrlConfigRepository urlConfigRepository;

    @Override
    public UrlConfig getUrl(UrlConfigPK urlConfigPK) throws RecordNotFoundException {
        return urlConfigRepository.findById(urlConfigPK)
                .orElseThrow(() -> new RecordNotFoundException(urlConfigPK.toString(), "Record not found!"));
    }
}
