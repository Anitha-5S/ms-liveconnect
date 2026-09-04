package com.c2.lc.ms.customer.bos;

import com.c2.lc.ms.customer.messages.FirmMessage;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckGstBo {

    @SerializedName("c_gst_number")
    @Size(message = FirmMessage.INVALIDATE_GST_NUMBER, max = 15,min = 15)
    @NotEmpty(message = "c_gst_number can not be empty!")
    private String gstNumber;

}
