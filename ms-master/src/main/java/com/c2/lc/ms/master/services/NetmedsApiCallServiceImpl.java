package com.c2.lc.ms.master.services;

import com.c2.lc.lib.services.BaseServicesImpl;
import com.google.gson.JsonArray;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class NetmedsApiCallServiceImpl extends BaseServicesImpl implements NetmedsApiCallService {

    @Autowired private RestTemplate restTemplate;

    @Value("${netmeds.item-push.api}") private String postUrl;
    @Value("${netmeds.item-push.api.basic-auth.username}") private String username;
    @Value("${netmeds.item-push.api.basic-auth.password}") private String password;


    @Override
    public String makeApiCall(JsonArray items) {

        String authorizationString = username+":"+password;
        String base64Creds = Base64.getEncoder().encodeToString(authorizationString.getBytes());
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + base64Creds);

        /*String requestBody = "[\n" +
                "    {\n" +
                "        \"productCode\": 361,\n" +
                "        \"productName\": \"ABAMUNE L TAB 30'S\",\n" +
                "        \"shortName\" : \"-\",\n" +
                "        \"itemPackCode\" : \"7\",\n" +
                "        \"itemPackName\" : \"30's\",\n" +
                "        \"itemGroupCode\" : \"IG0107\",\n" +
                "        \"itemGroupName\" : \"TABLET\",\n" +
                "        \"itemCategoryCode\" : \"CT0001\",\n" +
                "        \"itemCategoryName\" : \"SWALLOW\",\n" +
                "        \"contentCode\" : \"CO0812\",\n" +
                "        \"contentName\" : \"ABACAVIR+LAMIVUDINE\",\n" +
                "        \"itemBrandCode\" : \"19\",\n" +
                "        \"itemBrandName\" : \"ABAMUNE\",\n" +
                "        \"mfactCode\" : \"M10308\",\n" +
                "        \"mfactName\" : \"NA\",\n" +
                "        \"itemNote\" : \"-\",\n" +
                "        \"itemCreatedDate\" : \"2020-07-20 14:42:17.850\",\n" +
                "        \"itemFullName\" : \"ABAMUNE L TAB 3'S\",\n" +
                "        \"itemHsnSacCode\" : \"3004909\",\n" +
                "        \"itemLock\" : 0,\n" +
                "        \"itemLastModifiedDate\" : \"2020-07-20 14:42:17.850\",\n" +
                "        \"itemLastModifiedTime\" : \"2020-07-20 14:42:17.850\",\n" +
                "        \"uCode\" : 361,\n" +
                "        \"stripInBox\" :1,\n" +
                "        \"itemGstCode\" : 12,\n" +
                "        \"ScheduleCode\" : \"1\",\n" +
                "        \"ScheduleName\" : \"SCHEDULED H DRUGS\",\n" +
                "        \"PackTypeCode\" : \"P00014\",\n" +
                "        \"PackTypeName\" : \"CONTAINER\",\n" +
                "        \"invdate\" : \"2020-07-20 14:42:17.850\"\n" +
                "    },\n" +
                "    {\n" +
                "         \"productCode\": 228938,\n" +
                "        \"productName\": \"ELIPRAN 20MG TAB\",\n" +
                "        \"shortName\" : \"-\",\n" +
                "        \"itemPackCode\" : \"16\",\n" +
                "        \"itemPackName\" : \"2's\",\n" +
                "        \"itemGroupCode\" : \"IG0107\",\n" +
                "        \"itemGroupName\" : \"TABLET\",\n" +
                "        \"itemCategoryCode\" : \"CT0001\",\n" +
                "        \"itemCategoryName\" : \"SWALLOW\",\n" +
                "        \"contentCode\" : \"CO0130\",\n" +
                "        \"contentName\" : \"ELETRIPTAN\",\n" +
                "        \"itemBrandCode\" : \"133043\",\n" +
                "        \"itemBrandName\" : \"ELIPRAN\",\n" +
                "        \"mfactCode\" : \"M01176\",\n" +
                "        \"mfactName\" : \"INTAS PHARMACEUTICALS PVT LTD\",\n" +
                "        \"itemNote\" : \"-\",\n" +
                "        \"itemCreatedDate\" : \"2020-07-20 14:42:17.850\",\n" +
                "        \"itemFullName\" : \"ELIPRAN 20MG TAB\",\n" +
                "        \"itemHsnSacCode\" : \"3004909\",\n" +
                "        \"itemLock\" : 0,\n" +
                "        \"itemLastModifiedDate\" : \"2020-07-20 14:42:17.850\",\n" +
                "        \"itemLastModifiedTime\" : \"2020-07-20 14:42:17.850\",\n" +
                "        \"uCode\" : 361,\n" +
                "        \"stripInBox\" :1,\n" +
                "        \"itemGstCode\" : 12,\n" +
                "        \"ScheduleCode\" : \"1\",\n" +
                "        \"ScheduleName\" : \"SCHEDULED H DRUGS\",\n" +
                "        \"PackTypeCode\" : \"P00006\",\n" +
                "        \"PackTypeName\" : \"STRIPS\",\n" +
                "        \"invdate\" : \"2020-07-20 14:42:17.850\"\n" +
                "    }\n" +
                "]";*/


        return this.callWebClientPostSyncApiWithHeader(postUrl,items.toString(),headers);
    }
}
