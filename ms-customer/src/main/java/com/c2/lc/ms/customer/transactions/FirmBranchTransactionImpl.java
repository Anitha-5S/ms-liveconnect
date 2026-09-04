package com.c2.lc.ms.customer.transactions;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.bos.*;
import com.c2.lc.ms.customer.entities.comm.EcoUsers;
import com.c2.lc.ms.customer.entities.customer.FirmEntity;
import com.c2.lc.ms.customer.entities.customer.FirmUserEntity;
import com.c2.lc.ms.customer.entities.customer.FirmUserRoleEntity;
import com.c2.lc.ms.customer.entities.customer.UserOwnerEntity;
import com.c2.lc.ms.customer.services.interfaces.*;
import com.c2.lc.ms.customer.transactions.base.LcBaseTransactionImpl;
import com.c2.lc.ms.customer.transactions.interfaces.FirmBranchTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class FirmBranchTransactionImpl extends LcBaseTransactionImpl implements FirmBranchTransaction {

    @Autowired
    private FirmUserRoleService firmUserRoleService;
    @Autowired
    private FirmDefaultService firmDefaultService;
    @Autowired
    private FirmBranchService firmBranchService;
    @Autowired
    private FirmUserService firmUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private FirmService firmService;
    @Autowired
    private LcUserService lcUserService;

    @Override
    public Long getDefaultFirmId(Long userId) throws RecordNotFoundException {
        return firmDefaultService.getDefaultFirm(userId).getNFirmId();
    }

    @Override
    public void setDefaultBranch(Long userId, Long branchCode) throws RecordNotFoundException {
        firmDefaultService.setDefaultFirm(userId, branchCode);
    }

    @Override
    public List<ListBranchModelBO> getBranchList(Long firmId) {
        return firmBranchService.getBranchListDetails(firmId);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void addBranch(LcHeaderBO header, BranchDetailsBO branch) throws RecordNotFoundException, DuplicateRecordException, InvalidRequestException {
        FirmEntity firm = new FirmEntity(header.getUserId(), helper.getCurrentTime());
        firm.setCStatus(Constants.STATUS_ACTIVE);
        firm = firmService.saveFirmBranch(header, branch);
        firmUserService.addFirmToUser(header.getUserId(), firm.getNFirmId(), Constants.STATUS_ACTIVE);

        UserOwnerEntity userOwnerEntity = userService.getParentUser(header.getUserId());
        if (userOwnerEntity != null) {
            firmUserService.addFirmToUser(userOwnerEntity.getParentUser(), firm.getNFirmId(), Constants.STATUS_ACTIVE);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deleteBranch(LcHeaderBO header, Long branchId) throws RecordNotFoundException {
        firmService.makeFirmInactive(branchId);
        List<FirmUserEntity> userList = firmUserService.listUsers(branchId);
        if (!userList.isEmpty()) {
            for (FirmUserEntity user : userList) {
                firmUserService.deleteFirmUser(branchId, user.getUserDetail().getNUserId());
                if (!Objects.equals(user.getUserDetail().getNUserId(), header.getUserId())) {
                    lcUserService.deleteBranchUser(user.getUserDetail().getNUserId(), branchId, user.getFirm().getC2Code());
                }
            }
        }
        List<FirmUserRoleEntity> firmUserRoleEntity = firmUserRoleService.getUserRoleByFirmId(branchId);
        for (FirmUserRoleEntity userRole : firmUserRoleEntity) {
            firmUserRoleService.deleteById(userRole.getId());
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateBranch(LcHeaderBO header, Long branchId, BranchDetailsBO branch) throws RecordNotFoundException, InvalidRequestException {
        firmService.updateBranch(header, branchId, branch);
    }

    @Override
    public FirmEntity getFirmById(Long firmId) throws RecordNotFoundException {
        return firmService.getFirmById(firmId);
    }

    @Override
    public JsonArray searchBranchList(Long firmId, String searchString) throws RecordNotFoundException {
        if (!searchString.matches("^[a-zA-Z0-9]+$")) {
            throw new RecordNotFoundException("Please make a valid search " + searchString);
        }
        return firmBranchService.searchBranchList(firmId, searchString);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void addUsersToBranch(Long userId, Long firmId, JsonArray data) {

        for (JsonElement jsonObject : data) {
            BranchModelBO branchModelBO = helper.fromJson(jsonObject.getAsJsonObject(), BranchModelBO.class);
            List<FirmUserRoleEntity> list = firmUserRoleService.getUserRoleByFirmId(branchModelBO.getNBranchId());
            for (FirmUserRoleEntity firmUserRoleEntity : list) {
                firmUserRoleService.deleteById(firmUserRoleEntity.getId());
            }
        }
        assignUserRolesToFirm(userId, data);
    }

    private void assignUserRolesToFirm(Long userId, JsonArray data) {
        for (JsonElement jsonObject : data) {
            BranchModelBO branchModelBO = helper.fromJson(jsonObject.getAsJsonObject(), BranchModelBO.class);
            firmUserRoleService.addRoleToUser(userId, branchModelBO.getNBranchId(), branchModelBO.getNUserId(), branchModelBO);
        }
    }

    @Override
    public List<ListUserRoleModelBO> getListUsersRole(Long userId, Long firmId, Long branchId) {

        List<ListUserRoleModelBO> listUserRoleModelBOS = new ArrayList<>();
        List<ListUserModelBO> userDetailsByFirmId = userService.getUserDetailsByFirmId(userId, firmId);
        for (ListUserModelBO list : userDetailsByFirmId) {
            ListUserRoleModelBO model = new ListUserRoleModelBO();
            if (firmUserRoleService.checkFirmUserRoleExists(list.getUserId(), branchId)) {
                FirmUserRoleEntity firmUserRoleEntity = firmUserRoleService.getFirmUserRole(list.getUserId(), branchId);
                model.setUserId(list.getUserId());
                model.setCName(list.getUserName());
                JsonObject jsonObject = helper.fromJson(firmUserRoleEntity.getCFirmUserRole(), JsonObject.class);
                model.setUserRoles(jsonObject);
                listUserRoleModelBOS.add(model);
            }
        }
        return listUserRoleModelBOS;
    }

    @Override
    public BranchDetailsBO getBranchDetails(Long branchId) throws RecordNotFoundException {
        return firmBranchService.getBranchDetails(branchId);
    }

    @Override
    public List<BranchListBo> getListOfBranch(LcHeaderBO headerBO, PageBO pageBO) {
        return firmBranchService.getListOfBranch(headerBO, pageBO);
    }

    @Override
    public int getCount(Long userId) {
        return firmUserService.getBranchCount(userId);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public EcoUsers ecoUserUpdate(LcHeaderBO headerBO, String newBranch) throws RecordNotFoundException {
        return firmBranchService.ecoUserUpdate(headerBO, newBranch);
    }

    @Override
    public Boolean isBranchExist(Long userId, Long branchCode) throws RecordNotFoundException {
        return firmDefaultService.isBranchExist(userId, branchCode);
    }

    @Override
    public JsonObject callC2Service(String c2Code, Long userId, String cBrCode, String role, String deviceToken) throws CommunicationErrorException, InvalidRequestException {
        JsonObject request = new JsonObject();
        request.addProperty("c_c2code", c2Code);
        request.addProperty("c_br_code", cBrCode);
        request.addProperty("c_terminal_id", userId);
        request.addProperty("c_type", role);
        request.addProperty("c_device_token", deviceToken);
        return firmDefaultService.callC2Service(request);
    }
}
