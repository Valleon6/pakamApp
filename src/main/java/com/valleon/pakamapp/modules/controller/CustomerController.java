package com.valleon.pakamapp.modules.controller;

import com.valleon.pakamapp.modules.assessment.service.AssessmentService;
import com.valleon.pakamapp.modules.customer.entity.Customer;
import com.valleon.pakamapp.modules.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private CustomerService customerService;

    private AssessmentService assessmentService;



}
