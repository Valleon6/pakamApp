package com.valleon.pakamapp.modules.controller;

import com.valleon.pakamapp.modules.assessment.service.AssessmentService;
import com.valleon.pakamapp.modules.payload.AssessmentDTO;
import com.valleon.pakamapp.modules.payload.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/assessment")
public class AssessmentController {
    private final AssessmentService assessmentService;
//    @GetMapping("/customer")
//    private ResponseMessage getCustomerAssessment(@RequestParam("cco") String customerCode){
//        return assessmentService.getCustomerAssessments(customerCode);
//    }

    @GetMapping("/customer")
    private ResponseMessage getCustomerAssessmentOrderByDate(
            @RequestParam("cco") String customerCode,
            @RequestParam(name = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "15", required = false) int pageSize) throws Exception {
        return assessmentService.getCustomerAssessmentsOrderByDateDesc(customerCode, pageNo, pageSize);
    }

    @GetMapping("/single")
    private ResponseMessage getSingleAssessment(@RequestParam("aco") String assessmentCode) {
        return assessmentService.getSingleAssessment(assessmentCode);
    }

    @PostMapping("/create")
    private ResponseMessage createAssessment(@RequestBody AssessmentDTO assessmentDTO, @RequestParam("cco") String customerCode) {
        return assessmentService.createAssessment(assessmentDTO, customerCode);
    }

    @PutMapping("/update")
    private ResponseMessage updateAssessment(@RequestBody AssessmentDTO assessmentDTO, @RequestParam("aco") String assessmentCode) {
        return assessmentService.updateAssessment(assessmentDTO, assessmentCode);
    }

    @PutMapping("/delete")
    private ResponseMessage deleteAssessment(@RequestParam("aco") String assessmentCode) {
        return assessmentService.deleteAssessment(assessmentCode);
    }

}
