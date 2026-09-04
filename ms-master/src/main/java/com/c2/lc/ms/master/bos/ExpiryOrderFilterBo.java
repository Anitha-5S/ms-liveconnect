package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.ms.master.bos.customerbos.PackBO;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpiryOrderFilterBo extends PageBO {

    @SerializedName("ac_seller_code")
    private List<String> sellerCodes = new ArrayList<>();

    @SerializedName("ac_order_status")
    private List<Integer> orderStatus = new ArrayList<>();

    @SerializedName("d_from_date")
    private LocalDateTime fromDate;

    @SerializedName("d_to_date")
    private LocalDateTime toDate;

    @SerializedName("c_search_term")
    private String searchKey;
}
