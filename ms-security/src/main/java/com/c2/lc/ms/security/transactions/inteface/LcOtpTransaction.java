package com.c2.lc.ms.security.transactions.inteface;

import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.exceptions.SessionExpiredException;
import com.c2.lc.lib.transactions.interfaces.BaseTransaction;
import com.c2.lc.ms.security.bos.UpdatePasswordBO;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface LcOtpTransaction extends BaseTransaction {
    void sendOtp(String mobileNumber);
    void verifyOTP(String mobileNo, int otp) throws InvalidRequestException, RecordNotFoundException, SessionExpiredException, InvalidKeyException, NoSuchAlgorithmException;
    void updatePassword(UpdatePasswordBO updatePasswordBO) throws InvalidRequestException, RecordNotFoundException, SessionExpiredException, InvalidKeyException, NoSuchAlgorithmException;
}
