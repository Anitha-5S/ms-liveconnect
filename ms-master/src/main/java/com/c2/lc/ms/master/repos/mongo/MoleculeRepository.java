package com.c2.lc.ms.master.repos.mongo;

import com.c2.lc.ms.master.entities.mongo.LcMolecule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MoleculeRepository extends MongoRepository<LcMolecule, String> {

    @Query("{_id : ?0}")
    LcMolecule getById(String id);
}
