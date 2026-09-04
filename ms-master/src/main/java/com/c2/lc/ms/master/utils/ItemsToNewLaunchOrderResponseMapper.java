package com.c2.lc.ms.master.utils;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.ThumbnailBO;
import com.c2.lc.ms.master.entities.mongo.LcShortBook;
import com.c2.lc.ms.master.entities.mongo.LcWatchList;
import com.c2.lc.ms.master.repos.mongo.LcShortBookMongoRepository;
import com.c2.lc.ms.master.repos.mongo.LcWatchListMongoRepository;
import com.c2.lc.ms.master.services.interfaces.ShortBookWatchListService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemsToNewLaunchOrderResponseMapper extends LoBaseController {

    @Autowired private LcShortBookMongoRepository lcShortBookMongoRepository;
    @Autowired private LcWatchListMongoRepository lcWatchListMongoRepository;
    @Autowired private ShortBookWatchListService shortBookWatchListService;
    public JsonArray toNewLaunchItemResponse(JsonArray jsonArray, LcHeaderBO lcHeaderBO) {


        JsonArray list = new JsonArray();

        /*List<LcWatchList> lcWatchLists = shortBookWatchListService.getWatchList(lcHeaderBO.getUserId(),
                lcHeaderBO.getFirmId(),lcHeaderBO.getBranchId());
        List<LcShortBook> lcShortBooks = shortBookWatchListService.getShortBook(lcHeaderBO.getUserId(),
                lcHeaderBO.getFirmId(),lcHeaderBO.getBranchId());*/
        jsonArray.forEach(jsonElement -> {
            JsonObject newResponse = new JsonObject();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray jsonArray1 = new JsonArray();
            JsonObject jObj = new JsonObject();
            if (jsonObject.get("c_item_code") != null) {
                newResponse.addProperty("c_item_code", jsonObject.get("c_item_code").getAsString());
            }
            if (jsonObject.get("c_item_name") != null) {
                newResponse.addProperty("c_item_name", jsonObject.get("c_item_name").getAsString());
            }
            if (jsonObject.get("c_pack_name") != null) {
                newResponse.addProperty("c_pack_name", jsonObject.get("c_pack_name").getAsString());
            }
            if (jsonObject.get("n_mrp") != null) {
                newResponse.addProperty("n_mrp", jsonObject.get("n_mrp").getAsDouble());
            }
            if (jsonObject.get("c_contains") != null) {
                newResponse.addProperty("c_contains", jsonObject.get("c_contains").getAsString());
            }
            if (!helper.isEmpty(jsonObject.get("c_web_img_link"))) {
                jObj.addProperty("c_thumbnail_image", helper.getString(jsonObject.get("c_web_img_link")));
                jsonArray1.add(jObj);
                newResponse.add("ac_thumbnail_images", jsonArray1);
            } else
                jsonObject.add("ac_thumbnail_images", jsonArray1);
           /* newResponse.addProperty("c_watchlist_status", lcWatchLists.stream().filter(o -> o.getItemCode().equals(jsonObject.get("c_item_code").getAsString())).findFirst().isPresent()
                    ?Constants.STATUS_YES:Constants.STATUS_NO);
            newResponse.addProperty("c_short_book_status",lcShortBooks.stream().filter(o -> o.getItemCode().equals(jsonObject.get("c_item_code").getAsString())).findFirst().isPresent()
                    ?Constants.STATUS_YES:Constants.STATUS_NO);*/
           /* newResponse.addProperty("c_watchlist_status", lcWatchListMongoRepository.getByItemAndBranch(jsonObject.get("c_item_code").getAsString()
                        , lcHeaderBO.getBranchId(), lcHeaderBO.getUserId(), lcHeaderBO.getFirmId())==null
                        ? com.c2.lc.lib.utils.Constants.STATUS_NO: Constants.STATUS_YES);


                newResponse.addProperty("c_shortbook_status", lcShortBookMongoRepository.getByItemAndBranch(jsonObject.get("c_item_code").getAsString()
                        , lcHeaderBO.getBranchId(), lcHeaderBO.getUserId(), lcHeaderBO.getFirmId())==null
                        ? com.c2.lc.lib.utils.Constants.STATUS_NO: Constants.STATUS_YES);
*/
            newResponse.addProperty("c_pack_type_name",jsonObject.get("c_pack_type_name").getAsString());
            newResponse.addProperty("c_discount_status", com.c2.lc.lib.utils.Constants.STATUS_NO);
            newResponse.addProperty("c_watchlist_status", Constants.STATUS_NO);
            newResponse.addProperty("c_short_book_status", Constants.STATUS_NO);

            list.add(newResponse);
        });
        return list;
    }
}
