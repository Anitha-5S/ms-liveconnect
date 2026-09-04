package com.c2.lc.ms.security.services;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.exceptions.SessionExpiredException;
import com.c2.lc.lib.exceptions.UnAuthorizedException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.security.entities.LcSessionEntity;
import com.c2.lc.ms.security.entities.LcUserEntity;
import com.c2.lc.ms.security.repos.LcSessionRepo;
import com.c2.lc.ms.security.services.Interface.LcSessionService;
import com.c2.lc.ms.security.services.base.SecurityBaseServiceImpl;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LcSessionServiceImpl extends SecurityBaseServiceImpl implements LcSessionService {

    @Value("${hash.algorithm}") private String hashAlgorithm;
    @Value("${aes.key}") private String aesKey;
    @Value("${aes.iv}") private String aesIV;

    @Autowired private LcSessionRepo lcSessionRepo;

    @Override
    public void validateToken(String key, String token) throws UnAuthorizedException, SessionExpiredException {
        Optional<LcSessionEntity> session = lcSessionRepo.findById(key);
        if (session.isPresent()) {
            LcSessionEntity entity = session.get();
            if (!entity.getToken().equals(token)) {
                throw new UnAuthorizedException(Messages.UNAUTHORIZED_REQUEST);
            } else if (entity.getStatus().equals("L")) {
                throw new SessionExpiredException(token, Messages.SESSION_INVALID);
            } else if (entity.getStatus().equals("X")) {
                throw new SessionExpiredException(token, Messages.SESSION_INVALID);
            } else if (entity.getValidTill().isBefore(helper.getCurrentTime())) {
                entity.setStatus("X");
                lcSessionRepo.save(entity);
                throw new SessionExpiredException(token, Messages.SESSION_INVALID);
            }
        } else {
            throw new UnAuthorizedException(Messages.UNAUTHORIZED_REQUEST);
        }
    }

    @Override
    public String [] getDecryptedKeyValues(String key) throws Exception {
        return this.aesCbcEncryption.getDecryptedData(key, aesKey, aesIV).split("\\|");
    }

    @Override
    public JsonObject createSession(LcUserEntity entity) throws Exception {
        String key = getEncryptedKeyValue(entity.getNPid() + "|" + entity.getC2Code() + "|" + entity.getBrCode() + "|" + entity.getTerminalId() + "|" + entity.getType());
        String token = getEncryptedKeyValue(helper.generateNonce());
        LocalDateTime dateTime = helper.getCurrentTime();
        dateTime = dateTime.plusYears(1L);
        token = token.length() > 32 ? token.substring(0, 32) : token;

        LcSessionEntity session = lcSessionRepo.findById(key).orElse(null);
        if (session == null) {
            session = new LcSessionEntity();
            session.setKey(key);
        }
        session.setToken(token);
        session.setValidTill(dateTime);
        session.setStatus(Constants.STATUS_ACTIVE);
        lcSessionRepo.save(session);

        JsonObject ret = new JsonObject();
        ret.addProperty("key", key);
        ret.addProperty("value", token);
        return  ret;
    }
    private String getEncryptedKeyValue(String key) throws Exception {
        return this.aesCbcEncryption.getEncryptedData(key, aesKey, aesIV);
    }

    @Override
    public void logout(String key) throws Exception {

        LcSessionEntity session = lcSessionRepo.findById(key).orElse(null);
        if (session == null) {
            throw new RecordNotFoundException("", "Record not found!");
        }
        session.setStatus("L");
        lcSessionRepo.save(session);
    }

}
