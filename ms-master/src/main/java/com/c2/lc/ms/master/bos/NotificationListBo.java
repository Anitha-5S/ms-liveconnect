package com.c2.lc.ms.master.bos;

import com.c2.lc.lib.bo.NextPageBO;
import com.c2.lc.ms.master.entities.mongo.LcNotification;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationListBo {

    @SerializedName("j_list")
    private List<LcNotification> notificationList;

    @SerializedName("page")
    private NextPageBO nextPage;

}
