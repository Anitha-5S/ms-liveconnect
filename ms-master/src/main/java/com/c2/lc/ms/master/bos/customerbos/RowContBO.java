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
public class RowContBO extends BaseBO implements Serializable {

    @SerializedName("c_code")
    @NotEmpty(message = "'c_code' cannot be empty or null!")
    @NotBlank(message = "'c_code' cannot be blank!")
    @Size(
            max = 6,
            message = "'c_code' cannot exceed {max} characters!"
    )
    private String cCode;

    @SerializedName("c_name")
    @Size(
            max = 1000,
            message = "'c_name' cannot exceed {max} characters!"
    )
    @NotBlank(message = "'c_name' cannot be blank!")
    @NotEmpty(message = "'c_name' cannot be empty or null!")
    private String cName;

    @SerializedName("c_sh_name")
    @Size(
            max = 6,
            message = "'c_sh_name' cannot exceed {max} characters!"
    )
    @NotBlank(message = "'c_sh_name' cannot be blank!")
    @NotEmpty(message = "'c_sh_name' cannot be empty or null!")
    private String cShName;

    @SerializedName("c_note")
    @Size(
            max = 100,
            message = "'c_note' cannot exceed {max} characters!"
    )
    private String cNote;

    @SerializedName("c_note1")
    @Size(
            max = 100,
            message = "'c_note1' cannot exceed {max} characters!"
    )
    private String cNote1;

    @SerializedName("c_note2")
    @Size(
            max = 100,
            message = "'c_note2' cannot exceed {max} characters!"
    )
    private String cNote2;

    @SerializedName("c_note3")
    @Size(
            max = 100,
            message = "'c_note3' cannot exceed {max} characters!"
    )
    private String cNote3;

    @SerializedName("c_note4")
    @Size(
            max = 100,
            message = "'c_note4' cannot exceed {max} characters!"
    )
    private String cNote4;

    @SerializedName("d_ldate")
    @Pattern(regexp="^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$",message="d_ldate must be in yyyy-mm-dd format")
    private String dLdate;

    @SerializedName("d_adate")
    @Pattern(regexp="^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$",message="d_adate must be in yyyy-mm-dd format")
    private String dAdate;

    @SerializedName("c_createuser")
    @Size(
            max = 10,
            message = "'c_createuser' cannot exceed {max} characters!"
    )
    @NotEmpty(message = "'c_createuser' cannot be empty or null!")
    private String cCreateuser;

    @SerializedName("n_audited")
    @NotNull(message = "'n_audited' cannot be null!")
    @Digits(integer = 1, fraction = 0, message ="'n_audited' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigDecimal nAudited;

    @SerializedName("n_predefined")
    @NotNull(message = "'n_predefined' cannot be null!")
    @Digits(integer = 1, fraction = 0, message ="'n_predefined' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigDecimal nPredefined;

    @SerializedName("t_ltime")
    private Timestamp tLtime;

    @SerializedName("c_disease_cat_code")
    @Size(
            max = 6,
            message = "'c_disease_cat_code' cannot exceed {max} characters!"
    )
    private String cDiseaseCatCode;

    @SerializedName("c_schedule_code")
    @Size(
            max = 6,
            message = "'c_schedule_code' cannot exceed {max} characters!"
    )
    private String cScheduleCode;

    @SerializedName("n_lock")
    @Digits(integer = 1, fraction = 0, message ="'n_lock' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigDecimal nLock;

    @SerializedName("c_modiuser")
    @Size(
            max = 10,
            message = "'c_modiuser' cannot exceed {max} characters!"
    )
    private String cModiuser;
}
