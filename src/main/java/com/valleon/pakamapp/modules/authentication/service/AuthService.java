package com.valleon.pakamapp.modules.authentication.service;

import com.valleon.pakamapp.modules.payload.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;

import java.util.Locale;

public interface AuthService {

    ResponseMessage login(LoginDTO loginDTO) throws Exception;

    ResponseMessage register(HttpServletRequest request, RegisterDTO registerDTO) throws Exception;

    ResponseMessage resetPassword(HttpServletRequest request, ResetDTO resetDTO) throws Exception;

    ResponseMessage activateUser(HttpServletResponse response, Locale locale, Model model, String token) throws Exception;

    ResponseMessage savePassword(PasswordDTO passwordDto) throws Exception;

}
