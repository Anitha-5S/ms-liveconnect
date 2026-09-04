package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "u_item_mst")
public class UItemMstEntity implements Serializable {
    private static final long serialVersionUID = 4087262022346404928L;
    private String cCode;
    private String cName;
    private String cFullName;
    private String cShName;
    private String cBarcodeLabel;
    private String cItemBrandCode;
    private String cStrength;
    private String cPackTypeCode;
    private String cItemCatCode;
    private String cItemMfacCode;
    private String cItemGrpCode;
    private String cItemContCode;
    private String cItemStorageCode;
    private String cItemStorageCareCode;
    private String cItemPackCode;
    private int nInnerPackLot;
    private int nOuterPackLot;
    private String cUom;
    private BigDecimal nItemLength;
    private BigDecimal nItemWidth;
    private BigDecimal nItemHeight;
    private BigDecimal nItemWeight;
    private BigDecimal nInnerLength;
    private BigDecimal nInnerWidth;
    private BigDecimal nInnerHeight;
    private BigDecimal nInterWeight;
    private BigDecimal nOuterLength;
    private BigDecimal nOuterWidth;
    private BigDecimal nOuterHeight;
    private BigDecimal nOuterWeight;
    private String cBarcode;
    private BigInteger nIncExcTax;
    private BigInteger nServiceItem;
    private BigInteger nBlockExpPrint;
    private BigInteger nBatchNoRule;
    private BigInteger nExpDtRule;
    private int nShelfLife;
    private String cWebImgLink;
    private String cNote;
    private BigInteger nPriceControlProduct;
    private BigDecimal nMaxMrp;
    private BigInteger nLock;
    private BigInteger nAudited;
    private BigInteger nPredefined;
    private String cCreateuser;
    private LocalDate dAdate;
    private LocalDate dLdate;
    private Timestamp tLtime;
    private String cModiuser;
    private Integer nQtyPerBox;
    private BigInteger nActive;
    private BigInteger nBan;
    private String cRemark;
    private BigDecimal nLastMrp;
    private String cHsnCode;
    private String cGstCode;
    private BigInteger nChronicFlag;

    @Id
    @Column(name = "c_code", nullable = false, length = 6)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    @Basic
    @Column(name = "c_name", nullable = false, length = 80)
    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    @Basic
    @Column(name = "c_full_name", nullable = true, length = 150)
    public String getcFullName() {
        return cFullName;
    }

    public void setcFullName(String cFullName) {
        this.cFullName = cFullName;
    }

    @Basic
    @Column(name = "c_sh_name", nullable = false, length = 6)
    public String getcShName() {
        return cShName;
    }

    public void setcShName(String cShName) {
        this.cShName = cShName;
    }

    @Basic
    @Column(name = "c_barcode_label", nullable = true, length = 40)
    public String getcBarcodeLabel() {
        return cBarcodeLabel;
    }

    public void setcBarcodeLabel(String cBarcodeLabel) {
        this.cBarcodeLabel = cBarcodeLabel;
    }

    @Basic
    @Column(name = "c_item_brand_code", nullable = false, length = 6)
    public String getcItemBrandCode() {
        return cItemBrandCode;
    }

    public void setcItemBrandCode(String cItemBrandCode) {
        this.cItemBrandCode = cItemBrandCode;
    }

    @Basic
    @Column(name = "c_strength", nullable = true, length = 20)
    public String getcStrength() {
        return cStrength;
    }

    public void setcStrength(String cStrength) {
        this.cStrength = cStrength;
    }

    @Basic
    @Column(name = "c_pack_type_code", nullable = false, length = 6)
    public String getcPackTypeCode() {
        return cPackTypeCode;
    }

    public void setcPackTypeCode(String cPackTypeCode) {
        this.cPackTypeCode = cPackTypeCode;
    }

    @Basic
    @Column(name = "c_item_cat_code", nullable = false, length = 6)
    public String getcItemCatCode() {
        return cItemCatCode;
    }

    public void setcItemCatCode(String cItemCatCode) {
        this.cItemCatCode = cItemCatCode;
    }

    @Basic
    @Column(name = "c_item_mfac_code", nullable = false, length = 6)
    public String getcItemMfacCode() {
        return cItemMfacCode;
    }

