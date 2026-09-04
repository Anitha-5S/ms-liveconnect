package com.c2.lc.ms.security.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.security.bos.VerifyOtpBO;
import com.c2.lc.ms.security.configs.MsMessages;
import com.c2.lc.ms.security.controllers.base.LcBaseController;
import com.c2.lc.ms.security.transactions.inteface.LcOtpTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/c2/lc/ms/otp")
public class OtpController extends LcBaseController {

    @Autowired private LcOtpTransaction lcOtpTransaction;

    /**
     * API Id : 3.1.2
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By : selva.sk@c2info.com 2021-07-19 18 10
     */
    @GetMapping(value = "/send/{mobileNumber}", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> sendOtp(@PathVariable String mobileNumber) {
        ApiResponse apiResponse = this.initializeResponse("/c2/lc/ms/otp/send/" + mobileNumber);

        try {
            boolean check = helper.isPhoneNumberFormat(mobileNumber);
            if (check) {
                lcOtpTransaction.sendOtp(mobileNumber);
                this.addMessage(apiResponse, MsMessages.OTP_SENT);
            } else
                this.addMessage(apiResponse, Messages.INVALID_MOBILE_FORMAT);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 3.1.3
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/verify", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> verifyOTP(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/c2/lc/ms/otp/verify " + payload);

        try {
            VerifyOtpBO verifyOtpBO = helper.fromJson(payload, VerifyOtpBO.class);
            this.validateInputPayload(verifyOtpBO);

            lcOtpTransaction.verifyOTP(verifyOtpBO.getMobileNumber(), verifyOtpBO.getOTP());
            this.addMessage(apiResponse, MsMessages.OTP_VERIFIED);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
}