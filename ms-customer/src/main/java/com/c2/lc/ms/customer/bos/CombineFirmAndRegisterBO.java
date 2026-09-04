package com.c2.lc.ms.customer.bos;

import com.c2.lc.ms.customer.entities.customer.FirmEntity;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CombineFirmAndRegisterBO {

    @SerializedName("firmEntity")
    private FirmEntity firmEntity;

    @SerializedName("j_register")
    private JsonObject regObj;
}
