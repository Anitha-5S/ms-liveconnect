package com.c2.lc.ms.customer.entities.seller;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "lc_c2code_mst")
@AllArgsConstructor
@NoArgsConstructor
public class LcC2CodeMstEntity implements Serializable {
    private static final long serialVersionUID = 884684547453256298L;

    private String cCode;
    private String cName;
    private String cShName;
    private String cGrpNo;
    private Integer nType;
    private String cDesc;
    private String cContactPerson;
    private String cCity;
    private String cPin;
    private String cPhone1;
    private String cPhone2;
    private String cFax;
    private String cMobile;
    private String cEmail;
    private String cAreaCode;
    private String cMrCode;
    private String cCustCategoryCode;
    private String cBranchCode;
    private BigDecimal nDiscount;
    private Integer nLock;
    private String cDrugLicenceNo1;
    private String cDrugLicenceNo2;
    private String cStNo;
    private String cCstNo;
    private Timestamp dDate;
    private String cRemark;
    private BigDecimal nCreditLimit;
    private Integer nCreditDays;
    private Integer nDebitDays;
    private BigDecimal nDebitLimit;
    private Integer nFlag;
    private Integer nOrderDays;
    private Timestamp dLdate;
    private Timestamp dAdate;
    private String cCreateuser;
    private String cTaxTypeCode;
    private BigDecimal nPoints;
    private String cCardNo;
    private BigDecimal nCardValue;
    private Integer nRateOn;
    private String cDiscountSlabCode;
    private Integer nShift;
    private BigDecimal nPointFactor;
    private Timestamp tLtime;
    private Timestamp dActivatedDate;
    private Timestamp dDeactivatedDate;
    private BigDecimal nCardReverseDis;
    private Integer nRoaming;
    private Timestamp dDlExpiryDate;
    private Integer nAdminReconcile;
    private String cDrugLicenceNo3;
    private String cTallyhname;
    private String cTallygname;
    private String cGeoLat;
    private String cGeoLon;
    private Integer nInterPur;
    private String cPrintName;
    private String cPanNo;
    private String cTanNo;
    private Integer nEnableLiveOrder;
    private Integer nAutolock;
    private Integer nGraceDays;
    private BigDecimal nGraceLimit;
    private Integer nMaxChqBounce;
    private Integer nAdminSettlement;
    private String cWeb;
    private Integer nRateIncVat;
    private Integer nRoadPermitFlag;
    private String cOrdInvTermCode;
    private String cTransportCode;
    private String cTinNo;
    private String cExciseNo;
    private String cBankCode;
    private String cIfscCode;
    private String cMicrCode;
    private String cBankActNo;
    private String cCorContPerson;
    private String cCorPhNo;
    private String nMaxLineItem;
    private Integer nDiscFlag;
    private Integer nChqPrintEnable;
    private String cModiuser;
    private String cBldgLandmarkCode;
    private String cBldgLandmarkCodeC;
    private String cCinNo;
    private String cCurrencyCode;
    private String cWebsite;
    private String cPwd;
    private Integer nSoConversionType;
    private Integer nDcPrintRate;
    private Integer nTaxPrint;
    private Integer nSearchItem;
    private Integer nSplRateFlag;
    private String cCommonPath;
    private Integer nDuedateBasedOn;
    private Integer nDuedateCalDays;
    private Integer nDuedateCalMethod;
    private Integer nOnlinePay;
    private String cParentActCode;
    private String cImportBrCode;
    private String cImportTraderCode;
    private Integer nExpDays;
    private Integer nUpdateStock;
    private Integer nServiceChargeInclExcl;
    private Integer nSoDcProcess;
    private Integer nNoOfCopies;
    private String cEmpCode;
    private Integer nMinStockDay;
    private String cProvisionalGstnNo;
    private String cPermanentGstnNo;
    private Integer nSeperateUpdn;
    private String cEdiCode;
    private String dStockLdate;
    private String cSlugName;
    private String cCustBranchCode;
    private BigInteger nOrderFlag;
    private BigInteger nDocumentFlag;
    private Integer nSaleRateFlag;
    private byte nSellerFlag;
    private BigInteger nCustomerMappingFlag;
    private String cSoftwareName;
    private Integer nLoMstSyncFlag;
    private Integer nLoOrderSyncFlag;
    private BigInteger nExeDownloadApproveFlag;
    private BigInteger nItemDivisonFilterFlag;
    private String cAdd1;
    private String cAdd2;
    private String cAdd3;
    private PincodeMstEntity pinCodeObj;

    @ManyToOne
    @JoinColumns ({
            @JoinColumn(name = "c_code", insertable = false, updatable = false),
            @JoinColumn(name = "c_c2code", insertable = false, updatable = false)
    })
    public PincodeMstEntity getPinCodeObj() {
        return pinCodeObj;
    }

    public void setPinCodeObj(PincodeMstEntity pinCodeObj) {
        this.pinCodeObj = pinCodeObj;
    }


    @Id
    @Column(name = "c_code", nullable = false, length = 20)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    @Basic
    @Column(name = "c_name", nullable = true, length = 50)
    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    @Basic
    @Column(name = "c_sh_name", nullable = true, length = 40)
    public String getcShName() {
        return cShName;
    }

    public void setcShName(String cShName) {
        this.cShName = cShName;
    }

    @Basic
    @Column(name = "c_grp_no", nullable = true, length = 40)
    public String getcGrpNo() {
        return cGrpNo;
    }

    public void setcGrpNo(String cGrpNo) {
        this.cGrpNo = cGrpNo;
    }

    @Basic
    @Column(name = "n_type", nullable = true)
    public Integer getnType() {
        return nType;
    }

    public void setnType(Integer nType) {
        this.nType = nType;
    }

    @Basic
    @Column(name = "c_desc", nullable = true, length = 50)
    public String getcDesc() {
        return cDesc;
    }

    public void setcDesc(String cDesc) {
        this.cDesc = cDesc;
    }

    @Basic
    @Column(name = "c_contact_person", nullable = true, length = 50)
    public String getcContactPerson() {
        return cContactPerson;
    }

    public void setcContactPerson(String cContactPerson) {
        this.cContactPerson = cContactPerson;
    }

    @Basic
    @Column(name = "c_add_1", nullable = true, length = 150)
    public String getcAdd1() {
        return cAdd1;
    }

