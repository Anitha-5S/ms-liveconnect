package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.lib.db.DateAudit;
import com.c2.lc.ms.customer.entities.customer.pk.FirmUserRolePKEntity;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "recent_searched_items")
public class RecentSearchedItemsEntity implements Serializable {

    @Id
    @SerializedName("n_user_id")
    @Column(name = "n_user_id")
    private Long nUserId;

    @SerializedName("j_item_code")
    @Column(name = "j_item_code")
    private String jItemCode;

    public RecentSearchedItemsEntity() {
    }
}