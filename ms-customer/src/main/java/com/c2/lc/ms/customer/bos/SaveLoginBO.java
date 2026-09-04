package com.c2.lc.ms.customer.bos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveLoginBO {

    private Long n_profile_id;
    private String c_user_id;
    private String c_mobile_no;
    private String c_password;
}
