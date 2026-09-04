package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.lib.db.DateAudit;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "schedule_demo")
@NamedQuery(name = "ScheduleDemo.findAll", query = "SELECT sd FROM ScheduleDemoEntity sd")
public class ScheduleDemoEntity extends DateAudit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("n_demo_id")
    @Column(name = "n_demo_id", unique = true, nullable = false)
    private Long nDemoId;

    @SerializedName("c_store_name")
    @NotEmpty(message = "'c_store_name' can not be empty!")
    @Size(message = "'c_store_name' should be of between {min} and {max} characters!", min = 4, max = 255)
    @Column(name = "c_store_name", length = 255)
    private String cStoreName;

    @SerializedName("c_owner_name")
    @NotEmpty(message = "'c_owner_name' can not be empty!")
    @Size(message = "'c_owner_name' should be of between {min} and {max} characters!", min = 4, max = 255)
    @Column(name = "c_owner_name", length = 255)
    private String cOwnerName;

    @SerializedName("c_mobile_no")
    @Size(message = "'c_mobile_no' should be of {min} characters!", min = 10, max = 10)
    @Column(name = "c_mobile_no", length = 10)
    private String cMobileNo;

    @SerializedName("c_pincode")
    @NotEmpty(message = "'c_pincode' can not be empty!")
    @Size(message = "'c_pincode' should be of {min} characters!", min = 6, max = 6)
    @Column(name = "c_pincode", length = 6)
    private String cPincode;

    @SerializedName("c_description")
    @NotEmpty(message = "'c_description' can not be empty!")
    @Size(message = "'c_description' should be of between {min} and {max} characters!", min = 4, max = 1024)
    @Column(name = "c_description", length = 1024)
    private String cDescription;

    @SerializedName("c_existing_customer")
    @Column(name = "c_existing_customer", length = 1)
    private String cExistingCustomer;

    @SerializedName("c_product")
    @Column(name = "c_product", length = 2)
    private String cProduct;

    @SerializedName("c_status")
    @Column(name = "c_status", length = 2)
    private String cStatus = "N";

}