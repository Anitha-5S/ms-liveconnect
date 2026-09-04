package com.c2.lc.ms.security.transactions.inteface;

import com.c2.lc.lib.exceptions.SessionExpiredException;
import com.c2.lc.lib.exceptions.UnAuthorizedException;
import com.c2.lc.ms.security.bos.ChangePasswordBO;
import com.c2.lc.ms.security.entities.LcUserEntity;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.security.bos.UpdatePasswordBO;
import com.google.gson.JsonObject;

import javax.transaction.Transactional;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface LcAuthTransaction {

    JsonObject register(LcUserEntity lcUserEntity) throws Exception;

    JsonObject login(String mobileNumber, String password, String type) throws Exception;


    @Transactional(rollbackOn = Exception.class)
    void logout(String key) throws Exception;

    void updatePassword(UpdatePasswordBO updatePasswordBO) throws InvalidRequestException, RecordNotFoundException, InvalidKeyException, NoSuchAlgorithmException, SessionExpiredException;

    void forgotPassword(ChangePasswordBO changePasswordBO) throws InvalidRequestException, RecordNotFoundException, InvalidKeyException, NoSuchAlgorithmException, SessionExpiredException;

    String[]  validateToken(String key, String token) throws SessionExpiredException, UnAuthorizedException, InvalidRequestException;
}
