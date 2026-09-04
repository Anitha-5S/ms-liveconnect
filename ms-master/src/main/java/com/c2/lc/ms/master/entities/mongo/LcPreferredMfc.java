package com.c2.lc.ms.master.entities.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Document("preferred_mfac_statewise")
public class LcPreferredMfc {

    @Id
    private String id;

    @Size(min = 3, max = 8, message = "'c_mfac_code' cannot exceed min = {min} , max = {max} characters!")
    @NotEmpty(message = "'c_mfac_code' cannot be empty!")
    @Field("c_mfac_code")
    @SerializedName("c_mfac_code")
    @Indexed
    private String manufactureCode;

    @Size(min = 2, max = 50, message = "'c_mfac_name' cannot exceed min = {min} , max = {max} characters!")
    @NotEmpty(message = "'c_mfac_name' cannot be empty!")
    @Field("c_mfac_name")
    @SerializedName("c_mfac_name")
    @Indexed
    private String manufactureName;

    @SerializedName("c_web_img_link")
    @Field("c_web_img_link")
    private String imageUrl;

    @SerializedName("c_state_code")
    @Field("c_state_code")
    @Indexed
    private String stateCode;

    @SerializedName("c_state_name")
    @Field("c_state_name")
    private String stateName;

    @SerializedName("n_count")
    @Field("n_count")
    private String priorityCount;

    @SerializedName("c_mfac_group_code")
    @Field("c_mfac_group_code")
    private String mfacGroupCode;

    @SerializedName("c_mfac_group_name")
    @Field("c_mfac_group_name")
    private String mfacGroupName;

}
