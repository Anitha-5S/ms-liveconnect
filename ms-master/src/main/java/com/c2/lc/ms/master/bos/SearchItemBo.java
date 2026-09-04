package com.c2.lc.ms.master.bos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
public class SearchItemBo implements Serializable {

    @Id
    @JsonProperty("objectID")
    private String objectId;

    @JsonProperty("c_item_code")
    private String itemCode;

    @JsonProperty("c_item_name")
    private String itemName;

    @JsonProperty("c_packing_size")
    private String packageSize;

}
