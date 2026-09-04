package com.c2.lc.ms.customer.bos;

import com.c2.lc.lib.properties.Messages;
import com.c2.lc.ms.customer.messages.FirmMessage;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerPriorityBo {

    @Min(value = 1, message = "c_priority should not be less than 1")
    @SerializedName("n_priority")
    private int priority;

    @Min(value = 1, message = "c_current_priority should not be less than 1")
    @SerializedName("n_current_priority")
    private int currentPriority;

    @SerializedName("c_mobile")
    private String mobile;

}
