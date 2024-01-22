package com.valleon.pakamapp.modules.controller;

import com.valleon.pakamapp.modules.authentication.service.AuthService;
import com.valleon.pakamapp.modules.payload.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseMessage login(@Valid @RequestBody LoginDTO loginDTO) throws Exception {
        return authService.login(loginDTO);
    }

    @PostMapping("/register")
    public ResponseMessage register(HttpServletRequest request, @Valid @RequestBody RegisterDTO registerDTO) throws Exception {
        return authService.register(request, registerDTO);
    }

    @PostMapping("/customer/reset-password")
    public ResponseMessage resetPassword(HttpServletRequest request, @Valid @RequestParam("email") ResetDTO resetDTO) throws Exception {
        return authService.resetPassword(request, resetDTO);
    }

    @GetMapping("/register/customer/activate")
    public ResponseMessage activateCustomer(HttpServletResponse response, Locale locale, Model model, @RequestParam("token") String token) throws Exception {
        return authService.activateUser(response, locale, model, token);
    }

    @PostMapping("/user/reset-password/change-password")
    public ResponseMessage changePassword(@Valid @RequestBody PasswordDTO passwordDTO) throws Exception {
        return authService.savePassword(passwordDTO);
    }

}
