package com.c2.lc.ms.customer.bos;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class AddressModelBO {

    @SerializedName("c_state_code")
    private String stateCode;

    @SerializedName("c_state_name")
    private String stateName;

    @SerializedName("c_sh_state_name")
    private String shStateName;

    @SerializedName("c_city_name")
    private String cityName;

    @SerializedName("c_city_code")
    private String cityCode;

    @SerializedName("j_area_list")
    private JsonArray areaList;

}