    public void setcAdd1(String cAdd1) {
        this.cAdd1 = cAdd1;
    }

    @Basic
    @Column(name = "c_add_2", nullable = true, length = 150)
    public String getcAdd2() {
        return cAdd2;
    }

    public void setcAdd2(String cAdd2) {
        this.cAdd2 = cAdd2;
    }

    @Basic
    @Column(name = "c_add_3", nullable = true, length = 150)
    public String getcAdd3() {
        return cAdd3;
    }

    public void setcAdd3(String cAdd3) {
        this.cAdd3 = cAdd3;
    }

    @Basic
    @Column(name = "c_city", nullable = true, length = 50)
    public String getcCity() {
        return cCity;
    }

    public void setcCity(String cCity) {
        this.cCity = cCity;
    }

    @Basic
    @Column(name = "c_pin", nullable = true, length = 20)
    public String getcPin() {
        return cPin;
    }

    public void setcPin(String cPin) {
        this.cPin = cPin;
    }

    @Basic
    @Column(name = "c_phone_1", nullable = true, length = 20)
    public String getcPhone1() {
        return cPhone1;
    }

    public void setcPhone1(String cPhone1) {
        this.cPhone1 = cPhone1;
    }

    @Basic
    @Column(name = "c_phone_2", nullable = true, length = 20)
    public String getcPhone2() {
        return cPhone2;
    }

    public void setcPhone2(String cPhone2) {
        this.cPhone2 = cPhone2;
    }

    @Basic
    @Column(name = "c_fax", nullable = true, length = 20)
    public String getcFax() {
        return cFax;
    }

    public void setcFax(String cFax) {
        this.cFax = cFax;
    }

    @Basic
    @Column(name = "c_mobile", nullable = true, length = 20)
    public String getcMobile() {
        return cMobile;
    }

    public void setcMobile(String cMobile) {
        this.cMobile = cMobile;
    }

    @Basic
    @Column(name = "c_email", nullable = true, length = 150)
    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    @Basic
    @Column(name = "c_area_code", nullable = true, length = 20)
    public String getcAreaCode() {
        return cAreaCode;
    }

    public void setcAreaCode(String cAreaCode) {
        this.cAreaCode = cAreaCode;
    }

    @Basic
    @Column(name = "c_mr_code", nullable = true, length = 20)
    public String getcMrCode() {
        return cMrCode;
    }

    public void setcMrCode(String cMrCode) {
        this.cMrCode = cMrCode;
    }

    @Basic
    @Column(name = "c_cust_category_code", nullable = true, length = 20)
    public String getcCustCategoryCode() {
        return cCustCategoryCode;
    }

    public void setcCustCategoryCode(String cCustCategoryCode) {
        this.cCustCategoryCode = cCustCategoryCode;
    }

    @Basic
    @Column(name = "c_branch_code", nullable = true, length = 20)
    public String getcBranchCode() {
        return cBranchCode;
    }

    public void setcBranchCode(String cBranchCode) {
        this.cBranchCode = cBranchCode;
    }

    @Basic
    @Column(name = "n_discount", nullable = true, precision = 2)
    public BigDecimal getnDiscount() {
        return nDiscount;
    }

    public void setnDiscount(BigDecimal nDiscount) {
        this.nDiscount = nDiscount;
    }

    @Basic
    @Column(name = "n_lock", nullable = true)
    public Integer getnLock() {
        return nLock;
    }

    public void setnLock(Integer nLock) {
        this.nLock = nLock;
    }

    @Basic
    @Column(name = "c_drug_licence_no_1", nullable = true, length = 20)
    public String getcDrugLicenceNo1() {
        return cDrugLicenceNo1;
    }

    public void setcDrugLicenceNo1(String cDrugLicenceNo1) {
        this.cDrugLicenceNo1 = cDrugLicenceNo1;
    }

    @Basic
    @Column(name = "c_drug_licence_no_2", nullable = true, length = 20)
    public String getcDrugLicenceNo2() {
        return cDrugLicenceNo2;
    }

    public void setcDrugLicenceNo2(String cDrugLicenceNo2) {
        this.cDrugLicenceNo2 = cDrugLicenceNo2;
    }

    @Basic
    @Column(name = "c_st_no", nullable = true, length = 20)
    public String getcStNo() {
        return cStNo;
    }

    public void setcStNo(String cStNo) {
        this.cStNo = cStNo;
    }

    @Basic
    @Column(name = "c_cst_no", nullable = true, length = 20)
    public String getcCstNo() {
        return cCstNo;
    }

    public void setcCstNo(String cCstNo) {
        this.cCstNo = cCstNo;
    }

    @Basic
    @Column(name = "d_date", nullable = true)
    public Timestamp getdDate() {
        return dDate;
    }

    public void setdDate(Timestamp dDate) {
        this.dDate = dDate;
    }

    @Basic
    @Column(name = "c_remark", nullable = true, length = 120)
    public String getcRemark() {
        return cRemark;
    }

    public void setcRemark(String cRemark) {
        this.cRemark = cRemark;
    }

    @Basic
    @Column(name = "n_credit_limit", nullable = true, precision = 2)
    public BigDecimal getnCreditLimit() {
        return nCreditLimit;
    }

    public void setnCreditLimit(BigDecimal nCreditLimit) {
        this.nCreditLimit = nCreditLimit;
    }

    @Basic
    @Column(name = "n_credit_days", nullable = true, precision = 0)
    public Integer getnCreditDays() {
        return nCreditDays;
    }

    public void setnCreditDays(Integer nCreditDays) {
        this.nCreditDays = nCreditDays;
    }

    @Basic
    @Column(name = "n_debit_days", nullable = true, precision = 0)
    public Integer getnDebitDays() {
        return nDebitDays;
    }

    public void setnDebitDays(Integer nDebitDays) {
        this.nDebitDays = nDebitDays;
    }

    @Basic
    @Column(name = "n_debit_limit", nullable = true, precision = 2)
    public BigDecimal getnDebitLimit() {
        return nDebitLimit;
    }

    public void setnDebitLimit(BigDecimal nDebitLimit) {
        this.nDebitLimit = nDebitLimit;
    }

    @Basic
    @Column(name = "n_flag", nullable = true)
    public Integer getnFlag() {
        return nFlag;
    }

