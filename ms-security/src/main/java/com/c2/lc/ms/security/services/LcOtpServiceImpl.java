package com.c2.lc.ms.security.services;

import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.security.entities.LcOtpEntity;
import com.c2.lc.ms.security.repos.LcOtpRepository;
import com.c2.lc.ms.security.services.Interface.LcOtpService;
import com.c2.lc.ms.security.services.base.SecurityBaseServiceImpl;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.exceptions.SessionExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
public class LcOtpServiceImpl extends SecurityBaseServiceImpl implements LcOtpService {

    @Autowired private LcOtpRepository lcOtpRepository;

    @Override
    public void saveOtp(String mobileNumber, int otp) {
        LcOtpEntity lcOtpEntity = lcOtpRepository.findByMobileNo(mobileNumber);

        if (lcOtpEntity == null) {
            lcOtpEntity = new LcOtpEntity();
            lcOtpEntity.setMobileNumber(mobileNumber);
        }

        lcOtpEntity.setValidTill(helper.getCurrentTime().plusMinutes(5));
        lcOtpEntity.setNOtp(otp);
        lcOtpEntity.setUsed(Constants.STATUS_NO);
        saveEntity(lcOtpEntity);
    }

    private void saveEntity(LcOtpEntity lcOtpEntity) {
        lcOtpRepository.save(lcOtpEntity);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void verifyOTP(String mobileNumber, int otpRequest) throws RecordNotFoundException, InvalidRequestException, SessionExpiredException {

        LcOtpEntity lcOtpEntity = lcOtpRepository.findByMobileNo(mobileNumber);
        if (lcOtpEntity == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        int otpResult = lcOtpEntity.getNOtp();

        if (lcOtpEntity.getUsed().equals(Constants.STATUS_YES)) {
            throw new SessionExpiredException(otpRequest + "", Messages.OTP_ALREADY_USED);
        } else if (otpResult != otpRequest) {
            throw new InvalidRequestException(otpRequest + "", Messages.INVALID_OTP);
        } else if (lcOtpEntity.getValidTill().isBefore(helper.getCurrentTime())) {
            throw new SessionExpiredException(otpRequest + "", Messages.OTP_EXPIRED);
        }
        lcOtpEntity.setUsed(Constants.STATUS_YES);
        saveEntity(lcOtpEntity);
    }
}
