package com.c2.lc.ms.master.bos.customerbos;

import com.c2.lc.lib.db.DateAudit;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
public class ContactDetailBO implements Serializable {

    private Long nContactId;

    private String cMobileNo;

    private String cPin;

}