package com.c2.lc.ms.customer.services;

import com.c2.lc.ms.customer.bos.BranchModelBO;
import com.c2.lc.ms.customer.bos.UserModelBO;
import com.c2.lc.ms.customer.entities.customer.FirmEntity;
import com.c2.lc.ms.customer.entities.customer.FirmUserRoleEntity;
import com.c2.lc.ms.customer.entities.customer.pk.FirmUserRolePKEntity;
import com.c2.lc.ms.customer.repos.customer.FirmRepo;
import com.c2.lc.ms.customer.repos.customer.FirmUserRoleRepo;
import com.c2.lc.ms.customer.services.base.LcBaseServiceImpl;
import com.c2.lc.ms.customer.services.interfaces.FirmUserRoleService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FirmUserRoleServiceImpl extends LcBaseServiceImpl implements FirmUserRoleService {

    @Autowired private FirmUserRoleRepo firmUserRoleRepo;
    @Autowired private FirmRepo firmRepo;

    public void createFirmUserRole(Long userId, Long firmId, Long nUserId, JsonObject userRole) {
        FirmUserRoleEntity firmUserRoleEntity = new FirmUserRoleEntity(userId, helper.getCurrentTime());
        if(userRole.get("n_firm_id") == null || userRole.get("n_firm_id").getAsString().equals("")){
            throw new InvalidRequestException("Select at least One Store");
        }
        Optional<FirmEntity> firm = firmRepo.findById(userRole.get("n_firm_id").getAsLong());
        if(firm.isEmpty()){
            throw new IllegalArgumentException("Invalid firm/branch Id");
        }
        if(userRole.get("c_view_trans_status").getAsString().equals("") || userRole.get("c_view_trans_status") == null
            && userRole.get("c_place_order_status").getAsString().equals("") || userRole.get("c_place_order_status") == null){
            throw new IllegalArgumentException("Please select at-least one access right to the user");
        }

        FirmUserRolePKEntity firmUserRolePKEntity = new FirmUserRolePKEntity(nUserId, userRole.get("n_firm_id").getAsLong());
        firmUserRoleEntity.setId(firmUserRolePKEntity);
        long id = userRole.get("n_firm_id").getAsLong();
        userRole.remove("n_firm_id");
        firmUserRoleEntity.setCFirmUserRole(userRole.toString());
        firmUserRoleRepo.save(firmUserRoleEntity);
        userRole.addProperty("n_firm_id", id);
    }

    @Override
    public void addRoleToUser(Long userId, Long firmId, Long createdUserId, UserModelBO model) {
        for (JsonElement userRole : model.getUserRoles()) {
            createFirmUserRole(userId, firmId, createdUserId, userRole.getAsJsonObject());
        }
    }

    @Override
    public void addRoleToUser(Long userId, Long branchId, Long createdUserId, BranchModelBO branchModelBO) {
        createFirmUserRoleFromBranch(userId, branchId, createdUserId, branchModelBO);
    }

    public void createFirmUserRoleFromBranch(Long userId, Long branchId, Long nUserId, BranchModelBO branchModelBO) {
        FirmUserRoleEntity firmUserRoleEntity = new FirmUserRoleEntity(userId, helper.getCurrentTime());
        FirmUserRolePKEntity firmUserRolePKEntity = new FirmUserRolePKEntity(nUserId, branchId);
        firmUserRoleEntity.setId(firmUserRolePKEntity);
        firmUserRoleEntity.setCFirmUserRole(branchModelBO.getUserRoles().toString());
        firmUserRoleRepo.save(firmUserRoleEntity);
    }

    @Override
    public void deleteById(FirmUserRolePKEntity firmUserRolePKEntity) {
        firmUserRoleRepo.deleteById(firmUserRolePKEntity);
    }

    @Override
    public List<FirmUserRoleEntity> getUserRoleByUserId(Long nUserId) {
        return firmUserRoleRepo.findByUserId(nUserId);
    }

    @Override
    public JsonArray getArrayUserRoleByUserId(Long uId) {
        List<FirmUserRoleEntity> list = getUserRoleByUserId(uId);
        JsonArray userRoleArray = new JsonArray();
        for (FirmUserRoleEntity firmUserRoleEntity : list) {
            JsonObject userRole = helper.fromJson(firmUserRoleEntity.getCFirmUserRole(), JsonObject.class);
            userRole.addProperty("n_firm_id", firmUserRoleEntity.getId().getNFirmId());
            userRoleArray.add(userRole);
        }
        return userRoleArray;
    }

    @Override
    public JsonObject getUserRolesOnId(Long uId, Long firmId) {
        List<FirmUserRoleEntity> list = getUserRoleByUserId(uId);
        JsonObject userRole = null;
        for (FirmUserRoleEntity firmUserRoleEntity : list) {
            userRole = helper.fromJson(firmUserRoleEntity.getCFirmUserRole(), JsonObject.class);
            userRole.addProperty("n_firm_id", firmUserRoleEntity.getId().getNFirmId());
        }
        return userRole;
    }

    @Override
    public FirmUserRoleEntity getFirmUserRole(Long userId, Long firmId) {
        return firmUserRoleRepo.getFirmUserRole(userId, firmId);
    }

    @Override
    public boolean checkFirmUserRoleExists(Long userId, Long branchId) {
        return firmUserRoleRepo.checkFirmUserRoleExists(userId, branchId) != null;
    }

    @Override
    public List<FirmUserRoleEntity> getUserRoleByFirmId(Long firmId) {
        return firmUserRoleRepo.findByFirmId(firmId);
    }

    @Override
    public List<FirmUserRoleEntity> getBranchesByUserId(Long uId) {
        return firmUserRoleRepo.findByUserId(uId);
    }
}
