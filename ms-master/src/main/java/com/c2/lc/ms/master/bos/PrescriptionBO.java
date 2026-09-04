package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.utils.Constants;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionBO {

    @SerializedName("n_pres_id")
    private String prescriptionId = Constants.EMPTY_STRING;

    @SerializedName("c_patient_name")
    private String patientName;

    @SerializedName("c_doctor_name")
    private String doctorName;

    @SerializedName("c_doctor_mobile_no")
    private String doctorMobile;

    @SerializedName("contact")
    private ContactBO prescriptionContact;

    @SerializedName("d_prescription_expiry_date")
    private LocalDate prescriptionExpiryDate = null;

    @SerializedName("prescription_docs")
    private List<PrescriptionDocsBO> prescriptionDocsBOList = new ArrayList<>();

}
