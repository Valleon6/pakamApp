package com.valleon.pakamapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.valleon.pakamapp.exception.Codes;
import com.valleon.pakamapp.exception.Message;
import com.valleon.pakamapp.exception.ResourceNotFoundException;
import com.valleon.pakamapp.modules.customer.entity.Customer;
import com.valleon.pakamapp.modules.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import java.io.OutputStream;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTAuthFilter jwtAuthFilter;
    private final CustomerRepository customerRepository;

    @Autowired
    void configureObjectMapper(final ObjectMapper mapper) {
        mapper.registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests()
                .requestMatchers(
                        "/**/auth/**",
                        "/**/assessment/**",
                        "/swagger-resources/**",
                        "/swagger-ui/**",
                        "/v3/api-docs",
                        "/webjars/**",
                        "/actuator/**"
                )
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value()); // 403
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> responseMap = new HashMap<>();
                    responseMap.put("error", true);
                    responseMap.put("message", Message.INVALID_TOKEN);

                    OutputStream out = response.getOutputStream();
                    mapper.writeValue(out, responseMap);
                    out.flush();
                })
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder((passwordEncoder()));
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        int strength = 10; // work factor of bcrypt
        return new BCryptPasswordEncoder(strength, new SecureRandom());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String usernameOrEmail) throws ResourceNotFoundException {
                LocalDateTime time = LocalDateTime.now();
                Customer user = customerRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(time, Codes.USER_NOT_FOUND, Message.USER_NOT_FOUND));

                Set<GrantedAuthority> authorities = user
                        .getRole()
                        .stream()
                        .map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

                return new org.springframework.security.core.userdetails.User(user.getEmail(),
                        user.getPassword(),
                        authorities);
            }
        };
    }
}
