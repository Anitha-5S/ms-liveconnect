package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.interfaces.BaseDBService;
import com.c2.lc.ms.master.entities.mongo.LcMolecule;
import com.google.gson.JsonArray;

import java.util.List;

public interface MoleculeService extends BaseDBService {

    void saveMolecule(LcMolecule lcMolecule);

    LcMolecule getById(String id) throws RecordNotFoundException;

    void deleteMolecule(String id) throws RecordNotFoundException;

    List<LcMolecule> getProductsByMolecule(SearchBO searchBO) throws RecordNotFoundException;

    long count(SearchBO searchBO);

    JsonArray list(SearchBO searchBO)throws RecordNotFoundException;
}
