package com.c2.lc.ms.master.transactions;

import com.c2.lc.ms.master.services.interfaces.UrlConfigService;
import com.c2.lc.ms.master.transactions.interfaces.UrlConfigTransaction;
import com.c2.lc.ms.master.entities.mysql.UrlConfig;
import com.c2.lc.ms.master.entities.mysql.UrlConfigPK;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UrlConfigTransactionImpl implements UrlConfigTransaction {

    @Autowired private UrlConfigService urlConfigService;

    @Override
    public UrlConfig getUrl(String c_c2code, String c_product_code, String c_env) throws RecordNotFoundException {
      return urlConfigService.getUrl(new UrlConfigPK(c_c2code, c_product_code, c_env));
    }
}
