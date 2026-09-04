package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.ms.master.entities.mongo.LcMolecule;
import com.c2.lc.ms.master.services.interfaces.MoleculeService;
import com.c2.lc.ms.master.transactions.interfaces.MoleculeTransaction;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MoleculeTransactionImpl extends BaseTransactionImpl implements MoleculeTransaction {

    @Autowired private MoleculeService moleculeService;

    @Override
    public void saveMolecule(LcMolecule lcMolecule) {
        moleculeService.saveMolecule(lcMolecule);
    }

    @Override
    public LcMolecule getById(String id) throws RecordNotFoundException {
        return moleculeService.getById(id);
    }

    @Override
    public List<LcMolecule> getProductsByMolecule(SearchBO searchBO) throws RecordNotFoundException {
        return moleculeService.getProductsByMolecule(searchBO);
    }

    @Override
    public JsonArray list(SearchBO searchBO) throws RecordNotFoundException{
        return moleculeService.list(searchBO);
    }

    @Override
    public long count(SearchBO searchBO) {
        return moleculeService.count(searchBO);
    }
}
