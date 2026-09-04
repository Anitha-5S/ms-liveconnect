package com.c2.lc.ms.master.services;

import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.entities.mongo.LcShortBook;
import com.c2.lc.ms.master.entities.mongo.LcWatchList;
import com.c2.lc.ms.master.repos.mongo.LcShortBookMongoRepository;
import com.c2.lc.ms.master.repos.mongo.LcWatchListMongoRepository;
import com.c2.lc.ms.master.services.interfaces.ShortBookWatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortBookWatchListServiceImpl extends BaseDBServiceImpl implements ShortBookWatchListService {

    @Autowired
    private LcShortBookMongoRepository lcShortBookMongoRepository;

    @Autowired
    private LcWatchListMongoRepository lcWatchListMongoRepository;

    @Override
    public String isShortBook(Long userId, Long firmId, Long branchId, String itemCode) {
        LcShortBook lcShortBook = lcShortBookMongoRepository.getByItemAndBranch(itemCode, branchId.toString(), userId, firmId);
        if (lcShortBook== null)
            return Constants.STATUS_NO;
        return Constants.STATUS_YES;
    }

    @Override
    public String isWatchList(Long userId, Long firmId, Long branchId, String itemCode) {

        LcWatchList lcWatchList = lcWatchListMongoRepository.getByItemAndBranch(itemCode, branchId.toString(), userId, firmId);
        if (lcWatchList== null)
            return Constants.STATUS_NO;
        return Constants.STATUS_YES;
    }
    @Override
    public List<LcWatchList> getWatchList(Long userId, Long firmId, Long branchId) {

      return lcWatchListMongoRepository.getByBranch(branchId.toString(), userId, firmId);
    }

    @Override
    public List<LcShortBook> getShortBook(Long userId, Long firmId, Long branchId) {

        return lcShortBookMongoRepository.getByBranch(branchId.toString(), userId, firmId);
    }


    @Override
    public String isScheme(String code) {
        return Constants.STATUS_NO;
    }
}
