package com.c2.lc.ms.master.bos.customerbos;

import com.c2.lc.lib.base.BaseBO;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RowPackTypeBO extends BaseBO implements Serializable {

    @SerializedName("c_code")
    @Size(
            max = 6,
            message = "'c_code' cannot exceed {max} characters!"
    )
    @NotBlank(message = "'c_code' cannot be blank!")
    @NotEmpty(message = "'c_code' cannot be empty or null!")
    private String cCode;

    @SerializedName("c_name")
    @Size(
            max = 60,
            message = "'c_name' cannot exceed {max} characters!"
    )
    @NotBlank(message = "'c_name' cannot be blank!")
    @NotEmpty(message = "'c_name' cannot be empty or null!")
    private String cName;

    @SerializedName("d_ldate")
    private String dLdate;

    @SerializedName("d_adate")
    @NotNull(message = "'d_adate' cannot be empty or null!")
    private String dAdate;

    @SerializedName("c_createuser")
    @Size(
            max = 10,
            message = "'c_createuser' cannot exceed {max} characters!"
    )
    @NotBlank(message = "'c_createuser' cannot be blank!")
    @NotEmpty(message = "'c_createuser' cannot be empty or null!")
    private String cCreateUser;

    @SerializedName("n_audited")
    @NotNull(message = "'n_audited' cannot be null!")
    @Digits(integer = 1, fraction = 0, message ="'n_audited' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigInteger nAudited;

    @SerializedName("n_predefined")
    @Digits(integer = 1, fraction = 0, message ="'n_predefined' size within has to be in the range integer : {integer}, fraction : {fraction}")
    @NotNull(message = "'n_predefined' cannot be null!")

    private BigInteger nPredefined;

    @SerializedName("c_sh_name")
    @Size(
            max = 6,
            message = "'c_sh_name' cannot exceed {max} characters!"
    )
    private String cShName;

    @SerializedName("t_ltime")
    private Timestamp tLtime;

    @SerializedName("c_modiuser")
    @Size(
            max = 10,
            message = "'c_modiuser' cannot exceed {max} characters!"
    )
    private String cModiuser;
}