    public void setcItemMfacCode(String cItemMfacCode) {
        this.cItemMfacCode = cItemMfacCode;
    }

    @Basic
    @Column(name = "c_item_grp_code", nullable = false, length = 6)
    public String getcItemGrpCode() {
        return cItemGrpCode;
    }

    public void setcItemGrpCode(String cItemGrpCode) {
        this.cItemGrpCode = cItemGrpCode;
    }

    @Basic
    @Column(name = "c_item_cont_code", nullable = false, length = 6)
    public String getcItemContCode() {
        return cItemContCode;
    }

    public void setcItemContCode(String cItemContCode) {
        this.cItemContCode = cItemContCode;
    }

    @Basic
    @Column(name = "c_item_storage_code", nullable = false, length = 6)
    public String getcItemStorageCode() {
        return cItemStorageCode;
    }

    public void setcItemStorageCode(String cItemStorageCode) {
        this.cItemStorageCode = cItemStorageCode;
    }

    @Basic
    @Column(name = "c_item_storage_care_code", nullable = false, length = 6)
    public String getcItemStorageCareCode() {
        return cItemStorageCareCode;
    }

    public void setcItemStorageCareCode(String cItemStorageCareCode) {
        this.cItemStorageCareCode = cItemStorageCareCode;
    }

    @Basic
    @Column(name = "c_item_pack_code", nullable = false, length = 6)
    public String getcItemPackCode() {
        return cItemPackCode;
    }

    public void setcItemPackCode(String cItemPackCode) {
        this.cItemPackCode = cItemPackCode;
    }

    @Basic
    @Column(name = "n_inner_pack_lot", nullable = false, precision = 0)
    public int getnInnerPackLot() {
        return nInnerPackLot;
    }

    public void setnInnerPackLot(int nInnerPackLot) {
        this.nInnerPackLot = nInnerPackLot;
    }

    @Basic
    @Column(name = "n_outer_pack_lot", nullable = false, precision = 0)
    public int getnOuterPackLot() {
        return nOuterPackLot;
    }

    public void setnOuterPackLot(int nOuterPackLot) {
        this.nOuterPackLot = nOuterPackLot;
    }

    @Basic
    @Column(name = "c_uom", nullable = true, length = 10)
    public String getcUom() {
        return cUom;
    }

    public void setcUom(String cUom) {
        this.cUom = cUom;
    }

    @Basic
    @Column(name = "n_item_length", nullable = false, precision = 3)
    public BigDecimal getnItemLength() {
        return nItemLength;
    }

    public void setnItemLength(BigDecimal nItemLength) {
        this.nItemLength = nItemLength;
    }

    @Basic
    @Column(name = "n_item_width", nullable = false, precision = 3)
    public BigDecimal getnItemWidth() {
        return nItemWidth;
    }

    public void setnItemWidth(BigDecimal nItemWidth) {
        this.nItemWidth = nItemWidth;
    }

    @Basic
    @Column(name = "n_item_height", nullable = false, precision = 3)
    public BigDecimal getnItemHeight() {
        return nItemHeight;
    }

    public void setnItemHeight(BigDecimal nItemHeight) {
        this.nItemHeight = nItemHeight;
    }

    @Basic
    @Column(name = "n_item_weight", nullable = false, precision = 3)
    public BigDecimal getnItemWeight() {
        return nItemWeight;
    }

    public void setnItemWeight(BigDecimal nItemWeight) {
        this.nItemWeight = nItemWeight;
    }

    @Basic
    @Column(name = "n_inner_length", nullable = false, precision = 3)
    public BigDecimal getnInnerLength() {
        return nInnerLength;
    }

    public void setnInnerLength(BigDecimal nInnerLength) {
        this.nInnerLength = nInnerLength;
    }

    @Basic
    @Column(name = "n_inner_width", nullable = false, precision = 3)
    public BigDecimal getnInnerWidth() {
        return nInnerWidth;
    }

    public void setnInnerWidth(BigDecimal nInnerWidth) {
        this.nInnerWidth = nInnerWidth;
    }

