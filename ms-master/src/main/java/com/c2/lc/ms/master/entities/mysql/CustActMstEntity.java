package com.c2.lc.ms.master.entities.mysql;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cust_act_mst", schema = "order_buk_new", catalog = "")
@IdClass(CustActMstEntityPK.class)
public class CustActMstEntity implements Serializable, Cloneable{

    @Id
    @Column(name = "c_c2code")
    private String cC2Code;

    @Id
    @Column(name = "c_code")
    private String cCode;

    @Column(name = "c_name")
    private String cName;

    @Column(name = "c_grp_no")
    private String cGrpNo;

    @Column(name = "n_op_bal")
    private BigDecimal nOpBal;

    @Column(name = "c_credit_debit")
    private String cCreditDebit;

    @Column(name = "n_customer")
    private BigDecimal nCustomer;

    @Column(name = "n_supplier")
    private BigDecimal nSupplier;

    @Column(name = "c_cash_bank_other")
    private String cCashBankOther;

    @Column(name = "n_credit_limit")
    private BigDecimal nCreditLimit;

    @Column(name = "n_credit_days")
    private BigDecimal nCreditDays;

    @Column(name = "n_debit_limit")
    private BigDecimal nDebitLimit;

    @Column(name = "n_debit_days")
    private BigDecimal nDebitDays;

    @Column(name = "c_add_1")
    private String cAdd1;

    @Column(name = "c_add_2")
    private String cAdd2;

    @Column(name = "c_add_3")
    private String cAdd3;

    @Column(name = "c_city")
    private String cCity;

    @Column(name = "c_pin")
    private String cPin;

    @Column(name = "c_phone_1")
    private String cPhone1;

    @Column(name = "c_phone_2")
    private String cPhone2;

    @Column(name = "c_fax")
    private String cFax;

    @Column(name = "c_area_code")
    private String cAreaCode;

    @Column(name = "c_sman_code")
    private String cSmanCode;

    @Column(name = "c_dman_code")
    private String cDmanCode;

    @Column(name = "n_cust_discount")
    private BigDecimal nCustDiscount;

    @Column(name = "n_cash_chq_party")
    private BigDecimal nCashChqParty;

    @Column(name = "n_lock")
    private BigDecimal nLock;

    @Column(name = "n_interest_rate")
    private BigDecimal nInterestRate;

    @Column(name = "c_drug_licence_no_1")
    private String cDrugLicenceNo1;

    @Column(name = "c_drug_licence_no_2")
    private String cDrugLicenceNo2;

    @Column(name = "c_st_no")
    private String cStNo;

    @Column(name = "c_cst_no")
    private String cCstNo;

    @Column(name = "c_contact_person")
    private String cContactPerson;

    @Column(name = "c_cust_category_code")
    private String cCustCategoryCode;

    @Column(name = "n_predefined")
    private BigDecimal nPredefined;

    @Column(name = "d_date")
    private LocalDate dDate;

    @Column(name = "n_head_office")
    private BigDecimal nHeadOffice;

    @Column(name = "c_customer_status")
    private String cCustomerStatus;

    @Column(name = "c_remark")
    private String cRemark;

    @Column(name = "c_route_code")
    private String cRouteCode;

    @Column(name = "n_lock_on_bills")
    private BigDecimal nLockOnBills;

    @Column(name = "n_bill_collect_after")
    private BigDecimal nBillCollectAfter;

    @Column(name = "c_mobile")
    private String cMobile;

    @Column(name = "c_bank_code")
    private String cBankCode;

    @Column(name = "c_sort_order")
    private String cSortOrder;

    @Column(name = "n_auto_lock")
    private BigDecimal nAutoLock;

    @Column(name = "n_dis_per")
    private BigDecimal nDisPer;

    @Column(name = "n_pcbc_charges")
    private BigDecimal nPcbcCharges;

    @Column(name = "c_other_info")
    private String cOtherInfo;

    @Column(name = "c_sort_order_col")
    private String cSortOrderCol;

    @Column(name = "n_max_item")
    private BigDecimal nMaxItem;

    @Column(name = "n_comp")
    private BigDecimal nComp;

    @Column(name = "c_nw_code")
    private String cNwCode;

    @Column(name = "d_dl_date")
    private LocalDate dDlDate;

    @Column(name = "n_print_ack")
    private BigDecimal nPrintAck;

    @Column(name = "n_os_print")
    private BigDecimal nOsPrint;

    @Column(name = "n_contolled_product")
    private BigDecimal nContolledProduct;

    @Column(name = "c_dc_group_code")
    private String cDcGroupCode;

    @Column(name = "c_user")
    private String cUser;

    @Column(name = "d_ldate")
    private LocalDate dLdate;

    @Column(name = "c_br_code")
    private String cBrCode;

    @Column(name = "c_mob_used_by")
    private String cMobUsedBy;

    @Column(name = "c_email_id")
    private String cEmailId;

