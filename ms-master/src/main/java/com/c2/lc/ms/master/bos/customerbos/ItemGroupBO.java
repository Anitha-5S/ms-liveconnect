package com.c2.lc.ms.master.bos.customerbos;

import com.c2.lc.lib.base.BaseBO;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ItemGroupBO extends BaseBO implements Serializable {

    @SerializedName("data")
    @Valid
    private ItemGroupDataBO data;

    @SerializedName("br_code")
    @NotEmpty(message = "br_code cannot be null")
    @Size(min = 2,max = 4,message = "br_code has to between {min} and {max} characters!")
    private String brCode;

    @NotEmpty(message = "c2_code cannot be blank")
    @Size(min = 5, max = 6, message = "c_c2code has to between {min} and {max} characters!")
    @SerializedName("c2_code")
    private String c2Code;
}
