package com.valleon.pakamapp.modules.authentication.service.impl;


import com.valleon.pakamapp.config.JwtUtil;
import com.valleon.pakamapp.exception.ApiRequestException;
import com.valleon.pakamapp.exception.Codes;
import com.valleon.pakamapp.exception.Message;
import com.valleon.pakamapp.modules.authentication.entity.CustomUserDetails;
import com.valleon.pakamapp.modules.authentication.entity.Role;
import com.valleon.pakamapp.modules.authentication.security.CustomUserDetailsService;
import com.valleon.pakamapp.modules.authentication.service.AuthService;
import com.valleon.pakamapp.modules.authentication.service.EmailService;
import com.valleon.pakamapp.modules.customer.entity.Customer;
import com.valleon.pakamapp.modules.customer.repository.CustomerRepository;
import com.valleon.pakamapp.modules.payload.*;
import com.valleon.pakamapp.utils.CheckValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class AuthServiceImpl implements AuthService {

    private final CustomUserDetailsService customUserDetailsService;

    private final AuthenticationManager authenticationManager;

    private final CustomerRepository customerRepository;

    private final EmailService emailService;

    @Value("${pekam.login.url:}") // Frontend login page url
    String pakamLoginPageUrl;

    @Value("${frontend.reset.password.url:}") // Frontend reset password page url
    String resetPasswordPageUrl;

    @Override
    public ResponseMessage login(LoginDTO loginDTO) throws Exception {
        LocalDateTime time = LocalDateTime.now();
        CheckValidation.checkEmail(loginDTO.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getEmail(), loginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            throw new Exception(Message.LOGIN_ERROR, e);
        }
        final CustomUserDetails user = customUserDetailsService.loadUserByUsername(loginDTO.getEmail());
        Customer customer = customerRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new ApiRequestException(Message.CUSTOMER_NOT_FOUND));
        Boolean active = customer.getIsActive();
        customerRepository.save(customer);
        if (active) {
            return new ResponseMessage(time, Codes.SUCCESS, Message.LOGIN_SUCCESS, JWTAuthResponse.builder().accessToken(JwtUtil.generateToken(user)).tokenType("Bearer").build());
        } else {
            return new ResponseMessage(time, Codes.USER_NOT_ACTIVE, Message.NOT_ACTIVATE_CUSTOMER, null);
        }
    }

    @Override
    public ResponseMessage register(HttpServletRequest request, RegisterDTO registerDTO) throws Exception {
        LocalDateTime time = LocalDateTime.now();
        CheckValidation.checkPhoneNumber(registerDTO.getPhoneNumber());
        CheckValidation.checkEmail(registerDTO.getEmail());
        CheckValidation.checkPassword(registerDTO.getPassword());
        try {
            if (customerRepository.existsByEmail(registerDTO.getEmail())) {
                return new ResponseMessage(time, Codes.USER_EXIST, Message.EMAIL_EXIST, null);
            }
            Customer customer = new Customer();
            customer.setCustomerCode("CUS-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase());
            customer.setFirstName(registerDTO.getFirstName());
            customer.setLastName(registerDTO.getLastName());
            customer.setEmail(registerDTO.getEmail());
            customer.setPhone(registerDTO.getPhoneNumber());

            int strength = 10;
            BCryptPasswordEncoder bCryptPasswordEncoder =
                    new BCryptPasswordEncoder(strength, new SecureRandom());
            customer.setPassword(bCryptPasswordEncoder.encode(registerDTO.getPassword()));

            Set<Role> role = new HashSet<>();
            Set<Customer> customers = new HashSet<>();
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setCustomers(customers);
            role.add(userRole);
            customer.setRole(role);
            customer.setIsActive(false);
            customerRepository.save(customer);

            String token = UUID.randomUUID().toString();
            customUserDetailsService.createEmailValidationTokenForUser(customer, token);
            String apiPath = "/customer/activate?token=";
            String url = request.getRequestURL() + apiPath + token;
            System.out.println(url);
            try {
                //Using an email service to send the activation url to the registered email
                emailService.constructValidationEmail(request.getRequestURL().toString(), apiPath, token, customer);
            } catch (Exception e) {
                throw new Exception(Message.EMAIL_SEND_ERROR + ": " + e);
            }
//            Manually activate customer for testing purposes'
//            customer.setIsActive(true);
//            customerRepository.save(customer);

            //Attaching token to response payload for testing purposes
            return new ResponseMessage(time, Codes.SUCCESS, Message.SUCCESS_ACCOUNT_CREATION + ". TOKEN: " + token, customer);
        } catch (Exception e) {
            return new ResponseMessage(time, Codes.SERVER_ERROR, Message.ERROR_ACCOUNT_CREATION, e.getMessage());
        }
    }

    @Override
    public ResponseMessage resetPassword(HttpServletRequest request, ResetDTO resetDTO) throws Exception {
        LocalDateTime time = LocalDateTime.now();
        try {
            CheckValidation.checkEmail(resetDTO.getEmail());
            Customer customer = customerRepository.findByEmail(resetDTO.getEmail())
                    .orElseThrow(() -> new ApiRequestException(Message.CUSTOMER_NOT_FOUND));
            String token = UUID.randomUUID().toString();
            customUserDetailsService.createPasswordResetTokenForUser(customer, token);
            String url = resetPasswordPageUrl + "?token=";
            try {
                emailService.constructResetTokenEmail(url, token, customer);
            } catch (Exception e) {
                throw new Exception(Message.EMAIL_SEND_ERROR + ": " + e);
            }
            //Attaching token to response payload for testing purposes
            return new ResponseMessage(time, Codes.SUCCESS, Message.SUCCESS_PASSWORD_RESET_EMAIL + ". RESET LINK: " + url + token, null);
        } catch (Exception e) {
            return new ResponseMessage(time, Codes.SERVER_ERROR, Message.ERROR_PASSWORD_RESET, e.getMessage());
        }
    }

    @Override
    public ResponseMessage activateUser(HttpServletResponse response, Locale locale, Model model, String token) throws Exception {
        LocalDateTime time = LocalDateTime.now();
        String result = customUserDetailsService.validateEmailValidationToken(token);
        if (result != null) {
            return new ResponseMessage(time, Codes.TOKEN_NOT_FOUND, Message.INVALID_TOKEN, null);
        } else {
            Customer customer = customerRepository.getUserByEmailValidationToken(token)
                    .orElseThrow(() -> new ApiRequestException(Message.CUSTOMER_NOT_FOUND));
            if (customer.getIsActive())
                return new ResponseMessage(time, Codes.TOKEN_NOT_FOUND, Message.ALREADY_ACTIVATE_CUSTOMER, null);
            customer.setIsActive(true);
            customerRepository.save(customer);
            try {
                emailService.sendRegistrationEmail(customer.getEmail(), customer.getFirstName());
            } catch (Exception e) {
                throw new Exception(Message.EMAIL_SEND_ERROR + ": " + e);
            }
            //The below is meant to redirect the customer to the Login page.
//            response.sendRedirect(pakamLoginPageUrl);
            return new ResponseMessage(time, Codes.SUCCESS, Message.ACTIVATE_CUSTOMER, customer);
        }
    }

    @Override
    public ResponseMessage savePassword(PasswordDTO passwordDto) throws Exception {
        LocalDateTime time = LocalDateTime.now();
        String result = customUserDetailsService.validatePasswordResetToken(passwordDto.getToken());
        if (result != null) {
            return new ResponseMessage(time, Codes.TOKEN_NOT_FOUND, result, null);
        }
        Customer customer = customerRepository.getUserByPasswordResetToken(passwordDto.getToken())
                .orElseThrow(() -> new ApiRequestException(Message.CUSTOMER_NOT_FOUND));
        if (customUserDetailsService.isChangeUserPassword(customer, customer.getPassword(), passwordDto.getNewPassword())) {
            return new ResponseMessage(time, Codes.SUCCESS, Message.SUCCESS_PASSWORD_RESET, null);
        } else {
            return new ResponseMessage(time, Codes.SERVER_ERROR, Message.ERROR_PASSWORD_RESET, null);
        }
    }
}
