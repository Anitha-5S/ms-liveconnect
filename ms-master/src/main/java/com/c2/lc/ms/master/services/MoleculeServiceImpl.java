package com.c2.lc.ms.master.services;

import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.ms.master.entities.mongo.LcMolecule;
import com.c2.lc.ms.master.entities.mongo.Molecule;
import com.c2.lc.ms.master.repos.mongo.MoleculeRepository;
import com.c2.lc.ms.master.services.interfaces.MoleculeService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoleculeServiceImpl extends BaseDBServiceImpl implements MoleculeService {

    @Autowired private MoleculeRepository moleculeRepository;
    @Autowired private MongoOperations mongoOperations;

    @Override
    public void saveMolecule(LcMolecule lcMolecule) {
        moleculeRepository.save(lcMolecule);
    }

    @Override
    public LcMolecule getById(String id) throws RecordNotFoundException {
        LcMolecule lcMolecule = moleculeRepository.getById(id);
        if (lcMolecule == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return lcMolecule;
    }

    @Override
    public void deleteMolecule(String id) throws RecordNotFoundException {
        moleculeRepository.deleteById(id);
    }

    @Override
    public List<LcMolecule> getProductsByMolecule(SearchBO searchBO) throws RecordNotFoundException {

        Query query = this.getMongoSearchParameter("c_drug_name", searchBO);
        List<LcMolecule> lcMolecules = mongoOperations.find(query, LcMolecule.class);
        if (lcMolecules.isEmpty()) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return lcMolecules;
    }

    @Override
    public long count(SearchBO searchBO) {
        Query query = this.getMongoSearchParameter("c_drug_name", searchBO);
        return mongoOperations.count(query, Molecule.class);
    }

    @Override
    public JsonArray list(SearchBO searchBO) throws RecordNotFoundException {
        org.springframework.data.mongodb.core.query.Query query = this.getMongoSearchParameter("c_drug_name", searchBO);

        List<Molecule> molecules = mongoOperations.find(query, Molecule.class);
        JsonArray jsonArray = new JsonArray();
        if (molecules.isEmpty()) {
            return jsonArray;
           //throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }

        for (Molecule lcMolecule: molecules) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_molecule_name", lcMolecule.getMoleculeName());
            jsonObject.addProperty("c_molecule_code", lcMolecule.getMoleculeCode());
            jsonArray.add(jsonObject);

        }
        return jsonArray;
    }
}
