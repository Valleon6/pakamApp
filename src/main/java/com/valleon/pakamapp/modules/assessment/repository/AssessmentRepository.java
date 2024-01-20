package com.valleon.pakamapp.modules.assessment.repository;

import com.valleon.pakamapp.modules.assessment.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, String> {

    Optional<List<Assessment>> findAllByCustomer_CustomerCodeAndIsExist(String customerCode, boolean isExist);

    Optional<Assessment> findByAssessmentCode (String assessmentCode);

}
