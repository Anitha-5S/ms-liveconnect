package com.c2.lc.ms.master.utils;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.entities.mongo.LcShortBook;
import com.c2.lc.ms.master.entities.mongo.LcWatchList;
import com.c2.lc.ms.master.services.interfaces.ShortBookWatchListService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemsToTopMostOrderResponseMapper {

    public JsonArray toTopMostOrderResponse(JsonArray jsonArray, ShortBookWatchListService shortBookWatchListService, LcHeaderBO lcHeaderBO) {

        JsonArray list = new JsonArray();

        List<LcWatchList> lcWatchLists = shortBookWatchListService.getWatchList(lcHeaderBO.getUserId(),
                lcHeaderBO.getFirmId(),lcHeaderBO.getFirmId());
        List<LcShortBook> lcShortBooks = shortBookWatchListService.getShortBook(lcHeaderBO.getUserId(),
                lcHeaderBO.getFirmId(),lcHeaderBO.getFirmId());
        jsonArray.forEach(jsonElement -> {
            JsonObject newResponse = new JsonObject();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.get("c_item_code") != null) {
                newResponse.addProperty("c_item_code", jsonObject.get("c_item_code").getAsString());
            }
            if (jsonObject.get("c_item_name") != null) {
                newResponse.addProperty("c_item_name", jsonObject.get("c_item_name").getAsString());
            }
            if (jsonObject.get("j_sub_detail").getAsJsonObject().get("c_pack_name") != null) {
                newResponse.addProperty("c_pack_name", jsonObject.get("j_sub_detail").getAsJsonObject()
                        .get("c_pack_name").getAsString());
            }
            if (jsonObject.get("n_mrp") != null) {
                newResponse.addProperty("n_max_mrp", jsonObject.get("n_mrp").getAsString());
            }
            if (jsonObject.get("j_sub_detail").getAsJsonObject().get("c_cont_name") != null) {
                newResponse.addProperty("c_content_name", jsonObject.get("j_sub_detail").getAsJsonObject()
                        .get("c_cont_name").getAsString());
            }
            if (jsonObject.get("a_thumbnail_urls").getAsJsonArray().get(0) != null) {
                newResponse.addProperty("ac_thumbnail_images", jsonObject.get("a_thumbnail_urls").getAsJsonArray()
                        .get(0).getAsString());
            }
            if (jsonObject.get("c_item_code").getAsString() != null) {
                newResponse.addProperty("c_watchlist_status", lcWatchLists.stream().filter(o -> o.getItemCode().equals(jsonObject.get("c_item_code").getAsString())).findFirst().isPresent()
                        ?Constants.STATUS_YES:Constants.STATUS_NO);
                newResponse.addProperty("c_short_book_status",lcShortBooks.stream().filter(o -> o.getItemCode().equals(jsonObject.get("c_item_code").getAsString())).findFirst().isPresent()
                        ?Constants.STATUS_YES:Constants.STATUS_NO);
            }
            newResponse.addProperty("c_discount_status", Constants.STATUS_NO); //TODO user query to update
            list.add(newResponse);
        });
        return list;
    }
}
