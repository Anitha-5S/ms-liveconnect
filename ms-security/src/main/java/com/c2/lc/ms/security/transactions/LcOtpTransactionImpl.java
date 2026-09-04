package com.c2.lc.ms.security.transactions;

import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.exceptions.SessionExpiredException;
import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.ms.security.bos.UpdatePasswordBO;
import com.c2.lc.ms.security.services.Interface.LcOtpService;
import com.c2.lc.ms.security.services.Interface.LcUserAttemptsService;
import com.c2.lc.ms.security.services.Interface.LcUserService;
import com.c2.lc.ms.security.transactions.inteface.LcOtpTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class LcOtpTransactionImpl extends BaseTransactionImpl implements LcOtpTransaction {

    @Autowired private LcOtpService lcOtpService;
    @Autowired private LcUserService lcUserService;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void sendOtp(String mobileNumber){
        int otp = generateOtp();
        lcOtpService.saveOtp(mobileNumber, otp);
        // TODO use communication service
        //lcOtpService.sendOtp(otp);
    }

    private int generateOtp() {
        //TODO generate random 4 digit value
        return 1111;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void verifyOTP(String mobileNo, int otp) throws SessionExpiredException, InvalidRequestException, RecordNotFoundException {
        lcOtpService.verifyOTP(mobileNo, otp);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void updatePassword(UpdatePasswordBO updatePasswordBO) throws InvalidRequestException, RecordNotFoundException, SessionExpiredException, InvalidKeyException, NoSuchAlgorithmException {
        lcUserService.updatePassword(updatePasswordBO);
       // lcUserAttemptsService.resetFailAttempts(updatePasswordBO.ge());
    }
}