    @Column(name = "c_common_code")
    private String cCommonCode;

    @Column(name = "n_auto_cr_adjust")
    private BigDecimal nAutoCrAdjust;

    @Column(name = "c_inv_type")
    private String cInvType;

    @Column(name = "c_message")
    private String cMessage;

    @Column(name = "n_sale_day_before_expiry")
    private BigDecimal nSaleDayBeforeExpiry;

    @Column(name = "n_outstation")
    private BigDecimal nOutstation;

    @Column(name = "c_pass")
    private String cPass;

    @Column(name = "n_rec_autoadj_flag")
    private BigDecimal nRecAutoadjFlag;

    @Column(name = "n_ship_code_req")
    private BigDecimal nShipCodeReq;

    @Column(name = "n_max_bill_amt")
    private BigDecimal nMaxBillAmt;

    @Column(name = "c_tallyhname")
    private String cTallyhname;

    @Column(name = "c_tallygname")
    private String cTallygname;

    @Column(name = "c_visit_day")
    private String cVisitDay;

    @Column(name = "n_min_dis_per")
    private BigDecimal nMinDisPer;

    @Column(name = "n_auto_dr_adjust")
    private BigDecimal nAutoDrAdjust;

    @Column(name = "n_inter_state")
    private BigDecimal nInterState;

    @Column(name = "n_override_manual_sch_flag")
    private BigDecimal nOverrideManualSchFlag;

    @Column(name = "n_tds_per")
    private BigDecimal nTdsPer;

    @Column(name = "c_pan_no")
    private String cPanNo;

    @Column(name = "n_geo_lat")
    private String nGeoLat;

    @Column(name = "n_geo_lon")
    private String nGeoLon;

    @Column(name = "n_geo_radius")
    private BigDecimal nGeoRadius;

    @Column(name = "c_bank_act_number")
    private String cBankActNumber;

    @Column(name = "n_inst")
    private BigDecimal nInst;

    @Column(name = "c_narcotic_dl_no")
    private String cNarcoticDlNo;

    @Column(name = "c_dl_sch_no")
    private String cDlSchNo;

    @Column(name = "c_food_lic_no")
    private String cFoodLicNo;

    @Column(name = "d_doi")
    private LocalDate dDoi;

    @Column(name = "c_ord_visit_day")
    private String cOrdVisitDay;

    @Column(name = "n_auto_counter_sale")
    private BigDecimal nAutoCounterSale;

    @Column(name = "n_dc_lock")
    private BigDecimal nDcLock;

    @Column(name = "n_order_to_dc")
    private BigDecimal nOrderToDc;

    @Column(name = "n_dc_conv_till_max_bill_amt")
    private BigDecimal nDcConvTillMaxBillAmt;

    @Column(name = "n_overdue_interest_rate")
    private BigDecimal nOverdueInterestRate;

    @Column(name = "d_start_date")
    private LocalDate dStartDate;

    @Column(name = "d_close_date")
    private LocalDate dCloseDate;

    @Column(name = "n_max_chq_bounce")
    private BigDecimal nMaxChqBounce;

    @Column(name = "n_employee")
    private BigDecimal nEmployee;

    @Column(name = "n_rate_type")
    private BigDecimal nRateType;

    @Column(name = "c_print_name")
    private String cPrintName;

    @Column(name = "n_eb_crnt_type")
    private BigDecimal nEbCrntType;

    @Column(name = "c_tan_no")
    private String cTanNo;

    @Column(name = "c_bank_branch")
    private String cBankBranch;

    @Column(name = "n_trading_flag")
    private BigDecimal nTradingFlag;

    @Column(name = "c_doc_format_code")
    private String cDocFormatCode;

    @Column(name = "n_auto_split_inv")
    private BigDecimal nAutoSplitInv;

    @Column(name = "n_avg_month_exp")
    private BigDecimal nAvgMonthExp;

    @Column(name = "n_order_based_on")
    private BigDecimal nOrderBasedOn;

    @Column(name = "n_order_days")
    private BigDecimal nOrderDays;

    @Column(name = "n_order_sale_days_month")
    private BigDecimal nOrderSaleDaysMonth;

    @Column(name = "n_order_day_month")
    private BigDecimal nOrderDayMonth;

    @Column(name = "n_order_split")
    private BigDecimal nOrderSplit;

    @Column(name = "n_order_ratio")
    private BigDecimal nOrderRatio;

    @Column(name = "c_order_rpt_name")
    private String cOrderRptName;

    @Column(name = "n_order_add_br_transfer_qty")
    private BigDecimal nOrderAddBrTransferQty;

    @Column(name = "n_order_consider_dc")
    private BigDecimal nOrderConsiderDc;

    @Column(name = "n_order_consider_lost_ord")
    private BigDecimal nOrderConsiderLostOrd;

    @Column(name = "n_order_conv_lot")
    private BigDecimal nOrderConvLot;