    public void setnFlag(Integer nFlag) {
        this.nFlag = nFlag;
    }

    @Basic
    @Column(name = "n_order_days", nullable = true, precision = 0)
    public Integer getnOrderDays() {
        return nOrderDays;
    }

    public void setnOrderDays(Integer nOrderDays) {
        this.nOrderDays = nOrderDays;
    }

    @Basic
    @Column(name = "d_ldate", nullable = true)
    public Timestamp getdLdate() {
        return dLdate;
    }

    public void setdLdate(Timestamp dLdate) {
        this.dLdate = dLdate;
    }

    @Basic
    @Column(name = "d_adate", nullable = true)
    public Timestamp getdAdate() {
        return dAdate;
    }

    public void setdAdate(Timestamp dAdate) {
        this.dAdate = dAdate;
    }

    @Basic
    @Column(name = "c_createuser", nullable = true, length = 20)
    public String getcCreateuser() {
        return cCreateuser;
    }

    public void setcCreateuser(String cCreateuser) {
        this.cCreateuser = cCreateuser;
    }

    @Basic
    @Column(name = "c_tax_type_code", nullable = true, length = 20)
    public String getcTaxTypeCode() {
        return cTaxTypeCode;
    }

    public void setcTaxTypeCode(String cTaxTypeCode) {
        this.cTaxTypeCode = cTaxTypeCode;
    }

    @Basic
    @Column(name = "n_points", nullable = true, precision = 2)
    public BigDecimal getnPoints() {
        return nPoints;
    }

    public void setnPoints(BigDecimal nPoints) {
        this.nPoints = nPoints;
    }

    @Basic
    @Column(name = "c_card_no", nullable = true, length = 20)
    public String getcCardNo() {
        return cCardNo;
    }

    public void setcCardNo(String cCardNo) {
        this.cCardNo = cCardNo;
    }

    @Basic
    @Column(name = "n_card_value", nullable = true, precision = 2)
    public BigDecimal getnCardValue() {
        return nCardValue;
    }

    public void setnCardValue(BigDecimal nCardValue) {
        this.nCardValue = nCardValue;
    }

    @Basic
    @Column(name = "n_rate_on", nullable = true)
    public Integer getnRateOn() {
        return nRateOn;
    }

    public void setnRateOn(Integer nRateOn) {
        this.nRateOn = nRateOn;
    }

    @Basic
    @Column(name = "c_discount_slab_code", nullable = true, length = 20)
    public String getcDiscountSlabCode() {
        return cDiscountSlabCode;
    }

    public void setcDiscountSlabCode(String cDiscountSlabCode) {
        this.cDiscountSlabCode = cDiscountSlabCode;
    }

    @Basic
    @Column(name = "n_shift", nullable = true)
    public Integer getnShift() {
        return nShift;
    }

    public void setnShift(Integer nShift) {
        this.nShift = nShift;
    }

    @Basic
    @Column(name = "n_point_factor", nullable = true, precision = 2)
    public BigDecimal getnPointFactor() {
        return nPointFactor;
    }

    public void setnPointFactor(BigDecimal nPointFactor) {
        this.nPointFactor = nPointFactor;
    }

    @Basic
    @Column(name = "t_ltime", nullable = true)
    public Timestamp gettLtime() {
        return tLtime;
    }

    public void settLtime(Timestamp tLtime) {
        this.tLtime = tLtime;
    }

    @Basic
    @Column(name = "d_activated_date", nullable = true)
    public Timestamp getdActivatedDate() {
        return dActivatedDate;
    }

    public void setdActivatedDate(Timestamp dActivatedDate) {
        this.dActivatedDate = dActivatedDate;
    }

    @Basic
    @Column(name = "d_deactivated_date", nullable = true)
    public Timestamp getdDeactivatedDate() {
        return dDeactivatedDate;
    }

    public void setdDeactivatedDate(Timestamp dDeactivatedDate) {
        this.dDeactivatedDate = dDeactivatedDate;
    }

    @Basic
    @Column(name = "n_card_reverse_dis", nullable = true, precision = 2)
    public BigDecimal getnCardReverseDis() {
        return nCardReverseDis;
    }

    public void setnCardReverseDis(BigDecimal nCardReverseDis) {
        this.nCardReverseDis = nCardReverseDis;
    }

    @Basic
    @Column(name = "n_roaming", nullable = true)
    public Integer getnRoaming() {
        return nRoaming;
    }

    public void setnRoaming(Integer nRoaming) {
        this.nRoaming = nRoaming;
    }

    @Basic
    @Column(name = "d_dl_expiry_date", nullable = true)
    public Timestamp getdDlExpiryDate() {
        return dDlExpiryDate;
    }

    public void setdDlExpiryDate(Timestamp dDlExpiryDate) {
        this.dDlExpiryDate = dDlExpiryDate;
    }

    @Basic
    @Column(name = "n_admin_reconcile", nullable = true)
    public Integer getnAdminReconcile() {
        return nAdminReconcile;
    }

    public void setnAdminReconcile(Integer nAdminReconcile) {
        this.nAdminReconcile = nAdminReconcile;
    }

    @Basic
    @Column(name = "c_drug_licence_no_3", nullable = true, length = 20)
    public String getcDrugLicenceNo3() {
        return cDrugLicenceNo3;
    }

    public void setcDrugLicenceNo3(String cDrugLicenceNo3) {
        this.cDrugLicenceNo3 = cDrugLicenceNo3;
    }

    @Basic
    @Column(name = "c_tallyhname", nullable = true, length = 40)
    public String getcTallyhname() {
        return cTallyhname;
    }

    public void setcTallyhname(String cTallyhname) {
        this.cTallyhname = cTallyhname;
    }

    @Basic
    @Column(name = "c_tallygname", nullable = true, length = 40)
    public String getcTallygname() {
        return cTallygname;
    }

    public void setcTallygname(String cTallygname) {
        this.cTallygname = cTallygname;
    }

    @Basic
    @Column(name = "c_geo_lat", nullable = true, length = 50)
    public String getcGeoLat() {
        return cGeoLat;
    }

    public void setcGeoLat(String cGeoLat) {
        this.cGeoLat = cGeoLat;
    }

