package com.c2.lc.ms.master.entities.mysql;

import com.c2.lc.ms.master.repos.mysql.CustActMstRequestPK;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cust_act_mst_request", schema = "order_buk_new", catalog = "")
public class CustActMstRequest implements Serializable {

    @EmbeddedId
    private CustActMstRequestPK id;

    @Column(name = "c_name")
    private String cname;

    @Column(name = "c_grp_no")
    private String cgrpNo;

    @Column(name = "n_op_bal")
    private BigDecimal nopBal;

    @Column(name = "c_credit_debit")
    private String ccreditDebit;

    @Column(name = "n_customer")
    private BigDecimal ncustomer;

    @Column(name = "n_supplier")
    private BigDecimal nsupplier;

    @Column(name = "c_cash_bank_other")
    private String ccashBankOther;

    @Column(name = "n_credit_limit")
    private BigDecimal ncreditLimit;

    @Column(name = "n_credit_days")
    private BigDecimal ncreditDays;

    @Column(name = "n_debit_limit")
    private BigDecimal ndebitLimit;

    @Column(name = "n_debit_days")
    private BigDecimal ndebitDays;

    @Column(name = "c_add_1")
    private String cadd1;

    @Column(name = "c_add_2")
    private String cadd2;

    @Column(name = "c_add_3")
    private String cadd3;

    @Column(name = "c_city")
    private String ccity;

    @Column(name = "c_pin")
    private String cpin;

    @Column(name = "c_phone_1")
    private String cphone1;

    @Column(name = "c_phone_2")
    private String cphone2;

    @Column(name = "c_fax")
    private String cfax;

    @Column(name = "c_area_code")
    private String careaCode;

    @Column(name = "c_sman_code")
    private String csmanCode;

    @Column(name = "c_dman_code")
    private String cdmanCode;

    @Column(name = "n_cust_discount")
    private BigDecimal ncustDiscount;

    @Column(name = "n_cash_chq_party")
    private BigDecimal ncashChqParty;

    @Column(name = "n_lock")
    private BigDecimal nlock;

    @Column(name = "n_interest_rate")
    private BigDecimal ninterestRate;

    @Column(name = "c_drug_licence_no_1")
    private String cdrugLicenceNo1;

    @Column(name = "c_drug_licence_no_2")
    private String cdrugLicenceNo2;

    @Column(name = "c_st_no")
    private String cstNo;

    @Column(name = "c_cst_no")
    private String ccstNo;

    @Column(name = "c_contact_person")
    private String ccontactPerson;

    @Column(name = "c_cust_category_code")
    private String ccustCategoryCode;

    @Column(name = "n_predefined")
    private BigDecimal npredefined;

    @Column(name = "d_date")
    private LocalDate dDate;

    @Column(name = "n_head_office")
    private BigDecimal nheadOffice;

    @Column(name = "c_customer_status")
    private String ccustomerStatus;

    @Column(name = "c_remark")
    private String cremark;

    @Column(name = "c_route_code")
    private String crouteCode;

    @Column(name = "n_lock_on_bills")
    private BigDecimal nlockOnBills;

    @Column(name = "n_bill_collect_after")
    private BigDecimal nbillCollectAfter;

    @Column(name = "c_mobile")
    private String cmobile;

    @Column(name = "c_bank_code")
    private String cbankCode;

    @Column(name = "c_sort_order")
    private String csortOrder;

    @Column(name = "n_auto_lock")
    private BigDecimal nautoLock;

    @Column(name = "n_dis_per")
    private BigDecimal ndisPer;

    @Column(name = "n_pcbc_charges")
    private BigDecimal npcbcCharges;

    @Column(name = "c_other_info")
    private String cOtherInfo;

    @Column(name = "c_sort_order_col")
    private String csortOrderCol;

    @Column(name = "n_max_item")
    private BigDecimal nmaxItem;

    @Column(name = "n_comp")
    private BigDecimal ncomp;

    @Column(name = "c_nw_code")
    private String cnwCode;

    @Column(name = "d_dl_date")
    private LocalDate ddLdate;

    @Column(name = "n_print_ack")
    private BigDecimal nprintAck;

    @Column(name = "n_os_print")
    private BigDecimal nosPrint;

    @Column(name = "n_contolled_product")
    private BigDecimal ncontolledProduct;

    @Column(name = "c_dc_group_code")
    private String cdcGroupCode;

    @Column(name = "c_user")
    private String cuser;

    @Column(name = "d_ldate")
    private LocalDate dLdate;

    @Column(name = "c_br_code")
    private String cbrCode;

    @Column(name = "c_mob_used_by")
    private String cmobUsedBy;

    @Column(name = "c_email_id")
    private String cemailId;

    @Column(name = "c_common_code")
    private String ccommonCode;

    @Column(name = "n_auto_cr_adjust")
    private BigDecimal nautoCrAdjust;

    @Column(name = "c_inv_type")
    private String cinvType;

    @Column(name = "c_message")
    private String cmessage;

    @Column(name = "n_sale_day_before_expiry")
    private BigDecimal nsaleDayBeforeExpiry;

    @Column(name = "n_outstation")
    private BigDecimal noutstation;

    @Column(name = "c_pass")
    private String cpass;

    @Column(name = "n_rec_autoadj_flag")
    private BigDecimal nrecAutoadjFlag;

    @Column(name = "n_ship_code_req")
    private BigDecimal nshipCodeReq;

    @Column(name = "n_max_bill_amt")
    private BigDecimal nmaxBillAmt;

    @Column(name = "c_tallyhname")
    private String ctallyhname;

    @Column(name = "c_tallygname")
    private String ctallygname;

