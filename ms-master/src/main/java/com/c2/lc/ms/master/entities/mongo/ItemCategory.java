package com.c2.lc.ms.master.entities.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("lc_item_category")
public class ItemCategory {

    @Id
    @Size(min = 3, max = 20, message = "'c_item_category_code' cannot exceed min = {min} , max = {max} characters!")
    @NotEmpty(message = "'c_item_category_code' cannot be empty!")
    @Field("c_item_category_code")
    @SerializedName("c_item_category_code")
    private String categoryCode;

    @Size(min = 2, max = 100, message = "'c_item_category_name' cannot exceed min = {min} , max = {max} characters!")
    @NotEmpty(message = "'c_item_category_name' cannot be empty!")
    @Field("c_item_category_name")
    @SerializedName("c_item_category_name")
    private String categoryName;

}
