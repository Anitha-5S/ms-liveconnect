package com.c2.lc.ms.customer.services.interfaces;

import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.entities.customer.FirmEntity;
import com.c2.lc.ms.customer.services.interfaces.base.LcBaseService;
import com.google.gson.JsonObject;

public interface FirmDefaultService extends LcBaseService {

    FirmEntity getDefaultFirm(Long userId) throws RecordNotFoundException;

    void setDefaultFirm(Long userId, Long branchCode) throws RecordNotFoundException;

    Boolean isBranchExist(Long userId, Long branchCode) throws RecordNotFoundException;

    JsonObject callC2Service(JsonObject request) throws CommunicationErrorException, InvalidRequestException;
}
