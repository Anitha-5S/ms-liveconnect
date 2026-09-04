package com.c2.lc.ms.customer.entities.customer.pk;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewLaunchNotificationPKEntity implements Serializable {

    private static final long serialVersionUID = 1768031425552493039L;

    @SerializedName("n_user_id")
    @Column(name = "n_user_id", unique=true,nullable = false)
    private Long nUserId;

    @SerializedName("c_buyer_code")
    @Column(name = "c_buyer_code", unique=true, nullable = false)
    private String cBuyerCode;

    @SerializedName("c_seller_code")
    @Column(name = "c_seller_code", unique=true, nullable = false)
    private String cSellerCode;

    @SerializedName("c_item_code")
    @Column(name = "c_item_code", unique=true, nullable = false)
    private String cItemCode;

}
