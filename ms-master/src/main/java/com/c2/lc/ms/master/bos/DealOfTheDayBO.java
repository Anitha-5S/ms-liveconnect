package com.c2.lc.ms.master.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealOfTheDayBO implements Serializable {

    @SerializedName("n_deal_id")
    private Long dealId;

    @SerializedName("c_item_code")
    @NotEmpty(message = "c_item_code should not be empty!")
    private String itemCode;

    @SerializedName("c_item_name")
    @NotEmpty(message = "c_item_name should not be empty!")
    private String itemName;

    @SerializedName("n_discount_amount")
    private BigDecimal discAmount;

    @SerializedName("c_discount_type")
    private String discType;

    @SerializedName("n_discount_percentage")
    private BigDecimal discPercentage;

    @SerializedName("c_deals_status")
    private String status;

    @SerializedName("dt_start_datetime")
    private LocalDateTime startDateTime;

    @SerializedName("dt_end_datetime")
    private LocalDateTime endDateTime;

}