package com.c2.lc.ms.master.bos;


import com.c2.lc.lib.base.BaseBO;
import com.c2.lc.ms.master.utils.MsMessages;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStockiestItemBO extends BaseBO implements Serializable {

    @Size(message = MsMessages.VALIDATE_CCODE_LENGTH, max = 100)
    @NotEmpty(message = "'c_code' cannot be empty!")
    @SerializedName("c_code")
    private String code;

    @Size(message = MsMessages.VALIDATE_C2CODE_LENGTH, max = 20)
    @NotEmpty(message = "'c_c2code' cannot be empty!")
    @SerializedName("c_c2code")
    private String c2code;

    @Size(message = MsMessages.VALIDATE_BRCODE_LENGTH, max = 100)
    @NotEmpty(message = "'c_br_code' cannot be empty!")
    @SerializedName("c_br_code")
    private String brCode;

    @Size(message = MsMessages.VALIDATE_NAME_LENGTH, max = 250)
    @NotEmpty(message = "'c_name' cannot be empty!")
    @SerializedName("c_name")
    private String name;

    @NotNull(message = "'n_qty_per_box' cannot be null!")
    @Digits(message = MsMessages.VALIDATE_QUANTITY_LENGTH, integer = 4, fraction = 0)
    @SerializedName("n_qty_per_box")
    private BigDecimal qtyPerBox;

    @Size(message = MsMessages.VALIDATE_CNOTE_LENGTH, max = 200)
    @NotEmpty(message = "'c_note' cannot be empty!")
    @SerializedName("c_note")
    private String note;

    @NotEmpty(message = "'d_adate' cannot be empty!")
    @SerializedName("d_adate")
    private String adate;

    @Size(message = MsMessages.VALIDATE_CSCHEDULCODE_LENGTH, max = 50)
    @NotEmpty(message = "'c_schedule_code' cannot be empty!")
    @SerializedName("c_schedule_code")
    private String scheduleCode;

    @Size(message = MsMessages.VALIDATE_NAME_LENGTH, max = 100)
    @NotEmpty(message = "'c_full_name' cannot be empty!")
    @SerializedName("c_full_name")
    private String fullName;

    @Size(message = MsMessages.VALIDATE_BRANDNAME_LENGTH, max = 100)
    @SerializedName("c_brand_name")
    private String brandName;

    @Size(message = MsMessages.VALIDATE_WEBIMAGE_LENGTH, max = 500)
    @SerializedName("c_web_img_link")
    private String webImgLink;

    @Size(message = MsMessages.VALIDATE_CUOM_LENGTH, max = 50)
    @SerializedName("c_uom")
    private String uom;

    @Digits(message = MsMessages.VALIDATE_ITEM_LENGTH, integer = 5, fraction = 4)
    @SerializedName("n_item_length")
    private BigDecimal itemLength;

    @Digits(message = MsMessages.VALIDATE_ITEM_WIDTH, integer = 5, fraction = 4)
    @SerializedName("n_item_width")
    private BigDecimal itemWidth;

    @Digits(message = MsMessages.VALIDATE_ITEM_HEIGHT, integer = 5, fraction = 4)
    @SerializedName("n_item_height")
    private BigDecimal itemHeight;

    @Digits(message = MsMessages.VALIDATE_ITEM_WEIGHT, integer = 5, fraction = 4)
    @SerializedName("n_item_weight")
    private BigDecimal itemWeight;

    @Digits(message = MsMessages.VALIDATE_INNER_LENGTH, integer = 5, fraction = 4)
    @SerializedName("n_inner_length")
    private BigDecimal innerLength;

    @Digits(message = MsMessages.VALIDATE_INNER_WIDTH, integer = 5, fraction = 4)
    @SerializedName("n_inner_width")
    private BigDecimal innerWidth;

    @Digits(message = MsMessages.VALIDATE_INNER_HEIGHT, integer = 5, fraction = 4)
    @SerializedName("n_inner_height")
    private BigDecimal innerHeight;

    @Digits(message = MsMessages.VALIDATE_INNER_WEIGHT, integer = 5, fraction = 4)
    @SerializedName("n_inter_weight")
    private BigDecimal interWeight;

    @Digits(message = MsMessages.VALIDATE_OUTER_LENGTH, integer = 5, fraction = 4)
    @SerializedName("n_outer_length")
    private BigDecimal outerLength;

    @Digits(message = MsMessages.VALIDATE_OUTER_WIDTH, integer = 5, fraction = 4)
    @SerializedName("n_outer_width")
    private BigDecimal outerWidth;

    @Digits(message = MsMessages.VALIDATE_OUTER_HEIGHT, integer = 5, fraction = 4)
    @SerializedName("n_outer_height")
    private BigDecimal outerHeight;

    @Digits(message = MsMessages.VALIDATE_OUTER_WEIGHT, integer = 5, fraction = 4)
    @SerializedName("n_outer_weight")
    private BigDecimal outerWeight;

    @Size(message = MsMessages.VALIDATE_BARCODELABLE_LENGTH, max = 100)
    @SerializedName("c_barcode_label")
    private String barcodeLabel;

    @Digits(message = MsMessages.VALIDATE_MRP_LIMIT, integer = 8, fraction = 3)
    @SerializedName("n_mrp")
    private BigDecimal mrp;

    @Digits(message = MsMessages.VALIDATE_SALERATE_LIMIT, integer = 8, fraction = 3)
    @SerializedName("n_sale_rate")
    private BigDecimal saleRate;


}
