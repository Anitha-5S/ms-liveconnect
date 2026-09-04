package com.c2.lc.ms.customer.bos;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BranchModelBO {

    @SerializedName("n_user_id")
    private Long nUserId;

    @SerializedName("n_branch_id")
    private Long nBranchId;

    @SerializedName("j_role")
    private JsonObject userRoles;
}
