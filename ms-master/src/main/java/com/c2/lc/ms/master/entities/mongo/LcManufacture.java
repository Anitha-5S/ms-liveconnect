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
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Document("lc_manufacture")
public class LcManufacture {


    @Id
    @Size(min = 3, max = 8, message = "'c_manufacture_code' cannot exceed min = {min} , max = {max} characters!")
    @NotEmpty(message = "'c_manufacture_code' cannot be empty!")
    @Field("c_manufacture_code")
    @SerializedName("c_manufacture_code")
    private String manufactureCode;

    @Size(min = 2, max = 50, message = "'c_manufacture_name' cannot exceed min = {min} , max = {max} characters!")
    @NotEmpty(message = "'c_manufacture_name' cannot be empty!")
    @Field("c_manufacture_name")
    @SerializedName("c_manufacture_name")
    private String manufactureName;

    @SerializedName("ac_thumbnail_images")
    @Field("ac_thumbnail_images")
    private List<String> thumbnailImages;

    @SerializedName("ac_images")
    @Field("ac_images")
    private List<String> images;

    @Size(min = 10, max = 50, message = "'c_email' cannot exceed min = {min} , max = {max} characters!")
    @Field("c_email")
    @SerializedName("c_email")
    private String email;

    @Size(min = 8, max = 50, message = "'c_website_url' cannot exceed min = {min} , max = {max} characters!")
    @Field("c_website_url")
    @SerializedName("c_website_url")
    private String websiteUrl;

    @Size(min = 10, max = 15, message = "'c_phoneNumber' cannot exceed min = {min} , max = {max} characters!")
    @Field("c_phoneNumber")
    @SerializedName("c_phoneNumber")
    private String phoneNumber;

    @Size(min = 8, max = 100, message = "'c_address' cannot exceed min = {min} , max = {max} characters!")
    @Field("c_address1")
    @SerializedName("c_address1")
    private String address1;

    @Size(min = 8, max = 100, message = "'c_address' cannot exceed min = {min} , max = {max} characters!")
    @Field("c_address2")
    @SerializedName("c_address2")
    private String address2;

    @Size(min = 5, max = 10, message = "'c_pin' cannot exceed min = {min} , max = {max} characters!")
    @Field("c_pin")
    @SerializedName("c_pin")
    private String pin;

    @Size(min = 8, max = 100, message = "'c_address' cannot exceed min = {min} , max = {max} characters!")
    @Field("c_area_code")
    @SerializedName("c_area_code")
    private String areaCode;

    @Size(min = 8, max = 20, message = "'c_address' cannot exceed min = {min} , max = {max} characters!")
    @Field("c_trade_licence_no")
    @SerializedName("c_trade_licence_no")
    private String tradeLicence;

    @Size(min = 2, max = 50, message = "'c_contact_name' cannot exceed min = {min} , max = {max} characters!")
    @Field("c_contact_name")
    @SerializedName("c_contact_name")
    private String contactName;

    @SerializedName("d_created_date")
    @Field("d_created_date")
    private LocalDate createdDate;
}
