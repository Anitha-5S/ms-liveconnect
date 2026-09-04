package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.ms.customer.entities.customer.pk.FirmSellersPKEntity;
import com.c2.lc.ms.customer.entities.customer.pk.NewLaunchNotificationPKEntity;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Data
@Entity
@IdClass(value = NewLaunchNotificationPKEntity.class)
@Table(name = "new_launch_notification")
public class NewLaunchNotificationEntity implements Serializable {

    private static final long serialVersionUID = -5443534162176298849L;

    @SerializedName("n_user_id")
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_user_id", unique = true, nullable = false)
    private Long nUserId;

    @Id
    @SerializedName("c_buyer_code")
    @Column(name = "c_buyer_code", unique=true, nullable = false)
    private String cBuyerCode;

    @Id
    @SerializedName("c_seller_code")
    @Column(name = "c_seller_code", unique=true, nullable = false)
    private String cSellerCode;

    @Id
    @SerializedName("c_item_code")
    @Column(name = "c_item_code", unique=true, nullable = false)
    private String cItemCode;

    @SerializedName("c_seller_name")
    @Column(name = "c_seller_name", nullable = false)
    private String cSellerName;

    @SerializedName("c_type")
    @Column(name = "c_type", nullable = false)
    private String cType;

    @SerializedName("d_adate")
    @Column(name = "d_adate")
    private LocalDate aDate;
}
