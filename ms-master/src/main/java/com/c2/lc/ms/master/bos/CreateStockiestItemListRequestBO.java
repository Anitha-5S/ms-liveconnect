package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.base.BaseBO;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStockiestItemListRequestBO extends BaseBO implements Serializable {

    @SerializedName("data")
    @NotEmpty(message = "'data' cannot be null!")
    private List<CreateStockiestItemBO> stockiestItemBoList;
}
