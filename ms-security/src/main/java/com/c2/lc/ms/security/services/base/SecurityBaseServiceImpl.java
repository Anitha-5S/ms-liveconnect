package com.c2.lc.ms.security.services.base;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.ms.security.services.Interface.base.SecurityBaseService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Slf4j
public abstract class SecurityBaseServiceImpl extends BaseDBServiceImpl implements SecurityBaseService {


    protected Boolean matchesData(String newPassword,String dbPassword){
        return newPassword.equals(dbPassword);
    }

    public boolean isPasswordValid(String password) {
        return password != null
                && password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{4,16}$");
    }

    protected int sendSMS(String mobileNumber,String message,String c2code) throws RecordNotFoundException {
        JsonObject smsGatewayObj = smsGatewayConfig(c2code);
        String url = smsGatewayObj.get("url").getAsString();
        String param = smsGatewayObj.get("param").getAsString();

        //STEP 4 - replace argss and send sms
        String replaceParam = param.replace("to=|mobno|&sender=LIVCON&message=|message|", "to=" + mobileNumber + "&sender=LIVCON&message=" + message);
        return callWebclient(url, replaceParam);
    }
    protected JsonObject smsGatewayConfig(String c2Code) throws RecordNotFoundException {
        String sql = "SELECT c_url, c_param FROM lc_sms_gateway_config WHERE n_active= 0 AND c_c2code = :c2Code";
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", c2Code);

        JsonObject jsonObject = new JsonObject();
        List<Object[]> resultList = this.getResultList(query);

        if(resultList.isEmpty()){
            throw new RecordNotFoundException("Default SMS GateWay Not Found ! ");
        }
        for (Object[] obj : resultList) {
            jsonObject.addProperty("url", obj[0].toString());
            jsonObject.addProperty("param", obj[1].toString());
        }
        return jsonObject;
    }

    protected int callWebclient(String url, String replaceNumber) {
        String ret="";
        int result = 0; //1 - sucess , 0 - false
        try{
            ret = this.getWebClient().build()
                    .method(HttpMethod.GET)
                    .uri(url+replaceNumber)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .flatMap(clientResponse -> clientResponse.bodyToMono(String.class)).block();
            result = 1;
        }catch (Exception e){
            ret = e.getMessage();
        }
        log.debug("Response From SMS API {}",ret);
        return result;
    }
}