    @Column(name = "c_visit_day")
    private String cvisitDay;

    @Column(name = "n_min_dis_per")
    private BigDecimal nminDisPer;

    @Column(name = "n_auto_dr_adjust")
    private BigDecimal nautoDrAdjust;

    @Column(name = "n_inter_state")
    private BigDecimal ninterState;

    @Column(name = "n_override_manual_sch_flag")
    private BigDecimal nOverrideManualSchFlag;

    @Column(name = "n_tds_per")
    private BigDecimal ntdsPer;

    @Column(name = "c_pan_no")
    private String cpanNo;

    @Column(name = "n_geo_lat")
    private String ngeoLat;

    @Column(name = "n_geo_lon")
    private String ngeoLon;

    @Column(name = "n_geo_radius")
    private BigDecimal ngeoRadius;

    @Column(name = "c_bank_act_number")
    private String cbankActNumber;

    @Column(name = "n_inst")
    private BigDecimal ninst;

    @Column(name = "c_narcotic_dl_no")
    private String cnarcoticDlNo;

    @Column(name = "c_dl_sch_no")
    private String cdlSchNo;

    @Column(name = "c_food_lic_no")
    private String cfoodLicNo;

//    @Column(name = "d_doi")
//    private LocalDate ddoi;

    @Column(name = "c_ord_visit_day")
    private String cordVisitDay;

    @Column(name = "n_auto_counter_sale")
    private BigDecimal nautoCounterSale;

    @Column(name = "n_dc_lock")
    private BigDecimal ndcLock;

    @Column(name = "n_order_to_dc")
    private BigDecimal norderToDc;

    @Column(name = "n_dc_conv_till_max_bill_amt")
    private BigDecimal ndcConvTillMaxBillAmt;

    @Column(name = "n_overdue_interest_rate")
    private BigDecimal nOverdueInterestRate;

//    @Column(name = "d_start_date")
//    private LocalDate dstartDate;

//    @Column(name = "d_close_date")
//    private LocalDate dcloseDate;

    @Column(name = "n_max_chq_bounce")
    private BigDecimal nmaxChqBounce;

    @Column(name = "n_employee")
    private BigDecimal nemployee;

    @Column(name = "n_rate_type")
    private BigDecimal nrateType;

    @Column(name = "c_print_name")
    private String cprintName;

    @Column(name = "n_eb_crnt_type")
    private BigDecimal nebCrntType;

    @Column(name = "c_tan_no")
    private String ctanNo;

    @Column(name = "c_bank_branch")
    private String cbankBranch;

    @Column(name = "n_trading_flag")
    private BigDecimal ntradingFlag;

    @Column(name = "c_doc_format_code")
    private String cdocFormatCode;

    @Column(name = "n_auto_split_inv")
    private BigDecimal nAutoSplitInv;

    @Column(name = "n_avg_month_exp")
    private BigDecimal navgMonthExp;

    @Column(name = "n_order_based_on")
    private BigDecimal norderBasedOn;

    @Column(name = "n_order_days")
    private BigDecimal norderDays;

    @Column(name = "n_order_sale_days_month")
    private BigDecimal norderSaleDaysMonth;

    @Column(name = "n_order_day_month")
    private BigDecimal norderDayMonth;

    @Column(name = "n_order_split")
    private BigDecimal norderSplit;

    @Column(name = "n_order_ratio")
    private BigDecimal norderRatio;

    @Column(name = "c_order_rpt_name")
    private String corderRptName;

    @Column(name = "n_order_add_br_transfer_qty")
    private BigDecimal norderAddBrTransferQty;

    @Column(name = "n_order_consider_dc")
    private BigDecimal norderConsiderDc;

    @Column(name = "n_order_consider_lost_ord")
    private BigDecimal norderConsiderLostOrd;

    @Column(name = "n_order_conv_lot")
    private BigDecimal norderConvLot;

    @Column(name = "c_luser")
    private String cluser;

    @Column(name = "c_lbt_no")
    private String clbtNo;

    @Column(name = "n_crnt_adj_type")
    private BigDecimal ncrntAdjType;

    @Column(name = "c_micr_code")
    private String cmicrCode;

    @Column(name = "n_mcrnt_flag")
    private BigDecimal nmcrntFlag;

    @Column(name = "n_calc_cash_disc_for_cr_disc_0")
    private BigDecimal ncalcCashDiscForCrDisc0;

    @Column(name = "n_counter_sale_info")
    private BigDecimal ncounterSaleInfo;

    @Column(name = "c_file_no")
    private String cfileNo;

    @Column(name = "n_incl_lost_ord_in_inv_export")
    private BigDecimal ninclLostOrdInInvExport;

    @Column(name = "c_short_name")
    private String cshortName;

    @Column(name = "n_color")
    private BigDecimal ncolor;

    @Column(name = "n_schm_slab2")
    private String nschmSlab2;

    @Column(name = "n_schm_slab3")
    private String nschmSlab3;

    @Column(name = "n_sch_flag")
    private Integer nschFlag;

    @Column(name = "n_purchase_download")
    private Integer npurchaseDownload;

    @Column(name = "n_stock_in_hand")
    private Integer nstockInHand;

    @Column(name = "n_order_entry")
    private Integer norderEntry;

    @Column(name = "n_visible_creditnote")
    private Integer nvisibleCreditnote;

    @Column(name = "n_visible_outstandingamt")
    private Integer nvisibleOutstandingamt;

    @Column(name = "n_invoice_download")
    private Integer ninvoiceDownload;

    @Column(name = "n_barcode_req")
    private BigDecimal nbarcodeReq;
}
