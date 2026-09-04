package com.c2.lc.ms.master.entities.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LcOfferMstEntityPK implements Serializable {

    @Id
    @Column(name = "c_item_code")
    private String itemCode;

    @Id
    @Column(name = "c_offer_code")
    private String offerCode;

    @Id
    @Column(name = "c_seller_c2code")
    private String sellerC2Code;

    @Override
    public String toString() {
        return itemCode+":"+offerCode+":"+sellerC2Code;
    }

}
