package com.c2.lc.ms.customer.transactions.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.*;
import com.c2.lc.ms.customer.bos.BranchDetailsBO;
import com.c2.lc.ms.customer.bos.BranchListBo;
import com.c2.lc.ms.customer.bos.ListBranchModelBO;
import com.c2.lc.ms.customer.bos.ListUserRoleModelBO;
import com.c2.lc.ms.customer.entities.comm.EcoUsers;
import com.c2.lc.ms.customer.entities.customer.FirmEntity;
import com.c2.lc.ms.customer.transactions.base.LcBaseTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public interface FirmBranchTransaction extends LcBaseTransaction {

    Long getDefaultFirmId(Long userId) throws RecordNotFoundException;

    void setDefaultBranch(Long userId, Long branchCode) throws RecordNotFoundException;

    List<ListBranchModelBO> getBranchList(Long firmId);

    void addBranch(LcHeaderBO header, BranchDetailsBO branch) throws RecordNotFoundException, DuplicateRecordException, InvalidRequestException;

    void deleteBranch(LcHeaderBO header, Long branchId) throws WarningException;

    void updateBranch(LcHeaderBO header, Long branchId, BranchDetailsBO firmEntity) throws RecordNotFoundException, InvalidRequestException;

    FirmEntity getFirmById(Long firmId) throws RecordNotFoundException;

    JsonArray searchBranchList(Long firmId, String searchString) throws NoSuchFieldException, RecordNotFoundException;

    void addUsersToBranch(Long userId, Long firmId, JsonArray branchModel);

    List<ListUserRoleModelBO> getListUsersRole(Long userId, Long firmId, Long branchId) throws RecordNotFoundException;

    BranchDetailsBO getBranchDetails(Long branchId) throws RecordNotFoundException;

    List<BranchListBo> getListOfBranch(LcHeaderBO headerBO, PageBO pageBO);

    int getCount(Long userId);

    EcoUsers ecoUserUpdate(LcHeaderBO headerBO, String newBranch) throws RecordNotFoundException;

    Boolean isBranchExist(Long userId, Long branchCode) throws RecordNotFoundException;

    JsonObject callC2Service(String c2Code, Long userId, String cBrCode, String role, String deviceToken) throws CommunicationErrorException, InvalidRequestException;
}
