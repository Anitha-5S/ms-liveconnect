package com.c2.lc.ms.master.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("category")
public class Category {

    @Expose(serialize = false, deserialize = false)
    private String id;

    @SerializedName("c_code")
    @NotEmpty(message = "Category code cannot be empty!")
    @Field("c_category_code")
    private String categoryCode;

    @SerializedName("c_name")
    @NotEmpty(message = "Category name cannot be empty!")
    @Field("c_category_name")
    private String categoryName;

    @SerializedName("c_sh_name")
    @Field("c_sh_name")
    private String shName;

    @Field("c_thumbnail_image")
    @SerializedName("c_thumbnail_image")
    private String thumbnailImage;

    @Field("c_created_at")
    @SerializedName("c_created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Field("c_created_by")
    @SerializedName("c_created_by")
    private String createdBy;
}
