package com.c2.lc.ms.customer.services.interfaces;

import com.c2.lc.ms.customer.bos.BranchModelBO;
import com.c2.lc.ms.customer.bos.UserModelBO;
import com.c2.lc.ms.customer.entities.customer.FirmUserRoleEntity;
import com.c2.lc.ms.customer.entities.customer.pk.FirmUserRolePKEntity;
import com.c2.lc.ms.customer.services.interfaces.base.LcBaseService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public interface FirmUserRoleService extends LcBaseService {
    void addRoleToUser(Long userId, Long firmId, Long createdUserId, UserModelBO userRole);

    void addRoleToUser(Long userId, Long firmId, Long createdUserId, BranchModelBO branchModelBO);

    void deleteById(FirmUserRolePKEntity firmUserRolePKEntity);

    List<FirmUserRoleEntity> getUserRoleByUserId(Long nUserId);

    JsonArray getArrayUserRoleByUserId(Long uId);

    JsonObject getUserRolesOnId(Long uId, Long firmId);

    FirmUserRoleEntity getFirmUserRole(Long nUserId, Long nBranchId);

    boolean checkFirmUserRoleExists(Long nUserId, Long nBranchId);

    List<FirmUserRoleEntity> getUserRoleByFirmId(Long firmId);

    List<FirmUserRoleEntity> getBranchesByUserId(Long uId);
}
