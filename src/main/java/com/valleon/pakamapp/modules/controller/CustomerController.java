package com.valleon.pakamapp.modules.controller;

import com.valleon.pakamapp.modules.customer.service.CustomerService;
import com.valleon.pakamapp.modules.payload.CustomerDTO;
import com.valleon.pakamapp.modules.payload.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/get")
    public ResponseMessage getCustomer(@RequestParam("cco") String customerCode) {
        return customerService.getCustomer(customerCode);
    }

    @PutMapping("/update")
    public ResponseMessage updateCustomer(@RequestBody CustomerDTO customerDTO, @RequestParam("cco") String customerCode) {
        return customerService.updateCustomer(customerDTO, customerCode);
    }

}
