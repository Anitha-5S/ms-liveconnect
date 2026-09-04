package com.c2.lc.ms.master.bos.customerbos;


import com.c2.lc.lib.base.BaseBO;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RowItemCategoryBO extends BaseBO implements Serializable {

    @SerializedName("c_code")
    @NotEmpty(message = "'c_code' cannot be empty or null!")
    @NotBlank(message = "'c_code' cannot be blank!")
    @Size(
            max = 20,
            message = "'c_code' cannot exceed {max} characters!"
    )
    private String cCode;

    @SerializedName("c_name")
    @NotEmpty(message = "'c_name' cannot be empty or null!")
    @NotBlank(message = "'c_name' cannot be blank!")
    @Size(
            max = 100,
            message = "'c_name' cannot exceed {max} characters!"
    )
    private String cName;

    @SerializedName("c_sh_name")
    @NotEmpty(message = "'c_sh_name' cannot be empty or null!")
    @NotBlank(message = "'c_sh_name' cannot be blank!")
    @Size(
            max = 10,
            message = "'c_sh_name' cannot exceed {max} characters!"
    )
    private String cShName;

    @SerializedName("n_rst")
    @Digits(integer = 2, fraction = 2, message ="'n_rst' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigDecimal nRst;

    @SerializedName("d_ldate")
    @Pattern(regexp="^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$",message="d_ldate must be in yyyy-mm-dd format")
    private String dLdate;

    @SerializedName("d_adate")
    @Pattern(regexp="^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$",message="d_adate must be in yyyy-mm-dd format")
    private String dAdate;

    @SerializedName("c_createuser")
    @NotEmpty(message = "'c_createuser' cannot be empty or null!")
    @Size(
            max = 10,
            message = "'c_createuser' cannot exceed {max} characters!"
    )
    private String cCreateuser;

    @SerializedName("n_audited")
    @NotNull(message = "'n_audited' cannot be null!")
    @Digits(integer = 1, fraction = 0, message ="'n_audited' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigDecimal nAudited;

    @SerializedName("n_predefined")
    @NotNull(message = "'n_predefined' cannot be null!")
    @Digits(integer = 1, fraction = 0, message ="'n_predefined' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigDecimal nPredefined;

    @SerializedName("n_discount")
    @NotNull(message = "'n_discount' cannot be empty or null!")
    @Digits(integer = 1, fraction = 0, message ="'n_discount' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigDecimal nDiscount;

    @SerializedName("n_points")
    @Digits(integer = 3, fraction = 2, message ="'n_points' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigDecimal nPoints;

    @SerializedName("t_ltime")
    private Timestamp tLtime;

    @SerializedName("c_item_category_head_code")
    @Size(
            max = 6,
            message = "'c_item_category_head_code' cannot exceed {max} characters!"
    )
    private String cItemCategoryHeadCode;

    @SerializedName("n_age_per")
    @Digits(integer = 2, fraction = 0, message ="'n_age_per' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigDecimal nAgePer;

    @SerializedName("c_modiuser")
    @Size(
            max = 10,
            message = "'c_modiuser' cannot exceed {max} characters!"
    )
    private String cModiuser;

    @SerializedName("c_image_url")
    @Size(
            max = 300,
            message = "'c_image_url' cannot exceed {max} characters!"
    )
    private String cImageUrl;

    @SerializedName("n_salable_online")
    @Digits(integer = 11, fraction = 0, message ="'n_salable_online' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigInteger nSalableOnline;

    @SerializedName("n_display_online")
    @Digits(integer = 11, fraction = 0, message ="'n_display_online' size within has to be in the range integer : {integer}, fraction : {fraction}")
    @NotNull(message = "'n_display_online' cannot be null!")
    private BigInteger nDisplayOnline;

    @SerializedName("n_active")
    @Digits(integer = 1, fraction = 0, message ="'n_active' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigDecimal nActive;


}
