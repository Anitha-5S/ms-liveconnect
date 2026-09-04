package com.c2.lc.ms.master.entities.mysql;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Embeddable
public class UStockiestItemEntityPK implements Serializable {
    @Column(name = "c_stockiest_code", nullable = false, length = 6)
    @NotNull
    private String cStockiestCode;

    @Column(name = "c_stockiest_item_code", nullable = false, length = 100)
    @NotNull
    private String cStockiestItemCode;
}
