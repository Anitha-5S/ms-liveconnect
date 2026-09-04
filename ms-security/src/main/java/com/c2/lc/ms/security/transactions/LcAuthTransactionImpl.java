package com.c2.lc.ms.security.transactions;

import com.c2.lc.lib.exceptions.SessionExpiredException;
import com.c2.lc.lib.exceptions.UnAuthorizedException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.ms.security.bos.ChangePasswordBO;
import com.c2.lc.ms.security.entities.LcUserEntity;
import com.c2.lc.ms.security.services.Interface.LcSessionService;
import com.c2.lc.ms.security.transactions.inteface.LcAuthTransaction;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.security.bos.UpdatePasswordBO;
import com.c2.lc.ms.security.services.Interface.LcOtpService;
import com.c2.lc.ms.security.services.Interface.LcUserService;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class LcAuthTransactionImpl implements LcAuthTransaction {

    @Autowired private LcSessionService lcSessionService;
    @Autowired private LcUserService lcUserService;
    @Autowired private LcOtpService lcOtpService;


    @Transactional(rollbackOn = Exception.class)
    @Override
    public JsonObject register(LcUserEntity lcUserEntity) throws Exception {
        lcUserService.createLcUser(lcUserEntity);
        return lcSessionService.createSession(lcUserEntity);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public JsonObject login(String mobileNumber, String password, String type) throws Exception {
        LcUserEntity lcUserEntity = lcUserService.login(mobileNumber, password, type);
        return lcSessionService.createSession(lcUserEntity);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void logout(String key) throws Exception {
        lcSessionService.logout(key);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updatePassword(UpdatePasswordBO updatePasswordBO) throws InvalidRequestException, RecordNotFoundException, InvalidKeyException, NoSuchAlgorithmException, SessionExpiredException {
        lcUserService.updatePassword(updatePasswordBO);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void forgotPassword(ChangePasswordBO changePasswordBO) throws InvalidRequestException, RecordNotFoundException, InvalidKeyException, NoSuchAlgorithmException, SessionExpiredException {
        lcUserService.forgotPassword(changePasswordBO);
    }

    @Override
    public String[] validateToken(String key, String token) throws SessionExpiredException, UnAuthorizedException, InvalidRequestException {
        String[] decrypted = {"-", "-", "-", "-", "-"};
        try {
            decrypted = lcSessionService.getDecryptedKeyValues(key);
        } catch (Exception e) {
            throw new InvalidRequestException(key, Messages.INVALID_REQUEST);
        }
        //TODO uncomment below
        //lcSessionService.validateToken(key, token);
        return decrypted;    }
}
