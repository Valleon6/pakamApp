package com.valleon.pakamapp.modules.assessment.service;

import com.valleon.pakamapp.exception.ApiRequestException;
import com.valleon.pakamapp.exception.Codes;
import com.valleon.pakamapp.exception.Message;
import com.valleon.pakamapp.modules.assessment.entity.Assessment;
import com.valleon.pakamapp.modules.assessment.repository.AssessmentRepository;
import com.valleon.pakamapp.modules.customer.entity.Customer;
import com.valleon.pakamapp.modules.customer.repository.CustomerRepository;
import com.valleon.pakamapp.modules.payload.AssessmentDTO;
import com.valleon.pakamapp.modules.payload.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final CustomerRepository customerRepository;
    private final AssessmentRepository assessmentRepository;

    public ResponseMessage createAssessment(AssessmentDTO assessmentDTO, String customerCode) {
        LocalDateTime time = LocalDateTime.now();
        Assessment assessment = new Assessment();
        Customer customer = customerRepository.findByCustomerCode(customerCode)
                .orElseThrow(() -> new ApiRequestException(Message.CUSTOMER_NOT_FOUND));
        try {
            assessment.setAssessmentCode("ASST-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8));
            assessment.setCustomer(customer);
            assessment.setExist(true);
            assessment.setDescription(assessmentDTO.getDescription());
            assessment.setFullName(assessmentDTO.getFullName());
            assessment.setQuantity(assessmentDTO.getQuantity());
            assessmentRepository.save(assessment);
            return new ResponseMessage<>(time, Codes.SUCCESS, Message.SUCCESS_CREATE_ASSESSMENT, assessment);
        } catch (Exception e) {
            return new ResponseMessage(time, Codes.NOT_ALLOWED, Message.ERROR_CREATING_ASSESSMENT, e.getMessage());
        }
    }

    public ResponseMessage updateAssessment(AssessmentDTO assessmentDto, String assessmentCode) {
        LocalDateTime time = LocalDateTime.now();
        try {
            Assessment assessmentToUpdate = assessmentRepository.findByAssessmentCode(assessmentCode)
                    .orElseThrow(() -> new ApiRequestException(Message.ASSESSMENT_NOT_FOUND));
            assessmentToUpdate.setFullName(assessmentDto.getFullName());
            assessmentToUpdate.setDescription(assessmentDto.getDescription());
            assessmentToUpdate.setQuantity(assessmentDto.getQuantity());
            assessmentRepository.save(assessmentToUpdate);
            return new ResponseMessage<>(time, Codes.SUCCESS, Message.SUCCESS_UPDATE, assessmentToUpdate);
        } catch (Exception e) {
            return new ResponseMessage(time, Codes.NOT_ALLOWED, Message.ERROR_UPDATING_ASSESSMENT, e.getMessage());
        }
    }

    public ResponseMessage deleteAssessment(String assessmentCode){
        LocalDateTime time = LocalDateTime.now();
        Assessment assessment = assessmentRepository.findByAssessmentCode(assessmentCode)
                .orElseThrow(()-> new ApiRequestException(Message.ERROR_GET));
        assessment.setExist(false);
        assessmentRepository.save(assessment);
        return new ResponseMessage(time, Codes.SUCCESS, Message.SUCCESS_DELETE_ASSESSMENT, null);
    }

    public ResponseMessage getUserAssessments(String customerCode) {
        LocalDateTime time = LocalDateTime.now();
        List<Assessment> assessments = assessmentRepository.findAllByCustomer_CustomerCodeAndIsExist(customerCode, true)
                .orElseThrow(() -> new ApiRequestException(Message.ERROR_GET));
        return new ResponseMessage(time, Codes.SUCCESS, Message.SUCCESS_GET, assessments);
    }

    public ResponseMessage getSingleAssessment(String assessmentCode) {
        LocalDateTime time = LocalDateTime.now();
        Assessment assessment = assessmentRepository.findByAssessmentCode(assessmentCode)
                .orElseThrow(() -> new ApiRequestException(Message.ERROR_GET));
        return new ResponseMessage(time, Codes.SUCCESS, Message.SUCCESS_GET, assessment);
    }


}
