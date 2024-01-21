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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
            assessment.setDateAdded(time);
            assessment.setDateUpdated(time);
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
            assessmentToUpdate.setDateUpdated(time);
            assessmentRepository.save(assessmentToUpdate);
            return new ResponseMessage<>(time, Codes.SUCCESS, Message.SUCCESS_UPDATE, assessmentToUpdate);
        } catch (Exception e) {
            return new ResponseMessage(time, Codes.NOT_ALLOWED, Message.ERROR_UPDATING_ASSESSMENT, e.getMessage());
        }
    }

    public ResponseMessage deleteAssessment(String assessmentCode) {
        LocalDateTime time = LocalDateTime.now();
        Assessment assessment = assessmentRepository.findByAssessmentCode(assessmentCode)
                .orElseThrow(() -> new ApiRequestException(Message.ERROR_GET));
        assessment.setDateUpdated(time);
        assessment.setExist(false);
        assessmentRepository.save(assessment);
        return new ResponseMessage(time, Codes.SUCCESS, Message.SUCCESS_DELETE_ASSESSMENT, null);
    }

//    public ResponseMessage getCustomerAssessments(String customerCode) {
//        LocalDateTime time = LocalDateTime.now();
//        List<Assessment> assessments = assessmentRepository.findAllByCustomer_CustomerCodeAndExist(customerCode, true)
//                .orElseThrow(() -> new ApiRequestException(Message.ERROR_GET));
//        return new ResponseMessage(time, Codes.SUCCESS, Message.SUCCESS_GET, assessments);
//    }

    public ResponseMessage getCustomerAssessmentsOrderByDateDesc(String customerCode, int pageNo, int pageSize) throws Exception {
        LocalDateTime time = LocalDateTime.now();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        try {
            Page<Assessment> assessmentPage =
                    assessmentRepository.findAllByCustomer_CustomerCodeOrderByDateAddedDesc(customerCode, pageable);
            List<Assessment> assessments = assessmentPage.getContent().stream()
                    .filter(assessment -> assessment.isExist())
                    .collect(Collectors.toCollection(LinkedList::new));
            return new ResponseMessage(
                    time,
                    Codes.SUCCESS,
                    Message.SUCCESS_GET,
                    assessments,
                    assessmentPage.getNumber() + 1,
                    assessmentPage.getSize(),
                    assessmentPage.hasPrevious() ? assessmentPage.getNumber() : null,
                    assessmentPage.hasNext() ? assessmentPage.getNumber() + 2 : null,
                    assessments.size(), assessmentPage.getTotalPages(),
                    assessmentPage.hasPrevious(),
                    assessmentPage.hasNext(),
                    assessmentPage.isLast());
        } catch (Exception e) {
            throw new Exception(Message.ERROR_GET + e.getMessage());
        }
    }

    public ResponseMessage getSingleAssessment(String assessmentCode) {
        LocalDateTime time = LocalDateTime.now();
        Assessment assessment = assessmentRepository.findByAssessmentCode(assessmentCode)
                .orElseThrow(() -> new ApiRequestException(Message.ERROR_GET));
        return new ResponseMessage(time, Codes.SUCCESS, Message.SUCCESS_GET, assessment);
    }


}
