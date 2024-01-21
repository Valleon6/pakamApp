package com.valleon.pakamapp.utils;

import com.valleon.pakamapp.exception.ApiRequestException;
import com.valleon.pakamapp.exception.Message;

public class CheckValidation {

    public static void checkPhoneNumber(String phoneNumber) throws Exception {
        if (!phoneNumber.matches(AppConstants.REGEX_PHONE_NUMBER)) {
            throw new ApiRequestException(Message.PHONE_NOT_VALID);
        }
    }

    public static void checkEmail(String email) throws Exception {
        if (!email.matches(AppConstants.REGEX_EMAIL)) {
            throw new Exception(Message.EMAIL_NOT_VALID);
        }
    }

    public static void checkPassword(String password) throws Exception {
        if (!password.matches(AppConstants.REGEX_PASSWORD)) {
            throw new Exception(Message.PASSWORD_ERROR);
        }
    }
}
