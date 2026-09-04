package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
public class MoleculeBo implements Serializable {

    @SerializedName("c_molecule_code")
    private String moleculeCode;

    @SerializedName("c_molecule_name")
    private String moleculeName;

}
