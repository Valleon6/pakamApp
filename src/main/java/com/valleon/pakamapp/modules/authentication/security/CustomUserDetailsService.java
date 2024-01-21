package com.valleon.pakamapp.modules.authentication.security;

import com.valleon.pakamapp.exception.ApiRequestException;
import com.valleon.pakamapp.exception.Message;
import com.valleon.pakamapp.modules.authentication.entity.CustomUserDetails;
import com.valleon.pakamapp.modules.authentication.entity.EmailValidationToken;
import com.valleon.pakamapp.modules.authentication.entity.PasswordResetToken;
import com.valleon.pakamapp.modules.authentication.repository.EmailTokenRepository;
import com.valleon.pakamapp.modules.authentication.repository.PasswordTokenRepository;
import com.valleon.pakamapp.modules.customer.entity.Customer;
import com.valleon.pakamapp.modules.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private CustomerRepository customerRepository;

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Autowired
    private EmailTokenRepository emailTokenRepository;

    public CustomUserDetailsService(CustomerRepository userRepository) {
        this.customerRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Customer user = customerRepository.findByEmail(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(Message.CUSTOMER_NOT_FOUND));

        Set<GrantedAuthority> authorities = user
                .getRole()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

        CustomUserDetails customerUserDetail = new CustomUserDetails();
        customerUserDetail.setUser(user);
        customerUserDetail.setAuthorities(authorities);
        return customerUserDetail;
    }


    public void createPasswordResetTokenForUser(Customer customer, String token) {
        final Calendar cal = Calendar.getInstance();
        PasswordResetToken myToken = new PasswordResetToken(token, customer);
        myToken.setCustomer(customer);
        myToken.setToken(token);
        cal.add(Calendar.MINUTE, 60 * 24);
        myToken.setExpiryDate(cal.getTime());
        passwordTokenRepository.save(myToken);

    }

    public void createEmailValidationTokenForUser(Customer customer, String token) {
        final Calendar cal = Calendar.getInstance();
        EmailValidationToken myToken = new EmailValidationToken();
        myToken.setCustomer(customer);
        myToken.setToken(token);
        cal.add(Calendar.MINUTE, 60 * 24);
        myToken.setExpiryDate(cal.getTime());
        emailTokenRepository.save(myToken);

    }

    public String validatePasswordResetToken(String token) {
        LocalDateTime time = LocalDateTime.now();
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApiRequestException(Message.INVALID_TOKEN));

        return !isPasswordTokenFound(passToken) ? "invalidToken"
                : isPasswordTokenExpired(passToken) ? "expired"
                : null;
    }

    public String validateEmailValidationToken(String token) {
        LocalDateTime time = LocalDateTime.now();
        final EmailValidationToken emailToken = emailTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApiRequestException(Message.INVALID_TOKEN));

        return !isEmailTokenFound(emailToken) ? "invalidToken"
                : isEmailTokenExpired(emailToken) ? "expired"
                : null;
    }

    private boolean isPasswordTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isPasswordTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

    private boolean isEmailTokenFound(EmailValidationToken passToken) {
        return passToken != null;
    }

    private boolean isEmailTokenExpired(EmailValidationToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

    public boolean isPasswordMatch(String oldPassword, String newPassword) {
        int strength = 10;
        BCryptPasswordEncoder bCryptPasswordEncoder =
                new BCryptPasswordEncoder(strength, new SecureRandom());
        boolean passwordsMatch = bCryptPasswordEncoder.matches(oldPassword, newPassword);
        if (passwordsMatch) {
            return true;
        } else {
            return false;
        }
    }
}
