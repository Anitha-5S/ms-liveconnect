package com.c2.lc.ms.master.bos.customerbos;

import com.c2.lc.lib.base.BaseBO;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class ItemGroupDataBO extends BaseBO implements Serializable {

    @SerializedName("row")
    @Valid
    private List<RowItemGroupBO> row;

    @SerializedName("c_table_name")
    @NotEmpty(message = "'c_table_name' cannot be null value")
    private String tableName;
}
