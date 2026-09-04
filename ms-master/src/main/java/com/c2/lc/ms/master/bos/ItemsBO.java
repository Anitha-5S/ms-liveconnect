package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.base.BaseBO;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;


@Data
public class ItemsBO extends BaseBO implements Serializable {

	@SerializedName("c_sh_name")
	private String cShName;

	@SerializedName("n_item_width")
	private Integer nItemWidth;

	@SerializedName("n_rate_item_batchwise")
	private Integer nRateItemBatchwise;

	@SerializedName("n_min_sale_qty")
	private Integer nMinSaleQty;

	@SerializedName("c_pack_code")
	private String cPackCode;

	@SerializedName("n_brand_seq")
	private Integer nBrandSeq;

	@SerializedName("c_gst_code")
	private String cGstCode;

	@SerializedName("c_code")
	private String cCode;

	@SerializedName("n_schedule")
	private Integer nSchedule;

	@SerializedName("n_shelf_life")
	private Integer nShelfLife;

	@SerializedName("c_hsn_sac_code")
	private String cHsnSacCode;

	@SerializedName("c_edi_code")
	private String cEdiCode;

	@SerializedName("c_schedule_code")
	private String cScheduleCode;

	@SerializedName("n_inc_exc_tax")
	private Integer nIncExcTax;

	@SerializedName("n_outer_width")
	private Integer nOuterWidth;

	@SerializedName("n_batch_no_rule")
	private Integer nBatchNoRule;

	@SerializedName("c_createuser")
	private String cCreateuser;

	@SerializedName("n_mrp")
	private Integer nMrp;

	@SerializedName("c_disease_cat_code")
	private String cDiseaseCatCode;

	@SerializedName("n_inner_pack_lot")
	private Integer nInnerPackLot;

	@SerializedName("n_discount_rate")
	private Integer nDiscountRate;

	@SerializedName("n_ptr")
	private Integer nPtr;

	@SerializedName("n_outer_weight")
	private Integer nOuterWeight;

	@SerializedName("n_qty_per_box")
	private Integer nQtyPerBox;

	@SerializedName("n_outer_height")
	private Integer nOuterHeight;

	@SerializedName("n_inter_weight")
	private Double nInterWeight;

	@SerializedName("t_ltime")
	private String tLtime;

	@SerializedName("n_self_barcode_req")
	private Integer nSelfBarcodeReq;

	@SerializedName("n_exclude_alternate")
	private Integer nExcludeAlternate;

	@SerializedName("c_name")
	private String cName;

	@SerializedName("c_storage_care_code")
	private String cStorageCareCode;

	@SerializedName("n_item_weight")
	private Double nItemWeight;

	@SerializedName("n_ptr_box")
	private Integer nPtrBox;

	@SerializedName("n_inner_length")
	private Integer nInnerLength;

	@SerializedName("n_block_exp_print")
	private Integer nBlockExpPrint;

	@SerializedName("n_hsn_sac_flag")
	private Integer nHsnSacFlag;

	@SerializedName("n_predefined")
	private Integer nPredefined;

	@SerializedName("n_salable_online")
	private Integer nSalableOnline;

	@SerializedName("n_type")
	private Integer nType;

	@SerializedName("n_gen_item")
	private Integer nGenItem;

	@SerializedName("d_ldate")
	private String dLdate;

	@SerializedName("n_service_item")
	private Integer nServiceItem;

	@SerializedName("n_item_height")
	private Integer nItemHeight;

	@SerializedName("c_storage_code")
	private String cStorageCode;

	@SerializedName("n_outer_length")
	private Integer nOuterLength;

	@SerializedName("n_outer_pack_lot")
	private Integer nOuterPackLot;

	@SerializedName("n_stk_serial")
	private Integer nStkSerial;

	@SerializedName("n_price_control_product")
	private Integer nPriceControlProduct;

	@SerializedName("n_consumption")
	private Integer nConsumption;

	@SerializedName("n_audited")
	private Integer nAudited;

	@SerializedName("d_adate")
	private String dAdate;

	@SerializedName("c_brand_name")
	private String cBrandName;

	@SerializedName("n_lock_po")
	private Integer nLockPo;

	@SerializedName("c_cat_code")
	private String cCatCode;

	@SerializedName("c_cont_code")
	private String cContCode;

	@SerializedName("n_item_length")
	private Integer nItemLength;

	@SerializedName("n_exp_dt_rule")
	private Integer nExpDtRule;

	@SerializedName("n_inner_height")
	private Integer nInnerHeight;

	@SerializedName("n_max_margin_per")
	private Integer nMaxMarginPer;

	@SerializedName("c_modiuser")
	private String cModiuser;

	@SerializedName("n_max_dis_per")
	private Integer nMaxDisPer;

	@SerializedName("c_pack_type_code")
	private String cPackTypeCode;

	@SerializedName("c_group_code")
	private String cGroupCode;

	@SerializedName("n_max_mrp")
	private Integer nMaxMrp;

	@SerializedName("c_brand_code")
	private String cBrandCode;

	@SerializedName("n_sp_sale_qty_from")
	private Integer nSpSaleQtyFrom;

	@SerializedName("n_non_returnable_item")
	private Integer nNonReturnableItem;

	@SerializedName("n_min_margin_per")
	private Integer nMinMarginPer;

	@SerializedName("c_mfac_code")
	private String cMfacCode;

	@SerializedName("n_inner_width")
	private Integer nInnerWidth;

	@SerializedName("n_mrp_box")
	private Integer nMrpBox;

	@SerializedName("n_lock")
	private Integer nLock;
}