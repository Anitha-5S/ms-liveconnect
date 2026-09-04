package com.c2.lc.ms.customer.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirmUserRoleBO implements Serializable {

    private static final long serialVersionUID = 4823829496621145799L;

    @SerializedName("c_view_transaction")
    private String viewTransaction;

    @SerializedName("c_place_order")
    private String placeOrder;

    @SerializedName("n_minimum_value")
    private Double minimumValue;

    @SerializedName("c_minimum_type")
    private String minimumType;

    @SerializedName("n_maximum_value")
    private Double maximumValue;

    @SerializedName("c_maximum_type")
    private String maximumType;

}
