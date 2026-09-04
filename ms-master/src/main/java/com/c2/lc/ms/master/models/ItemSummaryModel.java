package com.c2.lc.ms.master.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("ItemSummary")
public class ItemSummaryModel {

    @Id
    private String cItemCode;
    private Object data;

}
