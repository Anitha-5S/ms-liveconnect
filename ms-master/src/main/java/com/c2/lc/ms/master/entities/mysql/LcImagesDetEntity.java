package com.c2.lc.ms.master.entities.mysql;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "lc_images_det")
public class LcImagesDetEntity {

    @Id
    @Column(name = "n_id")
    private int nId;

    @Column(name = "n_sequence")
    private int nSequence;

    @Column(name = "n_status")
    private int nStatus;

    @Column(name = "t_aws_url")
    private String tAwsUrl;

    @Column(name = "n_mstid")
    private String mstId;

    @Column(name = "c_image_attribute")
    private String cImageAttribute;

    @Column(name = "d_created_on")
    private String dCreatedOn;

    @Column(name = "d_modified_on")
    private String dModifiedOn;

    @Column(name = "n_default_image")
    private int nDefaultImage;

    @Column(name = "t_file_name")
    private String tFileName;

    @Column(name = "n_offer_code")
    private String nOfferCode;

}
