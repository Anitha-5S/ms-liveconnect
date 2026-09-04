package com.c2.lc.ms.customer.services;

import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.entities.customer.FirmDefaultEntity;
import com.c2.lc.ms.customer.entities.customer.FirmEntity;
import com.c2.lc.ms.customer.repos.customer.FirmDefaultRepo;
import com.c2.lc.ms.customer.repos.customer.FirmRepo;
import com.c2.lc.ms.customer.repos.customer.UserDetailRepo;
import com.c2.lc.ms.customer.services.base.LcBaseServiceImpl;
import com.c2.lc.ms.customer.services.interfaces.FirmDefaultService;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FirmDefaultServiceImpl extends LcBaseServiceImpl implements FirmDefaultService {

    @Autowired private FirmDefaultRepo firmDefaultRepo;
    @Autowired private UserDetailRepo userDetailRepo;
    @Autowired private FirmRepo firmRepo;

    @Value("${create.token.api}")
    private String createTokenApi;

    @Override
    public FirmEntity getDefaultFirm(Long userId) throws RecordNotFoundException {
        FirmDefaultEntity firmDefaultEntity = firmDefaultRepo.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(userId, "Record not found!"));
        return firmDefaultEntity.getFirm();
    }

    @Override
    public void setDefaultFirm(Long userId, Long branchCode) throws RecordNotFoundException {
        FirmDefaultEntity firmDefaultEntity = firmDefaultRepo.findById(userId).orElse(null);
        if (firmDefaultEntity == null) {
            firmDefaultEntity = new FirmDefaultEntity(userId, helper.getCurrentTime());
            firmDefaultEntity.setNUserId(userId);
        } else {
            firmDefaultEntity.setNLastUpdatedBy(userId);
            firmDefaultEntity.setTLastUpdatedAt(helper.getCurrentTime());
        }
        firmDefaultEntity.setFirm(firmRepo.getOne(branchCode));
        firmDefaultRepo.save(firmDefaultEntity);
    }

    @Override
    public Boolean isBranchExist(Long userId, Long branchCode) throws RecordNotFoundException {
        FirmEntity firm = firmRepo.findById(branchCode).orElse(null);
        if (helper.isNull(firm)) {
            throw new RecordNotFoundException("No branch found");
        } else if (!firm.getNCreatedBy().equals(userId)) {
            throw new RecordNotFoundException("Target branch User Id and Current User Id are different!");
        }
        return true;
    }

    @Override
    public JsonObject callC2Service(JsonObject request) throws CommunicationErrorException, InvalidRequestException {

        String result = callWebClientPostSyncApi(createTokenApi, request.toString());
        log.debug("C2 Service Response : {}" + result);

        JsonObject responseObject;
        if (result == null || result.isEmpty()) {
            log.error("Result is null API {} -- Request {} -- Response {}", createTokenApi, request, result);
            throw new CommunicationErrorException("", "Error connecting to C2 Service!");
        } else {
            responseObject = helper.getJsonObject(result);
            if (responseObject.get("appStatusCode").getAsInt() != 0) {
                log.error("API {} -- Request {} -- Response {}", createTokenApi, request, result);
                throw new InvalidRequestException("", "Invalid Request!");
            } else {
                log.debug("Response {}", result);
            }
        }
        return responseObject.get("payloadJson").getAsJsonObject();
    }
}