    @Basic
    @Column(name = "c_geo_lon", nullable = true, length = 50)
    public String getcGeoLon() {
        return cGeoLon;
    }

    public void setcGeoLon(String cGeoLon) {
        this.cGeoLon = cGeoLon;
    }

    @Basic
    @Column(name = "n_inter_pur", nullable = true)
    public Integer getnInterPur() {
        return nInterPur;
    }

    public void setnInterPur(Integer nInterPur) {
        this.nInterPur = nInterPur;
    }

    @Basic
    @Column(name = "c_print_name", nullable = true, length = 40)
    public String getcPrintName() {
        return cPrintName;
    }

    public void setcPrintName(String cPrintName) {
        this.cPrintName = cPrintName;
    }

    @Basic
    @Column(name = "c_pan_no", nullable = true, length = 20)
    public String getcPanNo() {
        return cPanNo;
    }

    public void setcPanNo(String cPanNo) {
        this.cPanNo = cPanNo;
    }

    @Basic
    @Column(name = "c_tan_no", nullable = true, length = 20)
    public String getcTanNo() {
        return cTanNo;
    }

    public void setcTanNo(String cTanNo) {
        this.cTanNo = cTanNo;
    }

    @Basic
    @Column(name = "n_enable_live_order", nullable = true)
    public Integer getnEnableLiveOrder() {
        return nEnableLiveOrder;
    }

    public void setnEnableLiveOrder(Integer nEnableLiveOrder) {
        this.nEnableLiveOrder = nEnableLiveOrder;
    }

    @Basic
    @Column(name = "n_autolock", nullable = true)
    public Integer getnAutolock() {
        return nAutolock;
    }

    public void setnAutolock(Integer nAutolock) {
        this.nAutolock = nAutolock;
    }

    @Basic
    @Column(name = "n_grace_days", nullable = true)
    public Integer getnGraceDays() {
        return nGraceDays;
    }

    public void setnGraceDays(Integer nGraceDays) {
        this.nGraceDays = nGraceDays;
    }

    @Basic
    @Column(name = "n_grace_limit", nullable = true, precision = 2)
    public BigDecimal getnGraceLimit() {
        return nGraceLimit;
    }

    public void setnGraceLimit(BigDecimal nGraceLimit) {
        this.nGraceLimit = nGraceLimit;
    }

    @Basic
    @Column(name = "n_max_chq_bounce", nullable = true)
    public Integer getnMaxChqBounce() {
        return nMaxChqBounce;
    }

    public void setnMaxChqBounce(Integer nMaxChqBounce) {
        this.nMaxChqBounce = nMaxChqBounce;
    }

    @Basic
    @Column(name = "n_admin_settlement", nullable = true)
    public Integer getnAdminSettlement() {
        return nAdminSettlement;
    }

    public void setnAdminSettlement(Integer nAdminSettlement) {
        this.nAdminSettlement = nAdminSettlement;
    }

    @Basic
    @Column(name = "c_web", nullable = true, length = 40)
    public String getcWeb() {
        return cWeb;
    }

    public void setcWeb(String cWeb) {
        this.cWeb = cWeb;
    }

    @Basic
    @Column(name = "n_rate_inc_vat", nullable = true)
    public Integer getnRateIncVat() {
        return nRateIncVat;
    }

    public void setnRateIncVat(Integer nRateIncVat) {
        this.nRateIncVat = nRateIncVat;
    }

    @Basic
    @Column(name = "n_road_permit_flag", nullable = true)
    public Integer getnRoadPermitFlag() {
        return nRoadPermitFlag;
    }

    public void setnRoadPermitFlag(Integer nRoadPermitFlag) {
        this.nRoadPermitFlag = nRoadPermitFlag;
    }

    @Basic
    @Column(name = "c_ord_inv_term_code", nullable = true, length = 20)
    public String getcOrdInvTermCode() {
        return cOrdInvTermCode;
    }

    public void setcOrdInvTermCode(String cOrdInvTermCode) {
        this.cOrdInvTermCode = cOrdInvTermCode;
    }

    @Basic
    @Column(name = "c_transport_code", nullable = true, length = 20)
    public String getcTransportCode() {
        return cTransportCode;
    }

    public void setcTransportCode(String cTransportCode) {
        this.cTransportCode = cTransportCode;
    }

    @Basic
    @Column(name = "c_tin_no", nullable = true, length = 20)
    public String getcTinNo() {
        return cTinNo;
    }

    public void setcTinNo(String cTinNo) {
        this.cTinNo = cTinNo;
    }

    @Basic
    @Column(name = "c_excise_no", nullable = true, length = 20)
    public String getcExciseNo() {
        return cExciseNo;
    }

    public void setcExciseNo(String cExciseNo) {
        this.cExciseNo = cExciseNo;
    }

    @Basic
    @Column(name = "c_bank_code", nullable = true, length = 20)
    public String getcBankCode() {
        return cBankCode;
    }

    public void setcBankCode(String cBankCode) {
        this.cBankCode = cBankCode;
    }

    @Basic
    @Column(name = "c_ifsc_code", nullable = true, length = 20)
    public String getcIfscCode() {
        return cIfscCode;
    }

    public void setcIfscCode(String cIfscCode) {
        this.cIfscCode = cIfscCode;
    }

    @Basic
    @Column(name = "c_micr_code", nullable = true, length = 20)
    public String getcMicrCode() {
        return cMicrCode;
    }

    public void setcMicrCode(String cMicrCode) {
        this.cMicrCode = cMicrCode;
    }

    @Basic
    @Column(name = "c_bank_act_no", nullable = true, length = 30)
    public String getcBankActNo() {
        return cBankActNo;
    }

    public void setcBankActNo(String cBankActNo) {
        this.cBankActNo = cBankActNo;
    }

    @Basic
    @Column(name = "c_cor_cont_person", nullable = true, length = 20)
    public String getcCorContPerson() {
        return cCorContPerson;
    }

    public void setcCorContPerson(String cCorContPerson) {
        this.cCorContPerson = cCorContPerson;
    }

    @Basic
    @Column(name = "c_cor_ph_no", nullable = true, length = 20)
    public String getcCorPhNo() {
        return cCorPhNo;
    }

    public void setcCorPhNo(String cCorPhNo) {
        this.cCorPhNo = cCorPhNo;
    }

