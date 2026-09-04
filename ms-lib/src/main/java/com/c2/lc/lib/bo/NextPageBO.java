package com.c2.lc.lib.bo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NextPageBO implements Serializable {

    @SerializedName("n_next_page")
    private int page;

    @SerializedName("n_total")
    private int total;

}
