package com.c2.lc.ms.master.bos.customerbos;

import com.c2.lc.lib.base.BaseBO;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class CustomerMstDetailsBO extends BaseBO implements Serializable {

	@SerializedName("c_cust_cat_code")
	private String custCatCode;

	@SerializedName("c_code")
	private String code;

	@SerializedName("c_city")
	private String city;

	@SerializedName("c_phone2")
	private String phone2;

	@SerializedName("c_route_name")
	private String routeName;

	@SerializedName("c_phone1")
	private String phone1;

	@SerializedName("n_max_bill_amt")
	private BigDecimal maxBillAmt;

	@SerializedName("c_sman_name")
	private String smanName;

	@SerializedName("n_disc_per")
	private BigDecimal discPer;

	@SerializedName("n_credit_limit")
	private BigDecimal creditLimit;

	@SerializedName("c_add2")
	private String add2;

	@SerializedName("c_add1")
	private String add1;

	@SerializedName("c_add3")
	private String add3;

	@SerializedName("c_create_date")
	private String createDate;

	@SerializedName("c_route_code")
	private String routeCode;

	@SerializedName("n_max_items")
	private Integer maxItems;

	@SerializedName("n_credit_days")
	private Integer creditDays;

	@SerializedName("c_mobile")
	private String mobile;

	@SerializedName("dl1")
	private Object dl1;

	@SerializedName("dl3")
	private Object dl3;

	@SerializedName("dl2")
	private Object dl2;

	@SerializedName("c_message")
	private String message;

	@SerializedName("n_lock_flag")
	private BigInteger lockFlag;

	@SerializedName("stNo")
	private Object stNo;

	@SerializedName("c_sman_code")
	private String smanCode;

	@SerializedName("c_name")
	private String name;

	@SerializedName("c_cust_cat_name")
	private String custCatName;

	@SerializedName("c_ldate")
	private String ldate;

}