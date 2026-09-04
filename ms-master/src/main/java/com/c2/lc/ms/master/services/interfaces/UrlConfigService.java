package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.ms.master.entities.mysql.UrlConfig;
import com.c2.lc.ms.master.entities.mysql.UrlConfigPK;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.interfaces.BaseService;

public interface UrlConfigService extends BaseService {
    UrlConfig getUrl(UrlConfigPK urlConfigPK) throws RecordNotFoundException;
}
