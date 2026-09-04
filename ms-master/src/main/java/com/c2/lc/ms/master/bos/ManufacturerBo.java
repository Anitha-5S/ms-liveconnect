package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
public class ManufacturerBo implements Serializable {

    @SerializedName("c_mfg_code")
    private String mfgCode;

    @SerializedName("c_mfg_name")
    private String mfcName;

}
