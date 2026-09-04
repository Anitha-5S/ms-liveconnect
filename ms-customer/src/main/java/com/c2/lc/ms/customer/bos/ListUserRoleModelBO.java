package com.c2.lc.ms.customer.bos;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListUserRoleModelBO {

    @SerializedName("n_user_id")
    private Long userId;

    @SerializedName("c_name")
    private String cName;

    @SerializedName("j_role")
    private JsonObject userRoles;

}
