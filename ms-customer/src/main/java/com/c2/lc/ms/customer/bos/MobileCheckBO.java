package com.c2.lc.ms.customer.bos;

import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.messages.FirmMessage;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileCheckBO {


    @SerializedName("c_mobile_no")
    @Size(message = FirmMessage.INVALIDATE_MOBILE_LENGTH, max = 10,min = 10)
    @NotEmpty(message = "c_mobile_no can not be empty!")
    private String cMobileNo;

    @SerializedName("c_type")
    @Size(message = Messages.STATUS_FILED_LENGTH, max = 2)
    // TODO Validate for B & S
    private String cType;



}
