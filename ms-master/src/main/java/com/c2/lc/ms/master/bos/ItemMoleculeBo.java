package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemMoleculeBo {

    @SerializedName("c_molecule_code")
    @Field("c_molecule_code")
    private String moleculeCode;

    @SerializedName("c_molecule_name")
    @Field("c_molecule_name")
    private String moleculeName;

    @SerializedName("c_description")
    @Field("c_description")
    private String description;

    @SerializedName("c_usage")
    @Field("c_usage")
    private String usage ;

    @SerializedName("c_note")
    @Field("c_note")
    private String note ;

    @SerializedName("c_side_effect")
    @Field("c_side_effect")
    private String sideEfforts ;

    @SerializedName("c_contra_indications")
    @Field("c_contra_indications")
    private String contraIndications ;
}
