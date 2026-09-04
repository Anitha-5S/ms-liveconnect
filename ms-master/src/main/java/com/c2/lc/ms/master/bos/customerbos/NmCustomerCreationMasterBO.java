package com.c2.lc.ms.master.bos.customerbos;

import com.c2.lc.lib.base.BaseBO;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NmCustomerCreationMasterBO extends BaseBO implements Serializable {

    @SerializedName("c_pan")
    private String pan;

    @SerializedName("c_gstin")
    private String gstin;

    @SerializedName("c_dl_num1")
    private String dlNum1;

    @SerializedName("c_dl_num2")
    private String dlNum2;

    @SerializedName("c_state_code")
    private String stateCode;

    @SerializedName("c_party_name")
    private String partyName;

    @SerializedName("c_customer_ref_id")
    private String customerRefId;

    @SerializedName("c_party_short_name")
    private String partyShortName;

    @SerializedName("partyContacts")
    private List<NmCustomerCreationDetailsBO> nmCustomerCreationDetailsBOList;

}
