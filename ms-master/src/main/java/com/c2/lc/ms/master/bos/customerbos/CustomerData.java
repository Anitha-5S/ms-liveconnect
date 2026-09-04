package com.c2.lc.ms.master.bos.customerbos;

import com.c2.lc.lib.base.BaseBO;
import com.c2.lc.ms.master.utils.MsMessages;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper=false)
public class CustomerData extends BaseBO implements Serializable {
    @SerializedName("data")
    @Valid
    private CustomerMasterBO data;

    @SerializedName("c_br_code")
    @NotEmpty(message = "'br_code' cannot be null")
    @Size(message = MsMessages.VALIDATE_BRCODE_LENGTH, min = 2,max = 3)
    private String brCode;

    @NotEmpty(message = "'c2_code' cannot be null")
    @Size(message = MsMessages.VALIDATE_C2CODE_LENGTH, min = 5, max = 6)
    @SerializedName("c_c2_code")
    private String c2Code;
}