    @Basic
    @Column(name = "n_inner_height", nullable = false, precision = 3)
    public BigDecimal getnInnerHeight() {
        return nInnerHeight;
    }

    public void setnInnerHeight(BigDecimal nInnerHeight) {
        this.nInnerHeight = nInnerHeight;
    }

    @Basic
    @Column(name = "n_inter_weight", nullable = false, precision = 3)
    public BigDecimal getnInterWeight() {
        return nInterWeight;
    }

    public void setnInterWeight(BigDecimal nInterWeight) {
        this.nInterWeight = nInterWeight;
    }

    @Basic
    @Column(name = "n_outer_length", nullable = false, precision = 3)
    public BigDecimal getnOuterLength() {
        return nOuterLength;
    }

    public void setnOuterLength(BigDecimal nOuterLength) {
        this.nOuterLength = nOuterLength;
    }

    @Basic
    @Column(name = "n_outer_width", nullable = false, precision = 3)
    public BigDecimal getnOuterWidth() {
        return nOuterWidth;
    }

    public void setnOuterWidth(BigDecimal nOuterWidth) {
        this.nOuterWidth = nOuterWidth;
    }

    @Basic
    @Column(name = "n_outer_height", nullable = false, precision = 3)
    public BigDecimal getnOuterHeight() {
        return nOuterHeight;
    }

    public void setnOuterHeight(BigDecimal nOuterHeight) {
        this.nOuterHeight = nOuterHeight;
    }

    @Basic
    @Column(name = "n_outer_weight", nullable = false, precision = 3)
    public BigDecimal getnOuterWeight() {
        return nOuterWeight;
    }

    public void setnOuterWeight(BigDecimal nOuterWeight) {
        this.nOuterWeight = nOuterWeight;
    }

    @Basic
    @Column(name = "c_barcode", nullable = true, length = 20)
    public String getcBarcode() {
        return cBarcode;
    }

    public void setcBarcode(String cBarcode) {
        this.cBarcode = cBarcode;
    }

    @Basic
    @Column(name = "n_inc_exc_tax", nullable = false, precision = 0)
    public BigInteger getnIncExcTax() {
        return nIncExcTax;
    }

    public void setnIncExcTax(BigInteger nIncExcTax) {
        this.nIncExcTax = nIncExcTax;
    }

    @Basic
    @Column(name = "n_service_item", nullable = false, precision = 0)
    public BigInteger getnServiceItem() {
        return nServiceItem;
    }

    public void setnServiceItem(BigInteger nServiceItem) {
        this.nServiceItem = nServiceItem;
    }

    @Basic
    @Column(name = "n_block_exp_print", nullable = false, precision = 0)
    public BigInteger getnBlockExpPrint() {
        return nBlockExpPrint;
    }

    public void setnBlockExpPrint(BigInteger nBlockExpPrint) {
        this.nBlockExpPrint = nBlockExpPrint;
    }

    @Basic
    @Column(name = "n_batch_no_rule", nullable = false, precision = 0)
    public BigInteger getnBatchNoRule() {
        return nBatchNoRule;
    }

    public void setnBatchNoRule(BigInteger nBatchNoRule) {
        this.nBatchNoRule = nBatchNoRule;
    }

    @Basic
    @Column(name = "n_exp_dt_rule", nullable = false, precision = 0)
    public BigInteger getnExpDtRule() {
        return nExpDtRule;
    }

    public void setnExpDtRule(BigInteger nExpDtRule) {
        this.nExpDtRule = nExpDtRule;
    }

    @Basic
    @Column(name = "n_shelf_life", nullable = false, precision = 0)
    public int getnShelfLife() {
        return nShelfLife;
    }

    public void setnShelfLife(int nShelfLife) {
        this.nShelfLife = nShelfLife;
    }

    @Basic
    @Column(name = "c_web_img_link", nullable = true, length = 500)
    public String getcWebImgLink() {
        return cWebImgLink;
    }

    public void setcWebImgLink(String cWebImgLink) {
        this.cWebImgLink = cWebImgLink;
    }

    @Basic
    @Column(name = "c_note", nullable = true, length = 50)
    public String getcNote() {
        return cNote;
    }

    public void setcNote(String cNote) {
        this.cNote = cNote;
    }

