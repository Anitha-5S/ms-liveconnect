package com.c2.lc.ms.security.services.Interface;

import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.exceptions.SessionExpiredException;

public interface LcOtpService  {

    void verifyOTP(String mobileNumber, int otpRequest) throws RecordNotFoundException, InvalidRequestException, SessionExpiredException;
    void saveOtp(String mobileNumber, int otp);

}
