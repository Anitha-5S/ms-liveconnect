package com.c2.lc.ms.master.controllers;

/*import com.algolia.search.ApacheHttpRequester;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchConfig;
import com.algolia.search.SearchIndex;
import com.algolia.search.models.indexing.Query;
import com.algolia.search.models.indexing.SearchResult;*/
import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.NextPageBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.bos.ItemListBO;
import com.c2.lc.ms.master.bos.ItemListDetBO;
import com.c2.lc.ms.master.bos.ItemListResultBO;
import com.c2.lc.ms.master.bos.ItemMapCountBO;
import com.c2.lc.ms.master.transactions.interfaces.ItemSubMasterTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "${api.base.path}/item/sub/master/live")
public class ItemSubMasterController extends LoBaseController {

    @Autowired
    ItemSubMasterTransaction itemSubMasterTransaction;

    @PostMapping(value = "/count", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> countManufacture(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/item/sub/master/live/count");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);

           // String c2Code = json.get("c_c2code").getAsString();
            String cTypeCode = json.get("c_type_code").getAsString();

            ItemMapCountBO itemMapCountBO = itemSubMasterTransaction.count(header.getC2Code(), cTypeCode);
            this.setDataJsonObjectPayload(apiResponse, helper.toJsonObjectTree(itemMapCountBO, ItemMapCountBO.class));

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/deleteItem", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteManufacture(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/item/sub/master/live/deleteItem");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
           // String c2Code = json.get("c_c2code").getAsString();
            String filterType = json.get("c_filter_type").getAsString();
            String c_Code = json.get("c_code").getAsString();
            itemSubMasterTransaction.deleteItem(header.getC2Code(), filterType, c_Code);
            this.addMessage(apiResponse, "Item Moved Successfully");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/moveToOwnAllManufacture", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> moveToOwnAllManufacture(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/item/sub/master/live/moveToOwnAllManufacture");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            //String c2Code = json.get("c_c2code").getAsString();
            String cFilterType = json.get("c_filter_type").getAsString();
            JsonArray arr = new JsonArray();
            if(json.has("j_codes")) {
                arr = json.getAsJsonArray("j_codes");
            }
            itemSubMasterTransaction.moveToOwnAllManufactureList(header.getC2Code(), cFilterType, arr, json);
            this.addMessage(apiResponse, "Item Moved Successfully");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/moveToBlockedManufacture", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> moveToBlockedManufacture(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/item/sub/master/live/moveToBlockedManufacture");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
          //  String c2Code = json.get("c_c2code").getAsString();
            String cFilterType = json.get("c_filter_type").getAsString();
            String c_Code = json.get("c_code").getAsString();
            itemSubMasterTransaction.moveToBlockedManufacture(header.getC2Code(), cFilterType, c_Code);
            this.addMessage(apiResponse, "Item Moved Successfully");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/confirmManufacture", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> confirmManufacture(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/item/sub/master/live/confirmManufacture");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            //String c2Code = json.get("c_c2code").getAsString();
            String cFilterType = json.get("c_filter_type").getAsString();
            String c_Code = json.get("c_code").getAsString();
            String cSquareCode = json.get("c_csquare_mfg_code").getAsString();
            itemSubMasterTransaction.confirmManufacture(header.getC2Code(), cFilterType, c_Code, cSquareCode);
            this.addMessage(apiResponse, "Item Moved Successfully");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/moveToOwnManufacture", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> moveToOwnManufacture(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/item/sub/master/live/moveToOwnManufacture");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            //String c2Code = json.get("c_c2code").getAsString();
            String cFilterType = json.get("c_filter_type").getAsString();
            JsonArray arr = json.getAsJsonArray("j_codes");
            itemSubMasterTransaction.moveToOwnManufacture(header.getC2Code(), cFilterType, arr);
            this.addMessage(apiResponse, "Item Moved Successfully");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/listManufacture", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> listManufacture(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException, InputPayloadException {
        ApiResponse apiResponse = this.initializeResponse("/item/sub/master/live/listManufacture");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            PageBO pageBo = helper.fromJSON(payload, PageBO.class);
            NextPageBO nextPageBO = new NextPageBO();
            ItemListDetBO itemListDetBO = new ItemListDetBO();
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            this.validateInputPayload(pageBo);
           // String c2Code = json.get("c_c2code").getAsString();
            String listType = json.get("c_list_type").getAsString();
            String cFilterType = json.get("c_filter_type").getAsString();
            String searchKey = json.get("c_search_key").getAsString();
            List<ItemListBO> itemListBO = itemSubMasterTransaction.fetchItem(lcHeaderBO.getC2Code(), listType, cFilterType, searchKey, pageBo.getPage(), pageBo.getLimit());
            nextPageBO.setPage(pageBo.getPage() + 1);
            nextPageBO.setTotal(itemSubMasterTransaction.mappedItemsCount(lcHeaderBO.getC2Code(), listType, cFilterType, searchKey));
            itemListDetBO.setItemList(itemListBO);
            itemListDetBO.setNextPage(nextPageBO);
            this.setJsonPayload(apiResponse, helper.toJsonObjectTree(itemListDetBO, ItemListDetBO.class));
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(value = "/listManufactureFilter", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> listManufactureWithFilter(@RequestHeader Map<String, String> headers) throws InvalidRequestException, InputPayloadException {
        ApiResponse apiResponse = this.initializeResponse("/item/sub/master/live/listManufactureFilter");
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            /*JsonObject json = helper.fromJson(payload, JsonObject.class);
            String c2Code = json.get("c_c2code").getAsString();*/
            ItemListResultBO resultBO = new ItemListResultBO();
            List<JsonObject> itemListBO = itemSubMasterTransaction.fetchItemWithFilter(lcHeaderBO.getC2Code());
            resultBO.setResult(itemListBO);
            this.setJsonPayload(apiResponse, helper.toJsonObjectTree(resultBO, ItemListResultBO.class));
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

   /* @PostMapping(value = "/algolia", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> algolia(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException, InputPayloadException {
        ApiResponse apiResponse = this.initializeResponse("/item/sub/master/live/algolia");
        try {
            ItemListBO itemListBO = helper.fromJson(payload, ItemListBO.class);
            SearchConfig config = new SearchConfig.Builder("LK22RUGVYG", "03dc38390b98cde158fd9f29f18379fc").build();
            HttpAsyncClientBuilder builder = HttpAsyncClientBuilder.create();
            SearchClient client = new SearchClient(config, new ApacheHttpRequester(config, builder));
            SearchIndex index = client.initIndex("test", ItemListBO.class);

            itemListBO.setObjectId(new ObjectId().toString());
            System.out.println(itemListBO);
            index.saveObject(itemListBO);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/algolia/search", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> algoliaSearch(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException, InputPayloadException {
        ApiResponse apiResponse = this.initializeResponse("/item/sub/master/live/algolia/search");
        try {
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            String search = json.get("c_search_term").getAsString();
            SearchConfig config = new SearchConfig.Builder("LK22RUGVYG", "03dc38390b98cde158fd9f29f18379fc").build();
            HttpAsyncClientBuilder builder = HttpAsyncClientBuilder.create();
            SearchClient client = new SearchClient(config, new ApacheHttpRequester(config, builder));
            SearchIndex index = client.initIndex("test", ItemListBO.class);
            SearchResult list = index.search(new Query(search));
            JsonArray list1 = (JsonArray) helper.getGson().toJsonTree(list.getHits(),
                    new TypeToken<List<ItemListBO>>() {
                    }.getType());
            System.out.println(list.getHits());
            this.setDataJsonArrayPayload(apiResponse, list1);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
*/
    @PostMapping(value = "/item/sub/mapping/search", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> searchItem(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException, InputPayloadException {
        ApiResponse apiResponse = this.initializeResponse("/item/sub/master/live/item/sub/mapping/search");
        try {
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            String search = json.get("c_search_key").getAsString();
            String filterType = json.get("c_filter_type").getAsString();
            int offset = json.get("n_offset").getAsInt();
            int limit = json.get("n_limit").getAsInt();
            JsonArray result = itemSubMasterTransaction.subMappingSearch(search, filterType, offset, limit);
            /*JsonArray result = itemSubMasterTransaction.subMappingSearch(search, filterType, offset, limit);
            json.add("j_item_list", result);
            json.addProperty("n_next_offset", offset+1);
            //json.addProperty("n_total", itemSubMasterTransaction.getLc1SearchCount(colName, search));*/

            this.setDataJsonArrayPayload(apiResponse, result);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

}
