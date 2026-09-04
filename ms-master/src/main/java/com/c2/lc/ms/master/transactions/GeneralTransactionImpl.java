package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.ms.master.entities.mysql.LoGstTypeEntity;
import com.c2.lc.ms.master.models.MasterModel;
import com.c2.lc.ms.master.services.interfaces.GeneralService;
import com.c2.lc.ms.master.transactions.interfaces.GeneralTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneralTransactionImpl extends BaseTransactionImpl implements GeneralTransaction {

    @Autowired
    private GeneralService generalService;

    @Override
    public List<MasterModel> getStateList() {
        return generalService.getStateList();
    }

    @Override
    public List<MasterModel> getCityList(String code) {
        return generalService.getCityList(code);
    }

    @Override
    public List<MasterModel> getAreaList(String code) {
        return generalService.getAreaList(code);
    }

    @Override
    public List<LoGstTypeEntity> getGstTypeList() {
        return generalService.geGstTypeList();
    }

    @Override
    public List<MasterModel> getStateSearch(String search) throws RecordNotFoundException {
        return generalService.getStateSearch(search);
    }

    @Override
    public List<MasterModel> getCityListSearch(String code, String search) throws RecordNotFoundException {
        return generalService.getCityListSearch(code, search);
    }

    @Override
    public List<MasterModel> getAreaSearch(String code, String search) throws RecordNotFoundException {
        return generalService.getAreaSearch(code, search);
    }
}
