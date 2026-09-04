package com.c2.lc.ms.master.entities.mysql;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "u_stockiest_item", schema = "order_buk_new")
@NamedQuery(name = "UStockiestItemEntity.findAll", query = "SELECT i FROM UStockiestItemEntity i")
public class UStockiestItemEntity implements Serializable {


    private static final long serialVersionUID = 1L;

    /*@Id
    @Column(name = "c_stockiest_code", nullable = false, length = 6)
    public String getcStockiestCode() {
        return cStockiestCode;
    }

    public void setcStockiestCode(String cStockiestCode) {
        this.cStockiestCode = cStockiestCode;
    }

    private String cStockiestItemCode;

    @Id
    @Column(name = "c_stockiest_item_code", nullable = false, length = 100)
    public String getcStockiestItemCode() {
        return cStockiestItemCode;
    }



    public void setcStockiestItemCode(String cStockiestItemCode) {
        this.cStockiestItemCode = cStockiestItemCode;
    }*/

    @EmbeddedId
    private UStockiestItemEntityPK pK;

    @Column(name = "c_ucode", columnDefinition = "char(6)",nullable = false)
    private String cUcode;


    @Column(name = "d_ldate", nullable = true)
    private LocalDateTime dLdate;


    @Column(name = "n_supp_qb")
    private Integer nSuppQb;

}
