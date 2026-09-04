package com.c2.lc.ms.master.entities.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("lc_supplier")
public class LcSupplier {

    @Id
    @Size(min = 3, max = 8, message = "'c_supplier_code' cannot exceed min = {min} , max = {max} characters!")
    @NotEmpty(message = "'c_supplier_code' cannot be empty!")
    @Field("c_supplier_code")
    @SerializedName("c_supplier_code")
    private String supplierCode;

    @Size(min = 2, max = 50, message = "'c_supplier_name' cannot exceed min = {min} , max = {max} characters!")
    @NotEmpty(message = "'c_supplier_name' cannot be empty!")
    @Field("c_supplier_name")
    @SerializedName("c_supplier_name")
    private String supplierName;

    @SerializedName("ac_thumbnail_images")
    @Field("ac_thumbnail_images")
    private List<String> thumbnailImages;

    @SerializedName("ac_images")
    @Field("ac_images")
    private List<String> images;

    @Field("c_email")
    @SerializedName("c_email")
    private String email;

    @Field("c_website_url")
    @SerializedName("c_website_url")
    private String websiteUrl;

    @Field("c_phoneNumber")
    @SerializedName("c_phoneNumber")
    private String phoneNumber;

    @Field("c_address")
    @SerializedName("c_address")
    private String address;

    @Field("c_contact_name")
    @SerializedName("c_contact_name")
    private String contactName;
}
