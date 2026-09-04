package com.c2.lc.ms.security.services.Interface;

import com.c2.lc.lib.exceptions.SessionExpiredException;
import com.c2.lc.ms.security.bos.ChangePasswordBO;
import com.c2.lc.ms.security.entities.LcUserEntity;
import com.c2.lc.ms.security.bos.UpdatePasswordBO;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface LcUserService {

    void updatePassword(UpdatePasswordBO updatePasswordBO) throws RecordNotFoundException, InvalidRequestException, NoSuchAlgorithmException, InvalidKeyException, SessionExpiredException;//verify request will have new password

    void forgotPassword(ChangePasswordBO changePasswordBO) throws RecordNotFoundException, InvalidRequestException, NoSuchAlgorithmException, InvalidKeyException, SessionExpiredException;//verify request will have new password

    void createLcUser(LcUserEntity lcUserEntity) throws InvalidRequestException, NoSuchAlgorithmException, InvalidKeyException, DuplicateRecordException;

    LcUserEntity login(String mobileNo, String pwd, String type) throws RecordNotFoundException, NoSuchAlgorithmException, InvalidKeyException;

}
