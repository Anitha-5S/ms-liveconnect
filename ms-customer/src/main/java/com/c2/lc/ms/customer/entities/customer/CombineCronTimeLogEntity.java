package com.c2.lc.ms.customer.entities.customer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "combine_cron_time_log")
public class CombineCronTimeLogEntity implements Serializable {

    private static final long serialVersionUID = -5443534162176298849L;

    @Expose(serialize = false, deserialize = false)
    @SerializedName("n_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id", unique = true, nullable = false)
    private Long nId;

    @Expose(serialize = false, deserialize = false)
    @Column(name = "t_started_at")
    private LocalDateTime startedAt;

    @Expose(serialize = false, deserialize = false)
    @Column(name = "t_ended_at")
    private LocalDateTime endedAt;

    @Column(name = "c_completed_status")
    @SerializedName("c_completed_status")
    private String completedStatus;

    public CombineCronTimeLogEntity() { }
}