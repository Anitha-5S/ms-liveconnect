package com.c2.lc.ms.master.entities.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("lc_molecule")
public class Molecule {

    @Id
    @SerializedName("c_molecule_code")
    @Field("c_molecule_code")
    private String moleculeCode;

    @Indexed
    @SerializedName("c_drug_name")
    @Field("c_drug_name")
    private String moleculeName;

}
