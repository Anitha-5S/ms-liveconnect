package com.c2.lc.ms.master.bos.customerbos;

import com.c2.lc.lib.base.BaseBO;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@EqualsAndHashCode(callSuper=false)
@Getter
@Setter
public class RowPackBO extends BaseBO implements Serializable {
    @NotEmpty(message = "'c_code' cannot be empty or null!")
    @NotBlank(message = "'c_code' cannot be blank!")
    @Size(
            max = 100,
            message = "'c_code' cannot exceed {max} characters!"
    )
    @SerializedName("c_code")
    private String cCode;

    @SerializedName("c_name")
    @Size(
            max = 35,
            message = "'c_name' cannot exceed {max} characters!"
    )
    @NotEmpty(message = "'c_name' cannot be empty or null!")
    @NotBlank(message = "'c_name' cannot be blank!")
    private String cName;


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
    private BigInteger nAudited;

    @SerializedName("n_predefined")
    @NotNull(message = "'n_predefined' cannot be null!")
    @Digits(integer = 1, fraction = 0, message ="'n_predefined' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigInteger nPredefined;

    @JsonFormat(pattern = "yyyy-dd-MM")
    @SerializedName("t_ltime")
    private Timestamp tLtime;

    @SerializedName("c_modiuser")
    @Size(
            max = 10,
            message = "'c_modiuser' cannot exceed {max} characters!"
    )
    private String cModiuser;


}
