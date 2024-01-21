package com.valleon.pakamapp.modules.authentication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valleon.pakamapp.modules.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@RequiredArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Date expiryDate;

    public PasswordResetToken(String token, Customer customer) {
    }
    @Column(name = "date_created")
    private LocalDateTime dateCreated = LocalDateTime.now();
}

