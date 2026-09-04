package com.c2.lc.ms.master.bos;

import java.io.Serializable;
import java.util.List;

import com.c2.lc.lib.base.BaseBO;
import com.c2.lc.ms.master.utils.MsMessages;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyerSellerBO extends BaseBO implements Serializable {


	@Size(message = MsMessages.VALIDATE_C2CODE_LENGTH, min = 4,max = 6)
	@SerializedName("c2_code")
	private String c2Code;

	@SerializedName("items")
	private List<ItemsBO> items;

}