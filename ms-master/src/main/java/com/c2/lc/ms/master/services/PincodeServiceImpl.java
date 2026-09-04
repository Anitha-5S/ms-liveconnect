package com.c2.lc.ms.master.services;

import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.PincodeService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.List;

@Service
public class PincodeServiceImpl extends MasterBaseServiceImpl implements PincodeService {

    @Override
    public JsonArray getStateByPincode(String pinCode) {
        String sql = "SELECT c_code," + " concat('IND',right(concat('000',c_state_code),3))" + ",c_state " +
                        " FROM pincode_mst p " +
                        " WHERE p.c_code LIKE :pinCode";
        Query query = this.getQuery(sql);
        query.setParameter("pinCode", pinCode+"%");

        List<Object[]> resultList = this.getResultList(query);
        JsonArray jsonArray = new JsonArray();
        for (Object[] objects : resultList) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_code", helper.getString(objects[++i]));
            jsonObject.addProperty("c_state_code", helper.getString(objects[++i]));
            jsonObject.addProperty("c_state_name", helper.getString(objects[++i]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
