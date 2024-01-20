package com.valleon.pakamapp.modules.customer.repository;

import com.valleon.pakamapp.modules.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    Optional<Customer> findByCustomerCode(String customerCode);

    Optional<Customer> findByEmail (String email);
//    Boolean existsByUsername (String username);
    Boolean existsByEmail (String email);
//    Boolean existsByPhone(String phone);
    @Query("SELECT t.customer FROM PasswordResetToken t WHERE t.token = ?1")
    Optional<Customer> getUserByPasswordResetToken(String Token);
    @Query("SELECT t.customer FROM EmailValidationToken t WHERE t.token = ?1")
    Optional<Customer> getUserByEmailValidationToken(String Token);
}