    @Basic
    @Column(name = "n_price_control_product", nullable = false, precision = 0)
    public BigInteger getnPriceControlProduct() {
        return nPriceControlProduct;
    }

    public void setnPriceControlProduct(BigInteger nPriceControlProduct) {
        this.nPriceControlProduct = nPriceControlProduct;
    }

    @Basic
    @Column(name = "n_max_mrp", nullable = false, precision = 3)
    public BigDecimal getnMaxMrp() {
        return nMaxMrp;
    }

    public void setnMaxMrp(BigDecimal nMaxMrp) {
        this.nMaxMrp = nMaxMrp;
    }

    @Basic
    @Column(name = "n_lock", nullable = false, precision = 0)
    public BigInteger getnLock() {
        return nLock;
    }

    public void setnLock(BigInteger nLock) {
        this.nLock = nLock;
    }

    @Basic
    @Column(name = "n_audited", nullable = false, precision = 0)
    public BigInteger getnAudited() {
        return nAudited;
    }

    public void setnAudited(BigInteger nAudited) {
        this.nAudited = nAudited;
    }

    @Basic
    @Column(name = "n_predefined", nullable = false, precision = 0)
    public BigInteger getnPredefined() {
        return nPredefined;
    }

    public void setnPredefined(BigInteger nPredefined) {
        this.nPredefined = nPredefined;
    }

    @Basic
    @Column(name = "c_createuser", nullable = false, length = 10)
    public String getcCreateuser() {
        return cCreateuser;
    }

    public void setcCreateuser(String cCreateuser) {
        this.cCreateuser = cCreateuser;
    }

    @Basic
    @Column(name = "d_adate", nullable = false)
    public LocalDate getdAdate() {
        return dAdate;
    }

    public void setdAdate(LocalDate dAdate) {
        this.dAdate = dAdate;
    }

    @Basic
    @Column(name = "d_ldate", nullable = false)
    public LocalDate getdLdate() {
        return dLdate;
    }

