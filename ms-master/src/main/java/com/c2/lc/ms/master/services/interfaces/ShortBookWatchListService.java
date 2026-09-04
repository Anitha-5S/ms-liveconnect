package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.services.interfaces.BaseDBService;
import com.c2.lc.ms.master.entities.mongo.LcShortBook;
import com.c2.lc.ms.master.entities.mongo.LcWatchList;

import java.util.List;

public interface ShortBookWatchListService extends BaseDBService {

    public String isShortBook(Long userId, Long firmId, Long branchId, String itemCode);
    public String isWatchList(Long userId, Long firmId, Long branchId, String itemCode);
    List<LcWatchList> getWatchList(Long userId, Long firmId, Long branchId);
    List<LcShortBook> getShortBook(Long userId, Long firmId, Long branchId);
    public String isScheme(String code);
}
