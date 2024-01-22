package com.valleon.pakamapp.modules.payload;

import com.valleon.pakamapp.modules.customer.entity.Customer;
import com.valleon.pakamapp.utils.AppConstants;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO extends Customer {
    @Pattern(regexp = AppConstants.REGEX_EMAIL, message = "invalid email")
    private String email;

    private String customerCode;

    private String firstName;

    private String lastName;

    @Pattern(regexp = AppConstants.REGEX_PHONE_NUMBER, message = "invalid phone number")
    private String phone;

//    private String userName;

}
