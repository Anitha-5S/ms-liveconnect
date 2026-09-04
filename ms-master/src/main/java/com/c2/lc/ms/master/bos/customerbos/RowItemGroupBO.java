package com.c2.lc.ms.master.bos.customerbos;

import com.c2.lc.lib.base.BaseBO;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RowItemGroupBO extends BaseBO implements Serializable {

    @SerializedName("c_code")
    @NotBlank(message = "'c_code' cannot be blank!")
    @NotEmpty(message = "'c_code' cannot be empty or null!")
    @Size(
            max = 6,
            message = "'c_code' cannot exceed {max} characters!"
    )
    private String cCode;

    @SerializedName("c_name")
    @NotEmpty(message = "'c_name' cannot be empty or null!")
    @NotBlank(message = "'c_name' cannot be blank!")
    @Size(
            max = 60,
            message = "'c_name' cannot exceed {max} characters!"
    )
    private String cName;

    @SerializedName("c_sh_name")
    @NotEmpty(message = "'c_sh_name' cannot be empty or null!")
    @NotBlank(message = "'c_sh_name' cannot be blank!")
    @Size(
            max = 6,
            message = "'c_sh_name' cannot exceed {max} characters!"
    )
    private String cShName;

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
    @Digits(integer = 1, fraction = 0, message ="'n_predefined' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigDecimal nPredefined;

    @SerializedName("n_pur_exp_days")
    @Digits(integer = 3, fraction = 0, message ="'n_pur_exp_days' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigDecimal nPurExpDays;

    @SerializedName("n_sale_exp_days")
    @Digits(integer = 3, fraction = 0, message ="'n_sale_exp_days' size within has to be in the range integer : {integer}, fraction : {fraction}")


    private BigDecimal nSaleExpDays;

    @SerializedName("t_ltime")
    private Timestamp tLtime;

    @SerializedName("n_gdn_exp_days")
    @Digits(integer = 3, fraction = 0, message ="'n_gdn_exp_days' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigDecimal nGdnExpDays;

    @SerializedName("c_modiuser")
    @Size(
            max = 10,
            message = "'c_modiuser' cannot exceed {max} characters!"
    )
    private String cModiuser;
}