    @Basic
    @Column(name = "n_max_line_item", nullable = true, length = 120)
    public String getnMaxLineItem() {
        return nMaxLineItem;
    }

    public void setnMaxLineItem(String nMaxLineItem) {
        this.nMaxLineItem = nMaxLineItem;
    }

    @Basic
    @Column(name = "n_disc_flag", nullable = true)
    public Integer getnDiscFlag() {
        return nDiscFlag;
    }

    public void setnDiscFlag(Integer nDiscFlag) {
        this.nDiscFlag = nDiscFlag;
    }

    @Basic
    @Column(name = "n_chq_print_enable", nullable = true)
    public Integer getnChqPrintEnable() {
        return nChqPrintEnable;
    }

    public void setnChqPrintEnable(Integer nChqPrintEnable) {
        this.nChqPrintEnable = nChqPrintEnable;
    }

    @Basic
    @Column(name = "c_modiuser", nullable = true, length = 20)
    public String getcModiuser() {
        return cModiuser;
    }

    public void setcModiuser(String cModiuser) {
        this.cModiuser = cModiuser;
    }

    @Basic
    @Column(name = "c_bldg_landmark_code", nullable = true, length = 20)
    public String getcBldgLandmarkCode() {
        return cBldgLandmarkCode;
    }

    public void setcBldgLandmarkCode(String cBldgLandmarkCode) {
        this.cBldgLandmarkCode = cBldgLandmarkCode;
    }

    @Basic
    @Column(name = "c_bldg_landmark_code_c", nullable = true, length = 20)
    public String getcBldgLandmarkCodeC() {
        return cBldgLandmarkCodeC;
    }

    public void setcBldgLandmarkCodeC(String cBldgLandmarkCodeC) {
        this.cBldgLandmarkCodeC = cBldgLandmarkCodeC;
    }

    @Basic
    @Column(name = "c_cin_no", nullable = true, length = 20)
    public String getcCinNo() {
        return cCinNo;
    }

    public void setcCinNo(String cCinNo) {
        this.cCinNo = cCinNo;
    }

    @Basic
    @Column(name = "c_currency_code", nullable = true, length = 20)
    public String getcCurrencyCode() {
        return cCurrencyCode;
    }

    public void setcCurrencyCode(String cCurrencyCode) {
        this.cCurrencyCode = cCurrencyCode;
    }

    @Basic
    @Column(name = "c_website", nullable = true, length = 50)
    public String getcWebsite() {
        return cWebsite;
    }

    public void setcWebsite(String cWebsite) {
        this.cWebsite = cWebsite;
    }

    @Basic
    @Column(name = "c_pwd", nullable = true, length = 20)
    public String getcPwd() {
        return cPwd;
    }

    public void setcPwd(String cPwd) {
        this.cPwd = cPwd;
    }

    @Basic
    @Column(name = "n_so_conversion_type", nullable = true)
    public Integer getnSoConversionType() {
        return nSoConversionType;
    }

    public void setnSoConversionType(Integer nSoConversionType) {
        this.nSoConversionType = nSoConversionType;
    }

    @Basic
    @Column(name = "n_dc_print_rate", nullable = true)
    public Integer getnDcPrintRate() {
        return nDcPrintRate;
    }

    public void setnDcPrintRate(Integer nDcPrintRate) {
        this.nDcPrintRate = nDcPrintRate;
    }

    @Basic
    @Column(name = "n_tax_print", nullable = true)
    public Integer getnTaxPrint() {
        return nTaxPrint;
    }

    public void setnTaxPrint(Integer nTaxPrint) {
        this.nTaxPrint = nTaxPrint;
    }

    @Basic
    @Column(name = "n_search_item", nullable = true)
    public Integer getnSearchItem() {
        return nSearchItem;
    }

    public void setnSearchItem(Integer nSearchItem) {
        this.nSearchItem = nSearchItem;
    }

    @Basic
    @Column(name = "n_spl_rate_flag", nullable = true)
    public Integer getnSplRateFlag() {
        return nSplRateFlag;
    }

    public void setnSplRateFlag(Integer nSplRateFlag) {
        this.nSplRateFlag = nSplRateFlag;
    }

    @Basic
    @Column(name = "c_common_path", nullable = true, length = 20)
    public String getcCommonPath() {
        return cCommonPath;
    }

    public void setcCommonPath(String cCommonPath) {
        this.cCommonPath = cCommonPath;
    }

    @Basic
    @Column(name = "n_duedate_based_on", nullable = true)
    public Integer getnDuedateBasedOn() {
        return nDuedateBasedOn;
    }

    public void setnDuedateBasedOn(Integer nDuedateBasedOn) {
        this.nDuedateBasedOn = nDuedateBasedOn;
    }

    @Basic
    @Column(name = "n_duedate_cal_days", nullable = true)
    public Integer getnDuedateCalDays() {
        return nDuedateCalDays;
    }

    public void setnDuedateCalDays(Integer nDuedateCalDays) {
        this.nDuedateCalDays = nDuedateCalDays;
    }

    @Basic
    @Column(name = "n_duedate_cal_method", nullable = true)
    public Integer getnDuedateCalMethod() {
        return nDuedateCalMethod;
    }

    public void setnDuedateCalMethod(Integer nDuedateCalMethod) {
        this.nDuedateCalMethod = nDuedateCalMethod;
    }

    @Basic
    @Column(name = "n_online_pay", nullable = true)
    public Integer getnOnlinePay() {
        return nOnlinePay;
    }

    public void setnOnlinePay(Integer nOnlinePay) {
        this.nOnlinePay = nOnlinePay;
    }

    @Basic
    @Column(name = "c_parent_act_code", nullable = true, length = 20)
    public String getcParentActCode() {
        return cParentActCode;
    }

    public void setcParentActCode(String cParentActCode) {
        this.cParentActCode = cParentActCode;
    }

    @Basic
    @Column(name = "c_import_br_code", nullable = true, length = 20)
    public String getcImportBrCode() {
        return cImportBrCode;
    }

    public void setcImportBrCode(String cImportBrCode) {
        this.cImportBrCode = cImportBrCode;
    }

    @Basic
    @Column(name = "c_import_trader_code", nullable = true, length = 20)
    public String getcImportTraderCode() {
        return cImportTraderCode;
    }

