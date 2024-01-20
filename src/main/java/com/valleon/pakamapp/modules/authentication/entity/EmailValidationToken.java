package com.valleon.pakamapp.modules.authentication.entity;

import com.valleon.pakamapp.modules.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
public class EmailValidationToken {

    private static final int EXPIRATION = 60 * 24;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = Customer.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    private Date expiryDate;

    @Column(name = "dateCreated")
    private LocalDateTime dateCreated = LocalDateTime.now();
}

