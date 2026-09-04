package com.c2.lc.ms.customer.entities.customer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "combine_failed_mobile_numbers")
public class CombineFailedMobileNoEntity implements Serializable {

    private static final long serialVersionUID = -5443534162176298849L;

    @Expose(serialize = false, deserialize = false)
    @SerializedName("n_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id", unique = true, nullable = false)
    private Long nId;

    @Column(name = "c_mobile_number", length = 10)
    @SerializedName("c_mobile_number")
    private String mobileNo;

    @Column(name = "c_exception")
    @SerializedName("c_exception")
    private String exceptionMsg;

    @Expose(serialize = false, deserialize = false)
    @Column(name = "t_created_at", updatable = false)
    private LocalDateTime tCreatedAt;

    public CombineFailedMobileNoEntity() { }
}