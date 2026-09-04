package com.c2.lc.ms.master.transactions.interfaces;

import com.c2.lc.ms.master.entities.mysql.UrlConfig;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.transactions.interfaces.BaseTransaction;

public interface UrlConfigTransaction extends BaseTransaction {
    UrlConfig getUrl(String c_c2code, String c_product_code, String c_env) throws RecordNotFoundException;
}