    @Column(name = "c_luser")
    private String cLuser;

    @Column(name = "c_lbt_no")
    private String cLbtNo;

    @Column(name = "n_crnt_adj_type")
    private BigDecimal nCrntAdjType;

    @Column(name = "c_micr_code")
    private String cMicrCode;

    @Column(name = "n_mcrnt_flag")
    private BigDecimal nMcrntFlag;

    @Column(name = "n_calc_cash_disc_for_cr_disc_0")
    private BigDecimal nCalcCashDiscForCrDisc0;

    @Column(name = "n_counter_sale_info")
    private BigDecimal nCounterSaleInfo;

    @Column(name = "c_file_no")
    private String cFileNo;

    @Column(name = "n_incl_lost_ord_in_inv_export")
    private BigDecimal nInclLostOrdInInvExport;

    @Column(name = "c_short_name")
    private String cShortName;

    @Column(name = "n_color")
    private BigDecimal nColor;

    @Column(name = "n_schm_slab2")
    private String nSchmSlab2;

    @Column(name = "n_schm_slab3")
    private String nSchmSlab3;

    @Column(name = "n_sch_flag", columnDefinition = "TINYINT")
    private Integer nSchFlag;

    @Column(name = "n_purchase_download", columnDefinition = "TINYINT")
    private Integer nPurchaseDownload;

    @Column(name = "n_stock_in_hand", columnDefinition = "TINYINT")
    private Integer nStockInHand;

    @Column(name = "n_order_entry", columnDefinition = "TINYINT")
    private Integer nOrderEntry;

    @Column(name = "n_visible_creditnote", columnDefinition = "TINYINT")
    private Integer nVisibleCreditnote;

    @Column(name = "n_visible_outstandingamt", columnDefinition = "TINYINT")
    private Integer nVisibleOutstandingamt;

    @Column(name = "n_invoice_download", columnDefinition = "TINYINT")
    private Integer nInvoiceDownload;

    @Column(name = "n_barcode_req")
    private BigDecimal nBarcodeReq;


    @Override
    public CustActMstEntity clone() throws CloneNotSupportedException {
        return new CustActMstEntity(cC2Code,cCode,cName,cGrpNo,nOpBal,cCreditDebit,nCustomer,nSupplier,cCashBankOther,nCreditLimit,nCreditDays,nDebitLimit,nDebitDays,cAdd1, cAdd2,cAdd3, cCity,cPin,cPhone1,cPhone2,cFax,cAreaCode,cSmanCode,cDmanCode,nCustDiscount, nCashChqParty,nLock,nInterestRate,cDrugLicenceNo1,cDrugLicenceNo2,cStNo,cCstNo,cContactPerson,cCustCategoryCode,nPredefined,dDate,nHeadOffice,cCustomerStatus,cRemark,cRouteCode,nLockOnBills,nBillCollectAfter,cMobile,cBankCode,cSortOrder,nAutoLock,nDisPer,nPcbcCharges,cOtherInfo,cSortOrderCol,nMaxItem,nComp,cNwCode,dDlDate,nPrintAck,nOsPrint,nContolledProduct,cDcGroupCode,cUser,dLdate,cBrCode,cMobUsedBy,cEmailId,cCommonCode,nAutoCrAdjust,cInvType,cMessage,nSaleDayBeforeExpiry,nOutstation,cPass,nRecAutoadjFlag,nShipCodeReq,nMaxBillAmt,cTallyhname,cTallygname,cVisitDay,nMinDisPer,nAutoDrAdjust,nInterState,nOverrideManualSchFlag,nTdsPer,cPanNo,nGeoLat,nGeoLon,nGeoRadius,cBankActNumber,nInst,cNarcoticDlNo,cDlSchNo,cFoodLicNo,dDoi,cOrdVisitDay,nAutoCounterSale,nDcLock,nOrderToDc,nDcConvTillMaxBillAmt,nOverdueInterestRate,dStartDate,dCloseDate,nMaxChqBounce,nEmployee,nRateType,cPrintName,nEbCrntType,cTanNo,cBankBranch,nTradingFlag,cDocFormatCode,nAutoSplitInv,nAvgMonthExp,nOrderBasedOn,nOrderDays,nOrderSaleDaysMonth,nOrderDayMonth,nOrderSplit,nOrderRatio,cOrderRptName,nOrderAddBrTransferQty,nOrderConsiderDc,nOrderConsiderLostOrd,nOrderConvLot,cLuser,cLbtNo,nCrntAdjType,cMicrCode,nMcrntFlag,nCalcCashDiscForCrDisc0,nCounterSaleInfo,cFileNo,nInclLostOrdInInvExport,cShortName,nColor,nSchmSlab2,nSchmSlab3,nSchFlag,nPurchaseDownload,nStockInHand,nOrderEntry,nVisibleCreditnote,nVisibleOutstandingamt,nInvoiceDownload,nBarcodeReq) ;
    }



}
