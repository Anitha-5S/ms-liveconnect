package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.bo.SearchBO;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealSearchBo extends SearchBO {
    @SerializedName("dt_start_date")
    private String startDate;

    @SerializedName("dt_end_date")
    private String endDate;

    public DealSearchBo(String searchString, int page, int limit, String startDate, String endDate) {
        super(searchString, page, limit);
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
