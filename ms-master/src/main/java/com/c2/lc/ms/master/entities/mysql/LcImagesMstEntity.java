package com.c2.lc.ms.master.entities.mysql;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lc_images_mst")
@Data
public class LcImagesMstEntity {

    @Id
    @Column(name = "n_id")
    private int nId;

    @Column(name = "c_c2code")
    private String c2Code;

    @Column(name = "c_type")
    private String cType;

    @Column(name = "n_status")
    private int  nStatus;

    @Column(name = "c_create_by")
    private String cCreatedBy;

    @Column(name = "d_created_on")
    private String dCreatedOn;

    @Column(name = "c_code")
    private String cCode;

}
