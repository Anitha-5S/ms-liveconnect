package com.c2.lc.ms.security.services.Interface;

import com.c2.lc.ms.security.entities.LcUserAttemptsEntity;

public interface LcUserAttemptsService {

    void updateFailAttempts(String userName);

    void resetFailAttempts(String userName);

    LcUserAttemptsEntity getUserAttempts(String userName);
}
