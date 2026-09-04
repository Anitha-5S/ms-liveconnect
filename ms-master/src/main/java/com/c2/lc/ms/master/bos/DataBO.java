package com.c2.lc.ms.master.bos;

import java.io.Serializable;
import java.util.List;

import com.c2.lc.lib.base.BaseBO;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper=false)
public class DataBO extends BaseBO implements Serializable {

	@SerializedName("row")
	@Valid
	private List<RowItemBO> row;

	@SerializedName("c_table_name")
	@NotEmpty(message = "'c_table_name' cannot be null value")
	private String tableName;
}