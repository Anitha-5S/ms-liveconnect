package com.c2.lc.ms.master.models;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class NewLaunchRequest implements Serializable {

   @SerializedName("n_page")
   private int pageNo;

   @SerializedName("n_limit")
   private int rowLimit;

}
