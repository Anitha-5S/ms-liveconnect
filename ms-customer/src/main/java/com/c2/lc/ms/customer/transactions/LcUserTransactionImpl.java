package com.c2.lc.ms.customer.transactions;

import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.entities.comm.LcUser;
import com.c2.lc.ms.customer.services.interfaces.LcUserService;
import com.c2.lc.ms.customer.services.interfaces.UserService;
import com.c2.lc.ms.customer.transactions.interfaces.LcUserTransaction;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class LcUserTransactionImpl implements LcUserTransaction {

    @Autowired private LcUserService lcUserService;
    @Autowired private UserService userService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void saveUser(LcUser lcUser) throws DuplicateRecordException, NoSuchAlgorithmException, InvalidKeyException {
        lcUserService.saveUser(lcUser);
    }

    @Override
    public LcUser getLcUser(String mobileNumber) throws RecordNotFoundException {
        return lcUserService.getLcUser(mobileNumber);
    }

    @Override
    public LcUser getLcUserType(String mobileNumber, String b) {
        return null;
    }

    @Override
    public void delete(String mobile) throws RecordNotFoundException {
        lcUserService.delete(mobile);
    }

    @Override
    public boolean checkTSUserExist(String mobileNo, String c2code, String type) {
        return lcUserService.checkTSUserExist(mobileNo, c2code, type);
    }

    @Override
    public JsonObject getCustCount(String c2Code, String branch, String d_from_date, String d_to_date) throws RecordNotFoundException {
        return lcUserService.getCustCount(c2Code,branch,d_from_date,d_to_date);
    }

    @Override
    public List<JsonObject> fetchAllCustomers(String c2Code, SearchBO searchBO, JsonObject req) throws RecordNotFoundException {
        return lcUserService.fetchAllCustomers(c2Code, searchBO, req);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void updateCustStatus(JsonObject req) {
        lcUserService.updateStatus(req.get("n_user_id").getAsLong(), req.get("c_status").getAsString());
        userService.updateStatus(req.get("n_user_id").getAsLong(), req.get("c_status").getAsString());
    }

    @Override
    public int fetchAllCustomersCount(String c2Code, SearchBO searchBO, JsonObject req) throws RecordNotFoundException {
        return lcUserService.count(c2Code, searchBO, req);
    }

    @Override
    public int getLoginCount(String c2Code, String brCode, String terminalId, String type) {
        return lcUserService.getLoginCount(c2Code, brCode, terminalId, type);
    }
}
