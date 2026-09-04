package com.c2.lc.ms.customer.transactions.interfaces;

import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.entities.comm.LcUser;
import com.google.gson.JsonObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface LcUserTransaction {

    void saveUser(LcUser lcUser) throws DuplicateRecordException, NoSuchAlgorithmException, InvalidKeyException;


    LcUser getLcUser(String mobileNumber) throws RecordNotFoundException;

    LcUser getLcUserType(String mobileNumber, String b);

    void delete (String mobile) throws RecordNotFoundException;

    boolean checkTSUserExist(String mobileNo, String c2code, String type);

    JsonObject getCustCount(String c2Code, String branch, String d_from_date, String d_to_date) throws RecordNotFoundException;

    List<JsonObject> fetchAllCustomers(String c2Code, SearchBO searchBO, JsonObject req) throws RecordNotFoundException;

    void updateCustStatus(JsonObject req);

    int fetchAllCustomersCount(String c2Code, SearchBO searchBO, JsonObject req) throws RecordNotFoundException;

    int getLoginCount(String c2Code, String brCode, String terminalId, String type);
}
