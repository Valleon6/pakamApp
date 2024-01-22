package com.valleon.pakamapp.modules.assessment.repository;

import com.valleon.pakamapp.modules.assessment.entity.Assessment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, String> {

//    Optional<List<Assessment>> findAllByCustomer_CustomerCodeAndExist(String customerCode, boolean isExist);

    Page<Assessment> findAllByCustomer_CustomerCodeOrderByDateAddedDesc(String customerCode, Pageable pageable);
    Optional<Assessment> findByAssessmentCode(String assessmentCode);

}
