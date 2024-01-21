package com.valleon.pakamapp.modules.authentication.repository;

import com.valleon.pakamapp.modules.authentication.entity.EmailValidationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmailTokenRepository extends JpaRepository<EmailValidationToken, Long> {

    @Query("SELECT t FROM EmailValidationToken t WHERE t.token = ?1")
    Optional<EmailValidationToken> findByToken(String token);
}

