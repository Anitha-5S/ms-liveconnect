package com.c2.lc.ms.customer.entities.comm;

import com.c2.lc.lib.db.DateAudit;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "eco_users")
@IdClass(value = EcoUsersPK.class)
public class EcoUsers extends DateAudit implements Serializable {

    @Expose
    @SerializedName("c_c2code")
    @NotEmpty(message = "'c_c2code' can not be empty!")
    @Id
    @Column(name="c_c2code")
    private String c2Code;

    @Expose
    @SerializedName("c_br_code")
    @NotEmpty(message = "'c_br_code' can not be empty!")
    @Id
    @Column(name="c_br_code")
    private String brCode;

    @Expose
    @SerializedName("c_terminal_id")
    @NotEmpty(message = "'c_terminal_id' can not be empty!")
    @Id
    @Column(name="c_terminal_id")
    private String terminalId;

    @Expose
    @SerializedName("c_pwd")
    @NotEmpty(message = "'c_pwd' can not be empty!")
    @Column(name="c_pwd")
    private String pwd;

    @Column(name="c_status")
    private String status;

}
