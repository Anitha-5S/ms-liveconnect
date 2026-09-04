package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.bo.SearchBO;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemsSearchBO extends SearchBO implements Serializable {

    @SerializedName("c_manufacturers")
    private List<String> manufacturers = new ArrayList<>();

    @SerializedName("c_brands")
    private List<String> brands = new ArrayList<>();

    @SerializedName("c_sellers")
    private List<String> sellers = new ArrayList<>();

    @SerializedName("c_availability")
    private String availability;

    @SerializedName("c_discount")
    private List<String> discount = new ArrayList<>();

    @SerializedName("c_product_search")
    private String productName;


}
