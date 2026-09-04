package com.c2.lc.ms.master.entities.mysql;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UrlConfigPK implements Serializable {

    @Column(name = "c_c2code", nullable = false)
    private String c_c2code;


    @Column(name = "c_product_code", nullable = false)
    private String c_product_code;


    @Column(name = "c_env", nullable = false)
    private String c_env;

}
