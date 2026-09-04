package com.c2.lc.ms.master.bos;


import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.utils.Constants;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class OnePharmaBo {

    @SerializedName("c_from_date")
    private String fromDate;

    @SerializedName("c_to_date")
    private String toDate;

    @SerializedName("c_seller_code")
    private String sellerCode;

    @SerializedName("c_seller_name")
    private String sellerName;

    @SerializedName("n_inv_number")
    private String invoiceNumber;

    @SerializedName("c_inv_year")
    private String year;

    @SerializedName("c_inv_prefix")
    private String prefix = Constants.EMPTY_STRING;

    @SerializedName("c_table_headers")
    private String tableHeaders;

    @SerializedName("n_excel_fileType")
    private int excelFileType;
}
