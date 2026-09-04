package com.c2.lc.ms.master.models;

import com.c2.lc.ms.master.bos.customerbos.EgCustomerCreationBO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("eg_customer_creation_log")
@EqualsAndHashCode(callSuper = true)
public class EgCustomerCreationLog extends CustomerCreationLog {
    @Id
    private String id;
    private String egCustomerId;
    private EgCustomerCreationBO request;
}