    public void setcImportTraderCode(String cImportTraderCode) {
        this.cImportTraderCode = cImportTraderCode;
    }

    @Basic
    @Column(name = "n_exp_days", nullable = true)
    public Integer getnExpDays() {
        return nExpDays;
    }

    public void setnExpDays(Integer nExpDays) {
        this.nExpDays = nExpDays;
    }

    @Basic
    @Column(name = "n_update_stock", nullable = true)
    public Integer getnUpdateStock() {
        return nUpdateStock;
    }

    public void setnUpdateStock(Integer nUpdateStock) {
        this.nUpdateStock = nUpdateStock;
    }

    @Basic
    @Column(name = "n_service_charge_incl_excl", nullable = true)
    public Integer getnServiceChargeInclExcl() {
        return nServiceChargeInclExcl;
    }

    public void setnServiceChargeInclExcl(Integer nServiceChargeInclExcl) {
        this.nServiceChargeInclExcl = nServiceChargeInclExcl;
    }

    @Basic
    @Column(name = "n_so_dc_process", nullable = true)
    public Integer getnSoDcProcess() {
        return nSoDcProcess;
    }

    public void setnSoDcProcess(Integer nSoDcProcess) {
        this.nSoDcProcess = nSoDcProcess;
    }

    @Basic
    @Column(name = "n_no_of_copies", nullable = true)
    public Integer getnNoOfCopies() {
        return nNoOfCopies;
    }

    public void setnNoOfCopies(Integer nNoOfCopies) {
        this.nNoOfCopies = nNoOfCopies;
    }

    @Basic
    @Column(name = "c_emp_code", nullable = true, length = 20)
    public String getcEmpCode() {
        return cEmpCode;
    }

    public void setcEmpCode(String cEmpCode) {
        this.cEmpCode = cEmpCode;
    }

    @Basic
    @Column(name = "n_min_stock_day", nullable = true)
    public Integer getnMinStockDay() {
        return nMinStockDay;
    }

    public void setnMinStockDay(Integer nMinStockDay) {
        this.nMinStockDay = nMinStockDay;
    }

    @Basic
    @Column(name = "c_provisional_gstn_no", nullable = true, length = 20)
    public String getcProvisionalGstnNo() {
        return cProvisionalGstnNo;
    }

    public void setcProvisionalGstnNo(String cProvisionalGstnNo) {
        this.cProvisionalGstnNo = cProvisionalGstnNo;
    }

    @Basic
    @Column(name = "c_permanent_gstn_no", nullable = true, length = 20)
    public String getcPermanentGstnNo() {
        return cPermanentGstnNo;
    }

    public void setcPermanentGstnNo(String cPermanentGstnNo) {
        this.cPermanentGstnNo = cPermanentGstnNo;
    }

    @Basic
    @Column(name = "n_seperate_updn", nullable = true)
    public Integer getnSeperateUpdn() {
        return nSeperateUpdn;
    }

    public void setnSeperateUpdn(Integer nSeperateUpdn) {
        this.nSeperateUpdn = nSeperateUpdn;
    }

    @Basic
    @Column(name = "c_edi_code", nullable = true, length = 20)
    public String getcEdiCode() {
        return cEdiCode;
    }

    public void setcEdiCode(String cEdiCode) {
        this.cEdiCode = cEdiCode;
    }

    @Basic
    @Column(name = "d_stock_ldate", nullable = true, length = 45)
    public String getdStockLdate() {
        return dStockLdate;
    }

    public void setdStockLdate(String dStockLdate) {
        this.dStockLdate = dStockLdate;
    }

    @Basic
    @Column(name = "c_slug_name", nullable = true, length = 100)
    public String getcSlugName() {
        return cSlugName;
    }

    public void setcSlugName(String cSlugName) {
        this.cSlugName = cSlugName;
    }

    @Basic
    @Column(name = "c_cust_branch_code", nullable = true, length = 10)
    public String getcCustBranchCode() {
        return cCustBranchCode;
    }

    public void setcCustBranchCode(String cCustBranchCode) {
        this.cCustBranchCode = cCustBranchCode;
    }

    @Basic
    @Column(name = "n_order_flag", nullable = false, precision = 0)
    public BigInteger getnOrderFlag() {
        return nOrderFlag;
    }

    public void setnOrderFlag(BigInteger nOrderFlag) {
        this.nOrderFlag = nOrderFlag;
    }

    @Basic
    @Column(name = "n_document_flag", nullable = false, precision = 0)
    public BigInteger getnDocumentFlag() {
        return nDocumentFlag;
    }

    public void setnDocumentFlag(BigInteger nDocumentFlag) {
        this.nDocumentFlag = nDocumentFlag;
    }

    @Basic
    @Column(name = "n_sale_rate_flag", nullable = true)
    public Integer getnSaleRateFlag() {
        return nSaleRateFlag;
    }

    public void setnSaleRateFlag(Integer nSaleRateFlag) {
        this.nSaleRateFlag = nSaleRateFlag;
    }

    @Basic
    @Column(name = "n_seller_flag", nullable = false)
    public byte getnSellerFlag() {
        return nSellerFlag;
    }

    public void setnSellerFlag(byte nSellerFlag) {
        this.nSellerFlag = nSellerFlag;
    }

    @Basic
    @Column(name = "n_customer_mapping_flag", nullable = true, precision = 0)
    public BigInteger getnCustomerMappingFlag() {
        return nCustomerMappingFlag;
    }

    public void setnCustomerMappingFlag(BigInteger nCustomerMappingFlag) {
        this.nCustomerMappingFlag = nCustomerMappingFlag;
    }

    @Basic
    @Column(name = "c_software_name", nullable = true, length = 100)
    public String getcSoftwareName() {
        return cSoftwareName;
    }

    public void setcSoftwareName(String cSoftwareName) {
        this.cSoftwareName = cSoftwareName;
    }

    @Basic
    @Column(name = "n_lo_mst_sync_flag", nullable = true)
    public Integer getnLoMstSyncFlag() {
        return nLoMstSyncFlag;
    }

    public void setnLoMstSyncFlag(Integer nLoMstSyncFlag) {
        this.nLoMstSyncFlag = nLoMstSyncFlag;
    }

    @Basic
    @Column(name = "n_lo_order_sync_flag", nullable = true)
    public Integer getnLoOrderSyncFlag() {
        return nLoOrderSyncFlag;
    }

