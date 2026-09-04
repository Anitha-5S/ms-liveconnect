package com.c2.lc.ms.master.entities.mongo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document("lc_notification")
@NoArgsConstructor
@AllArgsConstructor
public class LcNotification {

    @Id
    @SerializedName("c_notification_id")
    @Field("c_notification_id")
    private String notificationId;

    @Expose(serialize = false)
    @SerializedName("n_user_id")
    @Field("n_user_id")
    @Indexed
    private long userId;

    @Expose(serialize = false)
    @SerializedName("n_firm_id")
    @Field("n_firm_id")
    @Indexed
    private long firmId;

    @Expose(serialize = false)
    @SerializedName("n_branch_id")
    @Field("n_branch_id")
    @Indexed
    private long brCode;

    @SerializedName("c_message")
    @Field("c_message")
    private String message;

    @SerializedName("c_title")
    @Field("c_title")
    private String title;

    @SerializedName("c_status")
    @Field("c_status")
    @Indexed
    private String status;

    @SerializedName("c_notify_type")
    @Field("c_notify_type")
    private String notifyType;

    @SerializedName("c_price")
    @Field("c_price")
    private String price;

    @SerializedName("c_discount_percentage")
    @Field("c_discount_percentage")
    private String discountPercentage;

    @SerializedName("c_scheme_type")
    @Field("c_scheme_type")
    private String schemeType;

    @SerializedName("c_product_name")
    @Field("c_product_name")
    private String productName;

    @SerializedName("dt_date_time")
    @Field("t_created_timestamp")
    private LocalDateTime createdTimeStamp;

    @Expose(serialize = false)
    @SerializedName("t_read_timestamp")
    @Field("t_read_timestamp")
    private LocalDateTime readTimeStamp;

    @Expose(serialize = false)
    @SerializedName("t_deleted_timestamp")
    @Field("t_deleted_timestamp")
    private LocalDateTime deletedTimeStamp;

}
