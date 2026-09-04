package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.base.BaseBO;
import com.c2.lc.ms.master.utils.MsMessages;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RowItemBO extends BaseBO implements Serializable {

	@SerializedName("c_sh_name")
	@Size(message = MsMessages.VALIDATE_NAME_LENGTH, min= 3, max = 20)
	private String cShName;

	@SerializedName("n_item_width")
	private BigDecimal nItemWidth;

	@SerializedName("n_rate_item_batchwise")
	private BigInteger nRateItemBatchwise;

	@SerializedName("n_min_sale_qty")
	private int nMinSaleQty;

	@SerializedName("c_pack_code")
	@Size(message = MsMessages.VALIDATE_PACKCODE_LENGTH, min = 3,max = 50)
	private String cPackCode;

	@SerializedName("n_brand_seq")
	private int nBrandSeq;

	@SerializedName("c_gst_code")
	private String cGstCode;

	@SerializedName("c_code")
	@NotNull(message = "c_code cannot be null")
	private String cCode;

	@SerializedName("c_nm_code")
	private String cNmCode;

	@SerializedName("n_schedule")
	private BigInteger nSchedule;

	@SerializedName("n_shelf_life")
	private int nShelfLife;

	@SerializedName("c_hsn_sac_code")
	@Size(message = MsMessages.VALIDATE_HSNSACCODE_LENGTH, min = 8,max = 10)
	private String cHsnSacCode;

	@SerializedName("c_edi_code")
	@Size(message = MsMessages.VALIDATE_EIDCODE_LENGTH, min = 3, max = 15)
	private String cEdiCode;

	@SerializedName("c_schedule_code")
	private String cScheduleCode;

	@SerializedName("n_inc_exc_tax")
	private BigInteger nIncExcTax;

	@SerializedName("n_outer_width")
	private BigDecimal nOuterWidth;

	@SerializedName("n_batch_no_rule")
	private BigInteger nBatchNoRule;

	@SerializedName("c_createuser")
	private String cCreateuser;

	@SerializedName("n_mrp")
	private BigDecimal nMrp;

	@SerializedName("c_disease_cat_code")
	private String cDiseaseCatCode;

	@SerializedName("n_inner_pack_lot")
	private int nInnerPackLot;

	@SerializedName("n_discount_rate")
	private BigDecimal nDiscountRate;

	@SerializedName("n_ptr")
	private int nPtr;

	@SerializedName("n_outer_weight")
	private BigDecimal nOuterWeight;

	@SerializedName("n_qty_per_box")
	private int nQtyPerBox;

	@SerializedName("n_outer_height")
	private BigDecimal nOuterHeight;

	@SerializedName("n_inter_weight")
	private BigDecimal nInterWeight;

	@SerializedName("t_ltime")
	private Timestamp tLtime;

	@SerializedName("n_self_barcode_req")
	private BigInteger nSelfBarcodeReq;

	@SerializedName("n_exclude_alternate")
	private BigInteger nExcludeAlternate;

	@SerializedName("c_name")
	@NotNull
	@NotBlank
	private String cName;

	@SerializedName("c_ucode")
	@Size(message = MsMessages.VALIDATE_CUCODE_LENGTH, min = 5,max = 6)
	private String cUcode;

	@SerializedName("c_storage_care_code")
	private String cStorageCareCode;

	@SerializedName("n_item_weight")
	private BigDecimal nItemWeight;

	@SerializedName("n_ptr_box")
	private BigDecimal nPtrBox;

	@SerializedName("n_inner_length")
	private BigDecimal nInnerLength;

	@SerializedName("n_block_exp_print")
	private BigInteger nBlockExpPrint;

	@SerializedName("n_hsn_sac_flag")
	private int nHsnSacFlag;

	@SerializedName("n_predefined")
	private BigInteger nPredefined;

	@SerializedName("n_salable_online")
	private BigInteger nSalableOnline;

	@SerializedName("n_type")
	private BigInteger nType;

	@SerializedName("n_gen_item")
	private BigInteger nGenItem;

	@SerializedName("d_ldate")
	private String dLdate;

	@SerializedName("n_service_item")
	private BigInteger nServiceItem;

	@SerializedName("n_item_height")
	private BigDecimal nItemHeight;

	@SerializedName("c_storage_code")
	private String cStorageCode;

	@SerializedName("n_outer_length")
	private BigDecimal nOuterLength;

	@SerializedName("n_outer_pack_lot")
	private int nOuterPackLot;

	@SerializedName("n_stk_serial")
	private BigInteger nStkSerial;

	@SerializedName("n_price_control_product")
	private BigInteger nPriceControlProduct;

	@SerializedName("n_consumption")
	private int nConsumption;

	@SerializedName("n_audited")
	private BigInteger nAudited;

	@SerializedName("d_adate")
	private String dAdate;

	@SerializedName("c_brand_name")
	private String cBrandName;

	@SerializedName("n_lock_po")
	private BigInteger nLockPo;

	@SerializedName("c_cat_code")
	private String cCatCode;

	@SerializedName("c_cont_code")
	private String cContCode;

	@SerializedName("n_item_length")
	private BigDecimal nItemLength;

	@SerializedName("n_exp_dt_rule")
	private BigInteger nExpDtRule;

	@SerializedName("n_inner_height")
	private BigDecimal nInnerHeight;

	@SerializedName("n_max_margin_per")
	private int nMaxMarginPer;

	@SerializedName("c_modiuser")
	private String cModiuser;

	@SerializedName("n_max_dis_per")
	private BigDecimal nMaxDisPer;

	@SerializedName("c_pack_type_code")
	private String cPackTypeCode;

	@SerializedName("c_group_code")
	private String cGroupCode;

	@SerializedName("n_max_mrp")
	private BigDecimal nMaxMrp;

	@SerializedName("c_brand_code")
	private String cBrandCode;

	@SerializedName("n_sp_sale_qty_from")
	private BigInteger nSpSaleQtyFrom;

	@SerializedName("n_non_returnable_item")
	private BigInteger nNonReturnableItem;

	@SerializedName("n_min_margin_per")
	private BigDecimal nMinMarginPer;

	@SerializedName("c_mfac_code")
	private String cMfacCode;

	@SerializedName("n_inner_width")
	private BigDecimal nInnerWidth;

	@SerializedName("n_mrp_box")
	private BigInteger nMrpBox;

	@SerializedName("n_lock")
	private BigInteger nLock;
}