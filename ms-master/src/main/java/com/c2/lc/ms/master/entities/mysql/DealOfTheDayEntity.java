package com.c2.lc.ms.master.entities.mysql;

import com.c2.lc.lib.db.DateAudit;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "deal_of_the_day")
public class DealOfTheDayEntity extends DateAudit implements Serializable {
    @SerializedName("n_deal_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_deal_id", unique = true, nullable = false)
    private Long dealId;

    @Column(name = "c_c2code")
    @SerializedName("c_c2code")
    private String c2Code;

    @Column(name = "c_item_code", nullable = false)
    @SerializedName("c_item_code")
    private String itemCode;

    @Column(name = "c_item_name")
    @SerializedName("c_item_name")
    private String itemName;

    @Column(name = "c_deal_status")
    @SerializedName("c_deal_status")
    private String status;

    @Column(name = "c_discount_type")
    @SerializedName("c_discount_type")
    private String discType;

    @Column(name = "n_discount_amount")
    @SerializedName("n_discount_amount")
    private BigDecimal discAmount;

    @Column(name = "n_discount_percentage")
    @SerializedName("n_discount_percentage")
    private BigDecimal discPercentage;

    @Column(name = "n_deal_rate", precision = 3)
    @SerializedName("n_deal_rate")
    private BigDecimal dealRate;

    @Column(name = "t_start_date")
    @SerializedName("t_start_date")
    private LocalDateTime startDate;

    @Column(name = "t_end_date")
    @SerializedName("t_end_date")
    private LocalDateTime endDate;

    public DealOfTheDayEntity(Long userId, LocalDateTime time){ super(userId, time);}

    public DealOfTheDayEntity() { }
}
