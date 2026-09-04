package com.c2.lc.ms.security.services.Interface;

import com.c2.lc.lib.exceptions.SessionExpiredException;
import com.c2.lc.lib.exceptions.UnAuthorizedException;
import com.c2.lc.ms.security.entities.LcUserEntity;
import com.c2.lc.ms.security.services.Interface.base.SecurityBaseService;
import com.google.gson.JsonObject;

public interface LcSessionService extends SecurityBaseService {
    void validateToken(String key, String token) throws UnAuthorizedException, SessionExpiredException;

    String [] getDecryptedKeyValues(String key) throws Exception;

    JsonObject createSession(LcUserEntity entity) throws Exception;

    void logout(String key) throws Exception;
}
