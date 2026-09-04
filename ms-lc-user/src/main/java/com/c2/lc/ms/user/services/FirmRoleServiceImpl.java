package com.c2.lc.ms.user.services;

import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.ms.user.entities.FirmRoleEntity;
import com.c2.lc.ms.user.repos.FirmRoleRepo;
import com.c2.lc.ms.user.services.interfaces.FirmRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Service
public class FirmRoleServiceImpl extends BaseDBServiceImpl implements FirmRoleService {

    @Autowired private FirmRoleRepo firmRoleRepo;

    @Override
    public FirmRoleEntity getExist(String mobile, String c2Code, String actCode, String type) {
        return firmRoleRepo.getExists(mobile, c2Code, actCode, type);
    }

    @Override
    public void saveOrUpdate(FirmRoleEntity firmRole) {
            firmRoleRepo.save(firmRole);
    }

    @Override
    @Transactional
    public Long getNextSeq() {
        Query query = this.getQuery("SELECT NEXTVAL(:sequence)");
        query.setParameter("sequence", "firm_role_seq");
        return ((BigInteger) this.getSingleResult(query)).longValue();
    }

    @Override
    public List<FirmRoleEntity> getNotInLock(String mobile, String c2Code, String actCode) {
        return firmRoleRepo.getNotInLock(mobile, c2Code, actCode);
    }
}
