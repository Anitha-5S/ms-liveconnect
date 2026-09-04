package com.c2.lc.ms.master.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class MasterModel  implements Serializable{


    private static final long serialVersionUID = -293984592589824189L;
    @SerializedName("c_code")
    private String cCode;

    @SerializedName("c_name")
    private String cName;

    @SerializedName("c_sh_name")
    private String cShName;

}
