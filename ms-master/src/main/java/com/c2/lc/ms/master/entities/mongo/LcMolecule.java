package com.c2.lc.ms.master.entities.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LcMolecule {

    @Indexed
    @SerializedName("c_molecule_code")
    @Field("c_molecule_code")
    private String moleculeCode;

    @Indexed
    @SerializedName("c_molecule_name")
    @Field("c_molecule_name")
    private String moleculeName;

    @SerializedName("c_drug_name")
    @Field("c_drug_name")
    private String drugName ;

    @SerializedName("c_therapeutic_class")
    @Field("c_therapeutic_class")
    private String therapeuticClass ;

    @SerializedName("c_available_doses")
    @Field("c_available_doses")
    private String availableDoses ;

    @SerializedName("c_indications")
    @Field("c_indications")
    private String indications ;

    @SerializedName("c_contraindications")
    @Field("c_contraindications")
    private String contraIndications ;

    @SerializedName("c_schedule")
    @Field("c_schedule")
    private String schedule ;

    @SerializedName("c_dosage_forms")
    @Field("c_dosage_forms")
    private String dosageForms ;

    @SerializedName("c_antidote")
    @Field("c_antidote")
    private String antidote;

    @SerializedName("c_pregnancy_category")
    @Field("c_pregnancy_category")
    private String pregnancyCategory ;

    @SerializedName("c_references")
    @Field("c_references")
    private String references ;



}
