package com.c2.lc.ms.customer.bos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListUserModelBO {

    @SerializedName("n_user_id")
    private Long userId;

    @SerializedName("c_name")
    private String userName;

    @SerializedName("c_mobile_no")
    private String mobileNo;

    @SerializedName("c_email_id")
    private String emailId;

}
