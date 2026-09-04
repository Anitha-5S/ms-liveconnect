package com.c2.lc.ms.customer.bos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class StoreCombineResultBo implements Serializable {

    @SerializedName("j_drug_license_no")
    private List<StoreCombineBO> result;

}