    public void setnLoOrderSyncFlag(Integer nLoOrderSyncFlag) {
        this.nLoOrderSyncFlag = nLoOrderSyncFlag;
    }

    @Basic
    @Column(name = "n_exe_download_approve_flag", nullable = true, precision = 0)
    public BigInteger getnExeDownloadApproveFlag() {
        return nExeDownloadApproveFlag;
    }

    public void setnExeDownloadApproveFlag(BigInteger nExeDownloadApproveFlag) {
        this.nExeDownloadApproveFlag = nExeDownloadApproveFlag;
    }

    @Basic
    @Column(name = "n_item_divison_filter_flag", nullable = true, precision = 0)
    public BigInteger getnItemDivisonFilterFlag() {
        return nItemDivisonFilterFlag;
    }

    public void setnItemDivisonFilterFlag(BigInteger nItemDivisonFilterFlag) {
        this.nItemDivisonFilterFlag = nItemDivisonFilterFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LcC2CodeMstEntity that = (LcC2CodeMstEntity) o;
        return nSellerFlag == that.nSellerFlag &&
                Objects.equals(cCode, that.cCode) &&
                Objects.equals(cName, that.cName) &&
                Objects.equals(cShName, that.cShName) &&
                Objects.equals(cGrpNo, that.cGrpNo) &&
                Objects.equals(nType, that.nType) &&
                Objects.equals(cDesc, that.cDesc) &&
                Objects.equals(cContactPerson, that.cContactPerson) &&
                Objects.equals(cAdd1, that.cAdd1) &&
                Objects.equals(cAdd2, that.cAdd2) &&
                Objects.equals(cAdd3, that.cAdd3) &&
                Objects.equals(cCity, that.cCity) &&
                Objects.equals(cPin, that.cPin) &&
                Objects.equals(cPhone1, that.cPhone1) &&
                Objects.equals(cPhone2, that.cPhone2) &&
                Objects.equals(cFax, that.cFax) &&
                Objects.equals(cMobile, that.cMobile) &&
                Objects.equals(cEmail, that.cEmail) &&
                Objects.equals(cAreaCode, that.cAreaCode) &&
                Objects.equals(cMrCode, that.cMrCode) &&
                Objects.equals(cCustCategoryCode, that.cCustCategoryCode) &&
                Objects.equals(cBranchCode, that.cBranchCode) &&
                Objects.equals(nDiscount, that.nDiscount) &&
                Objects.equals(nLock, that.nLock) &&
                Objects.equals(cDrugLicenceNo1, that.cDrugLicenceNo1) &&
                Objects.equals(cDrugLicenceNo2, that.cDrugLicenceNo2) &&
                Objects.equals(cStNo, that.cStNo) &&
                Objects.equals(cCstNo, that.cCstNo) &&
                Objects.equals(dDate, that.dDate) &&
                Objects.equals(cRemark, that.cRemark) &&
                Objects.equals(nCreditLimit, that.nCreditLimit) &&
                Objects.equals(nCreditDays, that.nCreditDays) &&
                Objects.equals(nDebitDays, that.nDebitDays) &&
                Objects.equals(nDebitLimit, that.nDebitLimit) &&
                Objects.equals(nFlag, that.nFlag) &&
                Objects.equals(nOrderDays, that.nOrderDays) &&
                Objects.equals(dLdate, that.dLdate) &&
                Objects.equals(dAdate, that.dAdate) &&
                Objects.equals(cCreateuser, that.cCreateuser) &&
                Objects.equals(cTaxTypeCode, that.cTaxTypeCode) &&
                Objects.equals(nPoints, that.nPoints) &&
                Objects.equals(cCardNo, that.cCardNo) &&
                Objects.equals(nCardValue, that.nCardValue) &&
                Objects.equals(nRateOn, that.nRateOn) &&
                Objects.equals(cDiscountSlabCode, that.cDiscountSlabCode) &&
                Objects.equals(nShift, that.nShift) &&
                Objects.equals(nPointFactor, that.nPointFactor) &&
                Objects.equals(tLtime, that.tLtime) &&
                Objects.equals(dActivatedDate, that.dActivatedDate) &&
                Objects.equals(dDeactivatedDate, that.dDeactivatedDate) &&
                Objects.equals(nCardReverseDis, that.nCardReverseDis) &&
                Objects.equals(nRoaming, that.nRoaming) &&
                Objects.equals(dDlExpiryDate, that.dDlExpiryDate) &&
                Objects.equals(nAdminReconcile, that.nAdminReconcile) &&
                Objects.equals(cDrugLicenceNo3, that.cDrugLicenceNo3) &&
                Objects.equals(cTallyhname, that.cTallyhname) &&
                Objects.equals(cTallygname, that.cTallygname) &&
                Objects.equals(cGeoLat, that.cGeoLat) &&
                Objects.equals(cGeoLon, that.cGeoLon) &&
                Objects.equals(nInterPur, that.nInterPur) &&
                Objects.equals(cPrintName, that.cPrintName) &&
                Objects.equals(cPanNo, that.cPanNo) &&
                Objects.equals(cTanNo, that.cTanNo) &&
                Objects.equals(nEnableLiveOrder, that.nEnableLiveOrder) &&
                Objects.equals(nAutolock, that.nAutolock) &&
                Objects.equals(nGraceDays, that.nGraceDays) &&
                Objects.equals(nGraceLimit, that.nGraceLimit) &&
                Objects.equals(nMaxChqBounce, that.nMaxChqBounce) &&
                Objects.equals(nAdminSettlement, that.nAdminSettlement) &&
                Objects.equals(cWeb, that.cWeb) &&
                Objects.equals(nRateIncVat, that.nRateIncVat) &&
                Objects.equals(nRoadPermitFlag, that.nRoadPermitFlag) &&
                Objects.equals(cOrdInvTermCode, that.cOrdInvTermCode) &&
                Objects.equals(cTransportCode, that.cTransportCode) &&
                Objects.equals(cTinNo, that.cTinNo) &&
                Objects.equals(cExciseNo, that.cExciseNo) &&
                Objects.equals(cBankCode, that.cBankCode) &&
                Objects.equals(cIfscCode, that.cIfscCode) &&
                Objects.equals(cMicrCode, that.cMicrCode) &&
                Objects.equals(cBankActNo, that.cBankActNo) &&
                Objects.equals(cCorContPerson, that.cCorContPerson) &&
                Objects.equals(cCorPhNo, that.cCorPhNo) &&
                Objects.equals(nMaxLineItem, that.nMaxLineItem) &&
                Objects.equals(nDiscFlag, that.nDiscFlag) &&
                Objects.equals(nChqPrintEnable, that.nChqPrintEnable) &&
                Objects.equals(cModiuser, that.cModiuser) &&
                Objects.equals(cBldgLandmarkCode, that.cBldgLandmarkCode) &&
                Objects.equals(cBldgLandmarkCodeC, that.cBldgLandmarkCodeC) &&
                Objects.equals(cCinNo, that.cCinNo) &&
                Objects.equals(cCurrencyCode, that.cCurrencyCode) &&
                Objects.equals(cWebsite, that.cWebsite) &&
                Objects.equals(cPwd, that.cPwd) &&
                Objects.equals(nSoConversionType, that.nSoConversionType) &&
                Objects.equals(nDcPrintRate, that.nDcPrintRate) &&
                Objects.equals(nTaxPrint, that.nTaxPrint) &&
                Objects.equals(nSearchItem, that.nSearchItem) &&
                Objects.equals(nSplRateFlag, that.nSplRateFlag) &&
                Objects.equals(cCommonPath, that.cCommonPath) &&
                Objects.equals(nDuedateBasedOn, that.nDuedateBasedOn) &&
                Objects.equals(nDuedateCalDays, that.nDuedateCalDays) &&
                Objects.equals(nDuedateCalMethod, that.nDuedateCalMethod) &&
                Objects.equals(nOnlinePay, that.nOnlinePay) &&
                Objects.equals(cParentActCode, that.cParentActCode) &&
                Objects.equals(cImportBrCode, that.cImportBrCode) &&
                Objects.equals(cImportTraderCode, that.cImportTraderCode) &&
                Objects.equals(nExpDays, that.nExpDays) &&
                Objects.equals(nUpdateStock, that.nUpdateStock) &&
                Objects.equals(nServiceChargeInclExcl, that.nServiceChargeInclExcl) &&
                Objects.equals(nSoDcProcess, that.nSoDcProcess) &&
                Objects.equals(nNoOfCopies, that.nNoOfCopies) &&
                Objects.equals(cEmpCode, that.cEmpCode) &&
                Objects.equals(nMinStockDay, that.nMinStockDay) &&
                Objects.equals(cProvisionalGstnNo, that.cProvisionalGstnNo) &&
                Objects.equals(cPermanentGstnNo, that.cPermanentGstnNo) &&
                Objects.equals(nSeperateUpdn, that.nSeperateUpdn) &&
                Objects.equals(cEdiCode, that.cEdiCode) &&
                Objects.equals(dStockLdate, that.dStockLdate) &&
                Objects.equals(cSlugName, that.cSlugName) &&
                Objects.equals(cCustBranchCode, that.cCustBranchCode) &&
                Objects.equals(nOrderFlag, that.nOrderFlag) &&
                Objects.equals(nDocumentFlag, that.nDocumentFlag) &&
                Objects.equals(nSaleRateFlag, that.nSaleRateFlag) &&
                Objects.equals(nCustomerMappingFlag, that.nCustomerMappingFlag) &&
                Objects.equals(cSoftwareName, that.cSoftwareName) &&
                Objects.equals(nLoMstSyncFlag, that.nLoMstSyncFlag) &&
                Objects.equals(nLoOrderSyncFlag, that.nLoOrderSyncFlag) &&
                Objects.equals(nExeDownloadApproveFlag, that.nExeDownloadApproveFlag) &&
                Objects.equals(nItemDivisonFilterFlag, that.nItemDivisonFilterFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cCode, cName, cShName, cGrpNo, nType, cDesc, cContactPerson, cAdd1, cAdd2, cAdd3, cCity, cPin, cPhone1, cPhone2, cFax, cMobile, cEmail, cAreaCode, cMrCode, cCustCategoryCode, cBranchCode, nDiscount, nLock, cDrugLicenceNo1, cDrugLicenceNo2, cStNo, cCstNo, dDate, cRemark, nCreditLimit, nCreditDays, nDebitDays, nDebitLimit, nFlag, nOrderDays, dLdate, dAdate, cCreateuser, cTaxTypeCode, nPoints, cCardNo, nCardValue, nRateOn, cDiscountSlabCode, nShift, nPointFactor, tLtime, dActivatedDate, dDeactivatedDate, nCardReverseDis, nRoaming, dDlExpiryDate, nAdminReconcile, cDrugLicenceNo3, cTallyhname, cTallygname, cGeoLat, cGeoLon, nInterPur, cPrintName, cPanNo, cTanNo, nEnableLiveOrder, nAutolock, nGraceDays, nGraceLimit, nMaxChqBounce, nAdminSettlement, cWeb, nRateIncVat, nRoadPermitFlag, cOrdInvTermCode, cTransportCode, cTinNo, cExciseNo, cBankCode, cIfscCode, cMicrCode, cBankActNo, cCorContPerson, cCorPhNo, nMaxLineItem, nDiscFlag, nChqPrintEnable, cModiuser, cBldgLandmarkCode, cBldgLandmarkCodeC, cCinNo, cCurrencyCode, cWebsite, cPwd, nSoConversionType, nDcPrintRate, nTaxPrint, nSearchItem, nSplRateFlag, cCommonPath, nDuedateBasedOn, nDuedateCalDays, nDuedateCalMethod, nOnlinePay, cParentActCode, cImportBrCode, cImportTraderCode, nExpDays, nUpdateStock, nServiceChargeInclExcl, nSoDcProcess, nNoOfCopies, cEmpCode, nMinStockDay, cProvisionalGstnNo, cPermanentGstnNo, nSeperateUpdn, cEdiCode, dStockLdate, cSlugName, cCustBranchCode, nOrderFlag, nDocumentFlag, nSaleRateFlag, nSellerFlag, nCustomerMappingFlag, cSoftwareName, nLoMstSyncFlag, nLoOrderSyncFlag, nExeDownloadApproveFlag, nItemDivisonFilterFlag);
    }
}
