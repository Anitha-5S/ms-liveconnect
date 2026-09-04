package com.c2.lc.ms.master.bos.customerbos;

import com.c2.lc.lib.base.BaseBO;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RowMfacBO extends BaseBO implements Serializable {

    @SerializedName("c_code")
    @NotEmpty(message = "'c_code' cannot be empty or null!")
    @NotBlank(message = "'c_code' cannot be blank!")
    @Size(
            max = 50,
            message = "'c_code' cannot exceed {max} characters!"
    )
    private String cCcode;

    @SerializedName("c_name")
    @Size(
            max = 255,
            message = "'c_name' cannot exceed {max} characters!"
    )
    @NotBlank(message = "'c_name' cannot be blank!")
    @NotEmpty(message = "'c_name' cannot be empty or null!")
    private String cNname;

    @SerializedName("c_sh_name")
    @Size(
            max = 20,
            message = "'c_sh_name' cannot exceed {max} characters!"
    )
    private String cShName;

    @SerializedName("c_add_1")
    @Size(
            max = 200,
            message = "'c_add_1' cannot exceed {max} characters!"
    )
    private String cAdd1;

    @SerializedName("c_add_2")
    @Size(
            max = 200,
            message = "'c_add_2' cannot exceed {max} characters!"
    )
    private String cAdd2;

    @SerializedName("c_add_3")
    @Size(
            max = 200,
            message = "'c_add_3' cannot exceed {max} characters!"
    )
    private String cAdd3;

    @SerializedName("c_city")
    @Size(
            max = 100,
            message = "'c_city' cannot exceed {max} characters!"
    )
    private String cCity;

    @SerializedName("c_pin")
    @Pattern(regexp="(^$|[0-9]{6})",message="c_pin must be number only of fixed length 6")
    private String cPincode;

    @SerializedName("c_phone_1")
    @Pattern(regexp="(^$|[0-9]{10})",message="c_phone_1 must be number only of fixed length 10")
    private String cPhone1;

    @SerializedName("c_phone_2")
    @Pattern(regexp="(^$|[0-9]{10})",message="c_phone_2 must be number only of fixed length 10")
    private String cPhone2;

    @SerializedName("c_fax")
    @Size(
            max = 20,
            message = "'c_fax' cannot exceed {max} characters!"
    )
    private String cFax;

    @SerializedName("c_contact_person")
    @Size(
            max = 200,
            message = "'c_contact_person' cannot exceed {max} characters!"
    )
    private String cContactPerson;

    @SerializedName("n_lock")
    @Digits(integer = 11, fraction = 0, message ="'n_lock' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private Integer nLock;

    @SerializedName("c_drug_licence_no_1")
    @Size(
            max = 100,
            message = "'c_drug_licence_no_1' cannot exceed {max} characters!"
    )
    private String cDrugLicenceNo1;

    @SerializedName("c_drug_licence_no_2")
    @Size(
            max = 100,
            message = "'c_drug_licence_no_2' cannot exceed {max} characters!"
    )
    private String cDrugLicenceNo2;

    @SerializedName("c_st_no")
    @Size(
            max = 200,
            message = "'c_st_no' cannot exceed {max} characters!"
    )
    private String cStNo;

    @SerializedName("c_cst_no")
    @Size(
            max = 200,
            message = "'c_cst_no' cannot exceed {max} characters!"
    )
    private String cCstNo;

    @SerializedName("c_email")
    @Size(
            max = 200,
            message = "'c_email' cannot exceed {max} characters!"
    )
    private String cEmail;

    @SerializedName("d_ldate")
    @Pattern(regexp="^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$",message="d_ldate must be in yyyy-mm-dd format")
    private String dLdate;

    @SerializedName("d_adate")

    @Pattern(regexp="^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$",message="d_adate must be in yyyy-mm-dd format")
    private String dAdate;

    @SerializedName("c_createuser")
    @Size(
            max = 50,
            message = "'c_createuser' cannot exceed {max} characters!"
    )
    private String cCreateuser;

    @SerializedName("n_audited")
    @Digits(integer = 1, fraction = 0, message ="'n_audited' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private Integer nAudited;

    @SerializedName("n_predefined")
    @Digits(integer = 1, fraction = 0, message ="'n_predefined' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private Integer nPredefined;

    @SerializedName("c_geo_lat")
    @Size(
            max = 20,
            message = "'c_geo_lat' cannot exceed {max} characters!"
    )
    private String cGeoLat;

    @SerializedName("c_geo_lon")
    @Size(
            max = 20,
            message = "'c_geo_lon' cannot exceed {max} characters!"
    )
    private String cGeoLon;

    @SerializedName("c_mfac_group_code")
    @Size(
            max = 50,
            message = "'c_mfac_group_code' cannot exceed {max} characters!"
    )
    private String cMfacGroupCode;

    @SerializedName("c_area_code")
    @Size(
            max = 50,
            message = "'c_area_code' cannot exceed {max} characters!"
    )
    private String cAreaCode;

    @SerializedName("c_modify_user")
    @Size(
            max = 50,
            message = "'c_modify_user' cannot exceed {max} characters!"
    )
    private String cModifyUser;

    @SerializedName("c_full_name")
    @Size(
            max = 250,
            message = "'c_full_name' cannot exceed {max} characters!"
    )
    private String cFullName;

    @SerializedName("n_salable_online")
    @NotNull(message = "'n_salable_online' cannot be null!")
    @Digits(integer = 1, fraction = 0, message ="'n_salable_online' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigDecimal nSalableOnline;

    @SerializedName("n_stocksale")
    @Digits(integer = 1, fraction = 0, message ="'n_stocksale' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigDecimal nStocksale;

    @SerializedName("n_block_crnt")
    @Digits(integer = 1, fraction = 0, message ="'n_block_crnt' size within has to be in the range integer : {integer}, fraction : {fraction}")
    private BigDecimal nBlockCrnt;


}
