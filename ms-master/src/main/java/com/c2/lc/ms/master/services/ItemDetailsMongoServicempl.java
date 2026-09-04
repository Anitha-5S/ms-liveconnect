package com.c2.lc.ms.master.services;

import com.c2.lc.ms.master.repos.mysql.ItemDetailMongoRepository;
import com.c2.lc.ms.master.repos.mysql.ItemSummaryRepository;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.ItemDetailsMongoService;
import com.c2.lc.ms.master.services.interfaces.ItemService;
import com.c2.lc.ms.master.models.ItemDetailModel;
import com.c2.lc.ms.master.models.ItemSummaryModel;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ItemDetailsMongoServicempl extends MasterBaseServiceImpl implements ItemDetailsMongoService {

    @Value("${master.service.url}")
    private String MASTER_URL;

    @Autowired
    ItemDetailMongoRepository itemDetailMongoRepository;
    @Autowired
    ItemSummaryRepository itemSummaryRepository;
    @Autowired
    ItemService itemService;


    @Override
    public ItemDetailModel getDetails(String itemCode) throws RecordNotFoundException, CommunicationErrorException {
        ItemDetailModel itemDetailModel = itemDetailMongoRepository.findByItemCode(itemCode);

        String url = MASTER_URL +"/item/detail/"+itemCode;
        return (itemDetailModel != null) ? itemDetailModel : callMasterGetService(url);
    }

    private ItemDetailModel callMasterGetService(String url) throws CommunicationErrorException, RecordNotFoundException {
        log.info("Calling ms-master-service endpoint, {}",url);
        String ret = this.callWebClientGetSyncApi(url);
        log.info("Response, {}",ret);
        JsonObject jsonObject = helper.getJsonObject(ret);
        if (jsonObject != null) {
            if (jsonObject.get("appStatusCode").getAsInt() != 0) {
                throw new RecordNotFoundException(jsonObject.get("messages").getAsString());
            }
        } else {
            throw new CommunicationErrorException("Master Service","Service un-available");
        }
        ItemDetailModel itemDetailModel = new ItemDetailModel();
        itemDetailModel.setData(BasicDBObject.parse(jsonObject.get("payloadJson").toString()));
        return itemDetailModel;
    }

    @Override
    public ItemDetailModel getSummary(String itemCode) throws RecordNotFoundException, CommunicationErrorException {
        ItemSummaryModel itemSummaryModel = itemSummaryRepository.findByItemCode(itemCode);
        ItemDetailModel itemDetailModel = new ItemDetailModel();
        if(itemSummaryModel != null){
            BeanUtils.copyProperties(itemSummaryModel, itemDetailModel);
        }
        String url = MASTER_URL +"/item/summary/"+itemCode;
        return (itemSummaryModel != null) ? itemDetailModel : callMasterGetService(url);
    }

    @Override
    public JsonArray getSummaryByMfac(String mfacCode, Long pageNumber, Long rowLimit) throws RecordNotFoundException, CommunicationErrorException {
        List<String> itemList = itemService.findByMfacCode(mfacCode,pageNumber,rowLimit);
        return getItemSummary(itemList);
    }

    @Override
    public JsonArray getSummaryByContent(String contCode, Long pageNumber, Long rowLimit) throws RecordNotFoundException, CommunicationErrorException {
        List<String> itemList = itemService.findByContCode(contCode,pageNumber,rowLimit);
        return getItemSummary(itemList);
    }

    @Override
    public JsonArray getItemSummary(List<String> itemList) throws CommunicationErrorException, RecordNotFoundException {
        JsonArray inputData = new JsonArray(); //Array sent to ms-master-service to get item summary
        JsonArray response = new JsonArray();

        for(String itemCode : itemList){
            ItemSummaryModel itemSummaryModel = itemSummaryRepository.findByItemCode(itemCode);
            if(itemSummaryModel == null) {
                inputData.add(itemCode);
            }else{
                JsonObject jsonObject = helper.getJsonObject(itemSummaryModel.getData());
                response.add(jsonObject);
            }
        }
        if(inputData.size() > 0 ){
            JsonObject inputItemList = new JsonObject();
            inputItemList.add("itemList",inputData);

            JsonArray responseArray = itemService.getItemSummary(inputItemList);
            for(JsonElement jsonElement : responseArray){
                response.add(jsonElement.getAsJsonObject());
            }
        }
        return response;
    }

    private JsonObject callMasterPostService(String url,String payload) throws CommunicationErrorException, RecordNotFoundException {
        log.info("Calling ms-master-service endpoint, {}",url);
        log.info("Calling ms-master-service payload, {}",payload);
        String ret = this.callWebClientPostSyncApi(url,payload);
        log.info("Response, {}",ret);
        JsonObject jsonObject = helper.getJsonObject(ret);
        if (jsonObject != null) {
            if (jsonObject.get("appStatusCode").getAsInt() != 0) {
                throw new RecordNotFoundException(jsonObject.get("messages").getAsString());
            }
        } else {
            throw new CommunicationErrorException("Master Service","Service un-available");
        }
        return helper.getJsonObject(jsonObject.get("payloadJson").toString());
    }
}
