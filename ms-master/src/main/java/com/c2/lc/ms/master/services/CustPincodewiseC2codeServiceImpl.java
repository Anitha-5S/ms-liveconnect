package com.c2.lc.ms.master.services;

import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.CustPincodewiseC2codeService;
import com.c2.lc.ms.master.entities.mysql.CustPincodewiseC2codeEntity;
import com.c2.lc.ms.master.repos.mysql.CustPincodewiseC2codeRepository;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.List;

@Service
public class CustPincodewiseC2codeServiceImpl extends MasterBaseServiceImpl implements CustPincodewiseC2codeService {

    @Autowired
    private CustPincodewiseC2codeRepository custPincodewiseC2codeRepository;

    @Override
    public List<CustPincodewiseC2codeEntity> getByPincode(String pincode) {
        return custPincodewiseC2codeRepository.getByPincode(pincode);
    }

    @Override
    public List<CustPincodewiseC2codeEntity> getByC2code(String c2code) {
        return custPincodewiseC2codeRepository.getByC2Code(c2code);
    }

    @Override
    public JsonObject getAllC2codeByStateCode(String stateCode) throws RecordNotFoundException {

        JsonObject respone = new JsonObject();
        String selectSql = "SELECT distinct (c_c2code) , 1 " +
                "FROM cust_pincodewise_c2code " +
                "WHERE c_pincode IN ( " +
                "        SELECT (c_code) AS c_pincode " +
                "        FROM pincode_mst " +
                "        WHERE c_state_code = :c_state_code " +
                "    )";

        Query query = this.getQuery(selectSql);
        query.setParameter("c_state_code", stateCode);

        List<Object[]> resultList = this.getResultList(query);

        if (resultList.size() > 0) {
            JsonArray list = new JsonArray();
            for (Object[] object : resultList) {
                int i = -1;
                list.add(helper.getString(object[++i]));
            }
            respone.add("data", list);
        } else {
            throw new RecordNotFoundException("c2codes are not mapped for this state code : " + stateCode + " ");
        }
        return respone;
    }
}