    public void setdLdate(LocalDate dLdate) {
        this.dLdate = dLdate;
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
    @Column(name = "c_modiuser", nullable = true, length = 10)
    public String getcModiuser() {
        return cModiuser;
    }

    public void setcModiuser(String cModiuser) {
        this.cModiuser = cModiuser;
    }

    @Basic
    @Column(name = "n_qty_per_box", nullable = true, precision = 0)
    public Integer getnQtyPerBox() {
        return nQtyPerBox;
    }

    public void setnQtyPerBox(Integer nQtyPerBox) {
        this.nQtyPerBox = nQtyPerBox;
    }

    @Basic
    @Column(name = "n_active", nullable = true, precision = 0)
    public BigInteger getnActive() {
        return nActive;
    }

    public void setnActive(BigInteger nActive) {
        this.nActive = nActive;
    }

    @Basic
    @Column(name = "n_ban", nullable = true, precision = 0)
    public BigInteger getnBan() {
        return nBan;
    }

    public void setnBan(BigInteger nBan) {
        this.nBan = nBan;
    }

    @Basic
    @Column(name = "c_remark", nullable = true, length = 150)
    public String getcRemark() {
        return cRemark;
    }

    public void setcRemark(String cRemark) {
        this.cRemark = cRemark;
    }

    @Basic
    @Column(name = "n_last_mrp", nullable = true, precision = 3)
    public BigDecimal getnLastMrp() {
        return nLastMrp;
    }

    public void setnLastMrp(BigDecimal nLastMrp) {
        this.nLastMrp = nLastMrp;
    }

    @Basic
    @Column(name = "c_hsn_code", nullable = true, length = 10)
    public String getcHsnCode() {
        return cHsnCode;
    }

    public void setcHsnCode(String cHsnCode) {
        this.cHsnCode = cHsnCode;
    }

    @Basic
    @Column(name = "c_gst_code", nullable = true, length = 10)
    public String getcGstCode() {
        return cGstCode;
    }

    public void setcGstCode(String cGstCode) {
        this.cGstCode = cGstCode;
    }

    @Basic
    @Column(name = "n_chronic_flag", nullable = false, precision = 0)
    public BigInteger getnChronicFlag() {
        return nChronicFlag;
    }

    public void setnChronicFlag(BigInteger nChronicFlag) {
        this.nChronicFlag = nChronicFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UItemMstEntity that = (UItemMstEntity) o;
        return nInnerPackLot == that.nInnerPackLot &&
                nOuterPackLot == that.nOuterPackLot &&
                nShelfLife == that.nShelfLife &&
                Objects.equals(cCode, that.cCode) &&
                Objects.equals(cName, that.cName) &&
                Objects.equals(cFullName, that.cFullName) &&
                Objects.equals(cShName, that.cShName) &&
                Objects.equals(cBarcodeLabel, that.cBarcodeLabel) &&
                Objects.equals(cItemBrandCode, that.cItemBrandCode) &&
                Objects.equals(cStrength, that.cStrength) &&
                Objects.equals(cPackTypeCode, that.cPackTypeCode) &&
                Objects.equals(cItemCatCode, that.cItemCatCode) &&
                Objects.equals(cItemMfacCode, that.cItemMfacCode) &&
                Objects.equals(cItemGrpCode, that.cItemGrpCode) &&
                Objects.equals(cItemContCode, that.cItemContCode) &&
                Objects.equals(cItemStorageCode, that.cItemStorageCode) &&
                Objects.equals(cItemStorageCareCode, that.cItemStorageCareCode) &&
                Objects.equals(cItemPackCode, that.cItemPackCode) &&
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
                Objects.equals(cBarcode, that.cBarcode) &&
                Objects.equals(nIncExcTax, that.nIncExcTax) &&
                Objects.equals(nServiceItem, that.nServiceItem) &&
                Objects.equals(nBlockExpPrint, that.nBlockExpPrint) &&
                Objects.equals(nBatchNoRule, that.nBatchNoRule) &&
                Objects.equals(nExpDtRule, that.nExpDtRule) &&
                Objects.equals(cWebImgLink, that.cWebImgLink) &&
                Objects.equals(cNote, that.cNote) &&
                Objects.equals(nPriceControlProduct, that.nPriceControlProduct) &&
                Objects.equals(nMaxMrp, that.nMaxMrp) &&
                Objects.equals(nLock, that.nLock) &&
                Objects.equals(nAudited, that.nAudited) &&
                Objects.equals(nPredefined, that.nPredefined) &&
                Objects.equals(cCreateuser, that.cCreateuser) &&
                Objects.equals(dAdate, that.dAdate) &&
                Objects.equals(dLdate, that.dLdate) &&
                Objects.equals(tLtime, that.tLtime) &&
                Objects.equals(cModiuser, that.cModiuser) &&
                Objects.equals(nQtyPerBox, that.nQtyPerBox) &&
                Objects.equals(nActive, that.nActive) &&
                Objects.equals(nBan, that.nBan) &&
                Objects.equals(cRemark, that.cRemark) &&
                Objects.equals(nLastMrp, that.nLastMrp) &&
                Objects.equals(cHsnCode, that.cHsnCode) &&
                Objects.equals(cGstCode, that.cGstCode) &&
                Objects.equals(nChronicFlag, that.nChronicFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cCode, cName, cFullName, cShName, cBarcodeLabel, cItemBrandCode, cStrength, cPackTypeCode, cItemCatCode, cItemMfacCode, cItemGrpCode, cItemContCode, cItemStorageCode, cItemStorageCareCode, cItemPackCode, nInnerPackLot, nOuterPackLot, cUom, nItemLength, nItemWidth, nItemHeight, nItemWeight, nInnerLength, nInnerWidth, nInnerHeight, nInterWeight, nOuterLength, nOuterWidth, nOuterHeight, nOuterWeight, cBarcode, nIncExcTax, nServiceItem, nBlockExpPrint, nBatchNoRule, nExpDtRule, nShelfLife, cWebImgLink, cNote, nPriceControlProduct, nMaxMrp, nLock, nAudited, nPredefined, cCreateuser, dAdate, dLdate, tLtime, cModiuser, nQtyPerBox, nActive, nBan, cRemark, nLastMrp, cHsnCode, cGstCode, nChronicFlag);
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
