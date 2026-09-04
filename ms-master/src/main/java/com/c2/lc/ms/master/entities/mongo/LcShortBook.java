package com.c2.lc.ms.master.entities.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Document("lc_shortbook")
@NoArgsConstructor
@AllArgsConstructor
public class LcShortBook {

    @Id
    private String id;

    @SerializedName("c_item_code")
    @Field("c_item_code")
    @Size(min = 3, max = 10, message = "'c_item_code' cannot exceed min = {min} , max = {max} characters!")
    @NotEmpty(message = "'c_item_code' cannot be empty!")
    private String itemCode;

    @Indexed
    @SerializedName("c_br_code")
    @Field("c_br_code")
    @Size(max = 10)
    private String brCode ;

    @Indexed
    @SerializedName("l_user_id")
    @Field("l_user_id")
    private Long userId;

    @Indexed
    @SerializedName("l_firm_id")
    @Field("l_firm_id")
    private Long firmId;

}
