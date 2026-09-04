package com.c2.lc.ms.customer.entities.comm;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lc_user_type")
@IdClass(LcUserTypePK.class)
public class LcUserType implements Serializable {

    @Id
    @SerializedName("c_mobile_no")
    @Column(name="c_mobile_no")
    private String mobileNo;

    @Id
    @SerializedName("c_type")
    @Column(name="c_type")
    private String type;


    public LcUserType(LcUserTypePK pk) {
        this.mobileNo = pk.getMobileNo();
        this.type = pk.getType();
    }
}
