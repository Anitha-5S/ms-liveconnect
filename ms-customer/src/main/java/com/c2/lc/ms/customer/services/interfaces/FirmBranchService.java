package com.c2.lc.ms.customer.services.interfaces;


import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.bos.BranchDetailsBO;
import com.c2.lc.ms.customer.bos.BranchListBo;
import com.c2.lc.ms.customer.bos.ListBranchModelBO;
import com.c2.lc.ms.customer.entities.comm.EcoUsers;
import com.c2.lc.ms.customer.services.interfaces.base.LcBaseService;
import com.google.gson.JsonArray;

import java.util.List;

public interface FirmBranchService extends LcBaseService {

    List<ListBranchModelBO> getBranchListDetails(Long firmId);

    JsonArray searchBranchList(Long firmId, String searchString);

    BranchDetailsBO getBranchDetails(Long branchId) throws RecordNotFoundException;

    List<BranchListBo> getListOfBranch(LcHeaderBO headerBO, PageBO pageBO);

    EcoUsers ecoUserUpdate(LcHeaderBO headerBO, String newBranch) throws RecordNotFoundException;
}
