package com.c2.lc.ms.master.models;

import com.c2.lc.ms.master.utils.MsMessages;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("advertisement")
public class Advertisement {

    @Id
    private String id;

    @NotEmpty(message = MsMessages.VALIDATE_IMAGE_DATA)
    @Field("c_file_data")
    @SerializedName("c_file_data")
    private String fileData;

    @Field("c_image_type")
    @SerializedName("c_image_type")
    private String imageType;

    @Field("c_type")
    @SerializedName("c_type")
    private String type;

    @Field("c_file_name")
    @SerializedName("c_file_name")
    private String fileName;

    @Field("c_banner_image_url")
    @SerializedName("c_banner_image_url")
    private String awsUrl;

    @Field("c_redirect_url")
    @SerializedName("c_redirect_url")
    private String redirectUrl;

    @Field("c_state")
    @SerializedName("c_state")
    private String state;

    @Field("c_city")
    @SerializedName("c_city")
    private String city;

    @Field("c_created_at")
    @SerializedName("c_created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Field("c_created_by")
    @SerializedName("c_created_by")
    private String createdBy;

}
