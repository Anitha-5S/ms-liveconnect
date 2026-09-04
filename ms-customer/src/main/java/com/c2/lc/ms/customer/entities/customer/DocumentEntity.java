package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.lib.db.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "other_documents")
public class DocumentEntity extends DateAudit implements Serializable {

    @Expose(serialize = false, deserialize = false)
    @SerializedName("n_documents_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_documents_id", unique = true, nullable = false)
    private Long nDocumentsId;

    @SerializedName("c_tan_no")
    @Column(name = "c_tan_no", length = 32)
    private String cTanNo;

    @SerializedName("c_tan_no_img")
    @Column(name = "c_tan_no_img", length = 1024)
    private String cTanNoImg;

    @SerializedName("c_pan_no")
    @Column(name = "c_pan_no", length = 32)
    private String cPanNo;

    @SerializedName("c_pan_no_img")
    @Column(name = "c_pan_no_img", length = 1024)
    private String cPanNoImg;

    @SerializedName("c_it_pan_no")
    @Column(name = "c_it_pan_no", length = 32)
    private String cItPanNo;

    @SerializedName("c_it_pan_no_img")
    @Column(name = "c_it_pan_no_img", length = 1024)
    private String cItPanNoImg;

    @SerializedName("c_electricity_bill")
    @Column(name = "c_electricity_bill", length = 32)
    private String cElectricityBill;

    @SerializedName("c_electricity_bill_img")
    @Column(name = "c_electricity_bill_img", length = 1024)
    private String cElectricityBillImg;

    @SerializedName("c_rent_agreement")
    @Column(name = "c_rent_agreement", length = 32)
    private String cRentAgreement;

    @SerializedName("c_rent_agreement_img")
    @Column(name = "c_rent_agreement_img", length = 1024)
    private String cRentAgreementImg;

    @SerializedName("c_partnership_deed")
    @Column(name = "c_partnership_deed", length = 32)
    private String cPartnershipDeed;

    @SerializedName("c_partnership_deed_img")
    @Column(name = "c_partnership_deed_img", length = 1024)
    private String cPartnershipDeedImg;

    @SerializedName("c_bank_statement")
    @Column(name = "c_bank_statement", length = 32)
    private String cBankStatement;

    @SerializedName("c_bank_statement_img")
    @Column(name = "c_bank_statement_img", length = 1024)
    private String cBankStatementImg;

    @SerializedName("c_authority_letter")
    @Column(name = "c_authority_letter", length = 32)
    private String cAuthorityLetter;

    @SerializedName("c_authority_letter_img")
    @Column(name = "c_authority_letter_img", length = 1024)
    private String cAuthorityLetterImg;

    public DocumentEntity(Long userId, LocalDateTime time){ super(userId, time);}

    public DocumentEntity() {

    }
}