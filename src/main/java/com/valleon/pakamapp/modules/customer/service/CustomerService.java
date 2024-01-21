package com.valleon.pakamapp.modules.customer.service;

import com.valleon.pakamapp.exception.ApiRequestException;
import com.valleon.pakamapp.exception.Codes;
import com.valleon.pakamapp.exception.Message;
import com.valleon.pakamapp.modules.assessment.repository.AssessmentRepository;
import com.valleon.pakamapp.modules.customer.entity.Customer;
import com.valleon.pakamapp.modules.customer.repository.CustomerRepository;
import com.valleon.pakamapp.modules.payload.CustomerDTO;
import com.valleon.pakamapp.modules.payload.ResponseMessage;
import com.valleon.pakamapp.utils.CheckValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final AssessmentRepository assessmentRepository;

    public ResponseMessage getCustomer(String customerCode) {
        LocalDateTime time = LocalDateTime.now();
        Customer customer = customerRepository.findByCustomerCode(customerCode)
                .orElseThrow(() -> new ApiRequestException(Message.CUSTOMER_NOT_FOUND));
  return new ResponseMessage<>(time, Codes.SUCCESS, Message.SUCCESS_GET, customer);
    }


    public ResponseMessage updateCustomer(CustomerDTO newCustomer, String CustomerCode) {
        LocalDateTime time = LocalDateTime.now();
        try {
            Customer customerToUpdate = customerRepository.findByCustomerCode(CustomerCode)
                    .orElseThrow(() -> new ApiRequestException(Message.CUSTOMER_NOT_FOUND));
            CheckValidation.checkEmail(newCustomer.getEmail());
            CheckValidation.checkPhoneNumber(newCustomer.getPhone());
            customerToUpdate.setFirstName(newCustomer.getFirstName());
            customerToUpdate.setLastName(newCustomer.getLastName());
            customerToUpdate.setEmail(newCustomer.getEmail());
            customerToUpdate.setPhone(newCustomer.getPhone());
            customerRepository.save(customerToUpdate);
            return new ResponseMessage(time, Codes.SUCCESS, Message.SUCCESS_UPDATE, customerToUpdate);
        } catch (Exception e) {
            return new ResponseMessage(time, Codes.NOT_ALLOWED, Message.ERROR_UPDATING_CUSTOMER, e.getMessage());
        }
    }
}
