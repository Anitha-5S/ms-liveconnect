package com.c2.lc.ms.master.bos.customerbos;

import com.c2.lc.lib.base.BaseBO;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Data
public class CustomerMasterBO extends BaseBO implements Serializable {

    @SerializedName("c_table_name")
    public String tableName;

    @SerializedName("row")
    public List<CustomerMstDetailsBO> custList;
}
