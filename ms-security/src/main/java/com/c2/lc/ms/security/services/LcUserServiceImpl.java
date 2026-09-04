package com.c2.lc.ms.security.services;

import com.c2.lc.lib.exceptions.SessionExpiredException;
import com.c2.lc.ms.security.bos.ChangePasswordBO;
import com.c2.lc.ms.security.entities.LcUserEntity;
import com.c2.lc.ms.security.bos.UpdatePasswordBO;
import com.c2.lc.ms.security.repos.LcUserRepository;
import com.c2.lc.ms.security.services.Interface.LcOtpService;
import com.c2.lc.ms.security.services.base.SecurityBaseServiceImpl;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.security.services.Interface.LcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class LcUserServiceImpl extends SecurityBaseServiceImpl implements LcUserService {

    @Autowired private LcUserRepository lcUserRepository;
    @Autowired private LcOtpService lcOtpService;

    @Value("${secret.key}")
    private String secretKey;
    @Value("${hash.algorithm}")
    private String hMacAlgorithm;

    @Override
    public void updatePassword(UpdatePasswordBO updatePasswordBO) throws RecordNotFoundException, InvalidRequestException, NoSuchAlgorithmException, InvalidKeyException, SessionExpiredException {

        //decode password and set back to same model

        updatePasswordBO.setOldPassword(getEncodedPassword(updatePasswordBO.getOldPassword()));

        lcOtpService.verifyOTP(updatePasswordBO.getMobileNumber(), updatePasswordBO.getOTP());

        if (!this.isPasswordValid(helper.getString(updatePasswordBO.getNewPassword()))) {
            throw new InvalidRequestException("Invalid Password! ", "New password must be at least 4 characters " +
                    "one number and one special character");
        }

        updatePasswordBO.setNewPassword(getEncodedPassword(updatePasswordBO.getNewPassword()));

        LcUserEntity lcUserEntity = lcUserRepository.findByMobileNumberAndType(updatePasswordBO.getMobileNumber(), updatePasswordBO.getType());
        if (lcUserEntity == null) {
            throw new RecordNotFoundException("Invalid Username! Please enter the Valid Username");
        }
        String oldPassword = updatePasswordBO.getOldPassword();

        if (!helper.isEmpty(oldPassword)) {
            String password = lcUserEntity.getPassword();

            if (this.matchesData(oldPassword, updatePasswordBO.getNewPassword())) {
                throw new InvalidRequestException("Invalid New Password! ", "Old password and new password can't be same");
            }

            if (!this.matchesData(oldPassword, password)) {
                throw new InvalidRequestException("Invalid Old Password! ", "Please enter valid old password");
            }
        }

        lcUserEntity.setPassword(updatePasswordBO.getNewPassword());
        save(lcUserEntity);
    }

    @Override
    public void forgotPassword(ChangePasswordBO changePasswordBO) throws RecordNotFoundException, InvalidRequestException, NoSuchAlgorithmException, InvalidKeyException, SessionExpiredException {

        //decode password and set back to same model
        lcOtpService.verifyOTP(changePasswordBO.getMobileNumber(), changePasswordBO.getOTP());

        if (!this.isPasswordValid(helper.getString(changePasswordBO.getNewPassword()))) {
            throw new InvalidRequestException("Invalid Password! ", "New password must be at least 4 characters " +
                    "one number and one special character");
        }

        changePasswordBO.setNewPassword(getEncodedPassword(changePasswordBO.getNewPassword()));

        LcUserEntity lcUserEntity = lcUserRepository.findByMobileNumberAndType(changePasswordBO.getMobileNumber(), changePasswordBO.getType());
        if (lcUserEntity == null) {
            throw new RecordNotFoundException("Invalid Username! Please enter the Valid Username");
        }

        lcUserEntity.setPassword(changePasswordBO.getNewPassword());
        save(lcUserEntity);
    }

    private String getEncodedPassword(String newPassword) throws InvalidKeyException, NoSuchAlgorithmException {
        return helper.generateHMacHash(hMacAlgorithm, secretKey, newPassword);
    }

    private void save(LcUserEntity lcUserEntity) {
        lcUserRepository.save(lcUserEntity);
    }

    @Override
    public void createLcUser(LcUserEntity lcUserEntity) throws InvalidRequestException, NoSuchAlgorithmException, InvalidKeyException, DuplicateRecordException {

        //validatePass(lcUserEntity);

        String mobileNumber = lcUserEntity.getMobileNumber();
        LcUserEntity userEntity;

        userEntity = lcUserRepository.findByMobileNumberAndType(mobileNumber, lcUserEntity.getType());
        if (userEntity != null) {
            throw new DuplicateRecordException("User Already Exists! ", mobileNumber + "");
        }

        lcUserEntity.setPassword(getEncodedPassword(lcUserEntity.getPassword()));
        lcUserEntity.setStatus(Constants.STATUS_ACTIVE);
        save(lcUserEntity);
    }

    private void validatePass(LcUserEntity lcUserEntity) throws InvalidRequestException {
        lcUserEntity.setPassword(helper.getDecodedString(lcUserEntity.getPassword()));

        if (!this.isPasswordValid(lcUserEntity.getPassword())) {
            throw new InvalidRequestException("Invalid Password! ", "New password must be at least 4 characters " +
                    " one number and one special character");
        }
    }

    public LcUserEntity login(String mobileNo, String pwd, String type) throws RecordNotFoundException, NoSuchAlgorithmException, InvalidKeyException {
        String password = getEncodedPassword(pwd);
        LcUserEntity lcUserEntity = lcUserRepository.findByMobileNumberAndPasswordAndType(mobileNo, password, type);
        if (lcUserEntity == null){
            throw new RecordNotFoundException(mobileNo, "Record not found!");
        }
        return lcUserEntity;
    }
 
}
