package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "cust_item_mst")
@IdClass(CustItemMstEntityPK.class)
public class CustItemMstEntity {

    private String cCode;

    @Id
    @Column(name = "c_code", nullable = false, length = 100)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    private String cNmCode;

    @Column(name = "c_nm_code", nullable = false, length = 100)
    public String getcNmCode() {
        return cNmCode;
    }

    public void setcNmCode(String cNmCode) {
        this.cNmCode = cNmCode;
    }

    private String cC2Code;

    @Id
    @Column(name = "c_c2code", nullable = false, length = 20)
    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
    }

    private String cBrCode;

    @Id
    @Column(name = "c_br_code", nullable = false, length = 100)
    public String getcBrCode() {
        return cBrCode;
    }

    public void setcBrCode(String cBrCode) {
        this.cBrCode = cBrCode;
    }

    private String cName;

    @Basic
    @Column(name = "c_name", nullable = false, length = 250)
    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    private String cShName;

    @Basic
    @Column(name = "c_sh_name", nullable = true, length = 20)
    public String getcShName() {
        return cShName;
    }

    public void setcShName(String cShName) {
        this.cShName = cShName;
    }

    private String cBarcode;

    @Basic
    @Column(name = "c_barcode", nullable = true, length = 100)
    public String getcBarcode() {
        return cBarcode;
    }

    public void setcBarcode(String cBarcode) {
        this.cBarcode = cBarcode;
    }

    private String cPackCode;

    @Basic
    @Column(name = "c_pack_code", nullable = false, length = 50)
    public String getcPackCode() {
        return cPackCode;
    }

    public void setcPackCode(String cPackCode) {
        this.cPackCode = cPackCode;
    }

    private String cCatCode;

    @Basic
    @Column(name = "c_cat_code", nullable = false, length = 50)
    public String getcCatCode() {
        return cCatCode;
    }

    public void setcCatCode(String cCatCode) {
        this.cCatCode = cCatCode;
    }

    private String cMfacCode;

    @Basic
    @Column(name = "c_mfac_code", nullable = false, length = 50)
    public String getcMfacCode() {
        return cMfacCode;
    }

    public void setcMfacCode(String cMfacCode) {
        this.cMfacCode = cMfacCode;
    }

    private Integer nQtyPerBox;

    @Basic
    @Column(name = "n_qty_per_box", nullable = true, precision = 0)
    public Integer getnQtyPerBox() {
        return nQtyPerBox;
    }

    public void setnQtyPerBox(Integer nQtyPerBox) {
        this.nQtyPerBox = nQtyPerBox;
    }

    private String cGroupCode;

    @Basic
    @Column(name = "c_group_code", nullable = false, length = 50)
    public String getcGroupCode() {
        return cGroupCode;
    }

    public void setcGroupCode(String cGroupCode) {
        this.cGroupCode = cGroupCode;
    }

    private BigInteger nIncExcTax;

    @Basic
    @Column(name = "n_inc_exc_tax", nullable = false, precision = 0)
    public BigInteger getnIncExcTax() {
        return nIncExcTax;
    }

    public void setnIncExcTax(BigInteger nIncExcTax) {
        this.nIncExcTax = nIncExcTax;
    }

    private String cContCode;

    @Basic
    @Column(name = "c_cont_code", nullable = false, length = 50)
    public String getcContCode() {
        return cContCode;
    }

    public void setcContCode(String cContCode) {
        this.cContCode = cContCode;
    }

    private BigInteger nSchedule;

    @Basic
    @Column(name = "n_schedule", nullable = true, precision = 0)
    public BigInteger getnSchedule() {
        return nSchedule;
    }

    public void setnSchedule(BigInteger nSchedule) {
        this.nSchedule = nSchedule;
    }

    private String cNote;

    @Basic
    @Column(name = "c_note", nullable = true, length = 200)
    public String getcNote() {
        return cNote;
    }

    public void setcNote(String cNote) {
        this.cNote = cNote;
    }

    private LocalDate dLdate;

    @Basic
    @Column(name = "d_ldate", nullable = true)
    public LocalDate getdLdate() {
        return dLdate;
    }

    public void setdLdate(LocalDate dLdate) {
        this.dLdate = dLdate;
    }

    private LocalDate dAdate;

    @Basic
    @Column(name = "d_adate", nullable = false)
    public LocalDate getdAdate() {
        return dAdate;
    }

    public void setdAdate(LocalDate dAdate) {
        this.dAdate = dAdate;
    }

    private BigInteger nAudited;

    @Basic
    @Column(name = "n_audited", nullable = false, precision = 0)
    public BigInteger getnAudited() {
        return nAudited;
    }

    public void setnAudited(BigInteger nAudited) {
        this.nAudited = nAudited;
    }

    private BigInteger nPredefined;

    @Basic
    @Column(name = "n_predefined", nullable = false, precision = 0)
    public BigInteger getnPredefined() {
        return nPredefined;
    }

    public void setnPredefined(BigInteger nPredefined) {
        this.nPredefined = nPredefined;
    }

    private String cDiseaseCatCode;

    @Basic
    @Column(name = "c_disease_cat_code", nullable = true, length = 50)
    public String getcDiseaseCatCode() {
        return cDiseaseCatCode;
    }

    public void setcDiseaseCatCode(String cDiseaseCatCode) {
        this.cDiseaseCatCode = cDiseaseCatCode;
    }

    private String cPackTypeCode;

    @Basic
    @Column(name = "c_pack_type_code", nullable = true, length = 50)
    public String getcPackTypeCode() {
        return cPackTypeCode;
    }

    public void setcPackTypeCode(String cPackTypeCode) {
        this.cPackTypeCode = cPackTypeCode;
    }

    private Integer nMinSaleQty;

    @Basic
    @Column(name = "n_min_sale_qty", nullable = true, precision = 0)
    public Integer getnMinSaleQty() {
        return nMinSaleQty;
    }

    public void setnMinSaleQty(Integer nMinSaleQty) {
        this.nMinSaleQty = nMinSaleQty;
    }

    private String cScheduleCode;

    @Basic
    @Column(name = "c_schedule_code", nullable = false, length = 50)
    public String getcScheduleCode() {
        return cScheduleCode;
    }

    public void setcScheduleCode(String cScheduleCode) {
        this.cScheduleCode = cScheduleCode;
    }

    private String cBrandName;

    @Basic
    @Column(name = "c_brand_name", nullable = true, length = 100)
    public String getcBrandName() {
        return cBrandName;
    }

    public void setcBrandName(String cBrandName) {
        this.cBrandName = cBrandName;
    }

    private String cStrength;

    @Basic
    @Column(name = "c_strength", nullable = true, length = 100)
    public String getcStrength() {
        return cStrength;
    }

    public void setcStrength(String cStrength) {
        this.cStrength = cStrength;
    }

    private BigDecimal nMaxDisPer;

    @Basic
    @Column(name = "n_max_dis_per", nullable = true, precision = 2)
    public BigDecimal getnMaxDisPer() {
        return nMaxDisPer;
    }

    public void setnMaxDisPer(BigDecimal nMaxDisPer) {
        this.nMaxDisPer = nMaxDisPer;
    }

    private Timestamp tLtime;

    @Basic
    @Column(name = "t_ltime", nullable = true)
    public Timestamp gettLtime() {
        return tLtime;
    }

    public void settLtime(Timestamp tLtime) {
        this.tLtime = tLtime;
    }

    private BigInteger nSelfBarcodeReq;

    @Basic
    @Column(name = "n_self_barcode_req", nullable = true, precision = 0)
    public BigInteger getnSelfBarcodeReq() {
        return nSelfBarcodeReq;
    }

    public void setnSelfBarcodeReq(BigInteger nSelfBarcodeReq) {
        this.nSelfBarcodeReq = nSelfBarcodeReq;
    }

    private BigDecimal nMinMarginPer;

    @Basic
    @Column(name = "n_min_margin_per", nullable = true, precision = 2)
    public BigDecimal getnMinMarginPer() {
        return nMinMarginPer;
    }

    public void setnMinMarginPer(BigDecimal nMinMarginPer) {
        this.nMinMarginPer = nMinMarginPer;
    }

    private BigDecimal nMaxMarginPer;

    @Basic
    @Column(name = "n_max_margin_per", nullable = true, precision = 2)
    public BigDecimal getnMaxMarginPer() {
        return nMaxMarginPer;
    }

    public void setnMaxMarginPer(BigDecimal nMaxMarginPer) {
        this.nMaxMarginPer = nMaxMarginPer;
    }

    private BigInteger nLockPo;

    @Basic
    @Column(name = "n_lock_po", nullable = true, precision = 0)
    public BigInteger getnLockPo() {
        return nLockPo;
    }

    public void setnLockPo(BigInteger nLockPo) {
        this.nLockPo = nLockPo;
    }

    private String cEdiCode;

    @Basic
    @Column(name = "c_edi_code", nullable = true, length = 15)
    public String getcEdiCode() {
        return cEdiCode;
    }

    public void setcEdiCode(String cEdiCode) {
        this.cEdiCode = cEdiCode;
    }

    private BigInteger nServiceItem;

    @Basic
    @Column(name = "n_service_item", nullable = true, precision = 0)
    public BigInteger getnServiceItem() {
        return nServiceItem;
    }

    public void setnServiceItem(BigInteger nServiceItem) {
        this.nServiceItem = nServiceItem;
    }

    private String cStorageCode;

    @Basic
    @Column(name = "c_storage_code", nullable = true, length = 50)
    public String getcStorageCode() {
        return cStorageCode;
    }

    public void setcStorageCode(String cStorageCode) {
        this.cStorageCode = cStorageCode;
    }

    private String cStorageCareCode;

    @Basic
    @Column(name = "c_storage_care_code", nullable = true, length = 50)
    public String getcStorageCareCode() {
        return cStorageCareCode;
    }

    public void setcStorageCareCode(String cStorageCareCode) {
        this.cStorageCareCode = cStorageCareCode;
    }

    private BigInteger nBlockExpPrint;

    @Basic
    @Column(name = "n_block_exp_print", nullable = true, precision = 0)
    public BigInteger getnBlockExpPrint() {
        return nBlockExpPrint;
    }

    public void setnBlockExpPrint(BigInteger nBlockExpPrint) {
        this.nBlockExpPrint = nBlockExpPrint;
    }

    private BigInteger nGenItem;

    @Basic
    @Column(name = "n_gen_item", nullable = true, precision = 0)
    public BigInteger getnGenItem() {
        return nGenItem;
    }

    public void setnGenItem(BigInteger nGenItem) {
        this.nGenItem = nGenItem;
    }

    private String cWebImgLink;

    @Basic
    @Column(name = "c_web_img_link", nullable = true, length = 500)
    public String getcWebImgLink() {
        return cWebImgLink;
    }

    public void setcWebImgLink(String cWebImgLink) {
        this.cWebImgLink = cWebImgLink;
    }

    private Integer nInnerPackLot;

    @Basic
    @Column(name = "n_inner_pack_lot", nullable = true, precision = 0)
    public Integer getnInnerPackLot() {
        return nInnerPackLot;
    }

    public void setnInnerPackLot(Integer nInnerPackLot) {
        this.nInnerPackLot = nInnerPackLot;
    }

    private Integer nOuterPackLot;

    @Basic
    @Column(name = "n_outer_pack_lot", nullable = true, precision = 0)
    public Integer getnOuterPackLot() {
        return nOuterPackLot;
    }

    public void setnOuterPackLot(Integer nOuterPackLot) {
        this.nOuterPackLot = nOuterPackLot;
    }

    private String cUom;

    @Basic
    @Column(name = "c_uom", nullable = true, length = 50)
    public String getcUom() {
        return cUom;
    }

    public void setcUom(String cUom) {
        this.cUom = cUom;
    }

    private BigDecimal nItemLength;

    @Basic
    @Column(name = "n_item_length", nullable = true, precision = 4)
    public BigDecimal getnItemLength() {
        return nItemLength;
    }

    public void setnItemLength(BigDecimal nItemLength) {
        this.nItemLength = nItemLength;
    }

    private BigDecimal nItemWidth;

    @Basic
    @Column(name = "n_item_width", nullable = true, precision = 4)
    public BigDecimal getnItemWidth() {
        return nItemWidth;
    }

    public void setnItemWidth(BigDecimal nItemWidth) {
        this.nItemWidth = nItemWidth;
    }

    private BigDecimal nItemHeight;

    @Basic
    @Column(name = "n_item_height", nullable = true, precision = 4)
    public BigDecimal getnItemHeight() {
        return nItemHeight;
    }

    public void setnItemHeight(BigDecimal nItemHeight) {
        this.nItemHeight = nItemHeight;
    }

    private BigDecimal nItemWeight;

    @Basic
    @Column(name = "n_item_weight", nullable = true, precision = 4)
    public BigDecimal getnItemWeight() {
        return nItemWeight;
    }

    public void setnItemWeight(BigDecimal nItemWeight) {
        this.nItemWeight = nItemWeight;
    }

    private BigDecimal nInnerLength;

    @Basic
    @Column(name = "n_inner_length", nullable = true, precision = 4)
    public BigDecimal getnInnerLength() {
        return nInnerLength;
    }

    public void setnInnerLength(BigDecimal nInnerLength) {
        this.nInnerLength = nInnerLength;
    }

    private BigDecimal nInnerWidth;

    @Basic
    @Column(name = "n_inner_width", nullable = true, precision = 4)
    public BigDecimal getnInnerWidth() {
        return nInnerWidth;
    }

    public void setnInnerWidth(BigDecimal nInnerWidth) {
        this.nInnerWidth = nInnerWidth;
    }

    private BigDecimal nInnerHeight;

    @Basic
    @Column(name = "n_inner_height", nullable = true, precision = 4)
    public BigDecimal getnInnerHeight() {
        return nInnerHeight;
    }

    public void setnInnerHeight(BigDecimal nInnerHeight) {
        this.nInnerHeight = nInnerHeight;
    }

    private BigDecimal nInterWeight;

    @Basic
    @Column(name = "n_inter_weight", nullable = true, precision = 4)
    public BigDecimal getnInterWeight() {
        return nInterWeight;
    }

    public void setnInterWeight(BigDecimal nInterWeight) {
        this.nInterWeight = nInterWeight;
    }

    private BigDecimal nOuterLength;

    @Basic
    @Column(name = "n_outer_length", nullable = true, precision = 4)
    public BigDecimal getnOuterLength() {
        return nOuterLength;
    }

    public void setnOuterLength(BigDecimal nOuterLength) {
        this.nOuterLength = nOuterLength;
    }

    private BigDecimal nOuterWidth;

    @Basic
    @Column(name = "n_outer_width", nullable = true, precision = 4)
    public BigDecimal getnOuterWidth() {
        return nOuterWidth;
    }

    public void setnOuterWidth(BigDecimal nOuterWidth) {
        this.nOuterWidth = nOuterWidth;
    }

    private BigDecimal nOuterHeight;

    @Basic
    @Column(name = "n_outer_height", nullable = true, precision = 4)
    public BigDecimal getnOuterHeight() {
        return nOuterHeight;
    }

    public void setnOuterHeight(BigDecimal nOuterHeight) {
        this.nOuterHeight = nOuterHeight;
    }

    private BigDecimal nOuterWeight;

    @Basic
    @Column(name = "n_outer_weight", nullable = true, precision = 4)
    public BigDecimal getnOuterWeight() {
        return nOuterWeight;
    }

    public void setnOuterWeight(BigDecimal nOuterWeight) {
        this.nOuterWeight = nOuterWeight;
    }

    private BigInteger nBatchNoRule;

    @Basic
    @Column(name = "n_batch_no_rule", nullable = true, precision = 0)
    public BigInteger getnBatchNoRule() {
        return nBatchNoRule;
    }

    public void setnBatchNoRule(BigInteger nBatchNoRule) {
        this.nBatchNoRule = nBatchNoRule;
    }

    private BigInteger nExpDtRule;

    @Basic
    @Column(name = "n_exp_dt_rule", nullable = true, precision = 0)
    public BigInteger getnExpDtRule() {
        return nExpDtRule;
    }

    public void setnExpDtRule(BigInteger nExpDtRule) {
        this.nExpDtRule = nExpDtRule;
    }

    private Integer nShelfLife;

    @Basic
    @Column(name = "n_shelf_life", nullable = true, precision = 0)
    public Integer getnShelfLife() {
        return nShelfLife;
    }

    public void setnShelfLife(Integer nShelfLife) {
        this.nShelfLife = nShelfLife;
    }

    private String cFullName;

    @Basic
    @Column(name = "c_full_name", nullable = true, length = 100)
    public String getcFullName() {
        return cFullName;
    }

    public void setcFullName(String cFullName) {
        this.cFullName = cFullName;
    }

    private BigInteger nLock;

    @Basic
    @Column(name = "n_lock", nullable = true, precision = 0)
    public BigInteger getnLock() {
        return nLock;
    }

    public void setnLock(BigInteger nLock) {
        this.nLock = nLock;
    }

    private BigInteger nPriceControlProduct;

    @Basic
    @Column(name = "n_price_control_product", nullable = true, precision = 0)
    public BigInteger getnPriceControlProduct() {
        return nPriceControlProduct;
    }

    public void setnPriceControlProduct(BigInteger nPriceControlProduct) {
        this.nPriceControlProduct = nPriceControlProduct;
    }

    private BigDecimal nMaxMrp;

    @Basic
    @Column(name = "n_max_mrp", nullable = true, precision = 3)
    public BigDecimal getnMaxMrp() {
        return nMaxMrp;
    }

    public void setnMaxMrp(BigDecimal nMaxMrp) {
        this.nMaxMrp = nMaxMrp;
    }

    private String cBarcodeLabel;

    @Basic
    @Column(name = "c_barcode_label", nullable = true, length = 100)
    public String getcBarcodeLabel() {
        return cBarcodeLabel;
    }

    public void setcBarcodeLabel(String cBarcodeLabel) {
        this.cBarcodeLabel = cBarcodeLabel;
    }

    private BigInteger nRateItemBatchwise;

    @Basic
    @Column(name = "n_rate_item_batchwise", nullable = true, precision = 0)
    public BigInteger getnRateItemBatchwise() {
        return nRateItemBatchwise;
    }

    public void setnRateItemBatchwise(BigInteger nRateItemBatchwise) {
        this.nRateItemBatchwise = nRateItemBatchwise;
    }

    private String cBrandCode;

    @Basic
    @Column(name = "c_brand_code", nullable = true, length = 100)
    public String getcBrandCode() {
        return cBrandCode;
    }

    public void setcBrandCode(String cBrandCode) {
        this.cBrandCode = cBrandCode;
    }

    private String cCatLogNo;

    @Basic
    @Column(name = "c_cat_log_no", nullable = true, length = 50)
    public String getcCatLogNo() {
        return cCatLogNo;
    }

    public void setcCatLogNo(String cCatLogNo) {
        this.cCatLogNo = cCatLogNo;
    }

    private BigInteger nType;

    @Basic
    @Column(name = "n_type", nullable = true, precision = 0)
    public BigInteger getnType() {
        return nType;
    }

    public void setnType(BigInteger nType) {
        this.nType = nType;
    }

    private String cCostCentre;

    @Basic
    @Column(name = "c_cost_centre", nullable = true, length = 6)
    public String getcCostCentre() {
        return cCostCentre;
    }

    public void setcCostCentre(String cCostCentre) {
        this.cCostCentre = cCostCentre;
    }

    private BigInteger nStkSerial;

    @Basic
    @Column(name = "n_stk_serial", nullable = true, precision = 0)
    public BigInteger getnStkSerial() {
        return nStkSerial;
    }

    public void setnStkSerial(BigInteger nStkSerial) {
        this.nStkSerial = nStkSerial;
    }

    private String cQcNote1;

    @Basic
    @Column(name = "c_qc_note1", nullable = true, length = 80)
    public String getcQcNote1() {
        return cQcNote1;
    }

    public void setcQcNote1(String cQcNote1) {
        this.cQcNote1 = cQcNote1;
    }

    private String cQcNote2;

    @Basic
    @Column(name = "c_qc_note2", nullable = true, length = 80)
    public String getcQcNote2() {
        return cQcNote2;
    }

    public void setcQcNote2(String cQcNote2) {
        this.cQcNote2 = cQcNote2;
    }

    private String cExternalCode;

    @Basic
    @Column(name = "c_external_code", nullable = true, length = 15)
    public String getcExternalCode() {
        return cExternalCode;
    }

    public void setcExternalCode(String cExternalCode) {
        this.cExternalCode = cExternalCode;
    }

    private BigInteger nSalableOnline;

    @Basic
    @Column(name = "n_salable_online", nullable = true, precision = 0)
    public BigInteger getnSalableOnline() {
        return nSalableOnline;
    }

    public void setnSalableOnline(BigInteger nSalableOnline) {
        this.nSalableOnline = nSalableOnline;
    }

    private String cSuccessiveItem;

    @Basic
    @Column(name = "c_successive_item", nullable = true, length = 6)
    public String getcSuccessiveItem() {
        return cSuccessiveItem;
    }

    public void setcSuccessiveItem(String cSuccessiveItem) {
        this.cSuccessiveItem = cSuccessiveItem;
    }

    private BigDecimal nLeastRate;

    @Basic
    @Column(name = "n_least_rate", nullable = true, precision = 2)
    public BigDecimal getnLeastRate() {
        return nLeastRate;
    }

    public void setnLeastRate(BigDecimal nLeastRate) {
        this.nLeastRate = nLeastRate;
    }

    private BigDecimal nMaxRate;

    @Basic
    @Column(name = "n_max_rate", nullable = true, precision = 2)
    public BigDecimal getnMaxRate() {
        return nMaxRate;
    }

    public void setnMaxRate(BigDecimal nMaxRate) {
        this.nMaxRate = nMaxRate;
    }

    private Integer nDisplayOnline;

    @Basic
    @Column(name = "n_display_online", nullable = true)
    public Integer getnDisplayOnline() {
        return nDisplayOnline;
    }

    public void setnDisplayOnline(Integer nDisplayOnline) {
        this.nDisplayOnline = nDisplayOnline;
    }

    private BigInteger nBlockExpiryReturn;

    @Basic
    @Column(name = "n_block_expiry_return", nullable = true, precision = 0)
    public BigInteger getnBlockExpiryReturn() {
        return nBlockExpiryReturn;
    }

    public void setnBlockExpiryReturn(BigInteger nBlockExpiryReturn) {
        this.nBlockExpiryReturn = nBlockExpiryReturn;
    }

    private Integer nQtyPerCase;

    @Basic
    @Column(name = "n_qty_per_case", nullable = true, precision = 0)
    public Integer getnQtyPerCase() {
        return nQtyPerCase;
    }

    public void setnQtyPerCase(Integer nQtyPerCase) {
        this.nQtyPerCase = nQtyPerCase;
    }

    private BigDecimal nDiscountRate;

    @Basic
    @Column(name = "n_discount_rate", nullable = true, precision = 2)
    public BigDecimal getnDiscountRate() {
        return nDiscountRate;
    }

    public void setnDiscountRate(BigDecimal nDiscountRate) {
        this.nDiscountRate = nDiscountRate;
    }

    private BigDecimal nSpDiscRate;

    @Basic
    @Column(name = "n_sp_disc_rate", nullable = true, precision = 2)
    public BigDecimal getnSpDiscRate() {
        return nSpDiscRate;
    }

    public void setnSpDiscRate(BigDecimal nSpDiscRate) {
        this.nSpDiscRate = nSpDiscRate;
    }

    private BigInteger nSchmSlab2;

    @Basic
    @Column(name = "n_schm_slab2", nullable = true, precision = 0)
    public BigInteger getnSchmSlab2() {
        return nSchmSlab2;
    }

    public void setnSchmSlab2(BigInteger nSchmSlab2) {
        this.nSchmSlab2 = nSchmSlab2;
    }

    private BigInteger nSchmSlab3;

    @Basic
    @Column(name = "n_schm_slab3", nullable = true, precision = 0)
    public BigInteger getnSchmSlab3() {
        return nSchmSlab3;
    }

    public void setnSchmSlab3(BigInteger nSchmSlab3) {
        this.nSchmSlab3 = nSchmSlab3;
    }

    private BigInteger nStocksale;

    @Basic
    @Column(name = "n_stocksale", nullable = true, precision = 0)
    public BigInteger getnStocksale() {
        return nStocksale;
    }

    public void setnStocksale(BigInteger nStocksale) {
        this.nStocksale = nStocksale;
    }

    private String cHsnCode;

    @Basic
    @Column(name = "c_hsn_code", nullable = true, length = 10)
    public String getcHsnCode() {
        return cHsnCode;
    }

    public void setcHsnCode(String cHsnCode) {
        this.cHsnCode = cHsnCode;
    }

    private Integer nMinRackQty;

    @Basic
    @Column(name = "n_min_rack_qty", nullable = true, precision = 0)
    public Integer getnMinRackQty() {
        return nMinRackQty;
    }

    public void setnMinRackQty(Integer nMinRackQty) {
        this.nMinRackQty = nMinRackQty;
    }

    private BigInteger nExcludeAlternate;

    @Basic
    @Column(name = "n_exclude_alternate", nullable = true, precision = 0)
    public BigInteger getnExcludeAlternate() {
        return nExcludeAlternate;
    }

    public void setnExcludeAlternate(BigInteger nExcludeAlternate) {
        this.nExcludeAlternate = nExcludeAlternate;
    }

    private BigDecimal nMrp;

    @Basic
    @Column(name = "n_mrp", nullable = true, precision = 3)
    public BigDecimal getnMrp() {
        return nMrp;
    }

    public void setnMrp(BigDecimal nMrp) {
        this.nMrp = nMrp;
    }

    private BigDecimal nSaleRate;

    @Basic
    @Column(name = "n_sale_rate", nullable = true, precision = 3)
    public BigDecimal getnSaleRate() {
        return nSaleRate;
    }

    public void setnSaleRate(BigDecimal nSaleRate) {
        this.nSaleRate = nSaleRate;
    }

    private String scheme;

    @Basic
    @Column(name = "scheme", nullable = true, length = 1000)
    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    private Integer nQty;

    @Basic
    @Column(name = "n_qty", nullable = true, precision = 0)
    public Integer getnQty() {
        return nQty;
    }

    public void setnQty(Integer nQty) {
        this.nQty = nQty;
    }

    private Integer nAvgQty;

    @Basic
    @Column(name = "n_avg_qty", nullable = true, precision = 0)
    public Integer getnAvgQty() {
        return nAvgQty;
    }

    public void setnAvgQty(Integer nAvgQty) {
        this.nAvgQty = nAvgQty;
    }

    private BigInteger nIncl;

    @Basic
    @Column(name = "n_incl", nullable = true, precision = 0)
    public BigInteger getnIncl() {
        return nIncl;
    }

    public void setnIncl(BigInteger nIncl) {
        this.nIncl = nIncl;
    }

    private BigDecimal nStRate;

    @Basic
    @Column(name = "n_st_rate", nullable = true, precision = 3)
    public BigDecimal getnStRate() {
        return nStRate;
    }

    public void setnStRate(BigDecimal nStRate) {
        this.nStRate = nStRate;
    }

    private BigInteger nContolledProduct;

    @Basic
    @Column(name = "n_contolled_product", nullable = true, precision = 0)
    public BigInteger getnContolledProduct() {
        return nContolledProduct;
    }

    public void setnContolledProduct(BigInteger nContolledProduct) {
        this.nContolledProduct = nContolledProduct;
    }

    private BigDecimal nMarginRetailer;

    @Basic
    @Column(name = "n_margin_retailer", nullable = true, precision = 3)
    public BigDecimal getnMarginRetailer() {
        return nMarginRetailer;
    }

    public void setnMarginRetailer(BigDecimal nMarginRetailer) {
        this.nMarginRetailer = nMarginRetailer;
    }

    private Integer nMaxSaleQty;

    @Basic
    @Column(name = "n_max_sale_qty", nullable = true, precision = 0)
    public Integer getnMaxSaleQty() {
        return nMaxSaleQty;
    }

    public void setnMaxSaleQty(Integer nMaxSaleQty) {
        this.nMaxSaleQty = nMaxSaleQty;
    }

    private BigDecimal nStkPos;

    @Basic
    @Column(name = "n_stk_pos", nullable = true, precision = 3)
    public BigDecimal getnStkPos() {
        return nStkPos;
    }

    public void setnStkPos(BigDecimal nStkPos) {
        this.nStkPos = nStkPos;
    }

    private BigInteger nStkAvlFlag;

    @Basic
    @Column(name = "n_stk_avl_flag", nullable = true, precision = 0)
    public BigInteger getnStkAvlFlag() {
        return nStkAvlFlag;
    }

    public void setnStkAvlFlag(BigInteger nStkAvlFlag) {
        this.nStkAvlFlag = nStkAvlFlag;
    }

    private BigDecimal nMarginRetailerMin;

    @Basic
    @Column(name = "n_margin_retailer_min", nullable = true, precision = 3)
    public BigDecimal getnMarginRetailerMin() {
        return nMarginRetailerMin;
    }

    public void setnMarginRetailerMin(BigDecimal nMarginRetailerMin) {
        this.nMarginRetailerMin = nMarginRetailerMin;
    }

    private BigDecimal nMarginWhMin;

    @Basic
    @Column(name = "n_margin_wh_min", nullable = true, precision = 3)
    public BigDecimal getnMarginWhMin() {
        return nMarginWhMin;
    }

    public void setnMarginWhMin(BigDecimal nMarginWhMin) {
        this.nMarginWhMin = nMarginWhMin;
    }

    private BigDecimal marginWh;

    @Basic
    @Column(name = "margin_wh", nullable = true, precision = 3)
    public BigDecimal getMarginWh() {
        return marginWh;
    }

    public void setMarginWh(BigDecimal marginWh) {
        this.marginWh = marginWh;
    }

    private BigDecimal nMaxDisc;

    @Basic
    @Column(name = "n_max_disc", nullable = true, precision = 3)
    public BigDecimal getnMaxDisc() {
        return nMaxDisc;
    }

    public void setnMaxDisc(BigDecimal nMaxDisc) {
        this.nMaxDisc = nMaxDisc;
    }

    private BigDecimal nMrpBox;

    @Basic
    @Column(name = "n_mrp_box", nullable = true, precision = 3)
    public BigDecimal getnMrpBox() {
        return nMrpBox;
    }

    public void setnMrpBox(BigDecimal nMrpBox) {
        this.nMrpBox = nMrpBox;
    }

    private BigDecimal nPtrBox;

    @Basic
    @Column(name = "n_ptr_box", nullable = true, precision = 3)
    public BigDecimal getnPtrBox() {
        return nPtrBox;
    }

    public void setnPtrBox(BigDecimal nPtrBox) {
        this.nPtrBox = nPtrBox;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustItemMstEntity that = (CustItemMstEntity) o;
        return Objects.equals(cCode, that.cCode) &&
                Objects.equals(cC2Code, that.cC2Code) &&
                Objects.equals(cBrCode, that.cBrCode) &&
                Objects.equals(cName, that.cName) &&
                Objects.equals(cShName, that.cShName) &&
                Objects.equals(cBarcode, that.cBarcode) &&
                Objects.equals(cPackCode, that.cPackCode) &&
                Objects.equals(cCatCode, that.cCatCode) &&
                Objects.equals(cMfacCode, that.cMfacCode) &&
                Objects.equals(nQtyPerBox, that.nQtyPerBox) &&
                Objects.equals(cGroupCode, that.cGroupCode) &&
                Objects.equals(nIncExcTax, that.nIncExcTax) &&
                Objects.equals(cContCode, that.cContCode) &&
                Objects.equals(nSchedule, that.nSchedule) &&
                Objects.equals(cNote, that.cNote) &&
                Objects.equals(dLdate, that.dLdate) &&
                Objects.equals(dAdate, that.dAdate) &&
                Objects.equals(nAudited, that.nAudited) &&
                Objects.equals(nPredefined, that.nPredefined) &&
                Objects.equals(cDiseaseCatCode, that.cDiseaseCatCode) &&
                Objects.equals(cPackTypeCode, that.cPackTypeCode) &&
                Objects.equals(nMinSaleQty, that.nMinSaleQty) &&
                Objects.equals(cScheduleCode, that.cScheduleCode) &&
                Objects.equals(cBrandName, that.cBrandName) &&
                Objects.equals(cStrength, that.cStrength) &&
                Objects.equals(nMaxDisPer, that.nMaxDisPer) &&
                Objects.equals(tLtime, that.tLtime) &&
                Objects.equals(nSelfBarcodeReq, that.nSelfBarcodeReq) &&
                Objects.equals(nMinMarginPer, that.nMinMarginPer) &&
                Objects.equals(nMaxMarginPer, that.nMaxMarginPer) &&
                Objects.equals(nLockPo, that.nLockPo) &&
                Objects.equals(cEdiCode, that.cEdiCode) &&
                Objects.equals(nServiceItem, that.nServiceItem) &&
                Objects.equals(cStorageCode, that.cStorageCode) &&
                Objects.equals(cStorageCareCode, that.cStorageCareCode) &&
                Objects.equals(nBlockExpPrint, that.nBlockExpPrint) &&
                Objects.equals(nGenItem, that.nGenItem) &&
                Objects.equals(cWebImgLink, that.cWebImgLink) &&
                Objects.equals(nInnerPackLot, that.nInnerPackLot) &&
                Objects.equals(nOuterPackLot, that.nOuterPackLot) &&
                Objects.equals(cUom, that.cUom) &&
                Objects.equals(nItemLength, that.nItemLength) &&
                Objects.equals(nItemWidth, that.nItemWidth) &&
                Objects.equals(nItemHeight, that.nItemHeight) &&
                Objects.equals(nItemWeight, that.nItemWeight) &&
                Objects.equals(nInnerLength, that.nInnerLength) &&
                Objects.equals(nInnerWidth, that.nInnerWidth) &&
                Objects.equals(nInnerHeight, that.nInnerHeight) &&
                Objects.equals(nInterWeight, that.nInterWeight) &&
                Objects.equals(nOuterLength, that.nOuterLength) &&
                Objects.equals(nOuterWidth, that.nOuterWidth) &&
                Objects.equals(nOuterHeight, that.nOuterHeight) &&
                Objects.equals(nOuterWeight, that.nOuterWeight) &&
                Objects.equals(nBatchNoRule, that.nBatchNoRule) &&
                Objects.equals(nExpDtRule, that.nExpDtRule) &&
                Objects.equals(nShelfLife, that.nShelfLife) &&
                Objects.equals(cFullName, that.cFullName) &&
                Objects.equals(nLock, that.nLock) &&
                Objects.equals(nPriceControlProduct, that.nPriceControlProduct) &&
                Objects.equals(nMaxMrp, that.nMaxMrp) &&
                Objects.equals(cBarcodeLabel, that.cBarcodeLabel) &&
                Objects.equals(nRateItemBatchwise, that.nRateItemBatchwise) &&
                Objects.equals(cBrandCode, that.cBrandCode) &&
                Objects.equals(cCatLogNo, that.cCatLogNo) &&
                Objects.equals(nType, that.nType) &&
                Objects.equals(cCostCentre, that.cCostCentre) &&
                Objects.equals(nStkSerial, that.nStkSerial) &&
                Objects.equals(cQcNote1, that.cQcNote1) &&
                Objects.equals(cQcNote2, that.cQcNote2) &&
                Objects.equals(cExternalCode, that.cExternalCode) &&
                Objects.equals(nSalableOnline, that.nSalableOnline) &&
                Objects.equals(cSuccessiveItem, that.cSuccessiveItem) &&
                Objects.equals(nLeastRate, that.nLeastRate) &&
                Objects.equals(nMaxRate, that.nMaxRate) &&
                Objects.equals(nDisplayOnline, that.nDisplayOnline) &&
                Objects.equals(nBlockExpiryReturn, that.nBlockExpiryReturn) &&
                Objects.equals(nQtyPerCase, that.nQtyPerCase) &&
                Objects.equals(nDiscountRate, that.nDiscountRate) &&
                Objects.equals(nSpDiscRate, that.nSpDiscRate) &&
                Objects.equals(nSchmSlab2, that.nSchmSlab2) &&
                Objects.equals(nSchmSlab3, that.nSchmSlab3) &&
                Objects.equals(nStocksale, that.nStocksale) &&
                Objects.equals(cHsnCode, that.cHsnCode) &&
                Objects.equals(nMinRackQty, that.nMinRackQty) &&
                Objects.equals(nExcludeAlternate, that.nExcludeAlternate) &&
                Objects.equals(nMrp, that.nMrp) &&
                Objects.equals(nSaleRate, that.nSaleRate) &&
                Objects.equals(scheme, that.scheme) &&
                Objects.equals(nQty, that.nQty) &&
                Objects.equals(nAvgQty, that.nAvgQty) &&
                Objects.equals(nIncl, that.nIncl) &&
                Objects.equals(nStRate, that.nStRate) &&
                Objects.equals(nContolledProduct, that.nContolledProduct) &&
                Objects.equals(nMarginRetailer, that.nMarginRetailer) &&
                Objects.equals(nMaxSaleQty, that.nMaxSaleQty) &&
                Objects.equals(nStkPos, that.nStkPos) &&
                Objects.equals(nStkAvlFlag, that.nStkAvlFlag) &&
                Objects.equals(nMarginRetailerMin, that.nMarginRetailerMin) &&
                Objects.equals(nMarginWhMin, that.nMarginWhMin) &&
                Objects.equals(marginWh, that.marginWh) &&
                Objects.equals(nMaxDisc, that.nMaxDisc) &&
                Objects.equals(nMrpBox, that.nMrpBox) &&
                Objects.equals(nPtrBox, that.nPtrBox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cCode, cC2Code, cBrCode, cName, cShName, cBarcode, cPackCode, cCatCode, cMfacCode, nQtyPerBox, cGroupCode, nIncExcTax, cContCode, nSchedule, cNote, dLdate, dAdate, nAudited, nPredefined, cDiseaseCatCode, cPackTypeCode, nMinSaleQty, cScheduleCode, cBrandName, cStrength, nMaxDisPer, tLtime, nSelfBarcodeReq, nMinMarginPer, nMaxMarginPer, nLockPo, cEdiCode, nServiceItem, cStorageCode, cStorageCareCode, nBlockExpPrint, nGenItem, cWebImgLink, nInnerPackLot, nOuterPackLot, cUom, nItemLength, nItemWidth, nItemHeight, nItemWeight, nInnerLength, nInnerWidth, nInnerHeight, nInterWeight, nOuterLength, nOuterWidth, nOuterHeight, nOuterWeight, nBatchNoRule, nExpDtRule, nShelfLife, cFullName, nLock, nPriceControlProduct, nMaxMrp, cBarcodeLabel, nRateItemBatchwise, cBrandCode, cCatLogNo, nType, cCostCentre, nStkSerial, cQcNote1, cQcNote2, cExternalCode, nSalableOnline, cSuccessiveItem, nLeastRate, nMaxRate, nDisplayOnline, nBlockExpiryReturn, nQtyPerCase, nDiscountRate, nSpDiscRate, nSchmSlab2, nSchmSlab3, nStocksale, cHsnCode, nMinRackQty, nExcludeAlternate, nMrp, nSaleRate, scheme, nQty, nAvgQty, nIncl, nStRate, nContolledProduct, nMarginRetailer, nMaxSaleQty, nStkPos, nStkAvlFlag, nMarginRetailerMin, nMarginWhMin, marginWh, nMaxDisc, nMrpBox, nPtrBox);
    }

    @PrePersist
    void onCreate() {
        this.setdLdate(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toLocalDateTime().toLocalDate());
        this.setdAdate(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toLocalDateTime().toLocalDate());
    }

    @PreUpdate
    void onPersist() {
        this.setdLdate(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toLocalDateTime().toLocalDate());
    }
}
