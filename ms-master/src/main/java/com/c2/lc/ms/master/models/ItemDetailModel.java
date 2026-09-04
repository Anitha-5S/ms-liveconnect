package com.c2.lc.ms.master.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("ItemDetails")
public class ItemDetailModel {

    @Id
    private String cItemCode;
    private Object data;

}
