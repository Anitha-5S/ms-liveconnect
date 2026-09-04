package com.c2.lc.ms.master.entities.mysql;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cust_act_det", schema = "order_buk_new", catalog = "")
public class CustActDetEntity implements Serializable {

    @EmbeddedId
    private CustActDetEntityPK id;

    @Column(name = "c_gst_no")
    private String cGstNo;

    @Column(name = "d_ldate")
    private LocalDate dLdate;

    @Column(name = "d_date")
    private LocalDate dDate;

    @Column(name = "t_ltime")
    private Timestamp tLtime;

}
